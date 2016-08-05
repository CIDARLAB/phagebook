/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Vendor;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class getProductById extends HttpServlet {

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
        
        
       String ids = request.getParameter("ids");
      
       
       boolean isValidRequest = false;
       if (ids != "" && ids != null){
           isValidRequest = true;
       }
       // items = [565f5518d4c61fb21a163eac, 565f5518d4c61fb21a163eaa, 565f5518d4c61fb21a163eab, 565f5518d4c61fb21a163eac]
       
       
       
        //ESTABLISH CONNECTION
       if (isValidRequest)
       {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "test"+ System.currentTimeMillis() ;
            createUserMap.put("username", username);
            createUserMap.put("password", "password");
            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");     
            clothoObject.login(loginMap);
       //
            List<String> productsAsStrings = Arrays.asList(ids.split("\\s*,\\s*"));
            System.out.println(productsAsStrings);
            List<Product> products = new LinkedList<>();
           
            //Populate the number of Products in a JSONArray
            JSONArray responseData = new JSONArray();
            
            for (int i = 0; i < productsAsStrings.size(); i++) {
                    JSONObject product = new JSONObject();
                    JSONObject productAtIndex = new JSONObject();
                    Product temp = ClothoAdapter.getProduct(productsAsStrings.get(i), clothoObject);
                    productAtIndex.put("clothoID", temp.getId());
                    productAtIndex.put("name", temp.getName());
                    Vendor comp = ClothoAdapter.getVendor(temp.getCompanyId(), clothoObject);
                    productAtIndex.put("company", comp.getName());
                    productAtIndex.put("cost", temp.getCost());
                    productAtIndex.put("description", temp.getDescription());
                    productAtIndex.put("goodType", temp.getGoodType());
                    productAtIndex.put("url", temp.getProductURL());
                    productAtIndex.put("quantity", temp.getInventory());
                    
                    product.put("product"+i, productAtIndex);
                    
                    responseData.add(i, temp);
                    System.out.println(responseData.toString());
                    for (int j = 0 ; j < responseData.size(); j++){
                        System.out.println(responseData.get(i));
                    }
                    
	    } 
            
            
            if (!responseData.isEmpty()){
                
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(responseData.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        conn.closeConnection();  
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
