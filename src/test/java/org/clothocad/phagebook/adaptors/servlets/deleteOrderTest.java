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
public class deleteOrderTest {
    
    public deleteOrderTest() {
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

    
    

    /**
     * Test of doPost method, of class deleteOrder.
     */
    @Test
    public void testDoPost() throws Exception {
        
            String user = "5706fd7632ea39591521e7df";
            String orderId = "5706fef732ea39591521e7f6";
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            
            Person userP = ClothoAdapter.getPerson(user, clothoObject);
     
            
            List<String> userCreatedOrders = userP.getCreatedOrders();
            
            if (userCreatedOrders.contains(orderId)){
                userCreatedOrders.remove(orderId);
                userP.setCreatedOrders(userCreatedOrders);
                
                clothoObject.logout();
                ClothoAdapter.setPerson(userP, clothoObject);
            }
        
    }

    
    
}
