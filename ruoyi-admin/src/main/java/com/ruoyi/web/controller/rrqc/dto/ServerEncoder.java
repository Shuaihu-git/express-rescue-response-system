package com.ruoyi.web.controller.rrqc.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.ruoyi.web.controller.rrqc.vo.LogVo;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


public class ServerEncoder implements Encoder.Text<LogVo> {
 
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        // 这里不重要
    }
 
    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub
        // 这里也不重要
 
    }
 
    /*
    *  encode()方法里的参数和Text<T>里的T一致，如果你是Student，这里就是encode（Student student）
    */
    @Override
    public String encode(LogVo responseMessage) throws EncodeException {
        try {
            JsonMapper jsonMapper = new JsonMapper();
            return jsonMapper.writeValueAsString(responseMessage);
 
        } catch ( JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
