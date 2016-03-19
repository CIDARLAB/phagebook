/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
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
import org.json.JSONObject;

/**
 *
 * @author anna_g
 */
public class editProject extends HttpServlet {
  private static Logger logger = Logger.getLogger(editProject.class.getName());

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

  }
  
   /**
   * This loops through the values passed in the request that was converted
   * to a hashmap to change the project.
   *
   * @param projectId Project Id
   * @param params the request values that were converted from httpObject to Hashmap
   * @param clothoObject to log in to clotho
   */
  static void editProjectFunction(Project project, HashMap params, Clotho clothoObject){
       
      // params is the hashmap of new values
      System.out.println("In Edit Project function");
      
      Iterator entries = params.entrySet().iterator();
      while (entries.hasNext()) {
        // reset the value if it is diff from the one in the project object
        Map.Entry entry = (Map.Entry) entries.next();
        String key = (String)entry.getKey();
        String value = (String)entry.getValue();
        System.out.println("Key = " + key + ", Value = " + value);
        
        String[] keyValue = new String[4]; 
        keyValue[0] = key; // type of new value (like "description")
        keyValue[1] = value; // the actual new value (like "This is a new desciption")
        if(key.equals("editorId")){
          Person editor = ClothoAdapter.getPerson(value, clothoObject);
          System.out.println();
        }
        if(key.equals("description")){
          keyValue[2]= "description";
          keyValue[3]= project.getDescription();
          //helperMsg(project.getDescription(), value);
          if(value != ""){
            project.setDescription(value);
          }
          
        }
        if(key.equals("name")){
          keyValue[2]= "name";
          keyValue[3]= project.getName();
          //helperMsg(project.getName(), value);
          if(value != ""){
            project.setName(value);
          }
          
        }
        if(key.equals("leadId")){
          //helperMsg(project.getLeadId(), value);
          System.out.println("Can't edit lead yet, sorry!");
        }
        if(key.equals("budget")){
          keyValue[2]= "budget";
          keyValue[3]= project.getBudget().toString();
          //helperMsg(Double.toString(project.getBudget()), value);
          if(value != ""){
            project.setBudget(Double.parseDouble(value));
          }
        }
        if(key.equals("projectGrant")){
          String oldGrantId = project.getGrantId();
          Grant newGrant = new Grant(value);
          String newGrantId = ClothoAdapter.createGrant(newGrant, clothoObject);
          
          keyValue[2]= "projectGrant";
          keyValue[3]= oldGrantId;

          project.setGrantId(newGrantId);
        }
      }
      String projectID = project.getId();
      System.out.println("in Edit Project Function Project ID is");
      System.out.println(projectID);
      System.out.println(clothoObject);
      String foo = ClothoAdapter.setProject(project, clothoObject);
      System.out.println(foo);
      //sendEmails(request);
    }
  
   public void helperMsg(String oldVal, String newVal){
      System.out.println("\nOld Value is: " + oldVal + "\nNew Value is: " + newVal);
   }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
  }

  //processes requests for projects and created "Project" object
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    
        response.setContentType("text/html;charset=UTF-8");
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
      System.out.println("got request in EditProject");
    
      System.out.println(request);
      
      // these will be in a cookie?
      String userID = request.getParameter("userID");
      String projectID = request.getParameter("projectID");
      
      Person editor = ClothoAdapter.getPerson(userID, clothoObject);
      Project project = ClothoAdapter.getProject(projectID, clothoObject);

      System.out.println(editor.getEmailId());
      System.out.println(project.getName());
      
      // get all of the parameters in the request
      Enumeration e = request.getParameterNames();
      
      // loop through the enumeration to get values from the request and add to
      // the hashmap
      HashMap reqHashMap = new HashMap();
      while(e.hasMoreElements()){
        String key = (String) e.nextElement();
        String value = request.getParameter(key);
        reqHashMap.put(key, value);
      }
      editProjectFunction(project, reqHashMap, clothoObject);
                      
      // create a result object and send it to the frontend
//      JSONObject result = new JSONObject();
//      result.put("success",1);
//
//      PrintWriter writer = response.getWriter();
//      writer.println(result);
//      writer.flush();
//      writer.close();
//    }
    }
     
  }

  @Override
  public String getServletInfo() {
    return "This servlet edits projects.";
  }// </editor-fold>

}
