/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.Date;
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
import org.clothocad.phagebook.dom.OrderStatus;
import org.clothocad.phagebook.dom.Project;
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
public class newOrderTest {
    
    public newOrderTest() {
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
     * Test of doPost method, of class newOrder.
     */
    @Test
    public void testNewOrder() throws Exception {
            
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            //CREATE THIS USER IN CLOTHO FIRST
            String username = "phagebook";
            String password = "backend";
            
            Map loginMap = new HashMap();
            loginMap.put("username"   , username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            
            //required properties
            
            String orderName = "test order";
            
            Person loggedInPerson = new Person();
            clothoObject.logout();
            ClothoAdapter.createPerson(loggedInPerson, clothoObject);
            Person PI1 = new Person();
            Person PI2 = new Person();
            ClothoAdapter.createPerson(PI1, clothoObject);
            ClothoAdapter.createPerson(PI2, clothoObject);
            String createdBy = loggedInPerson.getId();
            clothoObject.login(loginMap);
            Date date = new Date();
            Double budget = 100.0d;
            Integer orderLimit = 30; 
            
            
            
            Lab testLab = new Lab();
            List<String> leadPisOfLab = testLab.getLeadPIs(); 
            leadPisOfLab.add(PI1.getId());
            leadPisOfLab.add(PI2.getId());
            testLab.setLeadPIs(leadPisOfLab);
            
            ClothoAdapter.createLab(testLab, clothoObject);
            String labId = testLab.getId();
            
            Project affiliatedProject = new Project();
            ClothoAdapter.createProject(affiliatedProject, clothoObject);
            String associatedProjectId = affiliatedProject.getId();
            
            
            /*
            
                DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                                   PASSWORD: backend
            */
            
       
            
            Order order = new Order();
            order.setName(orderName);
            order.setCreatedById(createdBy);
            order.setDateCreated(date);
            order.setBudget(budget);
            order.setMaxOrderSize(orderLimit);
            order.setAffiliatedLabId(labId);
            order.setRelatedProjectId(associatedProjectId);
            order.setStatus(OrderStatus.INPROGRESS);
            
            ClothoAdapter.createOrder(order, clothoObject); // CREATED THE ORDER
                                                            // BUT I NOW NEED TO LINK IT TO THE USER
            Person creator = ClothoAdapter.getPerson(order.getCreatedById(), clothoObject);
            List<String> createdOrders = creator.getCreatedOrders();
            createdOrders.add(order.getId());
            
            clothoObject.logout();
            
            ClothoAdapter.setPerson(creator, clothoObject);
            
            
    }
    
}
