/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Project;

/**
 *
 * @author anna_g
 */
public class AuthPrivileges {
  
  private static void initialize(){
    
    // create all of the objects we will be initially testing
    Person doug = new Person();
    doug.setFirstName("Doug");
    doug.setEmailId("dougd@bu.edu");
    doug.setPassword("123456");

    
    Person prash = new Person();
    prash.setFirstName("Prashant");
    prash.setEmailId("abcde@gmail.com");
    prash.setPassword("1234567");

    
    Person anna = new Person();
    anna.setFirstName("Anna");
    anna.setEmailId("abcdef@gmail.com");
    anna.setPassword("12345678");
    
    Person sherlock = new Person();
    sherlock.setFirstName("Sherlock");
    sherlock.setEmailId("abcdefg@gmail.com");
    sherlock.setPassword("12345678");
    
    Institution boston = new Institution("BU");
    Institution cidar = new Institution("CIDAR");
    
    Project pb = new Project();
    Project cl = new Project();
    doug.addInstitution(boston);
    prash.addInstitution(boston);
    anna.addInstitution(boston);
    

    // make sure there is a clotho connection
    ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
    Clotho clothoObject = new Clotho(conn); 
    String username = "phagebooks";
    Map createUserMap = new HashMap();
    createUserMap.put("username", username);
    createUserMap.put("password", "password");
    clothoObject.createUser(createUserMap);
    Map loginMap = new HashMap();
    loginMap.put("username", username);
    loginMap.put("credentials", "password");
    clothoObject.login(loginMap);
    
    // now save these objects in clotho
  
    String projectIDpb = ClothoAdapter.createProject(pb, clothoObject);
    String projectIDcl = ClothoAdapter.createProject(cl, clothoObject);
    
    String institutionIDboston = ClothoAdapter.createInstiution(boston, clothoObject);    
    String institutionIDcidar = ClothoAdapter.createInstiution(cidar, clothoObject);
    
    clothoObject.logout();
    
    String personIDdoug = ClothoAdapter.createPerson(doug, clothoObject);
    String personIDanna = ClothoAdapter.createPerson(anna, clothoObject);    
    String personIDsher = ClothoAdapter.createPerson(sherlock, clothoObject);
    
    makePrivate(projectIDpb, clothoObject);
    
    clothoObject.logout();
    conn.closeConnection();

    System.out.println(projectIDpb);
    System.out.println(projectIDcl);
    System.out.println(institutionIDboston);
    System.out.println(institutionIDcidar);
    System.out.println(personIDdoug);
    System.out.println(personIDanna);
    System.out.println(personIDsher);
    
    


  }
  
  private static void makePrivate(String objectId, Clotho clothoObject){
    List<String> add = new ArrayList<String>();
    List<String> remove = new ArrayList<String>();


    Map grantMap = new HashMap();
    grantMap.put("id", objectId);
    grantMap.put("user", "none");
    grantMap.put("add", add);
    grantMap.put("remove", remove);
    
    System.out.println(grantMap);
    
    Map grantResult = new HashMap();
    grantResult = (Map)(clothoObject.grant(grantMap));
    System.out.println(grantResult);

  }
  

//  public static void main(String [ ] args)
//  {
//    initialize();
//  }
  
}
