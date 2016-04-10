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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class listCreatedOrdersOfPerson extends HttpServlet {

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
        
         //I AM ASSUMING THAT 
        /* ID is passed in of person
       
        */
        
        Object pUser = request.getParameter("user");
        String user = pUser != null ? (String) pUser: "";
        boolean isValid = false;
        
        if (!user.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            
            Person prashant = ClothoAdapter.getPerson(user, clothoObject);
            boolean exists = true;
            if (prashant.getId().equals("")){
                System.out.println("Person does not exist in list open orders of person");
                exists = false;
            } 
            if (exists){
                List<String> createdOrders = prashant.getCreatedOrders();
                JSONArray createdOrdersJSON = new JSONArray();
                for (String created : createdOrders ){
                    Order temp = ClothoAdapter.getOrder(created, clothoObject);
                    if (temp.getStatus().equals(OrderStatus.INPROGRESS)){
                    JSONObject tempAsJSON = new JSONObject();
                    
                    tempAsJSON.put("name", temp.getName());
                    
                    tempAsJSON.put("description", temp.getDescription());
                    tempAsJSON.put("taxRate", temp.getTaxRate());
                    
                    tempAsJSON.put("dateCreated", temp.getDateCreated().toString());
                    
                    Person createdById = ClothoAdapter.getPerson(temp.getCreatedById(), clothoObject);
                    tempAsJSON.put("createdById", createdById.getEmailId());
                    
                    tempAsJSON.put("creatorName", createdById.getFirstName() + " " + createdById.getLastName());
                  
                    tempAsJSON.put("products", new JSONArray(temp.getProducts())); //cart item ids and quantity
                    
                    
                    tempAsJSON.put("budget", temp.getBudget());
                    String approvedByEmail = "";
                    String approvedByName = "";
                    if (!temp.getApprovedById().equals("") && temp.getApprovedById().equals("Not Set")){
                        Person approver = ClothoAdapter.getPerson(temp.getApprovedById(), clothoObject);
                        approvedByEmail = (approver).getEmailId();
                        approvedByName  = approver.getFirstName() + " " + approver.getLastName();
                    }
                    tempAsJSON.put("approvedById", approvedByEmail);
                    tempAsJSON.put("approvedByName", approvedByName);
                    
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = temp.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        String receivedByID = "";
                        String receivedByFullName= "";
                        JSONObject receiverJSON = new JSONObject();
                        if (!receivedBys.get(i).equals("") && !receivedBys.get(i).equals("Not Set")){
                            Person receiver = ClothoAdapter.getPerson(receivedBys.get(i), clothoObject);
                            
                            receivedByID = receiver.getEmailId();
                            receivedByFullName = receiver.getFirstName() + " " + receiver.getLastName();
                            receiverJSON.put("receiverId", receivedByID);
                            receiverJSON.put("receiverName",receivedByFullName);
                          
                        }
                        receivedByIds.put(receiverJSON);
                    }
                    tempAsJSON.put("limit", temp.getMaxOrderSize());
                    tempAsJSON.put("receivedByIds", receivedByIds);
                    tempAsJSON.put("relatedProjectId", temp.getRelatedProjectId());
                    tempAsJSON.put("relatedProjectName", (ClothoAdapter.getProject(temp.getRelatedProjectId(), clothoObject)).getName());
                    tempAsJSON.put("status", temp.getStatus());
                    //this one should never be not set 
                    tempAsJSON.put("affiliatedLabName", (ClothoAdapter.getLab(temp.getAffiliatedLabId(), clothoObject)).getName());
                    tempAsJSON.put("affiliatedLabId", (temp.getAffiliatedLabId()));
                    tempAsJSON.put("id", temp.getId());
                    createdOrdersJSON.put(tempAsJSON); // put it in there...
                    }
                }
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(createdOrdersJSON);
                out.flush();
                
                
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "id provided does not exist");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
            }
            
            
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing an id to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
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
