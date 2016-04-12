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
public class sendOrderForApproval extends HttpServlet {

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
        String userId = pUserId != null ? (String) pUserId : "";
        
        Object pNewUserId = request.getParameter("newUserId");
        String newUserId = pNewUserId != null ? (String) pNewUserId : "";
        
        boolean isValid = false; 
        if (!orderId.equals("") && !userId.equals("") && !newUserId.equals("")){
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
            Person loggedInPerson = ClothoAdapter.getPerson(userId, clothoObject);
            Person newLoggedInPerson = ClothoAdapter.getPerson(newUserId, clothoObject);
            Order orderToTransfer = ClothoAdapter.getOrder(orderId, clothoObject);
            if (!orderToTransfer.getId().equals("")){
                List<String> receivedBys = orderToTransfer.getReceivedByIds();
                receivedBys.remove(loggedInPerson.getId()); //remove the user that is logged in from the order's receivedby
                receivedBys.add(newLoggedInPerson.getId()); //get the new person we want to attach.
                //this is just in order... now we gotta modify each person.
                
                List<String> loggedInPersonReceivedBy = loggedInPerson.getSubmittedOrders();
                loggedInPersonReceivedBy.remove(orderToTransfer.getId());
                List<String> newLoggedInPersonReceivedBy = newLoggedInPerson.getSubmittedOrders();
                newLoggedInPersonReceivedBy.add(orderToTransfer.getId());
                
                orderToTransfer.setStatus(OrderStatus.SUBMITTED);
                ClothoAdapter.setOrder(orderToTransfer, clothoObject);
                
                
                clothoObject.logout();
                ClothoAdapter.setPerson(loggedInPerson, clothoObject);
                ClothoAdapter.setPerson(newLoggedInPerson, clothoObject);
                
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order has been passed on to "+ newLoggedInPerson.getEmailId() );
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
                
                
            }else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order does not exist");
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
            }
            conn.closeConnection();
        }
        else
        {
            
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Parameters are missing");
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
