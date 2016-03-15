/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import static junit.framework.Assert.assertEquals;
import lombok.Getter;
import lombok.Setter;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import static org.clothocad.phagebook.adaptors.ClothoAdapter.clothoLogin;
import static org.clothocad.phagebook.adaptors.ClothoAdapter.clothoObject;
import static org.clothocad.phagebook.adaptors.ClothoAdapter.conn;
import org.clothocad.phagebook.adaptors.sendEmails;
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
  
    @BeforeClass
    public static void setUpClass() {
      

    }
    
    
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
      ClothoAdapter.clothoCreate("username", "password");
      ClothoAdapter.clothoLogin("username", "password");
            
        System.out.println("Making people.");
        
        Person person1 = new Person();
        String idP1 = "";
        String time = ""+ System.currentTimeMillis() ;

        person1.setFirstName("Person");
        person1.setLastName("Creator");
        person1.setId(idP1);
        person1.setEmailId("Creator" + time + "@gmail.com");
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
        person2.setEmailId("anna@goncharova.com");
        person2.setPassword("person2");
        
        String person2ID = ClothoAdapter.createPerson(person2, clothoObject);
        System.out.println(person2);
        System.out.println(person2ID);
        
        Person person3 = new Person();
        String idP3 = "";
        
        person3.setFirstName("Person");
        person3.setLastName("Lead");
        person3.setId(idP3);
        person3.setEmailId("agonchar@bu.edu");
        person3.setPassword("person3");
        
        String person3ID = ClothoAdapter.createPerson(person3, clothoObject);
        System.out.println(person3);
        System.out.println(person3ID);
        
        System.out.println();
        
        System.out.println("Making a project.");
        // Have to be logged in to Clotho as the Creator to create the 
        // project.
        clothoLogin(creatorEmail, creatorPassword);
        System.out.println("Person Id is: ");

        System.out.println(person1ID);

        String projectName = "Project";
        Organization cidar = new Organization("CIDAR");
        Double projectBudget = 0.0;
        Grant grant = new Grant("NSF");
        String grantID = ClothoAdapter.createGrant(grant,clothoObject);
        grant.setId(grantID);
        String des = "This is a super cool Project!";
        
        List<String> members = new ArrayList <String>();
        members.add(person3ID);
        // Project should be created like this from now on:
        Project project = new Project();
        project.setName(projectName);
        project.setBudget(projectBudget);
        project.setLeadId(person3ID);
        project.setCreatorId(person1ID);
        project.setGrantId(grantID);
        project.setDescription(des);
        project.setMembers(members);
        
        System.out.println("Project ID is!!");
        System.out.println(project.getId());
        // hacky -- set random ID so that the project got created in clotho
        //project.setId("random");
        String projectID = ClothoAdapter.createProject(project, clothoObject);
        // does not create a project ?
        
        project.setId(projectID);
        System.out.println("Project has been made.");
        System.out.println(project);
        System.out.println(projectID);
        System.out.println("----------");
    }
    
    @After
    public void tearDown() {
        clothoObject.logout();
        conn.closeConnection();
    }
    
    @Test
    public void testEditProject()
    {
//      System.out.println("***** \n EDIT PROJECT TEST \n *****");
//    
//      
//      clothoObject.logout(); //HAVE TO LOGOUT OF CLOTHO IF LOGGED IN BECAUSE 
//                             //YOU CAN'T EDIT A PERSON OBJECT IF YOU ARE NOT
//                             //LOGGED INTO CLOTHO AS THAT PERSON
//      Person person1 = new Person();
//      String idP1 = "";
//      String time = ""+ System.currentTimeMillis() ;
//
//      person1.setFirstName("Person");
//      person1.setLastName("Editor");
//      person1.setId(idP1);
//      person1.setEmailId("Creator" + time + "@gmail.com");
//      person1.setPassword("person1");
//
//      String creatorEmail = person1.getEmailId();
//      String creatorPassword = person1.getPassword();
//
//      String person1ID = ClothoAdapter.createPerson(person1, clothoObject);
//      System.out.println(person1);
//      System.out.println(person1ID);
//      
//      System.out.println(projectID);
//      Project oldProject = ClothoAdapter.getProject(projectID, clothoObject);
//      System.out.println("\n*******");
//      System.out.println("OLD PROJECT IS: ");
//      //System.out.println(oldProject.fullProjectDescription());
//      System.out.println("\n*******");
//
//
//      HashMap params = new HashMap<String,String>();
//      params.put("editorId", person1ID);
//      params.put("creator", "Bob Smith");
//      params.put("name", "Project");
//      params.put("projectGrant", "1234");
//      params.put("description", "NEW DESCRIPTION");
//      //params.put("leadId", "");
//      
//      Project editedProject = ClothoAdapter.getProject(projectID, clothoObject);
//      System.out.println("*******");
//      System.out.println("EDITED PROJECT IS: ");
//      //System.out.println(editedProject.fullProjectDescription());
//      System.out.println("\n*******");
    }
    
    /*
    ** Looks at changes in the project and sends out emails to people in the object.
    ** @params: Project Id String
    ** 
    */
    private void sendEmails(String projectId){
      Project project = ClothoAdapter.getProject(projectId, clothoObject);

      // get emails of the people attached to this project
       
      List<String> members = project.getMembers();
      
      Person creator = ClothoAdapter.getPerson(project.getCreatorId(), clothoObject);
      Person lead = ClothoAdapter.getPerson(project.getLeadId(), clothoObject);
      Map people = new HashMap();
      people.put(creator.getEmailId(), creator.getFirstName() + ' ' + creator.getLastName());
      people.put(creator.getEmailId(), lead.getFirstName() + ' ' + lead.getLastName());

      for(int i = 0; i<members.size(); i++){
        String personId = members.get(i);
        System.out.println(personId);
        Person member = ClothoAdapter.getPerson(personId, clothoObject);
        String memberEmail = member.getEmailId();
        String memberName = member.getFirstName() + ' ' + member.getLastName();
        people.put(memberEmail, memberName);
        
      }
      sendEmails.sendMessagesTo(people, "Changes!!");




      
    }
  
}
