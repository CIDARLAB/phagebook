/*

package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Herb

public class queryFirstLastName extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        Object pFirstName = request.getParameter("firstName");
        String firstName = pFirstName != null ? (String) pFirstName: "";
        
        Object pLastName = request.getParameter("lastName");
        String lastName = pLastName != null ? (String) pLastName: "";
        
        boolean isValid = false;
        if (!firstName.equals("") || !lastName.equals("")){
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
            Map query1 = new HashMap();
            Map query2 = new HashMap();
            if (!firstName.equals("")){
                query1.put("query", firstName); // the value for which we are querying.
                query1.put("key", "firstName");
            }
            if (!lastName.equals("")){
                query2.put("query", lastName); // the key of the object we are querying
                query2.put("key", "lastName");
            }       
            
           
            List<Person> peopleFirstName = ClothoAdapter.queryPerson(query1, clothoObject, ClothoAdapter.QueryMode.STARTSWITH);
            List<Person> peopleLastName  = ClothoAdapter.queryPerson(query2, clothoObject, ClothoAdapter.QueryMode.STARTSWITH);
            List<Person> combinedList = new ArrayList<Person>(peopleFirstName);
            combinedList.addAll(peopleLastName);
            Set<Person> hs = new HashSet<>();
            hs.addAll(combinedList);
            combinedList.clear();
            combinedList.addAll(hs);
            
            
            JSONArray peopleJSONArray = new JSONArray();
            
            for (Person retrieve : combinedList){
                JSONObject retrievedAsJSON = new JSONObject();
                retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
                //get position? role?? we will look into this
                retrievedAsJSON.put("firstName", retrieve.getFirstName());
                retrievedAsJSON.put("lastName", retrieve.getLastName());
                retrievedAsJSON.put("clothoId", retrieve.getId());


                JSONArray institutionList = new JSONArray();
                
                if (retrieve.getInstitutions() != null){
                    for (String institution: retrieve.getInstitutions()){
                        Institution inst = ClothoAdapter.getInstitution(institution, clothoObject);
                        institutionList.put(inst.getName());
                    }
                }
                JSONArray labList = new JSONArray();
                if (retrieve.getLabs() != null){
                    for (String lab:retrieve.getLabs()){
                        Lab labo = ClothoAdapter.getLab(lab, clothoObject);
                        labList.put(labo.getName());
                    }
                }
                
                retrievedAsJSON.put("mainInstitution" , ((institutionList.length() == 0) ? "Other" : institutionList.get(0)));
                retrievedAsJSON.put("mainLab", ((labList.length() == 0) ? "Other" : labList.get(0)));
                
                //retrievedAsJSON.put("labList", labList);
                peopleJSONArray.put(retrievedAsJSON);
            }
            
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            
            PrintWriter out = response.getWriter();
            out.print(peopleJSONArray);
            out.flush();
            
        }
        else
        {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "need parameters to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}


*/