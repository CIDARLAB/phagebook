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
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
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
public class loadPhagebookInstitutionsTest {
    
    public loadPhagebookInstitutionsTest() {
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
     * Test of processRequest method, of class loadPhagebookInstitutions.
     */
    @Test
    public void createPhagebookInstitutions() throws Exception {
        //always valid 
        //login
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = "phagebook";
        String password = "backend";
        /*

            DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                               PASSWORD: backend
        */
        
        Person globalPI = new Person();
        globalPI.setEmailId("globalPI@test.com");
        globalPI.setFirstName("Global");
        globalPI.setLastName("PI");
        ClothoAdapter.createPerson(globalPI, clothoObject);
        
        List<String> PIs = new ArrayList<>();
        PIs.add(globalPI.getId());
        Map loginMap = new HashMap();
        loginMap.put("username"   , username);
        loginMap.put("credentials", password);
        clothoObject.login(loginMap);
        
        Lab CIDAR = new Lab();
        CIDAR.setDescription("Center for Integrated Design Automation Research");
        CIDAR.setName("CIDAR");
        CIDAR.setUrl("www.cidar.com");
        CIDAR.setLeadPIs(PIs);
        
        
        Lab KhalilLab = new Lab();
        KhalilLab.setDescription("Khalil Lab Description");
        KhalilLab.setName("Khalil Lab");
        KhalilLab.setLeadPIs(PIs);
        /*
        Lab harvardLab1 = new Lab();
        harvardLab1.setName("The Laboratory at Harvard");
        harvardLab1.setLeadPIs(PIs);
        
        Lab harvardLab2 = new Lab();
        harvardLab2.setName("Harvard Library Innovation Lab");
        harvardLab2.setLeadPIs(PIs);
        
        Lab medialLab = new Lab();
        medialLab.setName("Media Lab");
        medialLab.setLeadPIs(PIs);
        
        Lab mitLab2 = new Lab();
        mitLab2.setName("MIT Lab 2");
        medialLab.setLeadPIs(PIs);
        */
        
        
        
        ClothoAdapter.createLab(CIDAR, clothoObject);
        ClothoAdapter.createLab(KhalilLab, clothoObject);
        /*
        ClothoAdapter.createLab(harvardLab1, clothoObject);
        ClothoAdapter.createLab(harvardLab2, clothoObject);
        ClothoAdapter.createLab(medialLab, clothoObject);
        ClothoAdapter.createLab(mitLab2, clothoObject);
        */
        List<String> PILabs = new ArrayList<>();
        
        PILabs.add(CIDAR.getId());
        PILabs.add(KhalilLab.getId());
        /*
        PILabs.add(harvardLab1.getId());
        PILabs.add(harvardLab2.getId());
        PILabs.add(medialLab.getId());
        PILabs.add(mitLab2.getId());
       
                
        */
        Institution bostonUniversity = new Institution();
        bostonUniversity.setName("Boston University");
        bostonUniversity.setDescription("Pick your odyssey." +
                "Explore BU’s 250+ programs of study, check out courses, or find student "+ 
                " resources and University policies.");
        
        bostonUniversity.setPhone("(617) 353-2000");
        bostonUniversity.setUrl("www.bu.edu");
        bostonUniversity.setType(Institution.InstitutionType.University);
        bostonUniversity.getLabs().add(CIDAR.getId());
        bostonUniversity.getLabs().add(KhalilLab.getId());
        
        
        Institution MIT              = new Institution();
        MIT.setName("Massachusetts Institute of Technology");
        MIT.setDescription("The Massachusetts Institute of Technology (MIT) was founded in 1861 by William Barton Rogers. MIT's charter (Massachusetts Acts of 1861, Chapter 183, for the bibliographically-minded) foresaw \"a school of industrial science [aiding] the advancement, development and practical application of science in connection with arts, agriculture, manufactures, and commerce.\"");
        MIT.setPhone("(617) 253-3400");
        MIT.setUrl("www.mit.edu");
        MIT.setType(Institution.InstitutionType.University);
        //MIT.getLabs().add(medialLab.getId());
        //MIT.getLabs().add(mitLab2.getId());
        
        
        Institution harvardUniversity = new Institution();
        harvardUniversity.setName("Harvard University");
        harvardUniversity.setDescription("Harvard is the oldest institution of higher education in the United States, established in 1636 by vote of the Great and General Court of the Massachusetts Bay Colony.");
        harvardUniversity.setPhone("(617) 495-1000");
        harvardUniversity.setUrl("www.harvard.edu");
        harvardUniversity.setType(Institution.InstitutionType.University);
        //harvardUniversity.getLabs().add(harvardLab1.getId());
        //harvardUniversity.getLabs().add(harvardLab2.getId());
        
        ClothoAdapter.createInstiution(MIT, clothoObject);
        ClothoAdapter.createInstiution(bostonUniversity, clothoObject);
        ClothoAdapter.createInstiution(harvardUniversity, clothoObject);
        clothoObject.logout();
        
        //globalPI.setLabs(PILabs);
        ClothoAdapter.setPerson(globalPI, clothoObject);
        
        
        
        
        
    }
    @Test
    public void testJSONCreation() {
        //always valid 
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
        
        Map query = new HashMap(); // blank map to get all objects of that class
        
        List<Institution> institutions = ClothoAdapter.queryInstitution(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
        
        JSONArray institutionsInClotho = new JSONArray();
        int countInstits = 0;
        int countLabs=0;
        for (Institution institute : institutions){
            countInstits++;
            JSONArray labs = new JSONArray();
            for (String labId : institute.getLabs()){
                countLabs++;
                Lab lab = ClothoAdapter.getLab(labId, clothoObject);
                JSONObject obj = new JSONObject();
                obj.put("labName", lab.getName());
                obj.put("labId", lab.getId());
                labs.put(obj);
            }
            JSONObject institution = new JSONObject();
            institution.put("institutionId", institute.getId());
            institution.put("institutionName", institute.getName());
            institution.put("labs", labs);
            institutionsInClotho.put(institution);
            
        }
        
        
    }

    
}

