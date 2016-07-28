/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Vendor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author jacob
 */
@Controller
public class AutoCompleteController {
    
    @RequestMapping(value="/autoComplete", method=RequestMethod.GET)
    protected void autoComplete(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        //I WILL RETURN THE MAP AS A JSON OBJECT.. it is client side's issue to parse all data for what they need!
        //they could check over there if the schema matches what they are querying for and so i can do this generically!
        //user should be logged in so I will log in as that user.
        
  
       String query  = params.get("query") != null ? params.get("query") : "";
       boolean isValid = false;
        System.out.println("Query is: " + query);
       if (!query.equals("")){
           isValid = true;
       }
       
       if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            //TODO: we need to have an authentication token at some point

            Map createUserMap = new HashMap();
            String username = "phagebook";
            String password = "backend";
            
            createUserMap.put("username", username);
            createUserMap.put("password", password);
            
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);  

            clothoObject.login(loginMap);

       
            JSONArray replies = (JSONArray) clothoObject.autocomplete(query);
            conn.closeConnection();
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(replies);
            out.flush();
            out.close();
            
            clothoObject.logout();
           
       }
       response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
       response.setContentType("application/json");
       JSONObject reply = new JSONObject();
       reply.put("message", "Auto Complete requires a query parameter");
       PrintWriter out = response.getWriter();
       out.print(reply);
       out.flush();
       out.close();
           
    }
    
    @RequestMapping(value="/autoCompleteProjects", method=RequestMethod.GET)
    protected void autoCompleteProjects(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        //I WILL RETURN THE MAP AS A JSON OBJECT.. it is client side's issue to parse all data for what they need!
        //they could check over there if the schema matches what they are querying for and so i can do this generically!
        //user should be logged in so I will log in as that user.
        
  
       String name  = params.get("name") != null ? params.get("name") : "";
       boolean isValid = false;
        System.out.println("Name is: " + name);
       if (!name.equals("")){
           isValid = true;
       }
       
       if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            //TODO: we need to have an authentication token at some point

            Map createUserMap = new HashMap();
            String username = "phagebook";
            String password = "backend";
            
            createUserMap.put("username", username);
            createUserMap.put("password", password);
            
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);  

            clothoObject.login(loginMap);
            Map query = new HashMap();
            
            query.put("query", name); // the value for which we are querying.
            query.put("key", "name"); // the key of the object we are querying
            
            List<Project> projects = ClothoAdapter.queryProject(query, clothoObject, ClothoAdapter.QueryMode.STARTSWITH);
            org.json.JSONArray responseArray = new org.json.JSONArray();
            for (Project proj : projects){
                JSONObject obj = new JSONObject();
                obj.put("id", proj.getId());
                obj.put("name", proj.getName());
                responseArray.put(obj);
            }
            
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseArray);
            out.flush();
            out.close();
            
            clothoObject.logout();
            conn.closeConnection();
           
       }
       response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
       response.setContentType("application/json");
       JSONObject reply = new JSONObject();
       reply.put("message", "Auto Complete requires a query parameter");
       PrintWriter out = response.getWriter();
       out.print(reply);
       out.flush();
       out.close();
    }
    
    @RequestMapping(value="/autoCompleteVendors", method=RequestMethod.GET)
    protected void autoCompleteVendors(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
         //I WILL RETURN THE MAP AS A JSON OBJECT.. it is client side's issue to parse all data for what they need!
        //they could check over there if the schema matches what they are querying for and so i can do this generically!
        //user should be logged in so I will log in as that user.
        
  
       String name  = params.get("name") != null ? params.get("name") : "";
       boolean isValid = false;
        System.out.println("Name is: " + name);
       if (!name.equals("")){
           isValid = true;
       }
       
       if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            //TODO: we need to have an authentication token at some point

            Map createUserMap = new HashMap();
            String username = "phagebook";
            String password = "backend";
            
            createUserMap.put("username", username);
            createUserMap.put("password", password);
            
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);  

            clothoObject.login(loginMap);
            Map query = new HashMap();
            
            query.put("query", name); // the value for which we are querying.
            query.put("key", "name"); // the key of the object we are querying
            
            List<Vendor> vendors = ClothoAdapter.queryVendor(query, clothoObject, ClothoAdapter.QueryMode.STARTSWITH);
            org.json.JSONArray responseArray = new org.json.JSONArray();
            for (Vendor vend : vendors){
                JSONObject obj = new JSONObject();
                obj.put("id", vend.getId());
                obj.put("name", vend.getName());
                responseArray.put(obj);
            }
            
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseArray);
            out.flush();
            out.close();
            
            clothoObject.logout();
            conn.closeConnection();
           
       }
       response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
       response.setContentType("application/json");
       JSONObject reply = new JSONObject();
       reply.put("message", "Auto Complete requires a query parameter");
       PrintWriter out = response.getWriter();
       out.print(reply);
       out.flush();
       out.close();
    }
}
