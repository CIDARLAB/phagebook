/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static junit.framework.Assert.assertEquals;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.model.Person.PersonRole;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.dom.Container;
import org.clothocad.phagebook.dom.Entry;
import org.clothocad.phagebook.dom.FundingAgency;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Instrument;
import org.clothocad.phagebook.dom.Inventory;
import org.clothocad.phagebook.dom.Notebook;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Protocol;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Sample;
import org.clothocad.phagebook.dom.Status;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Herb
 */
public class ClothoAdapterTest {
    public Clotho clothoObject;
    public ClothoConnection conn;
    public String username;
    public String password; 
    
    public ClothoAdapterTest() {
       
    }
    public String randomUsername(){
        return "test"+ System.currentTimeMillis();
    }
    
    public void clothoCreate(String username, String password)
    {       
       Map createUserMap = new HashMap();  
       createUserMap.put("username", username);
       createUserMap.put("password", password);
       
       clothoObject.createUser(createUserMap);
    }
    
    public void clothoLogin(String username, String password)
    {
       Map loginMap = new HashMap();
       loginMap.put("username", username);
       loginMap.put("credentials", password);  
       
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
       this.conn = new ClothoConnection(Args.clothoLocation);
       this.clothoObject = new Clotho(conn);
       
       this.username = randomUsername();
       this.password = "password";
       clothoCreate(this.username, this.password);
       clothoLogin(this.username, this.password);
    }
    
