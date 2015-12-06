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
import org.json.JSONObject;
//import net.sf.json.JSONArray;

/**
 *
 * @author innaturshudzhyan
 */
public class newOrder extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        
        String orderIds = request.getParameter("orderIds");
        
        List<String> productsAsStrings = Arrays.asList(orderIds.split("\\s*,\\s*"));
       
        List<Product> products = new LinkedList<>();
        
        
        
        
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
        
        Order order = new Order(name);
        order.setDescription(description);
        order.setCreatedOn(timeStamp);
        
        
        for (String id : productsAsStrings){
            products.add(ClothoAdaptor.getProduct(id, clothoObject));
        }
        order.setProducts(products);
        
        String id = (String)ClothoAdaptor.createOrder(order, clothoObject);
        
        conn.closeConnection();
        
        PrintWriter writer = response.getWriter();
        writer.println(id);
        writer.flush();
        writer.close();

    }
}
