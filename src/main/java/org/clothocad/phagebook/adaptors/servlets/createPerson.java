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
import org.clothocad.phagebook.adaptors.EmailHandler;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.model.Person;
import org.json.JSONObject;
import org.clothocad.phagebook.security.EmailSaltHasher;


// IMPORT PROJECT FILE HERE

/**
 *
 * @author anna_g
 */
public class createPerson extends HttpServlet {
    

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
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
       
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String emailId = request.getParameter("emailId");
        
        Person createdPerson = new Person();
        createdPerson.setFirstName(firstName);
        createdPerson.setLastName(lastName);
        createdPerson.setEmailId(emailId);
        createdPerson.setPassword(password);
        
        
        EmailSaltHasher salty = EmailSaltHasher.getEmailSaltHasher();
        String salt = EmailSaltHasher.csRandomAlphaNumericString();
        createdPerson.setSalt(salt);
        
        byte[] SaltedHashedEmail = salty.hash(emailId.toCharArray(), salt.getBytes("UTF-8"));

        createdPerson.setSaltedEmailHash(SaltedHashedEmail);
        boolean isUnique = false;
        Map clothoQuery = new HashMap();
        clothoQuery.put("emailId", createdPerson.getEmailId());
        List<Person> people = ClothoAdapter.queryPerson(clothoQuery, clothoObject);
        
        if (people.isEmpty()){
            isUnique = true;
        }
           
      
        if (isUnique){
            clothoObject.logout();
            ClothoAdapter.createPerson(createdPerson, clothoObject);

            EmailHandler emailer = EmailHandler.getEmailHandler();
            String link = Args.phagebookBaseURL + "/html/verifyEmail.html?emailId=" +createdPerson.getEmailId() + "&salt=" + createdPerson.getSalt() ;
            emailer.sendEmailVerification(createdPerson, link);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/JSON");
                PrintWriter out = response.getWriter();
                JSONObject obj = new JSONObject();
                obj.put("id", createdPerson.getEmailId());
                out.print(obj);
                out.flush();
                out.close();
            

        } else {
            System.out.println("User is not unique in Clotho");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.setContentType("application/JSON");
                PrintWriter out = response.getWriter();
                JSONObject obj = new JSONObject();
                obj.put("message", "Person Already Exists");
                out.print(obj);
                out.flush();
                out.close();
        }
        
        
        
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
