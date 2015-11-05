/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.ws;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author KatieLewis
 */
public class PhagebookSocket extends WebSocketAdapter {
    @Override
    public void onWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
        System.out.println("Socket connected. Welcome ::" + session.getRemoteAddress());
        
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message: " + message);
        
        JSONObject resultObject = handleIncomingMessage(message);
        System.out.println("RESULT Object " + resultObject.toJSONString());
        this.getSession().getRemote().sendStringByFuture(resultObject.toJSONString());
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason){
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
    
    private JSONObject handleIncomingMessage(String message){
        JSONObject messageObject = new JSONObject();
        JSONParser parser = new JSONParser();
        
        try{
            messageObject = (JSONObject)parser.parse(message);
        }
        catch(ParseException ex){
            Logger.getLogger(PhagebookSocket.class.getName()).log(Level.SEVERE, null,ex);
        }
        System.out.println("INCOMING MESSAGE Object" + messageObject.toString());
        JSONObject result = new JSONObject();
        result.put("channel", (String) messageObject.get("channel"));
        result.put("requestId",messageObject.get("requestId"));
        
        //check channel
        return result;
    }
    // get channel
}
