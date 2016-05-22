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
import org.clothocad.phagebook.dom.Lab;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class listPIsOfLab extends HttpServlet {

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
        
       
        Object pLabId = request.getParameter("lab");
        String labId = pLabId != null ? (String) pLabId: "";
        
        boolean isValid = false;
        
        if (!labId.equals("")){
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
            
            
            Lab lab = ClothoAdapter.getLab(labId, clothoObject); //Lab
            if (!lab.getId().equals("")){
                JSONArray PIs = new JSONArray();
                for (String pi : lab.getLeadPIs()){
                    Person piPers = ClothoAdapter.getPerson(pi, clothoObject);
                    JSONObject PI = new JSONObject();
                    PI.put("name", piPers.getFirstName() + " " + piPers.getLastName() );
                    PI.put("email", piPers.getEmailId());
                    PI.put("clothoId", piPers.getId());
                    String firstInstitutionId = (piPers.getInstitutions().size() > 0) ? piPers.getInstitutions().get(0): "None" ;
                    if (!firstInstitutionId.equals("None")){
                        PI.put("institutionName", ClothoAdapter.getInstitution(firstInstitutionId, clothoObject) .getName());
                    }
                    
                    String firstLabId = (piPers.getLabs().size() > 0) ? piPers.getLabs().get(0): "None";
                    if (!firstLabId.equals("None")) {
                        PI.put("labName", ClothoAdapter.getLab(firstLabId, clothoObject).getName());
                    }
                    PIs.put(PI);
                    
                }
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(PIs);
                out.flush();
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Lab not found in Clotho");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
                
            }
            
           conn.closeConnection(); 
        }
        else
        {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Lab Id Cannot Be Blank");
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
