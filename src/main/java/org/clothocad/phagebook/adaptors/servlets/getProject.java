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
        String id = request.getParameter("id");
        System.out.println("id ::" + id);
        System.out.println("inside servlet " + id);
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        JSONObject projectObject = new JSONObject();
        System.out.println("before getProject");
        Project proj =  ClothoAdapter.getProject(id, clothoObject);
        projectObject.put("description",proj.getDescription());
        
        projectObject.put("budget", proj.getBudget());
        projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
        projectObject.put("projectName", proj.getName());
        projectObject.put("notebooks", proj.getNotebooks());
        projectObject.put("updates", proj.getUpdates());
        Grant gran = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);
        projectObject.put("grant", gran.getId());

        if(proj.getDateCreated() != null){
            String delims = "[ ]+";
            String string = proj.getDateCreated().toString();
            String[] tokens = string.split(delims);
            projectObject.put("dateCreated", tokens[1] + " " + tokens[2] + " " + tokens[5]);
        }
        
        if(proj.getMembers() != null){
        }
        if(proj.getCreatorId() != null){
            
            Person creator = ClothoAdapter.getPerson(proj.getCreatorId(), clothoObject);
            projectObject.put("creator", creator.getFirstName()+" " +creator.getLastName());
        }
        projectObject.put("members", proj.getMembers());
<<<<<<< HEAD
        if(proj.getLeadId() != null){
            String leadId = proj.getLeadId();
            Person lead = ClothoAdapter.getPerson(leadId, clothoObject);
=======
        
        if(proj.getLeadId() != null){
            Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);
>>>>>>> 7706ad6eaa761e0168212ca012e8ddef8eb15530
            if(lead.getFirstName() != null && lead.getLastName() != null){
                projectObject.put("lead", lead.getFirstName()+lead.getLastName());
            }
        }
        //projectMap = (Map) ClothoAdapter.getProject(id, clothoObject);
        System.out.println("after getProject");
        System.out.println(projectObject);
        String project = projectObject.toString();
        System.out.println("stringified :: " + project);
         conn.closeConnection();
        writer.println(project); //Send back stringified JSON object
        writer.flush();
        writer.close();
}}
