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
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.adaptors.EmailHandler;
import org.json.JSONObject;

/**
 *
 * @author Allison Durkan
 */
public class resendVerification extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
       
      
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
        String userId = request.getParameter("id");
        boolean isValid = false;
        if (!userId.isEmpty()){
            isValid = true;
        }
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            createUserMap.put("username", "ClothoBackend");
            createUserMap.put("password", "phagebook");


            clothoObject.createUser(createUserMap);

            Map loginMap = new HashMap();
            loginMap.put("username", "ClothoBackend");
            loginMap.put("credentials", "phagebook");


            clothoObject.login(loginMap);
            
            //operating under the assumption that we will have the saved clotho ID of the user

            Person person1 = ClothoAdapter.getPerson(userId, clothoObject);
            
            if (person1 != null){
                String link = Args.phagebookBaseURL + "/html/verifyEmail.html?emailId=" + person1.getEmailId() + "&salt=" + person1.getSalt();
                EmailHandler handly = EmailHandler.getEmailHandler();
                handly.sendEmailVerification(person1, link);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Email Sent!");
                out.print(responseJSON.toString());
                out.flush();
                out.close();
            } 
        }
        else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Cannot find the user. Try again later!");
            out.print(responseJSON.toString());
            out.flush();
            out.close();
        }
        
        
        
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
