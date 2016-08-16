/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;

/**
 *
 * @author jacob
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LabController {

    private final String backendPhagebookUser = Args.defaultPhagebookUsername;
    private final String backendPhagebookPassword = Args.defaultPhagebookPassword;

    @RequestMapping(value = "/addPIToLab", method = RequestMethod.POST)
    protected void addPIToLab(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        //at most we'd add like 10 PIs to a lab...
        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";

        boolean isValid = false;

        if (!labId.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            // able to query now. 
            Lab lab = ClothoAdapter.getLab(labId, clothoObject); //Lab
            //Person person = ClothoAdapter.getPerson(userId, clothoObject); //if we ever wanted to use more props check priviledge maybe?
            List<String> labPIList = lab.getLeadPIs(); // to change
            JSONObject responseJSON = new JSONObject();

            if (!labPIList.contains(pLabId)) {
                labPIList.add(userId);
                responseJSON.put("message", "new PI added!");
            } else {
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

        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Lab or User Id cannot be blank.");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

    @RequestMapping(value = "/createLab", method = RequestMethod.POST)
    protected void createLab(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pInstitutionId = params.get("institution");
        String institutionId = pInstitutionId != null ? (String) pInstitutionId : "";

        Object pUser = params.get("user");
        String userId = pUser != null ? (String) pUser : "";

        Object pName = params.get("name");
        String name = pName != null ? (String) pName : "";

        Object pDescription = params.get("description");
        String description = pDescription != null ? (String) pDescription : "";

        Object pPhone = params.get("phone");
        String phone = pPhone != null ? (String) pPhone : "";

        Object pUrl = params.get("url");
        String url = pUrl != null ? (String) pUrl : "";

        boolean isValid = false;

        if (!institutionId.equals("") && !name.equals("") && !description.equals("")
                && !phone.equals("") && !url.equals("")) {
            isValid = true;
        }

        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            // able to query now. 
            Institution inst = ClothoAdapter.getInstitution(institutionId, clothoObject);

            Lab lab = new Lab();
            lab.setName(name);
            lab.setUrl(url);
            lab.setPhone(phone);
            lab.setDescription(description);
            lab.setInstitution(institutionId);

            List<String> leadPIList = new ArrayList<>();
            leadPIList.add(userId);
            lab.setLeadPIs(leadPIList);

            ClothoAdapter.createLab(lab, clothoObject);

            List<String> labs = inst.getLabs();
            labs.add(lab.getId());
            inst.setLabs(labs);
            ClothoAdapter.setInstitution(inst, clothoObject);
            JSONObject responseJSON = new JSONObject();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            responseJSON.put("labId", lab.getId());
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            conn.closeConnection();
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

    @RequestMapping(value = "/removePIFromLab", method = RequestMethod.POST)
    protected void removePIFromLab(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId : "";

        Object pUserId = params.get("userId");
        String userId = pUserId != null ? (String) pUserId : "";

        boolean isValid = false;

        if (!labId.equals("") && !userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            // able to query now. 
            Lab lab = ClothoAdapter.getLab(labId, clothoObject); //Lab
            //Person person = ClothoAdapter.getPerson(userId, clothoObject); //if we ever wanted to use more props check priviledge maybe?
            List<String> labPIList = lab.getLeadPIs(); // to change
            JSONObject responseJSON = new JSONObject();

            if (labPIList.contains(userId)) {
                labPIList.remove(userId);
                responseJSON.put("message", "PI removed");
            } else {
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

        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Lab or User Id cannot be blank.");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
    }

    @RequestMapping(value = "/listPIsOfLab", method = RequestMethod.GET)
    protected void listPIsOfLab(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("listPIS");
        Object pLabId = params.get("lab");
        String labId = pLabId != null ? (String) pLabId : "";

        boolean isValid = false;

        if (!labId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            // able to query now. 

            Lab lab = ClothoAdapter.getLab(labId, clothoObject); //Lab
            if (!lab.getId().equals("")) {
                JSONArray PIs = new JSONArray();
                for (String pi : lab.getLeadPIs()) {
                    Person piPers = ClothoAdapter.getPerson(pi, clothoObject);
                    JSONObject PI = new JSONObject();
                    PI.put("name", piPers.getFirstName() + " " + piPers.getLastName());
                    PI.put("email", piPers.getEmailId());
                    PI.put("clothoId", piPers.getId());
                    String firstInstitutionId = (piPers.getInstitutions().size() > 0) ? piPers.getInstitutions().get(0) : "None";
                    if (!firstInstitutionId.equals("None")) {
                        PI.put("institutionName", ClothoAdapter.getInstitution(firstInstitutionId, clothoObject).getName());
                    }

                    String firstLabId = (piPers.getLabs().size() > 0) ? piPers.getLabs().get(0) : "None";
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
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Lab Id Cannot Be Blank");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();

        }

    }
}
