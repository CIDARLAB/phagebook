/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Project;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author anna_g
 */
public class editProjectTest {
  
    public Clotho clothoObject;
    public ClothoConnection conn;

    public void clothoLogin(String un, String password){
      this.conn = new ClothoConnection(Args.clothoLocation);
      this.clothoObject = new Clotho(conn);

      Map createUserMap = new HashMap();
      String username = "test"+ System.currentTimeMillis() ;
      if(un.compareTo("username") == 0){
        createUserMap.put("username", username);

      }else{
        createUserMap.put("username", un);
      }
      createUserMap.put("password", password);

      clothoObject.createUser(createUserMap);
      
      Map loginMap = new HashMap();
      if(un.compareTo("username") == 0){
      loginMap.put("username", username);

      }else{
        loginMap.put("username", un);
      }
      loginMap.put("credentials", password);  

      clothoObject.login(loginMap);
    }
    /*
    ** Sets up Persons and Project
    **
    */
    public void make(){
      System.out.println("-----CREATE PERSON TEST-----");
        System.out.println("Making people.");
        clothoObject.logout(); //HAVE TO LOGOUT OF CLOTHO IF LOGGED IN BECAUSE 
                               //YOU CAN'T EDIT A PERSON OBJECT IF YOU ARE NOT
                               //LOGGED INTO CLOTHO AS THAT PERSON
        Person person1 = new Person();
        String idP1 = "";
        
        person1.setFirstName("Person");
        person1.setLastName("Creator");
        person1.setId(idP1);
        person1.setEmailId("Creator@gmail.com");
        person1.setPassword("person1");
        
        String creatorEmail = person1.getEmailId();
        String creatorPassword = person1.getPassword();
        
        String person1ID = ClothoAdapter.createPerson(person1, clothoObject);
        System.out.println(person1);
        System.out.println(person1ID);
        
        Person person2 = new Person();
        String idP2 = "";
        
        person2.setFirstName("Person");
        person2.setLastName("Member");
        person2.setId(idP2);
        person2.setEmailId("Member@gmail.com");
        person2.setPassword("person2");
        
        String person2ID = ClothoAdapter.createPerson(person2, clothoObject);
        System.out.println(person2);
        System.out.println(person2ID);
        
        Person person3 = new Person();
        String idP3 = "";
        
        person3.setFirstName("Person");
        person3.setLastName("Lead");
        person3.setId(idP3);
        person3.setEmailId("Lead@gmail.com");
        person3.setPassword("person3");
        
        String person3ID = ClothoAdapter.createPerson(person3, clothoObject);
        System.out.println(person3);
        System.out.println(person3ID);
        
        System.out.println();
        
        System.out.println("Making a project.");
        // Have to be logged in to Clotho as the Creator to create the 
        // project.
        this.clothoLogin(creatorEmail, creatorPassword);
        String projectName = "Project";
        Organization cidar = new Organization("CIDAR");
        Double projectBudget = 0.0;
        Grant grant = new Grant("NSF");
        String des = "This is a super cool Project!";
        
        Project project = new Project(person1, projectName, cidar, person2, projectBudget,
            grant, des);
        
        String projectID = ClothoAdapter.createProject(project, clothoObject);   
        
        System.out.println("Project had been made.");
        System.out.println(project);
        System.out.println(projectID);



        System.out.println("----------");
    }
    
    //(Person creator, String name, Organization lab, 
    //    Person lead, Double projectBudget, Grant projectGrant, String description)
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
       this.clothoLogin("username", "password");
       this.make();
    }
    
    @After
    public void tearDown() {
        clothoObject.logout();
        conn.closeConnection();
    }

    
    @Test
    public void testCreateCompany()
    {
      
    }
  
}
