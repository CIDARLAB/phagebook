/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

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
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Herb
 */
public class removeProductsFromOrder extends HttpServlet {

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
        
        //I WILL BE GETTING A JSON ARRAY OF CART ITEM IDS THAT I NEED TO REMOVE FROM AN ORDER
        //AND A USER NAME THAT IS ASSOCIATED WITH THAT ORDER
        //AND THAT ORDER ID.
        
        Object pUser = request.getParameter("user");
        String user = pUser != null ? (String) pUser: "";
        
        Object pCartItems = request.getParameter("cartItems");
        String cartItems = pCartItems != null ? (String) pCartItems: "";
        
        //DIRECT ASSUMPTION THAT CART ITEMS IS An ARRAY of JSON OBJECTS
        //EX
        // ["ID1", "ID2", "ID3"];
       
        
        Object pOrderId = request.getParameter("orderId");
        String orderId = pOrderId != null ? (String) pOrderId: "";
        
        boolean isValid = false;
        if (!user.equals("")&& !cartItems.equals("") && !orderId.equals("")){
            isValid = true;
        }
        
        if (isValid){
            
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            
            Person userP = ClothoAdapter.getPerson(user, clothoObject);
            Order ord = ClothoAdapter.getOrder(orderId, clothoObject);
            List<String> cartItemsInOrder = ord.getProducts(); //CART ITEM ID 
            String[] cartItemsToRemove = cartItems.split(",");
            if (ord.getCreatedById().equals(userP.getId()) || ord.getReceivedByIds().contains(userP.getId())){
                
                for (int i = 0; i < cartItemsToRemove.length; i++)
                {
                    
                    
                    if (cartItemsInOrder.contains(cartItemsToRemove[i])){ //they key exists in the map 
                        //remove that specific Cart Item.
                        CartItem cItem = ClothoAdapter.getCartItem(cartItemsToRemove[i], clothoObject);
                        int quantity = cItem.getQuantity();

                        Product product = ClothoAdapter.getProduct(cItem.getProductId(), clothoObject);
                        product.increaseInventory(quantity);

                        ClothoAdapter.setProduct(product, clothoObject); //increase inventory for that product

                    
                        //DON'T KNOW HOW TO DELETE FROM CLOTHO...
                        //will unlink though from the cart item map...
                       cartItemsInOrder.remove(cartItemsToRemove[i]);



                    }

                }

                ord.setProducts(cartItemsInOrder); // set the new cart item map with the ones we don't want nixed
                ClothoAdapter.setOrder(ord, clothoObject);

            }
            
            
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Order should be updated, please verify");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
            
        }else 
        {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "missing servlet parameters");
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
