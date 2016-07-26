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
import org.clothocad.phagebook.dom.ClothoBaseObject;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Inventory;
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.Notebook;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Project;
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
 * @author Herb These test will run on your local instance of Clotho
 */
public class ClothoAdapterTest {

    public Clotho clothoObject;
    public ClothoConnection conn;
    public String username;
    public String password;
    public ClothoBaseObject currentWorkingObject;

    public ClothoAdapterTest() {

    }

    public String randomUsername() {
        return "test" + System.currentTimeMillis();
    }

    public void clothoCreate(String username, String password) {
        Map createUserMap = new HashMap();
        createUserMap.put("username", username);
        createUserMap.put("password", password);

        clothoObject.createUser(createUserMap);
    }

    public void clothoLogin(String username, String password) {
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
        this.conn = new ClothoConnection(Args.clothoLocationLocal);
        this.clothoObject = new Clotho(conn);

        this.username = randomUsername();
        this.password = "password";
        clothoCreate(this.username, this.password);
        clothoLogin(this.username, this.password);
    }

    @After
    public void tearDown() {
        if (currentWorkingObject != null){
            if (currentWorkingObject.idForObject() != null && !currentWorkingObject.idForObject().equals("Not Set") && !currentWorkingObject.idForObject().equals("") ){
//                clothoObject.destroy(currentWorkingObject.idForObject());
                currentWorkingObject = null;
            }
        }
        clothoObject.logout();
        conn.closeConnection();
    }

