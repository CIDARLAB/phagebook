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

/**
 *
 * @author azula
 */
public class addColleagueRequest extends HttpServlet {

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

        Object pUserId = request.getParameter("loggedInClothoId");
        String userId = pUserId != null ? (String) pUserId : "";

        Object pRequestId = request.getParameter("colleagueClothoId");
        String requestId = pRequestId != null ? (String) pRequestId : "";

        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!userId.equals("")) {
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
            //userID = person logged into phagebook
            //requestId = person not logged in... receives request
            Person receivesRequest = ClothoAdapter.getPerson(requestId, clothoObject);
            if (!requestId.equals(userId)) {
                if (!receivesRequest.getId().equals("") && !userId.equals("")) {
                    if (!receivesRequest.getColleagues().contains(userId) && !receivesRequest.getColleagueRequests().contains(userId)) {
                        
                        receivesRequest.addColleagueRequest(userId);
                    }

                }
                
                clothoObject.logout();
                ClothoAdapter.setPerson(receivesRequest, clothoObject);
            }
            
            conn.closeConnection();

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "this servlet adds the userId of the logged in user to the requests of the person";
    }// </editor-fold>

}
