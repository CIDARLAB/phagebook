/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
import org.json.JSONObject;
/**
 *
 * @author KatieLewis
 */
public class getProject extends HttpServlet {
 @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        
      Object projectIdObj = request.getParameter("projectID");
      String projectId  = projectIdObj != null ? (String) projectIdObj : "" ;
      System.out.println("projectId is:"); 
      System.out.println(projectId);
      System.out.println("inside Get Project");

      //ESTABLISH CONNECTION
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
      
      System.out.println("getting project from Clotho");
        
      JSONObject projectObject = new JSONObject();
      Project proj =  ClothoAdapter.getProject(projectId, clothoObject);
      
      System.out.println("Got project from Clotho");
                
      String desc = proj.getDescription();
      System.out.println("Description is: " + desc);
      
      Grant grant;      
      if(!proj.getGrantId().equals("")){
        System.out.println(proj.getGrantId());
        grant = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);
        projectObject.put("grantName", grant.getName());
        projectObject.put("grant", grant.getId());
      }
        
      if(proj.getDateCreated() != null){
          String delims = "[ ]+";
          String stringDate = proj.getDateCreated().toString();
          System.out.println(stringDate);
          String[] tokens = stringDate.split(delims);
          projectObject.put("dateCreated", tokens[1] + " " + tokens[2] + " " + tokens[5]);
      }
      
      if(proj.getMembers() != null){
        
      
      }
        
      if(!(proj.getCreatorId().equals(""))){
          Person creator = ClothoAdapter.getPerson(proj.getCreatorId(), clothoObject);
          System.out.println( creator.getFirstName()+" " +creator.getLastName());
          projectObject.put("creator", creator.getFirstName()+" " +creator.getLastName());
          projectObject.put("creatorId", proj.getCreatorId());
           if(creator.getLabs().size()>0){
            // if a person has labs - get 'em!
             System.out.println("creator has more than 0 labs");
             projectObject.put("creatorLabs", creator.getLabs());
          }
      }

        if(!(proj.getLeadId().equals(""))){
            System.out.println("Getting Lead person");            
            String leadId = proj.getLeadId();
            System.out.println("Lead Id is "+leadId);
            projectObject.put("leadId", leadId);
            Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);
            if(lead.getFirstName() != null && lead.getLastName() != null){
              System.out.println("Getting Lead name: "+lead.getFirstName()+" "+ lead.getLastName());
              projectObject.put("lead", lead.getFirstName()+" "+lead.getLastName());
            }
            if(lead.getLabs().size()>0){
              System.out.println("lead has more than 0 labs");
              // if a person has labs - get 'em!
               projectObject.put("leadLabs", lead.getLabs());              
            }
        }
        
        projectObject.put("description",desc);
        projectObject.put("budget", proj.getBudget());
        projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
        projectObject.put("projectName", proj.getName());
        //projectObject.put("notebooks", proj.getNotebooks());
        projectObject.put("updates", proj.getUpdates());

        //projectMap = (Map) ClothoAdapter.getProject(id, clothoObject);
               
        String project = projectObject.toString();
       
        System.out.println("after getProject");       
        System.out.println("stringified :: " + project);
        
        conn.closeConnection();
        writer.println(project); //Send back stringified JSON object
        writer.flush();
        writer.close();
}}