    @Test
    public void testCreateVendor() {
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
        if (id1.equals(testCompany.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testCompany;
        System.out.println("----------");

    }

    @Test
    public void testCreateGrant() {
        System.out.println("-----CREATE GRANT TEST-----");

        Grant testGrant = new Grant();
        String idG = "";
        testGrant.setId(idG);

        String idG2 = ClothoAdapter.createGrant(testGrant, clothoObject);
        assertEquals(idG2, testGrant.getId());
        if (idG.equals(testGrant.getId())) {
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
        if (idG.equals(testGrant.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testGrant;
        System.out.println("----------");
    }

    @Test
    public void testCreateInstitution() {
        System.out.println("-----CREATE INSTITUTION TEST-----");

        Institution testInstitution = new Institution();
        String idI = "";
        testInstitution.setId(idI);

        String idI2 = ClothoAdapter.createInstiution(testInstitution, clothoObject);
        assertEquals(idI2, testInstitution.getId());
        if (idI.equals(testInstitution.getId())) {
            fail();
        }

        testInstitution.setName("Instituion creation test");

        String idI3 = ClothoAdapter.createInstiution(testInstitution, clothoObject);
        assertEquals(idI3, testInstitution.getId());
        if (idI.equals(testInstitution.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testInstitution;
        System.out.println("----------");
    }

    @Test
    public void testCreateInventory() {
        System.out.println("-----CREATE INVENTORY-----");

        Inventory testInventory = new Inventory();
        String idI = "";
        testInventory.setId(idI);

        String idI2 = ClothoAdapter.createInventory(testInventory, clothoObject);
        assertEquals(idI2, testInventory.getId());
        if (idI.equals(testInventory.getId())) {
            fail();
        }

        List<String> samples = new ArrayList<>();
        Sample sample = new Sample();
        samples.add(sample.getId());
        testInventory.setSamples(samples);

        String idI3 = ClothoAdapter.createInventory(testInventory, clothoObject);
        assertEquals(idI3, testInventory.getId());
        if (idI.equals(testInventory.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testInventory;
        System.out.println("----------");

    }

    @Test
    public void testCreateLab() {
        System.out.println("-----CREATE LAB TEST-----");

        Lab testLab = new Lab();
        String idI = "";
        testLab.setId(idI);

        String idI2 = ClothoAdapter.createLab(testLab, clothoObject);
        assertEquals(idI2, testLab.getId());
        if (idI.equals(testLab.getId())) {
            fail();
        }

        testLab.setName("Lab creation test");

        String idI3 = ClothoAdapter.createLab(testLab, clothoObject);
        assertEquals(idI3, testLab.getId());
        if (idI.equals(testLab.getId())) {
            fail();
        }

        this.currentWorkingObject = testLab;
        System.out.println("----------");
    }

    @Test
    public void testCreateNotebook() {
        System.out.println("-----CREATE NOTEBOOK TEST-----");

        Notebook testNotebook = new Notebook();
        String idN = "";
        testNotebook.setId(idN);

        String idN2 = ClothoAdapter.createNotebook(testNotebook, clothoObject);
        assertEquals(idN2, testNotebook.getId());
        if (idN.equals(testNotebook.getId())) {
            fail();
        }
        Person owner = new Person();
        owner.setFirstName("Spiders");
        owner.setLastName("George");
        testNotebook.setOwnerId(owner.getId());

        String idN3 = ClothoAdapter.createNotebook(testNotebook, clothoObject);
        assertEquals(idN3, testNotebook.getId());
        if (idN.equals(testNotebook.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testNotebook;
        System.out.println("----------");
    }

    @Test
    public void testCreateOrder() {
        System.out.println("-----CREATE ORDER TEST-----");

        Order testOrder = new Order();
        String idO = "";
        testOrder.setId(idO);

        String idO2 = ClothoAdapter.createOrder(testOrder, clothoObject);
        assertEquals(idO2, testOrder.getId());
        if (idO.equals(testOrder.getId())) {
            fail();
        }

        testOrder.setName("Order Creation Test");

        String idO3 = ClothoAdapter.createOrder(testOrder, clothoObject);
        assertEquals(idO3, testOrder.getId());
        if (idO.equals(testOrder.getId())) {
            fail();
        }

        this.currentWorkingObject = testOrder;
        System.out.println("----------");
    }

    @Test
    public void testCreateOrganization() {
        System.out.println("-----CREATE ORGANIZATION TEST-----");

        Organization testOrganization = new Organization();
        String idO = "";
        testOrganization.setId(idO);

        String idO2 = ClothoAdapter.createOrganization(testOrganization, clothoObject);
        assertEquals(idO2, testOrganization.getId());
        if (idO.equals(testOrganization.getId())) {
            fail();
        }

        testOrganization.setName("Testing Organization Creation");

        String idO3 = ClothoAdapter.createOrganization(testOrganization, clothoObject);
        assertEquals(idO3, testOrganization.getId());
        if (idO.equals(testOrganization.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testOrganization;
        System.out.println("----------");
    }

    @Test
    public void testCreatePerson() {
        System.out.println("-----CREATE PERSON TEST-----");

        clothoObject.logout(); //HAVE TO LOGOUT OF CLOTHO IF LOGGED IN BECAUSE 
        //YOU CAN'T EDIT A PERSON OBJECT IF YOU ARE NOT
        //LOGGED INTO CLOTHO AS THAT PERSON
        Person testPerson = new Person();
        String idP = "";
        testPerson.setId(idP);

        String idP2 = ClothoAdapter.createPerson(testPerson, clothoObject);
        assertEquals(idP2, testPerson.getId());
        if (idP.equals(testPerson.getId())) {
            fail();
        }

        testPerson.setFirstName("Allison");
        testPerson.setLastName("Durkan");

        String idP3 = ClothoAdapter.createPerson(testPerson, clothoObject);
        assertEquals(idP3, testPerson.getId());
        if (idP.equals(testPerson.getId())) {
            fail();
        }

        this.currentWorkingObject = testPerson;
        System.out.println("----------");
    }

    @Test
    public void testCreateProduct() {
        System.out.println("-----CREATE PRODUCT TEST-----");

        Product testProduct = new Product();
        String idP = "";
        testProduct.setId(idP);

        String idP2 = ClothoAdapter.createProduct(testProduct, clothoObject);
        assertEquals(idP2, testProduct.getId());
        if (idP.equals(testProduct.getId())) {
            fail();
        }

        testProduct.setName("Test Product");
        testProduct.setDescription("Testing Product Class");

        String idP3 = ClothoAdapter.createProduct(testProduct, clothoObject);
        assertEquals(idP3, testProduct.getId());
        if (idP.equals(testProduct.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testProduct;
        System.out.println("----------");

    }

    @Test
    public void testCreateProject() {
        System.out.println("-----CREATE PROJECT TEST-----");

        Project testProject = new Project();
        String idP = "";
        testProject.setId(idP);

        String idP2 = ClothoAdapter.createProject(testProject, clothoObject);
        assertEquals(idP2, testProject.getId());
        if (idP.equals(testProject.getId())) {
            fail();
        }

        testProject.setDescription("A different project");

        String idP3 = ClothoAdapter.createProject(testProject, clothoObject);
        assertEquals(idP3, testProject.getId());
        if (idP.equals(testProject.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testProject;
        System.out.println("----------");
    }

    @Test
    public void testCreatePublication() {
        System.out.println("-----CREATE PUBLICATION TEST-----");

        Publication testPublication = new Publication();
        String idP = "";
        testPublication.setId(idP);

        String idP2 = ClothoAdapter.createPublication(testPublication, clothoObject);
        assertEquals(idP2, testPublication.getId());
        if (idP.equals(testPublication.getId())) {
            fail();
        }

        Publication testPublication2 = new Publication();
        testPublication.setId(idP);

        String idP3 = ClothoAdapter.createPublication(testPublication, clothoObject);
        assertEquals(idP3, testPublication.getId());
        if (idP.equals(testPublication.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testPublication;
        System.out.println("----------");

    }

    @Test
    public void testCreateSample() {
        System.out.println("-----CREATE SAMPLE TEST-----");

        Sample testSample = new Sample();
        String idS = "";
        testSample.setId(idS);

        String idS2 = ClothoAdapter.createSample(testSample, clothoObject);
        assertEquals(idS2, testSample.getId());
        if (idS.equals(testSample.getId())) {
            fail();
        }

        testSample.setDescription("Clotho Adapter test of Sample");
        testSample.setName("Sample Test");

        String idS3 = ClothoAdapter.createSample(testSample, clothoObject);
        assertEquals(idS3, testSample.getId());
        if (idS.equals(testSample.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testSample;
        System.out.println("----------");

    }

    @Test
    public void testCreateStatus() {
        System.out.println("-----CREATE STATUS TEST-----");

        Status testStatus = new Status();
        String idS = "";
        testStatus.setId(idS);

        String idS2 = ClothoAdapter.createStatus(testStatus, clothoObject);
        assertEquals(idS2, testStatus.getId());
        if (idS.equals(testStatus.getId())) {
            fail();
        }

        testStatus.setText("Test Status");
        testStatus.setUserId((new Person()).getId());

        String idS3 = ClothoAdapter.createStatus(testStatus, clothoObject);
        assertEquals(idS3, testStatus.getId());
        if (idS.equals(testStatus.getId())) {
            fail();
        }
        
        this.currentWorkingObject = testStatus;
        System.out.println("----------");
    }

    //make and receive back exactly what you made
    @Test
    public void testGetVendor() {
        System.out.println("-----GET VENDOR TEST-----");

        //Company fields
        String contact = "Clotho Test Contact";
        String name = "Clotho Test Name";
        String description = "Clotho Test Description";
        String phone = "Clotho Test Phone";
        String url = "Clotho Test Url";
        //
        Vendor company1 = new Vendor();
        company1.setContact(contact);
        company1.setName(name);
        company1.setDescription(description);
        company1.setPhone(phone);
        company1.setUrl(url);

        String companyId = ClothoAdapter.createVendor(company1, clothoObject);
        if (company1.getId().equals("Not Set")) {
            fail();
        }

        Vendor company2 = ClothoAdapter.getVendor(companyId, clothoObject);

        assertEquals(company1.getContact(), company2.getContact());
        assertEquals(company1.getName(), company2.getName());
        assertEquals(company1.getDescription(), company2.getDescription());
        assertEquals(company1.getPhone(), company2.getPhone());
        assertEquals(company1.getUrl(), company2.getUrl());
        assertEquals(company1.getId(), company2.getId());
        
        this.currentWorkingObject = company1;
        System.out.println("----------");

    }

    @Test
    public void testGetCartItem() {
        System.out.println("-----GET CART ITEM TEST-----");
        CartItem cItem1 = new CartItem();
        //Cart Item Fields 
        //
        Product product = new Product();
        ClothoAdapter.createProduct(product, clothoObject);
        cItem1.setProductId(product.getId());
        Date dateCreated = new Date();
        cItem1.setDateCreated(dateCreated);

        Double discount = .5d;
        cItem1.setDiscount(discount);
        Integer quantity = 50;
        cItem1.setQuantity(quantity);

        //
        String cartItemId = ClothoAdapter.createCartItem(cItem1, clothoObject);
        if (cItem1.getId().equals("Not Set")) {
            fail();
        }

        CartItem cItem2 = ClothoAdapter.getCartItem(cartItemId, clothoObject);

        assertEquals(cItem1.getId(), cItem2.getId());
        assertEquals(cItem1.getDateCreated().toString(), cItem2.getDateCreated().toString());
        assertEquals(cItem1.getDiscount(), cItem2.getDiscount());
        assertEquals(cItem1.getQuantity(), cItem2.getQuantity());
        
        this.currentWorkingObject = cItem1;
        System.out.println("----------");
    }

    @Test
    public void testGetGrant() {
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
        Date startDate = new Date(787899600000L);
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
        if (grant1.getId().equals("Not Set")) {
            fail();
        }

        Grant grant2 = ClothoAdapter.getGrant(grantId, clothoObject);

        assertEquals(grant1.getName(), grant2.getName());
        assertEquals(grant1.getLeadPI(), grant2.getLeadPI());
        assertEquals(grant1.getCoPIs().size(), grant2.getCoPIs().size());
        for (int i = 0; i < grant1.getCoPIs().size(); i++) {
            assertEquals(grant1.getCoPIs().get(i), grant2.getCoPIs().get(i));

        }
        assertEquals(grant1.getProgramManagerId(), grant2.getProgramManagerId());
        assertEquals(grant1.getStartDate().toString(), grant2.getStartDate().toString());
        assertEquals(grant1.getEndDate().toString(), grant2.getEndDate().toString());
        assertEquals(grant1.getBudget(), grant2.getBudget());
        assertEquals(grant1.getAmountSpent(), grant2.getAmountSpent());
        assertEquals(grant1.getProjects().size(), grant2.getProjects().size());
        for (int i = 0; i < grant1.getProjects().size(); i++) {
            assertEquals(grant1.getProjects().get(i), grant2.getProjects().get(i));
        }
        assertEquals(grant1.getDescription(), grant2.getDescription());
        
        this.currentWorkingObject = grant1;
        System.out.println("----------");

    }

    @Test
    public void testGetInstitution() {
        System.out.println("-----GET INSTITUTION TEST-----");
        //INSTITUTION FIELDS
        String name = "Clotho Test Name";
        String description = "Clotho Test Description";
        String phone = "Clotho Test Phone";
        String url = "Clotho Test Url";
        //
        Institution institution1 = new Institution();
        institution1.setName(name);
        institution1.setDescription(description);
        institution1.setPhone(phone);
        institution1.setUrl(url);
        institution1.setType(Institution.InstitutionType.Corporation);

        String insitutionId = ClothoAdapter.createInstiution(institution1, clothoObject);
        if (institution1.getId().equals("Not Set")) {
            fail();
        }

        Institution institution2 = ClothoAdapter.getInstitution(insitutionId, clothoObject);

        assertEquals(institution1.getName(), institution2.getName());
        assertEquals(institution1.getDescription(), institution2.getDescription());
        assertEquals(institution1.getPhone(), institution2.getPhone());
        assertEquals(institution1.getUrl(), institution2.getUrl());
        assertEquals(institution1.getId(), institution2.getId());
        assertEquals(institution1.getType().toString(), institution2.getType().toString());
        
        this.currentWorkingObject = institution1;
        System.out.println("----------");

    }

    @Test
    public void testGetLab() {
        System.out.println("-----GET LAB TEST-----");
        //INSTITUTION FIELDS
        String name = "Clotho Test Name";
        String description = "Clotho Test Description";
        String phone = "Clotho Test Phone";
        String url = "Clotho Test Url";
        //
        Lab lab1 = new Lab();
        lab1.setName(name);
        lab1.setDescription(description);
        lab1.setPhone(phone);
        lab1.setUrl(url);

        String labId = ClothoAdapter.createLab(lab1, clothoObject);
        if (lab1.getId().equals("Not Set")) {
            fail();
        }

        Lab lab2 = ClothoAdapter.getLab(labId, clothoObject);

        assertEquals(lab1.getName(), lab2.getName());
        assertEquals(lab1.getDescription(), lab2.getDescription());
        assertEquals(lab1.getPhone(), lab2.getPhone());
        assertEquals(lab1.getUrl(), lab2.getUrl());
        assertEquals(lab1.getId(), lab2.getId());

        this.currentWorkingObject = lab1;
        System.out.println("----------");

    }

    @Test
    public void testGetOrder() {
        System.out.println("-----GET ORDER TEST-----");
        //TODO: ADD TESTS FOR MORE PROPERTIES OF ORDER

        //Order Fields
        String name = "Clotho Test Name";
        String description = "Clotho Test Description";
        Date dateCreated = new Date(787899600000L);
        Double budget = 100.0d;

        Integer maxOrderSize = 25;

        Person createdBy = new Person();
        Person receivedBy = new Person();
        Person receivedBy2 = new Person();
        clothoObject.logout();
        ClothoAdapter.createPerson(createdBy, clothoObject);
        ClothoAdapter.createPerson(receivedBy, clothoObject);
        ClothoAdapter.createPerson(receivedBy2, clothoObject);
        List<String> receivedByIds = new ArrayList<>();
        receivedByIds.add(receivedBy.getId());
        receivedByIds.add(receivedBy2.getId());
        clothoLogin(this.username, this.password);
        List<String> cartItems = new ArrayList<>();
        CartItem C1 = new CartItem();
        CartItem C2 = new CartItem();
        //CART ITEM PROPERTIES
        Product P1 = new Product();
        Product P2 = new Product();

        ClothoAdapter.createProduct(P1, clothoObject);
        ClothoAdapter.createProduct(P2, clothoObject);

        C1.setProductId(P1.getId());
        C1.setDiscount(1.0d);
        ClothoAdapter.createCartItem(C1, clothoObject);
        C2.setProductId(P2.getId());
        C2.setDiscount(1.0d);
        ClothoAdapter.createCartItem(C2, clothoObject);
        //
        cartItems.add(C1.getId());
        cartItems.add(C2.getId());

        //
        Lab affiliatedLab = new Lab();
        ClothoAdapter.createLab(affiliatedLab, clothoObject);

        Project proj = new Project();
        ClothoAdapter.createProject(proj, clothoObject);

        //
        Order order1 = new Order();
        order1.setName(name);
        order1.setDescription(description);
        order1.setDateCreated(dateCreated);
        order1.setCreatedById(createdBy.getId());
        order1.setProducts(cartItems);

        order1.setBudget(budget);
        order1.setMaxOrderSize(maxOrderSize);
        order1.setAffiliatedLabId(affiliatedLab.getId());
        order1.setReceivedByIds(receivedByIds);
        order1.setRelatedProjectId(proj.getId());

        String orderId = ClothoAdapter.createOrder(order1, clothoObject);
        if (order1.getId().equals("Not Set")) {
            fail();
        }

        Order order2 = ClothoAdapter.getOrder(orderId, clothoObject);

        assertEquals(order1.getId(), order2.getId());
        assertEquals(order1.getName(), order2.getName());
        assertEquals(order1.getDescription(), order2.getDescription());
        assertEquals(order1.getDateCreated().toString(), order2.getDateCreated().toString());
        assertEquals(order1.getCreatedById(), order2.getCreatedById());
        assertEquals(order1.getBudget(), order2.getBudget());
        assertEquals(order1.getMaxOrderSize(), order2.getMaxOrderSize());
        assertEquals(order1.getAffiliatedLabId(), order2.getAffiliatedLabId());
        assertEquals(order1.getReceivedByIds().size(), order2.getReceivedByIds().size());
        for (int i = 0; i < order1.getReceivedByIds().size(); i++) {
            assertEquals(order1.getReceivedByIds().get(i), order2.getReceivedByIds().get(i));
        }
        assertEquals(order1.getRelatedProjectId(), order2.getRelatedProjectId());
        for (int i = 0; i < order1.getProducts().size(); i++) {
            assertEquals(order1.getProducts().get(i), order2.getProducts().get(i));
        }
        
        this.currentWorkingObject = order1;
        System.out.println("----------");
    }

    @Test
    public void testGetPerson() {
        System.out.println("-----GET PERSON TEST-----");
        //PERSON FIELDS
        String salt = "CLOTHO TEST SALT";
        byte[] saltedEmailHash = new byte[1];
        String emailId = "CLOTHO TEST EMAIL";
        String firstName = "Doug";
        String lastName = "Densmore";
        String password = "CLOTHO TEST PASSWORD";
        String institution = "Boston University";
        String department = "Electrical and Computer Engineering";
        String title = "Associate Professor";
        String profileDescription = "This is the default description";
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
        Set<PersonRole> personRoles1 = new HashSet<>();
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
        List<String> submittedOrders = new ArrayList<>();
        Order O4 = new Order();
        Order O5 = new Order();
        Order O6 = new Order();
        ClothoAdapter.createOrder(O4, clothoObject);
        ClothoAdapter.createOrder(O5, clothoObject);
        ClothoAdapter.createOrder(O6, clothoObject);
        submittedOrders.add(O4.getId());
        submittedOrders.add(O5.getId());
        submittedOrders.add(O6.getId());
        List<String> approvedOrders = new ArrayList<>();
        Order O7 = new Order();
        Order O8 = new Order();
        ClothoAdapter.createOrder(O7, clothoObject);
        ClothoAdapter.createOrder(O8, clothoObject);
        approvedOrders.add(O7.getId());
        approvedOrders.add(O8.getId());

        //
        Person person1 = new Person();
        person1.setSalt(salt);
        person1.setProfileDescription(profileDescription);
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
        person1.setCreatedOrders(orders);
        person1.setSubmittedOrders(submittedOrders);
        person1.setApprovedOrders(approvedOrders);
        person1.setInstitution(institution);
        person1.setDepartment(department);
        person1.setTitle(title);

        String personId = ClothoAdapter.createPerson(person1, clothoObject);
        if (person1.getId().equals("Not Set")) {
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
        assertEquals(person1.getInstitution(), person2.getInstitution());
        assertEquals(person1.getProfileDescription(), person2.getProfileDescription());
        assertEquals(person1.getDepartment(), person2.getDepartment());
        assertEquals(person1.getTitle(), person2.getTitle());
        assertEquals(person1.getActivationString(), person2.getActivationString());
        assertEquals(person1.getColleagues().size(), person2.getColleagues().size());
        assertEquals(person1.getColleagues().size(), person2.getColleagues().size());
        for (int i = 0; i < person1.getColleagues().size(); i++) {
            assertEquals(person1.getColleagues().get(i), person2.getColleagues().get(i));
        }
        assertEquals(person1.getNotebooks().size(), person2.getNotebooks().size());
        for (int i = 0; i < person1.getNotebooks().size(); i++) {
            assertEquals(person1.getNotebooks().get(i), person2.getNotebooks().get(i));
        }
        assertEquals(person1.getStatuses().size(), person2.getStatuses().size());
        for (int i = 0; i < person1.getStatuses().size(); i++) {
            assertEquals(person1.getStatuses().get(i), person2.getStatuses().get(i));
        }
        assertEquals(person1.getLabs().size(), person2.getLabs().size());
        for (int i = 0; i < person1.getLabs().size(); i++) {
            assertEquals(person1.getLabs().get(i), person2.getLabs().get(i));
        }
        assertEquals(person1.getProjects().size(), person2.getProjects().size());
        for (int i = 0; i < person1.getProjects().size(); i++) {
            assertEquals(person1.getProjects().get(i), person2.getProjects().get(i));
        }
        assertEquals(person1.getPublications().size(), person2.getPublications().size());
        for (int i = 0; i < person1.getPublications().size(); i++) {
            assertEquals(person1.getPublications().get(i), person2.getPublications().get(i));
        }
        if (!person1.getRoles().equals(person2.getRoles())) {
            fail("Person Roles are not equal");
        }
        assertEquals(person1.getCreatedOrders().size(), person2.getCreatedOrders().size());
        for (int i = 0; i < person1.getCreatedOrders().size(); i++) {
            assertEquals(person1.getCreatedOrders().get(i), person2.getCreatedOrders().get(i));
        }
        assertEquals(person1.getSubmittedOrders().size(), person2.getSubmittedOrders().size());
        for (int i = 0; i < person1.getSubmittedOrders().size(); i++) {
            assertEquals(person1.getSubmittedOrders().get(i), person2.getSubmittedOrders().get(i));
        }
        assertEquals(person1.getApprovedOrders().size(), person2.getApprovedOrders().size());
        for (int i = 0; i < person1.getApprovedOrders().size(); i++) {
            assertEquals(person1.getApprovedOrders().get(i), person2.getApprovedOrders().get(i));
        }
        this.currentWorkingObject = person1;
        System.out.println("----------");
    }

    @Test
    public void testGetProduct() {
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
        if (product1.getId().equals("Not Set")) {
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

        this.currentWorkingObject = product1;
        System.out.println("----------");
    }

    @Test
    public void testGetProject() {
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
        if (project1.getId().equals("Not Set")) {
            fail("ID not set");
        }

        Project project2 = ClothoAdapter.getProject(projectId, clothoObject);

        assertEquals(project1.getId(), project2.getId());
        assertEquals(project1.getCreatorId(), project2.getCreatorId());
        assertEquals(project1.getLeadId(), project2.getLeadId());
        assertEquals(project1.getMembers().size(), project2.getMembers().size());
        for (int i = 0; i < project1.getMembers().size(); i++) {
            assertEquals(project1.getMembers().get(i), project2.getMembers().get(i));
        }
        assertEquals(project1.getNotebooks().size(), project2.getNotebooks().size());
        for (int i = 0; i < project1.getNotebooks().size(); i++) {
            assertEquals(project1.getNotebooks().get(i), project2.getNotebooks().get(i));
        }

        assertEquals(project1.getAffiliatedLabs().size(), project2.getAffiliatedLabs().size());
        for (int i = 0; i < project1.getAffiliatedLabs().size(); i++) {
            assertEquals(project1.getAffiliatedLabs().get(i), project2.getAffiliatedLabs().get(i));
        }

        assertEquals(project1.getName(), project2.getName());
        assertEquals(project1.getDateCreated().toString(), project2.getDateCreated().toString());

        assertEquals(project1.getUpdates().size(), project2.getUpdates().size());
        for (int i = 0; i < project1.getUpdates().size(); i++) {
            assertEquals(project1.getUpdates().get(i), project2.getUpdates().get(i));
        }

        assertEquals(project1.getBudget(), project2.getBudget());
        assertEquals(project1.getGrantId(), project2.getGrantId());

        assertEquals(project1.getDescription(), project2.getDescription());

        this.currentWorkingObject = project1;
        System.out.println("----------");
    }

    @Test
    public void testGetPublication() {
        System.out.println("-----GET PUBLICATION TEST-----");
        //PUBLICATION PROPERTIES
        //
        Publication publication1 = new Publication();

        String publicationId = ClothoAdapter.createPublication(publication1, clothoObject);
        if (publication1.getId().equals("Not Set")) {
            fail("ID not set");
        }

        Publication publication2 = ClothoAdapter.getPublication(publicationId, clothoObject);
        assertEquals(publication1.getId(), publication2.getId());
        
        this.currentWorkingObject = publication1;
        System.out.println("----------");
    }

    @Test
    public void testGetSample() {
        System.out.println("-----GET SAMPLE TEST-----");
        //SAMPLE PROPERTIES
        String name = "CLOTHO TEST NAME";
        String description = "CLOTHO TEST DESCRIPTION";
        //
        Sample sample1 = new Sample();
        sample1.setName(name);
        sample1.setDescription(description);

        String sampleId = ClothoAdapter.createSample(sample1, clothoObject);
        if (sample1.getId().equals("Not Set")) {
            fail("Id not set");
        }
        Sample sample2 = ClothoAdapter.getSample(sampleId, clothoObject);

        assertEquals(sample1.getId(), sample2.getId());
        assertEquals(sample1.getName(), sample2.getName());
        assertEquals(sample1.getDescription(), sample2.getDescription());
        
        this.currentWorkingObject = sample1;
        System.out.println("----------");
    }

    @Test
    public void testGetStatus() {
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
        if (status1.getId().equals("Not Set")) {
            Assert.fail("Id not set.");
        }

        Status status2 = ClothoAdapter.getStatus(statusId, clothoObject);

        assertEquals(status1.getId(), status2.getId());
        assertEquals(status1.getText(), status2.getText());
        assertEquals(status1.getUserId(), status2.getUserId());
        assertEquals(status1.getCreated().toString(), status2.getCreated().toString());
        
        this.currentWorkingObject = status1;
        System.out.println("----------");
    }

}
