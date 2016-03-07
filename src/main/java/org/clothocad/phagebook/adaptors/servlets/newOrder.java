/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderStatus;
import org.json.JSONException;
import org.json.JSONObject;
//import net.sf.json.JSONArray;

/**
 *
 * @author innaturshudzhyan
 */
public class newOrder extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
       
        //request.getParameter RETURNS NULL IF IT DOESN'T EXIST!!!
        Object pOrderName = request.getParameter("name");
        String orderName  = pOrderName != null ? (String) pOrderName : "" ;
        
        //WEBSITE WILL SEND THE ID
        Object pCreatedBy = request.getParameter("createdBy");
        String createdBy  = pCreatedBy != null ? (String) pCreatedBy : "";
        
        //WEBSITE WILL SEND THIS
        Object pLabId     = request.getParameter("labId");
        String labId      =  pLabId != null ? (String) pLabId: "" ;
        
        Object pAssociatedProject  = request.getParameter("associatedProjectId");
        String associatedProjectId = pAssociatedProject != null ? (String) pAssociatedProject: "";
        
        Object pBudget   =    request.getParameter("budget");
        String strBudget =    pBudget != null ?  (String) pBudget : "";
        Double budget   =     Double.parseDouble(strBudget);
        
        
        Object pOrderLimit  = request.getParameter("orderLimit");
        String strOrderLimit = pOrderLimit != null ? (String) pOrderLimit : "";
        Integer orderLimit   =  Integer.parseInt(strOrderLimit);
        
        
        
        
        Date date = new Date();
        
        boolean isValid = false;
        //All parameters needed to create a new order as per the wire frame. 
        
        
        
        
        if (!orderName.equals("") && !createdBy.equals("") && !labId.equals("")
                && !associatedProjectId.equals("") && !strBudget.equals("")
                && !strOrderLimit.equals(""))
        {
            isValid = true;
        }
        
        
        if (isValid){
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "phagebook";
            String password = "backend";
            /*
            
                DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                                   PASSWORD: backend
            */
            Map loginMap = new HashMap();
            loginMap.put("username"   , username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
       
            
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
            
            
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Missing Required Parameters.");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
        }
     
        
        
        
        
        
        
        
        
        /*
        
        String orderIds = request.getParameter("orderIds") != null ? request.getParameter("orderIds") : "";
       
        JSONObject orderIdsObject = new JSONObject(orderIds);
        System.out.println(orderIdsObject);
      
        ////
        boolean isValid = false;
        if (!orderName.isEmpty() && !description.isEmpty() && !createdBy.isEmpty() && !orderIds.isEmpty())
        {
            isValid = true;
        }
        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "phagebook";
            createUserMap.put("username", username);
            createUserMap.put("password", "password");

            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");

            clothoObject.login(loginMap);
            ///

            Map<String, Integer> products = new HashMap<>() ;
            Iterator<String> it = orderIdsObject.keys();
            while (it.hasNext()) 
            {
                String key = it.next();
                CartItem productOrder = ClothoAdapter.getCartItem(key, clothoObject);
                System.out.println(key.toString());
                try 
                { 

                    int quantity = Integer.parseInt((String) orderIdsObject.get(key));

                    System.out.println(quantity);
                    products.put(productOrder.getId(), quantity);

                } catch (JSONException e)
                {
                    // Something went wrong!
                    System.out.println("something went wrong in mapToOrder");
                }

            }


            System.out.println(products);


            Order order = new Order(orderName);
            order.setProducts(products);
            order.setDescription(description);
            order.setDateCreated(date);
            order.setCreatedById(createdBy);    //connect that person to the order
            order.setStatus(OrderStatus.INPROGRESS);

            boolean personExists = false;
            Person creator = ClothoAdapter.getPerson(createdBy, clothoObject);
            //getPerson will return a person with a blank email ID if it can't find the person in clotho..
            if (!creator.getEmailId().isEmpty()){
                personExists = true;
            }
            
            if (personExists){
                ClothoAdapter.createOrder(order, clothoObject);
                creator.getCreatedOrders().add(order.getId());

                ClothoAdapter.setPerson(creator, clothoObject);
        
                conn.closeConnection();

                response.setStatus(HttpServletResponse.SC_CREATED);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("id", order.getId());
                responseJSON.put("message", "Order created in Clotho!");
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Missing Required Parameters.");
             writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
            
            
        }
                
                */

    }
}
