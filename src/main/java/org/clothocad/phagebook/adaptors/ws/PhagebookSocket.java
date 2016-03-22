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
import java.util.List;
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
                    System.out.println("LOGIN RESULT "  + loginResult.toString());
                    Person person = ClothoAdapter.getPerson(loginResult.get("id").toString(), clothoObject);
                    Status stat = new Status(createStatusMap.get("status").toString(), person);
                    ClothoAdapter.createStatus(stat, clothoObject);
                    person.getStatuses().add(stat.getId());
                    
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
                case CREATE_PROJECT_STATUS:
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
                    
                    Status stat = new Status(projectStatusMap.get("status").toString(),person);
                    
                    ClothoAdapter.createStatus(stat, clothoObject);
                    
                    project.getUpdates().add(stat.getId());
                    
                    ClothoAdapter.setProject(project, clothoObject);

                    result.put("data", "Status created successfully.");

                    break;
                 
                }
                case GET_PROJECTS:
                {
                    Map getProjectsMap = new HashMap();
                    getProjectsMap = (Map)messageObject.get("data");
                    
                    Map loginMap = new HashMap();
                    loginMap.put("username",getProjectsMap.get("username"));
                    loginMap.put("credentials",getProjectsMap.get("password"));
                    
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    
                    Map projectsMap = new HashMap();
                    List<String> projectids = ClothoAdapter.getPerson(loginResult.get("id").toString(), clothoObject).getProjects();
                    int numberOfProjects = projectids.size();
                    
                    for (int i = 0; i < numberOfProjects; i++)
                    {
                        Project project = ClothoAdapter.getProject(projectids.get(i), clothoObject);
                   
                        projectsMap.put("projectId",project.getId());
                        projectsMap.put("projectName",project.getName());
                    }
                    
                    result.put("data", projectsMap);
                    
                    break;
                }
                case GET_ORDERS:
                {
                    Map getOrdersMap = new HashMap();
                    getOrdersMap = (Map)messageObject.get("data");
                    
                    Map loginMap = new HashMap();
                    loginMap.put("username",getOrdersMap.get("username"));
                    loginMap.put("credentials",getOrdersMap.get("password"));
                    
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    
                    Map ordersMap = new HashMap();
                    List<String> orderids = ClothoAdapter.getPerson(loginResult.get("id").toString(), clothoObject).getApprovedOrders();

                    int numberOfOrders = orderids.size();
                    
                    for (int i = 0; i < numberOfOrders; i++)
                    {
                        Order order = ClothoAdapter.getOrder(orderids.get(i), clothoObject);
                   
                        ordersMap.put("projectId",order.getId());
                        ordersMap.put("projectName",order.getName());
                    }
                    
                    result.put("data", ordersMap);
                    
                    break;
                }
                case GET_PROJECT:
                {
                    Map getProjectMap = new HashMap();
                    getProjectMap = (Map)messageObject.get("data");
                    
                    Map loginMap = new HashMap();
                    loginMap.put("username",getProjectMap.get("username"));
                    loginMap.put("credentials",getProjectMap.get("password"));
                    
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    
                    Project project = ClothoAdapter.getProject(getProjectMap.get("id").toString(), clothoObject);
                    
                    JSONObject JSONProject = new JSONObject();
                    
                    JSONProject.put("creatorId",project.getCreatorId());
                    JSONProject.put("leadId",project.getLeadId());
                    JSONProject.put("members",project.getMembers());
                    JSONProject.put("notebooks",project.getNotebooks());
                    JSONProject.put("affiliatedLabs",project.getAffiliatedLabs());
                    JSONProject.put("name",project.getName());
                    JSONProject.put("dateCreated",project.getDateCreated());
                    JSONProject.put("updates",project.getUpdates());
                    JSONProject.put("budget",project.getBudget());
                    JSONProject.put("grantId",project.getGrantId());
                    JSONProject.put("description",project.getDescription());
                    JSONProject.put("id",project.getId());
                    
                    result.put("data", JSONProject);
                    
                    break;
                }
                case GET_ORDER:
                {
                    Map getOrderMap = new HashMap();
                    getOrderMap = (Map)messageObject.get("data");
                    
                    Map loginMap = new HashMap();
                    loginMap.put("username",getOrderMap.get("username"));
                    loginMap.put("credentials",getOrderMap.get("password"));
                    
                    System.out.println("Here at after getOrderMap Result: " + getOrderMap.toString() );
                    
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    System.out.println("Here at after login Result: " + loginResult.toString());
                    System.out.println("Here at after getOrderMap Result: " + getOrderMap.toString() );
                    Order order = ClothoAdapter.getOrder(getOrderMap.get("id").toString(), clothoObject);
                    
                    JSONObject JSONOrder = new JSONObject();
                    
                    JSONOrder.put("id",order.getId());
                    JSONOrder.put("name",order.getName());
                    JSONOrder.put("description",order.getDescription());
                    JSONOrder.put("dateCreated",order.getDateCreated());
                    JSONOrder.put("createdById",order.getCreatedById());
                    JSONOrder.put("products",order.getProducts());
                    JSONOrder.put("budget",order.getBudget());
                    JSONOrder.put("maxOrderSize",order.getMaxOrderSize());
                    JSONOrder.put("approvedById",order.getApprovedById());
                    JSONOrder.put("receivedById",order.getReceivedByIds());
                    JSONOrder.put("relatedProjects",order.getRelatedProjectId());
                    JSONOrder.put("status",order.getStatus());
                    
                    result.put("data", JSONOrder);
                    
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
