package com.mauriciodelira;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Maurício de Lira
 */
@ServerEndpoint("/messages")
public class Messages {
    
    private String invert(String message) {
        StringBuilder sb = new StringBuilder();
        for(int i = message.length()-1; i >= 0; i--) {
            sb.append(message.charAt(i));
        }
        return sb.toString();
    }
    
    @OnMessage
    public String onMessage(String message) {
        return invert(message);
    }
    
}
