///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.clothocad.phagebook.adaptors.servlets;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.cidarlab.citationsapi.PhagebookCitation;
//import org.clothoapi.clotho3javaapi.Clotho;
//import org.clothoapi.clotho3javaapi.ClothoConnection;
//import org.clothocad.model.Person;
//import org.clothocad.phagebook.adaptors.ClothoAdapter;
//import org.clothocad.phagebook.controller.Args;
//import org.clothocad.phagebook.dom.Status;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
///**
// *
// * @author innaturshudzhyan
// */
//public class loadPublications extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//
//        String clothoId = (String) request.getParameter("clothoId");
//
//        boolean isValid = false;
//        if (clothoId != null && clothoId != "") {
//            isValid = true;
//        }
//
//        if (isValid) {
//        
//
//            
//            
////            //ESTABLISH CONNECTION
//            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
//            Clotho clothoObject = new Clotho(conn);
//            Map createUserMap = new HashMap();
//            String username = "test" + System.currentTimeMillis();
//            createUserMap.put("username", username);
//            createUserMap.put("password", "password");
//            clothoObject.createUser(createUserMap);
//            Map loginMap = new HashMap();
//            loginMap.put("username", username);
//            loginMap.put("credentials", "password");
//            clothoObject.login(loginMap);
//
//            Person person = ClothoAdapter.getPerson(clothoId, clothoObject);
//            
//            JSONObject phagebookCitationsAsJSON = new JSONObject();
//            phagebookCitationsAsJSON.put("phagebookCitationIdJSON", person.getPhagebookCitations());
//            
//            JSONArray citations = new JSONArray();
//            
//            Map<Integer,List<PhagebookCitation>> yearCitationsMap = new HashMap<Integer,List<PhagebookCitation>>();
//            List<Integer> years = new ArrayList<Integer>();
//            for (String pubId : person.getPhagebookCitations())
//            {
//                PhagebookCitation pC = ClothoAdapter.getPhagebookCitation(pubId, clothoObject);
//                if(!years.contains(pC.getYear())){
//                    years.add(pC.getYear());
//                }
//                if(!yearCitationsMap.containsKey(pC.getYear())){
//                    yearCitationsMap.put(pC.getYear(), new ArrayList<PhagebookCitation>());
//                    
//                }
//                yearCitationsMap.get(pC.getYear()).add(pC);
//                /*JSONObject pubJSON = new JSONObject();
//                
//                pubJSON.put("pubTitle", pC.getTitle());
//                pubJSON.put("pubAuthors", pC.getAuthors());
//                pubJSON.put("pubYear", pC.getYear());
//                pubJSON.put("pubInfo", pC.getOtherInformation());
//                pubJSON.put("pubBibtex", pC.getBibtex());
//                citations.put(pubJSON);
//                */
//            }
//            
//            for(int i=0;i<years.size();i++){
//                for(int j=0;j<years.size();j++){
//                    if(years.get(i) > years.get(j)){
//                        int temp = years.get(i);
//                        years.set(i,years.get(j));
//                        years.set(j,temp);
//                    }
//                }
//            }
//            
//            List<PhagebookCitation> allCitations = new ArrayList<PhagebookCitation>();
//            for(int i=0;i<years.size();i++){
//                allCitations.addAll(yearCitationsMap.get(years.get(i)));
//            }
//            
//            for(PhagebookCitation pC:allCitations){
//                JSONObject pubJSON = new JSONObject();
//                
//                pubJSON.put("pubTitle", pC.getTitle());
//                pubJSON.put("pubAuthors", pC.getAuthors());
//                pubJSON.put("pubYear", pC.getYear());
//                pubJSON.put("pubInfo", pC.getOtherInformation());
//                pubJSON.put("pubBibtex", pC.getBibtex());
//                citations.put(pubJSON);
//            }
//            
//            
//            
//            response.setContentType("application/json");
////            
////            String id = person.getPhagebookCitations().get(0);
////            
////            PhagebookCitation p = ClothoAdapter.getPhagebookCitation(id, clothoObject);
//            
//            PrintWriter writer = response.getWriter();
//        
//            writer.println(citations);
//            writer.flush();
//            writer.close();
//            
////            Person retrieve = ClothoAdapter.getPerson(clothoId, clothoObject);
////            
////            JSONObject statusesAsJSON = new JSONObject();
////        
////            statusesAsJSON.put("statusIdJSON", retrieve.getStatuses());
////            JSONArray statuses = new JSONArray();
////            for ( String statId : retrieve.getStatuses()){
////                Status tempStat = ClothoAdapter.getStatus(statId, clothoObject);
////                
////                JSONObject statJSON = new JSONObject();
////                
////                statJSON.put("statusText", tempStat.getText());
////                statJSON.put("dateCreated", tempStat.getCreated().toString());
////                
////                statuses.put(statJSON);
////            }
////            response.setContentType("application/json");
////            PrintWriter out = response.getWriter();
////            out.print(statuses);
////            out.flush();
////            out.close();
//                conn.closeConnection();
//        } else {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//
//        }
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "used by the profile load to display statuses of the user";
//    }// </editor-fold>
//
//}
