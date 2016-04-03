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
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
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
    
    //processes requests for projects and created "Project" object
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
      ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
      Clotho clothoObject = new Clotho(conn);
      Map createUserMap = new HashMap();
      String username = "username";
      String password = "password";

      createUserMap.put("username", username);
      createUserMap.put("password", password);

      clothoObject.createUser(createUserMap);
      Map loginMap = new HashMap();
      loginMap.put("username", username);
      loginMap.put("credentials", password);  

      clothoObject.login(loginMap);
      
      JSONObject result = new JSONObject();



      Object prName = request.getParameter("name");
      String projectName  = prName != null ? (String) prName : "" ;
        System.out.println("New Project name is:"); 
        System.out.println(projectName);  

      
      Object mFName = request.getParameter("memberFirstName");
      String memberFirstName  = mFName != null ? (String) mFName : "" ;
      Object mLName = request.getParameter("memberFirstName");
      String memberLastName  = mLName != null ? (String) mLName : "" ;
        System.out.println("Member's first name is:"); 
        System.out.println(memberFirstName);
        System.out.println("Member's last name is:"); 
        System.out.println(memberLastName);
        
        
      Object leadFName = request.getParameter("leadFirstName");
      String leadStringFirstName  = leadFName != null ? (String) leadFName : "" ;
      Object leadLName = request.getParameter("leadLastName");
      String leadStringLastName  = leadLName != null ? (String) leadLName : "" ;
      // gets the lead's name
        System.out.println("lead's FirstName is:");
        System.out.println(leadStringFirstName);
        System.out.println("lead's LastName is:"); 
        System.out.println(leadStringLastName);

        
      Object labsName = request.getParameter("labs");
      String labs  = labsName != null ? (String) labsName : "" ;
        System.out.println("Labs is:"); 
        System.out.println(labs);

        
      String projectBudgetVal = request.getParameter("projectBudget");
      System.out.println((String)projectBudgetVal);
      double projectBudget = 0.0;
      if(!projectBudgetVal.equals("")  && projectBudgetVal != null){
         result.put("budget", "1");
        projectBudget =Double.parseDouble((String) projectBudgetVal);
        if(projectBudget<0){
          result.put("budget", "0");
          projectBudget = 0;
          
        }
      }
        System.out.println("Budget is:"); 
        System.out.println(projectBudget);
        
        
      Object grantName = request.getParameter("grant");
      String grant  = grantName != null ? (String) grantName : "" ;
        System.out.println("Grant is"); 
        System.out.println(grant);
      
      
      Object descriptionObj = request.getParameter("description");
      String description  = descriptionObj != null ? (String) descriptionObj : "" ;
       System.out.println("Description is"); 
       System.out.println(description);

      
      
      Object leadEmailAdr = request.getParameter("leadEmailId");
      String leadEmailId  = leadEmailAdr != null ? (String) leadEmailAdr : "" ;
        System.out.println("leadEmailId is"); 
        System.out.println(leadEmailId);
        


      Date date1= new java.util.Date();
      System.out.println(new Timestamp(date1.getTime()));
      String date = new Timestamp(date1.getTime()).toString();
      System.out.println(date);


        Person creator;
        String usernameCreator;
        String passwordCreator;
        String creatorID;
       String rand =  request.getParameter("emailId");
       System.out.println("***");
       System.out.println(rand);
       System.out.println("***");
      if(request.getParameter("emailId")!=null){
        System.out.println("creator exists");
        creatorID = request.getParameter("emailId");
        System.out.println("Creator's Id is: " + creatorID);
        creator = ClothoAdapter.getPerson(creatorID, clothoObject);
        usernameCreator = creator.getEmailId();
        passwordCreator = creator.getPassword();

      }else{
        
        System.out.println("(180) Creator does not exist, so creating a new one");
        creator = new Person();
        creator.setFirstName("Anna");
        creator.setLastName("Goncharova");
        creator.setEmailId("anna@gmail.com");
        creator.setPassword("1234567890");
        creator.setActivated(true);
        creatorID = ClothoAdapter.createPerson(creator, clothoObject);
        creator = ClothoAdapter.getPerson(creatorID, clothoObject);
      
      }
      
      // create the Grant object 
      
      // TODO: check whether grant exists.
      Grant grantObject = new Grant();
      grantObject.setName(grant);
      String grantID = ClothoAdapter.createGrant(grantObject, clothoObject);
      grantObject.setId(grantID);
      

      // need to add support for creating and adding a number of labs/organizations
      // format of passed in lab? 
      // ** Send the input data as an array.
      // for now assume there is only one organization/lab
      // TODO: add options for a bunch of labs
      Organization lab = new Organization(labs);
      List<String> labsList = new ArrayList<String>();
      labsList.add(lab.getName());

      

      // TODO: Add form checking for lead and 
      // ajax support for getting lead names and displaying the options.
      // TODO: Check whether lead exists in the DB.
      String testLeadFirstName = "Bob";
      String testLeadLastName = "Smith";
      String testLeadEmail = "bob@smith.com";
      Person leadPerson = new Person();

      leadPerson.setFirstName(testLeadFirstName);
      leadPerson.setLastName(testLeadLastName);
      leadPerson.setEmailId(testLeadEmail);
      
      clothoObject.logout();

      String leadPersonID = ClothoAdapter.createPerson(leadPerson, clothoObject);
      leadPerson.setId(leadPersonID);
      

      System.out.println("About to create a new project.");
      // create and set the fields for a new project
        Project project = new Project();
        project.setName(projectName);
        project.setBudget(projectBudget);
        project.setLeadId(leadPersonID);
        project.setCreatorId(creatorID);
        project.setGrantId(grantID);
        project.setAffiliatedLabs(labsList);
        project.setDescription(description);

      clothoObject.login(loginMap);
      String projectID = ClothoAdapter.createProject(project, clothoObject);   
      
      // attach the project id to the list of the projects of according members
      List<String> leadProjects =leadPerson.getProjects();
      leadProjects.add(projectID);
      // update the lead in clotho
      leadPersonID = ClothoAdapter.setPerson(leadPerson, clothoObject);
      
      List<String> creatorProjects = creator.getProjects();
      creatorProjects.add(projectID);
      
      
      
      // for debugging; display all projects
      for (int i = 0; i < creatorProjects.size(); i++) {
        System.out.println(creatorProjects.get(i));
      }
      
      // update the lead in clotho
      creatorID = ClothoAdapter.setPerson(creator, clothoObject);
      
//      List<String> creatorProjects =leadPerson.getProjects();

       System.out.println("New Project ID is "+ projectID);

      clothoObject.logout();
      conn.closeConnection();


      if(projectID != null){
        result.put("success",1);
        result.put("projectId", projectID);
        result.put("leadId", leadPersonID);
        System.out.println("successful");     
      }else{
        result.put("error",1);
        System.out.println("not successful!"); 
      }

      PrintWriter writer = response.getWriter();
      writer.println(result);
      writer.flush();
      writer.close();
    }
    
    // separate method for opening a connection to clotho and saving the project
    // in Clotho;
    // returns the ID given to the project by the database


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible to processing a 'create new "
                + "project' request";
    }// </editor-fold>

}
