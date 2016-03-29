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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Product;

/**
 *
 * @author Herb
 */
public class parseCartItem extends HttpServlet {

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
        
        Object pUser = request.getParameter("user");
        String user = pUser != null ? (String) pUser: "";
        
        Object pCartItemId = request.getParameter("cartItem");
        String cartItemId = pCartItemId != null ? (String) pCartItemId: "";
        boolean isValid = false;
        
        
        if (!user.equals("") && !cartItemId.equals("")){
            isValid = true;
        }
        JSONObject responseJSON = new JSONObject();
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
            
            CartItem cartItem = ClothoAdapter.getCartItem(cartItemId, clothoObject);
            
            
            Iterator it = cartItem.getProductWithDiscount().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Product product = ClothoAdapter.getProduct((String) pair.getKey(), clothoObject);
                responseJSON.put("discount", pair.getValue());
                responseJSON.put("productName", product.getName());
                responseJSON.put("productUnitPrice", product.getUnitPrice());
            }
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            
        }
        else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJSON.put("message", "id provided does not exist");
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
