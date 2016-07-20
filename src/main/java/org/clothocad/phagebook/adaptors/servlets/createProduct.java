/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class createProduct extends HttpServlet {

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
        
        
        Object pProductUrl =  request.getParameter("productUrl") ;
        String productUrl =   pProductUrl != null ? (String) pProductUrl : "";
        
        Object pCompanyId = request.getParameter("company");  
        String companyId = pCompanyId != null ? (String) pCompanyId : "";
        
        Object pGoodType = request.getParameter("goodType");  
        String goodType = pGoodType != null ? (String) pGoodType : "";
        
        Object pCost = request.getParameter("cost");
        Double cost = pCost != null ? Double.parseDouble((String) pCost): -1.0d;
        
        Object pQuantity = request.getParameter("quantity");
        Integer quantity = pQuantity != null ? Integer.parseInt((String) pQuantity) : -1;
      
        Object pName = request.getParameter("name");
        String name = pName != null? (String) pName : "";
        
       
        Object pDescription = request.getParameter("description");
        String description = pDescription != null ? (String) pDescription: "";
        
        Vendor comp = new Vendor();
        boolean isValidRequest = false;
        if((!name.isEmpty() && (cost > 0) && (quantity > 0) && !companyId.isEmpty())){
            
            isValidRequest = true;
            comp = ClothoAdapter.getVendor(companyId, clothoObject);
            if (comp.getId().equals("")){
                isValidRequest = false;
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "No Company with that id exists");
                out.print(responseJSON.toString());
                out.flush();
                out.close();
                clothoObject.logout();
                
                return;
            }
        }
        
        if (isValidRequest){
            Product prod = new Product();
            prod.setName(name);
            prod.setCost(cost);
            prod.setInventory(quantity);
            prod.setCompanyId(comp.getId());
            prod.setDescription( description );
            prod.setProductURL( productUrl) ;
            prod.setGoodType( GoodType.valueOf( (!goodType.equals("")) ? goodType : "INSTRUMENT"));
            
            
            //everything is set for that product
            ClothoAdapter.createProduct(prod, clothoObject);
            JSONObject product = new JSONObject();
            product.put("id", prod.getId());
            product.put("name", prod.getName());
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(product);
                out.flush();
                out.close();
                
            clothoObject.logout();
            conn.closeConnection();
            
                    
            
            
        }else {
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
