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
      System.out.println(request);

       // who is the user ?
       // get all of the fields from the request
       String name = request.getParameter("name");
       System.out.println("(91) name is"); 
       System.out.println(name);  
       
       // gets the lead's name
       String lead = "";
       String leadString = request.getParameter("lead");
       System.out.println("(97) leadString is: " + leadString);  
       String[] leadName = new String[2];
       if(!leadString.equals("")){
         System.out.println("(100) Lead name is not null");  
         lead = leadString;
         if(leadString.contains(" ")){
           leadName = leadString.split(" ");
           System.out.println("(104) Split lead name is " + leadName[0] + " " + leadName[1]); 
           
         }
       }       

       System.out.println("(109) lead is"); 
       System.out.println(lead);
       
       // gets the labs 
       String labs = "";
       String labsString = request.getParameter("labs");
       
       if(labsString != null){
         labs = labsString;        
       }
       System.out.println("(119) labs is"); 
       System.out.println(labs);
          
      // gets the project budget from the form
       double projectBudget = 0; 
       String projectBudgetfromJSON = request.getParameter("projectBudget");
       
       if(projectBudgetfromJSON != null ){
         projectBudget = Double.parseDouble(projectBudgetfromJSON);
       }  
       
       String grant = "";
       String grantString = request.getParameter("grant");
       if(grantString != null){
         grant = grantString;        
       }
       System.out.println("(135) grant is"); 
       System.out.println(grant);
       
       String description = "";
       String descriptionString = request.getParameter("description");
       if(descriptionString != null ){
         description = descriptionString;        
       }
       System.out.println("(143) description is"); 
       System.out.println(description);
       
       String date = request.getParameter("date");
       System.out.println("(147) date is");
       System.out.println(date);

       ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
       Clotho clothoObject = new Clotho(conn);
       String creatorId = request.getParameter("id");
       
       System.out.println("(151) id of the creator is: " + creatorId);
       Person creator = ClothoAdapter.getPerson(creatorId, clothoObject); 
       // a sample user
//       Person creator = new Person();
//       creator.setFirstName("Anna");
//       creator.setEmailId("abcdejkhf@gmail.com");
//       creator.setPassword("12345786678");;
          
       // create a lead object using the name from the form\
       // set the lea's name to name from the form
       // ***** have to add split string code for creating first and last name


          
       // create a Grant object -- edit grant class later to allow for 
       // creating grants
       Grant grantObject = new Grant(name);
       
       // need to add support for creating and adding a number of labs/ organizations
       // format of passed in lab?
       // for now assume there is only one organization/lab
       Organization lab = new Organization(labs);
       
       // initialize a null string to store the projectID value
       String projectID = "";
       
       System.out.println("creator"); 
       System.out.println(creator); 
       System.out.println("name");
       System.out.println(name);
       System.out.println("lab");
       System.out.println(lab);
       System.out.println("projectBudget");
       System.out.println(projectBudget);
       System.out.println("grant");
       System.out.println(grantObject);
       System.out.println("description"); 
       System.out.println(description);
      
//      System.out.println("about to create the creator");
//      String creatorID = ClothoAdapter.createPerson(creator, clothoObject);    

      String username = creator.getEmailId();
      String password = creator.getPassword();
      

       
//      Map createUserMap = new HashMap();
//      createUserMap.put("username", username);
//      createUserMap.put("password", password);
//      clothoObject.createUser(createUserMap);
      Map loginMap = new HashMap();
      loginMap.put("username", username);
      loginMap.put("credentials", password);
      clothoObject.login(loginMap);
      
      
      Person leadPerson = new Person();
      leadPerson.setFirstName(leadName[0]);
      leadPerson.setLastName(leadName[1]);
      System.out.println("Lead Person is " + leadPerson);
      String leadPersonID = ClothoAdapter.createPerson(leadPerson, clothoObject); 
//      creator = ClothoAdapter.getPerson(creatorID, clothoObject);
      
      System.out.println("about to create the project");

      Project project = new Project(creator, name, description);            
      projectID = ClothoAdapter.createProject(project, clothoObject); 
      
      clothoObject.logout();
      conn.closeConnection();

      System.out.println("project id is");
      System.out.println(projectID);
//      System.out.println("creator id is");
//      System.out.println(creatorID);

      JSONObject result = new JSONObject();
 
      
      if(projectID != null){
        result.put("success",1);
        result.put("projectID", projectID);
        System.out.println("successful");     
      }else{
        result.put("error",1);
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
