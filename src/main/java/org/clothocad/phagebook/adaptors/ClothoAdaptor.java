/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;
import com.sun.istack.internal.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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

/**
 * @author Johan Ospina
 */
public class ClothoAdaptor {
    // <editor-fold defaultstate="collapsed" desc="Create Methods">
     
    
    //The way this works is that it creates a hashmap with ids for everything the object has pointers to 
    //or a primitive value.
    //schema is so that it can be queried by clotho.
    
    public static String createCompany(Company company, Clotho clothoObject)
    {
        
        Map map = new HashMap();
        map.put("schema", Company.class.getCanonicalName());
        if (company.getContact() != null && !company.getContact().isEmpty()){
            
            map.put("contact", company.getContact());
            
        }
        /* Don't need to put an ID if I am going to override it later...
        if (company.getId()   != null && !company.getId().isEmpty()   ){
            
            map.put("id", company.getId());
            
        }
        */
        if (company.getName()        != null && !company.getName().isEmpty()               ){
            map.put("name", company.getName());
        }
        if (company.getDescription() != null && !company.getDescription().isEmpty()        ){
            map.put("description", company.getDescription());
        }
        if (company.getPhone()       != null && !company.getContact().isEmpty()            ){
            map.put("phone", company.getPhone());
        }
        if (company.getUrl() != null && !company.getUrl().isEmpty()                        ){
            map.put("url", company.getUrl());
        }
        String id = (String) clothoObject.set(map);
        company.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createContainer(Container container, Clotho clothoObject)
    {
   
        Map map = new HashMap();
        map.put("schema", Container.class.getCanonicalName());
        if (container.getName() != null && !container.getName().isEmpty()){ 
            map.put("name", container.getName());
        }
        
        if (container.getDescription() != null && !container.getDescription().isEmpty()){
            map.put("description", container.getDescription());
        }
        String id = (String) clothoObject.set(map);
        container.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    public static String createEntry(Entry entry, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Entry.class.getCanonicalName());
        if (entry.getNotebook().getId() != null && !entry.getNotebook().getId().isEmpty()){
            map.put("notebook", entry.getNotebook().getId());
        }
        
        if (entry.getDateCreated() != null){
            map.put("dateCreated", entry.getDateCreated().toString());
        }
        
        if (entry.getLastModified() != null){
            map.put("lastModified", entry.getLastModified().toString());
        }
        
        if ( entry.getText() != null && !entry.getText().isEmpty()){  
            map.put("text", entry.getText());
        }
        
        if ( entry.getTitle() != null && !entry.getTitle().isEmpty()){  
            map.put("title", entry.getTitle());
        }
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        entry.setId(id);
        return id;
        
    }
    public static String createFundingAgency(FundingAgency fundingAgency, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", FundingAgency.class.getCanonicalName());
        if (fundingAgency.getName() != null && !fundingAgency.getName().isEmpty()){
            map.put("name", fundingAgency.getName());
        }
        
        if (fundingAgency.getDescription() != null && !fundingAgency.getDescription().isEmpty()){
            map.put("description", fundingAgency.getDescription());
        }
        
        if (fundingAgency.getPhone() != null && !fundingAgency.getPhone().isEmpty() ){
            map.put("phone", fundingAgency.getPhone());
        }
        
        if (fundingAgency.getUrl() != null && !fundingAgency.getUrl().isEmpty()){
            map.put("url", fundingAgency.getUrl());
        }
        String id = (String) clothoObject.set(map);
        fundingAgency.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createGood(Good good, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Good.class.getCanonicalName());
        if (good.getName() != null && !good.getName().isEmpty()    ){
            map.put("name", good.getName());
        }
        if (good.getDescription() != null && !good.getDescription().isEmpty()){
            map.put("description", good.getDescription());
        }
                
        String id = (String) clothoObject.set(map);
        good.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createGrant(Grant grant, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Grant.class.getCanonicalName());
        
        if ( !grant.getName().isEmpty() && grant.getName() != null)
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
                    if (coPI.getId() != null)
                    {
                        coPIs.add(coPI.getId());
                    }

                }

                map.put("coPIs", coPIs);
            }
        }
        
        if (!grant.getProgramManager().isEmpty() && grant.getProgramManager() != null)
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
                    if (project.getId() != null){
                        projects.add(project.getId());
                    }
                }
                map.put("projects" , projects);
            }
        }
        
        
        if (grant.getDescription() != null)
        {
            map.put("description", grant.getDescription());
        }
        
        String id = (String) clothoObject.set(map);
        grant.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    public static String createInstiution(Institution institution, Clotho clothoObject)
    {
        Map map = new HashMap();

        map.put("schema", Institution.class.getCanonicalName());
        
        if (!institution.getName().isEmpty() && institution.getName() != null){
              
            map.put("name", institution.getName());
        
        }
        if (!institution.getDescription().isEmpty() && institution.getDescription() != null){
            map.put("description", institution.getDescription());
        }
        
        if (!institution.getPhone().isEmpty() && institution.getPhone() != null){
            map.put("phone", institution.getPhone());
        }
        
        if (!institution.getUrl().isEmpty() && institution.getUrl() != null) {
            map.put("url", institution.getUrl());
        }
        
        
        String id = (String) clothoObject.set(map);
        institution.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createInstrument(Instrument instrument, Clotho clothoObject)
    {
  
        Map map = new HashMap();
        
        map.put("schema", Instrument.class.getCanonicalName());
        if (!instrument.getName().isEmpty() && instrument.getName() != null){
            map.put("name", instrument.getName());
        }
        
        if (!instrument.getDescription().isEmpty() && instrument.getDescription() != null) {
            map.put("description", instrument.getDescription());
        }
        
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        instrument.setId(id);
        return id;
    }
    public static String createInventory(Inventory inventory, Clotho clothoObject)
    {
       
        Map map = new HashMap();
        map.put("schema", Inventory.class.getCanonicalName());
    
        JSONArray samples = new JSONArray();
        if (!inventory.getSamples().isEmpty()){
            for (Sample sample : inventory.getSamples()){
                if (sample.getId() != null) {
                    samples.add(sample.getId());
                }
            }
           
        }
        map.put("samples", samples);
        JSONArray instruments = new JSONArray(); 
        if (!inventory.getInstruments().isEmpty()){
            for (Instrument instrument : inventory.getInstruments()){
                if (instrument.getId() != null){
                    instruments.add(instrument.getId());
                }
            }
        }
        
        map.put("instruments", instruments);
        
      
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        inventory.setId(id);
        
        return id;
    }
    public static String createNotebook(Notebook notebook, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Notebook.class.getCanonicalName());
        if (notebook.getOwner().getId() != null){
            map.put("owner", notebook.getOwner().getId());
        }
        
        JSONArray entries = new JSONArray();
        if (!notebook.getEntries().isEmpty()){
            for (Entry entry : notebook.getEntries()){
                if (entry.getId() != null) {
                    entries.add(entry.getId());
                }
            }
        }
        
        map.put("entries", entries);
         
        if (notebook.getAffiliatedProject().getId() != null){
            map.put("affiliatedProject", notebook.getAffiliatedProject().getId());
        }
        
        if (notebook.getDateCreated() != null){
            map.put("dateCreated", notebook.getDateCreated().toString());
        }
       
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        notebook.setId(id);
        return id;
    }
    public static String createOrder(Order order, Clotho clothoObject)
    {
 
        Map map = new HashMap();
        map.put("schema", Order.class.getCanonicalName());
     
        
        if (order.getProducts() != null)
        {
            JSONObject products = new JSONObject();
        
            Iterator it = order.getProducts().entrySet().iterator();
            while (it.hasNext()) 
            {
                Map.Entry pair = (Map.Entry)it.next();
                if (((Product) pair.getKey()).getId() != null){
                    products.put(((Product)pair.getKey()).getId(), pair.getValue());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

            map.put("products" , products);
        } 
        if (order.getName() != null)
        {
            map.put("name", order.getName());
            
        }
        
        String id = (String) clothoObject.set(map);
        order.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    public static String createOrganization(Organization organization, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Organization.class.getCanonicalName());
      
        if (organization.getName() != null) {
            map.put("name", organization.getName());
        }
        if (organization.getDescription() != null){
            map.put("description", organization.getDescription());
        }
        if (organization.getPhone() != null){
            map.put("phone", organization.getPhone());
        }
        if (organization.getUrl() != null) {
            map.put("url", organization.getUrl());
        }
        
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        organization.setId(id);
        return id;
    }
    public static String createPerson(Person person, Clotho clothoObject)
    {
       
        Map map = new HashMap();
        map.put("schema", Person.class.getCanonicalName());


        if(person.getSalt() != null && !person.getSalt().isEmpty()){
            map.put("salt", person.getSalt());
        }
         
    
        if (person.getSaltedEmailHash() != null) 
        {
            try {
                String hash = new String (person.getSaltedEmailHash(), "UTF-8");
                map.put("saltedEmailHash", hash);
            }
            catch (UnsupportedEncodingException ex){
                Logger.getLogger(ClothoAdaptor.class).log(Level.SEVERE, null, ex);
            }
        }
        
        if (person.getProjects() != null)
        {
            if (!person.getProjects().isEmpty())
            {
                JSONArray projects = new JSONArray();
                for (Project project : person.getProjects()){
                    if (project.getId() != null){
                        projects.add(project.getId());
                    }
                }
                map.put("project", projects);
            }
        }
       
        if (person.getStatuses() != null)
        {
            if (!person.getStatuses().isEmpty()){
                JSONArray statuses = new JSONArray();
                for (Status status : person.getStatuses()){
                    if (status.getId() != null){
                        statuses.add(status.getId());
                    }
                }
                map.put("statuses", statuses);
            }
        }
        
        if (person.getStatuses() != null)
        {
            if (!person.getNotebooks().isEmpty()){
                JSONArray notebooks = new JSONArray();
                for (Notebook notebook : person.getNotebooks()){
                    if (notebook.getId() != null){
                        notebooks.add(notebook.getId());
                    }
                }
                map.put("notebooks", notebooks);
            }
        }
        if (person.getLabs() != null)
        {
            if(!person.getLabs().isEmpty())
            {
                JSONArray labs = new JSONArray();
                JSONArray roles;
                Map rolesMap = new HashMap();
                for (Institution institution : person.getLabs())
                {
                    if (institution.getId() != null){
                        labs.add(institution.getId());
                    }
                    //iterate through the roles in the Set
                    if (person.getRole(institution) != null)
                    {
                        Iterator<PersonRole> it = person.getRole(institution).iterator();
                        roles = new JSONArray();
                        while(it.hasNext())
                        {
                            roles.add(it.next().toString());
                        }
                        if (institution.getId() != null){
                            rolesMap.put(institution.getId(), roles);
                        }
                    }

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
                    if (colleague.getId() != null){
                        colleagues.add(colleague.getId());
                    }

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
                    if (order.getId()!= null)
                    {
                        orders.add(order.getId());
                    }
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
                    if (publication.getId() != null){
                        publications.add(publication.getId());
                    }
                    
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
        
        //defaults are false and "" for these two lines in the constructors
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
        
        Map loginResult;
        loginResult = (Map)(clothoObject.login(loginUserMap));
        System.out.println(loginResult.toString());
        map.put("id", loginResult.get("id"));
        
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        clothoObject.logout();
        return id;
        
    }
    
    public static String createProduct(Product product, Clotho clothoObject)
    {
       
        Map map = new HashMap();
        map.put("schema", Product.class.getCanonicalName());
      
        map.put("cost",product.getCost());
       
        if (product.getProductURL() != null){
            map.put("productURL", product.getProductURL());
        }
        if (product.getGoodType() != null) {
            map.put("goodType", product.getGoodType().toString());
        }
        System.out.println(product.getGoodType().toString());
     
        map.put("quantity", product.getQuantity());
        
        if (product.getName() != null) {
            map.put("name", product.getName());
        }
        
        if (product.getDescription() != null) {
            map.put("description", product.getDescription());
        }
  
        if (product.getCompany().getId() != null){
            map.put("company", product.getCompany().getId());
        } else {
            map.put("company", product.getCompany().getName());
        }
        
        String id = (String) clothoObject.set(map) ;
        product.setId(id);
        makePublic(id, clothoObject);
        return id;
       
    }
    public static String createProject(Project project, Clotho clothoObject)
    {
 
        Map map = new HashMap();
        map.put("schema", Project.class.getCanonicalName());
        
       
        if(project.getCreator() != null)
        {
            if (project.getCreator().getId() != null)
            {
                map.put("creator", project.getCreator().getId());
            }    
        }
      
        if(project.getLead() != null)
        {
            if (project.getLead().getId() != null){
                map.put("lead", project.getLead().getId());
            }    
        }

        if (project.getMembers() != null){
            if (!project.getMembers().isEmpty())
            {
               
                JSONArray members = new JSONArray();

                for (Person member: project.getMembers() ){
                    if (member.getId() != null){
                        members.add(member.getId());
                    }

                }
                map.put("members", members);
            }
        }
        if (project.getNotebooks() != null)
        {
            if (!project.getNotebooks().isEmpty()){
                JSONArray notebooks = new JSONArray();

                for (Notebook notebook: project.getNotebooks() ){
                    if (notebook.getId() != null) {
                        notebooks.add(notebook.getId());
                    }

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
                    
                    if (affiliatedLab.getId() != null){
                        affiliatedLabs.add(affiliatedLab.getId());
                    }

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
                    if (update.getId() != null){
                       updates.add(update.getId());
                    }

                }
                map.put("updates", updates);
            }
        }
        
        if (project.getName() != null && !project.getName().isEmpty())
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
        if (project.getDescription() != null && !project.getDescription().isEmpty())
        {
            map.put("description", project.getDescription());
        }

        String id = (String) clothoObject.set(map) ;
        project.setId(id);
        makePublic(id, clothoObject);
        
        
        return id;
    }
    public static String createProtocol(Protocol protocol, Clotho clothoObject)
    {

        Map map = new HashMap();
        map.put("schema", Protocol.class.getCanonicalName());
        if (protocol.getCreator().getId() != null) {
            map.put("creator", protocol.getCreator().getId());
        }
        if (protocol.getProtocolName() != null && !protocol.getProtocolName().isEmpty()){
            map.put("protocolName", protocol.getProtocolName());
        }
        
        JSONArray equipment = new JSONArray();
        if (!protocol.getEquipment().isEmpty()){
        for (Instrument instrument : protocol.getEquipment()){
            if (instrument.getId() != null){
                equipment.add(instrument.getId());
            }
        }
            map.put("equipment", equipment);
        }
        
        JSONArray samples = new JSONArray();
        if (!protocol.getSamples().isEmpty()){
        for (Sample sample : protocol.getSamples())
            {
                if (sample.getId() != null) {
                    samples.add(sample.getId());
                }
            }
            map.put("samples", samples);
        }
        
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        protocol.setId(id);
        return id;
    }
    public static String createPublication(Publication publication, Clotho clothoObject)
    {
  
        Map map = new HashMap();
    
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        publication.setId(id);
        
        return id;
    }
    public static String createSample(Sample sample, Clotho clothoObject)
    {
     
        Map map = new HashMap();
        map.put("schema", Sample.class.getCanonicalName());
      
        if (sample.getName() != null && !sample.getName().isEmpty()){
            map.put("name", sample.getName());
        }
        if (sample.getDescription() != null && !sample.getDescription().isEmpty()){
            map.put("description", sample.getDescription());
        }
        
        String id = (String) clothoObject.set(map);
        sample.setId(id);
        makePublic(id, clothoObject);
        
        return id;
    }
    public static String createStatus(Status status, Clotho clothoObject)
    {
  
        Map map = new HashMap();
        map.put("schema", Status.class.getCanonicalName());
        if (status.getText() != null && !status.getText().isEmpty()){
            map.put("text", status.getText());
        }
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        status.setId(id);
        return id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Get Methods">
    public static Company getCompany(String id, Clotho clothoObject)
    {
        Map companyMap;
        companyMap = (Map) clothoObject.get(id);
        //id is in the parameter, not sent to map methods
        Company company = mapToCompany(companyMap, clothoObject);
        
        company.setId(id);
        
        return company;
    }
    public static Container getContainer(String id, Clotho clothoObject)
    {
        Map containerMap;
        containerMap = (Map) clothoObject.get(id);
        
        //container properties
        Container container = mapToContainer(containerMap, clothoObject);
        container.setId(id);
        
        
        return container;
    }
    public static Entry getEntry(String id, Clotho clothoObject)
    {
        Map entryMap;
        entryMap = (Map) clothoObject.get(id);
        
        //entry properties as strings
        Entry entry = mapToEntry(entryMap, clothoObject);
        entry.setId(id);
              
        return entry;
      
    }
    public static FundingAgency getFundingAgency(String id, Clotho clothoObject)
    {
        Map fundingAgencyMap;
        fundingAgencyMap = (Map) clothoObject.get(id);
        
        FundingAgency fundingAgency = mapToFundingAgency(fundingAgencyMap, clothoObject);
        fundingAgency.setId(id);
          
        return fundingAgency;
       
    }
    //good is abstract, can't be gotten
    public static Grant getGrant(String id, Clotho clothoObject)
    {
        Map grantMap;
        grantMap = (Map) clothoObject.get(id);
        
        Grant grant = mapToGrant(grantMap, clothoObject);
        grant.setId(id);
        
        return grant;

    }
    public static Institution getInstitution(String id, Clotho clothoObject)
    {
        Map institutionMap;
        institutionMap = (Map) clothoObject.get(id);
        
        //id provided
        Institution institution = mapToInstitution(institutionMap, clothoObject);
        institution.setId(id);
                        
        return institution;
    }
    public static Instrument getInstrument(String id, Clotho clothoObject)
    {
        
        Map instrumentMap;
        instrumentMap = (Map) clothoObject.get(id);
        
        Instrument instrument = mapToInstrument(instrumentMap, clothoObject);
        instrument.setId(id);
        
        
        return instrument;
        
    }
    public static Notebook getNotebook(String id, Clotho clothoObject)
    {
        Map notebookMap;
        notebookMap = (Map) clothoObject.get(id);
        Notebook notebook = mapToNotebook(notebookMap, clothoObject);
        notebook.setId(id);
        
        return notebook;
    }
    public static Order getOrder(String id, Clotho clothoObject)
    {
        Map orderMap;
        orderMap = (Map) clothoObject.get(id);
        
        Order order = mapToOrder(orderMap, clothoObject);
        order.setId(id);
        return order;
    }
    
    public static Person getPerson(String id, Clotho clothoObject)
    {
        
        Map personMap;
        personMap = (Map) clothoObject.get(id);
        
        System.out.println(personMap);
        Person person = mapToPerson(personMap, clothoObject);
        person.setId(id);
         
        return person;
    }
    public static Product getProduct(String id, Clotho clothoObject)
    {
        Map productMap;
        productMap = (Map) clothoObject.get(id);
        
        Product product = mapToProduct(productMap, clothoObject);
        product.setId(id);
        
        return product;
        
    }
    public static Project getProject(String id, Clotho clothoObject)
    {
        System.out.println("Enter get project");
        System.out.println(id);
        Map projectMap;
        projectMap = (Map) clothoObject.get(id);
        System.out.println("got id");
        Project project = mapToProject(projectMap, clothoObject);
        project.setId(id);
        System.out.println("leave get project");
  
        return project;
           
    }
    public static Protocol getProtocol(String id, Clotho clothoObject)
    {
        
        Map protocolMap;
        protocolMap = (Map) clothoObject.get(id);
        
        Protocol protocol = mapToProtocol(protocolMap, clothoObject);
        protocol.setId(id);
        return protocol;
        
    }
    public static Publication getPublication(String id, Clotho clothoObject)
    {
        Map publicationMap;
        publicationMap = (Map) clothoObject.get(id);
        Publication publication = mapToPublication(publicationMap, clothoObject);
        publication.setId(id);
        return publication;
    }
    public static Sample getSample(String id, Clotho clothoObject)
    {
        Map sampleMap;
        sampleMap = (Map) clothoObject.get(id);
        
        //instrument properties
        Sample sample = mapToSample(sampleMap, clothoObject);
        sample.setId(id);
               
        return sample;
        
    }
    public static Status getStatus(String id, Clotho clothoObject)
    {
        Map statusMap;
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
        List<Company> companies = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            companies.add(mapToCompany((Map) queryResult, clothoObject));
        }
        
        return companies;
        
        
    }
    public static List<Container> queryContainer(Map query , Clotho clothoObject)
    {
        query.put("schema", Container.class.getCanonicalName());
        List<Container> containers = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            containers.add(mapToContainer((Map) queryResult, clothoObject));
        }
        
        
        return containers;
        
    }
    public static List<Entry> queryEntry(Map query , Clotho clothoObject)
    {
        query.put("schema", Entry.class.getCanonicalName());
        List<Entry> entries = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            entries.add(mapToEntry((Map) queryResult, clothoObject));
        }
        
        
        
        return entries;
      
    }
    public static List<FundingAgency> queryFundingAgency(Map query , Clotho clothoObject)
    {
        query.put("schema", FundingAgency.class.getCanonicalName());
        List<FundingAgency> fundingAgencies = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            fundingAgencies.add(mapToFundingAgency((Map) queryResult, clothoObject));
        }
        
        
        
        return fundingAgencies;
       
    }
    //good is abstract, can't be gotten
    public static List<Grant> queryGrant(Map query , Clotho clothoObject)
    {
        query.put("schema", Grant.class.getCanonicalName());
        List<Grant> grants = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            grants.add(mapToGrant((Map) queryResult, clothoObject));
        }
        
        
        
        return grants;

    }
    public static List<Institution> queryInstitution(Map query , Clotho clothoObject)
    {
        query.put("schema", Institution.class.getCanonicalName());
        List<Institution> institutions = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            institutions.add(mapToInstitution((Map) queryResult, clothoObject));
        }
        
        
        
        return institutions;
    }
    public static List<Instrument> queryInstrument(Map query , Clotho clothoObject)
    {
        
        query.put("schema", Instrument.class.getCanonicalName());
        List<Instrument> instruments = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            instruments.add(mapToInstrument((Map) queryResult, clothoObject));
        }
        
        
        
        return instruments;
        
    }
    public static List<Notebook> queryNotebook(Map query , Clotho clothoObject)
    {
        query.put("schema", Notebook.class.getCanonicalName());
        List<Notebook> notebooks = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            notebooks.add(mapToNotebook((Map) queryResult, clothoObject));
        }
        
        
        
        
        return notebooks;
    }
    public static List<Order> queryOrder(Map query , Clotho clothoObject)
    {
        query.put("schema", Order.class.getCanonicalName());
        List<Order> orders = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            orders.add(mapToOrder((Map) queryResult, clothoObject));
        }
        
        
        
        return orders;
    }
    
    public static List<Person> queryPerson(Map query , Clotho clothoObject)
    {
        
        //query.put("schema", Person.class.getCanonicalName());
        //TODO CHANGE THIS IN THE FUTURE
        query.put("schema", Person.class.getCanonicalName());
        
        List<Person> people = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            people.add(mapToPerson((Map) queryResult, clothoObject));
        }
        
        
        
        
        return people;
    }
    public static List<Product> queryProduct(Map query , Clotho clothoObject)
    {
        query.put("schema", Product.class.getCanonicalName());
        List<Product> products = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            products.add(mapToProduct((Map) queryResult, clothoObject));
        }
        
        
        
        return products;
        
    }
    public static List<Project> queryProject(Map query , Clotho clothoObject)
    {
        query.put("schema", Project.class.getCanonicalName());
        List<Project> projects = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            projects.add(mapToProject((Map) queryResult, clothoObject));
        }
        
        
        
        return projects;
           
    }
    public static List<Protocol> queryProtocol(Map query , Clotho clothoObject)
    {
        query.put("schema", Protocol.class.getCanonicalName());
        List<Protocol> protocols = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            protocols.add(mapToProtocol((Map) queryResult, clothoObject));
        }
        
        
        
        
        return protocols;
    }
    public static List<Publication> queryPublication(Map query , Clotho clothoObject)
    {
        query.put("schema", Publication.class.getCanonicalName());
        List<Publication> publications = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            publications.add(mapToPublication((Map) queryResult, clothoObject));
        }
        
        
        
        return publications;
    }
    public static List<Sample> querySample(Map query , Clotho clothoObject)
    {
        query.put("schema", Sample.class.getCanonicalName());
        List<Sample> samples = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            samples.add(mapToSample((Map) queryResult, clothoObject));
        }
        
        
        
        
        return samples;
        
    }
    public static List<Status> queryStatus(Map query , Clotho clothoObject)
    {
        query.put("schema", Product.class.getCanonicalName());
        List<Status> statuses = new LinkedList<>();
        
        JSONArray queryResults = (JSONArray) clothoObject.query(query);
        
        for (Object queryResult : queryResults) {
            statuses.add(mapToStatus((Map) queryResult, clothoObject));
        }
        
        
        
        return statuses;
    }  
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Map Methods">
    public static Company mapToCompany(Map map, Clotho clothoObject)
    {
        
        
        String contact = "";

        if (map.containsKey("contact")){
             contact = (String) map.get("contact");
        }
       
        String name = "";
        
        if (map.containsKey("name"))
        {
             name = (String) map.get("name");
        }
        
        String description = "";
        
        if (map.containsKey("name")){
             description = (String) map.get("description");
        }
         
        String phone = "";
        
        if (map.containsKey("phone")){
             phone = (String) map.get("phone");
        }
        
        String url = "";
        
        if (map.containsKey("url")){
             url = (String) map.get("url");
        }
        
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
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
        String name = "";
        if (map.containsKey("name")){
            name = (String) map.get("name");
        }
        String description = "";
        if (map.containsKey("description")){
            description = (String) map.get("description");
        }
        
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
        Container container = new Container(name);
        container.setDescription(description);
        container.setId(id);
        
        
        
        return container;
    }
    public static Entry mapToEntry(Map map, Clotho clothoObject)
    {
       
        Notebook notebook = null;
        if (map.containsKey("notebook")){
        String notebookId = (String) map.get("notebook");
        
        notebook = getNotebook(notebookId, clothoObject);
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
        Date dateCreated = null;
        if (map.containsKey("dateCreated")){
            String dateCreatedText = (String) map.get("dateCreated");
            
            dateCreated = new Date();
            try {
                dateCreated = df.parse(dateCreatedText);
            } catch (ParseException e) {
            }
        }
        
        Date lastModified = null;
        if (map.containsKey("lastModified")){
            String lastModifiedText = (String) map.get("lastModified");
            lastModified = new Date();
            try {
                lastModified = df.parse(lastModifiedText);
            } catch (ParseException e) {
            }
        }
        String text = "";
        if (map.containsKey("text")){
            text = (String) map.get("text");
        }
        String title = "";
        if (map.containsKey("title")){
           title = (String) map.get("title");
        }
        
        Entry entry = new Entry(notebook, dateCreated, text, title);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
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
        
        Grant grant = new Grant("");
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
        

        Map productIds = (Map) map.get("products");
        Map<Product, Integer> products = new HashMap<>() ;
        
        Iterator it = productIds.entrySet().iterator();
        while (it.hasNext()) 
        {
            Map.Entry entryPair = (Map.Entry) it.next();
            System.out.println("HERE in CLOTHO APP 0 --" + entryPair.getKey());
            Product productOrder = ClothoAdaptor.getProduct((String) entryPair.getKey(), clothoObject);
            
            System.out.println("HERE in CLOTHO APP");
            try 
            {
                System.out.println(entryPair);
                System.out.println( entryPair.getValue().getClass());
                int quantity = (int) entryPair.getValue();
                System.out.println("HERE AT AFTER QUANTITY" + quantity);
                products.put(productOrder, quantity);
                
                
            } catch (Exception e)
            {
                // Something went wrong!
                System.out.println("something went wrong in mapToOrder");
                e.toString();
            }
            
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
        
        System.out.println("first set done");
                
        person.setFirstName( map.containsKey("firstName") ? (String) map.get("firstName") : "");
        person.setLastName( map.containsKey("lastName") ? (String) map.get("lastName") : "");
        
        System.out.println("has name");
        person.setEmailId( map.containsKey("emailId") ? (String) map.get("emailId") : "");
        person.setPassword( map.containsKey("password") ? (String) map.get("password"): "");
        System.out.println("has email and password");
        person.setActivated( (boolean) map.get("activated") );
        person.setActivationString((String) map.get("activationString"));
    
        
        person.setSalt(map.containsKey("salt") ? (String) map.get("salt") : "");
        person.setSaltedEmailHash(map.containsKey("saltedEmailHash") ? ((String) map.get("saltedEmailHash")).getBytes() : "".getBytes());

        
        
            
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
        System.out.println("reached end of person map ");
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
        GoodType goodType = GoodType.valueOf( (String) map.get("goodType"));
        double cost = (Double) map.get("cost");
        int quantity = (int) map.get("quantity");
        
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        
        Product product = new Product(name, company, cost);
        product.setDescription(description);
        product.setProductURL(productURL);
        product.setGoodType(goodType);
        product.setQuantity(quantity);
        product.setId(id);
        return product;
        
    }
    public static Project mapToProject(Map map, Clotho clothoObject)
    {
       System.out.println("Map :: " + map);
        
        Person creator = new Person();
        if(map.containsKey("creator"))
        {
            String creatorId = (String) map.get("creator");
            creator = getPerson(creatorId, clothoObject);
        }
        System.out.println("here2");
        
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

        Grant grant = new Grant("grant");
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

//            DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
//
//            try {
//                dateCreated = df.parse(dateCreatedText);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println("idk2");

        
        List<Status> updates = new LinkedList<Status>() ;
        
        JSONArray updateIds = new JSONArray();
        
        if (map.containsKey("updates")){

            for (int i = 0; i < updateIds.size(); i++){
                updates.add(getStatus(updateIds.getString(i) , clothoObject));
            }
        }
        Project project = new Project(creator,name,description);
        
            
//        project.setMembers(members);
//        project.setUpdates(updates);
//        project.setAffiliatedLabs(affiliatedLabs);
//        project.setNotebooks(notebooks);
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
        /*TODO: ACTUALLY WRITE STATUS.
        Status status = new Status("",null);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");;
        }
        status.setId(id);
        
        return status;
                */
      //Status result = Status(null);
      System.out.println("not complete");
      return null;
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
        8
        Map grantResult = new HashMap();
        grantResult = (Map)(clothoObject.grant(grantMap));
    }
    //  </editor-fold>
}
