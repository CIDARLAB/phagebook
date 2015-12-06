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
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
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
        Map projectMap = new HashMap();
        JSONObject projectObject = new JSONObject();
        System.out.println("before getProject");
        Project proj =  ClothoAdaptor.getProject(id, clothoObject);
        projectObject.put("description",proj.getDescription());
        
        projectObject.put("budget", proj.getBudget());
        projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
        projectObject.put("projectName", proj.getName());
        projectObject.put("notebooks", proj.getNotebooks());
        projectObject.put("dateCreated", proj.getDateCreated());
        projectObject.put("updates", proj.getUpdates());
        projectObject.put("grant", proj.getGrant());

        if(proj.getMembers() != null){
        }
        if(proj.getCreator() != null){
            Person creator = proj.getCreator();
            projectObject.put("creator", creator.getFirstName()+creator.getLastName());
        }
        projectObject.put("members", proj.getMembers());
        if(proj.getLead() != null){
            Person lead = proj.getLead();
            projectObject.put("lead", lead.getFirstName()+lead.getLastName());
        }
        //projectMap = (Map) ClothoAdaptor.getProject(id, clothoObject);
        System.out.println("after getProject");
        System.out.println(projectObject);
        String project = projectObject.toString();
        System.out.println("stringified :: " + project);
         conn.closeConnection();
        writer.println(project); //Send back stringified JSON object
        writer.flush();
        writer.close();
}}
