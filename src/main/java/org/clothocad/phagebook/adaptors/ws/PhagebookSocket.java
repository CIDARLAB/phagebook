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
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Status;
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
         if (isValidMessage(messageObject)) {
            switch (getChannel((String) messageObject.get("channel"))) {
                case createStatus:
                    Map map = new HashMap();
                    //Cast into hashmap or map
                    map = (HashMap) messageObject.get("data");
                    String username = (String) map.get("username");
                    String password = (String) map.get("password");
                    ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
                    Clotho clothoObject = new Clotho(conn);
                    Map createUserMap = new HashMap();
                    createUserMap.put("username", username);
                    createUserMap.put("password", password);

                    clothoObject.createUser(createUserMap);
                     Map loginMap = new HashMap();
                    loginMap.put("username", username);
                    loginMap.put("credentials", password);
                    
                    String userID = (String) map.get("personID");
                    Person user = ClothoAdaptor.getPerson(userID, clothoObject);
                    Status newStatus = new Status((String) map.get("text"), user);
                    user.addStatus(newStatus);
                    
                    if(map.get("projectID") != null){
                        String projectID = (String) map.get("projectID");
                        Project project = (Project) ClothoAdaptor.getProject(projectID, clothoObject);
                        project.addStatus(newStatus);
                    }
 
                    break;
                case updateOrderStatus:
                    break;
                default:
                    result.put("data", "");
                    break;
            }
        }
        return result;
    }
     private boolean isValidMessage(JSONObject messageObject){
        boolean isValid = true;
        
        return isValid;
    }
    
    private Channel getChannel(String messageChannel){
        Channel channel = null;
        channel = Channel.valueOf(messageChannel);
        return channel;
    }
}
