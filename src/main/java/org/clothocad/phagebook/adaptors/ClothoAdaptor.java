/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.io.UnsupportedEncodingException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DateFormatter;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
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
import org.clothocad.model.Person;
import org.clothocad.model.Person.PersonRole;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Protocol;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Sample;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONObject;
import net.sf.json.JSONNull;
import org.json.JSONException;

/**
 * @author Johan Ospina
 */
public class ClothoAdaptor {
    // <editor-fold defaultstate="collapsed" desc="Create Methods">
     
    public static String createCompany(Company company, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Company.class.getCanonicalName());
        if (company.getContact() != null && company.getContact() != ""){
            map.put("contact", company.getContact());
        }
        if (company.getId() != null) {
            map.put("id", company.getId());
        }
        map.put("name", company.getName());
        if (company.getDescription() != null && company.getDescription() != ""){
            map.put("description", company.getDescription());
        }
        if (company.getPhone() != null && company.getContact() != ""){
            map.put("phone", company.getPhone());
        }
        if (company.getUrl() != null && company.getUrl() != ""){
            map.put("url", company.getUrl());
        }
        id = (String) clothoObject.set(map);
        company.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createContainer(Container container, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Container.class.getCanonicalName());
        if (container.getName() != null && container.getName() != ""){ 
            map.put("name", container.getName());
        }
        if (container.getId() != null) {
            map.put("id", container.getId());
        }
        
        if (container.getDescription() != null && container.getDescription() != ""){
            map.put("description", container.getDescription());
        }
        id = (String) clothoObject.set(map);
        container.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    public static String createEntry(Entry entry, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Entry.class.getCanonicalName());
        if (entry.getNotebook().getId() != null){
            map.put("notebook", entry.getNotebook().getId());
        }
        
        if (entry.getDateCreated() != null){
            map.put("dateCreated", entry.getDateCreated().toString());
        }
        
        if (entry.getLastModified() != null){
            map.put("lastModified", entry.getLastModified().toString());
        }
        
        if ( entry.getText() != null && entry.getText() != ""){  
            map.put("text", entry.getText());
        }
        
        if ( entry.getTitle() != null && entry.getTitle() != ""){  
            map.put("title", entry.getTitle());
        }
        
        
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
        map.put("schema", FundingAgency.class.getCanonicalName());
        if (fundingAgency.getId() != null){
            map.put("id", fundingAgency.getId());
        }
        if (fundingAgency.getName() != null && fundingAgency.getName() != "" ){
            map.put("name", fundingAgency.getName());
        }
        
        if (fundingAgency.getDescription() != null && fundingAgency.getDescription() != "" ){
            map.put("description", fundingAgency.getDescription());
        }
        
        if (fundingAgency.getPhone() != null && fundingAgency.getPhone() != "" ){
            map.put("phone", fundingAgency.getPhone());
        }
        
        if (fundingAgency.getUrl() != null && fundingAgency.getUrl() != "" ){
            map.put("url", fundingAgency.getUrl());
        }
        id = (String) clothoObject.set(map);
        fundingAgency.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createGood(Good good, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Good.class.getCanonicalName());
        if (good.getId() != null){
            map.put("id", good.getId());
        }
        if (good.getName() != null && good.getName() != "" ){
            map.put("name", good.getName());
        }
        if (good.getDescription() != null && good.getDescription() != "" ){
            map.put("description", good.getDescription());
        }
                
        id = (String) clothoObject.set(map);
        good.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createGrant(Grant grant, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Grant.class.getCanonicalName());
        
        if (grant.getName() != "" && grant.getName() != null)
        {
            map.put("name", grant.getName());
        }
        
        if (grant.getLeadPI() != null)
        {
            map.put("leadPI", grant.getLeadPI().getId());
        }
        
        if (grant.getCoPIs() != null)
        { 
            if (!grant.getCoPIs().isEmpty())
            {
                JSONArray coPIs = new JSONArray();

                for (Person coPI: grant.getCoPIs() )
                {

                    coPIs.add(coPI.getId());

                }

                map.put("coPIs", coPIs);
            }
        }
        
        if (grant.getProgramManager() != "" && grant.getProgramManager() != null)
        {
            map.put("programManager", grant.getProgramManager());
        }
        
        if (grant.getStartDate() != null) 
        {
            map.put("startDate", grant.getStartDate().toString());
        }
        
        if (grant.getEndDate() != null)
        {
            map.put("endDate", grant.getEndDate().toString());
        }
        
        if (grant.getBudget() != null)
        {
            map.put("budget", grant.getBudget());
        }
        
        if (grant.getAmountSpent() != null)
        {
            map.put("amountSpent", grant.getAmountSpent());
        }
        
