/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.Order;
import org.json.JSONObject;
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
public class submitOrderToPIsTest {
    
    public submitOrderToPIsTest() {
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
     * Test of processRequest method, of class submitOrderToPIs.
     */
    //@Test
    public void testSubmitOrderToPIs() throws Exception {
        
        /*
            RUN IN CONJUNCTION OF NEW ORDER TEST FILE SO YOU HAVE A VALID ORDER ID
        */
            //login
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "phagebook";
            String password = "backend";
            /*
            
                DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                                   PASSWORD: backend
            */
            Map loginMap = new HashMap();
            loginMap.put("username"   , username);
            loginMap.put("credentials", password);
            
            
            clothoObject.login(loginMap);
            //MAKE A LAB THAT HAS PI's 
           
            
            String orderId = "56df086ad4c6def5c7c960f2";
            
            
            
            Order orderToSubmit = ClothoAdapter.getOrder(orderId, clothoObject);
            if (!orderToSubmit.getId().equals("")){
                
                List<Person> PIs = new ArrayList<>();
                Lab orderLab = ClothoAdapter.getLab(orderToSubmit.getAffiliatedLabId(), clothoObject);
                
                for (String PIid : orderLab.getLeadPIs()){
                    Person pi = ClothoAdapter.getPerson(PIid, clothoObject); //gets that person object
                    
                    List<String> submittedOrders = pi.getSubmittedOrders(); // we don't want to replace, we want to add on. 
                    submittedOrders.add(orderToSubmit.getId()); //add our order Id to that list for each person
                    PIs.add(pi); // add that pi to a list. 
                    List<String> receivedByIdsForOrder = orderToSubmit.getReceivedByIds(); //get the list of people who have received the order
                    receivedByIdsForOrder.add(PIid); // add the current PIid to that list. 
                    
                }
                
                
                // have all the people that the order should be submitted to that are PI's of that lab
                
                //add those to both order and each person.
                //TODO ADD THIS...
                clothoObject.logout();
                for (Person pi : PIs){
                    Map piMap = new HashMap();
                    piMap.put("username"   , pi.getEmailId());
                    piMap.put("credentials", pi.getPassword());
                    ClothoAdapter.setPerson(pi, clothoObject);
                    clothoObject.logout();
                }
                clothoObject.login(loginMap);
                ClothoAdapter.setOrder(orderToSubmit, clothoObject);
                
                
                       
                
                
                
                
            }
            else {
            
                fail("Order does not exist");
                
            }
        }
        
    

   

}
