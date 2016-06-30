/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.adaptors.EmailHandler;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderStatus;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Status;
import org.clothocad.phagebook.security.EmailSaltHasher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author jacob
 */
@Controller
@RequestMapping(value="/Person")
public class PersonController {
    
    @RequestMapping(value="/listApprovedOrdersOfPerson", method=RequestMethod.GET)
    protected void listApprovedOrdersOfPerson(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
         //I AM ASSUMING THAT 
        /* ID is passed in of person
       
        */
        
        Object pUser = params.get("user");
        String user = pUser != null ? (String) pUser: "";
        boolean isValid = false;
        
        if (!user.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            
            Person prashant = ClothoAdapter.getPerson(user, clothoObject);
            boolean exists = true;
            if (prashant.getId().equals("")){
                System.out.println("Person does not exist in list open orders of person");
                exists = false;
            } 
            if (exists){
                List<String> approvedOrders = prashant.getApprovedOrders();
                JSONArray approvedOrdersJSON = new JSONArray();
                for (String approved : approvedOrders ){
                    Order temp = ClothoAdapter.getOrder(approved, clothoObject);
                    JSONObject tempAsJSON = new JSONObject();
                    tempAsJSON.put("name", temp.getName());
                    tempAsJSON.put("description", temp.getDescription());
                    tempAsJSON.put("dateCreated", temp.getDateCreated().toString());
                    tempAsJSON.put("createdById", (ClothoAdapter.getPerson(temp.getCreatedById(), clothoObject)).getEmailId());
                    tempAsJSON.put("products", temp.getProducts());
                    tempAsJSON.put("budget", temp.getBudget());
                    tempAsJSON.put("approvedById", (ClothoAdapter.getPerson(temp.getApprovedById(), clothoObject)).getEmailId());
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = temp.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        JSONObject receivedByJSON = new JSONObject();
                        receivedByJSON.put("user "+ i , (ClothoAdapter.getPerson(receivedBys.get(i), clothoObject)).getEmailId());
                        receivedByIds.put(receivedByJSON);
                    }
                    tempAsJSON.put("receivedByIds", receivedByIds);
                    tempAsJSON.put("relatedProjectId", temp.getRelatedProjectId());
                    tempAsJSON.put("status", temp.getStatus());
                    tempAsJSON.put("affiliatedLabId", (ClothoAdapter.getLab(temp.getAffiliatedLabId(), clothoObject)));
                    approvedOrdersJSON.put(tempAsJSON); // put it in there...
                }
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(approvedOrdersJSON);
                out.flush();
                
                
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "id provided does not exist");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
            }
            
            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing an id to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="/listCreatedOrdersOfPerson", method=RequestMethod.GET)
    protected void listCreatedOrdersOfPerson(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        
         //I AM ASSUMING THAT 
        /* ID is passed in of person
       
        */
        
        Object pUser = params.get("user");
        String user = pUser != null ? (String) pUser: "";
        boolean isValid = false;
        
        if (!user.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            
            Person prashant = ClothoAdapter.getPerson(user, clothoObject);
            boolean exists = true;
            if (prashant.getId().equals("")){
                System.out.println("Person does not exist in list open orders of person");
                exists = false;
            } 
            if (exists){
                List<String> createdOrders = prashant.getCreatedOrders();
                JSONArray createdOrdersJSON = new JSONArray();
                for (String created : createdOrders ){
                    Order temp = ClothoAdapter.getOrder(created, clothoObject);
                    if (temp.getStatus().equals(OrderStatus.INPROGRESS)){
                    JSONObject tempAsJSON = new JSONObject();
                    
                    tempAsJSON.put("name", temp.getName());
                    
                    tempAsJSON.put("description", temp.getDescription());
                    tempAsJSON.put("taxRate", temp.getTaxRate());
                    
                    tempAsJSON.put("dateCreated", temp.getDateCreated().toString());
                    
                    Person createdById = ClothoAdapter.getPerson(temp.getCreatedById(), clothoObject);
                    tempAsJSON.put("createdById", createdById.getEmailId());
                    
                    tempAsJSON.put("creatorName", createdById.getFirstName() + " " + createdById.getLastName());
                  
                    tempAsJSON.put("products", new JSONArray(temp.getProducts())); //cart item ids and quantity
                    
                    
                    tempAsJSON.put("budget", temp.getBudget());
                    String approvedByEmail = "";
                    String approvedByName = "";
                    if (!temp.getApprovedById().equals("") && temp.getApprovedById().equals("Not Set")){
                        Person approver = ClothoAdapter.getPerson(temp.getApprovedById(), clothoObject);
                        approvedByEmail = (approver).getEmailId();
                        approvedByName  = approver.getFirstName() + " " + approver.getLastName();
                    }
                    tempAsJSON.put("approvedById", approvedByEmail);
                    tempAsJSON.put("approvedByName", approvedByName);
                    
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = temp.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        String receivedByID = "";
                        String receivedByFullName= "";
                        JSONObject receiverJSON = new JSONObject();
                        if (!receivedBys.get(i).equals("") && !receivedBys.get(i).equals("Not Set")){
                            Person receiver = ClothoAdapter.getPerson(receivedBys.get(i), clothoObject);
                            
                            receivedByID = receiver.getEmailId();
                            receivedByFullName = receiver.getFirstName() + " " + receiver.getLastName();
                            receiverJSON.put("receiverId", receivedByID);
                            receiverJSON.put("receiverName",receivedByFullName);
                          
                        }
                        receivedByIds.put(receiverJSON);
                    }
                    tempAsJSON.put("limit", temp.getMaxOrderSize());
                    tempAsJSON.put("receivedByIds", receivedByIds);
                    tempAsJSON.put("relatedProjectId", temp.getRelatedProjectId());
                    tempAsJSON.put("relatedProjectName", (ClothoAdapter.getProject(temp.getRelatedProjectId(), clothoObject)).getName());
                    tempAsJSON.put("status", temp.getStatus());
                    //this one should never be not set 
                    tempAsJSON.put("affiliatedLabName", (ClothoAdapter.getLab(temp.getAffiliatedLabId(), clothoObject)).getName());
                    tempAsJSON.put("affiliatedLabId", (temp.getAffiliatedLabId()));
                    tempAsJSON.put("id", temp.getId());
                    createdOrdersJSON.put(tempAsJSON); // put it in there...
                    }
                }
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(createdOrdersJSON);
                out.flush();
                
                
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "id provided does not exist");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
            }
            
            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing an id to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="/listOrdersOfPerson", method=RequestMethod.GET)
    protected void listOrdersOfPerson(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        Object pUser = params.get("user");
        String user = pUser != null ? (String) pUser: "";
        boolean isValid = false;
        
        if (!user.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            
            Person prashant = ClothoAdapter.getPerson(user, clothoObject);
            boolean exists = true;
            if (prashant.getId().equals("")){
                System.out.println("Person does not exist in list open orders of person");
                exists = false;
            } 
            if (exists){
                
                List<String> approvedOrderIds = prashant.getApprovedOrders();
                List<String> deniedOrderIds   = prashant.getDeniedOrders();
                JSONArray allOrders = new JSONArray();
                for (String approved : approvedOrderIds){
                    System.out.println("APPROVED ID: "+approved);
                    Order approvedOrder = ClothoAdapter.getOrder(approved, clothoObject);
                    
                   
                    JSONObject approvedJSON = new JSONObject();
                    approvedJSON.put("name", approvedOrder.getName());
                    approvedJSON.put("dateApproved", approvedOrder.getDateApproved());
                    approvedJSON.put("description", approvedOrder.getDescription());
                    approvedJSON.put("clothoId", approvedOrder.getId());
                    approvedJSON.put("dateCreated", approvedOrder.getDateCreated().toString());
                    Person creator = ClothoAdapter.getPerson(approvedOrder.getCreatedById(), clothoObject);
                    approvedJSON.put("createdById", creator.getEmailId());
                    approvedJSON.put("createdByName", creator.getFirstName() + " " +creator.getLastName());
                    approvedJSON.put("products", approvedOrder.getProducts());
                    approvedJSON.put("orderLimit", approvedOrder.getMaxOrderSize());
                    approvedJSON.put("taxRate", approvedOrder.getTaxRate());
                    approvedJSON.put("budget", approvedOrder.getBudget());
                    System.out.println(approvedOrder.getApprovedById());
                    if (!approvedOrder.getApprovedById().equals("") && !approvedOrder.getApprovedById().equals("Not Set") ){
                        approvedJSON.put("approvedById", (ClothoAdapter.getPerson(approvedOrder.getApprovedById(), clothoObject)).getEmailId());
                    }
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = approvedOrder.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        if (!receivedBys.get(i).equals("") && !receivedBys.get(i).equals("Not Set")){
                        JSONObject receivedByJSON = new JSONObject();
                        receivedByJSON.put( ""+ i , (ClothoAdapter.getPerson(receivedBys.get(i), clothoObject)).getEmailId());
                        receivedByIds.put(receivedByJSON);
                        }
                    }
                    approvedJSON.put("receivedByIds", receivedByIds);
                    approvedJSON.put("relatedProjectName", (ClothoAdapter.getProject(approvedOrder.getRelatedProjectId(), clothoObject)).getName());
                    approvedJSON.put("status", approvedOrder.getStatus());
                    approvedJSON.put("affiliatedLabId", approvedOrder.getAffiliatedLabId());
                    
                    
                    allOrders.put(approvedJSON);
                }
                
                for (String denied : deniedOrderIds){
                    Order deniedOrder = ClothoAdapter.getOrder(denied, clothoObject);
                    
                    JSONObject deniedJSON = new JSONObject();
                    deniedJSON.put("name", deniedOrder.getName());
                    deniedJSON.put("description", deniedOrder.getDescription());
                    deniedJSON.put("clothoId", deniedOrder.getId());
                    deniedJSON.put("dateCreated", deniedOrder.getDateCreated().toString());
                    Person creator = ClothoAdapter.getPerson(deniedOrder.getCreatedById(), clothoObject);
                    deniedJSON.put("createdById", creator.getEmailId());
                    deniedJSON.put("createdByName", creator.getFirstName() + " " +creator.getLastName());
                    deniedJSON.put("products", deniedOrder.getProducts());
                    deniedJSON.put("orderLimit", deniedOrder.getMaxOrderSize());
                    deniedJSON.put("taxRate", deniedOrder.getTaxRate());
                    deniedJSON.put("budget", deniedOrder.getBudget());
                    if (!deniedOrder.getApprovedById().equals("") && !deniedOrder.getApprovedById().equals("Not Set") ){
                        deniedJSON.put("approvedById", (ClothoAdapter.getPerson(deniedOrder.getApprovedById(), clothoObject)).getEmailId());
                    }
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = deniedOrder.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        if (!receivedBys.get(i).equals("") && !receivedBys.get(i).equals("Not Set")){
                        JSONObject receivedByJSON = new JSONObject();
                        receivedByJSON.put( ""+ i , (ClothoAdapter.getPerson(receivedBys.get(i), clothoObject)).getEmailId());
                        receivedByIds.put(receivedByJSON);
                        }
                    }
                    deniedJSON.put("receivedByIds", receivedByIds);
                    deniedJSON.put("relatedProjectName", (ClothoAdapter.getProject(deniedOrder.getRelatedProjectId(), clothoObject)).getName());
                    deniedJSON.put("status", deniedOrder.getStatus());
                    deniedJSON.put("affiliatedLabId", deniedOrder.getAffiliatedLabId());
                    
                    
                    allOrders.put(deniedJSON);
                }
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(allOrders);
                out.flush();
                        
            }else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "id provided does not exist");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
            }
            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing an id to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="/listSubmittedOrdersOfPerson", method=RequestMethod.GET)
    protected void listSubmittedOrdersOfPerson(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        
        Object pUser = params.get("user");
        String user = pUser != null ? (String) pUser: "";
        boolean isValid = false;
        
        if (!user.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            
            Person prashant = ClothoAdapter.getPerson(user, clothoObject);
            boolean exists = true;
            if (prashant.getId().equals("")){
                System.out.println("Person does not exist in list submitted orders of person");
                exists = false;
            } 
            if (exists){
                List<String> submittedOrders = prashant.getSubmittedOrders();
                JSONArray createdOrdersJSON = new JSONArray();
                for (String created : submittedOrders ){
                    Order temp = ClothoAdapter.getOrder(created, clothoObject);
                    JSONObject tempAsJSON = new JSONObject();
                    tempAsJSON.put("name", temp.getName());
                    tempAsJSON.put("description", temp.getDescription());
                    tempAsJSON.put("clothoId", temp.getId());
                    tempAsJSON.put("dateCreated", temp.getDateCreated().toString());
                    Person creator = ClothoAdapter.getPerson(temp.getCreatedById(), clothoObject);
                    tempAsJSON.put("createdById", creator.getEmailId());
                    tempAsJSON.put("createdByName", creator.getFirstName() + " " +creator.getLastName());
                    tempAsJSON.put("products", temp.getProducts());
                    tempAsJSON.put("orderLimit", temp.getMaxOrderSize());
                    tempAsJSON.put("taxRate", temp.getTaxRate());
                    tempAsJSON.put("budget", temp.getBudget());
                    if (!temp.getApprovedById().equals("") && !temp.getApprovedById().equals("Not Set") ){
                        tempAsJSON.put("approvedById", (ClothoAdapter.getPerson(temp.getApprovedById(), clothoObject)).getEmailId());
                    }
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = temp.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        if (!receivedBys.get(i).equals("") && !receivedBys.get(i).equals("Not Set")){
                        JSONObject receivedByJSON = new JSONObject();
                        receivedByJSON.put( ""+ i , (ClothoAdapter.getPerson(receivedBys.get(i), clothoObject)).getEmailId());
                        receivedByIds.put(receivedByJSON);
                        }
                    }
                    tempAsJSON.put("receivedByIds", receivedByIds);
                    tempAsJSON.put("relatedProjectName", (ClothoAdapter.getProject(temp.getRelatedProjectId(), clothoObject)).getName());
                    tempAsJSON.put("status", temp.getStatus());
                    tempAsJSON.put("affiliatedLabId", temp.getAffiliatedLabId());
                    createdOrdersJSON.put(tempAsJSON); // put it in there...
                }
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(createdOrdersJSON);
                out.flush();
                
                
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "id provided does not exist");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
            }
            
            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing an id to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="/createPerson", method=RequestMethod.POST)
    protected void createPerson(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String firstName     = params.get("firstName");
        String lastName      = params.get("lastName");
        String password      = params.get("password");
        String emailId       = params.get("emailId");
        
        
        Object pInstitutionId = params.get("institution");
        String institutionId = pInstitutionId != null ? (String) pInstitutionId : "";
        
        System.out.println("Passed in instit ID" + institutionId);
        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId : "";
        Person createdPerson = new Person();
        createdPerson.setFirstName(firstName);
        createdPerson.setLastName(lastName);
        createdPerson.setEmailId(emailId);
        createdPerson.setPassword(password);

        if (!institutionId.equals("")){
            List<String> institutions = createdPerson.getInstitutions();
            institutions.add(institutionId);
            createdPerson.setInstitutions(institutions);
            createdPerson.setInstitution(institutionId);
            System.out.println("I GOT TO HERE WITH ID " + institutionId);
        }
        
        if (!labId.equals("")){
            List<String> labs = createdPerson.getLabs();
            labs.add(labId);

            createdPerson.setLabs(labs);
        }

        EmailSaltHasher salty = EmailSaltHasher.getEmailSaltHasher();
        String salt = EmailSaltHasher.csRandomAlphaNumericString();
        createdPerson.setSalt(salt);

        byte[] SaltedHashedEmail = salty.hash(emailId.toCharArray(), salt.getBytes("UTF-8"));

        createdPerson.setSaltedEmailHash(SaltedHashedEmail);
        boolean isUnique = false;
        Map clothoQuery = new HashMap();
        clothoQuery.put("emailId", createdPerson.getEmailId());
        List<Person> people = ClothoAdapter.queryPerson(clothoQuery, clothoObject, ClothoAdapter.QueryMode.EXACT);

        if (people.isEmpty()) {
            isUnique = true;
        }

        if (isUnique) {
            clothoObject.logout();
            System.out.println(createdPerson.getInstitutions() + " THE INSTITUTIONS");
            ClothoAdapter.createPerson(createdPerson, clothoObject);

            EmailHandler emailer = EmailHandler.getEmailHandler();

            String link = Args.phagebookBaseURL + "/html/validateEmail.html?emailId=" + createdPerson.getEmailId() + "&salt=" + createdPerson.getSalt() ;
            System.out.println(link);
            
            emailer.sendEmailVerification(createdPerson, link);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/JSON");
            PrintWriter out = response.getWriter();

            JSONObject obj = new JSONObject();
            obj.put("clothoId", createdPerson.getId());
            obj.put("emailId", createdPerson.getEmailId());
            out.print(obj);
            out.flush();
            out.close();

        } else {
            System.out.println("User is not unique in Clotho");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.setContentType("application/JSON");
            PrintWriter out = response.getWriter();
            JSONObject obj = new JSONObject();
            obj.put("message", "Person Already Exists");
            out.print(obj);
            out.flush();
            out.close();
        }
        conn.closeConnection();

        

    }
    
    @RequestMapping(value="/getPersonByIdGET", method=RequestMethod.GET)
    protected void getPersonByIdGET(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("reached doGet");

        String userId = (String) params.get("userId");
        System.out.println(userId);
        boolean isValid = false;
        if (userId != null && userId != "") {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "test" + System.currentTimeMillis();
            createUserMap.put("username", username);
            createUserMap.put("password", "password");
            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");
            clothoObject.login(loginMap);
            //

            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            Institution institute = ClothoAdapter.getInstitution(retrieve.getInstitution(), clothoObject);
            JSONObject retrievedAsJSON = new JSONObject();
            retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
            System.out.println("passed sucks");
            //get position? role?? we will look into this
            retrievedAsJSON.put("firstName", retrieve.getFirstName());
            retrievedAsJSON.put("lastName", retrieve.getLastName());
            retrievedAsJSON.put("loggedUserId", retrieve.getId());
            retrievedAsJSON.put("institution", institute.getName());
            retrievedAsJSON.put("department", retrieve.getDepartment());
            retrievedAsJSON.put("title", retrieve.getTitle());
            retrievedAsJSON.put("email", retrieve.getEmailId());
            retrievedAsJSON.put("profileDescription", retrieve.getProfileDescription());
            retrievedAsJSON.put("statuses", retrieve.getStatuses());
            String labId = retrieve.getLabs().size() > 0 ? retrieve.getLabs().get(0) : "0";

            retrievedAsJSON.put("lab", ClothoAdapter.getLab(labId, clothoObject));
            System.out.println("this is our JSON obj");
            System.out.println(retrievedAsJSON);

            JSONObject statusList = new JSONObject();
            if (retrieve.getStatuses() != null) {
                for (String status : retrieve.getStatuses()) {
                    Status stat = ClothoAdapter.getStatus(status, clothoObject);

                    statusList.put("text", stat.getText());
                    statusList.put("date", stat.getCreated().toString());
                }
            }

            JSONObject publicationList = new JSONObject();
            if (retrieve.getPublications() != null) {

                for (String publication : retrieve.getPublications()) {
                    Publication pub = ClothoAdapter.getPublication(publication, clothoObject);
                    publicationList.put("id", pub.getId());
                }
            }
            /*
            JSONObject labList = new JSONObject();
            if (retrieve.getLabs() != null) {
                for (String lab : retrieve.getLabs()) {
                    Institution inst = ClothoAdapter.getInstitution(lab, clothoObject);
                    labList.put("name", inst.getName());
                    Set<PersonRole> rolesAtInstitution = retrieve.getRole(lab);
                    JSONObject positions = new JSONObject();
                    Iterator<PersonRole> it = rolesAtInstitution.iterator();
                    while (it.hasNext()) {
                        positions.put(inst.getName(), it.next());
                    }
                    labList.put("roles", positions);
                }
            }
             */
            retrievedAsJSON.put("statusList", statusList);
            retrievedAsJSON.put("publicationList", publicationList);
            //retrievedAsJSON.put("labList", labList);
            System.out.println("looking at retrieved");
            System.out.println(retrievedAsJSON);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(retrievedAsJSON);
            out.flush();
            out.close();
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @RequestMapping(value="/getPersonByIdPOST", method=RequestMethod.POST)
    protected void getPersonByIdPOST(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        //
        

        System.out.println("reached doPost");
        /* Map myMap = params.getMap();

        Iterator iterator = myMap.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            System.out.println(myMap.get(key).toString());
            System.out.println(key);
        }*/

        Object pUserId = params.get("clothoId");
        String userId = pUserId != null ? (String) pUserId : "";

        Object pNewFirstName = params.get("editFirstName");
        String newFirstName = pNewFirstName != null ? (String) pNewFirstName : "";

        Object pNewLastName = params.get("editLastName");
        String newLastName = pNewLastName != null ? (String) pNewLastName : "";

        /*Object pNewInstitution = params.get("editInstitution");
        String newInstitution = pNewInstitution != null ? (String) pNewInstitution: "";
         */
        Object pNewDepartment = params.get("editDepartment");
        String newDepartment = pNewDepartment != null ? (String) pNewDepartment : "";

        Object pNewTitle = params.get("editTitle");
        String newTitle = pNewTitle != null ? (String) pNewTitle : "";
/*
        Object pNewLab = params.get("editLab");
        String newLab = pNewLab != null ? (String) pNewLab : "";

        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId : "";*/

        Object pProfileDescription = params.get("editProfileDescription");
        String newProfileDescription = pProfileDescription != null ? (String) pProfileDescription : "";

        System.out.println(userId + " " + newFirstName + newLastName + " " + newDepartment + " " + newTitle + " " + newProfileDescription);
        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //

            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            if (!retrieve.getId().equals("")) {
                if (!newFirstName.equals("")) {
                    retrieve.setFirstName(newFirstName);
                }

                if (!newLastName.equals("")) {
                    retrieve.setLastName(newLastName);
                }

                /*if (newEmail != NULL && !"".equals(newEmail)){
                
            }
            
            if (newPassword != NULL && !"".equals(newPassword)){
                
            }*/
 /* if (newInstitution != NULL && !"".equals(newInstitution)){
                List<String> institutions = retrieve.getInstitutions();
                institutions.add(institutionId);
                retrieve.setInstitutions(institutions);
                editPerson = true;
            }*/
                if (!newDepartment.equals("")) {
                    retrieve.setDepartment(newDepartment);
                }

                if (!newTitle.equals("")) {
                    retrieve.setTitle(newTitle);
                }
                System.out.println("new profile description");
                System.out.println(newProfileDescription);
                if (!newProfileDescription.equals("")) {
                    System.out.println("we are inside set profile desc");
                    retrieve.setProfileDescription(newProfileDescription);
                }

                /*  if (newLab != NULL && !"".equals(newLab)) {
                List<String> labs = retrieve.getLabs();
                labs.add(labId);
                retrieve.setLabs(labs);
                editPerson = true;
            }*/
                clothoObject.logout();
                ClothoAdapter.setPerson(retrieve, clothoObject);

            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }

    }
    
    @RequestMapping(value="/loadUserStatuses", method=RequestMethod.GET)
    protected void loadUserStatuses(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        System.out.println("reached doGet inside loadUserStatuses");

        String clothoId = (String) params.get("clothoId");
        
        boolean isValid = false;
        if (clothoId != null && clothoId != "") {
            isValid = true;
        }
        
        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "test" + System.currentTimeMillis();
            createUserMap.put("username", username);
            createUserMap.put("password", "password");
            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");
            clothoObject.login(loginMap);
            //          
            
            Person retrieve = ClothoAdapter.getPerson(clothoId, clothoObject);
            
            JSONObject statusesAsJSON = new JSONObject();
        
            statusesAsJSON.put("statusIdJSON", retrieve.getStatuses());
            JSONArray statuses = new JSONArray();
            for ( String statId : retrieve.getStatuses()){
                Status tempStat = ClothoAdapter.getStatus(statId, clothoObject);
                
                JSONObject statJSON = new JSONObject();
                
                statJSON.put("statusText", tempStat.getText());
                statJSON.put("dateCreated", tempStat.getCreated().toString());
                
                statuses.put(statJSON);
            }
            
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(statuses);
            out.flush();
            out.close();
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
    }
    
    @RequestMapping(value="/loginUser", method=RequestMethod.POST)
    public void loginUser(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws Exception {
        //
        
        //changed gettting params
        String email = params.get("email");
        String password = params.get("password");
                
        boolean isValidRequest = false;
        if((!email.isEmpty()) && (!password.isEmpty())){
            isValidRequest = true;
        }
        
        
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        if(isValidRequest){
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map loginMap = new HashMap();
            loginMap.put("username", email);
            loginMap.put("credentials", password);
            clothoObject.logout();
            //should have been successful its null if not successful.
            Object NULL = null;
            
            
            boolean isLoggedIn = false;
            
            if(clothoObject.login(loginMap).equals(NULL)){
                System.out.println("Null OBJECT!!");
                isLoggedIn = false;
            }
            else{
                System.out.println("Reached here..");
                isLoggedIn = true;
            }
           
            System.out.println("abcd");
            
            
            Map clothoQuery = new HashMap();
            clothoQuery.put("emailId", email);
            Person loggedInPerson = null;
            if (isLoggedIn){
                System.out.println("is logged in");
                loggedInPerson = ClothoAdapter.queryPerson(clothoQuery, clothoObject, ClothoAdapter.QueryMode.EXACT).get(0);
            }

            
            if ( isLoggedIn && !loggedInPerson.equals(NULL))
            {
                if (loggedInPerson.isActivated())
                {

                    //return success, this means its a valid request
                    //response.setStatus(HttpServletResponse.SC_OK);

                    String idVal = (String) loggedInPerson.getId();
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("clothoId", idVal);
                    responseJSON.put("emailId", loggedInPerson.getEmailId());
                    responseJSON.put("activated", "true");
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(responseJSON);
                    out.flush();
                    out.close();
                }
                else
                {
                    //person is not activated in need to go to the email verification page
                    String idVal = (String) loggedInPerson.getId();
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("clothoId", idVal);
                    responseJSON.put("emailId", loggedInPerson.getEmailId());
                    responseJSON.put("activated", "false");
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(responseJSON);
                    out.flush();
                    out.close();
                    
                }
            }else {
                //user did not make clotho return a login response...
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                JSONObject obj = new JSONObject();
                obj.put("message", "No user found with those credenteials");
                out.print(obj);
                out.flush();
                out.close();
            
            }
            conn.closeConnection();
        }
    }
    
    @RequestMapping(value="/createStatus", method=RequestMethod.POST)
    protected void createStatus(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        

        System.out.println("inside create status post");

        Object pUserId = params.get("clothoId");
        String userId = pUserId != null ? (String) pUserId : "";

        Object pNewStatus = params.get("status");
        String newStatus = pNewStatus != null ? (String) pNewStatus : "";

        System.out.println(newStatus);

        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!userId.equals("") && !newStatus.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //

            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            if (!retrieve.getId().equals("")) {
                
                if (!newStatus.equals("")) {
                    Status newStatusThing = new Status();
                    
                    newStatusThing.setText(newStatus);
                    newStatusThing.setUserId(retrieve.getId());
                    ClothoAdapter.createStatus(newStatusThing, clothoObject);
                    
                    retrieve.addStatus(newStatusThing);
                }
                
                clothoObject.logout();
                ClothoAdapter.setPerson(retrieve, clothoObject);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_CREATED);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "status added");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
                out.close();
                
                conn.closeConnection();
            }
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            out.close();
        }

    }
    
    @RequestMapping(value="/queryFirstLastName", method=RequestMethod.GET)
    protected void queryFirstLastName(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        Object pFirstName = params.get("firstName");
        String firstName = pFirstName != null ? (String) pFirstName: "";
        
        Object pLastName = params.get("lastName");
        String lastName = pLastName != null ? (String) pLastName: "";
        
        boolean isValid = false;
        if (!firstName.equals("") || !lastName.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            Map query = new HashMap();
            if (!firstName.equals("")){
                query.put("firstName", firstName); // the value for which we are querying.
            }
            if (!lastName.equals("")){
                query.put("lastName", lastName); // the key of the object we are querying
            }       
            
           
            List<Person> people = ClothoAdapter.queryPerson(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
            JSONArray peopleJSONArray = new JSONArray();
            
            for (Person retrieve : people){
                JSONObject retrievedAsJSON = new JSONObject();
                retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
                //get position? role?? we will look into this
                retrievedAsJSON.put("firstName", retrieve.getFirstName());
                retrievedAsJSON.put("lastName", retrieve.getLastName());
                retrievedAsJSON.put("clothoId", retrieve.getId());

                String firstInstitutionId = (retrieve.getInstitutions().size() > 0) ? retrieve.getInstitutions().get(0): "None" ;
                    if (!firstInstitutionId.equals("None")){
                        retrievedAsJSON.put("institutionName", ClothoAdapter.getInstitution(firstInstitutionId, clothoObject) .getName());
                    }
                    
                    String firstLabId = (retrieve.getLabs().size() > 0) ? retrieve.getLabs().get(0): "None";
                    if (!firstLabId.equals("None")) {
                        retrievedAsJSON.put("labName", ClothoAdapter.getLab(firstLabId, clothoObject).getName());
                    }

                
                
                //retrievedAsJSON.put("labList", labList);
                peopleJSONArray.put(retrievedAsJSON);
            }
            
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            
            PrintWriter out = response.getWriter();
            out.print(peopleJSONArray);
            out.flush();
            conn.closeConnection();
        }
        else
        {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "need parameters to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
        
    }     
}
