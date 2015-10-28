/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;



import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.dom.Person;
import org.json.JSONObject;


// IMPORT PROJECT FILE HERE

/**
 *
 * @author anna_g
 */
public class createPerson extends HttpServlet {
    private Object EmailSaltHasher;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    //processes requests for projects and created "Project" objet
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                
                System.out.println("got an a new Person request here!");
                
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String password = request.getParameter("password");
                String emailId = request.getParameter("emailId");
                // create order object
                
                // create a result object and send it to the frontend
                JSONObject result = new JSONObject();
                result.put("success",1);
                result.put("firstName", firstName);
                result.put("lastName", lastName);
                result.put("emailId",emailId);
                result.put("password Before Hash", password);
                PrintWriter writer = response.getWriter();
                writer.println(result);
                writer.flush();
                writer.close();
            }
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
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String emailId = request.getParameter("emailId");
        
        //EmailSaltHasher salty = new EmailSaltHasher();
        
        
        
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
