/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.controller.OrderController;
import org.clothocad.phagebook.controller.Utilities;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderColumns;
import org.json.JSONArray;

/**
 *
 * @author innaturshudzhyan
 */
public class exportOrderCSV extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = "phagebook";
        String password = "backend";
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);  

        clothoObject.login(loginMap);
        
        Order order = ClothoAdapter.getOrder(request.getParameter("orderId"),clothoObject);
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
        
        for(String cartItem : CList){
            
            switch (cartItem){ //can add all of them for a customizable form
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
         
        cartItems = OrderController.createOrderForm(order, ColumnList);
        String path = Utilities.getFilepath();
        FileWriter file = new FileWriter(new File(path + "src/main/webapp/resources/OrderSheets/","Order_" + order.getId() + ".csv"));
        
        for (String cartItem: cartItems)
        {
           file.append(cartItem); 
        }
        file.flush();
        file.close();

        PrintWriter writer = response.getWriter();
        
        writer.println(order.getId());
        writer.flush();
        writer.close();
    }    
}
