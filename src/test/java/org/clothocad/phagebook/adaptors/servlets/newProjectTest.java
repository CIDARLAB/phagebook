/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
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
    public Clotho clothoObject;
    public ClothoConnection conn;
    
    public newProjectTest() {
       
    }
    
    public void clothoLogin(){
       this.conn = new ClothoConnection(Args.clothoLocation);
       this.clothoObject = new Clotho(conn);
       
       Map createUserMap = new HashMap();
       String username = "test"+ System.currentTimeMillis() ;
       
       
       createUserMap.put("username", username);
       createUserMap.put("password", "password");
       
       
       clothoObject.createUser(createUserMap);
       
       Map loginMap = new HashMap();
       loginMap.put("username", username);
       loginMap.put("credentials", "password");  
       
       clothoObject.login(loginMap);
       
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
       this.clothoLogin();
    }
    
    @After
    public void tearDown() {
        clothoObject.logout();
        conn.closeConnection();
    }
    
    @Test
    public void whatDo()
    {
      // How to test the projects page???!
      
    
    }
}

