/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.dom.Person;
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
 * @author azula
 */
public class getPersonByIdTest {
    
    public getPersonByIdTest() {
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
     * Test of processRequest method, of class getPersonById.
     */
   /* @Test
    public void testProcessRequest() throws Exception {
        System.out.println("processRequest");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        getPersonById instance = new getPersonById();
        instance.processRequest(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of doGet method, of class getPersonById.
     */
   /* @Test
    public void testDoGet() throws Exception {
        System.out.println("doGet");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        getPersonById instance = new getPersonById();
        instance.doGet(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of doPost method, of class getPersonById.
     */
    @Test
    public void testDoPost() throws Exception {
                System.out.println("reached doPost");
        /* Map myMap = request.getParameterMap();

        Iterator iterator = myMap.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            System.out.println(myMap.get(key).toString());
            System.out.println(key);
        }*/

        Object pUserId = "570b0c57347458d2ef333a06";
        String userId = pUserId != null ? (String) pUserId : "";

        Object pNewFirstName = "Ally";
        String newFirstName = pNewFirstName != null ? (String) pNewFirstName : "";

        Object pNewLastName = "Durks";
        String newLastName = pNewLastName != null ? (String) pNewLastName : "";

        /*Object pNewInstitution = request.getParameter("editInstitution");
        String newInstitution = pNewInstitution != null ? (String) pNewInstitution: "";
         */
        Object pNewDepartment = "Not Set";
        String newDepartment = pNewDepartment != null ? (String) pNewDepartment : "";

        Object pNewTitle = "WHY DOES LIFE HATE ME";
        String newTitle = pNewTitle != null ? (String) pNewTitle : "";
/*
        Object pNewLab = request.getParameter("editLab");
        String newLab = pNewLab != null ? (String) pNewLab : "";

        Object pLabId = request.getParameter("lab");
        String labId = pLabId != null ? (String) pLabId : "";*/

        Object pProfileDescription = "Not Set";
        String newProfileDescription = pProfileDescription != null ? (String) pProfileDescription : "";

        System.out.println(userId + " " + newFirstName + newLastName + " " + newDepartment + " " + newTitle + " " + newProfileDescription);
        boolean isValid = false; //used only to make sure the person exists in Clotho
        if (!userId.equals("")) {
            isValid = true;
        }

        if (isValid) {
            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            //

            Person retrieve = ClothoAdapter.getPerson(userId, clothoObject);
            if (!retrieve.getId().equals("")) {
                if (!newFirstName.equals("")) {
                    retrieve.setFirstName(newFirstName);
                }

                if (!newLastName.equals("")) {
                    retrieve.setLastName(newLastName);
                }

                /*if (newEmail != NULL && !"".equals(newEmail)){
                
            }
            
            if (newPassword != NULL && !"".equals(newPassword)){
                
            }*/
 /* if (newInstitution != NULL && !"".equals(newInstitution)){
                List<String> institutions = retrieve.getInstitutions();
                institutions.add(institutionId);
                retrieve.setInstitutions(institutions);
                editPerson = true;
            }*/
                if (!newDepartment.equals("")) {
                    retrieve.setDepartment(newDepartment);
                }

                if (!newTitle.equals("")) {
                    retrieve.setTitle(newTitle);
                }
                System.out.println("new profile description");
                System.out.println(newProfileDescription);
                if (!newProfileDescription.equals("")) {
                    System.out.println("we are inside set profile desc");
                    retrieve.setProfileDescription(newProfileDescription);
                }

                /*  if (newLab != NULL && !"".equals(newLab)) {
                List<String> labs = retrieve.getLabs();
                labs.add(labId);
                retrieve.setLabs(labs);
                editPerson = true;
            }*/
                clothoObject.logout();
                ClothoAdapter.setPerson(retrieve, clothoObject);

            }
        }
        
        
        
    }

    /**
     * Test of getServletInfo method, of class getPersonById.
     */
   /* @Test
    public void testGetServletInfo() {
        System.out.println("getServletInfo");
        getPersonById instance = new getPersonById();
        String expResult = "";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
    
    
}
