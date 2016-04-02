/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Project;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author anna_g
 */
public class getAllProjects extends HttpServlet {

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
    PrintWriter writer = response.getWriter();

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
    
    // get userId from the cookie
    Object userIDC = request.getParameter("userID");
    String userID  = userIDC != null ? (String) userIDC : "" ;
    System.out.println(userID);
    
    // get person 
    Person user = ClothoAdapter.getPerson(userID, clothoObject);
    
    // get the projects array
    
    List<String> projectsList  =user.getProjects();
    System.out.println(projectsList);
    JSONArray listOfProjects = new JSONArray();

    // get and add each project to the response
    if(projectsList.size()>0){

      for (int i = 0; i <projectsList.size(); i++){
        JSONObject projectObject = new JSONObject();

          System.out.println("Person has a bunch of projects");
        String projectId = projectsList.get(i);

        Project proj =  ClothoAdapter.getProject(projectId, clothoObject);
        String desc = proj.getDescription();
          System.out.println("Description is: \n" + desc);


        projectObject.put("description",desc);
        projectObject.put("budget", proj.getBudget());
        //projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
        projectObject.put("projectName", proj.getName());
        //projectObject.put("notebooks", proj.getNotebooks());
        projectObject.put("updates", proj.getUpdates());
        Grant grant = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);
        projectObject.put("grant", grant.getId());

        if(proj.getDateCreated() != null){
          String delims = "[ ]+";
          String stringDate = proj.getDateCreated().toString();
          System.out.println(stringDate);
          String[] tokens = stringDate.split(delims);
          projectObject.put("dateCreated", tokens[1] + " " + tokens[2] + " " + tokens[5]);
        }
        if(proj.getMembers() != null){
          // just return the size of the array
          int membersSize = proj.getMembers().size();
          projectObject.put("membersNum", membersSize);
          
        }
        if(!(proj.getCreatorId().equals(""))){

          Person creator = ClothoAdapter.getPerson(proj.getCreatorId(), clothoObject);
          System.out.println( creator.getFirstName()+" " +creator.getLastName());
          projectObject.put("creator", creator.getFirstName()+" " +creator.getLastName());
        }
        //projectObject.put("members", proj.getMembers());

        if(!(proj.getLeadId().equals(""))){
          System.out.println("getting lead");
          String leadtId = proj.getLeadId();
          System.out.println(leadtId);
          Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);
          
          if(lead.getFirstName() != null && lead.getLastName() != null){
            System.out.println(lead.getFirstName()+lead.getLastName());
            projectObject.put("lead", lead.getFirstName()+" "+lead.getLastName());
          }
          // add the project to the json object of all projects
          listOfProjects.put(projectObject);
         }
      System.out.println("***");
      System.out.println("RESULT IS:");
      System.out.println(listOfProjects);
      System.out.println("***");      
      //projectMap = (Map) ClothoAdapter.getProject(id, clothoObject);
      System.out.println("after getProject");
      System.out.println(projectObject);
      }
    }
        conn.closeConnection();

        writer.println(listOfProjects); //Send back stringified JSON object
        writer.flush();
        writer.close(); 
     

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
