/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Company;
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
import org.clothocad.phagebook.dom.Sample;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotSame;
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
    public ClothoAdapterTest() {
       //ESTABLISH CONNECTION
       conn = new ClothoConnection(Args.clothoLocation);
       clothoObject = new Clotho(conn);
       Map createUserMap = new HashMap();
       String username = "test"+ System.currentTimeMillis() ;
       createUserMap.put("username", username);
       createUserMap.put("password", "password");
       clothoObject.createUser(createUserMap);
       Map loginMap = new HashMap();
       loginMap.put("username", username);
       loginMap.put("credentials", "password");     
       clothoObject.login(loginMap);
       //
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
        clothoObject.logout();
        conn.closeConnection();
    }

    
    @Test
    public void testCreateCompany()
    {
     
        Company testCompany = new Company();
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
        
        String id2 = ClothoAdapter.createCompany(testCompany, clothoObject);
        assertEquals(id2, testCompany.getId());
        if (id1.equals(testCompany.getId())){
            fail();
        }
        
    }
    @Test
    public void testCreateContainer()
    {
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
        
        
    }
    @Test
    public void testCreateEntry(){
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
        testEntry.setNotebook(notebook);
        
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
        
        testEntry.getNotebook().setOwner(owner);
        testEntry.setDateCreated(dateCreated);
        
        String idE3 = ClothoAdapter.createEntry(testEntry, clothoObject);
        assertEquals(idE3, testEntry.getId());
        if(idE.equals(testEntry.getId())){
            fail();
        }
        
        //Note, we are not creating anything else in clotho except the entry!
        
    
        
    }
    @Test
    public void testCreateFundingAgency(){
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
        
        
    }
    
    @Test
    public void testCreateGrant(){
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
        testGrant.setLeadPI(leadPI);
        testGrant.setProgramManager("Prashant Vaidyanathan");
        
        String idG3 = ClothoAdapter.createGrant(testGrant, clothoObject);
        assertEquals(idG3, testGrant.getId());
        if(idG.equals(testGrant.getId())){
            fail();
        }
        
        
    }
    @Test
    public void testCreateInstitution(){
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
      
        
    }
    @Test
    public void testCreateInstrument(){
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
        
    }
    @Test
    public void testCreateInventory(){
        Inventory testInventory = new Inventory();
        String idI = "";
        testInventory.setId(idI);
        
        String idI2 = ClothoAdapter.createInventory(testInventory, clothoObject);
        assertEquals(idI2, testInventory.getId());
        if(idI.equals(testInventory.getId())){
            fail();
        }
        
        List<Sample> samples = new ArrayList<>();
        Sample sample = new Sample();
        samples.add(sample);
        testInventory.setSamples(samples);
        
        String idI3 = ClothoAdapter.createInventory(testInventory, clothoObject);
        assertEquals(idI3, testInventory.getId());
        if(idI.equals(testInventory.getId())){
            fail();
        }
        
        
        
    }
    @Test
    public void testCreateNotebook(){
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
        testNotebook.setOwner(owner);
        
        String idN3 = ClothoAdapter.createNotebook(testNotebook, clothoObject);
        assertEquals(idN3, testNotebook.getId());
        if(idN.equals(testNotebook.getId())){
            fail();
        }
        
    }
    @Test
    public void testCreateOrder(){
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
        
    }
    @Test
    public void testCreateOrganization(){
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
        
        
    }
    @Test
    public void testCreatePerson(){
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
        
    }
    @Test
    public void testCreateProduct(){
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
        
    }
    @Test
    public void testCreateProject(){
        Project testProject = new Project();
        String idP = "";
        testProject.setId(idP);
        
        String idP2 = ClothoAdapter.createProject(testProject, clothoObject);
        assertEquals(idP2, testProject.getId());
        if(idP.equals(testProject.getId())){
            fail();
        }
    }
    @Test
    public void testCreateProtocol(){
        
    }
    @Test
    public void testCreatePublication(){
        
    }
    @Test
    public void testCreateSample(){
        
    }
    @Test
    public void testCreateStatus(){
        
    }
   
     @Test
    public void testGetCompany(){
        
    }
    @Test
    public void testGetContainer(){
        
    }
    @Test
    public void testGetEntry(){
        
    }
    @Test
    public void testGetFundingAgency(){
        
    }
   
    @Test
    public void testGetGrant(){
        
    }
    @Test
    public void testGetInstitution(){
        
    }
    @Test
    public void testGetInventory(){
        
    }
    @Test
    public void testGetNotebook(){
        
    }
    @Test
    public void testGetOrder(){
        
    }
    
    @Test
    public void testGetPerson(){
        
    }
    @Test
    public void testGetProduct(){
        
    }
    @Test
    public void testGetProject(){
        
    }
    @Test
    public void testGetProtocol(){
        
    }
    @Test
    public void testGetPublication(){
        
    }
    @Test
    public void testGetSample(){
        
    }
    @Test
    public void testGetStatus(){
        
    }
 
    
    //@Test
    public void anotherTest(){
       
       
       //COMPANIES
       Company amazon = new Company("Amazon");
       Company apple = new Company("Apple");
       Company nike = new Company("Nike");
       
       //CLOTHO CREATION
       
       String companyId = (String) ClothoAdapter.createCompany(amazon, clothoObject);
       amazon.setId(companyId);
       
       String companyId2 = (String) ClothoAdapter.createCompany(apple, clothoObject);
       apple.setId(companyId2);
       
       String companyId3 = (String) ClothoAdapter.createCompany(nike, clothoObject);
       nike.setId(companyId3);
       
       
       //PRODUCT: TELESCOPE
       Product telescope = new Product("Telescope", amazon, 1000);
       //PROPERTIES
       telescope.setDescription("See the stars");
       telescope.setGoodType(GoodType.INSTRUMENT);
       telescope.setQuantity(23);
       telescope.setProductURL("www.google.com");
       //
       
       //PRODUCT: MICROSCOPE
       Product microscope = new Product("Microscope",amazon,4000);
       //PROPERTIES
       microscope.setDescription("Magnifies stuff");
       microscope.setQuantity(65);
       microscope.setGoodType(GoodType.INSTRUMENT);
       microscope.setProductURL("www.example.com");
       //
       //
       
       //PRODUCT: MICROSCOPEN
       Product microscopen = new Product("Microscope",nike,3000);
       //PROPERTIES
       microscopen.setDescription("Magnifies stuff but by Nike");
       microscopen.setQuantity(23);
       microscopen.setGoodType(GoodType.INSTRUMENT);
       microscopen.setProductURL("www.nike.com");
       //
       
       //PRODUCT: TELESCOPE2N
       Product telescope2n = new Product("Telescope2", nike, 100);
       //PROPERTIES
       telescope2n.setDescription("See the stars by Nike");
       telescope2n.setGoodType(GoodType.INSTRUMENT);
       telescope2n.setQuantity(12);
       telescope2n.setProductURL("www.nike.com");
       //
       //
       //PRODUCT: MICROSCOPE2 
       Product microscope2 = new Product("Microscope2",apple,2000);
       //PROPERTIES
       microscope2.setDescription("Magnifies stuff BETTER");
       microscope2.setQuantity(40);
       microscope2.setGoodType(GoodType.INSTRUMENT);
       microscope2.setProductURL("www.example2.com");
       //
       //
       
       //PRODUCT: TELESCOPE2
       Product telescope2 = new Product("Telescope2", apple, 1000);
       //PROPERTIES
       telescope2.setDescription("See the stars");
       telescope2.setGoodType(GoodType.INSTRUMENT);
       telescope2.setQuantity(23);
       telescope2.setProductURL("www.google.com");
       //
              
       
       String telescopeID   = (String) ClothoAdapter.createProduct(telescope   , clothoObject);
       String microscopeID  = (String) ClothoAdapter.createProduct(microscope  , clothoObject);
       String microscopenID = (String) ClothoAdapter.createProduct(microscopen , clothoObject);   
       String microscope2ID = (String) ClothoAdapter.createProduct(microscope2 , clothoObject); 
       String telescope2ID =  (String) ClothoAdapter.createProduct(telescope2  , clothoObject);
       String telescope2nID = (String) ClothoAdapter.createProduct(telescope2n , clothoObject);
       
               
       
       
       
       
       
    }
    
}
