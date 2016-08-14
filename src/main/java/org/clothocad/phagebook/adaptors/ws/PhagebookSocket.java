/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.clothocad.phagebook.dom.Order.OrderStatus;
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
                    
                    clothoObject.login(loginMap);
                    
                    Map queryMap = new HashMap();
                    queryMap.put("emailId",createStatusMap.get("username"));
                    
                    // This is where it accesses Clotho and updates status
                    List<Person> person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
                    List<String> statuses = person.get(0).getStatuses();
                    if(statuses == null){
                        statuses = new ArrayList<String>();
                    }
                    
                    Status newStatus = new Status();
                    newStatus.setText((String)createStatusMap.get("status"));
                    newStatus.setUserId(person.get(0).getId());
                    //String statusId = ClothoAdapter.createStatus(newStatus, clothoObject);
                    
                    statuses.add(ClothoAdapter.createStatus(newStatus, clothoObject));
                    clothoObject.logout();
                    person.get(0).setStatuses(statuses);
                    ClothoAdapter.setPerson(person.get(0), clothoObject);
                    
                    System.out.println("person status  -------" + person.get(0).getStatuses());
                    // Status updated
                    
                    result.put("data", "Status created successfully.");

                    break;
                }
                case CHANGE_ORDERING_STATUS:
                {
                    Map getOrderMap = new HashMap();
                    getOrderMap = (Map)messageObject.get("data");
                    
//                    Map loginMap = new HashMap();
//                    loginMap.put("username",getOrderMap.get("username"));
//                    loginMap.put("credentials",getOrderMap.get("password"));
//                  
//                    Map loginResult = new HashMap();
//                    loginResult = (Map)clothoObject.login(loginMap);
                    
                    System.out.println("in Phagebook Socket, right before get Order ");
                    Order order = ClothoAdapter.getOrder(getOrderMap.get("id").toString(), clothoObject);
                    
                    System.out.println("Order id received :: " + order.getId());
                    
                    order.setStatus(OrderStatus.valueOf(getOrderMap.get("status").toString()));
                    System.out.println("Changed the order status " + order.getStatus() );
                    ClothoAdapter.setOrder(order, clothoObject);

                    System.out.println("order status -------" + order.getStatus());
                    
                    result.put("data", "Status created successfully.");

                    break;
                }
                case CREATE_PROJECT_STATUS:
                {
                    Map getProjectMap = new HashMap();
                    getProjectMap = (Map)messageObject.get("data");
                    
//                    Map loginMap = new HashMap();
//                    loginMap.put("username",getProjectMap.get("username"));
//                    loginMap.put("credentials",getProjectMap.get("password"));
//                    
//                    Map loginResult = new HashMap();
//                    loginResult = (Map)clothoObject.login(loginMap);
//                    
//                    Map queryMap = new HashMap();
//                    queryMap.put("emailId",getProjectMap.get("username"));
//                    
//                    List<Person> person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
//                    List<String> projectids = person.get(0).getProjects();
                    
                    Project project = ClothoAdapter.getProject(getProjectMap.get("id").toString(), clothoObject);
                    
                    
                    List<String> statuses = new ArrayList<String>();
                    statuses.add((String)getProjectMap.get("status"));
                    
                    project.setUpdates(statuses);
                    
                    ClothoAdapter.setProject(project, clothoObject);

                    System.out.println("project status -------" + project.getUpdates());

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
                    
                    Map queryMap = new HashMap();
                    queryMap.put("emailId",getProjectsMap.get("username"));
                    
                    List<Person> person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
                    
                    List<String> projectids = person.get(0).getProjects();
                    System.out.println("Name  :: " + person.toString());
                    System.out.println("Ids of the Project :: "+ person.get(0).toString());
                    System.out.println("Projects :: " + person.get(0).getProjects());
                    //System.out.println("person.get(0).getProjects() --------- " + person.get(0).getProjects());
                    
                    int numberOfProjects = projectids.size();
                    
                    //System.out.println("number of projects ------------- "+ numberOfProjects);
                    
                    List<Map> allProjects = new ArrayList<Map>();
                    for (int i = 0; i < numberOfProjects; i++)
                    {
                        Map projectsMap = new HashMap();
                        
                        Project project = new Project();
                        project = ClothoAdapter.getProject(projectids.get(i), clothoObject);
                        
                        projectsMap.put("projectId",project.getId());
                        projectsMap.put("projectName",project.getName());
                        allProjects.add(projectsMap);
                    }
                    
                    result.put("data", allProjects);
                    
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
                    
                    
                    Map queryMap = new HashMap();
                    queryMap.put("emailId",getOrdersMap.get("username"));
                    
                    List<Person> person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
                    
                    List<String> orderids = person.get(0).getCreatedOrders();
                    
                    int numberOfOrders = orderids.size();
                    
                    List<Map> allOrders = new ArrayList<Map>();
                    for (int i = 0; i < numberOfOrders; i++)
                    {
                        Map ordersMap = new HashMap();
                        
                        Order order = new Order();
                        order = ClothoAdapter.getOrder(orderids.get(i), clothoObject);
                        
                        ordersMap.put("orderId",order.getId());
                        ordersMap.put("orderName",order.getName());
                        allOrders.add(ordersMap);
                    }
                    
                    result.put("data", allOrders);
                    
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
                    JSONProject.put("dateCreated",project.getDateCreated().toString());
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
                  
                    Map loginResult = new HashMap();
                    loginResult = (Map)clothoObject.login(loginMap);
                    Order order = ClothoAdapter.getOrder(getOrderMap.get("id").toString(), clothoObject);
                    
                    JSONObject JSONOrder = new JSONObject();
                    
                    JSONOrder.put("id",order.getId());
                    JSONOrder.put("name",order.getName());
                    JSONOrder.put("description",order.getDescription());
                    JSONOrder.put("dateCreated",order.getDateCreated().toString());
                    JSONOrder.put("createdById",order.getCreatedById());
                    JSONOrder.put("products",order.getProducts().toString());
                    JSONOrder.put("budget",order.getBudget().toString());
                    JSONOrder.put("maxOrderSize",order.getMaxOrderSize().toString());
                    JSONOrder.put("approvedById",order.getApprovedById());
                    JSONOrder.put("receivedById",order.getReceivedByIds());
                    JSONOrder.put("relatedProjects",order.getRelatedProjectId());
                    JSONOrder.put("status",order.getStatus().toString());
                    
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