        if (grant.getProjects() != null) 
        {
            if (!grant.getProjects().isEmpty())
            {
                JSONArray projects = new JSONArray();
                for (Project project : grant.getProjects())
                {
                    projects.add(project.getId());
                }
                map.put("projects" , projects);
            }
        }
        
        
        if (grant.getDescription() != null)
        {
            map.put("description", grant.getDescription());
        }
        if (grant.getId() != null)
        {
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
        if (institution.getId() != null)
        {
            map.put("id", institution.getId());
        }
        map.put("schema", Institution.class.getCanonicalName());
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
        map.put("schema", Instrument.class.getCanonicalName());
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
        map.put("schema", Inventory.class.getCanonicalName());
        JSONArray samples = new JSONArray();
        for (Sample sample : inventory.getSamples()){
            samples.add(sample.getId());
        }
        map.put("samples", samples);
        JSONArray instruments = new JSONArray(); 
        for (Instrument instrument : inventory.getInstruments()){
            instruments.add(instrument.getId());
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
        map.put("schema", Notebook.class.getCanonicalName());
        map.put("owner", notebook.getOwner().getId());
        JSONArray entries = new JSONArray();
        for (Entry entry : notebook.getEntries()){
           entries.add(entry.getId());
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
        map.put("schema", Order.class.getCanonicalName());
        if (order.getId() != null)
        {
            map.put("id", order.getId());
        }
        
        if (order.getProducts() != null)
        {
            JSONObject products = new JSONObject();
        
            Iterator it = order.getProducts().entrySet().iterator();
            while (it.hasNext()) 
            {
                Map.Entry pair = (Map.Entry)it.next();
                products.put(((Product)pair.getKey()).getId(), pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }

            map.put("products" , products);
        }
        if (order.getName() != null)
        {
            map.put("name", order.getName());
        }
        
        id = (String) clothoObject.set(map);
        order.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createOrganization(Organization organization, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Organization.class.getCanonicalName());
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
        map.put("schema", Person.class.getCanonicalName());
       
        if(person.getSalt() != null && person.getSalt() != "")
        {
            map.put("salt", person.getSalt());
        }
         
    
        if (person.getSaltedEmailHash() != null && person.getSaltedEmailHash() != "") 
        {
            map.put("saltedEmailHash", person.getSaltedEmailHash());
        }
        
        if (person.getProjects() != null)
        {
            if (!person.getProjects().isEmpty())
            {
                JSONArray projects = new JSONArray();
                for (Project project : person.getProjects()){
                    projects.add(project.getId());
                }
                map.put("project", projects);
            }
        }
       
        if (person.getStatuses() != null)
        {
            if (!person.getStatuses().isEmpty()){
                JSONArray statuses = new JSONArray();
                for (Status status : person.getStatuses()){
                    statuses.add(status.getId());
                }
                map.put("statuses", statuses);
            }
        }
        
        if (person.getStatuses() != null)
        {
            if (!person.getNotebooks().isEmpty()){
                JSONArray notebooks = new JSONArray();
                for (Notebook notebook : person.getNotebooks()){
                    notebooks.add(notebook.getId());
                }
                map.put("notebooks", notebooks);
            }
        }
        if (person.getLabs() != null)
        {
            if(!person.getLabs().isEmpty())
            {
                JSONArray labs = new JSONArray();
                JSONArray roles = new JSONArray();
                Map rolesMap = new HashMap();
                for (Institution institution : person.getLabs())
                {
                    labs.add(institution.getId());
                    //iterate through the roles in the Set
                    Iterator<PersonRole> it = person.getRole(institution).iterator();
                    roles = new JSONArray();
                    while(it.hasNext())
                    {
                        roles.add(it.next().toString());
                    }
                    rolesMap.put(institution.getId(), roles);

                }
                map.put("labs", labs);
                map.put("roles", rolesMap);
            }
        }
        if (person.getColleagues() != null)
        {
            if (!person.getColleagues().isEmpty())
            {
                JSONArray colleagues = new JSONArray();
                for (Person colleague : person.getColleagues())
                {
                    colleagues.add(colleague.getId());

                }
                map.put("colleagues", colleagues);
            }
        }
        if (person.getOrders() != null)
        {
            if (!person.getOrders().isEmpty())
            {
                JSONArray orders = new JSONArray();
                for (Order order : person.getOrders())
                {
                    orders.add(order.getId());
                }
                map.put("orders", orders);
            }
        }
        if(person.getPublications() != null)
        {
            if (!person.getPublications().isEmpty())
            {
                JSONArray publications = new JSONArray();
                for (Publication publication : person.getPublications())
                {
                    publications.add(publication.getId());
                }
                map.put("publications", publications);
            }
        }
        if (person.getFirstName() !=null)
            map.put("firstName", person.getFirstName());
        
        if(person.getLastName() != null)
            map.put("lastName", person.getLastName());
        
        if(person.getEmailId() != null)
            map.put("emailId", person.getEmailId());
        
        if(person.getPassword() != null)
            map.put("password", person.getPassword());
        
        map.put("activated", person.isActivated());
        
        
        map.put("activationString", person.getActivationString());
       
       
        String username = person.getEmailId()  ;
        String password = person.getPassword();
        
        Map createUserMap = new HashMap();
        createUserMap.put("username", username);
        createUserMap.put("password", password);
        
        Map createUserResult = new HashMap();
        createUserResult = (Map)(clothoObject.createUser(createUserMap));
        
        Map loginUserMap = new HashMap();
        loginUserMap.put("username", username);
        loginUserMap.put("credentials", password);
        
        Map loginResult = new HashMap();
        loginResult = (Map)(clothoObject.login(loginUserMap));
        System.out.println(loginResult.toString());
        map.put("id", loginResult.get("id"));
        
        id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        clothoObject.logout();
        return id;
        
    }
    
    public static String createProduct(Product product, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        
        map.put("schema", Product.class.getCanonicalName());
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
    public static String createProject(Project project, Clotho clothoObject){
        String id ="";
        Map map = new HashMap();
        map.put("schema", Project.class.getCanonicalName());

        if(project.getId() != null && project.getId() != "")
        {
            map.put("id",project.getId());
        }
        
        System.out.println("Step 1");
        if(project.getCreator() != null)
        {
            if (project.getCreator().getId() != null)
            {
                map.put("creator", project.getCreator().getId());
            }    
        }
        System.out.println("Step 2");
        if(project.getLead() != null)
        {
            if (project.getLead().getId() != null){
                map.put("lead", project.getLead().getId());
            }    
        }
        System.out.println("Step 3");
        if (project.getMembers() != null){
            if (!project.getMembers().isEmpty())
            {
                System.out.println("Step 3");
                JSONArray members = new JSONArray();

                for (Person member: project.getMembers() ){

                    members.add(member.getId());

                }
                map.put("members", members);
            }
        }
        if (project.getNotebooks() != null)
        {
            if (!project.getNotebooks().isEmpty()){
                JSONArray notebooks = new JSONArray();

                for (Notebook notebook: project.getNotebooks() ){

                    notebooks.add(notebook.getId());

                }
                map.put("notebooks", notebooks);
            }
        }
        
        if (project.getAffiliatedLabs() != null)
        {
            if (!project.getAffiliatedLabs().isEmpty())
            {
                JSONArray affiliatedLabs = new JSONArray();

                for (Organization affiliatedLab: project.getAffiliatedLabs() ){

                    affiliatedLabs.add(affiliatedLab.getId());

                }
                map.put("affiliatedLabs", affiliatedLabs);
            }
        }
        if (project.getUpdates() != null)
        {
            if (!project.getUpdates().isEmpty())
            {
                JSONArray updates = new JSONArray();

                for (Status update: project.getUpdates() )
                {

                    updates.add(update.getId());

                }
                map.put("updates", updates);
            }
        }
        
        if (project.getName() != null && project.getName() != "")
        {
            map.put("name", project.getName());
        }
        
        if (project.getDateCreated() != null)
        {
            map.put("dateCreated", project.getDateCreated().toString());
        }
        
       
        
        if (project.getBudget() != null)
        {
            map.put("budget", project.getBudget());
        }
        
        if (project.getGrant() != null)
        {
            map.put("grant", project.getGrant());
        }
        if (project.getDescription() != null && project.getDescription() != "")
        {
            map.put("description", project.getDescription());
        }

        id = (String) clothoObject.set(map) ;
        project.setId(id);
        makePublic(id, clothoObject);
        
        
        return id;
    }
    public static String createProtocol(Protocol protocol, Clotho clothoObject)
    {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Protocol.class.getCanonicalName());
        map.put("creator", protocol.getCreator().getId());
        map.put("protocolName", protocol.getProtocolName());
        
        JSONArray equipment = new JSONArray();
        for (Instrument instrument : protocol.getEquipment()){
            equipment.add(instrument.getId());
        }
        map.put("equipment", equipment);
        
        JSONArray samples = new JSONArray();
        for (Sample sample : protocol.getSamples()){
            samples.add(sample.getId());
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
        map.put("schema", Sample.class.getCanonicalName());
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
        map.put("schema", Status.class.getCanonicalName());
        if (status.getId() != null){
            map.put("id", status.getId());
        }
        map.put("text", status.getText());
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
        //id is in the parameter, not sent to map methods
        Company company = mapToCompany(companyMap, clothoObject);
        
        company.setId(id);
        
        return company;
    }
    public static Container getContainer(String id, Clotho clothoObject)
    {
        Map containerMap = new HashMap();
        containerMap = (Map) clothoObject.get(id);
        
        //container properties
        Container container = mapToContainer(containerMap, clothoObject);
        container.setId(id);
        
        
        return container;
    }
    public static Entry getEntry(String id, Clotho clothoObject)
    {
        Map entryMap = new HashMap();
        entryMap = (Map) clothoObject.get(id);
        
        //entry properties as strings
        Entry entry = mapToEntry(entryMap, clothoObject);
        entry.setId(id);
              
        return entry;
      
    }
    public static FundingAgency getFundingAgency(String id, Clotho clothoObject)
    {
        Map fundingAgencyMap = new HashMap();
        fundingAgencyMap = (Map) clothoObject.get(id);
        
        FundingAgency fundingAgency = mapToFundingAgency(fundingAgencyMap, clothoObject);
        fundingAgency.setId(id);
          
        return fundingAgency;
       
    }
    //good is abstract, can't be gotten
    public static Grant getGrant(String id, Clotho clothoObject)
    {
        Map grantMap = new HashMap();
        grantMap = (Map) clothoObject.get(id);
        
        Grant grant = mapToGrant(grantMap, clothoObject);
        grant.setId(id);
        
        return grant;

    }
    public static Institution getInstitution(String id, Clotho clothoObject)
    {
        Map institutionMap = new HashMap();
        institutionMap = (Map) clothoObject.get(id);
        
        //id provided
        Institution institution = mapToInstitution(institutionMap, clothoObject);
        institution.setId(id);
                        
        return institution;
    }
    public static Instrument getInstrument(String id, Clotho clothoObject)
    {
        
        Map instrumentMap = new HashMap();
        instrumentMap = (Map) clothoObject.get(id);
        
        Instrument instrument = mapToInstrument(instrumentMap, clothoObject);
        instrument.setId(id);
        
        
        return instrument;
        
    }
    public static Notebook getNotebook(String id, Clotho clothoObject)
    {
        Map notebookMap = new HashMap();
        notebookMap = (Map) clothoObject.get(id);
        Notebook notebook = mapToNotebook(notebookMap, clothoObject);
        notebook.setId(id);
        
        return notebook;
    }
    public static Order getOrder(String id, Clotho clothoObject)
    {
        Map orderMap = new HashMap();
        orderMap = (Map) clothoObject.get(id);
        
        Order order = mapToOrder(orderMap, clothoObject);
        order.setId(id);
        return order;
    }
    
    public static Person getPerson(String id, Clotho clothoObject)
    {
        
        Map personMap = new HashMap();
        personMap = (Map) clothoObject.get(id);
        
        System.out.println(personMap);
        Person person = mapToPerson(personMap, clothoObject);
        person.setId(id);
         
        return person;
    }
    public static Product getProduct(String id, Clotho clothoObject)
    {
        Map productMap = new HashMap();
        productMap = (Map) clothoObject.get(id);
        
        Product product = mapToProduct(productMap, clothoObject);
        product.setId(id);
        
        return product;
        
    }
    public static Project getProject(String id, Clotho clothoObject)
    {
        System.out.println("Enter get project");
        Map projectMap = new HashMap();
        projectMap = (Map) clothoObject.get(id);
        
        Project project = mapToProject(projectMap, clothoObject);
        project.setId(id);
        System.out.println("leave get project");
        
        return project;
           
    }
    public static Protocol getProtocol(String id, Clotho clothoObject)
    {
        
        Map protocolMap = new HashMap();
        protocolMap = (Map) clothoObject.get(id);
        
        Protocol protocol = mapToProtocol(protocolMap, clothoObject);
        protocol.setId(id);
        return protocol;
        
    }
    public static Publication getPublication(String id, Clotho clothoObject)
    {
        Map publicationMap = new HashMap();
        publicationMap = (Map) clothoObject.get(id);
        Publication publication = mapToPublication(publicationMap, clothoObject);
        publication.setId(id);
        return publication;
    }
    public static Sample getSample(String id, Clotho clothoObject)
    {
        Map sampleMap = new HashMap();
        sampleMap = (Map) clothoObject.get(id);
        
        //instrument properties
        Sample sample = mapToSample(sampleMap, clothoObject);
        sample.setId(id);
               
        return sample;
        
    }
    public static Status getStatus(String id, Clotho clothoObject)
    {
        Map statusMap = new HashMap();
        statusMap = (Map) clothoObject.get(id);
        Status status = mapToStatus(statusMap, clothoObject);
        status.setId(id);
        return status;
    }  
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Query Methods">
        public static List<Company> queryCompany(Map query , Clotho clothoObject)
    {
        //queries return a JSON array of maps
        query.put("schema", Company.class.getCanonicalName());
        List<Company> companies = new LinkedList<Company>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          companies.add(mapToCompany( (Map)queryResults.get(i), clothoObject));
        }
        
        return companies;
        
        
    }
    public static List<Container> queryContainer(Map query , Clotho clothoObject)
    {
        query.put("schema", Container.class.getCanonicalName());
        List<Container> containers = new LinkedList<Container>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          containers.add(mapToContainer( (Map)queryResults.get(i), clothoObject));
        }
        
        
        return containers;
        
    }
    public static List<Entry> queryEntry(Map query , Clotho clothoObject)
    {
        query.put("schema", Entry.class.getCanonicalName());
        List<Entry> entries = new LinkedList<Entry>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          entries.add(mapToEntry( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return entries;
      
    }
    public static List<FundingAgency> queryFundingAgency(Map query , Clotho clothoObject)
    {
        query.put("schema", FundingAgency.class.getCanonicalName());
        List<FundingAgency> fundingAgencies = new LinkedList<FundingAgency>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          fundingAgencies.add(mapToFundingAgency( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return fundingAgencies;
       
    }
    //good is abstract, can't be gotten
    public static List<Grant> queryGrant(Map query , Clotho clothoObject)
    {
        query.put("schema", Grant.class.getCanonicalName());
        List<Grant> grants = new LinkedList<Grant>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          grants.add(mapToGrant( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return grants;

    }
    public static List<Institution> queryInstitution(Map query , Clotho clothoObject)
    {
        query.put("schema", Institution.class.getCanonicalName());
        List<Institution> institutions = new LinkedList<Institution>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          institutions.add(mapToInstitution( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return institutions;
    }
    public static List<Instrument> queryInstrument(Map query , Clotho clothoObject)
    {
        
        query.put("schema", Instrument.class.getCanonicalName());
        List<Instrument> instruments = new LinkedList<Instrument>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          instruments.add(mapToInstrument( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return instruments;
        
    }
    public static List<Notebook> queryNotebook(Map query , Clotho clothoObject)
    {
        query.put("schema", Notebook.class.getCanonicalName());
        List<Notebook> notebooks = new LinkedList<Notebook>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          notebooks.add(mapToNotebook( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        
        return notebooks;
    }
    public static List<Order> queryOrder(Map query , Clotho clothoObject)
    {
        query.put("schema", Order.class.getCanonicalName());
        List<Order> orders = new LinkedList<Order>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          orders.add(mapToOrder( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return orders;
    }
    
    public static List<Person> queryPerson(Map query , Clotho clothoObject)
    {
        
        //query.put("schema", Person.class.getCanonicalName());
        //TODO CHANGE THIS IN THE FUTURE
        query.put("schema", Person.class.getCanonicalName());
        
        List<Person> people = new LinkedList<Person>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          people.add(mapToPerson( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        
        return people;
    }
    public static List<Product> queryProduct(Map query , Clotho clothoObject)
    {
        query.put("schema", Product.class.getCanonicalName());
        List<Product> products = new LinkedList<Product>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          products.add(mapToProduct( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return products;
        
    }
    public static List<Project> queryProject(Map query , Clotho clothoObject)
    {
        query.put("schema", Project.class.getCanonicalName());
        List<Project> projects = new LinkedList<Project>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          projects.add(mapToProject((Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return projects;
           
    }
    public static List<Protocol> queryProtocol(Map query , Clotho clothoObject)
    {
        query.put("schema", Protocol.class.getCanonicalName());
        List<Protocol> protocols = new LinkedList<Protocol>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          protocols.add(mapToProtocol( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        
        return protocols;
    }
    public static List<Publication> queryPublication(Map query , Clotho clothoObject)
    {
        query.put("schema", Publication.class.getCanonicalName());
        List<Publication> publications = new LinkedList<Publication>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          publications.add(mapToPublication( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return publications;
    }
    public static List<Sample> querySample(Map query , Clotho clothoObject)
    {
        query.put("schema", Sample.class.getCanonicalName());
        List<Sample> samples = new LinkedList<Sample>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          samples.add(mapToSample( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        
        return samples;
        
    }
    public static List<Status> queryStatus(Map query , Clotho clothoObject)
    {
        query.put("schema", Product.class.getCanonicalName());
        List<Status> statuses = new LinkedList<Status>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (int i = 0; i < queryResults.size(); i++){
          statuses.add(mapToStatus( (Map)queryResults.get(i), clothoObject));
        }
        
        
        
        return statuses;
    }  
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Map Methods">
    public static Company mapToCompany(Map map, Clotho clothoObject)
    {
        //id is in the parameter
        String contact = "";
        if (map.containsKey("contact"))
        {
             //contact = (String) map.get("contact");
        }
       
        String name = "";
        
        if (map.containsKey("name"))
        {
             name = (String) map.get("name");
        }
        
        String description = "";
        
        if (map.containsKey("name")){
             description = (String) map.get("description");;
        }
         
        String phone = "";
        
        if (map.containsKey("phone")){
             phone = (String) map.get("phone");;
        }
        
        String url = "";
        
        if (map.containsKey("url")){
             url = (String) map.get("url");;
        }
        
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        
        Company savedCompany = new Company(name);
        savedCompany.setContact(contact);
        savedCompany.setDescription(description);
        savedCompany.setPhone(phone);
        savedCompany.setUrl(url);
        savedCompany.setId(id);
        
               
        return savedCompany;
    }
    public static Container mapToContainer(Map map, Clotho clothoObject)
    {
        
        
        //container properties except the id property
        String name = (String) map.get("name");
        String description = (String) map.get("description");
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        Container container = new Container(name);
        container.setDescription(description);
        container.setId(id);
        
        
        
        return container;
    }
    public static Entry mapToEntry(Map map, Clotho clothoObject)
    {
       
        
        String notebookId = (String) map.get("notebook");
        Notebook notebook = getNotebook(notebookId, clothoObject);
        String dateCreatedText = (String) map.get("dateCreated");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        Date dateCreated = new Date();
        try {
            dateCreated = df.parse(dateCreatedText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String lastModifiedText = (String) map.get("lastModified");
        Date lastModified = new Date();
        try {
            lastModified = df.parse(lastModifiedText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String text = (String) map.get("text");
        String title = (String) map.get("title");
        Entry entry = new Entry(notebook, dateCreated, text, title);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
       
        entry.setLastModified(lastModified);
        entry.setId(id);
        
        return entry;
      
    }
    public static FundingAgency mapToFundingAgency(Map map, Clotho clothoObject)
    {
        String name = (String) map.get("name");
        String description = (String) map.get("description");
        String phone = (String) map.get("phone");
        String url = (String) map.get("url");
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        FundingAgency fundingAgency = new FundingAgency(name);
        fundingAgency.setPhone(phone);
        fundingAgency.setUrl(url);
        fundingAgency.setDescription(description);
        fundingAgency.setId(id);
        
    
        
        return fundingAgency;
       
    }
    //good is abstract, can't be gotten
    public static Grant mapToGrant(Map map, Clotho clothoObject)
    {
        String name = (String) map.get("name");
        String leadPIid = (String) map.get("leadPI");
        Person leadPI = getPerson(leadPIid, clothoObject);
        JSONArray coPIids = (JSONArray) map.get("coPIs");
        List<Person> coPIs = new LinkedList<Person>() ;
        for (int i = 0; i < coPIids.size(); i++){
            coPIs.add(getPerson(coPIids.getString(i) , clothoObject));
        }
        
        String programManager = (String) map.get("programManager");
        
        String startDateText = (String) map.get("startDate");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        Date startDate = new Date();
        try {
            startDate = df.parse(startDateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String endDateText = (String) map.get("endDate");
        Date endDate = new Date();
        try {
            endDate = df.parse(endDateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       
        double budget = (double) map.get("budget");
        double amountSpent = (double) map.get("amountSpent");
        
        JSONArray projectIds = (JSONArray) map.get("projects");
        List<Project> projects = new LinkedList<Project>();
        for (int i = 0; i < projectIds.size(); i++){
            projects.add(getProject(projectIds.getString(i), clothoObject));
        }
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        String description = (String) map.get("description");
        
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
        grant.setId(id);
        
        
        return grant;

    }
    public static Institution mapToInstitution(Map map, Clotho clothoObject)
    {
        //id provided
        String name = (String) map.get("name");
        String description = (String) map.get("description");
        String phone = (String) map.get("phone");
        String url = (String) map.get("url");
        
        Institution institution = new Institution(name);
        institution.setPhone(phone);
        institution.setUrl(url);
        institution.setDescription(description);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        institution.setId(id);
        
        
        
        
        return institution;
    }
    public static Instrument mapToInstrument(Map map, Clotho clothoObject)
    {
        
              
        //instrument properties
        String name = (String) map.get("name");
        String description = (String) map.get("description");
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        Instrument instrument = new Instrument(name);
        instrument.setDescription(description);
        instrument.setId(id);
        
               
        return instrument;
        
    }
    public static Notebook mapToNotebook(Map map, Clotho clothoObject)
    {
       
        
        String ownerId = (String) map.get("owner");
        Person owner = getPerson(ownerId, clothoObject);
        JSONArray entriesIds = (JSONArray) map.get("entries");
        List<Entry> entries = new LinkedList<Entry>() ;
        for (int i = 0; i < entriesIds.size(); i++){
            entries.add(getEntry(entriesIds.getString(i) , clothoObject));
        }
        
        String affiliatedProjectId = (String) map.get("affiliatedProject");
        Project affiliatedProject = getProject(affiliatedProjectId, clothoObject);
        
        String dateCreatedText = (String) map.get("dateCreated");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        Date dateCreated = new Date();
        try {
            dateCreated = df.parse(dateCreatedText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        Notebook notebook = new Notebook(owner, affiliatedProject, dateCreated);
        notebook.setEntries(entries);
        notebook.setId(id);
        
                
        return notebook;
    }
    public static Order mapToOrder(Map map, Clotho clothoObject)
    {
        String name = (String) map.get("name");
        
        JSONObject productIds = (JSONObject) map.get("products");
        Map<Product, Integer> products = new HashMap<Product, Integer>() ;
        
        Iterator<String> it = productIds.keys();
        while (it.hasNext()) 
            {
            String key = it.next();
            Product productOrder = ClothoAdaptor.getProduct(key, clothoObject);
            
            try 
            {
                int quantity = (int) productIds.get(key);
                
            } catch (JSONException e)
            {
                // Something went wrong!
                System.out.println("something went wrong in mapToOrder");
            }
        }
        for (int i = 0; i < productIds.length(); i++){
            products.add(getProduct(productIds.getString(i) , clothoObject));
        }
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        Order order = new Order(name);
        order.setProducts(products);
        order.setId(id);
        
        return order;
    }
    
    public static Person mapToPerson(Map map, Clotho clothoObject)
    {
        JSONArray projectIds = new JSONArray();
        List<Project> projects = new LinkedList<Project>() ;
        System.out.println(map.toString());
        if ( map.containsKey("projects")){
            
            projectIds = (JSONArray) map.get("projects");
            
            for (int i = 0; i < projectIds.size(); i++){
                projects.add(getProject(projectIds.getString(i) , clothoObject));
            }
        }
        JSONArray statusIds = new JSONArray();
        List<Status> statuses = new LinkedList<Status>() ;
        if ( map.containsKey("statuses")){
            statusIds = (JSONArray) map.get("statuses");
            
            for (int i = 0; i < statusIds.size(); i++){
                statuses.add(getStatus(statusIds.getString(i) , clothoObject));
            }
        }
        JSONArray notebookIds = new JSONArray();
        List<Notebook> notebooks = new LinkedList<Notebook>() ;
        
        if ( map.containsKey("notebooks")){
            notebookIds = (JSONArray) map.get("notebooks");
            
            for (int i = 0; i < notebookIds.size(); i++){
                notebooks.add(getNotebook(notebookIds.getString(i) , clothoObject));
            }
        }
        JSONArray labIds = new JSONArray();
        List<Institution> labs = new LinkedList<Institution>() ;
        if ( map.containsKey("labs")){
            labIds = (JSONArray) map.get("labs");
            
            for (int i = 0; i < labIds.size(); i++){
                labs.add(getInstitution(labIds.getString(i) , clothoObject));
            }
        }
        List<Person> colleagues = new LinkedList<Person>() ;
        JSONArray colleagueIds = new JSONArray();
        if ( map.containsKey("colleagues")){
            colleagueIds = (JSONArray) map.get("colleagues");
            
            for (int i = 0; i < colleagueIds.size(); i++){
                colleagues.add(getPerson(colleagueIds.getString(i) , clothoObject));
            }
        }
        JSONArray orderIds = new JSONArray();
        List<Order> orders = new LinkedList<Order>() ;
        if ( map.containsKey("orders")){
            orderIds = (JSONArray) map.get("orders");
            
            for (int i = 0; i < orderIds.size(); i++){
                orders.add(getOrder(orderIds.getString(i) , clothoObject));
            }
        }
        JSONArray publicationIds = new JSONArray();
        List<Publication> publications = new LinkedList<Publication>() ;
        if ( map.containsKey("publications")){
            publicationIds = (JSONArray) map.get("publications");
            
            for (int i = 0; i < publicationIds.size(); i++){
                publications.add(getPublication(publicationIds.getString(i) , clothoObject));
            }
        }
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
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
                
        person.setFirstName( map.containsKey("firstName") ? (String) map.get("firstName") : "");
        person.setLastName( map.containsKey("lastName") ? (String) map.get("lastName") : "");
        person.setEmailId( map.containsKey("emailId") ? (String) map.get("emailId") : "");
        person.setPassword( map.containsKey("password") ? (String) map.get("password"): "");
        person.setActivated( (boolean) map.get("activated") );
        person.setActivationString((String) map.get("activationString"));
        
        person.setSalt(map.containsKey("salt") ? (String) map.getOrDefault("salt", "") : "");
        person.setSaltedEmailHash(map.containsKey("saltedEmailHash") ? (String) map.getOrDefault("saltedEmailHash", "") : "");
        person.setId(id);
        
        
            
        Map rolesMap = new HashMap();
        if ( map.containsKey("roles")){
            rolesMap = (Map)map.get("roles");
            for(int i=0;i<labs.size();i++){
                JSONArray labroles = new JSONArray();
                labroles = (JSONArray)rolesMap.get(labs.get(i).getId());
                for(int j=0;j<labroles.size();j++ ){
                    person.addRole(labs.get(i), PersonRole.valueOf((String)labroles.get(j)));
                }

            }
        }
        
        return person;
    }
    public static Product mapToProduct(Map map, Clotho clothoObject)
    {
        String name = (String) map.get("name");
        String description = (String) map.get("description");
        String productURL = (String) map.get("productURL");
        String companyId = (String) map.get("company");
        Company company = null;
        if (companyId != null){
            company = getCompany(companyId, clothoObject);
        }
        GoodType goodType = GoodType.valueOf((String) map.get("goodType"));
        double cost = (Double) map.get("cost");
        int quantity = (int) map.get("quantity");
        
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        Product product = new Product(name, company, cost);
        product.setDescription(description);
        product.setProductURL(productURL);
        product.setQuantity(quantity);
        product.setId(id);
        
        
        return product;
        
    }
    public static Project mapToProject(Map map, Clotho clothoObject)
    {
       
        Person creator = new Person();
        if(map.containsKey("creator"))
        {
            String creatorId = (String) map.get("creator");
            creator = getPerson(creatorId, clothoObject);
        }
        
        
        Person lead = new Person();
        if(map.containsKey("lead")){
            String leadId = (String) map.get("lead");
            lead = getPerson(leadId, clothoObject);
        }
        
        List<Person> members = new LinkedList<Person>() ;
        
        JSONArray memberIds = new JSONArray();
        if (map.containsKey("members"))
        {
                memberIds = (JSONArray) map.get("members");
        }
        
        List<Notebook> notebooks = new LinkedList<Notebook>() ;
        
        List<Organization> affiliatedLabs = new LinkedList<Organization>() ;
        
        String name = "";
        if (map.containsKey("name"))
        {
            name = (String) map.get("name");
        }
        
       
        
        double budget = 0;
        if (map.containsKey("budget"))
        {
            budget = (double) map.get("budget");
        }
        
        String description = "";
        if (map.containsKey("description"))
        {
                description = (String) map.get("description");
        }
        
        Grant grant = new Grant();
        if (map.containsKey("grant"))
        {
            String grantId = (String) map.get("grant");
            grant = getGrant(grantId, clothoObject);
        }
        
        for (int i = 0; i < memberIds.size(); i++)
        {
            members.add(getPerson(memberIds.getString(i) , clothoObject));
        }
        
        JSONArray notebookIds = new JSONArray();
        if (map.containsKey("notebooks")){
            notebookIds = (JSONArray) map.get("notebooks");
        }
        
        
        for (int i = 0; i < notebookIds.size(); i++){
            notebooks.add(getNotebook(notebookIds.getString(i) , clothoObject));
        }
        
        
        
        JSONArray affiliatedLabIds = new JSONArray();
        if (map.containsKey("affiliatedLabs"))
        {
            affiliatedLabIds = (JSONArray) map.get("affiliatedLabs");
        }       
        for (int i = 0; i < affiliatedLabIds.size(); i++){
            affiliatedLabs.add(getInstitution(affiliatedLabIds.getString(i) , clothoObject));
        }
        
      
        
        
        String dateCreatedText = "";
        Date dateCreated = new Date();
       
        if (map.containsKey("dateCreated"))
        {
        
            dateCreatedText = (String) map.get("dateCreated");

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 

            try {
                dateCreated = df.parse(dateCreatedText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        
        List<Status> updates = new LinkedList<Status>() ;
        
        JSONArray updateIds = new JSONArray();
        
        if (map.containsKey("updates")){

            for (int i = 0; i < updateIds.size(); i++){
                updates.add(getStatus(updateIds.getString(i) , clothoObject));
            }
        }
        
        Project project = new Project(creator,name,description);
        
            
        project.setMembers(members);
        project.setUpdates(updates);
        project.setAffiliatedLabs(affiliatedLabs);
        project.setNotebooks(notebooks);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        return project;
     
    }
    public static Protocol mapToProtocol(Map map, Clotho clothoObject)
    {
        /**
         * creator       : Person
         * protocolName  : String
         * equipment     : List<Instrument>
         * samples       : List<Samples>
         * id            : String
         */
        
        String creatorId = (String) map.get("creatorId");
        Person creator = getPerson(creatorId, clothoObject);
        String protocolName = (String) map.get("protocolName");
        
        
        JSONArray equipmentIds = (JSONArray) map.get("equipment");
        List<Instrument> equipment = new LinkedList<Instrument>() ;
        for (int i = 0; i < equipmentIds.size(); i++){
            equipment.add(getInstrument(equipmentIds.getString(i) , clothoObject));
        }
        
        JSONArray sampleIds = (JSONArray) map.get("samples");
        List<Sample> samples = new LinkedList<Sample>() ;
        for (int i = 0; i < equipmentIds.size(); i++){
            samples.add(getSample(sampleIds.getString(i) , clothoObject));
        }
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        Protocol protocol = new Protocol();
        protocol.setCreator(creator);
        protocol.setProtocolName(protocolName);
        protocol.setSamples(samples);
        protocol.setEquipment(equipment);
        protocol.setId(id);
        
        
        return protocol;
        
    }
    public static Publication mapToPublication(Map map, Clotho clothoObject)
    {
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        Publication publication = new Publication();
        publication.setId(id);
        
        return publication;
    }
    public static Sample mapToSample(Map map, Clotho clothoObject)
    {
        //instrument properties
        String name = (String) map.get("name");
        String description = (String) map.get("description");
        
        Sample sample = new Sample(name);
        sample.setDescription(description);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        sample.setId(id);
        
        
        
        
        return sample;
        
    }
    public static Status mapToStatus(Map map, Clotho clothoObject)
    {
        //TODO: ACTUALLY WRITE STATUS.
        Status status = new Status("",null);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        status.setId(id);
        
        return status;
    }  
    // </editor-fold>
    //  <editor-fold defaultstate="collapsed" desc="Set Methods ">
    public static String setPerson(Person person, Clotho clothoObject){
        return ClothoAdaptor.createPerson(person, clothoObject);
    }
    
    
    
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
