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
import org.clothocad.phagebook.dom.Order.OrderStatus;

/**
 *
 * @author Herb
 */
public class denyOrder extends HttpServlet {

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
        
        boolean isValid = false;
        if (!orderId.equals("") && !userId.equals("")){
            isValid = true;
        }
        
        if (isValid){
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
            
            Order orderToDeny = ClothoAdapter.getOrder(orderId, clothoObject);
            List<String> receivedByList = orderToDeny.getReceivedByIds();
            
            for (String receivedById : receivedByList){ //for each PI I need to remove the order
                Person receiver = ClothoAdapter.getPerson(receivedById, clothoObject);
                List<String> originalReceivedBy= receiver.getSubmittedOrders();//original
                originalReceivedBy.remove(orderToDeny.getId());
                receiver.setSubmittedOrders(originalReceivedBy);
                
                clothoObject.logout();
                ClothoAdapter.setPerson(receiver, clothoObject);
                
                
                
                
            }
            clothoObject.logout();
            clothoObject.login(loginMap);
                    
            //removed all trace that this order was submitted
            
            Person creator = ClothoAdapter.getPerson(orderToDeny.getCreatedById(), clothoObject);
            List<String> originalDeniedOrders = creator.getDeniedOrders();
            originalDeniedOrders.add(orderToDeny.getId());
            orderToDeny.setStatus(OrderStatus.DENIED);
            ClothoAdapter.setOrder(orderToDeny, clothoObject);
            clothoObject.logout();
            ClothoAdapter.setPerson(creator, clothoObject);
            
            conn.closeConnection();
            
            
                
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
