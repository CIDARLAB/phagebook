/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.RestAPI;

/**
 *
 * @author jacob
 */
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.adaptors.sendEmails;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Status;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/")
public class RestAPI {

    @RequestMapping(value = "createStatus", method = RequestMethod.POST)
    public String createStatus(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();

        System.out.println("username: " + username);
        System.out.println("password: " + password);
        System.out.println("status: " + status);

        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        Map queryMap = new HashMap();
        queryMap.put("emailId", username);

        // This is where it accesses Clotho and updates status
        Person person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT).get(0);
        List<String> statuses = person.getStatuses();
        if (statuses == null) {
            statuses = new ArrayList<String>();
        }

        Status newStatus = new Status();
        newStatus.setText(status);
        newStatus.setUserId(person.getId());
        //String statusId = ClothoAdapter.createStatus(newStatus, clothoObject);

        statuses.add(ClothoAdapter.createStatus(newStatus, clothoObject));
        clothoObject.logout();
        person.setStatuses(statuses);
        ClothoAdapter.setPerson(person, clothoObject);

        System.out.println("person status  -------" + person.getStatuses());
        // Status updated

        conn.closeConnection();

        return "Success";
    }

    @RequestMapping(value = "approveOrder", method = RequestMethod.POST)
    public JSONObject changeOrderingStatus(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String orderID = data.getId();

        Map queryMap = new HashMap();
        queryMap.put("emailId", username);

        String userId = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT).get(0).getId();

        JSONObject responseJSON = new JSONObject();

        boolean isValid = false;
        if (!orderID.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //login
            String usrname = "phagebook";
            String pssword = "backend";
            /*
            
                DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                                   PASSWORD: backend
             */
            Map loginMap = new HashMap();
            loginMap.put("username", usrname);
            loginMap.put("credentials", pssword);
            clothoObject.login(loginMap);

            Order orderToApprove = ClothoAdapter.getOrder(orderID, clothoObject);
            List<String> receivedByList = orderToApprove.getReceivedByIds();
            String finalApprover = "";
            String fAEmailId = "";
            for (String id : receivedByList) {
                if (id.equals(userId)) {
                    finalApprover = id;
                    Person approver = ClothoAdapter.getPerson(id, clothoObject);
                    clothoObject.logout();
                    Map login2 = new HashMap();
                    fAEmailId = approver.getEmailId();
                    login2.put("username", fAEmailId);
                    login2.put("credentials", approver.getPassword());
                    clothoObject.login(login2);
                    List<String> approvedOrder = approver.getApprovedOrders();
                    List<String> submittedOrders = approver.getSubmittedOrders(); // need to add to approved and remove from submitted..
                    approvedOrder.add(orderToApprove.getId());
                    submittedOrders.remove(orderToApprove.getId());
                    clothoObject.logout();
                    ClothoAdapter.setPerson(approver, clothoObject);
                    clothoObject.login(loginMap);
                    orderToApprove.setDateApproved(new Date());

                    orderToApprove.setStatus(Order.OrderStatus.APPROVED);
                    orderToApprove.setApprovedById(finalApprover);
                    ClothoAdapter.setOrder(orderToApprove, clothoObject);

                }

            }

            responseJSON.put("message", "Order has been approved!");
            responseJSON.put("approvedBy", finalApprover);
            responseJSON.put("approvedByEmail", fAEmailId);
            conn.closeConnection();

        } else {

            responseJSON.put("message", "missing parameters for servlet call");
        }
        return responseJSON;
    }

    @RequestMapping(value = "createProjectStatus", method = RequestMethod.POST)
    public String createProjectStatus(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String id = data.getId();

        org.json.JSONObject result = new org.json.JSONObject();

        Map createUserMap = new HashMap();
        String usrname = "username";
        String pssword = "password";

        createUserMap.put("username", usrname);
        createUserMap.put("password", pssword);

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", usrname);
        loginMap.put("credentials", pssword);

        clothoObject.login(loginMap);
        System.err.println("Got a new Update request in addUpdateToProject ");
        // New Update will be a string.
        // declare these here 
        Map queryMap = new HashMap();
        queryMap.put("emailId", username);

        Person person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT).get(0);
        String userID = person.getId();
        String projectID = id;
        String newStatus = status;

        // if there is a status
        if (newStatus.length() != 0) {
            List<String> allUpdates = addUpdateToProjectHelper(userID, projectID, newStatus, clothoObject);
            List<Map<String, String>> listOfUpdates = new ArrayList<Map<String, String>>();
            Person per = ClothoAdapter.getPerson(userID, clothoObject);
//            System.out.println(per.getEmailId());
//            System.out.println(per.getProjects());

            for (String s : allUpdates) {
                Status update = ClothoAdapter.getStatus(s, clothoObject);
                Map u = new HashMap();
                u.put("date", update.getCreated());
                u.put("userId", update.getUserId());
                // get a person's first and last name
                Person p = ClothoAdapter.getPerson(update.getUserId(), clothoObject);
                u.put("userName", p.getFirstName() + " " + p.getLastName());
                u.put("text", update.getText());
                listOfUpdates.add(u);
            }
            result.put("success", 1);
            result.put("updates", listOfUpdates);
        } else {
            System.out.println("Update was too short -- letting the user know!");

            result.put("short", 1);
        }
        clothoObject.logout();
        conn.closeConnection();

        return "Success";
    }

    protected static List<String> addUpdateToProjectHelper(String userID, String projectID,
            String newStatus, Clotho clothoObject) {

        // create a new status object
        Status newUpdate = new Status();
        newUpdate.setText(newStatus);
        newUpdate.setUserId(userID);
        System.out.println(newUpdate);
        System.out.println("About to create a Status in Clotho");
        String statusID = ClothoAdapter.createStatus(newUpdate, clothoObject);

        System.out.println("Status has been created in Clotho and ID is: " + statusID);

        // get the objects associated with the passed in IDS from clotho
        Person editor = ClothoAdapter.getPerson(userID, clothoObject);
        System.out.println("User Id is: ");
        System.out.println(userID);

        Project project = ClothoAdapter.getProject(projectID, clothoObject);
        System.out.println("Project Id is: ");
        System.out.println(projectID);

        String editorName = editor.getFirstName() + " " + editor.getLastName();
        System.out.println(editorName);
        System.out.println(project.getName());

        // get the existing list of project updates and add the id of the  new update
        List<String> projectUpdates = project.getUpdates();
        projectUpdates.add(statusID);

        // update the update lists in the project object
        project.setUpdates(projectUpdates);

        List<String> allUpdates = project.getUpdates();
        // change the project in clotho
        String prId = ClothoAdapter.setProject(project, clothoObject);
        System.out.println("In addProjectUpdate function projectID is: " +prId);

        return allUpdates;
    }

    @RequestMapping(value = "getProjects", method = RequestMethod.POST)
    public JSONObject getProjects(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String id = data.getId();

        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        Map loginResult = new HashMap();
        loginResult = (Map) clothoObject.login(loginMap);

        Map queryMap = new HashMap();
        queryMap.put("emailId", username);

        Person person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT).get(0);

        List<String> projectids = person.getProjects();
        System.out.println("Name  :: " + person.toString());
        System.out.println("Ids of the Project :: " + person.toString());
        System.out.println("Projects :: " + person.getProjects());
        //System.out.println("person.getProjects() --------- " + person.getProjects());

        int numberOfProjects = projectids.size();

        //System.out.println("number of projects ------------- "+ numberOfProjects);
        List<Map> allProjects = new ArrayList<Map>();
        for (int i = 0; i < numberOfProjects; i++) {
            Map projectsMap = new HashMap();

            Project project = new Project();
            project = ClothoAdapter.getProject(projectids.get(i), clothoObject);

            projectsMap.put("projectId", project.getId());
            projectsMap.put("projectName", project.getName());
            allProjects.add(projectsMap);
        }
        clothoObject.logout();
        conn.closeConnection();
        JSONObject result = new JSONObject();
        result.put("data", allProjects);

        return result;
    }

    @RequestMapping(value = "getOrders", method = RequestMethod.POST)
    public JSONObject getOrders(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String id = data.getId();

        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        Map loginResult = new HashMap();
        loginResult = (Map) clothoObject.login(loginMap);

        Map queryMap = new HashMap();
        queryMap.put("emailId", username);

        Person person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT).get(0);

        List<String> orderids = person.getCreatedOrders();

        int numberOfOrders = orderids.size();

        List<Map> allOrders = new ArrayList<Map>();
        for (int i = 0; i < numberOfOrders; i++) {
            Map ordersMap = new HashMap();

            Order order = new Order();
            order = ClothoAdapter.getOrder(orderids.get(i), clothoObject);

            ordersMap.put("orderId", order.getId());
            ordersMap.put("orderName", order.getName());
            allOrders.add(ordersMap);
        }
        JSONObject result = new JSONObject();
        result.put("data", allOrders);

        conn.closeConnection();

        return result;
    }

    @RequestMapping(value = "getProject", method = RequestMethod.POST)
    public JSONObject getProject(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String id = data.getId();
        {
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);

            Map loginResult = new HashMap();
            loginResult = (Map) clothoObject.login(loginMap);

            Project project = ClothoAdapter.getProject(id.toString(), clothoObject);

            JSONObject JSONProject = new JSONObject();

            JSONProject.put("creatorId", project.getCreatorId());
            JSONProject.put("leadId", project.getLeadId());
            JSONProject.put("members", project.getMembers());
            JSONProject.put("notebooks", project.getNotebooks());
            JSONProject.put("affiliatedLabs", project.getAffiliatedLabs());
            JSONProject.put("name", project.getName());
            JSONProject.put("dateCreated", project.getDateCreated().toString());
            JSONProject.put("updates", project.getUpdates());
            JSONProject.put("budget", project.getBudget());
            JSONProject.put("grantId", project.getGrantId());
            JSONProject.put("description", project.getDescription());
            JSONProject.put("id", project.getId());

            JSONObject result = new JSONObject();
            result.put("data", JSONProject);

            conn.closeConnection();

            return result;
        }
    }

    @RequestMapping(value = "getOrder", method = RequestMethod.POST)
    public JSONObject getOrder(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String id = data.getId();
        {
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);

            Map loginResult = new HashMap();
            loginResult = (Map) clothoObject.login(loginMap);
            Order order = ClothoAdapter.getOrder(id.toString(), clothoObject);

            JSONObject JSONOrder = new JSONObject();

            JSONOrder.put("id", order.getId());
            JSONOrder.put("name", order.getName());
            JSONOrder.put("description", order.getDescription());
            JSONOrder.put("dateCreated", order.getDateCreated().toString());
            JSONOrder.put("createdById", order.getCreatedById());
            JSONOrder.put("products", order.getProducts().toString());
            JSONOrder.put("budget", order.getBudget().toString());
            JSONOrder.put("maxOrderSize", order.getMaxOrderSize().toString());
            JSONOrder.put("approvedById", order.getApprovedById());
            JSONOrder.put("receivedById", order.getReceivedByIds());
            JSONOrder.put("relatedProjects", order.getRelatedProjectId());
            JSONOrder.put("status", order.getStatus().toString());

            JSONObject result = new JSONObject();
            result.put("data", JSONOrder);

            conn.closeConnection();

            return JSONOrder;
        }
    }
}
