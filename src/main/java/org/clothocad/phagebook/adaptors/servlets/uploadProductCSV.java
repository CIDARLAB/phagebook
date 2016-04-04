/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.clothocad.phagebook.controller.OrderController;

/**
 *
 * @author innaturshudzhyan
 */
public class uploadProductCSV extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
       
        //request.getParameter RETURNS NULL IF IT DOESN'T EXIST!!!
        JSONArray arr = new JSONArray(request.getParameter("jsonArray"));
        
        /////////////////Ask Johan
        List<String> productIds = new ArrayList<String>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        String username = "innatur@bu.edu";
        String password = "12345";
        
        clothoObject.login(username, password);
        
        productIds = OrderController.getProducts(arr, clothoObject);
        
        PrintWriter writer = response.getWriter();
        
        writer.println(productIds.toString());
        writer.flush();
        writer.close();
        
    }    
     
}
