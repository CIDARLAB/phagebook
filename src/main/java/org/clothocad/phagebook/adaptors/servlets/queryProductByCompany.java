/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Company;
import org.clothocad.phagebook.dom.Product;

/**
 *
 * @author Herb
 */
public class queryProductByCompany extends HttpServlet {

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
        String companyName = ""; 
        companyName = request.getParameter("Name");
        boolean isValidRequest = false;
        if (companyName != "" && companyName != null){
            isValidRequest = true;
        }
        
        if (isValidRequest){
            //create a clothoUser and Login to Query
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            createUserMap.put("username", "ClothoBackend");
            createUserMap.put("password", "phagebook");
            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", "ClothoBackend");
            loginMap.put("credentials", "phagebook");
            clothoObject.login(loginMap);

            //Query for the company
            
            Map query = new HashMap();
            
            query.put("name", companyName);
            
            List<Company> queryCompanyResults = new LinkedList<>();
            queryCompanyResults = ClothoAdapter.queryCompany(query, clothoObject);
            //To get Company Name and ID to query for products with that company...
            List<String> companyIDs = new LinkedList<>();
            for (Company company : queryCompanyResults ){
                companyIDs.add(company.getId());
            }
            
            
            
            JSONArray results = new JSONArray();
            for (String companyID : companyIDs)
            {
                Map queryForClotho = new HashMap();
                queryForClotho.put("company", companyID);
                List<Product> queryProductResults = ClothoAdapter.queryProduct(queryForClotho, clothoObject);
            
                for (Product product : queryProductResults){
                    JSONObject productAsJson = new JSONObject();
                    productAsJson.put("cost", product.getCost());
                    productAsJson.put("clothoID", product.getId());
                    productAsJson.put("productURL", (product.getProductURL() != null) ? product.getProductURL() : "");
                    productAsJson.put("goodType", (product.getGoodType() != null) ? product.getGoodType() : "");
                    productAsJson.put("quantity", product.getQuantity());
                    productAsJson.put("name", product.getName());
                    productAsJson.put("description", product.getDescription());
                    productAsJson.put("company", product.getCompany().getName());

                    results.add(productAsJson);
                }
            }
            
            
            if (!results.isEmpty()){
                
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(results.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            
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
        return "Short description";
    }// </editor-fold>

}
