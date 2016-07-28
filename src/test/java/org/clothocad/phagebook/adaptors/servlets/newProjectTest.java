/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
//import static org.clothocad.phagebook.adaptors.servlets.editProjectTest.clothoObject;
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
public class newProjectTest {
    static Project project = new Project();
    static ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
    static Clotho clothoObject = new Clotho(conn);

    
    
    @BeforeClass
    public static void setUpClass() {
      
        Map createUserMap = new HashMap();
        String username = "username";
        String password = "password";
        
        createUserMap.put("username", username);
        createUserMap.put("password", password);

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);  

//        clothoObject.login(loginMap);
        
        System.out.println("Making a person!");
        
        Person person = new Person();

        person.setFirstName("Anna");
        person.setLastName("Goncharova");
        person.setEmailId("anna@goncharova.com");
        person.setPassword("class811");
        person.setActivated(true);
        
        String personID = ClothoAdapter.createPerson(person, clothoObject);
        System.out.println(person);
        System.out.println(personID);
        
        // create an array for the project Ids
        List<String> projectIds = new ArrayList<String>();
        clothoObject.login(loginMap);

        String project1Name = "Phagebook";
        Organization cidar = new Organization("CIDAR");
        Double project1Budget = 0.0;
        Grant grant = new Grant("NSF");
        String grantID = ClothoAdapter.createGrant(grant,clothoObject);
        grant.setId(grantID);
        String des = "Phagebook is great! I love writing Java code for "
                + "Phagebook!";
        Project project1 = new Project();
        // Project should be created like this from now on:
        project1.setName(project1Name);
        project1.setBudget(project1Budget);
        project1.setLeadId(personID);
        project1.setCreatorId(personID);
        project1.setGrantId(grantID);
        project1.setDescription(des);
        
        String project1ID = ClothoAdapter.createProject(project1, clothoObject);

        projectIds.add(project1ID);
        
        project.setId(project1ID);
        System.out.println("Project # 1 has been made.");
        System.out.println(project);
        System.out.println(project1ID);
        System.out.println("----------");
        
        
        
        String project2Name = "BatMobile";
        Organization project2org = new Organization("Bat Org");
        Double project2Budget = 1000000.0;
        String des2 = "On the bat mobile to the stars!";
        Project project2 = new Project();
        // Project should be created like this from now on:
        project2.setName(project2Name);
        project2.setBudget(project2Budget);
        project2.setLeadId(personID);
        project2.setCreatorId(personID);
        project2.setGrantId(grantID);
        project2.setDescription(des2);
        
        String project2ID = ClothoAdapter.createProject(project2, clothoObject);
        
        projectIds.add(project2ID);
        
        project.setId(project2ID);
        System.out.println("Project # 2 has been made.");
        System.out.println(project);
        System.out.println(project2ID);
        System.out.println("----------");
        
        clothoObject.logout();
        // attach the project id list to the person and update in the database
        person.setProjects(projectIds);
        personID = ClothoAdapter.setPerson(person, clothoObject);
        System.out.println("should be gucci!");
        
        // get this person and check how many projects they've got
        Person test = ClothoAdapter.getPerson(personID, clothoObject);
        System.out.println(test.getProjects().size());
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
    public void multipleProjects()
    {
      
      
    
    }
}

