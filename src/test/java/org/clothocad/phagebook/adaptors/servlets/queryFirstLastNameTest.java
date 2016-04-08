/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Status;
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
public class queryFirstLastNameTest {
    
    public queryFirstLastNameTest() {
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
     * Test of doPost method, of class queryFirstLastName.
     */
    @Test
    public void testServlet() throws Exception {
        Object pFirstName = "Johan";
        String firstName = pFirstName != null ? (String) pFirstName: "";
        
        Object pLastName = "Ospina";
        String lastName = pLastName != null ? (String) pLastName: "";
        
        boolean isValid = false;
        if (!firstName.equals("") || !lastName.equals("")){
            isValid = true;
        }
        
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);     
            clothoObject.login(loginMap);
            // able to query now. 
            Map query1 = new HashMap();
            Map query2 = new HashMap();
            if (!firstName.equals("")){
                query1.put("query", firstName); // the value for which we are querying.
                query1.put("key", "firstName");
            }
            if (!lastName.equals("")){
                query2.put("query", lastName); // the key of the object we are querying
                query2.put("key", "lastName");
            }       
            
           
            List<Person> peopleFirstName = ClothoAdapter.queryPerson(query1, clothoObject, ClothoAdapter.QueryMode.STARTSWITH);
            List<Person> peopleLastName  = ClothoAdapter.queryPerson(query2, clothoObject, ClothoAdapter.QueryMode.STARTSWITH);
            List<Person> combinedList = new ArrayList<Person>(peopleFirstName);
            combinedList.addAll(peopleLastName);
            Set<Person> hs = new HashSet<>();
            hs.addAll(combinedList);
            combinedList.clear();
            combinedList.addAll(hs);
            
            
            JSONArray peopleJSONArray = new JSONArray();
            
            for (Person retrieve : combinedList){
                JSONObject retrievedAsJSON = new JSONObject();
                retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
                //get position? role?? we will look into this
                retrievedAsJSON.put("firstName", retrieve.getFirstName());
                retrievedAsJSON.put("lastName", retrieve.getLastName());
                retrievedAsJSON.put("clothoId", retrieve.getId());


                JSONArray institutionList = new JSONArray();
                
                if (retrieve.getInstitutions() != null){
                    for (String institution: retrieve.getInstitutions()){
                        Institution inst = ClothoAdapter.getInstitution(institution, clothoObject);
                        institutionList.put(inst.getName());
                    }
                }
                JSONArray labList = new JSONArray();
                if (retrieve.getLabs() != null){
                    for (String lab:retrieve.getLabs()){
                        Lab labo = ClothoAdapter.getLab(lab, clothoObject);
                        labList.put(labo.getName());
                    }
                }
                
                retrievedAsJSON.put("mainInstitution" , ((institutionList.length() == 0) ? "Other" : institutionList.get(0)));
                retrievedAsJSON.put("mainLab", ((labList.length() == 0) ? "Other" : labList.get(0)));
                
                //retrievedAsJSON.put("labList", labList);
                peopleJSONArray.put(retrievedAsJSON);
            }
            
            
    }

    }
}