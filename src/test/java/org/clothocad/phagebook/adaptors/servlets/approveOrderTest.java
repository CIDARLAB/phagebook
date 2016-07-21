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
import org.clothocad.phagebook.dom.Order.OrderStatus;
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
public class approveOrderTest {
    
    public approveOrderTest() {
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
    public void testApproveOrder(){
        Object pOrderId =  "570b317932ea3b037dd690d4";	
        String orderId = pOrderId != null ? (String) pOrderId : "";
        
        Object pUserId = "570b17d732ea3b037dd69093";
        String userId = pUserId != null ? (String) pUserId : "";
        
        boolean isValid = false;
        if (!orderId.equals("") && !userId.equals("")){
            isValid = true;
        }
        
        if (isValid){
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
            clothoObject.login(loginMap);
            
            Order orderToApprove = ClothoAdapter.getOrder(orderId, clothoObject);
            List<String> receivedByList = orderToApprove.getReceivedByIds();
            String finalApprover = "";
            String fAEmailId = "";
            for (String id : receivedByList){
                if (id.equals(userId)){
                    finalApprover = id;
                    Person approver = ClothoAdapter.getPerson(id, clothoObject);
                    clothoObject.logout();
                    Map login2 = new HashMap();
                    fAEmailId = approver.getEmailId();
                    login2.put("username", fAEmailId);
                    login2.put("credentials", approver.getPassword());
                    clothoObject.login(login2);
                    List<String> approvedOrder = approver.getApprovedOrders();
                    List<String> submittedOrders = approver.getSubmittedOrders(); // need to add to approved and remove from submitted..
                    approvedOrder.add(orderToApprove.getId());
                    submittedOrders.remove(orderToApprove.getId());
                    clothoObject.logout();
                    ClothoAdapter.setPerson(approver, clothoObject);
                    clothoObject.login(loginMap);
                    orderToApprove.setStatus(OrderStatus.APPROVED);
                    ClothoAdapter.setOrder(orderToApprove, clothoObject);
                    
                    
                    
                }
                
            }
            
    }
    }
}

    

