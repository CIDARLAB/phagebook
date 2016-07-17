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
import org.clothocad.phagebook.adaptors.EmailCredentials;
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
    String statusID = ClothoAdapter.createStatus(newUpdate, clothoObject);

//    System.out.println(newUpdate);
//    System.out.println("About to create a Status in Clotho");    
//    System.out.println("Status has been created in Clotho and ID is: "+statusID);
    
    // get the objects associated with the passed in IDS from clotho
    Person editor = ClothoAdapter.getPerson(userID, clothoObject);
//    System.out.println("User Id is: ");
//    System.out.println(userID);
    
    Project project = ClothoAdapter.getProject(projectID, clothoObject);
    String projectName = project.getName();
//    System.out.println("Project Id is: ");
//    System.out.println(projectID);
    
    String editorName = editor.getFirstName() + " " +editor.getLastName();
//    System.out.println(editorName);
//    System.out.println(project.getName());
     
    // get the existing list of project updates and add the id of the  new update
    List<String> projectUpdates = project.getUpdates();
    projectUpdates.add(statusID);    
    // update the update lists in the project object
    project.setUpdates(projectUpdates);
       
    // change the project in clotho (no use for return string)
    String prId = ClothoAdapter.setProject(project, clothoObject);
    
    if(emailPeople){
      
      Person creator = ClothoAdapter.getPerson(project.getCreatorId(), clothoObject);
      Person lead = ClothoAdapter.getPerson(project.getLeadId(), clothoObject);
      String leadName = lead.getFirstName() + " " + lead.getLastName();
      String leadEmail = lead.getEmailId();
      List<String> members = project.getMembers();
      String m = concatBody(leadName, editorName, projectName, newStatus);
      String messageSubject = "New Update added to" + projectName + " by "+ editorName;
      
      EmailCredentials.logInAndSendMessage(m, messageSubject, leadEmail);

      // loop through the list and call the emailing function
      for(int i = 0; i<members.size(); i++){
          String personId = members.get(i);
          Person member = ClothoAdapter.getPerson(personId, clothoObject);
          String memberEmail = member.getEmailId();
          if(memberEmail.equals("Not set")){
            break;
          }
          String memberName = member.getFirstName() + ' ' + member.getLastName();          
          String messageBody = concatBody(memberName, editorName, projectName, newStatus);
          System.out.println("in addUpdateToProject about to send emails");          
          EmailCredentials.logInAndSendMessage(messageBody, messageSubject, memberEmail);
        }
      }    

    List<String> allUpdates= project.getUpdates();
    return allUpdates;
  }
  
  private static String concatBody(String memberName, String editorName, String projectName, String newStatus){
    String imgSource = "http://cidarlab.org/wp-content/uploads/2015/09/phagebook_AWH.png" ;

    String messageBody = "<img height=\"50\" width=\"200\" src=\""+ imgSource+ "\">" + 
            "<p>Hi " + memberName +",</p>" +
            " <p>A new update was added to project " + projectName + 
            " by " + editorName + ".</p>" +
            " <p>The update is: " +"<i>"  +
            newStatus
            + "</i>"+"</p>" +
            " <p>Have a great day,</p>"   +
            " <p>The Phagebook Team</p>"  ;
    return messageBody;
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
      JSONObject result = new JSONObject();

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


      // if there is a status
      if(newStatus.length() != 0){
        List<String> allUpdates = addProjectUpdate(userID, projectID, newStatus, emailPeople, clothoObject);
        List<Map<String, String>> listOfUpdates = new ArrayList<Map<String, String>>();
        Person per = ClothoAdapter.getPerson(userID, clothoObject);
        System.out.println(per.getEmailId());
        System.out.println(per.getProjects());
        
        for (String s : allUpdates)
        {
            Status update = ClothoAdapter.getStatus(s, clothoObject);
            Map u = new HashMap();
            u.put("date", update.getCreated());
            u.put("userId", update.getUserId());
            // get a person's first and last name
            Person p = ClothoAdapter.getPerson(update.getUserId(), clothoObject);
            u.put("userName", p.getFirstName()+ " " +p.getLastName());
            u.put("text", update.getText());
            listOfUpdates.add(u);
        }
        result.put("success",1);
        result.put("updates",listOfUpdates);
      }else{
        System.out.println("Update was too short -- letting the user know!");

        result.put("short",1);
      }
      conn.closeConnection();
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
