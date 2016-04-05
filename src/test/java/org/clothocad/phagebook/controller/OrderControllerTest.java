/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controller;

import java.util.ArrayList;
import java.util.List;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import static org.clothocad.phagebook.controller.Args.getResourcesFilepath;
import static org.clothocad.phagebook.controller.OrderController.getVendors;
import static org.clothocad.phagebook.controller.OrderController.getProducts;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
import org.json.JSONObject;
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

    
    //@Test
    public void testGetProducts(){
        //List<Product> products = getProducts("../orderTemplates/ProductTemplate.csv");
        //System.out.println(products);
    }
    
    @Test
    public void testGetVendors(){
        
        JSONArray arr = new JSONArray();
        JSONObject ven1 = new JSONObject();
        JSONObject ven2 = new JSONObject();
        ven1.put("Name", "Apple");
        ven1.put("Description", "Horrible Company");
        ven1.put("Contact", "Meh");
        ven1.put("Phone", "7778887878");
        ven1.put("URL", "www.banana.com");
        
        ven2.put("Name", "Microsoft");
        ven2.put("Description", "Best company in the world");
        ven2.put("Contact", "Inna");
        ven2.put("Phone", "6178179898");
        ven2.put("URL", "www.microsoft.com");
        
        arr.put(ven1);
        arr.put(ven2);
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        List<String> vendorsIds = new ArrayList<String>();
        vendorsIds = getVendors(arr, clothoObject);
        
        
    }
    /**
     * Test of getProducts method, of class OrderController.
     */
    //@Test
//    public void testGetProducts_String() {
//        System.out.println("getProducts");
//        String filename = "";
//        List<Product> expResult = null;
//        List<Product> result = OrderController.getProducts(filename);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getProducts method, of class OrderController.
//     */
//    //@Test
//    public void testGetProducts_List() {
//        System.out.println("getProducts");
//        List<String> csvLines = null;
//        List<Product> expResult = null;
//        List<Product> result = OrderController.getProducts(csvLines);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCompanies method, of class OrderController.
//     */
//    //@Test
//    public void testGetCompanies_String() {
//        System.out.println("getCompanies");
//        String filename = "";
//        List<Vendor> expResult = null;
//        List<Vendor> result = OrderController.getCompanies(filename);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCompanies method, of class OrderController.
//     */
//    //@Test
//    public void testGetCompanies_List() {
//        System.out.println("getCompanies");
//        List<String> csvLines = null;
//        List<Vendor> expResult = null;
//        List<Vendor> result = OrderController.getCompanies(csvLines);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    
    
}
