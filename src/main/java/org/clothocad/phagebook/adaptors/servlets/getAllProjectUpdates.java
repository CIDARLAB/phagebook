/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import static org.clothocad.phagebook.adaptors.servlets.addUpdateToProject.addProjectUpdate;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONObject;

/**
 *
 * @author anna_g
 */
public class getAllProjectUpdates extends HttpServlet {

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
    try (PrintWriter out = response.getWriter()) {
      
      /* TODO output your page here. You may use following sample code. */
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet getAllProjectUpdates</title>");      
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servlet getAllProjectUpdates at " + request.getContextPath() + "</h1>");
      out.println("</body>");
      out.println("</html>");
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
      System.out.println("in the doGet of getAllProjectUpdates!!");
      Object projectIdObj = request.getParameter("projectId");
      String projectId  = projectIdObj != null ? (String) projectIdObj : "" ;
      System.out.println(projectId);
      
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
      
      // get this project
      Project pr = ClothoAdapter.getProject(projectId, clothoObject);
      List<String> allUpdates = pr.getUpdates();
      List<Map<String, String>> listOfUpdates = new ArrayList<Map<String, String>>();
      JSONObject result = new JSONObject();      

        for (String s : allUpdates)
        {
//            clothoObject.logout();
            System.out.println("in the loop!");
            System.out.println(s);
            Status update = ClothoAdapter.getStatus(s, clothoObject);
            System.out.println(update.getText());
            Map u = new HashMap();
            u.put("date", update.getCreated());
            u.put("userId", update.getUserId());
            // get a person's first and last name
            clothoObject.logout();
            Person p = ClothoAdapter.getPerson(update.getUserId(), clothoObject);
            u.put("userName", p.getFirstName()+ " " +p.getLastName());
            u.put("text", update.getText());
            listOfUpdates.add(u);
        }
        System.out.println("");
        conn.closeConnection();
        result.put("success",1);
        result.put("updates",listOfUpdates);
        System.out.println(result);
        
        PrintWriter writer = response.getWriter();
        writer.println(result);
        writer.flush();
        writer.close();
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
