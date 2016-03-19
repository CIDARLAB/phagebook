                                                                      /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.S3Adapter;
import org.clothocad.phagebook.security.EmailSaltHasher;

/**
 *
 * @author Herb
 */
public class verifyEmail extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
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
        String emailId ;
        String salt ;
        boolean hasValidParameters = false;
        salt = request.getParameter("salt");
        emailId = request.getParameter("emailId");
        if (!salt.isEmpty() && !emailId.isEmpty()){
            hasValidParameters = true;
        }
        if (hasValidParameters){
            
            EmailSaltHasher salty = EmailSaltHasher.getEmailSaltHasher();
            Map query = new HashMap();
            List<Person> queryPersons = new LinkedList<>();
            query.put("emailId", emailId);
            
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            createUserMap.put("username", "ClothoBackend");
            createUserMap.put("password", "phagebook");

           
            clothoObject.createUser(createUserMap);

            Map loginMap = new HashMap();
            loginMap.put("username", "ClothoBackend");
            loginMap.put("credentials", "phagebook");


            clothoObject.login(loginMap);
           
           
          
            queryPersons = ClothoAdapter.queryPerson(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
            
            byte[] recreatedHash = salty.hash(emailId.toCharArray(), salt.getBytes("UTF-8"));
           
            boolean isValidated = salty.isExpectedPassword(emailId.toCharArray(), salt.getBytes("UTF-8"), queryPersons.get(0).getSaltedEmailHash());
            
       
            
            if (isValidated){
                Person pers = queryPersons.get(0);
                S3Adapter.initializeUserFolder(pers);//queryPersons.get(0).getId()
                
                System.out.println("User "  + queryPersons.get(0).getEmailId() + " has been validated");
               
                pers.setActivated(true);
                clothoObject.logout();
                ClothoAdapter.setPerson(pers, clothoObject);  
                System.out.println("HERE AT VERIFY EMAIL: "+ ClothoAdapter.getPerson(pers.getId(), clothoObject).isActivated());
                
                
                
            } else if (!isValidated){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            clothoObject.logout();
            
            
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
