/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
import org.junit.Test;

/**
 *
 * @author Herb
 */
public class PhagebookInit {
    
    @Test
    public void phagebookInit() throws Exception {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        Person backendUser = new Person();
        String emailId = "phagebook"; // it is also the name in clotho but doens't have a name property
        backendUser.setEmailId(emailId);
        backendUser.setFirstName("Phagebook");
        backendUser.setLastName("Backend");
        backendUser.setPassword("backend");
        
        Map backendUserQueryMap = new HashMap();
        backendUserQueryMap.put("emailId", emailId);
        List<Person> backendUsers = ClothoAdapter.queryPerson(backendUserQueryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        if (backendUsers.isEmpty()){
            ClothoAdapter.createBackendUser(backendUser, clothoObject);
        }

        String username = Args.defaultPhagebookUsername;
        String password = Args.defaultPhagebookPassword;
        
        /*
            DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                               PASSWORD: backend
        */
        Person globalPI = new Person();
        globalPI.setEmailId("globalPI@test.com");
        globalPI.setFirstName("Global");
        globalPI.setLastName("PI");
        globalPI.setPassword("phagebook");
        
        Map globalPIQueryMap = new HashMap();
        globalPIQueryMap.put("emailId", globalPI.getEmailId());
        List<Person> piUsers = ClothoAdapter.queryPerson(globalPIQueryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        if (piUsers.isEmpty()){
            ClothoAdapter.createPerson(globalPI, clothoObject);
        }
        
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
        
        Map cidarLabMap = new HashMap();
        cidarLabMap.put("name", "CIDAR");
        List<Lab> cidarLabs = ClothoAdapter.queryLab(cidarLabMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        if (cidarLabs.isEmpty()){
            ClothoAdapter.createLab(CIDAR, clothoObject);
        }
        
        Map khalilLabMap = new HashMap();
        khalilLabMap.put("name", "Khalil Lab");
        List<Lab> khalilLabs = ClothoAdapter.queryLab(khalilLabMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        if (khalilLabs.isEmpty()){
            ClothoAdapter.createLab(KhalilLab, clothoObject);
        }
    
        List<String> PILabs = new ArrayList<>();
        
        PILabs.add(CIDAR.getId());
        PILabs.add(KhalilLab.getId());
       
                
      
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
        
        
        Institution harvardUniversity = new Institution();
        harvardUniversity.setName("Harvard University");
        harvardUniversity.setDescription("Harvard is the oldest institution of higher education in the United States, established in 1636 by vote of the Great and General Court of the Massachusetts Bay Colony.");
        harvardUniversity.setPhone("(617) 495-1000");
        harvardUniversity.setUrl("www.harvard.edu");
        harvardUniversity.setType(Institution.InstitutionType.University);
        
        Map mitQueryMap = new HashMap();
        mitQueryMap.put("name", "Massachusetts Institute of Technology");
        List<Institution> mitInstituions = ClothoAdapter.queryInstitution(mitQueryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        if (mitInstituions.isEmpty()){
            ClothoAdapter.createInstiution(MIT, clothoObject);
        }
        
        Map buQueryMap = new HashMap();
        buQueryMap.put("name", "Boston University");
        List<Institution> buInsitutions = ClothoAdapter.queryInstitution(buQueryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        if (buInsitutions.isEmpty()){
            ClothoAdapter.createInstiution(bostonUniversity, clothoObject);
        }
        
        Map harvardQueryMap = new HashMap();
        harvardQueryMap.put("name", "Harvard University");
        List<Institution> harvardInstituions = ClothoAdapter.queryInstitution(harvardQueryMap, clothoObject, ClothoAdapter.QueryMode.EXACT);
        if (harvardInstituions.isEmpty()){
            ClothoAdapter.createInstiution(harvardUniversity, clothoObject);
        }
        
        //clothoObject.logout();
        globalPI.setLabs(PILabs);
        if (!globalPI.getId().equals("") && !globalPI.getId().equals("Not Set")){
            ClothoAdapter.setPerson(globalPI, clothoObject);
        }
        conn.closeConnection();
        
        
    }

}
