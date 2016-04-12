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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cidarlab.citationsapi.PhagebookCitation;
import org.cidarlab.citationsapi.Pubmed;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author innaturshudzhyan
 */
public class addPubMedTest {
    
    public addPubMedTest() {
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

    /**
     * Test of doPost method, of class addPubMed.
     */
    @Test
    public void testDoPost() throws Exception {
        Object pCreatedBy = "570aea4ad4c6bb2c05c397d4";
        String createdBy  = pCreatedBy != null ? (String) pCreatedBy : "";
        System.out.println("CREATED BY" + createdBy);
        String id ="24911500";
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map loginMap = new HashMap();
            
        loginMap.put("username", "phagebook");
        loginMap.put("credentials", "backend");
        clothoObject.login(loginMap);

        PhagebookCitation pC = Pubmed.getPhagebookCitation(id);
        pC.setUser(createdBy);
        
        ClothoAdapter.createPhagebookCitation(pC, clothoObject);
        PhagebookCitation returs = ClothoAdapter.getPhagebookCitation(pC.getId(), clothoObject);
        
        Person user = ClothoAdapter.getPerson(pC.getUser(), clothoObject);
        
        List<String> ids = new ArrayList<>();
        ids.add(pC.getId());
        
        user.setPhagebookCitations(ids);
        
        clothoObject.logout();
        ClothoAdapter.setPerson(user, clothoObject);
        clothoObject.login(loginMap);
        user = ClothoAdapter.getPerson(user.getId(), clothoObject);
        System.out.println("The id of ph citations " + user.getPhagebookCitations());
    }
    
}
