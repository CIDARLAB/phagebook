/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.util;

import java.io.UnsupportedEncodingException;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.security.EmailSaltHasher;
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
public class CreatePersonInClotho {
    
    public CreatePersonInClotho() {
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
       @Test
       public void createPersonInClotho() throws UnsupportedEncodingException 
       {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Person doug1 = new Person();
            String  emailId = "sample@clothoLab.com";
            doug1.setEmailId(emailId);
            doug1.setActivated(true);
            doug1.setFirstName("Doug");
            doug1.setLastName("One");
            doug1.setPassword("Clotho");
            EmailSaltHasher salty = EmailSaltHasher.getEmailSaltHasher();
            String salt = EmailSaltHasher.csRandomAlphaNumericString();
            doug1.setSalt(salt);
        
            byte[] SaltedHashedEmail = salty.hash(doug1.getEmailId().toCharArray(), salt.getBytes("UTF-8"));

            doug1.setSaltedEmailHash(SaltedHashedEmail);
            ClothoAdapter.createPerson(doug1, clothoObject);
            
            
           

           
           
       
       }
}
