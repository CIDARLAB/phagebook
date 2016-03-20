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
import org.clothocad.phagebook.adaptors.sendEmails;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONObject;

/**
 *
 * @author anna_g
 */
public class addUpdateToProject extends HttpServlet {

  /**
   * 
   * This function creates and associates a new status object with project in 
   * clotho. It returns a list of strings that contain the text of
   * each update. Continually, depending on the value passed from the 
   * frontend it would or would not email people.
   * 
   * @param userID
   * @param emailPeople
   * @param projectID
   * @param newStatus

   */
  protected static List<String> addProjectUpdate(String userID, String projectID,
          String newStatus, boolean emailPeople, Clotho clothoObject){
    
    // create a new status object
    Status newUpdate = new Status();
    newUpdate.setText(newStatus);
    newUpdate.setUserId(userID);
    System.out.println(newUpdate);
    System.out.println("About to create a Status in Clotho");
    String statusID = ClothoAdapter.createStatus(newUpdate, clothoObject);
    
    System.out.println("Status has been created in Clotho and ID is: "+statusID);
    
    // get the objects associated with the passed in IDS from clotho
    Person editor = ClothoAdapter.getPerson(userID, clothoObject);
    Project project = ClothoAdapter.getProject(projectID, clothoObject);  
    
    String editorName = editor.getFirstName() + " " +editor.getLastName();
    System.out.println(editorName);
    System.out.println(project.getName());
     
    // get the existing list of project updates and add the id of the  new update
    List<String> projectUpdates = project.getUpdates();
    projectUpdates.add(statusID);
    
    // update the update lists in the project object
    project.setUpdates(projectUpdates);
    
    List<String> allUpdates= project.getUpdates();
    // change the project in clotho
    String foo = ClothoAdapter.setProject(project, clothoObject);
    System.out.println("In addProjectUpdate function projectID is:");
    System.out.println(foo);
    // TODO: email the peeps associate with the project what update was added
    if(emailPeople){
      System.out.println();
      System.out.println("I will email the people now");
      System.out.println();
      sendEmails.sendEmails(foo, editorName, clothoObject);
    }

    return allUpdates;
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
    try (PrintWriter out = response.getWriter()) {

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
      System.err.println("Got a new Update request in addUpdateToProject ");
      // New Update will be a string.
      // declare these here 
      String userID = "";
      String projectID = "";
      String newStatus = "";
      boolean emailPeople = false;
      if(request.getParameter("userID")!=null){
        userID = request.getParameter("userID");
      }
      if(request.getParameter("projectID")!=null){
        projectID = request.getParameter("projectID");
      }
      if(request.getParameter("newStatus")!=null){
        newStatus = request.getParameter("newStatus");
      }
      if(request.getParameter("emailPeople")!=null){
        // what if it is not a boolean?
        emailPeople =  Boolean.parseBoolean(request.getParameter("emailPeople"));
      }

      JSONObject result = new JSONObject();      

      // if there is a status
      if(newStatus.length() != 0){
        List<String> allUpdates = addProjectUpdate(userID, projectID, newStatus, emailPeople, clothoObject);
        String listString = "";
        for (String s : allUpdates)
        {
            //listString += s + "\t";
            Status update = ClothoAdapter.getStatus(s, clothoObject);
            System.out.println(update.getText());
            listString += update.getText() + " ";
        }
        result.put("success",1);
        result.put("updates",listString);
      }else{
        System.out.println("Update was too short -- letting the user know!");

        result.put("short",1);
      }
      
      PrintWriter writer = response.getWriter();
      writer.println(result);
      writer.flush();
      writer.close();

    }
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
