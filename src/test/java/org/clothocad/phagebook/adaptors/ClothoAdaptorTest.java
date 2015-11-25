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
    
    public void anotherTest(){
                
       ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
       Clotho clothoObject = new Clotho(conn);
       
//       Map createUserMap = new HashMap();
//       String username = "test"+ System.currentTimeMillis() ;
//       createUserMap.put("username", username);
//       createUserMap.put("password", "password");
//       
//       System.out.println("Create User Map " + createUserMap.toString());
//       clothoObject.createUser(createUserMap);
//       
//       
//       Map loginMap = new HashMap();
//       loginMap.put("username", username);
//       loginMap.put("credentials", "password");
//       
//       System.out.println("Login Map " + loginMap);
//       
//       clothoObject.login(loginMap);
       
//       Company amazon = new Company("Amazon");
//       String companyId = (String) ClothoAdaptor.createCompany(amazon, clothoObject);
//       amazon.setId(companyId);
//       
//       Product microscope = new Product("Microscope",amazon,4000);
//       microscope.setDescription("Magnifies stuff BETTER");
//       microscope.setQuantity(65);
//       microscope.setGoodType(GoodType.INSTRUMENT);
//       microscope.setProductURL("www.example.com");
//          
//       String productId = (String) ClothoAdaptor.createProduct(microscope, clothoObject);
//       
//       microscope.setDescription("Magnifies stuff AND I CHANGED DESC");
//       microscope.setQuantity(20);
//       String productId2 = (String) ClothoAdaptor.createProduct(microscope, clothoObject);
//       
//       clothoObject.logout();
       
       Person person = new Person();
       person.setEmailId("johanospina@me.com");
       person.setFirstName("Johan");
       person.setLastName("Ospina");
       person.setPassword("123");
       person.setSalt("SP3zzlOq9Z1xUdsJnAWHPaHx8");
       
       person.setSaltedEmailHash("\"j��2y��{^����`����4�ř�Uʭ'�z�~\"");
       
       ClothoAdaptor.createPerson(person, clothoObject);
        //Product prod = (Product) ClothoAdaptor.getProduct(productId, clothoObject);
        Map query = new HashMap();
        query.put("emailId", "johanospina@me.com");
        List<Person> people = ClothoAdaptor.queryPerson(query, clothoObject);
        for(Person pers : people){
            System.out.println(pers.getEmailId());
        }
    }
    
}
