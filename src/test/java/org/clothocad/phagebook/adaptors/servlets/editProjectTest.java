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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static Logger logger = Logger.getLogger(editProjectTest.class.getName());

  
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
    ** Sets up Persons for testing and Project
    ** Returns the project ID.
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
        System.out.println("Person Id is: ");

        System.out.println(person1ID);

        String projectName = "Project";
        Organization cidar = new Organization("CIDAR");
        Double projectBudget = 0.0;
        Grant grant = new Grant("NSF");
        String grantID = ClothoAdapter.createGrant(grant,clothoObject);
        grant.setId(grantID);
        String des = "This is a super cool Project!";
        

        Project project = new Project(person1ID, projectName, cidar, person2ID, projectBudget,
            grantID, des);
        
        String projectID = ClothoAdapter.createProject(project, clothoObject);   
        
        System.out.println("Project has been made.");
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
      
      clothoObject.logout(); //HAVE TO LOGOUT OF CLOTHO IF LOGGED IN BECAUSE 
                             //YOU CAN'T EDIT A PERSON OBJECT IF YOU ARE NOT
                             //LOGGED INTO CLOTHO AS THAT PERSON
      Person person1 = new Person();
      String idP1 = "";
      String time = ""+ System.currentTimeMillis() ;

      person1.setFirstName("Person");
      person1.setLastName("Editor");
      person1.setId(idP1);
      person1.setEmailId("Creator" + time + "@gmail.com");
      person1.setPassword("person1");

      String creatorEmail = person1.getEmailId();
      String creatorPassword = person1.getPassword();

      String person1ID = ClothoAdapter.createPerson(person1, clothoObject);
      System.out.println(person1);
      System.out.println(person1ID);
      
      String projectID = make();
      System.out.println(projectID);
      Project oldProject = ClothoAdapter.getProject(projectID, clothoObject);
      System.out.println("\n*******");
      System.out.println("OLD PROJECT IS: ");
      //System.out.println(oldProject.fullProjectDescription());
      System.out.println("\n*******");


      HashMap params = new HashMap<String,String>();
      params.put("editorId", person1ID);
      params.put("creator", "Bob Smith");
      params.put("name", "Project");
      params.put("projectGrant", "1234");
      params.put("description", "NEW DESCRIPTION");
      //params.put("leadId", "");
      
      editProject(projectID, params);
      
      Project editedProject = ClothoAdapter.getProject(projectID, clothoObject);
      System.out.println("*******");
      System.out.println("EDITED PROJECT IS: ");
      //System.out.println(editedProject.fullProjectDescription());
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
      logger.log(Level.INFO, "Processing request for editing the project");

      Iterator entries = params.entrySet().iterator();
      while (entries.hasNext()) {
        // reset the value if it is diff from the one in the project object
        Map.Entry entry = (Map.Entry) entries.next();
        String key = (String)entry.getKey();
        String value = (String)entry.getValue();
        System.out.println("Key = " + key + ", Value = " + value);
        
        // declare an array for keeping track of old and new values, use for logging
        // [0] type of new value, [1] new value
        // [2] type if old value (same as [0]), [3] old value
        String[] keyValue = new String[4]; 
        keyValue[0] = key; // type of new value (like "description")
        keyValue[1] = value; // the actual new value (like "This is a new desciption")
        if(key.equals("editorId")){
          logger.log(Level.INFO, "Person Id {1} is editing the project", keyValue);
          Person editor = ClothoAdapter.getPerson(value, clothoObject);
          System.out.println();
        }
        if(key.equals("description")){
          keyValue[2]= "description";
          keyValue[3]= project.getDescription();
          logger.log(Level.INFO, "Old value, {0}: {1}, is changed to new value, {2}: {3}.",keyValue);
          helperMsg(project.getDescription(), value);
          project.setDescription(value);
        }
        if(key.equals("name")){
          keyValue[2]= "name";
          keyValue[3]= project.getName();
          logger.log(Level.INFO, "Old value, {0}: {1}, is changed to new value, {2}: {3}.",keyValue);
          helperMsg(project.getName(), value);
          project.setName(value);
        }
        if(key.equals("leadId")){
//          keyValue[2]= "leadId";
//          keyValue[3]= project.getLeadId();
//          logger.log(Level.INFO, "Old value, {0}: {1}, is changed to new value, {2}: {3}.",keyValue);
          helperMsg(project.getLeadId(), value);
          System.out.println("Can't edit lead yet, sorry!");
        }
        if(key.equals("projectBudget")){
          keyValue[2]= "projectBudget";
          keyValue[3]= project.getBudget().toString();
          logger.log(Level.INFO, "Old value, {0}: {1}, is changed to new value, {2}: {3}.",keyValue);
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
          String oldGrantId = project.getGrantId();
          Grant newGrant = new Grant(value);
          String newGrantId = ClothoAdapter.createGrant(newGrant, clothoObject);
          
          keyValue[2]= "projectGrant";
          keyValue[3]= oldGrantId;
          logger.log(Level.INFO, "Old value, {0}: {1}, is changed to new value, {2}: {3}.",keyValue);
          helperMsg(Double.toString(project.getBudget()), value);

          //newGrant.setId(newGrantId);
          helperMsg(project.getGrantId(),value);
          project.setGrantId(newGrantId);
        }
        
        
      }
      String foo = ClothoAdapter.setProject(project, clothoObject);
      System.out.println(foo);
      // TODO: keep track of old changes + new changes
    
    }
    
    public void helperMsg(String oldVal, String newVal){
      System.out.println("\nOld Value is: " + oldVal + "\nNew Value is: " + newVal);

    }
    
    /*
    ** Looks at changes in the project and sends out emails to people in the object.
    ** @params: Project
    ** 
    */
    private void sendEmails(Project project){
      
      
      
    }
  
}
