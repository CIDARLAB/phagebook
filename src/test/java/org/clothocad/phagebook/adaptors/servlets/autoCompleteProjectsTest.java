/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Project;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Herb
 */
public class autoCompleteProjectsTest {
    
    public autoCompleteProjectsTest() {
    }
    
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
    public void testFunctionality(){
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            //TODO: we need to have an authentication token at some point
            String name = "L";
            Map createUserMap = new HashMap();
            String username = "phagebook";
            String password = "backend";
            
            createUserMap.put("username", username);
            createUserMap.put("password", password);
            
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);  

            clothoObject.login(loginMap);
            Map query = new HashMap();
            
            query.put("query", name); // the value for which we are querying.
            query.put("key", "name"); // the key of the object we are querying
            
            List<Project> projects = ClothoAdapter.queryProject(query, clothoObject, ClothoAdapter.QueryMode.STARTSWITH);
            JSONArray responseArray = new JSONArray();
            for (Project proj : projects){
                JSONObject obj = new JSONObject();
                obj.put("id", proj.getId());
                obj.put("name", proj.getName());
                responseArray.put(obj);
            }
    }
    /**
     * Test of doGet method, of class autoCompleteProjects.
     */
    @Test
    public void createProjectsToTest() throws Exception {
        
        Project proj1 = new Project();
        proj1.setName("Cello");
        
        Project proj2 = new Project();
        proj2.setName("Clotho Revamp");
        
        Project proj3 = new Project();
        proj3.setName("Synbio Interface");
        
        Project proj4 = new Project();
        proj4.setName("Low power CAD");
        
        //login
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = "phagebook";
        String password = "backend";
        /*

            DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                               PASSWORD: backend
        */
        Map loginMap = new HashMap();
        loginMap.put("username"   , username);
        loginMap.put("credentials", password);
        clothoObject.login(loginMap);
        
        ClothoAdapter.createProject(proj1, clothoObject);
        ClothoAdapter.createProject(proj2, clothoObject);
        ClothoAdapter.createProject(proj3, clothoObject);
        ClothoAdapter.createProject(proj4, clothoObject);
        
        clothoObject.logout();
        
    }

    
    
}
