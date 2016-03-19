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
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Vendor;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class createVendor extends HttpServlet {

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
        
        
        //get all necessary fields to create 
        //REQUIRING NAME & DESCRIPTION & CONTACT 
        boolean isValid = false; 
        
        String name = request.getParameter("name") != null ? request.getParameter("name") : "";
        
        String description = request.getParameter("description") != null ? request.getParameter("description") : "";
        
        String contact = request.getParameter("contact") != null ? request.getParameter("contact"):"";
        
        
        String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "" ;
        String url = request.getParameter("url") != null ? request.getParameter("url") : "" ;
        
        
        if (!name.isEmpty() && !description.isEmpty() && !contact.isEmpty())
        {
            isValid = true;
            
            
        } else 
        {
            
        }
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            //TODO: we need to have an authentication token at some point

            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password); 
            clothoObject.login(loginMap);
        
            
            Vendor vendor = new Vendor();
            vendor.setName(name);
            vendor.setDescription(description);
            vendor.setContact(contact);
            
            if (!phone.isEmpty())
            {
                vendor.setPhone(phone);
            }
            if (!url.isEmpty())
            {
                vendor.setUrl(url);
            }
            
             //everything is set for that product
            ClothoAdapter.createVendor(vendor, clothoObject);
            JSONObject vendorJSON = new JSONObject();
            vendorJSON.put("id", vendor.getId());
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(vendorJSON);
                out.flush();
                out.close();
                
            clothoObject.logout();
            
            
        }else
        {
            JSONObject msg = new JSONObject();
            msg.put("message", "Need to send name, description, and contact");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(msg);
                out.flush();
                out.close();
        }
        
    }

}
