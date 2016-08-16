/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.websocket;

import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.dom.Project;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author KatieLewis
 */
public class createUsers {
   
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
    public void createPerson(){
        Person person1 = new Person();
        person1.setEmailId("kmlewis");
        person1.setPassword("abc");
        person1.setFirstName("Katie");
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        String id = ClothoAdapter.createPerson(person1, clothoObject);
        System.out.println("createUser :: " + person1);
        Person getPerson = ClothoAdapter.getPerson(id, clothoObject);
        System.out.println("get person :: " + getPerson);
        //person1.setId(id);
        //How can I set the person ID as a property so that I can retrieve it later?
        //Person user = ClothoAdapter.getPerson(id, clothoObject);
        //Organization BU = new Institution("BU");
        //Project project = new Project(user, "Phagebook",BU , "Social Synbio project");
       // String projectID = ClothoAdapter.createProject(project, clothoObject);
        //project.setId(projectID);
    }//
}
