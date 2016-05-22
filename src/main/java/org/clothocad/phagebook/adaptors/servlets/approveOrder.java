/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
public class approveOrder extends HttpServlet {

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
            
            Order orderToApprove = ClothoAdapter.getOrder(orderId, clothoObject);
            List<String> receivedByList = orderToApprove.getReceivedByIds();
            String finalApprover = "";
            String fAEmailId = "";
            for (String id : receivedByList){
                if (id.equals(userId)){
                    finalApprover = id;
                    Person approver = ClothoAdapter.getPerson(id, clothoObject);
                    clothoObject.logout();
                    Map login2 = new HashMap();
                    fAEmailId = approver.getEmailId();
                    login2.put("username", fAEmailId);
                    login2.put("credentials", approver.getPassword());
                    clothoObject.login(login2);
                    List<String> approvedOrder = approver.getApprovedOrders();
                    List<String> submittedOrders = approver.getSubmittedOrders(); // need to add to approved and remove from submitted..
                    approvedOrder.add(orderToApprove.getId());
                    submittedOrders.remove(orderToApprove.getId());
                    clothoObject.logout();
                    ClothoAdapter.setPerson(approver, clothoObject);
                    clothoObject.login(loginMap);
                    orderToApprove.setDateApproved(new Date());
                    
                    orderToApprove.setStatus(OrderStatus.APPROVED);
                    orderToApprove.setApprovedById(finalApprover);
                    ClothoAdapter.setOrder(orderToApprove, clothoObject);
                    
                    
                }
                
            }
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Order has been approved!");
            responseJSON.put("approvedBy", finalApprover);
            responseJSON.put("approvedByEmail", fAEmailId);
            conn.closeConnection();
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            
            
        }
        else 
        {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters for servlet call");
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
