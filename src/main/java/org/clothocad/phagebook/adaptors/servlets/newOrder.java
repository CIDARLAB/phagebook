/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.controller.OrderController;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
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
        
        System.out.println("IN THE SERVLET");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        
        String orderIds = request.getParameter("orderIds");
        JSONObject orderIdsObject = new JSONObject(orderIds);
        System.out.println(orderIdsObject);
      
        ////
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
        
        Map<Product, Integer> products = new HashMap<>() ;
        Iterator<String> it = orderIdsObject.keys();
        while (it.hasNext()) 
        {
            String key = it.next();
            Product productOrder = ClothoAdaptor.getProduct(key, clothoObject);
            System.out.println(key.toString());
            try 
            { 
                
                int quantity = Integer.parseInt((String) orderIdsObject.get(key));
                
                System.out.println(quantity);
                products.put(productOrder, quantity);
                
            } catch (JSONException e)
            {
                // Something went wrong!
                System.out.println("something went wrong in mapToOrder");
            }

        }
        
        
        System.out.println("HERE");
        
        System.out.println(products);
        
        
        Order order = new Order(name);
        order.setProducts(products);
        order.setDescription(description);
        order.setCreatedOn(timeStamp);
        
       //
        
        String id = (String)ClothoAdaptor.createOrder(order, clothoObject);
        
        conn.closeConnection();
        
        PrintWriter writer = response.getWriter();
        writer.println(id);
        writer.flush();
        writer.close();

    }
}
