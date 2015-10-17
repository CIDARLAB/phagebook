/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import static org.clothocad.phagebook.adaptors.ClothoAdaptor.createProduct;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Company;
import org.clothocad.phagebook.dom.GoodType;
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
public class ClothoAdaptorTest {
    
    public ClothoAdaptorTest() {
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
     * Test of createProduct method, of class ClothoAdaptor.
     */
    @Test
    public void testCreateProduct() {
        
       ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
       Clotho clothoObject = new Clotho(conn);
       Map createUserMap = new HashMap();
       String username = "test"+ System.currentTimeMillis() ;
       createUserMap.put("username", username);
       createUserMap.put("password", "password");
       
       clothoObject.createUser(createUserMap);
       
       
       Map loginMap = new HashMap();
       loginMap.put("username", username);
       loginMap.put("credentials", "password");
       
       clothoObject.login(loginMap);
       
       Company amazon = new Company("Amazon");
       amazon.setId("org.company.amazon");
       Product microscope = new Product("Microscope",amazon,4000);
       microscope.setDescription("Magnifies stuff BETTER");
       microscope.setQuantity(65);
       microscope.setGoodType(GoodType.INSTRUMENT);
       microscope.setProductURL("www.example.com");
       
        String productId = (String) createProduct(microscope,clothoObject);
        
        //This makes that object 
        List<String> add = new ArrayList<String>();
        List<String> remove = new ArrayList<String>();
        
        add.add("public");
        
        Map grantMap = new HashMap();
        grantMap.put("id", productId);
        grantMap.put("user", "none");
        grantMap.put("add", add);
        grantMap.put("remove", remove);
        
        Map grantResult = new HashMap();
        grantResult = (Map)(clothoObject.grant(grantMap));
        // make this a function.
        //Company comp = (Company) ClothoAdaptor.getCompany("",clothoObject);
         
       conn.closeConnection();
                
       
       
       
    }
    
}
