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
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class loadPhagebookInstitutions extends HttpServlet {

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
        
        //always valid 
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
        
        Map query = new HashMap(); // blank map to get all objects of that class
        
        List<Institution> institutions = ClothoAdapter.queryInstitution(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
        
        JSONArray institutionsInClotho = new JSONArray();
        int countInstits = 0;
        int countLabs=0;
        for (Institution institute : institutions){
            countInstits++;
            JSONArray labs = new JSONArray();
            for (String labId : institute.getLabs()){
                countLabs++;
                Lab lab = ClothoAdapter.getLab(labId, clothoObject);
                JSONObject obj = new JSONObject();
                obj.put("labName", lab.getName());
                obj.put("labId", lab.getId());
                labs.put(obj);
            }
            JSONObject institution = new JSONObject();
            institution.put("institutionId", institute.getId());
            institution.put("institutionName", institute.getName());
            institution.put("labs", labs);
            institutionsInClotho.put(institutions);
            
        }
        
        
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();
        obj.put("message", "found " + countLabs + " labs and " + countInstits +" institutions" );
        obj.put("institutions", institutionsInClotho);
        out.print(obj);
        out.flush();
        out.close();
        
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
