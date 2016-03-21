/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.HashMap;
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
            Map query = new HashMap();
            if (!firstName.equals("")){
                query.put("firstName", firstName); // the value for which we are querying.
            }
            if (!lastName.equals("")){
                query.put("lastName", lastName); // the key of the object we are querying
            }       
            
           
            List<Person> people = ClothoAdapter.queryPerson(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
            JSONArray peopleJSONArray = new JSONArray();
            
            for (Person retrieve : people){
                JSONObject retrievedAsJSON = new JSONObject();
                retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
                //get position? role?? we will look into this
                retrievedAsJSON.put("firstName", retrieve.getFirstName());
                retrievedAsJSON.put("lastName", retrieve.getLastName());
                retrievedAsJSON.put("clothoId", retrieve.getId());

                JSONObject statusList = new JSONObject();
                if (retrieve.getStatuses() != null){
                    for (String status:retrieve.getStatuses()){
                        Status stat = ClothoAdapter.getStatus(status, clothoObject);

                        statusList.put("text", stat.getText());
                        statusList.put("date", stat.getCreated().toString());
                    }
                }

                JSONObject publicationList = new JSONObject();
                if (retrieve.getPublications() != null){

                    for (String publication:retrieve.getPublications()){
                        Publication pub = ClothoAdapter.getPublication(publication, clothoObject);
                        publicationList.put("id", pub.getId());
                    }
                }

                JSONObject labList = new JSONObject();
                if (retrieve.getLabs() != null){
                    for (String lab:retrieve.getLabs()){
                        Institution inst = ClothoAdapter.getInstitution(lab, clothoObject);
                        labList.put("name", inst.getName());
                        Set<Person.PersonRole> rolesAtInstitution = retrieve.getRole(lab);
                        JSONObject positions = new JSONObject();
                        Iterator <Person.PersonRole> it = rolesAtInstitution.iterator();
                        while(it.hasNext()){
                            positions.put(inst.getName(), it.next());
                        }
                        labList.put("roles", positions);
                    }
                }
                retrievedAsJSON.put("statusList", statusList);
                retrievedAsJSON.put("publicationList", publicationList);
                retrievedAsJSON.put("labList", labList);
                peopleJSONArray.put(retrievedAsJSON);
            }
    }

    }
}