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
import org.eclipse.jetty.websocket.WebSocket;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author KatieLewis
 */
public class PhagebookSocket 
        implements WebSocket.OnTextMessage {
    
    private WebSocket.Connection connection;
    
    
    @Override
    public void onMessage(String data) {
        JSONObject object;
        object = new JSONObject(data);
        System.out.println("Channel :: " + (String)object.get("channel"));
        try {
            JSONObject result = new JSONObject();
            JSONObject resultData = new JSONObject();
            
            resultData.put("meh","blah");
            result.put("data", resultData);
            result.put("channel", object.get("channel"));
            result.put("requestId", object.get("requestId"));
            connection.sendMessage(result.toString());
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (IOException ex) {
            Logger.getLogger(PhagebookSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onOpen(Connection connection) {
        this.connection = connection;
        connection.setMaxIdleTime(7 * 24 * 3600000);
        connection.setMaxBinaryMessageSize(999999);
        connection.setMaxTextMessageSize(999999);
        System.out.println("New Connection opened :: " + connection.getProtocol());
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClose(int closeCode, String message) {
        System.out.println("Connection Closed");
    }
    
    
    /*@Override
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
    */
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
                    //Create a map, with key "id" , and the id of the status as the value. pass that to data.
                    result.put("data", "");
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
