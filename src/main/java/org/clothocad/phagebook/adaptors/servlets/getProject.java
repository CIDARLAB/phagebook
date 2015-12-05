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
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
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
        projectMap = (Map) ClothoAdaptor.getProject(id, clothoObject);
        System.out.println(projectMap);
        if(projectMap.containsKey("description")){
            if(projectMap.get("description") != null){
               projectObject.put("description", projectMap.get("description"));
            }
        }
        if(projectMap.containsKey("lead")){
            if(projectMap.get("lead") != null){
                projectObject.put("lead",projectMap.get("lead"));
            }
        }
        if(projectMap.containsKey("budget")){
            if(projectMap.get("budget") != null){
                projectObject.put("budget", projectMap.get("budget"));
            }
        }
        if(projectMap.containsKey("members")){
            if(projectMap.get("members") != null){
                projectObject.put("members", projectMap.get("members"));
            }
        }
        if(projectMap.containsKey("affiliatedLabs")){
            if(projectMap.get("affiliatedLabs") !=null){
                projectObject.put("affiliatedLabs", projectMap.get("affiliatedLabs"));
            }
        }
        if(projectMap.containsKey("name")){
            if(projectMap.get("name") != null){
                projectObject.put("projectName", projectMap.get("name"));
            }
        }
        if(projectMap.containsKey("dateCreated")){
            if(projectMap.get("dateCreated") != null){
                projectObject.put("dateCreated", projectMap.get("dateCreated"));
            }
        }
        if(projectMap.containsKey("updates")){
            if(projectMap.get("updates") != null){
                projectObject.put("updates", projectMap.get("updates"));
            }
        }
        if(projectMap.containsKey("creator")){
            if(projectMap.get("creator") != null){
                projectObject.put("creator", projectMap.get("creator"));
            }
        }
        if(projectMap.containsKey("grant")){
            if(projectMap.get("grant") != null){
                projectObject.put("grant", projectMap.get("grant"));
            }
        }
        if(projectMap.containsKey("notebooks")){
            if(projectMap.get("notebooks") != null){
                projectObject.put("notebooks", projectMap.get("notebooks"));
            }
        }
        System.out.println(projectObject);
        String project = projectObject.toString();
        System.out.println("stringified :: " + project);
         conn.closeConnection();
        writer.println(project); //Send back stringified JSON object
        writer.flush();
        writer.close();
}}
