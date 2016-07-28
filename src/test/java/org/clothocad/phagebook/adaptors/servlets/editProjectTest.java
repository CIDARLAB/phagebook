///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.clothocad.phagebook.adaptors.servlets;
//
//import org.clothocad.phagebook.adaptors.servlets.editProject;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.clothoapi.clotho3javaapi.Clotho;
//import org.clothoapi.clotho3javaapi.ClothoConnection;
//import org.clothocad.model.Person;
//import org.clothocad.phagebook.adaptors.ClothoAdapter;
//import static org.clothocad.phagebook.adaptors.ClothoAdapter.clothoObject;
//import org.clothocad.phagebook.controller.Args;
//import org.clothocad.phagebook.dom.Grant;
//import org.clothocad.phagebook.dom.Organization;
//import org.clothocad.phagebook.dom.Project;
//import org.clothocad.phagebook.dom.Status;
//import org.junit.After;
//import org.junit.AfterClass;
//import static org.junit.Assert.assertEquals;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
///**
// *
// * @author anna_g
// */
//public class editProjectTest {
//    static Project project = new Project();
//    static ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
//    static Clotho clothoObject = new Clotho(conn);
//
//  
//    @BeforeClass
//    public static void setUpClass() {
//            
//
//        //TODO: we need to have an authentication token at some point
//        Map createUserMap = new HashMap();
//        String username = "username";
//        String password = "password";
//        
//        createUserMap.put("username", username);
//        createUserMap.put("password", password);
//
//        clothoObject.createUser(createUserMap);
//        Map loginMap = new HashMap();
//        loginMap.put("username", username);
//        loginMap.put("credentials", password);  
//
//        clothoObject.login(loginMap);
//        
//        System.out.println("Making people.");
//        
//        Person person1 = new Person();
//        String idP1 = "";
//        String time = ""+ System.currentTimeMillis() ;
//
//        person1.setFirstName("Person");
//        person1.setLastName("Creator");
//        person1.setId(idP1);
//        person1.setEmailId("anna@goncharova.com");
//        person1.setPassword("person1");
//        
//        String creatorEmail = person1.getEmailId();
//        String creatorPassword = person1.getPassword();
//        
//        String person1ID = ClothoAdapter.createPerson(person1, clothoObject);
//        System.out.println(person1);
//        System.out.println(person1ID);
//        
//        Person person2 = new Person();
//        String idP2 = "";
//        
//        person2.setFirstName("Person");
//        person2.setLastName("Member");
//        person2.setId(idP2);
//        person2.setEmailId("anna@goncharova.com");
//        person2.setPassword("person2");
//        
//        String person2ID = ClothoAdapter.createPerson(person2, clothoObject);
//        System.out.println(person2);
//        System.out.println(person2ID);
//        
//        Person person3 = new Person();
//        String idP3 = "";
//        
//        person3.setFirstName("Person");
//        person3.setLastName("Lead");
//        person3.setId(idP3);
//        person3.setEmailId("agonchar@bu.edu");
//        person3.setPassword("person3");
//        
//        String person3ID = ClothoAdapter.createPerson(person3, clothoObject);
//        System.out.println(person3);
//        System.out.println(person3ID);
//       
//        clothoObject.login(loginMap);
//
//        System.out.println();
//        
//        System.out.println("Making a project.");
//        // Have to be logged in to Clotho as the Creator to create the 
//        // project.
//        //clothoObject.login(loginMap);
//        System.out.println("Person Id is: ");
//
//        System.out.println(person1ID);
//
//        String projectName = "Project";
//        Organization cidar = new Organization("CIDAR");
//        Double projectBudget = 0.0;
//        Grant grant = new Grant("NSF");
//        String grantID = ClothoAdapter.createGrant(grant,clothoObject);
//        grant.setId(grantID);
//        String des = "This is a super cool Project!";
//        
//        List<String> members = new ArrayList <String>();
//        members.add(person3ID);
//        // Project should be created like this from now on:
//        project.setName(projectName);
//        project.setBudget(projectBudget);
//        project.setLeadId(person3ID);
//        project.setCreatorId(person1ID);
//        project.setGrantId(grantID);
//        project.setDescription(des);
//        project.setMembers(members);
//        
//        System.out.println("Project ID is!!");
//        System.out.println(project.getId());
//        // hacky -- set random ID so that the project got created in clotho
//        //project.setId("random");
//        String projectID = ClothoAdapter.createProject(project, clothoObject);
//        // does not create a project ?
//        
//        project.setId(projectID);
//        System.out.println("Project has been made.");
//        System.out.println(project);
//        System.out.println(projectID);
//        System.out.println("----------");
//
//    }
//    
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//
//    }
//    
//    @After
//    public void tearDown() {
//    }
//    
//    /*
//    ** Checks if changes passed in were null. If they were null old values 
//    ** should not change.
//    */
////    @Test
//    public void testIfNull()
//    {
//      System.out.println("Test # 1");
//      String oldName = project.getName();
//      Double oldBudget = project.getBudget();
//      String oldDesc = project.getDescription();
//      String prID = project.getId();
//      System.out.println("Name of project is:");
//      System.out.println(oldName);
//      System.out.println("ID of project is:");
//      System.out.println(prID);
//      //editProject(Project project, HashMap params, Clotho clothoObject)
//      HashMap params = new HashMap();
//      params.put("name", "");
//      params.put("budget", "");
//      params.put("description", "");
//      
//      editProject.editProjectFunction(project, params, clothoObject);
//      
//      Project project1 = ClothoAdapter.getProject(prID, clothoObject);
//      String newName = project1.getName();
//      Double newBudget = project1.getBudget();
//      String newDesc = project1.getDescription();
//      
//      assertEquals(newBudget,oldBudget);
//      assertEquals(newDesc,oldDesc);
//      assertEquals(oldName,newName);
//      //assertEquals(newDesc,"random desc");
//
//    }
//    
//    /*
//    ** Checks if status was updated by comparing sizes of the 
//    ** original Status list and changed status list
//    */
//    @Test
//    public void testProjectAddStatus()
//    {
//      String userId =project.getCreatorId();
//      String leadId = project.getLeadId();
//      String projectId = project.getId();
//      
//      // add some updates to the project -- make it two new updates
//      Status update1= new Status();
//      // update made by the creator of the project
//      update1.setText("update1");
//      update1.setUserId(userId);
//      System.out.println(update1);
//      System.out.println("About to create a Status in Clotho (in editProjectTest)");
//      String update1ID = ClothoAdapter.createStatus(update1, clothoObject);
//      System.out.println("Status ID is: "+update1ID);
//
//      
//      Status update2 = new Status();
//      // update made by the creator of the project
//      update2.setText("update1");
//      update2.setUserId(leadId);
//      System.out.println(update2);
//      System.out.println("About to create a Status in Clotho (in editProjectTest)");
//      String update2ID = ClothoAdapter.createStatus(update2, clothoObject);
//      System.out.println("Status ID is: "+update2ID);
//      
//      // store the updates Ids in a list
//      List<String> updatesIds = new ArrayList();
//      updatesIds.add(update1ID);
//      updatesIds.add(update2ID);
//      System.out.println(Arrays.toString(updatesIds.toArray()));
//      System.out.println(updatesIds.size());
//      //int oldSize = updatesIds.size();
//      
//      // now attach the list to this project
//      project.setUpdates(updatesIds);
//      
//      // update the project in Clotho
//      String projectId1 = ClothoAdapter.setProject(project, clothoObject);
//      
//      // now test the add update function -- should increase size by 1
//      addUpdateToProject.addProjectUpdate(userId, projectId1, "update3",true, clothoObject);
//      
//      // check if the size changes
//      Project updatedProject = ClothoAdapter.getProject(projectId1, clothoObject);
//      List<String> updatesIdsUpdated = updatedProject.getUpdates();
//      System.out.println(Arrays.toString(updatesIdsUpdated.toArray()));
//      System.out.println(updatesIdsUpdated.size());
//      int newSize = updatesIdsUpdated.size();
//
//      assertEquals(newSize,3);
//
////      addUpdateToProject.addProjectUpdate(userID, projectID, , clothoObject);
//    }
//    
//    @Test
//    public void testEditProject()
//    {
//
//    }
//    
//
//  
//}
