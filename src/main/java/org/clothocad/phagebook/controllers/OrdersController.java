/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;

import java.io.File;
import java.io.FileWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Map;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.controller.Utilities;
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Order.OrderColumns;
import org.clothocad.phagebook.dom.Order.OrderStatus;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jacob
 */
@Controller
public class OrdersController {

    private final String backendPhagebookUser = Args.defaultPhagebookUsername;
    private final String backendPhagebookPassword = Args.defaultPhagebookPassword;

    @RequestMapping(value = "/addProductsToOrder", method = RequestMethod.POST)
    protected void addProductsToOrder(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        /*Add products to an order...
         *ASSUMPTION 1: I receive a JSONArray with key value pairs <String, int> product id, and quantity
         *ASSUMPTION 2: Along with each object I get a "discount" : double key...
         * I take care of everything here 
         *ASSUMPTION 3: I GET THE LOGGED IN PERSONS'S ID (if I need it) from the cookie
         *ASSUMPTION 4: I GET THE ORDER ID PASSED IN.
         */

        Object pCartItems = params.get("CartItems");
        String cartItems = pCartItems != null ? (String) pCartItems : "";

        Object pUser = params.get("loggedInUserId");
        String user = pUser != null ? (String) pUser : "";

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        boolean isValid = false;

        if (!cartItems.equals("") && !user.equals("") && !orderId.equals("")) {
            isValid = true;
        }

        //should now have something like this parsed.
        /*
         [{"productId": "<ID>" , "quantity": "5", "discount": "100" }, {"productId": "<ID> ,.. etc"}];  
        
        
         */
        if (isValid) {
            //NEED TO LOG INTO CLOTHO... better way TBA

            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //

            //STEP 1, get the order we want to modify...
            //assuming valid order ID.
            Order editableOrder = ClothoAdapter.getOrder(orderId, clothoObject);
            //now we have the order object.
            JSONArray cartItemsJSONArray = new JSONArray(cartItems);
            //we have our JSONArray of products to add with discounts
            List<String> items = editableOrder.getProducts(); //initialize, we want to add not replace
            Date date = new Date();
            for (int i = 0; i < cartItemsJSONArray.length(); i++) {
                //process the information that we have

                JSONObject obj = (JSONObject) cartItemsJSONArray.get(i);
                Product product = ClothoAdapter.getProduct(obj.getString("productId"), clothoObject);
                product.decreaseInventory(obj.getInt("quantity"));

                CartItem item = new CartItem();
                item.setDateCreated(date);
                item.setProductId(obj.getString("productId"));
                item.setDiscount(obj.getDouble("discount"));
                item.setQuantity(obj.getInt("quantity"));

                ClothoAdapter.createCartItem(item, clothoObject);
                ClothoAdapter.setProduct(product, clothoObject);

                items.add(item.getId());

            }
            //now have a CART ITEM OBJECT all with ID's 

            editableOrder.setProducts(items);

            ClothoAdapter.setOrder(editableOrder, clothoObject);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "successfully modified order object");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();

            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters for servlet call");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

    @RequestMapping(value = "/approveOrder", method = RequestMethod.POST)
    public void approveOrder(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";

        boolean isValid = false;
        if (!orderId.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);

            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;

            /*
             DIRECT ASSUMPTION THAT USER: phagebook exists and their 
             PASSWORD: backend
             */
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Order orderToApprove = ClothoAdapter.getOrder(orderId, clothoObject);
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

                    orderToApprove.setStatus(OrderStatus.APPROVED);
                    orderToApprove.setApprovedById(finalApprover);
                    ClothoAdapter.setOrder(orderToApprove, clothoObject);

                }

            }

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Order has been approved!");
            responseJSON.put("approvedBy", finalApprover);
            responseJSON.put("approvedByEmail", fAEmailId);
            conn.closeConnection();
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();

        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters for servlet call");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

