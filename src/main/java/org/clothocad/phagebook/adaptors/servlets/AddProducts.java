package org.clothocad.phagebook.adaptors.servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import org.clothocad.phagebook.dom.Company;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author innaturshudzhyan
 */

public class AddProducts extends HttpServlet {

        @Override
       
        public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        
        String companyName = request.getParameter("CompanyName");
        
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
        
        

        
        Map companyMap = new HashMap();
        companyMap.put("name",companyName);
        //System.out.println("Company Name :: " + companyName);
        if (!ClothoAdapter.queryCompany(companyMap,clothoObject).isEmpty()){
           writer.println(companyName);
        }
//        else{
//            writer.println("The company does not exist");
//        }
        //System.out.println("reached this part!!");
        conn.closeConnection();
        
        
        writer.flush();
        writer.close();
        
        }
}
