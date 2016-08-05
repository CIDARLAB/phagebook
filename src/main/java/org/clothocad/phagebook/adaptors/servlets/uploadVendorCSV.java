/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

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
import org.clothocad.phagebook.controller.Args;
import org.json.JSONArray;
import org.clothocad.phagebook.controller.OrderController;

/**
 *
 * @author innaturshudzhyan
 */
public class uploadVendorCSV extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        JSONArray arr = new JSONArray(request.getParameter("jsonArray"));

        List<String> vendorIds = new ArrayList<String>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = "phagebook";
        String password = "backend";
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);  

        clothoObject.login(loginMap);
        
        vendorIds = OrderController.getVendors(arr, clothoObject);
        
        PrintWriter writer = response.getWriter();
        conn.closeConnection();
        writer.println("created " + vendorIds);
        writer.flush();
        writer.close();
    }    
     
}
