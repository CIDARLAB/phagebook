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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Status;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/")
public class RestAPI {

    @RequestMapping(value = "createStatus/", method = RequestMethod.POST)
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
        List<Person> person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        List<String> statuses = person.get(0).getStatuses();
        if (statuses == null) {
            statuses = new ArrayList<String>();
        }

        Status newStatus = new Status();
        newStatus.setText(status);
        newStatus.setUserId(person.get(0).getId());
        //String statusId = ClothoAdapter.createStatus(newStatus, clothoObject);

        statuses.add(ClothoAdapter.createStatus(newStatus, clothoObject));
        clothoObject.logout();
        person.get(0).setStatuses(statuses);
        ClothoAdapter.setPerson(person.get(0), clothoObject);

        System.out.println("person status  -------" + person.get(0).getStatuses());
        // Status updated

        return "success";
    }

    @RequestMapping(value = "changeOrderingStatus/", method = RequestMethod.POST)
    public String changeOrderingStatus(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String id = data.getId();

        System.out.println("in Phagebook Socket, right before get Order ");
        Order order = ClothoAdapter.getOrder(id, clothoObject);

        System.out.println("Order id received :: " + order.getId());

        order.setStatus(Order.OrderStatus.valueOf(status));
        System.out.println("Changed the order status " + order.getStatus());
        ClothoAdapter.setOrder(order, clothoObject);

        System.out.println("order status -------" + order.getStatus());

        return "Success";
    }

    @RequestMapping(value = "createProjectStatus/", method = RequestMethod.POST)
    public String createProjectStatus(@RequestBody Data data) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = data.getUsername();
        String password = data.getPassword();
        String status = data.getStatus();
        String id = data.getId();
        Project project = ClothoAdapter.getProject(id.toString(), clothoObject);

        List<String> statuses = new ArrayList<String>();
        statuses.add((String) status);

        project.setUpdates(statuses);

        ClothoAdapter.setProject(project, clothoObject);

        System.out.println("project status -------" + project.getUpdates());

        return "Status created successfully.";

    }

    @RequestMapping(value = "getProjects/", method = RequestMethod.POST)
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

        List<Person> person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);

        List<String> projectids = person.get(0).getProjects();
        System.out.println("Name  :: " + person.toString());
        System.out.println("Ids of the Project :: " + person.get(0).toString());
        System.out.println("Projects :: " + person.get(0).getProjects());
        //System.out.println("person.get(0).getProjects() --------- " + person.get(0).getProjects());

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

    @RequestMapping(value = "getOrders/", method = RequestMethod.POST)
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

        List<Person> person = ClothoAdapter.queryPerson(queryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);

        List<String> orderids = person.get(0).getCreatedOrders();

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

        return result;
    }

    @RequestMapping(value = "getProject/", method = RequestMethod.POST)
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

            return result;
        }
    }

    @RequestMapping(value = "getOrder/", method = RequestMethod.POST)
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

            return result;
        }
    }
}
