/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.controller.Args;
import org.json.JSONObject;

/**
 *
 * @author Herb
 * WILL AUTOCOMPLETE AND RETURN A JSON OBJECT WITH EVERY PROPERTY OF THE OBJECTS THAT IT GOT...
 * 
 */
public class autoComplete extends HttpServlet {

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
        processRequest(request, response);
        
        //I WILL RETURN THE MAP AS A JSON OBJECT.. it is client side's issue to parse all data for what they need!
        //they could check over there if the schema matches what they are querying for and so i can do this generically!
        //user should be logged in so I will log in as that user.
        
  
       String query  = request.getParameter("query") != null ? request.getParameter("query") : "";
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
            String username = "test"+ System.currentTimeMillis() ;


            createUserMap.put("username", username);
            createUserMap.put("password", "password");


            clothoObject.createUser(createUserMap);

            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");  

            clothoObject.login(loginMap);

       
            JSONArray replies = (JSONArray) clothoObject.autocomplete(query);
       
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
    }
    // </editor-fold>
    
    

}
