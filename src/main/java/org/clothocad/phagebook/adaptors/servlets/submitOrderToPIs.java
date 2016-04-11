/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderStatus;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class submitOrderToPIs extends HttpServlet {

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
        
        boolean isValid = false; 
        if (!orderId.equals("")){
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
            
            
            Order orderToSubmit = ClothoAdapter.getOrder(orderId, clothoObject);
            if (!orderToSubmit.getId().equals("")){
                
                List<Person> PIs = new ArrayList<>();
                Lab orderLab = ClothoAdapter.getLab(orderToSubmit.getAffiliatedLabId(), clothoObject);
                
                for (String PIid : orderLab.getLeadPIs()){
                    Person pi = ClothoAdapter.getPerson(PIid, clothoObject); //gets that person object
                    
                    List<String> submittedOrders = pi.getSubmittedOrders(); // we don't want to replace, we want to add on. 
                    submittedOrders.add(orderToSubmit.getId()); //add our order Id to that list for each person
                    PIs.add(pi); // add that pi to a list. 
                    List<String> receivedByIdsForOrder = orderToSubmit.getReceivedByIds(); //get the list of people who have received the order
                    receivedByIdsForOrder.add(PIid); // add the current PIid to that list. 
                    
                }
                
                
                // have all the people that the order should be submitted to that are PI's of that lab
                
                //add those to both order and each person.
                //TODO ADD THIS...
                clothoObject.logout();
                for (Person pi : PIs){
                    Map piMap = new HashMap();
                    piMap.put("username"   , pi.getEmailId());
                    piMap.put("credentials", pi.getPassword());
                    ClothoAdapter.setPerson(pi, clothoObject);
                    clothoObject.logout();
                }
                clothoObject.login(loginMap);
                orderToSubmit.setStatus(OrderStatus.SUBMITTED);
                ClothoAdapter.setOrder(orderToSubmit, clothoObject);
                
                
                       
                
                
                
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Success! Order and PI's should be changed.");
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
            }
            else {
            
            
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter writer = response.getWriter();
                response.setContentType("application/JSON");
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Order does not exist");
                writer.println(responseJSON.toString());
                writer.flush();
                writer.close();
            }
        }
        else 
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/JSON");
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Missing Required Parameters.");
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

    private Map HashMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
