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
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.model.Person;
import org.clothocad.model.Person.PersonRole;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderStatus;
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
    }

    @Override
    public void onClose(int closeCode, String message) {
        System.out.println("Connection Closed");
    }

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
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            switch (getChannel((String) messageObject.get("channel"))) {
                case CREATE_STATUS:
                {
                    Map createStatusMap = new HashMap();
                    createStatusMap = (Map)messageObject.get("data");
                    
                    Map loginMap = new HashMap();
                    loginMap.put("username",createStatusMap.get("username"));
                    loginMap.put("credentials",createStatusMap.get("password"));
                    
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    
                    Person person = ClothoAdapter.getPerson(loginResult.get("id").toString(), clothoObject);
                  
                    person.getStatuses().add(new Status(createStatusMap.get("status").toString(),person));
                    ClothoAdapter.setPerson(person, clothoObject);
   
                    result.put("data", "Status created successfully.");

                    break;
                }
                case CHANGE_ORDERING_STATUS:
                {
                    Map orderStatusMap = new HashMap();
                    orderStatusMap = (Map)messageObject.get("data");
                    
                    Map loginMap = new HashMap();
                    loginMap.put("username",orderStatusMap.get("username"));
                    loginMap.put("credentials",orderStatusMap.get("password"));
                    
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    
                    Order order = ClothoAdapter.getOrder(loginResult.get("id").toString(), clothoObject);
                    
                    order.setStatus(OrderStatus.valueOf(orderStatusMap.get("status").toString()));
                    
                    ClothoAdapter.setOrder(order, clothoObject);

                    result.put("data", "Status created successfully.");

                    break;
                }
                 case CHANGE_PROJECT_STATUS:
                {
                    Map projectStatusMap = new HashMap();
                    projectStatusMap = (Map)messageObject.get("data");
                    
                    Map loginMap = new HashMap();
                    loginMap.put("username",projectStatusMap.get("username"));
                    loginMap.put("credentials",projectStatusMap.get("password"));
                    
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    
                    Project project = ClothoAdapter.getProject(loginResult.get("id").toString(), clothoObject);
                    
                    Person person = ClothoAdapter.getPerson(loginResult.get("id").toString(), clothoObject);
                    
                    project.getUpdates().add(new Status(projectStatusMap.get("status").toString(),person));
                    
                    ClothoAdapter.setProject(project, clothoObject);

                    result.put("data", "Status created successfully.");

                    break;
                }
                default:
                    result.put("data", "Error...");
                    break;
            }
            conn.closeConnection();
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