    @RequestMapping(value = "/createOrderWithIdArray", method = RequestMethod.POST)
    public void createOrderWithIdArray() {
        System.out.println("createOrderWithIdArray: no functionality");
    }

    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    public void deleteOrder(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pUser = params.get("user");
        String user = pUser != null ? (String) pUser : "";

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        boolean isValid = false;
        if (!user.equals("") && !orderId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Person userP = ClothoAdapter.getPerson(user, clothoObject);

            List<String> userCreatedOrders = userP.getCreatedOrders();

            if (userCreatedOrders.contains(orderId)) {
                userCreatedOrders.remove(orderId);
                userP.setCreatedOrders(userCreatedOrders);

                clothoObject.logout();
                ClothoAdapter.setPerson(userP, clothoObject);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order deleted");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();

            } else {

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order not found for this user");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
            }

            conn.closeConnection();

        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Missing Parameters");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

    @RequestMapping(value = "/denyOrder", method = RequestMethod.POST)
    public void denyOrder(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";

        boolean isValid = false;
        if (!orderId.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            /*
            
             DIRECT ASSUMPTION THAT USER: phagebook exists and their 
             PASSWORD: backend
             */
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Order orderToDeny = ClothoAdapter.getOrder(orderId, clothoObject);
            List<String> receivedByList = orderToDeny.getReceivedByIds();

            for (String receivedById : receivedByList) { //for each PI I need to remove the order
                Person receiver = ClothoAdapter.getPerson(receivedById, clothoObject);
                List<String> originalReceivedBy = receiver.getSubmittedOrders();//original
                originalReceivedBy.remove(orderToDeny.getId());
                receiver.setSubmittedOrders(originalReceivedBy);

                clothoObject.logout();
                ClothoAdapter.setPerson(receiver, clothoObject);

            }
            clothoObject.logout();
            clothoObject.login(loginMap);

            //removed all trace that this order was submitted
            Person creator = ClothoAdapter.getPerson(orderToDeny.getCreatedById(), clothoObject);
            List<String> originalDeniedOrders = creator.getDeniedOrders();
            originalDeniedOrders.add(orderToDeny.getId());
            orderToDeny.setStatus(OrderStatus.DENIED);
            ClothoAdapter.setOrder(orderToDeny, clothoObject);
            clothoObject.logout();
            ClothoAdapter.setPerson(creator, clothoObject);

            conn.closeConnection();

        }
    }

    @RequestMapping(value = "/exportOrderCSV", method = RequestMethod.GET)
    public void exportOrderCSV(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        String username = this.backendPhagebookUser;
        String password = this.backendPhagebookPassword;
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        Order order = ClothoAdapter.getOrder(params.get("orderId"), clothoObject);
        System.out.println(order.getId());
        List<String> cartItems = new ArrayList<String>();
        List<OrderColumns> ColumnList = new ArrayList<OrderColumns>();
        List<String> CList = new ArrayList<String>();

        CList.add("ITEM");
        CList.add("QTY.");
        CList.add("UNIT PRICE");
        CList.add("CUSTOM UNIT PRICE");
        CList.add("TOTAL PRICE");

        ColumnList.add(OrderColumns.SERIAL_NUMBER);

        for (String cartItem : CList) {

            switch (cartItem) { //can add all of them for a customizable form
                case "ITEM":
                    ColumnList.add(OrderColumns.PRODUCT_NAME);
                    break;
                case "QTY.":
                    ColumnList.add(OrderColumns.QUANTITY);
                    break;
                case "UNIT PRICE":
                    ColumnList.add(OrderColumns.UNIT_PRICE);
                    break;
                case "CUSTOM UNIT PRICE":
                    ColumnList.add(OrderColumns.CUSTOM_UNIT_PRICE);
                    break;
                case "TOTAL PRICE":
                    ColumnList.add(OrderColumns.TOTAL_PRICE);
                    break;
            }
        }

        cartItems = org.clothocad.phagebook.controller.OrderController.createOrderForm(order, ColumnList);
        String path = Utilities.getFilepath();
        FileWriter file = new FileWriter(new File(path + "src/main/webapp/resources/OrderSheets/", "Order_" + order.getId() + ".csv"));

        for (String cartItem : cartItems) {
            file.append(cartItem);
        }
        file.flush();
        file.close();

        PrintWriter writer = response.getWriter();

        writer.println(order.getId());
        writer.flush();
        writer.close();
        conn.closeConnection();
    }

    @RequestMapping(value = "/markOrderAsReceived", method = RequestMethod.POST)
    public void markOrderAsReceived(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : ""; //for error checking later
        boolean isValid = false;
        if (!orderId.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            /*
            
             DIRECT ASSUMPTION THAT USER: phagebook exists and their 
             PASSWORD: backend
             */
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Person user = ClothoAdapter.getPerson(userId, clothoObject);
            clothoObject.logout();

            Map loginMap2 = new HashMap();
            loginMap2.put("username", user.getEmailId());
            loginMap2.put("credentials", user.getPassword());
            clothoObject.login(loginMap2);

            Order order = ClothoAdapter.getOrder(orderId, clothoObject);
            /*TODO CHECK IF THE ORDER's RECEIVED BY IDS MATCHES THE USER ID PASSED IN*/
            List<String> receivedBys = order.getReceivedByIds();
            for (String receiver : receivedBys) {
                if (receiver.equals(userId)) // CHECK IF THE ID'S MATCH WITH THE ONE SUBMITTED.
                {
                    order.setStatus(OrderStatus.RECEIVED);
                }
            }

            ClothoAdapter.setOrder(order, clothoObject);

            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing required parameters for marking an order");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing required parameters for marking an order");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/resubmitOrder", method = RequestMethod.POST)
    public void resubmitOrder(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        String username = this.backendPhagebookUser;
        String password = this.backendPhagebookPassword;
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        Order orderOld = ClothoAdapter.getOrder(params.get("orderId"), clothoObject);

        String orderName = orderOld.getName();
        String createdBy = orderOld.getCreatedById();
        String labId = orderOld.getAffiliatedLabId();
        Double taxRate = orderOld.getTaxRate();
        String associatedProjectId = orderOld.getRelatedProjectId();
        Double budget = orderOld.getBudget();
        Integer orderLimit = orderOld.getMaxOrderSize();

        Date date = new Date();

        boolean isValid = false;
        //All parameters needed to create a new order as per the wire frame. 

        if (isValid) {
            /*
            
             DIRECT ASSUMPTION THAT USER: phagebook exists and their 
             PASSWORD: backend
             */
            Order order = new Order();
            order.setName(orderName);
            order.setCreatedById(createdBy);
            order.setDateCreated(date);
            order.setBudget(budget);
            order.setMaxOrderSize(orderLimit);

            order.setAffiliatedLabId(labId);
            order.setRelatedProjectId(associatedProjectId);
            order.setStatus(OrderStatus.INPROGRESS);

            ClothoAdapter.createOrder(order, clothoObject); // CREATED THE ORDER
            // BUT I NOW NEED TO LINK IT TO THE USER
            Person creator = ClothoAdapter.getPerson(order.getCreatedById(), clothoObject);
            List<String> createdOrders = creator.getCreatedOrders();
            createdOrders.add(order.getId());
            System.out.println("I am still on this part");
            clothoObject.logout();

            ClothoAdapter.setPerson(creator, clothoObject); // LINK CREATED

            response.setStatus(HttpServletResponse.SC_CREATED);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "order created");
            responseJSON.put("orderId", order.getId());
            writer.println(responseJSON);
            writer.flush();
            writer.close();

            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Missing Required Parameters.");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
        }

        PrintWriter writer = response.getWriter();
        writer.println("temp");
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "/newOrder", method = RequestMethod.POST)
    public void newOrder(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {

        //params.get RETURNS NULL IF IT DOESN'T EXIST!!!
        Object pOrderName = params.get("name");
        String orderName = pOrderName != null ? (String) pOrderName : "";

        //WEBSITE WILL SEND THE ID
        Object pCreatedBy = params.get("createdBy");
        String createdBy = pCreatedBy != null ? (String) pCreatedBy : "";

        //WEBSITE WILL SEND THIS
        Object pLabId = params.get("labId");
        String labId = pLabId != null ? (String) pLabId : "";

        Object pTaxRate = params.get("tax");
        String taxRateString = pTaxRate != null ? (String) pTaxRate : "";
        Double taxRate = Double.parseDouble(taxRateString) / 100;

        Object pAssociatedProject = params.get("associatedProjectId");
        String associatedProjectId = pAssociatedProject != null ? (String) pAssociatedProject : "";

        Object pBudget = params.get("budget");
        String strBudget = pBudget != null ? (String) pBudget : "";
        Double budget = Double.parseDouble(strBudget);

        Object pOrderLimit = params.get("orderLimit");
        String strOrderLimit = pOrderLimit != null ? (String) pOrderLimit : "";
        Integer orderLimit = Integer.parseInt(strOrderLimit);

        Date date = new Date();

        boolean isValid = false;
        //All parameters needed to create a new order as per the wire frame. 

        if (!orderName.equals("") && !createdBy.equals("") && !labId.equals("")
                && !associatedProjectId.equals("") && !strBudget.equals("")
                && !strOrderLimit.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            /*
            
             DIRECT ASSUMPTION THAT USER: phagebook exists and their 
             PASSWORD: backend
             */
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Order order = new Order();
            order.setName(orderName);
            order.setCreatedById(createdBy);
            order.setDateCreated(date);
            order.setBudget(budget);
            order.setMaxOrderSize(orderLimit);
            if (!taxRateString.equals("")) {
                order.setTaxRate((1 + taxRate));
            } else {
                order.setTaxRate(1.07d);
            }

            order.setAffiliatedLabId(labId);
            order.setRelatedProjectId(associatedProjectId);
            order.setStatus(OrderStatus.INPROGRESS);

            ClothoAdapter.createOrder(order, clothoObject); // CREATED THE ORDER
            // BUT I NOW NEED TO LINK IT TO THE USER
            Person creator = ClothoAdapter.getPerson(order.getCreatedById(), clothoObject);
            List<String> createdOrders = creator.getCreatedOrders();
            createdOrders.add(order.getId());
            System.out.println("I am still on this part");
            clothoObject.logout();

            ClothoAdapter.setPerson(creator, clothoObject); // LINK CREATED

            response.setStatus(HttpServletResponse.SC_CREATED);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "order created");
            responseJSON.put("orderId", order.getId());
            writer.println(responseJSON);
            writer.flush();
            writer.close();

            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Missing Required Parameters.");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
        }
    }

    //TODO
    @RequestMapping(value = "/processOrder")
    protected void processRequest(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            System.out.println("got an Order request here!");

            String name = params.get("name");
            System.out.println(name);
            // create order object
            Order order = new Order(name);

            // create a result object and send it to the frontend
            JSONObject result = new JSONObject();
            result.put("success", 1);

            PrintWriter writer = response.getWriter();
            writer.println(result);
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/sendOrderForApproval", method = RequestMethod.POST)
    public void sendOrderForApproval(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";

        Object pNewUserId = params.get("newUserId");
        String newUserId = pNewUserId != null ? (String) pNewUserId : "";

        boolean isValid = false;
        if (!orderId.equals("") && !userId.equals("") && !newUserId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);

            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            /*
            
             DIRECT ASSUMPTION THAT USER: phagebook exists and their 
             PASSWORD: backend
             */
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            Person loggedInPerson = ClothoAdapter.getPerson(userId, clothoObject);
            Person newLoggedInPerson = ClothoAdapter.getPerson(newUserId, clothoObject);
            Order orderToTransfer = ClothoAdapter.getOrder(orderId, clothoObject);
            if (!orderToTransfer.getId().equals("")) {
                List<String> receivedBys = orderToTransfer.getReceivedByIds();
                receivedBys.remove(loggedInPerson.getId()); //remove the user that is logged in from the order's receivedby
                receivedBys.add(newLoggedInPerson.getId()); //get the new person we want to attach.
                //this is just in order... now we gotta modify each person.

                List<String> loggedInPersonReceivedBy = loggedInPerson.getSubmittedOrders();
                loggedInPersonReceivedBy.remove(orderToTransfer.getId());
                List<String> newLoggedInPersonReceivedBy = newLoggedInPerson.getSubmittedOrders();
                newLoggedInPersonReceivedBy.add(orderToTransfer.getId());

                orderToTransfer.setStatus(OrderStatus.SUBMITTED);
                ClothoAdapter.setOrder(orderToTransfer, clothoObject);

                clothoObject.logout();
                ClothoAdapter.setPerson(loggedInPerson, clothoObject);
                ClothoAdapter.setPerson(newLoggedInPerson, clothoObject);

                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order has been passed on to " + newLoggedInPerson.getEmailId());
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();

            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order does not exist");
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
            }
            conn.closeConnection();
        } else {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Parameters are missing");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
        }

    }

    @RequestMapping(value = "/submitOrderToPIs", method = RequestMethod.POST)
    public void submitOrderToPIs(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        boolean isValid = false;
        if (!orderId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            /*
            
             DIRECT ASSUMPTION THAT USER: phagebook exists and their 
             PASSWORD: backend
             */
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Order orderToSubmit = ClothoAdapter.getOrder(orderId, clothoObject);
            if (!orderToSubmit.getId().equals("")) {

                List<Person> PIs = new ArrayList<>();
                Lab orderLab = ClothoAdapter.getLab(orderToSubmit.getAffiliatedLabId(), clothoObject);

                for (String PIid : orderLab.getLeadPIs()) {
                    Person pi = ClothoAdapter.getPerson(PIid, clothoObject); //gets that person object

                    List<String> submittedOrders = pi.getSubmittedOrders(); // we don't want to replace, we want to add on. 
                    submittedOrders.add(orderToSubmit.getId()); //add our order Id to that list for each person
                    PIs.add(pi); // add that pi to a list. 
                    List<String> receivedByIdsForOrder = orderToSubmit.getReceivedByIds(); //get the list of people who have received the order
                    receivedByIdsForOrder.add(PIid); // add the current PIid to that list. 

                }

                // have all the people that the order should be submitted to that are PI's of that lab
                //add those to both order and each person.
                //TODO ADD THIS...
                clothoObject.logout();
                for (Person pi : PIs) {
                    Map piMap = new HashMap();
                    piMap.put("username", pi.getEmailId());
                    piMap.put("credentials", pi.getPassword());
                    ClothoAdapter.setPerson(pi, clothoObject);
                    clothoObject.logout();
                }
                clothoObject.login(loginMap);
                orderToSubmit.setStatus(OrderStatus.SUBMITTED);
                ClothoAdapter.setOrder(orderToSubmit, clothoObject);

                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Success! Order and PI's should be changed.");
                writer.println(responseJSON.toString());
                conn.closeConnection();
                writer.flush();
                writer.close();
            } else {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order does not exist");
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Missing Required Parameters.");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping(value = "/removeProductsFromOrder", method = RequestMethod.POST)
    protected void removeProductsFromOrder(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        //I WILL BE GETTING A JSON ARRAY OF CART ITEM IDS THAT I NEED TO REMOVE FROM AN ORDER
        //AND A USER NAME THAT IS ASSOCIATED WITH THAT ORDER
        //AND THAT ORDER ID.
        Object pUser = params.get("user");
        String user = pUser != null ? (String) pUser : "";

        Object pCartItems = params.get("cartItem");
        String cartItem = pCartItems != null ? (String) pCartItems : "";

        Object pOrderId = params.get("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";

        boolean isValid = false;
        if (!user.equals("") && !cartItem.equals("") && !orderId.equals("")) {
            isValid = true;
        }

        if (isValid) {

            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Person userP = ClothoAdapter.getPerson(user, clothoObject);
            Order ord = ClothoAdapter.getOrder(orderId, clothoObject);
            List<String> cartItemsInOrder = ord.getProducts(); //CART ITEM ID 

            if (ord.getCreatedById().equals(userP.getId()) || ord.getReceivedByIds().contains(userP.getId())) {

                if (cartItemsInOrder.contains(cartItem)) { //they key exists in the map 

                    //remove that specific Cart Item.
                    CartItem cItem = ClothoAdapter.getCartItem(cartItem, clothoObject);
                    int quantity = cItem.getQuantity();

                    Product product = ClothoAdapter.getProduct(cItem.getProductId(), clothoObject);
                    product.increaseInventory(quantity);

                    ClothoAdapter.setProduct(product, clothoObject); //increase inventory for that product

                    //DON'T KNOW HOW TO DELETE FROM CLOTHO...
                    //will unlink though from the cart item map...
                    cartItemsInOrder.remove(cartItem);

                    ord.setProducts(cartItemsInOrder); // set the new cart item map with the ones we don't want nixed
                    ClothoAdapter.setOrder(ord, clothoObject);

                }

            }

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Order should be updated, please verify");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing servlet parameters");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

    @RequestMapping(value = "/parseCartItem", method = RequestMethod.GET)
    protected void parseCartItem(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pUser = params.get("user");
        String user = pUser != null ? (String) pUser : "";

        Object pCartItemId = params.get("cartItem");
        String cartItemId = pCartItemId != null ? (String) pCartItemId : "";
        boolean isValid = false;

        if (!user.equals("") && !cartItemId.equals("")) {
            isValid = true;
        }
        net.sf.json.JSONObject responseJSON = new net.sf.json.JSONObject();
        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            // able to query now. 

            CartItem cartItem = ClothoAdapter.getCartItem(cartItemId, clothoObject);

            Product product = ClothoAdapter.getProduct(cartItem.getProductId(), clothoObject);
            responseJSON.put("productId", product.getId());
            responseJSON.put("discount", cartItem.getDiscount());
            responseJSON.put("productName", product.getName());
            responseJSON.put("productUnitPrice", product.getUnitPrice());
            responseJSON.put("quantity", cartItem.getQuantity());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJSON.put("message", "id provided does not exist");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }

    }

    @RequestMapping(value = "/getOrderProducts", method = RequestMethod.GET)
    protected void getOrderProducts(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        String username = this.backendPhagebookUser;
        String password = this.backendPhagebookPassword;
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        Order order = ClothoAdapter.getOrder(params.get("orderId"), clothoObject);
        System.out.println(order.getId());
        List<String> cartItems = new ArrayList<String>();

        Double orderValue = 0.0;

        OrderColumns quantity = OrderColumns.QUANTITY;
        OrderColumns unitPrice = OrderColumns.UNIT_PRICE;
        
        String quantities = quantity.toString();
        String unitPrices = unitPrice.toString();

//            Int quantityVal = quantity.toInt();
//            Double unitPrice = quantity,toDouble(); 
//            
//            orderValue += quantityVal * unitPrice;

        net.sf.json.JSONObject responseJSON = new net.sf.json.JSONObject();

        responseJSON.put("orderValue", orderValue);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(responseJSON);
        out.flush();
        conn.closeConnection();
    }
}
