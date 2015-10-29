/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.dom.Project;
import org.json.JSONObject;

// IMPORT PROJECT FILE HERE

/**
 *
 * @author anna_g
 */
public class processProject extends HttpServlet {

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
        
        System.out.println("got request here!");
                
                // get all of the fields from the request
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                
                // how to transfer date from the website? Maybe just initialize the
                // date at the server
                // get the name of the person who created the project
                
                /*
                Double projectBudget = Double.parseDouble(request.getParameter("projectBudget"));
                Organization lab = new Organization(request.getParameter("lab"));
                Grant projectGrant = new Grant(request.getParameter("projectGrant"));
                
                
                // or get the object from the server?
                
                */
                Person creator = new Person();
                creator.setFirstName("Leela");
                creator.setId("Leela");
                // create project object
                //Project project = new Project( createdDate, creator,  name,  lab,
                //        lead, projectBudget, projectGrant,  description );
                
                Project project = new Project(creator, name,  description);
                
                ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
                Clotho clothoObject = new Clotho(conn); 
                String username = "phagebook";
                Map createUserMap = new HashMap();
                createUserMap.put("username", username);
                createUserMap.put("password", "password");
                clothoObject.createUser(createUserMap);
                Map loginMap = new HashMap();
                loginMap.put("username", username);
                loginMap.put("credentials", "password");

                clothoObject.login(loginMap);
                
                ClothoAdaptor.createProject(project, clothoObject);
                conn.closeConnection();
                System.out.println("hey");
                
                JSONObject res = new JSONObject();
                String result = "creator is " + creator.getFirstName() + " description is " + project.getDescription();
                System.out.println(result);
                res.put(result,1);
                
                PrintWriter writer = response.getWriter();
                writer.println(res.toString());
                writer.flush();
                writer.close();
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
