/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.clothocad.phagebook.dom.Order;
import static org.junit.Assert.*;

/**
 *
 * @author innaturshudzhyan
 */
public class OrderTest {
    
    public OrderTest() {
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
     * Test of getProducts method, of class Order.
     */
    @Test
    public void testCreateOrder() {

    
    }

    /**
     * Test of setProducts method, of class Order.
     */
    
    //@org.junit.Test
    public void testSetProducts() {
    }

    /**
     * Test of getId method, of class Order.
     */
    //@org.junit.Test
    public void testGetId() {
        System.out.println("getId");
        Order instance = null;
        String expResult = "";
        String result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class Order.
     */
    //@org.junit.Test
    public void testGetName() {
        System.out.println("getName");
        Order instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class Order.
     */
    //@org.junit.Test
    public void testSetId() {
        System.out.println("setId");
        String id = "";
        Order instance = null;
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setName method, of class Order.
     */
    //@org.junit.Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        Order instance = null;
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
