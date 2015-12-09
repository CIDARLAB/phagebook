/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.model.Person;
import org.clothocad.model.Person.PersonRole;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Status;
import org.eclipse.jetty.websocket.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
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
        try {
            connection.sendMessage(handleIncomingMessage(data).toString());
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
    private JSONObject handleIncomingMessage(String message) {
        JSONObject messageObject = new JSONObject();
        JSONParser parser = new JSONParser();

        try {
            messageObject = (JSONObject) parser.parse(message);
        } catch (ParseException ex) {
            Logger.getLogger(PhagebookSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("INCOMING MESSAGE Object" + messageObject.toString());
        JSONObject result = new JSONObject();
        result.put("channel", (String) messageObject.get("channel"));
        result.put("requestId", messageObject.get("requestId"));
        if (isValidMessage(messageObject)) {
            switch (getChannel((String) messageObject.get("channel"))) {
                case createStatus:
                    Map map = new HashMap();
                    map = (HashMap) messageObject.get("data");
                    System.out.println(map);
                    ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
                    Clotho clothoObject = new Clotho(conn);

                    //Get person id from data object -- need to use it somewhere
                    String userID = (String) map.get("personID");
                    System.out.println(userID);
                    //Person user = new Person();
                    //user = (Person)clothoObject.get(userID);
                    //user = (Person)clothoObject.get(userID);
                    Map personMap1 = new HashMap();
                        //Person newPerson = new Person();
                    personMap1 = (Map)clothoObject.get(userID);
                    //Create new status with text in data object
                    String statusText = (String)map.get("text");
                    System.out.println(statusText);
                    System.out.println("personMap1:: " + personMap1);
                    //Person user = (Person) personMap1;
                    Person user = ClothoAdaptor.mapToPerson(personMap1, clothoObject);
                    System.out.println(user);
                    Status newStatus = new Status(statusText, user);
                    System.out.println("made new status object");
                    //Add new status to the user
                    //ClothoAdaptor.createPerson(user, clothoObject);
                    String username = user.getEmailId();
                    String password = user.getPassword();
                    Map loginMap = new HashMap();
                    loginMap.put("username", username);
                    loginMap.put("credentials", password);
                    clothoObject.login(loginMap);
                    String statusId = ClothoAdaptor.createStatus(newStatus, clothoObject);
                    System.out.println("Status created");
                    newStatus.setId(statusId);
                    user.addStatus(newStatus);
                    clothoObject.logout();
                    ClothoAdaptor.setPerson(user, clothoObject);
                    
//                  //Do I need to recreate the user once I add the status?
                    //Check if data json contains a project ID (to add new status to)
                    if (map.containsKey("projectID")) {
                        String projectID = (String) map.get("projectID");
                        Project projectFromClotho = ClothoAdaptor.getProject(projectID, clothoObject);
                        projectFromClotho.addStatus(newStatus);
                        //why do I need to recreate projcet when I add the new status? Do I need to recreate everything once it is updated?
                        ClothoAdaptor.createProject(projectFromClotho, clothoObject);
                    }
                    //Create a map, with key "id" , and the id of the status as the value. pass that to data.
                    JSONObject returnObj = new JSONObject();
                    returnObj.put("id", newStatus.getId());
                    result.put("data", returnObj);
                    break;
                case updateOrderStatus:
                    break;
                case login:
                    Map dataMap = new HashMap();
                    //Get data from message
                    dataMap = (HashMap) messageObject.get("data");
                    System.out.println("dataMap :: " + dataMap);
                    //Get username and password from data map
                    String username1 = (String) dataMap.get("username");
                    String password1 = (String) dataMap.get("password");
                    
                    System.out.println(username1);
                    System.out.println(password1);
                    //Start new Clotho connection and create Clotho object
                    ClothoConnection connect = new ClothoConnection(Args.clothoLocation);
                    Clotho clothoObject1 = new Clotho(connect);
                    //Create a login map with username and password
                    Map loginMap1 = new HashMap();
                    loginMap1.put("username", username1);
                    loginMap1.put("credentials", password1);
                    //Call clotho login function and pass login map. Set result to a new hashmap
                    Map loginResult = new HashMap();
                    loginResult = (Map) clothoObject1.login(loginMap1);
                    System.out.println("Before if");
                    //If loginResult has id key, login was successful
                    if (loginResult.containsKey("id")) {
                        System.out.println("Inside If");
                        //Call Clotho.get to get the person obj
                        String id = (String) loginResult.get("id");
                        //Return the person object
                        JSONObject resultObject = new JSONObject();
                        Map personMap = new HashMap();
                        //Person newPerson = new Person();
                        personMap = (Map)clothoObject1.get(id);
                        
                        //newPerson= (Person)clothoObject1.get(id);
                        
                        System.out.println("Person Map :: " + personMap.toString());
                        
                        resultObject.put("personObject", personMap);
                        System.out.println("back in Phagebook Socket");
                        result.put("data", resultObject);
                        System.out.println("Reached here. End of login API block.");
                    } //If login was successful, send error message
                    else {
                        JSONObject resultObject = new JSONObject();
                        resultObject.put("message", "Login failed");
                        result.put("data", resultObject);
                    }
                    break;
                default:
                    result.put("data", "");
                    break;
            }
        }
        return result;
    }

    private boolean isValidMessage(JSONObject messageObject) {
        boolean isValid = true;

        return isValid;
    }

    private Channel getChannel(String messageChannel) {
        Channel channel = null;
        channel = Channel.valueOf(messageChannel);
        return channel;
    }

}
