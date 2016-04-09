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
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class createLab extends HttpServlet {

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
        
        Object pInstitutionId = request.getParameter("institution");
        String institutionId = pInstitutionId != null ? (String) pInstitutionId: "";
        
        Object pUser = request.getParameter("user");
        String userId = pUser != null ? (String) pUser: "";
        
        Object pName = request.getParameter("name");
        String name = pName != null ? (String) pName : "";
        
        Object pDescription = request.getParameter("description");
        String description = pDescription != null ? (String) pDescription : "";
        
        Object pPhone = request.getParameter("phone");
        String phone = pPhone != null ? (String) pPhone : "";
        
        Object pUrl  = request.getParameter("url");
        String url = pUrl != null ? (String) pUrl : "";
        
        boolean isValid = false;
        
        if (!institutionId.equals("") && !name.equals("") && !description.equals("")
                && !phone.equals("") && !url.equals("")){
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
            Institution inst = ClothoAdapter.getInstitution(institutionId, clothoObject);
            
            Lab lab = new Lab();
            lab.setName(name);
            lab.setUrl(url);
            lab.setPhone(phone);
            lab.setDescription(description);
            lab.setInstitution(institutionId);
            
            List<String> leadPIList = new ArrayList<>();
            leadPIList.add(userId);
            lab.setLeadPIs(leadPIList);
            
            ClothoAdapter.createLab(lab, clothoObject);
            
            List<String> labs = inst.getLabs();
            labs.add(lab.getId());
            inst.setLabs(labs);
            ClothoAdapter.setInstitution(inst, clothoObject);
            JSONObject responseJSON = new JSONObject();
            
            
            
            
            
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            responseJSON.put("labId", lab.getId());
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
        else{
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters");
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
        return "creates a lab in clotho and puts the logged in user as a PI";
    }// </editor-fold>

}
