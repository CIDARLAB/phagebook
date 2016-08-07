/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jacob
 */
@Controller
public class ColleagueController {
    
    @RequestMapping(value="/addColleagueRequest", method=RequestMethod.POST)
    public void addColleagueRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object pUserId = request.getParameter("loggedInClothoId");
        String userId = pUserId != null ? (String) pUserId : "";

        Object pRequestId = request.getParameter("colleagueClothoId");
        String requestId = pRequestId != null ? (String) pRequestId : "";

        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //
            //userID = person logged into phagebook
            //requestId = person not logged in... receives request
            Person receivesRequest = ClothoAdapter.getPerson(requestId, clothoObject);
            if (!requestId.equals(userId)) {
                if (!receivesRequest.getId().equals("") && !userId.equals("")) {
                    if (!receivesRequest.getColleagues().contains(userId) && !receivesRequest.getColleagueRequests().contains(userId)) {

                        receivesRequest.addColleagueRequest(userId);
                    }

                }
                
                clothoObject.logout();
                ClothoAdapter.setPerson(receivesRequest, clothoObject);
            }
            
            conn.closeConnection();

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
    }
    
    @RequestMapping(value="/approveColleagueRequest", method=RequestMethod.POST)
    public void approveColleagueRequest(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pColleagueId = params.get("colleagueId");
        String colleagueId = pColleagueId != null ? (String) pColleagueId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";
        
        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!colleagueId.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //
            Person user = ClothoAdapter.getPerson(userId, clothoObject);
            List<String> colleagueRequests = user.getColleagueRequests();
            if (colleagueRequests.contains(colleagueId)){
                colleagueRequests.remove(colleagueId);
                user.setColleagueRequests(colleagueRequests);
                
            }
            
            Person colleague = ClothoAdapter.getPerson(colleagueId, clothoObject);
            List<String> colleaguesUser = user.getColleagues();
            List<String> colleaguesColleague = colleague.getColleagues();
            if (!colleaguesUser.contains(colleague.getId())){
                if (!colleaguesColleague.contains(user.getId())){
                    colleaguesUser.add(colleague.getId());
                    colleaguesColleague.add(user.getId());
                    colleague.setColleagues(colleaguesColleague);
                    user.setColleagues(colleaguesUser);
                    
                }
            }
            clothoObject.logout();
            ClothoAdapter.setPerson(user, clothoObject);
            ClothoAdapter.setPerson(colleague, clothoObject);
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Colleague Request Accepted!");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            conn.closeConnection();
            
            
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters for ajax");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="/denyColleagueRequest", method=RequestMethod.POST)    
    public void denyColleagueRequest(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        Object pColleagueId = params.get("colleagueId");
        String colleagueId = pColleagueId != null ? (String) pColleagueId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";
        
        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!colleagueId.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //
            
            Person user = ClothoAdapter.getPerson(userId, clothoObject);
            List<String> colleagueRequests = user.getColleagueRequests();
            if (colleagueRequests.contains(colleagueId)){
                colleagueRequests.remove(colleagueId);
                user.setColleagueRequests(colleagueRequests);
                
            }
            clothoObject.logout();
            ClothoAdapter.setPerson(user, clothoObject);
            
            conn.closeConnection();
            
        }
        else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters for ajax");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="/listColleagueRequests", method=RequestMethod.GET)    
    public void listColleagueRequests(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";
        System.out.println("In listColleagueRequest");

        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //
            
            
            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            JSONArray humans = new JSONArray();
            if (!retrieve.getId().equals("")){
                //valid human ;)
                
                List<String> humanConnectionRequests = retrieve.getColleagueRequests();
                
                for (String humanId :humanConnectionRequests ){
                    JSONObject humanJSON = new JSONObject();
                    Person human  = ClothoAdapter.getPerson(humanId, clothoObject) ;
                    humanJSON.put("firstName", human.getFirstName());
                    humanJSON.put("lastName", human.getLastName());
                    humanJSON.put("clothoId", human.getId());
                    
                    humans.put(humanJSON);
                    
                }
                
                
            }
            
            clothoObject.logout();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(humans);
            out.flush();
            conn.closeConnection();

        } else {
            
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing an id to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="/loadColleagues", method=RequestMethod.GET)    
    public void loadColleagues(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("reached doGet");

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";

        
        System.out.println(userId);
        boolean isValid = false;
        if (userId != null && userId != "") {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "test" + System.currentTimeMillis();
            createUserMap.put("username", username);
            createUserMap.put("password", "password");
            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");
            clothoObject.login(loginMap);
            //

            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            JSONArray humans = new JSONArray();
            if (!retrieve.getId().equals("")){
                //valid human ;)
                
                List<String> humanConnections = retrieve.getColleagues();
                
                for (String humanId :humanConnections ){
                    JSONObject humanJSON = new JSONObject();
                    Person human  = ClothoAdapter.getPerson(humanId, clothoObject);
                    String institutionId = human.getInstitution();
                    humanJSON.put("institutionName", ClothoAdapter.getInstitution(institutionId, clothoObject).getName());
                    humanJSON.put("fullname", human.getFirstName() + " " + human.getLastName());
                    humanJSON.put("clothoId", human.getId());
                    
                    humans.put(humanJSON);
                    
                }
                
                
            }
            
            clothoObject.logout();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(humans);
            out.flush();
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
    }
    
    
}
