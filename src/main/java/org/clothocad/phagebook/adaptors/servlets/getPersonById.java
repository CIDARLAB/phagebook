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
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/**
 *
 * @author Allison Durkan
 */
public class getPersonById extends HttpServlet {

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
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");
            clothoObject.login(loginMap);
            //

            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);

            JSONObject retrievedAsJSON = new JSONObject();
            retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
            //get position? role?? we will look into this
            retrievedAsJSON.put("firstName", retrieve.getFirstName());
            retrievedAsJSON.put("lastName", retrieve.getLastName());
            retrievedAsJSON.put("loggedUserId", retrieve.getId());
            retrievedAsJSON.put("institution", retrieve.getInstitution());
            retrievedAsJSON.put("department", retrieve.getDepartment());
            retrievedAsJSON.put("title", retrieve.getTitle());
            
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
            PrintWriter out = response.getWriter();
            out.print(retrievedAsJSON);
            out.flush();
            out.close();

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
        return "Short description";
    }// </editor-fold>

}
