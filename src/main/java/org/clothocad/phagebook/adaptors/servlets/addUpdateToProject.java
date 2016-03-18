/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.util.List;
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

/**
 *
 * @author anna_g
 */
public class addUpdateToProject extends HttpServlet {

  /**
   * 
   * This function creates and associates a new status object with project in 
   * clotho.
   * 
   * @param userID
   * @param projectID
   * @param newStatus

   */
  protected static void addProjectUpdate(String userID, String projectID, String newStatus, 
          Clotho clothoObject){
    
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
    
    System.out.println(editor.getEmailId());
    System.out.println(project.getName());
     
    // get the existing list of project updates and add the id of the  new update
    List<String> projectUpdates = project.getUpdates();
    projectUpdates.add(statusID);
    
    // update the update lists in the project object
    project.setUpdates(projectUpdates);
    
    // change the project in clotho
    String foo = ClothoAdapter.setProject(project, clothoObject);
    System.out.println("In addProjectUpdate function projectID is:");
    System.out.println(foo);
    // TODO: email the peeps associate with the project what update was added
    sendEmails.sendEmails(foo,newStatus, clothoObject);
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
    ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
    Clotho clothoObject = new Clotho(conn);

    // New Update will be a string.
    String userID = request.getParameter("userID");
    String projectID = request.getParameter("projectID");
    String newStatus = "";
    
    addProjectUpdate(userID, projectID, newStatus, clothoObject);
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
