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
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONObject;

/**
 *
 * @author Allison Durkan
 */
public class createStatus extends HttpServlet {

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
        // black magic
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

        System.out.println("inside create status post");

        Object pUserId = request.getParameter("clothoId");
        String userId = pUserId != null ? (String) pUserId : "";

        Object pNewStatus = request.getParameter("status");
        String newStatus = pNewStatus != null ? (String) pNewStatus : "";

        System.out.println(newStatus);

        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!userId.equals("") && !newStatus.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //

            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            if (!retrieve.getId().equals("")) {
                
                if (!newStatus.equals("")) {
                    Status newStatusThing = new Status();
                    
                    newStatusThing.setText(newStatus);
                    newStatusThing.setUserId(retrieve.getId());
                    ClothoAdapter.createStatus(newStatusThing, clothoObject);
                    
                    retrieve.addStatus(newStatusThing);
                }
                
                clothoObject.logout();
                ClothoAdapter.setPerson(retrieve, clothoObject);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_CREATED);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "status added");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
                out.close();
                
                conn.closeConnection();
            }
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            out.close();
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet allows a new status to be added to the person object";
    }// </editor-fold>

}
