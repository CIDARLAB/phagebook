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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class listOrdersOfPerson extends HttpServlet {

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
                
                List<String> approvedOrderIds = prashant.getApprovedOrders();
                List<String> deniedOrderIds   = prashant.getDeniedOrders();
                JSONArray allOrders = new JSONArray();
                for (String approved : approvedOrderIds){
                    System.out.println("APPROVED ID: "+approved);
                    Order approvedOrder = ClothoAdapter.getOrder(approved, clothoObject);
                    
                   
                    JSONObject approvedJSON = new JSONObject();
                    approvedJSON.put("name", approvedOrder.getName());
                    approvedJSON.put("dateApproved", approvedOrder.getDateApproved());
                    approvedJSON.put("description", approvedOrder.getDescription());
                    approvedJSON.put("clothoId", approvedOrder.getId());
                    approvedJSON.put("dateCreated", approvedOrder.getDateCreated().toString());
                    Person creator = ClothoAdapter.getPerson(approvedOrder.getCreatedById(), clothoObject);
                    approvedJSON.put("createdById", creator.getEmailId());
                    approvedJSON.put("createdByName", creator.getFirstName() + " " +creator.getLastName());
                    approvedJSON.put("products", approvedOrder.getProducts());
                    approvedJSON.put("orderLimit", approvedOrder.getMaxOrderSize());
                    approvedJSON.put("taxRate", approvedOrder.getTaxRate());
                    approvedJSON.put("budget", approvedOrder.getBudget());
                    System.out.println(approvedOrder.getApprovedById());
                    if (!approvedOrder.getApprovedById().equals("") && !approvedOrder.getApprovedById().equals("Not Set") ){
                        approvedJSON.put("approvedById", (ClothoAdapter.getPerson(approvedOrder.getApprovedById(), clothoObject)).getEmailId());
                    }
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = approvedOrder.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        if (!receivedBys.get(i).equals("") && !receivedBys.get(i).equals("Not Set")){
                        JSONObject receivedByJSON = new JSONObject();
                        receivedByJSON.put( ""+ i , (ClothoAdapter.getPerson(receivedBys.get(i), clothoObject)).getEmailId());
                        receivedByIds.put(receivedByJSON);
                        }
                    }
                    approvedJSON.put("receivedByIds", receivedByIds);
                    approvedJSON.put("relatedProjectName", (ClothoAdapter.getProject(approvedOrder.getRelatedProjectId(), clothoObject)).getName());
                    approvedJSON.put("status", approvedOrder.getStatus());
                    approvedJSON.put("affiliatedLabId", approvedOrder.getAffiliatedLabId());
                    
                    
                    allOrders.put(approvedJSON);
                }
                
                for (String denied : deniedOrderIds){
                    Order deniedOrder = ClothoAdapter.getOrder(denied, clothoObject);
                    
                    JSONObject deniedJSON = new JSONObject();
                    deniedJSON.put("name", deniedOrder.getName());
                    deniedJSON.put("description", deniedOrder.getDescription());
                    deniedJSON.put("clothoId", deniedOrder.getId());
                    deniedJSON.put("dateCreated", deniedOrder.getDateCreated().toString());
                    Person creator = ClothoAdapter.getPerson(deniedOrder.getCreatedById(), clothoObject);
                    deniedJSON.put("createdById", creator.getEmailId());
                    deniedJSON.put("createdByName", creator.getFirstName() + " " +creator.getLastName());
                    deniedJSON.put("products", deniedOrder.getProducts());
                    deniedJSON.put("orderLimit", deniedOrder.getMaxOrderSize());
                    deniedJSON.put("taxRate", deniedOrder.getTaxRate());
                    deniedJSON.put("budget", deniedOrder.getBudget());
                    if (!deniedOrder.getApprovedById().equals("") && !deniedOrder.getApprovedById().equals("Not Set") ){
                        deniedJSON.put("approvedById", (ClothoAdapter.getPerson(deniedOrder.getApprovedById(), clothoObject)).getEmailId());
                    }
                    JSONArray receivedByIds = new JSONArray();
                    List<String> receivedBys = deniedOrder.getReceivedByIds();
                    for (int i = 0; i< receivedBys.size() ; i++){
                        if (!receivedBys.get(i).equals("") && !receivedBys.get(i).equals("Not Set")){
                        JSONObject receivedByJSON = new JSONObject();
                        receivedByJSON.put( ""+ i , (ClothoAdapter.getPerson(receivedBys.get(i), clothoObject)).getEmailId());
                        receivedByIds.put(receivedByJSON);
                        }
                    }
                    deniedJSON.put("receivedByIds", receivedByIds);
                    deniedJSON.put("relatedProjectName", (ClothoAdapter.getProject(deniedOrder.getRelatedProjectId(), clothoObject)).getName());
                    deniedJSON.put("status", deniedOrder.getStatus());
                    deniedJSON.put("affiliatedLabId", deniedOrder.getAffiliatedLabId());
                    
                    
                    allOrders.put(deniedJSON);
                }
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(allOrders);
                out.flush();
                        
            }else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "id provided does not exist");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
            }
            conn.closeConnection();
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
