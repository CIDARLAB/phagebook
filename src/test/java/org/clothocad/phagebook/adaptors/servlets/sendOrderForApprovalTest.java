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
import org.clothocad.phagebook.dom.Order;
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
public class sendOrderForApprovalTest {
    
    public sendOrderForApprovalTest() {
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
     * Test of processRequest method, of class sendOrderForApproval.
     */
    //@Test
    public void testTransfer() throws Exception {
            //use new Order Test to get an order ID. 
            String userId = "56f2edc2d4c6875fb6d32a51";
            String orderId = "56f2edc4d4c6875fb6d32a57";
            
            
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
         
            String username = "phagebook";
            String password = "backend";
            /*
            
                DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                                   PASSWORD: backend
            */
            Map loginMap = new HashMap();
            loginMap.put("username"   , username);
            loginMap.put("credentials", password);
            
            
            String newUserId = "";
            Person newLoggedInPerson = new Person();
            ClothoAdapter.createPerson(newLoggedInPerson, clothoObject);
            clothoObject.login(loginMap);
            Person loggedInPerson = ClothoAdapter.getPerson(userId, clothoObject);
            String newPersonId = newLoggedInPerson.getId();
            
            Order orderToTransfer = ClothoAdapter.getOrder(orderId, clothoObject);
            if (!orderToTransfer.getId().equals("")){
                List<String> receivedBys = orderToTransfer.getReceivedByIds();
                receivedBys.remove(loggedInPerson.getId()); //remove the user that is logged in from the order's receivedby
                receivedBys.add(newLoggedInPerson.getId()); //get the new person we want to attach.
                //this is just in order... now we gotta modify each person.
                
                List<String> loggedInPersonReceivedBy = loggedInPerson.getSubmittedOrders();
                loggedInPersonReceivedBy.remove(orderToTransfer.getId());
                List<String> newLoggedInPersonReceivedBy = newLoggedInPerson.getSubmittedOrders();
                newLoggedInPersonReceivedBy.add(orderToTransfer.getId());
                
                ClothoAdapter.setOrder(orderToTransfer, clothoObject);
                
                
                clothoObject.logout();
                ClothoAdapter.setPerson(loggedInPerson, clothoObject);
                ClothoAdapter.setPerson(newLoggedInPerson, clothoObject);
                }
    }
    
    
}
