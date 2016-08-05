/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Vendor;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class autoCompleteVendors extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         //I WILL RETURN THE MAP AS A JSON OBJECT.. it is client side's issue to parse all data for what they need!
        //they could check over there if the schema matches what they are querying for and so i can do this generically!
        //user should be logged in so I will log in as that user.
        
  
       String name  = request.getParameter("name") != null ? request.getParameter("name") : "";
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
            JSONArray responseArray = new JSONArray();
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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
