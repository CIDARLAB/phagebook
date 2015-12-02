                                                                      /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.model.Person;
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet verifyEmail</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet verifyEmail at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
            
            
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
        String emailId = "";
        String salt = "";
        boolean hasValidParameters = false;
        salt = request.getParameter("salt");
        emailId = request.getParameter("emailId");
        if (salt != "" && emailId != ""){
            hasValidParameters = true;
        }
        if (hasValidParameters){
            
            EmailSaltHasher salty = EmailSaltHasher.getEmailSaltHasher();
            Map query = new HashMap();
            List<Person> queryPersons = new LinkedList<Person>();
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
           
          
            queryPersons = ClothoAdaptor.queryPerson(query, clothoObject);
            
            String recreatedHash = salty.hash(emailId.toCharArray(), salt.getBytes("UTF-8"));
            //System.out.println(salty.hash(people.get(0).getEmailId().toCharArray(), people.get(0).getSalt().getBytes("UTF-8"))
            
            boolean isValidated = queryPersons.get(0).getSaltedEmailHash().equals(recreatedHash);
            System.out.println("is Validated = " + isValidated);
            System.out.println("retrieved Hash = " + queryPersons.get(0).getSaltedEmailHash());
            System.out.println("recreatedHash = " + recreatedHash);
            if (isValidated){
                System.out.println("-------I'm at isValidated--------");
                queryPersons.get(0).setActivated(true);
                clothoObject.logout();
                ClothoAdaptor.setPerson(queryPersons.get(0), clothoObject);
                
                
            }
            clothoObject.logout();
            
            
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
