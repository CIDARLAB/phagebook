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
      //ESTABLISH CONNECTION
      ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
      Clotho clothoObject = new Clotho(conn);
      Map createUserMap = new HashMap();
      String username = "test" + System.currentTimeMillis();
      createUserMap.put("username", username);
      createUserMap.put("password", "password");
      clothoObject.createUser(createUserMap);
      clothoObject.logout();
      Map loginMap = new HashMap();
      loginMap.put("username", "test1468902986260");
      loginMap.put("credentials", "password");
      clothoObject.login(loginMap);
      
      // create project first to modify as progress 
      // through the server
      Project project = new Project();
      String projectId = ClothoAdapter.createProject(project, clothoObject); 
      clothoObject.logout();
      
      System.out.println("in processProject servlet");

      
      JSONObject result = new JSONObject();

      Object prName = request.getParameter("name");
      String projectName  = prName != null ? (String) prName : "" ;
        System.out.println("New Project name is:"); 
        System.out.println(projectName);
      
      project.setName(projectName);
        
      Object creatorId = request.getParameter("emailID");
      String creatorIdStr  = creatorId != null ? (String) creatorId : "" ;
        System.out.println("Creator's ID is:"); 
        System.out.println(creatorIdStr);      
        
      Object leadId = request.getParameter("leadID");
      String leadIdStr  = leadId != null ? (String) leadId : "" ;
        System.out.println("Lead's ID is:"); 
        System.out.println(leadIdStr);
        
      Person creator = ClothoAdapter.getPerson(creatorIdStr, clothoObject);
        System.out.println("Creator is " + creator.getFirstName() +" "+ 
                creator.getLastName()); 
      List<String> projectsCreator = creator.getProjects();
      projectsCreator.add(projectId);
      project.setCreatorId(creatorIdStr);
      ClothoAdapter.setPerson(creator, clothoObject);
        System.out.println("Successfully set creator."); 
      
      if(leadIdStr.compareTo("0") != 0){
        Person lead = ClothoAdapter.getPerson(leadIdStr, clothoObject);
          System.out.println("Lead is " + lead.getFirstName() +" "+ 
                  lead.getLastName()); 
        List<String> projectsLead = lead.getProjects();
        projectsLead.add(projectId);
        project.setLeadId(leadIdStr);
        ClothoAdapter.setPerson(lead, clothoObject);
          System.out.println("Successfully set lead."); 
      }

      
      Object members = request.getParameter("members");
      String membersStr  = members != null ? (String) members : "" ;
      if(membersStr.compareTo("") != 0){
        String[] membersArr = membersStr.split(",");
          for(int i=0; i<membersArr.length; i++){
            
            System.out.println(membersArr[i]);
            if(membersArr[i].compareTo("0") != 0){
              Person member = ClothoAdapter.getPerson(membersArr[i], clothoObject);
              List<String> projectsMember = member.getProjects();
              projectsMember.add(projectId);
              List<String> projectMembers = project.getMembers();
              projectMembers.add(membersArr[i]);
              ClothoAdapter.setPerson(member, clothoObject);
            }            
          }
      }
            
      Object projectBudget = request.getParameter("projectBudget");
      String projectBudgetStr  = projectBudget != null ? (String) projectBudget : "" ;
        System.out.println("Project's budget is:"); 
        System.out.println(projectBudgetStr);
      project.setBudget(Double.parseDouble(projectBudgetStr));
        
      Object grantObj = request.getParameter("grant");
      String grantStr  = projectBudget != null ? (String) projectBudget : "" ;
        System.out.println("Project's budget is:"); 
        System.out.println(projectBudgetStr);
      // TODO: add option for searching grants
      Grant grant = new Grant();
      grant.setName(grantStr);
      String grantId = ClothoAdapter.createGrant(grant, clothoObject);
      project.setGrantId(grantId);
            
      ClothoAdapter.setProject(project,clothoObject);
      
      

      PrintWriter writer = response.getWriter();
      writer.println(result);
      writer.flush();
      writer.close();
      conn.closeConnection();
    }
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
