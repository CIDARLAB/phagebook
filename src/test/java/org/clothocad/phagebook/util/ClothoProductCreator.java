/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.util;

import java.util.HashMap;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
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
public class ClothoProductCreator {
    
    
    public ClothoProductCreator() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
     @Test
     public void anotherTest(){
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
        
       
        //COMPANIES
        Company amazon = new Company("Amazon");
        Company apple = new Company("Apple");
        Company nike = new Company("Nike");
        
        //CLOTHO CREATION
        
        String companyId = (String) ClothoAdapter.createCompany(amazon, clothoObject);
        amazon.setId(companyId);
        
        String companyId2 = (String) ClothoAdapter.createCompany(apple, clothoObject);
        apple.setId(companyId2);
        
        String companyId3 = (String) ClothoAdapter.createCompany(nike, clothoObject);
        nike.setId(companyId3);
        
        
        //PRODUCT: TELESCOPE
        Product telescope = new Product("Telescope", amazon, 1000);
        //PROPERTIES
        telescope.setDescription("See the stars");
        telescope.setGoodType(GoodType.INSTRUMENT);
        telescope.setQuantity(23);
        telescope.setProductURL("www.google.com");
        //
        
        //PRODUCT: MICROSCOPE
        Product microscope = new Product("Microscope",amazon,4000);
        //PROPERTIES
        microscope.setDescription("Magnifies stuff");
        microscope.setQuantity(65);
        microscope.setGoodType(GoodType.INSTRUMENT);
        microscope.setProductURL("www.example.com");
        //
        //
        
        //PRODUCT: MICROSCOPEN
        Product microscopen = new Product("Microscope",nike,3000);
        //PROPERTIES
        microscopen.setDescription("Magnifies stuff but by Nike");
        microscopen.setQuantity(23);
        microscopen.setGoodType(GoodType.INSTRUMENT);
        microscopen.setProductURL("www.nike.com");
        //
        
        //PRODUCT: TELESCOPE2N
        Product telescope2n = new Product("Telescope2", nike, 100);
        //PROPERTIES
        telescope2n.setDescription("See the stars by Nike");
        telescope2n.setGoodType(GoodType.INSTRUMENT);
        telescope2n.setQuantity(12);
        telescope2n.setProductURL("www.nike.com");
        //
        //
        //PRODUCT: MICROSCOPE2 
        Product microscope2 = new Product("Microscope2",apple,2000);
        //PROPERTIES
        microscope2.setDescription("Magnifies stuff BETTER");
        microscope2.setQuantity(40);
        microscope2.setGoodType(GoodType.INSTRUMENT);
        microscope2.setProductURL("www.example2.com");
        //
        
        //PRODUCT: TELESCOPE2
        Product telescope2 = new Product("Telescope2", apple, 1000);
        //PROPERTIES
        telescope2.setDescription("See the stars");
        telescope2.setGoodType(GoodType.INSTRUMENT);
        telescope2.setQuantity(23);
        telescope2.setProductURL("www.google.com");
        //
               
        
        String telescopeID   = (String) ClothoAdapter.createProduct(telescope   , clothoObject);
        String microscopeID  = (String) ClothoAdapter.createProduct(microscope  , clothoObject);
        String microscopenID = (String) ClothoAdapter.createProduct(microscopen , clothoObject);   
        String microscope2ID = (String) ClothoAdapter.createProduct(microscope2 , clothoObject); 
        String telescope2ID =  (String) ClothoAdapter.createProduct(telescope2  , clothoObject);
        String telescope2nID = (String) ClothoAdapter.createProduct(telescope2n , clothoObject);
        
        clothoObject.logout();
        conn.closeConnection();
   }
}

