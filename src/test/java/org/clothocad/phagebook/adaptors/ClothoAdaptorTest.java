/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.model.Person;
import org.clothocad.phagebook.dom.Company;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.security.EmailSaltHasher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    }
    @Test
    public void anotherTest(){
                
       ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
       Clotho clothoObject = new Clotho(conn);
       
       Map createUserMap = new HashMap();
       String username = "test"+ System.currentTimeMillis() ;
       createUserMap.put("username", username);
       createUserMap.put("password", "password");
       
       System.out.println("Create User Map " + createUserMap.toString());
       clothoObject.createUser(createUserMap);
       
       
       Map loginMap = new HashMap();
       loginMap.put("username", username);
       loginMap.put("credentials", "password");
       
       System.out.println("Login Map " + loginMap);
       
       clothoObject.login(loginMap);
       
       Company amazon = new Company("Amazon");
       String companyId = (String) ClothoAdaptor.createCompany(amazon, clothoObject);
       amazon.setId(companyId);
       
       Product microscope = new Product("Microscope",amazon,4000);
       microscope.setDescription("Magnifies stuff BETTER");
       microscope.setQuantity(65);
       microscope.setGoodType(GoodType.INSTRUMENT);
       microscope.setProductURL("www.example.com");
          
       String productId = (String) ClothoAdaptor.createProduct(microscope, clothoObject);
       
       
       Company apple = new Company("Apple");
       String companyId2 = (String) ClothoAdaptor.createCompany(apple, clothoObject);
       apple.setId(companyId2);
       
       Product microscope2 = new Product("Microscope2",apple,2000);
       microscope2.setDescription("Magnifies stuff BETTER");
       microscope2.setQuantity(40);
       microscope2.setGoodType(GoodType.INSTRUMENT);
       microscope2.setProductURL("www.example2.com");
       
       String productId2 = (String) ClothoAdaptor.createProduct(microscope2, clothoObject);
       
       clothoObject.logout();
       conn.closeConnection();
       
       
    }
    
}
