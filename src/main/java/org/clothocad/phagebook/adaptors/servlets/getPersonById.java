/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.xml.bind.JAXBIntrospector.getValue;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.model.Person.PersonRole;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONObject;
import static org.json.JSONObject.NULL;

/**
 *
 * @author Allison Durkan
 */
public class getPersonById extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

        System.out.println("reached doGet");

        String userId = (String) request.getParameter("userId");
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
            clothoObject.logout();
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");
            clothoObject.login(loginMap);
            //
            System.out.println("Simon says :: Login Complete");
            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            Institution institute = ClothoAdapter.getInstitution(retrieve.getInstitution(), clothoObject);
            JSONObject retrievedAsJSON = new JSONObject();
            retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
            System.out.println("passed sucks");
            //get position? role?? we will look into this
            retrievedAsJSON.put("firstName", retrieve.getFirstName());
            retrievedAsJSON.put("lastName", retrieve.getLastName());
            retrievedAsJSON.put("loggedUserId", retrieve.getId());
            retrievedAsJSON.put("institution", institute.getName());
            retrievedAsJSON.put("department", retrieve.getDepartment());
            retrievedAsJSON.put("title", retrieve.getTitle());
            retrievedAsJSON.put("email", retrieve.getEmailId());
            retrievedAsJSON.put("profileDescription", retrieve.getProfileDescription());
            retrievedAsJSON.put("statuses", retrieve.getStatuses());
            retrievedAsJSON.put("listColleagueRequests", retrieve.getColleagueRequests());
            String labId = retrieve.getLabs().size() > 0 ? retrieve.getLabs().get(0) : "0";
         
            retrievedAsJSON.put("lab", ClothoAdapter.getLab(labId, clothoObject));
            System.out.println("this is our JSON obj");
            System.out.println(retrievedAsJSON);

            JSONObject statusList = new JSONObject();
            if (retrieve.getStatuses() != null) {
                for (String status : retrieve.getStatuses()) {
                    Status stat = ClothoAdapter.getStatus(status, clothoObject);

                    statusList.put("text", stat.getText());
                    statusList.put("date", stat.getCreated().toString());
                }
            }

            JSONObject publicationList = new JSONObject();
            if (retrieve.getPublications() != null) {

                for (String publication : retrieve.getPublications()) {
                    Publication pub = ClothoAdapter.getPublication(publication, clothoObject);
                    publicationList.put("id", pub.getId());
                }
            }
            /*
            JSONObject labList = new JSONObject();
            if (retrieve.getLabs() != null) {
                for (String lab : retrieve.getLabs()) {
                    Institution inst = ClothoAdapter.getInstitution(lab, clothoObject);
                    labList.put("name", inst.getName());
                    Set<PersonRole> rolesAtInstitution = retrieve.getRole(lab);
                    JSONObject positions = new JSONObject();
                    Iterator<PersonRole> it = rolesAtInstitution.iterator();
                    while (it.hasNext()) {
                        positions.put(inst.getName(), it.next());
                    }
                    labList.put("roles", positions);
                }
            }
             */
            retrievedAsJSON.put("statusList", statusList);
            retrievedAsJSON.put("publicationList", publicationList);
            //retrievedAsJSON.put("labList", labList);
            System.out.println("looking at retrieved");
            System.out.println(retrievedAsJSON);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(retrievedAsJSON);
            out.flush();
            out.close();
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }

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
        //processRequest(request, response);
        processRequest(request, response);

        System.out.println("reached doPost");
        /* Map myMap = request.getParameterMap();

        Iterator iterator = myMap.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            System.out.println(myMap.get(key).toString());
            System.out.println(key);
        }*/

        Object pUserId = request.getParameter("clothoId");
        String userId = pUserId != null ? (String) pUserId : "";

        Object pNewFirstName = request.getParameter("editFirstName");
        String newFirstName = pNewFirstName != null ? (String) pNewFirstName : "";

        Object pNewLastName = request.getParameter("editLastName");
        String newLastName = pNewLastName != null ? (String) pNewLastName : "";

        /*Object pNewInstitution = request.getParameter("editInstitution");
        String newInstitution = pNewInstitution != null ? (String) pNewInstitution: "";
         */
        Object pNewDepartment = request.getParameter("editDepartment");
        String newDepartment = pNewDepartment != null ? (String) pNewDepartment : "";

        Object pNewTitle = request.getParameter("editTitle");
        String newTitle = pNewTitle != null ? (String) pNewTitle : "";
/*
        Object pNewLab = request.getParameter("editLab");
        String newLab = pNewLab != null ? (String) pNewLab : "";

        Object pLabId = request.getParameter("lab");
        String labId = pLabId != null ? (String) pLabId : "";*/

        Object pProfileDescription = request.getParameter("editProfileDescription");
        String newProfileDescription = pProfileDescription != null ? (String) pProfileDescription : "";

        System.out.println(userId + " " + newFirstName + newLastName + " " + newDepartment + " " + newTitle + " " + newProfileDescription);
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
            if (!retrieve.getId().equals("")) {
                if (!newFirstName.equals("")) {
                    retrieve.setFirstName(newFirstName);
                }

                if (!newLastName.equals("")) {
                    retrieve.setLastName(newLastName);
                }

                /*if (newEmail != NULL && !"".equals(newEmail)){
                
            }
            
            if (newPassword != NULL && !"".equals(newPassword)){
                
            }*/
 /* if (newInstitution != NULL && !"".equals(newInstitution)){
                List<String> institutions = retrieve.getInstitutions();
                institutions.add(institutionId);
                retrieve.setInstitutions(institutions);
                editPerson = true;
            }*/
                if (!newDepartment.equals("")) {
                    retrieve.setDepartment(newDepartment);
                }

                if (!newTitle.equals("")) {
                    retrieve.setTitle(newTitle);
                }
                System.out.println("new profile description");
                System.out.println(newProfileDescription);
                if (!newProfileDescription.equals("")) {
                    System.out.println("we are inside set profile desc");
                    retrieve.setProfileDescription(newProfileDescription);
                }

                /*  if (newLab != NULL && !"".equals(newLab)) {
                List<String> labs = retrieve.getLabs();
                labs.add(labId);
                retrieve.setLabs(labs);
                editPerson = true;
            }*/
                clothoObject.logout();
                ClothoAdapter.setPerson(retrieve, clothoObject);

            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Allows people to update their profile settings";
    }// </editor-fold>

}
