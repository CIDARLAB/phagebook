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
        System.out.println("got request here!");

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

      System.out.println("request is: ");


       // who is the user ?
       // get all of the fields from the request
       String name = request.getParameter("name");
       String leadString = request.getParameter("lead");
       String labs = request.getParameter("labs");
       
       double projectBudget = Double.parseDouble(request.getParameter("projectBudget"));
       String grantString = request.getParameter("grant");
       String description = request.getParameter("description");
       String date = request.getParameter("date");

       // a sample user
       Person creator = new Person();
       creator.setFirstName("Leela");
       creator.setId("Leela");
       
       // create a lead object using the name from the form
       Person lead = new Person();
       creator.setFirstName(leadString);
       creator.setId("Leela");
      
       // create a Grant object -- edit grant class later to allow for 
       // creating grants
       Grant grant = new Grant();
       
       // need to add support for creating and adding a number of labs/ organizations
       // format of passed in lab?
       // for now assume there is only one organization/lab
       Organization lab = new Organization(labs);
       
       // initialize a null string to store a projectID value
       String projectID = null;
       
       // use different contructors based on how filled out the form is
       if(lead == null && projectBudget == 0 && labs == null ){

        Project newProject = new Project(creator, name, description); 
        projectID = createProjectInClotho(newProject);

       }else if(lead == null && projectBudget == 0) {
         
        Project newProject = new Project(creator, name, lab, description); 
        projectID = createProjectInClotho(newProject);
      
       }else{
        // here assume that all vals are present
        Project newProject = new Project(creator, name, lab, lead, projectBudget, grant, description); 
        projectID = createProjectInClotho(newProject);
       
       }
       
      JSONObject res = new JSONObject();
 
      if(projectID != null){
        res.put("projectID", projectID);
      }else{
        // how to say that the save was not successful?
      }


      PrintWriter writer = response.getWriter();
      writer.println(request.toString());
      writer.flush();
      writer.close();
    }
    
    // separate method for opening a connection to clotho and saving the project
    // in Clotho;
    // returns the ID given to the project by the database
    private String createProjectInClotho(Project newProject){
        
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

      String projectID = ClothoAdaptor.createProject(newProject, clothoObject);

      conn.closeConnection();
      
      return projectID;
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
