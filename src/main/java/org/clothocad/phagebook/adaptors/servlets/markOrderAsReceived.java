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
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderStatus;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class markOrderAsReceived extends HttpServlet {

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
        
        Object pOrderId = request.getParameter("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";
        
        Object pUserId = request.getParameter("userId");
        String userId = pUserId != null ? (String) pUserId : ""; //for error checking later
        boolean isValid = false;
        if (!orderId.equals("") && !userId.equals("")){
            isValid = true;
        }
        
        if (isValid)
        {
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
         
            String username = "phagebook";
            String password = "backend";
            /*
            
                DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                                   PASSWORD: backend
            */
            Map loginMap = new HashMap();
            loginMap.put("username"   , username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            
            Person user = ClothoAdapter.getPerson(userId, clothoObject);
            clothoObject.logout();
            
            Map loginMap2 = new HashMap();
            loginMap2.put("username"   , user.getEmailId());
            loginMap2.put("credentials", user.getPassword());
            clothoObject.login(loginMap2);
            
            Order order = ClothoAdapter.getOrder(orderId, clothoObject);
            /*TODO CHECK IF THE ORDER's RECEIVED BY IDS MATCHES THE USER ID PASSED IN*/
            List<String> receivedBys = order.getReceivedByIds(); 
            for (String receiver : receivedBys)
            {
                if (receiver.equals(userId)) // CHECK IF THE ID'S MATCH WITH THE ONE SUBMITTED.
                {
                    order.setStatus(OrderStatus.RECEIVED);
                }
            }
            
            ClothoAdapter.setOrder(order, clothoObject);
            
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing required parameters for marking an order");
            writer.println(responseJSON.toString());
            writer.flush();
            writer.close();
            conn.closeConnection();
        }
        else{
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "missing required parameters for marking an order");
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
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
