/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
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
public class EmailSaltHasherTest {
    
    public EmailSaltHasherTest() {
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
     * Test of getEmailSaltHasher method, of class EmailSaltHasher.
     */
    @Test
    public void testGetEmailSaltHasher() {
       
    }

    /**
     * Test of getNextSalt method, of class EmailSaltHasher.
     */
    @Test
    public void testGetNextSalt() {
      
    }

    /**
     * Test of hash method, of class EmailSaltHasher.
     */
    @Test
    public void testHash() {
        
    }

    /**
     * Test of generateSaltForUserAccount method, of class EmailSaltHasher.
     */
    @Test
    public void testGenerateSaltForUserAccount() {
       
    }

    /**
     * Test of csRandomAlphaNumericString method, of class EmailSaltHasher.
     */
    @Test
    public void testCsRandomAlphaNumericString() {
       
    }

    /**
     * Test of isExpectedPassword method, of class EmailSaltHasher.
     */
    @Test
    public void testIsExpectedPassword() {
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
       
       Person P1 = new Person();
       Person P2 = new Person();
       Person P3 = new Person();
       
       clothoObject.logout();
       ClothoAdapter.createPerson(P2, clothoObject);
       ClothoAdapter.createPerson(P3, clothoObject);
       //you are logged out now
       
       
       List <Person> colls = new ArrayList<>();
       colls.add(P2);
       colls.add(P3);
       P1.setColleagues(colls);
       P1.setFirstName("Anna");
       P1.setLastName("Goncharova");
       
             
        
    }

    /**
     * Test of generateRandomPassword method, of class EmailSaltHasher.
     */
    @Test
    public void testGenerateRandomPassword() {
       
    }
    
}
