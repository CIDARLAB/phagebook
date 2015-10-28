/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.security;

import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Person;
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
public class PasswordProcessorTest {
    
    public PasswordProcessorTest() {
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
     * Test of createSalt method, of class PasswordProcessor.
     */
    @Test
    public void testCreateSalt() {
        
    }

    /**
     * Test of createHash method, of class PasswordProcessor.
     */
    @Test
    public void testCreateSaltedHash() throws Exception {
       
       ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
       Clotho clothoObject = new Clotho(conn);
       Person johan = new Person();
       johan.setEmailId("johanos@bu.edu");
       johan.setFirstName("Johan");
       johan.setLastName("Ospina");
       johan.setPassword("koala");
       
       
       //ClothoAdaptor.createPerson(johan, clothoObject);
  
    }

    /**
     * Test of validatePassword method, of class PasswordProcessor.
     */
    @Test
    public void testValidatePassword() {
       
    }
    
}
