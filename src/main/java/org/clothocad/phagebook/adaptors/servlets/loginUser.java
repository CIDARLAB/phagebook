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

/**
 *
 * @author Allison Durkan
 */
public class loginUser extends HttpServlet {

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
        //response.setContentType("text/html;charset=UTF-8");
        //try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

        //}
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");  
        boolean isValidRequest = false;
        if((!email.isEmpty()) && (!password.isEmpty())){
            isValidRequest = true;
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        if(isValidRequest){
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map loginMap = new HashMap();
            loginMap.put("username", email);
            loginMap.put("credentials", password);
            
            Map id = (Map) clothoObject.login(loginMap);
            
            Map clothoQuery = new HashMap();
            clothoQuery.put("emailId", email);
            Person loggedInPerson = ClothoAdapter.queryPerson(clothoQuery, clothoObject).get(0);
          
            System.out.println("GOT HERE IN LOGIN PERSON");
            
            
            if (loggedInPerson.isActivated() )
            {
              
                //return success, this means its a valid request
                //response.setStatus(HttpServletResponse.SC_OK);
                System.out.print("Id is " + loggedInPerson.getId());
                String idVal = (String) loggedInPerson.getId();
                response.setContentType("text/plain");
                PrintWriter out = response.getWriter();
                out.print(idVal);
                out.flush();
                out.close();
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = response.getWriter();
                out.print("false");
                out.flush();
                out.close();
            }
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