    @After
    public void tearDown() {
        clothoObject.logout();
        conn.closeConnection();
    }

    
    @Test
    public void testCreateVendor()
    {
        System.out.println("-----CREATE COMPANY TEST-----");
        Vendor testCompany = new Vendor();
        String contact = "Clotho Test";
        String description = "Testing Company Object Creation Clotho";
        String id1 = "";
        String name = "Company Test";
        String phone = "(123)456-7890";
        String url = "red.com";
                
                
                
        testCompany.setContact(contact);
        testCompany.setDescription(description);
        testCompany.setId(id1);
        testCompany.setName(name);
        testCompany.setPhone(phone);
        testCompany.setUrl(url);
        
        String id2 = ClothoAdapter.createVendor(testCompany, clothoObject);
        assertEquals(id2, testCompany.getId());
        if (id1.equals(testCompany.getId())){
            fail();
        }
        
        System.out.println("----------");
        
    }
    @Test
    public void testCreateContainer()
    {
        System.out.println("-----CREATE CONTAINER TEST-----");
        Container testContainer = new Container();
        String description = "Testing Container Object Creation Clotho";
        String id1 = "";
        String name = "Container Test";
        testContainer.setDescription(description);
        testContainer.setId(id1);
        testContainer.setName(name);
        
        String id2 = ClothoAdapter.createContainer(testContainer, clothoObject);
        assertEquals(id2, testContainer.getId());
        if(id1.equals(testContainer.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreateEntry(){
        System.out.println("-----CREATE ENTRY TEST-----");

        Entry testEntry = new Entry();
        Notebook notebook = new Notebook(); //Default NOTEBOOK
        Date dateCreated = new Date();      //Default DATE
        Date lastModified = new Date();     //Default DATE
        String text = "Test Entry";  
        String title = "Test Entry";
        String idE = "";
        
        testEntry.setId(idE);
        testEntry.setTitle(title);
        testEntry.setText(text);
        testEntry.setLastModified(lastModified);
        testEntry.setDateCreated(dateCreated);
        testEntry.setNotebookId(notebook.getId());
        
        String idE2 = ClothoAdapter.createEntry(testEntry, clothoObject);
        assertEquals(idE2, testEntry.getId());
        if(idE.equals(testEntry.getId())){
            fail();
        }
        
        //create a second notebook
        Person owner = new Person();
        owner.setFirstName("Gwen");
        owner.setLastName("Stefani");
        //THIS IS MY BIRTHDAY Y'ALL BETTER REMEMBER IT!
        dateCreated = new Date(787899600000L);
        clothoObject.logout();
        ClothoAdapter.createPerson(owner, clothoObject);
        clothoLogin(this.username, this.password);
        
        testEntry.setDateCreated(dateCreated);
        
        String idE3 = ClothoAdapter.createEntry(testEntry, clothoObject);
        assertEquals(idE3, testEntry.getId());
        if(idE.equals(testEntry.getId())){
            fail();
        }
        
        //Note, we are not creating anything else in clotho except the entry!
        
        System.out.println("----------");
        
    }
    @Test
    public void testCreateFundingAgency(){
        System.out.println("-----CREATE FUNDING AGENCY TEST-----");
 
        FundingAgency testFundingAgency = new FundingAgency();
        String idF = "";
        
        testFundingAgency.setId(idF);
        
        String idF2 = ClothoAdapter.createFundingAgency(testFundingAgency, clothoObject);
        assertEquals(idF2, testFundingAgency.getId());
        if(idF.equals(testFundingAgency.getId())){
            fail();
        }
        
        testFundingAgency.setName("Funding Agency Test");
        testFundingAgency.setDescription("A test of the Clotho Creation of Funding Agency");
        
        String idF3 = ClothoAdapter.createFundingAgency(testFundingAgency, clothoObject);
        assertEquals(idF3, testFundingAgency.getId());
        if(idF.equals(testFundingAgency.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    
    @Test
    public void testCreateGrant(){
        System.out.println("-----CREATE GRANT TEST-----");

        Grant testGrant = new Grant();
        String idG = "";
        testGrant.setId(idG);
        
        String idG2 = ClothoAdapter.createGrant(testGrant, clothoObject);
        assertEquals(idG2, testGrant.getId());
        if(idG.equals(testGrant.getId())){
            fail();
        }
        
        Person leadPI = new Person();
        String testID = "Test ID";
        leadPI.setFirstName("Nelson");
        leadPI.setLastName("Mandela");
        leadPI.setId(testID);
        testGrant.setLeadPI(leadPI.getId());
        testGrant.setProgramManagerId("Not Set");
        
        String idG3 = ClothoAdapter.createGrant(testGrant, clothoObject);
        assertEquals(idG3, testGrant.getId());
        if(idG.equals(testGrant.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreateInstitution(){
        System.out.println("-----CREATE INSTITUTION TEST-----");
      
        Institution testInstitution = new Institution();
        String idI = "";
        testInstitution.setId(idI);
        
        String idI2 = ClothoAdapter.createInstiution(testInstitution, clothoObject);
        assertEquals(idI2, testInstitution.getId());
        if(idI.equals(testInstitution.getId())){
            fail();
        }
        
        testInstitution.setName("Instituion creation test");
        
        String idI3 = ClothoAdapter.createInstiution(testInstitution, clothoObject);
        assertEquals(idI3, testInstitution.getId());
        if (idI.equals(testInstitution.getId())){
            fail();
        }
      
        System.out.println("----------");
    }
    @Test
    public void testCreateInstrument(){
        System.out.println("-----CREATE INSTRUMENT TEST-----");
        
        Instrument testInstrument = new Instrument();
        String idI = "";
        testInstrument.setId(idI);
        
        String idI2 = ClothoAdapter.createInstrument(testInstrument, clothoObject);
        assertEquals(idI2, testInstrument.getId());
        if(idI.equals(testInstrument.getId())){
            fail();
        }
        
        String name = "Test Instrument";
        String description = " Testing Instrument Creation In Clotho";
        
        testInstrument.setName(name);
        testInstrument.setDescription(description);
        
        String idI3 =ClothoAdapter.createInstrument(testInstrument, clothoObject);
        assertEquals(idI3, testInstrument.getId());
        if(idI.equals(testInstrument.getId())){
            fail();
        }
        
        System.out.println("----------");
        
    }
    @Test
    public void testCreateInventory(){
        System.out.println("-----CREATE INVENTORY-----");

        Inventory testInventory = new Inventory();
        String idI = "";
        testInventory.setId(idI);
        
        String idI2 = ClothoAdapter.createInventory(testInventory, clothoObject);
        assertEquals(idI2, testInventory.getId());
        if(idI.equals(testInventory.getId())){
            fail();
        }
        
        List<String> samples = new ArrayList<>();
        Sample sample = new Sample();
        samples.add(sample.getId());
        testInventory.setSamples(samples);
        
        String idI3 = ClothoAdapter.createInventory(testInventory, clothoObject);
        assertEquals(idI3, testInventory.getId());
        if(idI.equals(testInventory.getId())){
            fail();
        }
        
        System.out.println("----------");
        
    }
    @Test
    public void testCreateNotebook(){
        System.out.println("-----CREATE NOTEBOOK TEST-----");
       
        Notebook testNotebook = new Notebook();
        String idN = "";
        testNotebook.setId(idN);
        
        String idN2 = ClothoAdapter.createNotebook(testNotebook, clothoObject);
        assertEquals(idN2, testNotebook.getId());
        if(idN.equals(testNotebook.getId())){
            fail();
        }
        Person owner = new Person();
        owner.setFirstName("Spiders");
        owner.setLastName("George");
        testNotebook.setOwnerId(owner.getId());
        
        String idN3 = ClothoAdapter.createNotebook(testNotebook, clothoObject);
        assertEquals(idN3, testNotebook.getId());
        if(idN.equals(testNotebook.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreateOrder(){
        System.out.println("-----CREATE ORDER TEST-----");

        Order testOrder = new Order();
        String idO = "";
        testOrder.setId(idO);
        
        String idO2 = ClothoAdapter.createOrder(testOrder, clothoObject);
        assertEquals(idO2, testOrder.getId());
        if(idO.equals(testOrder.getId())){
            fail();
        }
        
        testOrder.setName("Order Creation Test");
        
        String idO3 = ClothoAdapter.createOrder(testOrder, clothoObject);
        assertEquals(idO3, testOrder.getId());
        if(idO.equals(testOrder.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreateOrganization(){
        System.out.println("-----CREATE ORGANIZATION TEST-----");
  
        Organization testOrganization = new Organization();
        String idO = "";
        testOrganization.setId(idO);
        
        String idO2 = ClothoAdapter.createOrganization(testOrganization, clothoObject);
        assertEquals(idO2, testOrganization.getId());
        if(idO.equals(testOrganization.getId())){
            fail();
        }
        
        testOrganization.setName("Testing Organization Creation");
        
        String idO3 = ClothoAdapter.createOrganization(testOrganization, clothoObject);
        assertEquals(idO3, testOrganization.getId());
        if(idO.equals(testOrganization.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreatePerson(){
        System.out.println("-----CREATE PERSON TEST-----");
      
        clothoObject.logout(); //HAVE TO LOGOUT OF CLOTHO IF LOGGED IN BECAUSE 
                               //YOU CAN'T EDIT A PERSON OBJECT IF YOU ARE NOT
                               //LOGGED INTO CLOTHO AS THAT PERSON
        Person testPerson = new Person();
        String idP = "";
        testPerson.setId(idP);
        
        String idP2 = ClothoAdapter.createPerson(testPerson, clothoObject);
        assertEquals(idP2, testPerson.getId());
        if(idP.equals(testPerson.getId())){
            fail();
        }
        
        testPerson.setFirstName("Allison");
        testPerson.setLastName("Durkan");
        
        String idP3 = ClothoAdapter.createPerson(testPerson, clothoObject);
        assertEquals(idP3, testPerson.getId());
        if(idP.equals(testPerson.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreateProduct(){
        System.out.println("-----CREATE PRODUCT TEST-----");
  
        Product testProduct = new Product();
        String idP = "";
        testProduct.setId(idP);
        
        String idP2 = ClothoAdapter.createProduct(testProduct, clothoObject);
        assertEquals(idP2, testProduct.getId());
        if(idP.equals(testProduct.getId())){
            fail();
        }
        
        testProduct.setName("Test Product");
        testProduct.setDescription("Testing Product Class");
        
        String idP3 = ClothoAdapter.createProduct(testProduct, clothoObject);
        assertEquals(idP3, testProduct.getId());
        if(idP.equals(testProduct.getId())){
            fail();
        }
        
        System.out.println("----------");
        
    }
    @Test
    public void testCreateProject(){
        System.out.println("-----CREATE PROJECT TEST-----");
  
        Project testProject = new Project();
        String idP = "";
        testProject.setId(idP);
        
        String idP2 = ClothoAdapter.createProject(testProject, clothoObject);
        assertEquals(idP2, testProject.getId());
        if(idP.equals(testProject.getId())){
            fail();
        }
        
        testProject.setDescription("A different project");
        
        String idP3 = ClothoAdapter.createProject(testProject, clothoObject);
        assertEquals(idP3, testProject.getId());
        if(idP.equals(testProject.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreateProtocol(){
        System.out.println("-----CREATE PROTOCOL TEST-----");

        Protocol testProtocol = new Protocol();
        String idP = "";
        testProtocol.setId(idP);
       
        String idP2 = ClothoAdapter.createProtocol(testProtocol, clothoObject);
        assertEquals(idP2, testProtocol.getId());
        if(idP.equals(testProtocol.getId())){
            fail();
        }
        
        testProtocol.setProtocolName("A different protocol");
        
        String idP3 = ClothoAdapter.createProtocol(testProtocol, clothoObject);
        assertEquals(idP3, testProtocol.getId());
        if(idP.equals(testProtocol.getId())){
            fail();
        }
        
        System.out.println("----------");
    }
    @Test
    public void testCreatePublication(){
        System.out.println("-----CREATE PUBLICATION TEST-----");
   
        Publication testPublication = new Publication();
        String idP = "";
        testPublication.setId(idP);
        
        String idP2 = ClothoAdapter.createPublication(testPublication, clothoObject);
        assertEquals(idP2, testPublication.getId());
        if(idP.equals(testPublication.getId())){
            fail();
        }
        
        Publication testPublication2 = new Publication();
        testPublication.setId(idP);
        
        String idP3 = ClothoAdapter.createPublication(testPublication, clothoObject);
        assertEquals(idP3, testPublication.getId());
        if(idP.equals(testPublication.getId())){
            fail();
        }
        
        System.out.println("----------");
        
    }
    @Test
    public void testCreateSample(){
        System.out.println("-----CREATE SAMPLE TEST-----");
      
        Sample testSample = new Sample();
        String idS = "";
        testSample.setId(idS);
        
        String idS2 = ClothoAdapter.createSample(testSample, clothoObject);
        assertEquals(idS2, testSample.getId());
        if(idS.equals(testSample.getId())){
            fail();
        }
        
        testSample.setDescription("Clotho Adapter test of Sample");
        testSample.setName("Sample Test");
        
        String idS3 = ClothoAdapter.createSample(testSample, clothoObject);
        assertEquals(idS3, testSample.getId());
        if(idS.equals(testSample.getId())){
            fail();
        }
       
        System.out.println("----------");
        
    }
    @Test
    public void testCreateStatus(){
        System.out.println("-----CREATE STATUS TEST-----");

        Status testStatus = new Status();
        String idS = "";
        testStatus.setId(idS);
        
        String idS2 = ClothoAdapter.createStatus(testStatus, clothoObject);
        assertEquals(idS2, testStatus.getId());
        if(idS.equals(testStatus.getId())){
            fail();
        }
        
        testStatus.setText("Test Status");
        testStatus.setUserId( (new Person()).getId());
        
        String idS3 = ClothoAdapter.createStatus(testStatus, clothoObject);
        assertEquals(idS3, testStatus.getId());
        if(idS.equals(testStatus.getId())){
            fail();
        }
        System.out.println("----------");
    }
   
    //make and receive back exactly what you made
    @Test
    public void testGetVendor()
    {
        System.out.println("-----GET VENDOR TEST-----");

        //Company fields
        String contact     = "Clotho Test Contact";
        String name        = "Clotho Test Name";
        String description = "Clotho Test Description";
        String phone       = "Clotho Test Phone";
        String url         = "Clotho Test Url";
        //
        Vendor company1 = new Vendor();
        company1.setContact(contact);
        company1.setName(name);
        company1.setDescription(description);
        company1.setPhone(phone);
        company1.setUrl(url);
        
        String companyId = ClothoAdapter.createVendor(company1, clothoObject);
        if(company1.getId().equals("Not Set")){
            fail();
        }
        
        Vendor company2 = ClothoAdapter.getVendor(companyId, clothoObject);
        
        assertEquals(company1.getContact(), company2.getContact());
        assertEquals(company1.getName(), company2.getName());
        assertEquals(company1.getDescription(), company2.getDescription());
        assertEquals(company1.getPhone(), company2.getPhone());
        assertEquals(company1.getUrl(), company2.getUrl());
        assertEquals(company1.getId(), company2.getId());

        System.out.println("----------");
        
    }
    @Test
    public void testGetContainer(){
        System.out.println("-----GET CONTAINER TEST-----");

        //Container Fields
            String name = "Clotho Test Name";
            String description = "Clotho Test Description";
        //
        Container container1 = new Container();
        container1.setName(name);
        container1.setDescription(description);
        
        String containerId = ClothoAdapter.createContainer(container1, clothoObject);
        if(container1.getId().equals("Not Set")){
            fail();
        }
        
        Container container2 = ClothoAdapter.getContainer(containerId, clothoObject);
        
        assertEquals(container1.getId(), container2.getId());
        assertEquals(container1.getName(), container2.getName());
        assertEquals(container1.getDescription(), container2.getDescription());
        
        System.out.println("----------");
    }
    @Test
    public void testGetEntry(){
        System.out.println("-----GET ENTRY TEST-----");
     
        //Entry Fields
            String text         = "Clotho Test Text";
            String title        = "Clotho Test Title";
            Notebook notebook                  = new Notebook();
                
                Person owner = new Person();
                    clothoObject.logout();
                    ClothoAdapter.createPerson(owner, clothoObject); 
                    clothoLogin(this.username, this.password);
                    notebook.setOwnerId(owner.getId());
                ClothoAdapter.createNotebook(notebook, clothoObject);
            Date dateCreated    = new Date(787899600000L);
            Date lastModified   = new Date(787899600000L);
           
        //
        Entry entry1 = new Entry(); 
        entry1.setText(text);
        entry1.setTitle(title);
        entry1.setNotebookId(notebook.getId());
        entry1.setDateCreated(dateCreated);
        entry1.setLastModified(lastModified);
        
        String entryId = ClothoAdapter.createEntry(entry1, clothoObject);
        if(entry1.getId().equals("Not Set")){
            fail();
        }
        Entry entry2 = ClothoAdapter.getEntry(entryId, clothoObject);
        
        assertEquals(entry1.getText(), entry2.getText());
        assertEquals(entry1.getTitle(), entry2.getTitle());
        assertEquals(entry1.getNotebookId(), entry2.getNotebookId());
        assertEquals(entry1.getDateCreated().toString(), entry2.getDateCreated().toString());
        assertEquals(entry1.getLastModified().toString(), entry2.getLastModified().toString());
        assertEquals(entry1.getId(), entry2.getId());
        System.out.println("----------");
    }
    @Test
    public void testGetFundingAgency(){
        System.out.println("-----GET FUNDING AGENCY TEST-----");
       
        String name = "Clotho Test Name";
        String description = "Clotho Test Description";
        String phone = "Clotho Test Phone";
        String url = "Clotho Test Url";
        FundingAgency fundingAgency1 = new FundingAgency();
        fundingAgency1.setName(name);
        fundingAgency1.setDescription(description);
        fundingAgency1.setPhone(phone);
        fundingAgency1.setUrl(url);
        
        String fundingAgencyId = ClothoAdapter.createFundingAgency(fundingAgency1, clothoObject);
        if(fundingAgency1.getId().equals("Not Set")){
            fail();
        }
        
        FundingAgency fundingAgency2 = ClothoAdapter.getFundingAgency(fundingAgencyId, clothoObject);
        
        assertEquals(fundingAgency1.getName(), fundingAgency2.getName());
        assertEquals(fundingAgency1.getDescription(), fundingAgency2.getDescription());
        assertEquals(fundingAgency1.getPhone(), fundingAgency2.getPhone());
        assertEquals(fundingAgency1.getUrl(), fundingAgency2.getUrl());
        assertEquals(fundingAgency1.getId(), fundingAgency2.getId());
        System.out.println("----------");
    }
   
    @Test
    public void testGetGrant()
    {
        System.out.println("-----GET GRANT TEST-----");
 
        //Grant Fields
            String name = "Clotho Test Name";
            Person leadPI = new Person();
                clothoObject.logout();
                ClothoAdapter.createPerson(leadPI, clothoObject);
                clothoLogin(this.username, this.password);
            //coPIs
                List<String> coPIs = new ArrayList<>();
                Person P1 = new Person();
                Person P2 = new Person();
                clothoObject.logout();
                    ClothoAdapter.createPerson(P1, clothoObject);
                    ClothoAdapter.createPerson(P2, clothoObject);
                clothoLogin(this.username, this.password);
                
                coPIs.add(P1.getId());
                coPIs.add(P2.getId());
            String programManager = "Clotho Test Program Manager";
            Date startDate  = new Date(787899600000L);
            Date endDate = new Date(787899600000L);
            Double budget = 0.0d;
            Double amountSpent = 0.0d;
            //projects
            List<String> projects = new ArrayList<>();
                Project Proj1 = new Project();
                Project Proj2 = new Project();
                    ClothoAdapter.createProject(Proj1, clothoObject);
                    ClothoAdapter.createProject(Proj2, clothoObject);
                    projects.add(Proj1.getId());
                    projects.add(Proj2.getId());
            String description = "Clotho Test Description";
        //
            
        Grant grant1 = new Grant();
        grant1.setName(name);
        grant1.setLeadPI(leadPI.getId());
        grant1.setCoPIs(coPIs);
        grant1.setProgramManagerId(programManager);
        grant1.setStartDate(startDate);
        grant1.setEndDate(endDate);
        grant1.setBudget(budget);
        grant1.setAmountSpent(amountSpent);
        grant1.setProjects(projects);
        grant1.setDescription(description);
        
        
        String grantId = ClothoAdapter.createGrant(grant1, clothoObject);
        if(grant1.getId().equals("Not Set")){
            fail();
        }
        
        Grant grant2 = ClothoAdapter.getGrant(grantId, clothoObject);
        
        assertEquals(grant1.getName(), grant2.getName());
        assertEquals(grant1.getLeadPI(), grant2.getLeadPI());
        assertEquals(grant1.getCoPIs().size(), grant2.getCoPIs().size());
        for (int i = 0; i < grant1.getCoPIs().size(); i++)
        {   
            assertEquals(grant1.getCoPIs().get(i), grant2.getCoPIs().get(i));
            
        }
        assertEquals(grant1.getProgramManagerId(), grant2.getProgramManagerId());
        assertEquals(grant1.getStartDate().toString(), grant2.getStartDate().toString());
        assertEquals(grant1.getEndDate().toString(), grant2.getEndDate().toString());
        assertEquals(grant1.getBudget(), grant2.getBudget());
        assertEquals(grant1.getAmountSpent(), grant2.getAmountSpent());
        assertEquals(grant1.getProjects().size(), grant2.getProjects().size());
        for (int i = 0; i < grant1.getProjects().size(); i++)
        {
            assertEquals(grant1.getProjects().get(i), grant2.getProjects().get(i));
        }
        assertEquals(grant1.getDescription(), grant2.getDescription());
        
               
        
        
        System.out.println("----------");
        
        
        
    }
    @Test
    public void testGetInstitution(){
        System.out.println("-----GET INSTITUTION TEST-----");
        //INSTITUTION FIELDS
            String name        = "Clotho Test Name";
            String description = "Clotho Test Description";
            String phone       = "Clotho Test Phone";
            String url         = "Clotho Test Url";
        //
        Institution institution1 = new Institution();
        institution1.setName(name);
        institution1.setDescription(description);
        institution1.setPhone(phone);
        institution1.setUrl(url);
        
        String insitutionId = ClothoAdapter.createInstiution(institution1, clothoObject);
        if(institution1.getId().equals("Not Set")){
            fail();
        }
        
        Institution institution2 = ClothoAdapter.getInstitution(insitutionId, clothoObject);
        
        assertEquals(institution1.getName(), institution2.getName());
        assertEquals(institution1.getDescription(), institution2.getDescription());
        assertEquals(institution1.getPhone(), institution2.getPhone());
        assertEquals(institution1.getUrl(), institution2.getUrl());
        assertEquals(institution1.getId(), institution2.getId());
                
        System.out.println("----------");
        
    }
    @Test
    public void testGetInventory(){
        System.out.println("-----GET INVENTORY TEST-----");
        //INVENTORY FIELDS
        List<String> samples = new ArrayList<>();
            Sample S1 = new Sample();
            Sample S2 = new Sample();
                ClothoAdapter.createSample(S1, clothoObject);
                ClothoAdapter.createSample(S2, clothoObject);
            samples.add(S1.getId());
            samples.add(S2.getId());
        List<String> instruments = new ArrayList<>();
            Instrument I1 = new Instrument();
            Instrument I2 = new Instrument();
                ClothoAdapter.createInstrument(I1, clothoObject);
                ClothoAdapter.createInstrument(I2, clothoObject);
            instruments.add(I1.getId());
            instruments.add(I2.getId());   
        //
        
        Inventory inventory1 = new Inventory();
        inventory1.setSamples(samples);
        inventory1.setInstruments(instruments);
        
        String inventoryId = ClothoAdapter.createInventory(inventory1, clothoObject);
        if(inventory1.getId().equals("Not Set")){
            fail();
        }
        
        Inventory inventory2 = ClothoAdapter.getInventory(inventoryId, clothoObject);
        
        assertEquals(inventory1.getSamples().size(), inventory2.getSamples().size());
        for (int i = 0; i < inventory1.getSamples().size(); i++){
            assertEquals(inventory1.getSamples().get(i), inventory2.getSamples().get(i));
        }
        assertEquals(inventory1.getInstruments().size(),inventory2.getInstruments().size());
        for (int i = 0; i < inventory1.getInstruments().size(); i++){
            assertEquals(inventory1.getInstruments().get(i), inventory2.getInstruments().get(i));
        }
        
        System.out.println("----------");
    }
    @Test
    public void testGetNotebook(){
        System.out.println("-----GET NOTEBOOK TEST-----");
        //Notebook fields
            Person owner = new Person();
                clothoObject.logout();
                ClothoAdapter.createPerson(owner, clothoObject);
                clothoLogin(this.username, this.password);
            List<String> entries = new ArrayList<>();
                Entry E1 = new Entry();
                Entry E2 = new Entry();
                    ClothoAdapter.createEntry(E1, clothoObject);
                    ClothoAdapter.createEntry(E2, clothoObject);
                entries.add(E1.getId());
                entries.add(E2.getId());
            Project affiliatedProject = new Project();
                ClothoAdapter.createProject(affiliatedProject, clothoObject);
            Date dateCreated = new Date(787899600000L);              
        //
        Notebook notebook1 = new Notebook();
        notebook1.setOwnerId(owner.getId());
        notebook1.setEntries(entries);
        notebook1.setAffiliatedProjectId(affiliatedProject.getId());
        notebook1.setDateCreated(dateCreated);
        
        String notebookId = ClothoAdapter.createNotebook(notebook1, clothoObject);
        if(notebook1.getId().equals("Not Set")){
            fail();
        }
        
        Notebook notebook2 = ClothoAdapter.getNotebook(notebookId, clothoObject);
        assertEquals(notebook1.getId(), notebook2.getId());
        assertEquals(notebook1.getOwnerId(), notebook2.getOwnerId());
        assertEquals(notebook1.getEntries().size(), notebook2.getEntries().size());
        for (int i = 0; i < notebook1.getEntries().size(); i++){
            assertEquals(notebook1.getEntries().get(i), notebook2.getEntries().get(i));
        }
        assertEquals(notebook1.getAffiliatedProjectId(), notebook2.getAffiliatedProjectId());
        assertEquals(notebook1.getDateCreated().toString(), notebook2.getDateCreated().toString());
        
        
        System.out.println("----------");
    }
    @Test
    public void testGetOrder(){
        System.out.println("-----GET ORDER TEST-----");
        //Order Fields
            String name = "Clotho Test Name";
            String description = "Clotho Test Description";
            Date dateCreated = new Date(787899600000L);
            Person createdBy = new Person();
                clothoObject.logout();
                ClothoAdapter.createPerson(createdBy, clothoObject);
                clothoLogin(this.username, this.password);
            Map<String, Integer> cartItems = new HashMap<>();
                CartItem C1 = new CartItem();
                CartItem C2 = new CartItem();
                //CART ITEM PROPERTIES
         
                    Product P1 = new Product();
                    Product P2 = new Product();
                    
                   
                    
                
                    ClothoAdapter.createProduct(P1, clothoObject);
                    ClothoAdapter.createProduct(P2, clothoObject);
                    Map<String, Double> productsWithDiscount1 = new HashMap();
                    productsWithDiscount1.put(P1.getId(), 100.0);
                    Map<String, Double> productsWithDiscount2 = new HashMap();
                    productsWithDiscount2.put(P2.getId(), 50.0);
                    C1.setProductWithDiscount(productsWithDiscount1);
                    C2.setProductWithDiscount(productsWithDiscount2);
                    ClothoAdapter.createCartItem(C1, clothoObject);
                    ClothoAdapter.createCartItem(C2, clothoObject);
                //
                cartItems.put(C1.getId(), 10);
                cartItems.put(C2.getId(), 20);
                
                //
        //
        Order order1 = new Order();
        order1.setName(name);
        order1.setDescription(description);
        order1.setDateCreated(dateCreated);
        order1.setCreatedById(createdBy.getId());
        order1.setProducts(cartItems);
        
        String orderId = ClothoAdapter.createOrder(order1, clothoObject);
        if(order1.getId().equals("Not Set")){
            fail();
        }
        
        Order order2 = ClothoAdapter.getOrder(orderId, clothoObject);
        assertEquals(order1.getId(), order2.getId());
        assertEquals(order1.getName(), order2.getName());
        assertEquals(order1.getDescription(), order2.getDescription());
        assertEquals(order1.getDateCreated().toString(), order2.getDateCreated().toString());
        assertEquals(order1.getCreatedById(), order2.getCreatedById());
        

        Map <String, Integer> idQuantityMap1 = new HashMap<>();
        
        for (Map.Entry pair : order1.getProducts().entrySet()) {
                if (((String) pair.getKey()) != null){
                    idQuantityMap1.put(((String)pair.getKey()), (Integer) pair.getValue());
                }
            }
        Map <String, Integer> idQuantityMap2 = new HashMap<>();
        for (Map.Entry pair : order2.getProducts().entrySet()){
            if  (((String) pair.getKey()) != null){
                idQuantityMap2.put(((String)pair.getKey()), (Integer) pair.getValue());
            }
        }
        if (idQuantityMap1.equals(idQuantityMap2)){
            System.out.println("THIS RETURNS TRUE");
            
        } else {
            fail();
        }
       
        
        
        System.out.println("----------");
    }
    
    @Test
    public void testGetPerson(){
        System.out.println("-----GET PERSON TEST-----");
        //PERSON FIELDS
            String salt = "CLOTHO TEST SALT";
            byte[] saltedEmailHash = new byte[1];
            String emailId = "CLOTHO TEST EMAIL";
            String firstName = "Doug";
            String lastName = "Densmore";
            String password = "CLOTHO TEST PASSWORD";
            boolean activated = true;
            String activationString = "CLOTHO TEST ACTIVATION STRING";
            
            List<String> colleagues = new ArrayList<>();
                Person P1 = new Person();
                Person P2 = new Person();
                Person P3 = new Person();
                 
                    clothoObject.logout();
                    ClothoAdapter.createPerson(P1, clothoObject);
                    ClothoAdapter.createPerson(P2, clothoObject);
                    ClothoAdapter.createPerson(P3, clothoObject);
                    
                    clothoLogin(this.username, this.password);
                    
                colleagues.add(P1.getId());
                colleagues.add(P2.getId());
                colleagues.add(P3.getId());
            List<String> notebooks = new ArrayList<>();
                Notebook N1 = new Notebook();
                Notebook N2 = new Notebook();
                Notebook N3 = new Notebook();
                    ClothoAdapter.createNotebook(N1, clothoObject);
                    ClothoAdapter.createNotebook(N2, clothoObject);
                    ClothoAdapter.createNotebook(N3, clothoObject);
                notebooks.add(N1.getId());
                notebooks.add(N2.getId());
                notebooks.add(N3.getId());
            List<String> statuses = new ArrayList<>();
                Status S1 = new Status();
                Status S2 = new Status();
                Status S3 = new Status();
                    ClothoAdapter.createStatus(S1, clothoObject);
                    ClothoAdapter.createStatus(S2, clothoObject);
                    ClothoAdapter.createStatus(S3, clothoObject);
                statuses.add(S1.getId());
                statuses.add(S2.getId());
                statuses.add(S3.getId());
            List<String> labs = new ArrayList<>();
                Institution L1 = new Institution();
                Institution L2 = new Institution();
                Institution L3 = new Institution();
                    ClothoAdapter.createInstiution(L1, clothoObject);
                    ClothoAdapter.createInstiution(L2, clothoObject);
                    ClothoAdapter.createInstiution(L3, clothoObject);
                labs.add(L1.getId());
                labs.add(L2.getId());    
                labs.add(L3.getId());
            List<String> projects = new ArrayList<>();
                Project Pro1 = new Project();
                Project Pro2 = new Project();
                Project Pro3 = new Project();
                    ClothoAdapter.createProject(Pro1, clothoObject);
                    ClothoAdapter.createProject(Pro2, clothoObject);
                    ClothoAdapter.createProject(Pro3, clothoObject);
                projects.add(Pro1.getId());
                projects.add(Pro2.getId());
                projects.add(Pro3.getId());
            List<String> publications = new ArrayList<>();
                Publication Pub1 = new Publication();
                Publication Pub2 = new Publication();
                Publication Pub3 = new Publication();
                    ClothoAdapter.createPublication(Pub1, clothoObject);
                    ClothoAdapter.createPublication(Pub2, clothoObject);
                    ClothoAdapter.createPublication(Pub3, clothoObject);
                publications.add(Pub1.getId());
                publications.add(Pub2.getId());
                publications.add(Pub3.getId());
            Map<String, Set<PersonRole>> roles = new HashMap();
                //institutionId and a Set<PersonRole> object
                String ID1 = L1.getId();
                String ID2 = L2.getId();
                String ID3 = L3.getId();
                Set<PersonRole> personRoles1= new HashSet<>();
                    personRoles1.add(PersonRole.GRADSTUDENT);
                    personRoles1.add(PersonRole.PI);
                Set<PersonRole> personRoles2 = new HashSet<>();
                    personRoles2.add(PersonRole.VISITINGRESEARCHER);
                    personRoles2.add(PersonRole.RAPROFESSOR);
                Set<PersonRole> personRoles3 = new HashSet<>();
                    personRoles3.add(PersonRole.POSTDOC);
                    personRoles3.add(PersonRole.LABMANAGER);
                roles.put(ID1, personRoles1);
                roles.put(ID2, personRoles2);
                roles.put(ID3, personRoles3);
            List<String> orders = new ArrayList<>();
                Order O1 = new Order();
                Order O2 = new Order();
                Order O3 = new Order();
                    ClothoAdapter.createOrder(O1, clothoObject);
                    ClothoAdapter.createOrder(O2, clothoObject);
                    ClothoAdapter.createOrder(O3, clothoObject);
                orders.add(O1.getId());
                orders.add(O2.getId());
                orders.add(O3.getId());
                
        //
        Person person1 = new Person();
        person1.setSalt(salt);
        person1.setSaltedEmailHash(saltedEmailHash);
        person1.setEmailId(emailId);
        person1.setFirstName(firstName);
        person1.setLastName(lastName);
        person1.setPassword(password);
        person1.setActivated(activated);
        person1.setActivationString(activationString);
        person1.setColleagues(colleagues);
        person1.setNotebooks(notebooks);
        person1.setStatuses(statuses);
        person1.setLabs(labs);
        person1.setProjects(projects);
        person1.setPublications(publications);
        person1.setRoles(roles);
        person1.setOrders(orders);
        
        
        String personId = ClothoAdapter.createPerson(person1, clothoObject);
           if(person1.getId().equals("Not Set"))
        {
            fail();
        }
           
        Person person2 = ClothoAdapter.getPerson(personId, clothoObject);
        
        assertEquals(person1.getId(), person2.getId());
        assertEquals(person1.getSalt(), person2.getSalt());
        Assert.assertArrayEquals(person1.getSaltedEmailHash(), person2.getSaltedEmailHash());
        assertEquals(person1.getEmailId(), person2.getEmailId());
        assertEquals(person1.getFirstName(), person2.getFirstName());
        assertEquals(person1.getLastName(), person2.getLastName());
        assertEquals(person1.getPassword(), person2.getPassword());
        assertEquals(person1.isActivated(), person2.isActivated());
        assertEquals(person1.getActivationString(), person2.getActivationString());
        assertEquals(person1.getColleagues().size(), person2.getColleagues().size());
        assertEquals(person1.getColleagues().size(), person2.getColleagues().size());
        for (int i = 0; i < person1.getColleagues().size(); i++){
            assertEquals(person1.getColleagues().get(i), person2.getColleagues().get(i));
        }
        assertEquals(person1.getNotebooks().size(), person2.getNotebooks().size());
        for (int i = 0; i < person1.getNotebooks().size(); i++){
            assertEquals(person1.getNotebooks().get(i), person2.getNotebooks().get(i));
        }
        assertEquals(person1.getStatuses().size(), person2.getStatuses().size());
        for (int i = 0; i < person1.getStatuses().size(); i++){
            assertEquals(person1.getStatuses().get(i), person2.getStatuses().get(i));
        }
        assertEquals(person1.getLabs().size(), person2.getLabs().size());
        for (int i = 0; i < person1.getLabs().size(); i++){
            assertEquals(person1.getLabs().get(i), person2.getLabs().get(i));
        }
        assertEquals(person1.getProjects().size(), person2.getProjects().size());
        for (int i = 0; i < person1.getProjects().size(); i++){
            assertEquals(person1.getProjects().get(i), person2.getProjects().get(i));
        }
        assertEquals(person1.getPublications().size(), person2.getPublications().size());
        for (int i = 0; i < person1.getPublications().size(); i++){
            assertEquals(person1.getPublications().get(i), person2.getPublications().get(i));
        }
        if (!person1.getRoles().equals(person2.getRoles())){
            fail("Person Roles are not equal");
        }
        assertEquals(person1.getOrders().size(), person2.getOrders().size());
        for (int i = 0; i < person1.getOrders().size(); i++){
            assertEquals(person1.getOrders().get(i), person2.getOrders().get(i));
        }
        System.out.println("----------");
    }
    @Test
    public void testGetProduct(){
        System.out.println("-----GET PRODUCT TEST-----");
        //PRODUCT FIELDS
        String productURL = "CLOTHO TEST URL";
        Vendor company = new Vendor();
            ClothoAdapter.createVendor(company, clothoObject);
        GoodType goodType = GoodType.SAMPLE;
        double cost = 10.0d;
        int inventory = 5;
        String name = "CLOTHO TEST NAME";
        String description = "CLOTHO TEST DESCRIPTION";
        
        //
        
        Product product1 = new Product();
        product1.setProductURL(productURL);
        product1.setCompanyId(company.getId());
        product1.setGoodType(goodType);
        product1.setCost(cost);
        product1.setInventory(inventory);
        
        product1.setName(name);
        product1.setDescription(description);
        
        String productId = ClothoAdapter.createProduct(product1, clothoObject);
        if(product1.getId().equals("Not Set"))
        {
            fail();
        }
        
        Product product2 = ClothoAdapter.getProduct(productId, clothoObject);
        
        assertEquals(product1.getId(), product2.getId());
        assertEquals(product1.getProductURL(), product2.getProductURL());
        assertEquals(product1.getCompanyId(), product2.getCompanyId());
        assertEquals(product1.getGoodType().toString(), product2.getGoodType().toString());
        assertEquals(product1.getCost(), product2.getCost());
        assertEquals(product1.getInventory(), product2.getInventory());
        assertEquals(product1.getName(), product2.getName());
        assertEquals(product1.getDescription(), product2.getDescription());
        
        System.out.println("----------");
    }
    @Test
    public void testGetProject(){
        System.out.println("-----GET PROJECT TEST-----");
        //PROJECT PROPERTIES
        Person creator = new Person();
        Person lead = new Person();
            clothoObject.logout();
            ClothoAdapter.createPerson(creator, clothoObject);
            ClothoAdapter.createPerson(lead, clothoObject);
            clothoLogin(this.username, this.password);
        List<String> members = new ArrayList<>();
            Person P1 = new Person();
            Person P2 = new Person();
            Person P3 = new Person();
                clothoObject.logout();
                ClothoAdapter.createPerson(P1, clothoObject);
                ClothoAdapter.createPerson(P2, clothoObject);
                ClothoAdapter.createPerson(P3, clothoObject);
                clothoLogin(this.username, this.password);
            members.add(P1.getId());
            members.add(P2.getId());
            members.add(P3.getId());
        List<String> notebooks = new ArrayList<>();
            Notebook N1 = new Notebook();
            Notebook N2 = new Notebook();
            Notebook N3 = new Notebook();
                ClothoAdapter.createNotebook(N1, clothoObject);
                ClothoAdapter.createNotebook(N2, clothoObject);
                ClothoAdapter.createNotebook(N3, clothoObject);
            notebooks.add(N1.getId());
            notebooks.add(N2.getId());
            notebooks.add(N3.getId());    
        List<String> affiliatedLabs = new ArrayList<>();
            Organization O1 = new Organization();
            Organization O2 = new Organization();
            Organization O3 = new Organization();
                ClothoAdapter.createOrganization(O1, clothoObject);
                ClothoAdapter.createOrganization(O2, clothoObject);
                ClothoAdapter.createOrganization(O3, clothoObject);
            affiliatedLabs.add(O1.getId());
            affiliatedLabs.add(O2.getId());    
            affiliatedLabs.add(O3.getId());
        String name = "CLOTHO TEST NAME";
        Date dateCreated = new Date(787899600000L);
        List<String> updates = new ArrayList<>();
            Status S1 = new Status();
            Status S2 = new Status();
            Status S3 = new Status();
                ClothoAdapter.createStatus(S1, clothoObject);
                ClothoAdapter.createStatus(S2, clothoObject);
                ClothoAdapter.createStatus(S3, clothoObject);
            updates.add(S1.getId());
            updates.add(S2.getId());
            updates.add(S3.getId());
        Double budget = 0.0d;
        Grant grant = new Grant();
            ClothoAdapter.createGrant(grant, clothoObject);
        String description = "CLOTHO TEST DESCRIPTION";
        //
        Project project1 = new Project();
        project1.setCreatorId(creator.getId());
        project1.setLeadId(lead.getId());
        project1.setMembers(members);
        project1.setNotebooks(notebooks);
        project1.setAffiliatedLabs(affiliatedLabs);
        project1.setName(name);
        project1.setDateCreated(dateCreated);
        project1.setUpdates(updates);
        project1.setBudget(budget);
        project1.setGrantId(grant.getId());
        project1.setDescription(description);
        
        String projectId = ClothoAdapter.createProject(project1, clothoObject);
        if(project1.getId().equals("Not Set")){
            fail("ID not set");
        }
        
        Project project2 = ClothoAdapter.getProject(projectId, clothoObject);
        
        assertEquals(project1.getId(), project2.getId());
        assertEquals(project1.getCreatorId(), project2.getCreatorId());
        assertEquals(project1.getLeadId(), project2.getLeadId());
        assertEquals(project1.getMembers().size(), project2.getMembers().size());
        for (int i = 0; i < project1.getMembers().size(); i++){
            assertEquals(project1.getMembers().get(i), project2.getMembers().get(i));
        }
        assertEquals(project1.getNotebooks().size(), project2.getNotebooks().size());
        for (int i = 0; i < project1.getNotebooks().size(); i++){
            assertEquals(project1.getNotebooks().get(i), project2.getNotebooks().get(i));
        }
        
        assertEquals(project1.getAffiliatedLabs().size(), project2.getAffiliatedLabs().size());
        for (int i = 0; i < project1.getAffiliatedLabs().size(); i++){
            assertEquals(project1.getAffiliatedLabs().get(i), project2.getAffiliatedLabs().get(i));
        }
        
        assertEquals(project1.getName(), project2.getName());
        assertEquals(project1.getDateCreated().toString(), project2.getDateCreated().toString());
        
        assertEquals(project1.getUpdates().size(), project2.getUpdates().size());
        for (int i = 0; i < project1.getUpdates().size(); i++){
            assertEquals(project1.getUpdates().get(i), project2.getUpdates().get(i));
        }
        
        assertEquals(project1.getBudget(), project2.getBudget());
        assertEquals(project1.getGrantId(), project2.getGrantId());
        
        assertEquals(project1.getDescription(), project2.getDescription());
     
        System.out.println("----------");
    }
    @Test
    public void testGetProtocol(){
        System.out.println("-----GET PROTOCOL TEST-----");
        //PROTOCOL PROPERTIES
            Person creator = new Person();
                clothoObject.logout();
                ClothoAdapter.createPerson(creator, clothoObject);
                clothoLogin(this.username, this.password);
            String protocolName = "CLOTHO TEST PROTOCOL NAME";
            List<String> equipment = new ArrayList<>();
                Instrument I1 = new Instrument();
                Instrument I2 = new Instrument();
                    ClothoAdapter.createInstrument(I1, clothoObject);
                    ClothoAdapter.createInstrument(I2, clothoObject);
                equipment.add(I1.getId());
                equipment.add(I2.getId());
            List<String> samples = new ArrayList<>();
                Sample S1 = new Sample();
                Sample S2 = new Sample();
                    ClothoAdapter.createSample(S1, clothoObject);
                    ClothoAdapter.createSample(S2, clothoObject);
                samples.add(S1.getId());
                samples.add(S2.getId());
        //
        Protocol protocol1 = new Protocol();
        protocol1.setCreatorId(creator.getId());
        protocol1.setProtocolName(protocolName);
        protocol1.setEquipment(equipment);
        protocol1.setSamples(samples);
        
        String protocolId = ClothoAdapter.createProtocol(protocol1, clothoObject);
        if (protocol1.getId().equals("Not Set")){
            fail("ID not set");
        }
        
        Protocol protocol2 = ClothoAdapter.getProtocol(protocolId, clothoObject);
        
        assertEquals(protocol1.getId(), protocol2.getId());
        assertEquals(protocol1.getCreatorId(), protocol2.getCreatorId());
        assertEquals(protocol1.getProtocolName(), protocol2.getProtocolName());
        assertEquals(protocol1.getEquipment().size(), protocol2.getEquipment().size());
        for (int i = 0; i < protocol1.getEquipment().size(); i++){
            assertEquals(protocol1.getEquipment().get(i), protocol2.getEquipment().get(i));
        }
        assertEquals(protocol1.getSamples().size(), protocol2.getSamples().size());
        for (int i = 0; i < protocol1.getSamples().size(); i++){
            assertEquals(protocol1.getSamples().get(i), protocol2.getSamples().get(i));
        }
        
     
        
        System.out.println("----------");
    }
    @Test
    public void testGetPublication(){
        System.out.println("-----GET PUBLICATION TEST-----");
        //PUBLICATION PROPERTIES
        //
        Publication publication1 = new Publication();
        
        String publicationId = ClothoAdapter.createPublication(publication1, clothoObject);
        if (publication1.getId().equals("Not Set")){
            fail("ID not set");
        }
        
        Publication publication2 = ClothoAdapter.getPublication(publicationId, clothoObject);
        assertEquals(publication1.getId(), publication2.getId());
        
        System.out.println("----------");
    }
    @Test
    public void testGetSample(){
        System.out.println("-----GET SAMPLE TEST-----");
        //SAMPLE PROPERTIES
            String name = "CLOTHO TEST NAME";
            String description = "CLOTHO TEST DESCRIPTION";
        //
        Sample sample1 = new Sample();
        sample1.setName(name);
        sample1.setDescription(description);
        
        String sampleId = ClothoAdapter.createSample(sample1, clothoObject);
        if(sample1.getId().equals("Not Set")){
            fail("Id not set");
        }
        Sample sample2 = ClothoAdapter.getSample(sampleId, clothoObject);
        
        assertEquals(sample1.getId(), sample2.getId());
        assertEquals(sample1.getName(), sample2.getName());
        assertEquals(sample1.getDescription(), sample2.getDescription());
        
        System.out.println("----------");
    }
    @Test
    public void testGetStatus(){
        System.out.println("-----GET STATUS TEST-----");
        //STATUS FIELDS 
            String text = "CLOTHO TEST TEXT";
            Person user = new Person();
                clothoObject.logout();
                ClothoAdapter.createPerson(user, clothoObject);
                clothoLogin(this.username, this.password);
            Date created = new Date(787899600000L);
            
        //
        Status status1 = new Status();
        status1.setText(text);
        status1.setUserId(user.getId());
        status1.setCreated(created);
        
        String statusId = ClothoAdapter.createStatus(status1, clothoObject);
        if (status1.getId().equals("Not Set")){
            Assert.fail("Id not set.");
        }
        
        Status status2 = ClothoAdapter.getStatus(statusId, clothoObject);
        
        assertEquals(status1.getId(), status2.getId());
        assertEquals(status1.getText(), status2.getText());
        assertEquals(status1.getUserId(), status2.getUserId());
        assertEquals(status1.getCreated().toString(), status2.getCreated().toString());
        System.out.println("----------");
    }
 
    
    
    
}
