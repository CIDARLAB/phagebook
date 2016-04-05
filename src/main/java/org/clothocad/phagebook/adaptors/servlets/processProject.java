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
import java.util.Arrays;
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
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.IOUtils;

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
      
      // create project first to modify at the end according to 
      // the other objects' ids
      Project project = new Project();
      project.setName(projectName);
      
      String projectID = ClothoAdapter.createProject(project, clothoObject); 
        
   
      clothoObject.logout();
      Object membersObj = request.getParameter("members");
      String membersString  = membersObj != null ? (String) membersObj : "" ;
      String[] membersNames = membersString.split(", ");

      System.out.println(membersString);
      // now parse the string into an array of names
//      ArrayList<String> membersN= new ArrayList<String>(Arrays.asList(membersNames));
      ArrayList<String> membersIDs = new ArrayList<String>();
      for(int i=0;i<membersNames.length;i++){
//        // get first and last names
        String fullName = membersNames[i];
        System.out.println(fullName);
        String[] splitted = fullName.split("\\s+");
        String memberFirstName = splitted[0];
        String memberLastName = splitted[1];
        System.out.println("Splitted and joined name is: " +memberFirstName
        + " "+ memberLastName);
//        // at some point may want to query for 
//        // the members
        Person member = new Person();
        member.setFirstName(memberFirstName);
        member.setLastName(memberLastName);
//        // create a project list for every member and add our
//        // project's id to that list
        List<String> projectList = new ArrayList<String>();
        projectList.add(projectID);
        member.setProjects(projectList);
//        
        String memberID = ClothoAdapter.createPerson(member, clothoObject);
        System.out.println("New member has been created and his ID is:");
        System.out.println(memberID);
          // add member's own Id to its object and update
        member.setId(memberID);
        memberID = ClothoAdapter.setPerson(member, clothoObject);
//        // now add the member'd id to the array of member ids to be
//        // later attached to the project
        membersIDs.add(memberID);
//        
      }
      for(int i=0;i<membersIDs.size();i++){
        System.out.println("The ID "+i+" in membersIDs is: "+ membersIDs.get(i));
        
      }
      System.out.println("Now getting lead!");
      Object leadIdObj = request.getParameter("leadID");
      String leadId  = leadIdObj != null ? (String) leadIdObj : "" ;
      System.out.println(leadId);
      if(!leadId.equals("0")){
          System.out.println("Lead exists in the database!");
          // we know that this person exists in the database
          Person lead = ClothoAdapter.getPerson(leadId, clothoObject);
          System.out.println(lead.getFirstName()+" "+lead.getLastName());
          String leadFullName = request.getParameter("leadName");
          System.out.println(leadFullName);
          System.out.println(lead.getFirstName()+" "+lead.getLastName());
          
          List<String> leadProjects = lead.getProjects();
          leadProjects.add(projectID);
          lead.setProjects(leadProjects);
//          
//          List<String> leadProjectsNew = lead.getProjects();
//          
//          System.out.println("Old size is:");
//          System.out.println(leadProjectsOld.size());
//          System.out.println("New size is:");
//          System.out.println(leadProjectsNew.size());
          // compare length, just to check
//          if(leadProjectsOld.size() < leadProjectsNew.size()){
//            System.out.println("all good! Lead has more projects than"
//                    + "he or she had before!!!");
//          }else{
//            System.out.println("nope! lead's projects were not changed!");
//          }
          
          String leadId2 = ClothoAdapter.setPerson(lead, clothoObject);
          project.setLeadId(leadId2);
       }else{
        // we want to use lead's name passed in from the form!
        Object leadFName = request.getParameter("leadFirstName");
        String leadStringFirstName  = leadFName != null ? (String) leadFName : "" ;
        Object leadLName = request.getParameter("leadLastName");
        String leadStringLastName  = leadLName != null ? (String) leadLName : "" ;
        // gets the lead's name
          System.out.println("lead's FirstName is:");
          System.out.println(leadStringFirstName);
          System.out.println("lead's LastName is:"); 
          System.out.println(leadStringLastName);
          // now create a new person lead and attach the project to him/her
          // and vice versa
          Person lead = new Person();
          lead.setFirstName(leadStringFirstName);
          lead.setLastName(leadStringLastName);
//          clothoObject.logout();
          String leadPersonID = ClothoAdapter.createPerson(lead, clothoObject);
          lead.setId(leadPersonID);
          List<String> projectList = new ArrayList<String>();
          projectList.add(projectID);
          lead.setProjects(projectList);
          leadPersonID = ClothoAdapter.setPerson(lead,clothoObject);
          project.setLeadId(leadPersonID);
      }
              
        
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
       String rand =  request.getParameter("emailID");
       System.out.println("***");
       System.out.println(rand);
       System.out.println("***");
      if(request.getParameter("emailID")!=null){
        System.out.println("creator exists");
        creatorID = request.getParameter("emailID");
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

      System.out.println("About to create a new project.");
      // create and set the fields for a new project
        project.setBudget(projectBudget);
        project.setCreatorId(creatorID);
        project.setGrantId(grantID);
        project.setAffiliatedLabs(labsList);
        project.setDescription(description);

      clothoObject.login(loginMap);
      projectID = ClothoAdapter.setProject(project, clothoObject);   
      
      // attach the project id to the list of the projects of according members
      clothoObject.logout();
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

//      clothoObject.logout();
      conn.closeConnection();


      if(projectID != null){
        result.put("success",1);
        result.put("projectId", projectID);
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
