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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Lab;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author jacob
 */
@Controller
@RequestMapping(value="/PI")
public class PIController {
    
    @RequestMapping(value="addPIToLab", method=RequestMethod.POST)
    public void addPIToLab(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        //at most we'd add like 10 PIs to a lab...
        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId: "";
        
        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId: "";
        
        
        boolean isValid = false;
        
        if (!labId.equals("") && !userId.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            Lab lab = ClothoAdapter.getLab(labId, clothoObject); //Lab
            //Person person = ClothoAdapter.getPerson(userId, clothoObject); //if we ever wanted to use more props check priviledge maybe?
            List<String> labPIList = lab.getLeadPIs(); // to change
            JSONObject responseJSON = new JSONObject();
            
            if (!labPIList.contains(pLabId)){
                labPIList.add(userId);
                responseJSON.put("message", "new PI added!");
            }else{
                responseJSON.put("message", "Person is already a PI!");
            }
            
            lab.setLeadPIs(labPIList);
            
            ClothoAdapter.setLab(lab, clothoObject);
            
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            conn.closeConnection();
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            
            
        } else{
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Lab or User Id cannot be blank.");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }
    
    @RequestMapping(value="listPIsOfLab", method=RequestMethod.GET)
    public void listPIsOfLab(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {        
       
        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId: "";
        
        boolean isValid = false;
        
        if (!labId.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            
            
            Lab lab = ClothoAdapter.getLab(labId, clothoObject); //Lab
            if (!lab.getId().equals("")){
                JSONArray PIs = new JSONArray();
                for (String pi : lab.getLeadPIs()){
                    Person piPers = ClothoAdapter.getPerson(pi, clothoObject);
                    JSONObject PI = new JSONObject();
                    PI.put("name", piPers.getFirstName() + " " + piPers.getLastName() );
                    PI.put("email", piPers.getEmailId());
                    PI.put("clothoId", piPers.getId());
                    String firstInstitutionId = (piPers.getInstitutions().size() > 0) ? piPers.getInstitutions().get(0): "None" ;
                    if (!firstInstitutionId.equals("None")){
                        PI.put("institutionName", ClothoAdapter.getInstitution(firstInstitutionId, clothoObject) .getName());
                    }
                    
                    String firstLabId = (piPers.getLabs().size() > 0) ? piPers.getLabs().get(0): "None";
                    if (!firstLabId.equals("None")) {
                        PI.put("labName", ClothoAdapter.getLab(firstLabId, clothoObject).getName());
                    }
                    PIs.put(PI);
                    
                }
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(PIs);
                out.flush();
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Lab not found in Clotho");
                PrintWriter out = response.getWriter();
                out.print(responseJSON);
                out.flush();
                
            }
            
           conn.closeConnection(); 
        }
        else
        {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Lab Id Cannot Be Blank");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            
        }
    }
    
    @RequestMapping(value="removePIFromLab", method=RequestMethod.POST)
    public void removePIFromLab(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId: "";
        
        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId: "";
        
        boolean isValid = false;
        
        if (!labId.equals("") && !userId.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            Lab lab = ClothoAdapter.getLab(labId, clothoObject); //Lab
            //Person person = ClothoAdapter.getPerson(userId, clothoObject); //if we ever wanted to use more props check priviledge maybe?
            List<String> labPIList = lab.getLeadPIs(); // to change
            JSONObject responseJSON = new JSONObject();
            
            if (labPIList.contains(userId)){
                labPIList.remove(userId);
                responseJSON.put("message", "PI removed");
            }else{
                responseJSON.put("message", "Person is not a PI!");
            }
            
            lab.setLeadPIs(labPIList);
            
            ClothoAdapter.setLab(lab, clothoObject);
            
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            conn.closeConnection();
            
            
        } else{
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Lab or User Id cannot be blank.");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

}
