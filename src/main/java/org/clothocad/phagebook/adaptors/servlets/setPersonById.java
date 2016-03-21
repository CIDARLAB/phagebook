/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author azula
 */
public class setPersonById extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet setPersonById</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet setPersonById at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        
        Object pCartItems = request.getParameter("first");
        String cartItems = pCartItems != null ? (String) pCartItems : "";
       
        Object pUser = request.getParameter("loggedInUserId");
        String user = pUser != null ? (String) pUser : "";
        
        Object pOrderId = request.getParameter("orderId");
        String orderId = pOrderId != null ? (String) pOrderId : "";
        
        boolean isValid = false;
        
        if (!cartItems.equals("") && !user.equals("") && !orderId.equals("") ){
            isValid = true;
        }

        if (isValid){
            //NEED TO LOG INTO CLOTHO... better way TBA

            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            //
            
            //STEP 1, get the order we want to modify...
            //assuming valid order ID.
            Order editableOrder = ClothoAdapter.getOrder(orderId, clothoObject);
            //now we have the order object.
            JSONArray cartItemsJSONArray = new JSONArray(cartItems);
            //we have our JSONArray of products to add with discounts
            Map<String, Integer> items = editableOrder.getProducts(); //initialize, we want to add not replace
            Date date = new Date();
            for (int i = 0; i < cartItemsJSONArray.length(); i++){
                //process the information that we have
                
                Map<String, Double>  productItemMap = new HashMap<>();
                JSONObject obj = (JSONObject) cartItemsJSONArray.get(i);
                Product product = ClothoAdapter.getProduct(obj.getString("productId"), clothoObject);
                product.decreaseInventory(obj.getInt("quantity"));
                productItemMap.put( obj.getString("productId") , obj.getDouble("discount"));
                
                CartItem item = new CartItem();
                item.setDateCreated(date);
                item.setProductWithDiscount(productItemMap);
                
                ClothoAdapter.createCartItem(item, clothoObject);
                
                items.put(item.getId(), obj.getInt("quantity"));
                ClothoAdapter.setProduct(product, clothoObject);
                
                
            }
            //now have a CART ITEM ArrayList... all with ID's 
            
            
            editableOrder.setProducts(items);
            
            ClothoAdapter.setOrder(editableOrder, clothoObject);
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "successfully modified order object");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            

        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing parameters for servlet call");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
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