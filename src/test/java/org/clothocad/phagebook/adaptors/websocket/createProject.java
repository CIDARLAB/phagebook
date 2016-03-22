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
import org.clothocad.phagebook.adaptors.ClothoAdapter;
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
        person1.setFirstName("Katie");
        person1.setLastName("Lewis");
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        String id = ClothoAdapter.createPerson(person1, clothoObject);
        Map loginMap = new HashMap();
       loginMap.put("username", "lewis");
       loginMap.put("credentials", "abc");
       clothoObject.login(loginMap);
        
        Person person2 = new Person();
        person2 = ClothoAdapter.getPerson(id,clothoObject);
        String description = "Currently, synthetic biology lacks a systematic "         
                + "design-build-test workflow for creating genetic circuits. Researchers often waste time and "
                + "resources on traditional trial-and-error approaches in designing and assembling large devices. "
                + "My research works towards overcoming this roadblock by creating a standardized experimental-computational "
                + "workflow that is clearly defined and applicable to a wide variety of researchers and projects. This workflow integrates software tools to "
                + "reduce human error and to structure the way designs are chosen, assembled, and tested.";
        String projectName = "Phagebook";
        Project project = new Project();
        project.setCreatorId(person2.getId());
        project.setDescription(description);
        
        ClothoAdapter.createProject(project, clothoObject);
    }
    
}
