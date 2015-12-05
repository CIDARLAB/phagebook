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
import org.clothocad.model.Person;
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
      System.out.println("got here1!"); 
      System.out.println(request);
      System.out.println("got here2!"); 


       // who is the user ?
       // get all of the fields from the request
       String name = request.getParameter("name");
       System.out.println("name is"); 
       System.out.println(name);
       
       String leadString = null;

       leadString = request.getParameter("lead");
       System.out.println("lead is"); 
       System.out.println(leadString);
       String labs = request.getParameter("labs");
       
       double projectBudget = 0;
       
       String projectBudgetfromJSON = request.getParameter("projectBudget");
       
       if(projectBudgetfromJSON != null){
         projectBudget = Double.parseDouble(projectBudgetfromJSON);
       }
       
       String grantString = request.getParameter("grant");
       String description = request.getParameter("description");
       System.out.println("description is");
       System.out.println(description);
       
       String date = request.getParameter("date");
       System.out.println("date is");
       System.out.println(date);

       // a sample user
       Person creator = new Person();
       creator.setFirstName("Leela");
       creator.setId("Leela");
       /*
       Person creator2 = new Person();
       creator2.setFirstName("Anna");
       
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
       
       String creator2ID = ClothoAdaptor.createPerson( creator2, clothoObject);
       
       */
       /*
       
       in cookie clotho ID (eventually) 
       ClothoAdapter.getPerson( clothoID, clothoObject);
       
       */
       
       // create a lead object using the name from the form
       Person lead = new Person();
       if(leadString != null)lead.setFirstName(leadString);
       
      
       // create a Grant object -- edit grant class later to allow for 
       // creating grants
       Grant grant = new Grant();
       
       // need to add support for creating and adding a number of labs/ organizations
       // format of passed in lab?
       // for now assume there is only one organization/lab
       Organization lab = new Organization(labs);
       
       // initialize a null string to store the projectID value
       String projectID = null;
       
       System.out.println("creator"); 
       System.out.println(creator); 
       System.out.println("name");
       System.out.println(name);
       System.out.println("lab");
       System.out.println(lab);
       System.out.println("lead");
       System.out.println(lead);
       System.out.println("projectBudget");
       System.out.println(projectBudget);
       System.out.println("grant");
       System.out.println(grant);
       System.out.println("description"); 
       System.out.println(description);
       
       Project newProject = new Project(creator, name, description); 
       projectID = createProjectInClotho(newProject);
        /*
       // use different contructors based on how filled out the form is
       if(lead == null && projectBudget == 0 && labs == null ){
        System.out.println("1");  
        System.out.println(creator); 
        System.out.println(name);
        System.out.println(description); 
        Project newProject = new Project(creator, name, description); 
        projectID = createProjectInClotho(newProject);

       }else if(lead == null && projectBudget == 0) {
        System.out.println("2");  
        System.out.println(creator); 
        System.out.println(name);
        System.out.println(lab);
        System.out.println(description); 
         
        Project newProject = new Project(creator, name, lab, description); 
        projectID = createProjectInClotho(newProject);
      
       }else{
        // here assume that all vals are present
        System.out.println("3");  
        System.out.println(creator); 
        System.out.println(name);
        System.out.println(lab);
        System.out.println(lead);
        System.out.println(projectBudget);
        System.out.println(grant);
        System.out.println(description); 
         
        Project newProject = new Project(creator, name, lab, lead, projectBudget, grant, description); 
        projectID = createProjectInClotho(newProject);
        System.out.println("project id is");
        System.out.println(projectID);
       
       }
       
      */ 
      System.out.println("almost done"); 
      System.out.println(projectID);
      JSONObject result = new JSONObject();
 
      
      if(projectID != null){
        result.put("success",1);
        result.put("projectID", projectID);
        System.out.println("result is: ");
        System.out.println(result);
      }else{
         System.out.println("not successful"); 
      }
      
      PrintWriter writer = response.getWriter();
      writer.println(result);
      writer.flush();
      writer.close();
    }
    
    // separate method for opening a connection to clotho and saving the project
    // in Clotho;
    // returns the ID given to the project by the database
    public String createProjectInClotho(Project newProject){
      System.out.println("newProject in createProjectInClotho");
      
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
