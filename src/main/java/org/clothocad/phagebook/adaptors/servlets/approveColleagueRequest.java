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
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class approveColleagueRequest extends HttpServlet {

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
        Object pColleagueId = request.getParameter("colleagueId");
        String colleagueId = pColleagueId != null ? (String) pColleagueId : "";

        Object pUserId = request.getParameter("userId");
        String userId = pUserId != null ? (String) pUserId : "";
        
        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!colleagueId.equals("") && !userId.equals("")) {
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
            Person user = ClothoAdapter.getPerson(userId, clothoObject);
            List<String> colleagueRequests = user.getColleagueRequests();
            if (colleagueRequests.contains(colleagueId)){
                colleagueRequests.remove(colleagueId);
                user.setColleagueRequests(colleagueRequests);
                
            }
            
            Person colleague = ClothoAdapter.getPerson(colleagueId, clothoObject);
            List<String> colleaguesUser = user.getColleagues();
            List<String> colleaguesColleague = colleague.getColleagues();
            if (!colleaguesUser.contains(colleague.getId())){
                if (!colleaguesColleague.contains(user.getId())){
                    colleaguesUser.add(colleague.getId());
                    colleaguesColleague.add(user.getId());
                    colleague.setColleagues(colleaguesColleague);
                    user.setColleagues(colleaguesUser);
                    
                }
            }
            clothoObject.logout();
            ClothoAdapter.setPerson(user, clothoObject);
            ClothoAdapter.setPerson(colleague, clothoObject);
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Colleague Request Accepted!");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            
            
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters for ajax");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
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