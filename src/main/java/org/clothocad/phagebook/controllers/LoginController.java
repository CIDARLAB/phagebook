/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;

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
import org.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Map;


@Controller
public class LoginController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        //response.setContentType("text/html;charset=UTF-8");
//        //try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//
//        //}
//    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @RequestMapping(value="/loginUser", method=RequestMethod.POST)
    public void loginUser(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws Exception {
        //processRequest(request, response);
        
        //changed gettting params
        String email = params.get("email");
        String password = params.get("password");
                
        boolean isValidRequest = false;
        if((!email.isEmpty()) && (!password.isEmpty())){
            isValidRequest = true;
        }
        
        
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        if(isValidRequest){
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map loginMap = new HashMap();
            loginMap.put("username", email);
            loginMap.put("credentials", password);
            clothoObject.logout();
            //should have been successful its null if not successful.
            Object NULL = null;
            
            
            boolean isLoggedIn = false;
            
            if(clothoObject.login(loginMap).equals(NULL)){
                System.out.println("Null OBJECT!!");
                isLoggedIn = false;
            }
            else{
                System.out.println("Reached here..");
                isLoggedIn = true;
            }
           
            System.out.println("abcd");
            
            
            Map clothoQuery = new HashMap();
            clothoQuery.put("emailId", email);
            Person loggedInPerson = null;
            if (isLoggedIn){
                System.out.println("is logged in");
                loggedInPerson = ClothoAdapter.queryPerson(clothoQuery, clothoObject, ClothoAdapter.QueryMode.EXACT).get(0);
            }

            
            if ( isLoggedIn && !loggedInPerson.equals(NULL))
            {
                if (loggedInPerson.isActivated())
                {

                    //return success, this means its a valid request
                    //response.setStatus(HttpServletResponse.SC_OK);

                    String idVal = (String) loggedInPerson.getId();
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("clothoId", idVal);
                    responseJSON.put("emailId", loggedInPerson.getEmailId());
                    responseJSON.put("activated", "true");
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(responseJSON);
                    out.flush();
                    out.close();
                }
                else
                {
                    //person is not activated in need to go to the email verification page
                    String idVal = (String) loggedInPerson.getId();
                    JSONObject responseJSON = new JSONObject();
                    responseJSON.put("clothoId", idVal);
                    responseJSON.put("emailId", loggedInPerson.getEmailId());
                    responseJSON.put("activated", "false");
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(responseJSON);
                    out.flush();
                    out.close();
                    
                }
            }else {
                //user did not make clotho return a login response...
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                JSONObject obj = new JSONObject();
                obj.put("message", "No user found with those credenteials");
                out.print(obj);
                out.flush();
                out.close();
            
            }
            conn.closeConnection();
        }
    }

    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>

}
