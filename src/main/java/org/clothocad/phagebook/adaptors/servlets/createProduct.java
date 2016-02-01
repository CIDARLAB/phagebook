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
import org.clothocad.phagebook.dom.Company;
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
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        //TODO: we need to have an authentication token at some point
        
       Map createUserMap = new HashMap();
       String username = "test"+ System.currentTimeMillis() ;
       
       
       createUserMap.put("username", username);
       createUserMap.put("password", "password");
       
       
       clothoObject.createUser(createUserMap);
       
       Map loginMap = new HashMap();
       loginMap.put("username", username);
       loginMap.put("credentials", "password");  
       
       clothoObject.login(loginMap);
        String productUrl = request.getParameter("productUrl") ;
        String companyId = request.getParameter("company");  
        String goodType = request.getParameter("goodType");
        double cost;  
        int quantity;
        try{
            String costy = request.getParameter("cost");
            cost =  Double.parseDouble(costy);
        }
        catch (NumberFormatException e){
            cost = 0;
        }
        try{
            
            quantity = Integer.parseInt(request.getParameter("quantity"));
        }
        catch(NumberFormatException e){
            quantity = 0;
        }
                
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Company comp = new Company();
        boolean isValidRequest = false;
        if((!name.isEmpty() && (cost != 0) && (quantity != 0) && !companyId.isEmpty())){
            isValidRequest = true;
            comp = ClothoAdapter.getCompany(companyId, clothoObject);
            if (comp.getName().equals("Not Set")){
                isValidRequest = false;
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/plain");
                PrintWriter out = response.getWriter();
                out.print("No Company with that ID exists");
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
            prod.setQuantity(quantity);
            prod.setCompany(comp);
            prod.setDescription( (description != null) ? description : "Not Set");
            prod.setProductURL( (productUrl != null) ? productUrl: "Not Set");
            prod.setGoodType( GoodType.valueOf( (goodType != null) ? goodType : "INSTRUMENT"));
            
            
            //everything is set for that product
            ClothoAdapter.createProduct(prod, clothoObject);
            JSONObject product = new JSONObject();
            product.put("id", prod.getId());
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(product.toString());
                out.flush();
                out.close();
                
            clothoObject.logout();
            
                    
            
            
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
