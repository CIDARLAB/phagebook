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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Project;

/**
 *
 * @author anna_g
 */
public class addUpdateToProject extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void addProjectUpdate(String userID, String projectID, String newStatus)
          throws ServletException, IOException {
    
    ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
    Clotho clothoObject = new Clotho(conn);
    
    Person editor = ClothoAdapter.getPerson(userID, clothoObject);
    Project project = ClothoAdapter.getProject(projectID, clothoObject);  
    
    System.out.println(editor.getEmailId());
    System.out.println(project.getName());
     
    List<String> projectUpdates = project.getUpdates();
    projectUpdates.add(newStatus);
    
    project.setUpdates(projectUpdates);
    
    String foo = ClothoAdapter.setProject(project, clothoObject);
    System.out.println(foo);
    
    // now email the peeps associate with the project what update was added

    
    /*response.setContentType("text/html;charset=UTF-8");
    
    try (PrintWriter out = response.getWriter()) {
      // TODO output your page here. You may use following sample code. 
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet addUpdateToProject</title>");      
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servlet addUpdateToProject at " + request.getContextPath() + "</h1>");
      out.println("</body>");
      out.println("</html>");
    }
*/
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
    // New Update will be a string.
    String userID = request.getParameter("userID");
    String projectID = request.getParameter("projectID");
    String newStatus = "";
    
    addProjectUpdate(userID, projectID, newStatus);
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
