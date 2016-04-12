/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import org.clothocad.phagebook.dom.Institution;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author azula
 */
public class loadColleagues extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        System.out.println("reached doGet");

        Object pUserId = request.getParameter("userId");
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
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(humans);
            out.flush();

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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "used on profile to load friends dynamically";
    }// </editor-fold>

}
