/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.websocket;
import java.util.HashMap;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.model.Person;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author KatieLewis
 */
public class createProject {
    
    @Test 
    public void createProject(){
       
        Person person1 = new Person();
        person1.setEmailId("lewis");
        person1.setPassword("abc");
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        String id = ClothoAdaptor.createPerson(person1, clothoObject);
        Map loginMap = new HashMap();
       loginMap.put("username", "lewis");
       loginMap.put("credentials", "abc");
       clothoObject.login(loginMap);
        
        Person person2 = new Person();
        person2 = ClothoAdaptor.getPerson(id,clothoObject);
        
        Project project = new Project(person2, "Phagebook", "description1");
        ClothoAdaptor.createProject(project, clothoObject);
    }
    
}
