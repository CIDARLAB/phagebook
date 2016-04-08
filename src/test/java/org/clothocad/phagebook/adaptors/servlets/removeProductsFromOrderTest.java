/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Herb
 */
public class removeProductsFromOrderTest {
    
    public removeProductsFromOrderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

   @Test
    public void testDoPost() throws Exception {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            
            
  
        String user =  "5706fd7632ea39591521e7df";
        
       
        String cartItem = "5707170332ea39591521e7fb";
        
        

        String orderId = "5706fef732ea39591521e7f6";
        
            Person userP = ClothoAdapter.getPerson(user, clothoObject);
            Order ord = ClothoAdapter.getOrder(orderId, clothoObject);
            List<String> cartItemsInOrder = ord.getProducts(); //CART ITEM ID 
            System.out.println("UNMODIFIED CART ITEMS: " +cartItemsInOrder.toString());
            if (ord.getCreatedById().equals(userP.getId()) || ord.getReceivedByIds().contains(userP.getId())){
                
               
                    
                    if (cartItemsInOrder.contains(cartItem)){ //they key exists in the map 
                        System.out.println("in this if statement");
                        //remove that specific Cart Item.
                        CartItem cItem = ClothoAdapter.getCartItem(cartItem, clothoObject);
                        int quantity = cItem.getQuantity();

                        Product product = ClothoAdapter.getProduct(cItem.getProductId(), clothoObject);
                        product.increaseInventory(quantity);

                        ClothoAdapter.setProduct(product, clothoObject); //increase inventory for that product
                       
                    
                        //DON'T KNOW HOW TO DELETE FROM CLOTHO...
                        //will unlink though from the cart item map...
                        cartItemsInOrder.remove(cartItem);
                        System.out.println("MODIFIED: "+ cartItemsInOrder.toString());
                       
                        ord.setProducts(cartItemsInOrder); // set the new cart item map with the ones we don't want nixed
                        ClothoAdapter.setOrder(ord, clothoObject);


                    }

                }
    
    }

    
}
