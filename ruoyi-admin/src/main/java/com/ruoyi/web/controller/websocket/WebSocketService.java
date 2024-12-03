package com.ruoyi.web.controller.websocket;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.dto.ScanLog;
import com.ruoyi.system.domain.dto.WebSocketClient;
import com.ruoyi.system.service.IScanLogService;
import com.ruoyi.web.controller.rrqc.dto.RRQCMessage;
import com.ruoyi.web.controller.rrqc.dto.ServerEncoder;
import com.ruoyi.web.controller.rrqc.vo.LogVo;
import com.ruoyi.web.controller.rrqc.vo.Weather;
import com.ruoyi.web.controller.rrqc.weather.QueryWeather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xiaohu
 *
 */

@ServerEndpoint(value = "/websocket/{userName}", encoders = {ServerEncoder.class})
@Component
@Slf4j
public class WebSocketService {
    @Autowired
    private IScanLogService scanLogService= SpringUtils.getBean("scanLogService");

    @Autowired
    private QueryWeather queryWeather=SpringUtils.getBean("queryWeather");

    private static ApplicationContext applicationContext;


    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    private static ConcurrentHashMap<String, WebSocketClient> webSocketMap = new ConcurrentHashMap<>();
    private static List<LogVo> messagesSendToAdmin = Collections.synchronizedList(new ArrayList<>());



    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userName
     */
    private String userName;


    /**
     * 客户端与服务端连接成功
     *
     * @param session 与某个客户端的连接会话
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        this.session = session;
        //根据token获取用户名
        this.userName = userName;
        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        webSocketMap.put(userName, client);
        log.info("连接成功！"+session.getRequestURI().toString());
        log.info("连接成功！"+session);
    }

    /**
     * 客户端与服务端连接关闭
     */
    @OnClose
    public void onClose(@PathParam("userName")String userName) {
        webSocketMap.remove(userName);
        log.info("连接关闭！");
    }

    /**
     * 客户端与服务端连接异常
     *
     * @param error 错误
     * @param session 会话
     */
    @OnError
    public void onError(Throwable error, Session session) {
    }

    /**
     * 客户端向服务端发送消息
     *
     * @param message 消息
     * @throws IOException 异常
     */
    @OnMessage
    public void onMsg(Session session, String message) throws IOException, ParseException {
        log.info("消息到达{}",message);
        RRQCMessage rrqcMessage = JSONObject.parseObject(message, RRQCMessage.class);

        if (!message.isEmpty()&&!ObjectUtils.isNull(rrqcMessage)){
            ScanLog scanLog = new ScanLog();
            scanLog.setAddress(rrqcMessage.getAddress());
            scanLog.setContent(rrqcMessage.getContent());
            scanLog.setTime(rrqcMessage.getTime());
            scanLog.setZhuangNum(rrqcMessage.getZhuangNum());
            scanLog.setType(rrqcMessage.getType());
            Weather weather = queryWeather.getWeather();
            if (!ObjectUtils.isNull(weather)){
                if (!ObjectUtils.isNull(weather.getNow())){
                    scanLog.setWeather(weather.getNow().getText());
                }
            }
            if (!ObjectUtils.isNull(rrqcMessage.getLocation())){
                scanLog.setLongitude(rrqcMessage.getLocation().getLongitude());
                scanLog.setLatitude(rrqcMessage.getLocation().getLatitude());
            }
            scanLog.setRemark(rrqcMessage.getRemark());
            scanLogService.insert(scanLog);
            if (webSocketMap.containsKey("admin")){
                sendMessage("admin",message);
            }else {
                messagesSendToAdmin.add(new LogVo(message));
            }

        }

    }

    public static void sendVo(String userName, LogVo vo) {
        try {
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            if (webSocketClient != null) {
                webSocketClient.getSession().getBasicRemote().sendObject(vo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (EncodeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 向指定客户端发送消息
     *
     * @param message 消息
     */
    public static void sendMessage(String userName, String message) {
        try {
            log.info("准备发消息！"+userName);
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            log.info("连接成功"+webSocketMap);
            if (webSocketClient != null) {
                log.info("发送的消息是！"+message);
                webSocketClient.getSession().getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("失败");
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void sendMessageToList(List<String> userIds, String message){
        for (String userId : userIds) {
            sendMessage(userId,message);
        }
    }
    @Scheduled(fixedRate = 1000 * 10)
    public void pushAdminWebSocket() {
        log.info("管理端是否在线{}", mapContainsAdmin());
        log.info("管理端在线成员{}", getAdmins());
        log.info("是否有消息滞留{}",!messagesSendToAdmin.isEmpty());
        if (mapContainsAdmin()&&!messagesSendToAdmin.isEmpty()) {
            log.info("执行滞留消息重发{}",mapContainsAdmin()&&!messagesSendToAdmin.isEmpty());
           messagesSendToAdmin.forEach(e -> sendMessageToList(getAdmins(), e.getData()));
           messagesSendToAdmin.clear();
        }else {
            log.info("不处理消息滞留重发问题");
        }

    }
    public static List<String> getAdmins(){
        Set<String> strings = webSocketMap.keySet();
        List<String> list = new ArrayList<>();
        for (String string : strings) {
            if (string.contains("admin")){
                list.add(string);
            }
        }
        return list;
    }
    public static boolean mapContainsAdmin(){
        Set<String> strings = webSocketMap.keySet();
        for (String string : strings) {
            return string.contains("admin");
        }
        return false;
    }

}

