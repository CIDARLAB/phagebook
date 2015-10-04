/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controller;

import java.util.List;
import static org.clothocad.phagebook.controller.OrderController.getCompanies;
import static org.clothocad.phagebook.controller.OrderController.getProducts;
import org.clothocad.phagebook.dom.Company;
import org.clothocad.phagebook.dom.Product;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author innaturshudzhyan
 */
public class OrderControllerTest {
    
    public OrderControllerTest() {
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
    public void testGetProducts(){
        List<Product> products = getProducts("/Users/innaturshudzhyan/Desktop/template.csv");
        assertEquals(3,products.size());
        assertEquals("Microscope",products.get(0).getName());
    }
    
    @Test
    public void testGetCompanies(){
        List<Company> companies = getCompanies("/Users/innaturshudzhyan/Desktop/template1.csv");
        assertEquals(1,companies.size());
        assertEquals("Name",companies.get(0).getName());
    }
    
    
    /**
     * Test of getProducts method, of class OrderController.
     */
    //@Test
    public void testGetProducts_String() {
        System.out.println("getProducts");
        String filename = "";
        List<Product> expResult = null;
        List<Product> result = OrderController.getProducts(filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProducts method, of class OrderController.
     */
    //@Test
    public void testGetProducts_List() {
        System.out.println("getProducts");
        List<String> csvLines = null;
        List<Product> expResult = null;
        List<Product> result = OrderController.getProducts(csvLines);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompanies method, of class OrderController.
     */
    //@Test
    public void testGetCompanies_String() {
        System.out.println("getCompanies");
        String filename = "";
        List<Company> expResult = null;
        List<Company> result = OrderController.getCompanies(filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompanies method, of class OrderController.
     */
    //@Test
    public void testGetCompanies_List() {
        System.out.println("getCompanies");
        List<String> csvLines = null;
        List<Company> expResult = null;
        List<Company> result = OrderController.getCompanies(csvLines);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
    
}
