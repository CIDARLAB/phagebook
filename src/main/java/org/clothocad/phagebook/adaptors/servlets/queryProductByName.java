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
import net.sf.json.JSONSerializer;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Product;


/**
 *
 * @author Herb
 */
public class queryProductByName extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
       
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
        
        Object pProductName = request.getParameter("name");
        String productName = pProductName != null ? (String) pProductName : "";
        
        Object pSearchType = request.getParameter("searchType");
        String searchType = pSearchType != null ? (String) pProductName: "";
        
        boolean isValidRequest = false;
        if (!productName.equals("") && !productName.equals("")){
            isValidRequest = true;
        }
        
        if (isValidRequest){
         
             //create a clothoUser and Login to Query
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            ClothoAdapter.QueryMode Qmode = ClothoAdapter.QueryMode.valueOf(searchType);
            Map loginMap = new HashMap();
            loginMap.put("username", "phagebook");
            loginMap.put("credentials", "backend");
            clothoObject.login(loginMap);

            //Query for the products
            
             Map query = new HashMap();
            
            switch (Qmode){
                case EXACT:
                    query.put("name", productName);
                    break;
                case STARTSWITH:
                     query.put("query", productName); // the value for which we are querying.
                     query.put("key", "name"); // the key of the object we are querying
       
                    break;
                default:
                    break;
                    
            }
            
            List<Product> queryProductResults = ClothoAdapter.queryProduct(query, clothoObject, ClothoAdapter.QueryMode.valueOf(searchType));

            //To get Vendor Name...
            JSONArray results = new JSONArray();
            for (Product product : queryProductResults){
                JSONObject productAsJson = new JSONObject();
                productAsJson.put("clothoID", product.getId());
                productAsJson.put("cost", product.getCost());
                productAsJson.put("productURL", (product.getProductURL() != null) ? product.getProductURL() : "");
                productAsJson.put("goodType", product.getGoodType());
                productAsJson.put("inventory", product.getInventory());
                productAsJson.put("name", product.getName());
                productAsJson.put("description", product.getDescription());
                productAsJson.put("vendor", ClothoAdapter.getVendor(product.getCompanyId(), clothoObject));
                
                results.add(productAsJson);
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
