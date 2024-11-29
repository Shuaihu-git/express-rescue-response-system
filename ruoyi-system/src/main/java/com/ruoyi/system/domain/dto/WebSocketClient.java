package com.ruoyi.system.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketClient {
    /*
     * 与某个客户端的连接会话
     */
    private Session session;
    /*
    * 连接的uri
    **/
    private String uri;
}
