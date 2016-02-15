/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    public String make(){
      System.out.println("-----CREATE PERSON TEST-----");
        System.out.println("Making people.");
        clothoObject.logout(); //HAVE TO LOGOUT OF CLOTHO IF LOGGED IN BECAUSE 
                               //YOU CAN'T EDIT A PERSON OBJECT IF YOU ARE NOT
                               //LOGGED INTO CLOTHO AS THAT PERSON
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
        person2.setEmailId("Member" + time + "@gmail.com");
        person2.setPassword("person2");
        
        String person2ID = ClothoAdapter.createPerson(person2, clothoObject);
        System.out.println(person2);
        System.out.println(person2ID);
        
        Person person3 = new Person();
        String idP3 = "";
        
        person3.setFirstName("Person");
        person3.setLastName("Lead");
        person3.setId(idP3);
        person3.setEmailId("Lead" + time + "@gmail.com");
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
        return projectID;
    }

    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
      this.clothoLogin("username", "password");
    }
    
    @After
    public void tearDown() {
        clothoObject.logout();
        conn.closeConnection();
    }

    
    @Test
    public void testEditProject()
    {
      System.out.println("***** \n EDIT PROJECT TEST \n *****");
    
    //(Person creator, String name, Organization lab, 
    //    Person lead, Double projectBudget, Grant projectGrant, String description)
      
      String projectID = make();
      System.out.println(projectID);
      Project oldProject = ClothoAdapter.getProject(projectID, clothoObject);
      System.out.println("\n*******");
      System.out.println("OLD PROJECT IS: ");
      System.out.println(oldProject.fullProjectDescription());
      System.out.println("\n*******");


      HashMap params = new HashMap<String,String>();
      params.put("creator", "Bob Smith");
      params.put("name", "Project");
      params.put("projectGrant", "1234");
      params.put("description", "NEW DESCRIPTION");
      editProject(projectID, params);
      
      Project editedProject = ClothoAdapter.getProject(projectID, clothoObject);
      System.out.println("*******");
      System.out.println("EDITED PROJECT IS: ");
      System.out.println(editedProject.fullProjectDescription());
      System.out.println("\n*******");

//      Project testProject = new Project();
//      String idP = "";
//      testProject.setId(idP);
//
//      String idP2 = ClothoAdapter.createProject(testProject, clothoObject);
//      assertEquals(idP2, testProject.getId());
//      if(idP.equals(testProject.getId())){
//          fail();
//      }
//
//      testProject.setDescription("A different project");
//
//      String idP3 = ClothoAdapter.createProject(testProject, clothoObject);
//      assertEquals(idP3, testProject.getId());
//      if(idP.equals(testProject.getId())){
//          fail();
//      }
//
//      System.out.println("----------");

      
    }
    
    private void editProject(String request, HashMap params){
      // TODO: Add methods for people checking (do we want to link people with 
      // projects or...?
      
      // for testing purposes request is the Project ID
      // params is the hashmap of new values
      
      //  Person creator, String name, Organization lab, 
      //  Person lead, Double projectBudget, Grant projectGrant, String description
      
      Project project = ClothoAdapter.getProject(request, clothoObject);
      System.out.println("In editProject test project is: ");
      System.out.println(project);
      // HashMap <String, Object> projectHM = project.getHashMap();
              
      Iterator entries = params.entrySet().iterator();
      while (entries.hasNext()) {
        // reset the value if it is diff from the one in the project object
        Map.Entry entry = (Map.Entry) entries.next();
        String key = (String)entry.getKey();
        String value = (String)entry.getValue();
        System.out.println("Key = " + key + ", Value = " + value);
        if(key.equals("description")){
          helperMsg(project.getDescription(), value);
          project.setDescription(value);
        
        }
        if(key.equals("name")){
          helperMsg(project.getName(), value);
          project.setName(value);
        }
        if(key.equals("lead")){
          helperMsg(project.getLead().getFirstName(), value);
          System.out.println("Can't edit lead yet, sorry!");
        }
        if(key.equals("projectBudget")){
          helperMsg(Double.toString(project.getBudget()), value);
          project.setBudget(Double.parseDouble(value));
        }
//        if(key.equals("lab")){
//          helperMsg(project.getAffiliatedLabs().get(0).toString(),value);
//          Organization newLab = new Organization(value);
//          List<Organization> newLabList = null;
//          newLabList.add(newLab);
//          project.setAffiliatedLabs(newLabList);
//        }
        if(key.equals("projectGrant")){
          // add association support for these big classes -- want to be able
          // to link to the grant
          helperMsg(project.getGrant().getName(),value);
          project.setGrant(new Grant(value));
        }
        
        
      }
      String foo = ClothoAdapter.setProject(project, clothoObject);
      System.out.println(foo);
    
    }
    
    public void helperMsg(String oldVal, String newVal){
      System.out.println("\nOld Value is: " + oldVal + "\nNew Value is: " + newVal);

    }
  
}
