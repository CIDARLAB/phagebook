/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.text.DateFormatter;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.phagebook.dom.Company;
import org.clothocad.phagebook.dom.Container;
import org.clothocad.phagebook.dom.Entry;
import org.clothocad.phagebook.dom.FundingAgency;
import org.clothocad.phagebook.dom.Good;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Instrument;
import org.clothocad.phagebook.dom.Inventory;
import org.clothocad.phagebook.dom.Notebook;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.dom.Person.PersonRole;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Protocol;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Sample;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONArray;

/**
 * @author Johan Ospina
 */
public class ClothoAdaptor {
    // <editor-fold defaultstate="collapsed" desc="Create Methods">
     
    public static String createCompany(Company company, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("contact", company.getContact());
        if (company.getId() != null) {
            map.put("id", company.getId());
        }
        map.put("name", company.getName());
        map.put("description", company.getDescription());
        map.put("phone", company.getPhone());
        map.put("url", company.getUrl());
        id = (String) clothoObject.set(map);
        company.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createContainer(Container container, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("name", container.getName());
        if (container.getId() != null) {
            map.put("id", container.getId());
        }
        map.put("description", container.getDescription());
        id = (String) clothoObject.set(map);
        container.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    public static String createEntry(Entry entry, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("notebook", entry.getNotebook().getId());
        map.put("dateCreated", entry.getDateCreated().toString());
        map.put("lastModified", entry.getLastModified().toString());
        map.put("text", entry.getText());
        map.put("title", entry.getTitle());
        if (entry.getId() != null){
            map.put("id", entry.getId());
        }
        
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        entry.setId(id);
        return id;
        
    }
    public static String createFundingAgency(FundingAgency fundingAgency, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (fundingAgency.getId() != null){
            map.put("id", fundingAgency.getId());
        }
        map.put("name", fundingAgency.getName());
        map.put("description", fundingAgency.getDescription());
        map.put("phone", fundingAgency.getPhone());
        map.put("url", fundingAgency.getUrl());
        id = (String) clothoObject.set(map);
        fundingAgency.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createGood(Good good, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (good.getId() != null){
            map.put("id", good.getId());
        }
        map.put("name", good.getName());
        map.put("description", good.getDescription());
                
        id = (String) clothoObject.set(map);
        good.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createGrant(Grant grant, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("name", grant.getName());
        map.put("leadPI", grant.getLeadPI().getId());
               
        JSONArray coPIs = new JSONArray();
        
        for (Person coPI: grant.getCoPIs() ){
            
            coPIs.put(coPI.getId());
            
        }
        map.put("coPIs", coPIs);
        map.put("programManager", grant.getProgramManager());
        map.put("startDate", grant.getStartDate().toString());
        map.put("endDate", grant.getEndDate().toString());
        map.put("budget", grant.getBudget());
        map.put("amountSpent", grant.getAmountSpent());
        
        JSONArray projects = new JSONArray();
        for (Project project : grant.getProjects()){
            projects.put(project.getId());
        }
        map.put("projects" , projects);
        map.put("description", grant.getDescription());
        if (grant.getId() != null){
        map.put("id", grant.getId());
        }
        
        
        id = (String) clothoObject.set(map);
        grant.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    public static String createInstiution(Institution institution, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (institution.getId() != null){
            map.put("id", institution.getId());
        }
        map.put("name", institution.getName());
        map.put("description", institution.getDescription());
        map.put("phone", institution.getPhone());
        map.put("url", institution.getUrl());
        
        id = (String) clothoObject.set(map);
        institution.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createInstrument(Instrument instrument, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (instrument.getId() != null){
            map.put("id", instrument.getId());
        }
        map.put("name", instrument.getName());
        map.put("description", instrument.getDescription());
        
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        instrument.setId(id);
        return id;
    }
    public static String createInventory(Inventory inventory, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        JSONArray samples = new JSONArray();
        for (Sample sample : inventory.getSamples()){
            samples.put(sample.getId());
        }
        map.put("samples", samples);
        JSONArray instruments = new JSONArray(); 
        for (Instrument instrument : inventory.getInstruments()){
            instruments.put(instrument.getId());
        }
        map.put("instruments", instruments);
        
        if (inventory.getId() != null){
            map.put("id", inventory.getId());
        }
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        inventory.setId(id);
        
        return id;
    }
    public static String createNotebook(Notebook notebook, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("owner", notebook.getOwner().getId());
        JSONArray entries = new JSONArray();
        for (Entry entry : notebook.getEntries()){
           entries.put(entry.getId());
        }
        map.put("entries", entries);
         
        map.put("affiliatedProject", notebook.getAffiliatedProject().getId());
        map.put("dateCreated", notebook.getDateCreated().toString());
        if (notebook.getId() != null){
            map.put("id", notebook.getId());
        }
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        notebook.setId(id);
        return id;
    }
    public static String createOrder(Order order, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (order.getId() != null){
            map.put("id", order.getId());
        }
        
        JSONArray products = new JSONArray();
        for (Product product : order.getProducts()){
           products.put(product.getId());
        }
        map.put("products" , products);
        map.put("name", order.getName());
        
        id = (String) clothoObject.set(map);
        order.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createOrganization(Organization organization, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (organization.getId() != null){
            map.put("id", organization.getId());
        }
        map.put("name", organization.getName());
        map.put("description", organization.getDescription());
        map.put("phone", organization.getPhone());
        map.put("url", organization.getUrl());
        
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        organization.setId(id);
        return id;
    }
    public static String createPerson(Person person, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("id", person.getId());
        
        JSONArray projects = new JSONArray();
        for (Project project : person.getProjects()){
            projects.put(project.getId());
        }
        map.put("project", projects);
        
        JSONArray statuses = new JSONArray();
        for (Status status : person.getStatuses()){
            statuses.put(status.getId());
        }
        map.put("statuses", statuses);
        
        
        JSONArray notebooks = new JSONArray();
        for (Notebook notebook : person.getNotebooks()){
            notebooks.put(notebook.getId());
        }
        map.put("notebooks", notebooks);
        
        
        JSONArray labs = new JSONArray();
        JSONArray roles = new JSONArray();
        Map rolesMap = new HashMap();
        for (Institution institution : person.getLabs()){
            labs.put(institution.getId());
            //iterate through the roles in the Set
            Iterator<PersonRole> it = person.getRole(institution).iterator();
            roles = new JSONArray();
            while(it.hasNext()){
                roles.put(it.next().toString());
            }
            rolesMap.put(institution.getId(), roles);
            
        }
        map.put("labs", labs);
        map.put("roles", rolesMap);
        
        JSONArray colleagues = new JSONArray();
        for (Person colleague : person.getColleagues()){
            colleagues.put(colleague.getId());
            
        }
        map.put("colleagues", colleagues);
        
        JSONArray orders = new JSONArray();
        for (Order order : person.getOrders()){
            orders.put(order.getId());
        }
        map.put("orders", orders);
        
        JSONArray publications = new JSONArray();
        for (Publication publication : person.getPublications()){
            publications.put(publication.getId());
        }
        map.put("publications", publications);
        
       
        
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        return id;
        
    }
    
    public static String createProduct(Product product, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("cost",product.getCost());
        map.put("productURL", product.getProductURL());
        map.put("goodType", product.getGoodType().toString());
        map.put("quantity", product.getQuantity());
        map.put("name", product.getName());
        map.put("description", product.getDescription());
        if (product.getId() != null )
        {
            map.put("id", product.getId());
        }
        if (product.getCompany().getId() != null){
        map.put("company", product.getCompany().getId());
        } else {
            map.put("company", product.getCompany().getName());
        }
        
        id = (String) clothoObject.set(map) ;
        product.setId(id);
        makePublic(id, clothoObject);
        return id;
       
    }
    public static String createProtocol(Protocol protocol, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("creator", protocol.getCreator().getId());
        map.put("protocolName", protocol.getProtocolName());
        
        JSONArray equipment = new JSONArray();
        for (Instrument instrument : protocol.getEquipment()){
            equipment.put(instrument.getId());
        }
        map.put("equipment", equipment);
        
        JSONArray samples = new JSONArray();
        for (Sample sample : protocol.getSamples()){
            samples.put(sample.getId());
        }
        map.put("samples", samples);
        
        if (protocol.getId() != null){
            map.put("id", protocol.getId());
        }
        
        
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        protocol.setId(id);
        return id;
    }
    public static String createPublication(Publication publication, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (publication.getId() != null){
            map.put("id", publication.getId());
        }
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        publication.setId(id);
        
        return id;
    }
    public static String createSample(Sample sample, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (sample.getId() != null){
            map.put("id", sample.getId());
        }
        map.put("name", sample.getName());
        map.put("description", sample.getDescription());
        
        id = (String) clothoObject.set(map);
        sample.setId(id);
        makePublic(id, clothoObject);
        
        return id;
    }
    public static String createStatus(Status status, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        if (status.getId() != null){
            map.put("id", status.getId());
        }
        
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        status.setId(id);
        return id;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Get Methods">
    public static Company getCompany(String id, Clotho clothoObject)
    {
        Map companyMap = new HashMap();
        companyMap = (Map) clothoObject.get(id);
        //id is in the parameter
        String contact = (String) companyMap.get("contact");
        String name = (String) companyMap.get("name");
        String description = (String) companyMap.get("description");
        String phone = (String) companyMap.get("phone");
        String url = (String) companyMap.get("url");
        
        Company savedCompany = new Company(name);
        savedCompany.setContact(contact);
        savedCompany.setDescription(description);
        savedCompany.setPhone(phone);
        savedCompany.setUrl(url);
        savedCompany.setId(id);
        
        return savedCompany;
    }
    public static Container getContainer(String id, Clotho clothoObject)
    {
        Map containerMap = new HashMap();
        containerMap = (Map) clothoObject.get(id);
        
        //container properties
        String name = (String) containerMap.get("name");
        String description = (String) containerMap.get("description");
        
        Container container = new Container(name);
        container.setDescription(description);
        container.setId(id);
        
        
        return container;
    }
    public static Entry getEntry(String id, Clotho clothoObject)
    {
        Map entryMap = new HashMap();
        entryMap = (Map) clothoObject.get(id);
        
        //entry properties as strings
        
        String notebookId = (String) entryMap.get("notebook");
        Notebook notebook = getNotebook(notebookId, clothoObject);
        String dateCreatedText = (String) entryMap.get("dateCreated");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        Date dateCreated = new Date();
        try {
            dateCreated = df.parse(dateCreatedText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String lastModifiedText = (String) entryMap.get("lastModified");
        Date lastModified = new Date();
        try {
            lastModified = df.parse(lastModifiedText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String text = (String) entryMap.get("text");
        String title = (String) entryMap.get("title");
        Entry entry = new Entry(notebook, dateCreated, text, title);
        entry.setId(id);
       
        entry.setLastModified(lastModified);
        
        return entry;
      
    }
    public static FundingAgency getFundingAgency(String id, Clotho clothoObject)
    {
        Map fundingAgencyMap = new HashMap();
        fundingAgencyMap = (Map) clothoObject.get(id);
        
        String name = (String) fundingAgencyMap.get("name");
        String description = (String) fundingAgencyMap.get("description");
        String phone = (String) fundingAgencyMap.get("phone");
        String url = (String) fundingAgencyMap.get("url");
        
        FundingAgency fundingAgency = new FundingAgency(name);
        fundingAgency.setId(id);
        fundingAgency.setPhone(phone);
        fundingAgency.setUrl(url);
        fundingAgency.setDescription(description);
    
        
        return fundingAgency;
       
    }
    //good is abstract, can't be gotten
    public static Grant getGrant(String id, Clotho clothoObject)
    {
        Map grantMap = new HashMap();
        grantMap = (Map) clothoObject.get(id);
        
        String name = (String) grantMap.get("name");
        String leadPIid = (String) grantMap.get("leadPI");
        Person leadPI = getPerson(id, clothoObject);
        JSONArray coPIids = (JSONArray) grantMap.get("coPIs");
        List<Person> coPIs = new LinkedList<Person>() ;
        for (int i = 0; i < coPIids.length(); i++){
            coPIs.add(getPerson(coPIids.getString(i) , clothoObject));
        }
        
        String programManager = (String) grantMap.get("programManager");
        
        String startDateText = (String) grantMap.get("startDate");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        Date startDate = new Date();
        try {
            startDate = df.parse(startDateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String endDateText = (String) grantMap.get("endDate");
        Date endDate = new Date();
        try {
            endDate = df.parse(endDateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       
        Double budget = Double.parseDouble((String) grantMap.get("budget"));
        Double amountSpent = Double.parseDouble((String) grantMap.get("amountSpent"));
        
        JSONArray projectIds = (JSONArray) grantMap.get("projects");
        List<Project> projects = new LinkedList<Project>();
        for (int i = 0; i < projectIds.length(); i++){
            projects.add(getProject(projectIds.getString(i), clothoObject));
        }
        
        String description = (String) grantMap.get("description");
        
        Grant grant = new Grant();
        grant.setName(name);
        grant.setLeadPI(leadPI);
        grant.setCoPIs(coPIs);
        grant.setProgramManager(programManager);
        grant.setStartDate(startDate);
        grant.setEndDate(endDate);
        grant.setBudget(budget);
        grant.setAmountSpent(amountSpent);
        grant.setProjects(projects);
        grant.setDescription(description);
        
        return grant;

    }
    public static Institution getInstitution(String id, Clotho clothoObject)
    {
        Map institutionMap = new HashMap();
        institutionMap = (Map) clothoObject.get(id);
        
        //id provided
        String name = (String) institutionMap.get("name");
        String description = (String) institutionMap.get("description");
        String phone = (String) institutionMap.get("phone");
        String url = (String) institutionMap.get("url");
        
        Institution institution = new Institution(name);
        institution.setId(id);
        institution.setPhone(phone);
        institution.setUrl(url);
        institution.setDescription(description);
        
        
        
        return institution;
    }
    public static Instrument getInstrument(String id, Clotho clothoObject)
    {
        
        Map instrumentMap = new HashMap();
        instrumentMap = (Map) clothoObject.get(id);
        
        //instrument properties
        String name = (String) instrumentMap.get("name");
        String description = (String) instrumentMap.get("description");
        
        Instrument instrument = new Instrument(name);
        instrument.setDescription(description);
        instrument.setId(id);
        
        
        return instrument;
        
    }
    public static Notebook getNotebook(String id, Clotho clothoObject)
    {
        Map notebookMap = new HashMap();
        notebookMap = (Map) clothoObject.get(id);
        
        String ownerId = (String) notebookMap.get("owner");
        Person owner = getPerson(ownerId, clothoObject);
        JSONArray entriesIds = (JSONArray) notebookMap.get("entries");
        List<Entry> entries = new LinkedList<Entry>() ;
        for (int i = 0; i < entriesIds.length(); i++){
            entries.add(getEntry(entriesIds.getString(i) , clothoObject));
        }
        
        String affiliatedProjectId = (String) notebookMap.get("affiliatedProject");
        Project affiliatedProject = getProject(affiliatedProjectId, clothoObject);
        
        String dateCreatedText = (String) notebookMap.get("dateCreated");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        Date dateCreated = new Date();
        try {
            dateCreated = df.parse(dateCreatedText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        Notebook notebook = new Notebook(owner, affiliatedProject, dateCreated);
        notebook.setEntries(entries);
        notebook.setId(id);
        
        return notebook;
    }
    public static Order getOrder(String id, Clotho clothoObject)
    {
        Map orderMap = new HashMap();
        orderMap = (Map) clothoObject.get(id);
        
        String name = (String) orderMap.get("name");
        
        JSONArray productIds = (JSONArray) orderMap.get("products");
        List<Product> products = new LinkedList<Product>() ;
        for (int i = 0; i < productIds.length(); i++){
            products.add(getProduct(productIds.getString(i) , clothoObject));
        }
        
        Order order = new Order(name);
        order.setId(id);
        order.setProducts(products);
        
        return order;
    }
    
    public static Person getPerson(String id, Clotho clothoObject)
    {
        
        Map personMap = new HashMap();
        personMap = (Map) clothoObject.get(id);
        
        
        JSONArray projectIds = (JSONArray) personMap.get("projects");
        List<Project> projects = new LinkedList<Project>() ;
        for (int i = 0; i < projectIds.length(); i++){
            projects.add(getProject(projectIds.getString(i) , clothoObject));
        }
        
        JSONArray statusIds = (JSONArray) personMap.get("statuses");
        List<Status> statuses = new LinkedList<Status>() ;
        for (int i = 0; i < statusIds.length(); i++){
            statuses.add(getStatus(statusIds.getString(i) , clothoObject));
        }
        
        JSONArray notebookIds = (JSONArray) personMap.get("notebooks");
        List<Notebook> notebooks = new LinkedList<Notebook>() ;
        for (int i = 0; i < notebookIds.length(); i++){
            notebooks.add(getNotebook(notebookIds.getString(i) , clothoObject));
        }
        
        JSONArray labIds = (JSONArray) personMap.get("labs");
        List<Institution> labs = new LinkedList<Institution>() ;
        for (int i = 0; i < labIds.length(); i++){
            labs.add(getInstitution(labIds.getString(i) , clothoObject));
        }
        
        JSONArray colleagueIds = (JSONArray) personMap.get("colleagues");
        List<Person> colleagues = new LinkedList<Person>() ;
        for (int i = 0; i < colleagueIds.length(); i++){
            colleagues.add(getPerson(colleagueIds.getString(i) , clothoObject));
        }
        
        JSONArray orderIds = (JSONArray) personMap.get("orders");
        List<Order> orders = new LinkedList<Order>() ;
        for (int i = 0; i < orderIds.length(); i++){
            orders.add(getOrder(orderIds.getString(i) , clothoObject));
        }
        
        JSONArray publicationIds = (JSONArray) personMap.get("publications");
        List<Publication> publications = new LinkedList<Publication>() ;
        for (int i = 0; i < publicationIds.length(); i++){
            publications.add(getPublication(publicationIds.getString(i) , clothoObject));
        }
        
        //JSONArray roles = (JSONArray) personMap.get("roles");
        //id and Roles are left
        
        /**
         * projects     : List<Project>
         * statuses     : List<Statuses>
         * notebooks    : List<Notebook>
         * labs         : List<Institution>
         * colleagues   : List<Person>
         * orders       : List<Order>
         * publications : List<Publication>
         * id           : String
         * roles        : Map<String, Set<PersonRole>>
         */
        Person person = new Person();
        person.setProjects(projects);
        person.setStatuses(statuses);
        person.setNotebooks(notebooks);
        person.setLabs(labs);
        person.setColleagues(colleagues);
        person.setOrders(orders);
        person.setPublications(publications);
        person.setId(id);
        
        Map rolesMap = new HashMap();
        rolesMap = (Map)personMap.get("roles");
        for(int i=0;i<labs.size();i++){
            JSONArray labroles = new JSONArray();
            labroles = (JSONArray)rolesMap.get(labs.get(i).getId());
            for(int j=0;j<labroles.length();j++ ){
                person.addRole(labs.get(i), PersonRole.valueOf((String)labroles.get(j)));
            }
           
        }
        
        return person;
    }
    public static Product getProduct(String id, Clotho clothoObject)
    {
        Map productMap = new HashMap();
        productMap = (Map) clothoObject.get(id);
        String name = (String) productMap.get("name");
        String description = (String) productMap.get("description");
        String productURL = (String) productMap.get("productURL");
        String companyId = (String) productMap.get("company");
        Company company = getCompany(companyId, clothoObject);
        GoodType goodType = GoodType.valueOf((String) productMap.get("goodType"));
        double cost = Double.parseDouble((String) productMap.get("cost"));
        int quantity = Integer.parseInt((String) productMap.get("quantity"));
        
        Product product = new Product(name, company, cost);
        product.setDescription(description);
        product.setProductURL(productURL);
        product.setQuantity(quantity);
        
        return product;
        
    }
    public static Project getProject(String id, Clotho clothoObject)
    {
        Map projectMap = new HashMap();
        projectMap = (Map) clothoObject.get(id);
        
        String creatorId = (String) projectMap.get("creator");
        
        Person creator = getPerson(creatorId, clothoObject);
        
        String leadId = (String) projectMap.get("lead");
        
        Person lead = getPerson(leadId, clothoObject);
        
        List<Person> members = new LinkedList<Person>() ;
        
        JSONArray memberIds = (JSONArray) projectMap.get("members");
        
        List<Notebook> notebooks = new LinkedList<Notebook>() ;
        
        List<Organization> affiliatedLabs = new LinkedList<Organization>() ;
        
        String name = (String) projectMap.get("name");
        
        Date dateCreated = new Date();
        
        List<Status> updates = new LinkedList<Status>() ;
        
        Double budget = Double.parseDouble((String) projectMap.get("budget"));
        
        String description = (String) projectMap.get("description");
        
        String grantId = (String) projectMap.get("grant");
        
        Grant grant = getGrant(grantId, clothoObject);
        
        
        for (int i = 0; i < memberIds.length(); i++){
            members.add(getPerson(memberIds.getString(i) , clothoObject));
        }
        
        JSONArray notebookIds = (JSONArray) projectMap.get("notebooks");
        
        for (int i = 0; i < notebookIds.length(); i++){
            notebooks.add(getNotebook(notebookIds.getString(i) , clothoObject));
        }
        
        JSONArray affiliatedLabIds = (JSONArray) projectMap.get("affiliatedLabs");
        
        for (int i = 0; i < affiliatedLabIds.length(); i++){
            affiliatedLabs.add(getInstitution(affiliatedLabIds.getString(i) , clothoObject));
        }
        
        
        
        String dateCreatedText = (String) projectMap.get("dateCreated");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        
        try {
            dateCreated = df.parse(dateCreatedText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        JSONArray updateIds = (JSONArray) projectMap.get("updates");
       
        for (int i = 0; i < updateIds.length(); i++){
            updates.add(getStatus(updateIds.getString(i) , clothoObject));
        }
        
        Project project = new Project(dateCreated, creator, name, null, 
        lead, budget, grant, description);
        
        project.setId(id);
        
        project.setMembers(members);
        project.setUpdates(updates);
        project.setAffiliatedLabs(affiliatedLabs);
        project.setNotebooks(notebooks);
        
        
        return project;
     
       
        
       
    }
    public static Protocol getProtocol(String id, Clotho clothoObject)
    {
        /**
         * creator       : Person
         * protocolName  : String
         * equipment     : List<Instrument>
         * samples       : List<Samples>
         * id            : String
         */
        Map protocolMap = new HashMap();
        protocolMap = (Map) clothoObject.get(id);
        
        String creatorId = (String) protocolMap.get("creatorId");
        Person creator = getPerson(creatorId, clothoObject);
        String protocolName = (String) protocolMap.get("protocolName");
        
        
        JSONArray equipmentIds = (JSONArray) protocolMap.get("equipment");
        List<Instrument> equipment = new LinkedList<Instrument>() ;
        for (int i = 0; i < equipmentIds.length(); i++){
            equipment.add(getInstrument(equipmentIds.getString(i) , clothoObject));
        }
        
        JSONArray sampleIds = (JSONArray) protocolMap.get("samples");
        List<Sample> samples = new LinkedList<Sample>() ;
        for (int i = 0; i < equipmentIds.length(); i++){
            samples.add(getSample(sampleIds.getString(i) , clothoObject));
        }
        
        Protocol protocol = new Protocol();
        protocol.setCreator(creator);
        protocol.setProtocolName(protocolName);
        protocol.setSamples(samples);
        protocol.setEquipment(equipment);
        
        return protocol;
        
    }
    public static Publication getPublication(String id, Clotho clothoObject)
    {
        Map publicationMap = new HashMap();
        publicationMap = (Map) clothoObject.get(id);
        Publication publication = new Publication();
        publication.setId(id);
        return publication;
    }
    public static Sample getSample(String id, Clotho clothoObject)
    {
        Map sampleMap = new HashMap();
        sampleMap = (Map) clothoObject.get(id);
        
        //instrument properties
        String name = (String) sampleMap.get("name");
        String description = (String) sampleMap.get("description");
        
        Sample sample = new Sample(name);
        sample.setDescription(description);
        sample.setId(id);
        
        
        return sample;
        
    }
    public static Status getStatus(String id, Clotho clothoObject)
    {
        Map statusMap = new HashMap();
        statusMap = (Map) clothoObject.get(id);
        Status status = new Status();
        status.setId(id);
        return status;
    }  
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Query Methods">
    
    // </editor-fold>
    //  <editor-fold defaultstate="collapsed" desc="Misc Methods">
    public static void makePublic(String objectId, Clotho clothoObject){
        List<String> add = new ArrayList<String>();
        List<String> remove = new ArrayList<String>();
        
        add.add("public");
        
        Map grantMap = new HashMap();
        grantMap.put("id", objectId);
        grantMap.put("user", "none");
        grantMap.put("add", add);
        grantMap.put("remove", remove);
        
        Map grantResult = new HashMap();
        grantResult = (Map)(clothoObject.grant(grantMap));
    }
    //  </editor-fold>
}
