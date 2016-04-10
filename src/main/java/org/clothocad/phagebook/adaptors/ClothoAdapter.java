/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;
import com.sun.media.jfxmedia.logging.Logger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.dom.Vendor;
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
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.OrderStatus;
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
public class ClothoAdapter {
    // <editor-fold defaultstate="collapsed" desc="Members">
    public static ClothoConnection conn;
    public static Clotho clothoObject;
    private static String username;
    private static String password;
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Create Methods">
     
    
    /**
     * This method is to create a CartItem Object in Clotho
     * @param CartItem cartItem object to create 
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createCartItem(CartItem cartItem, Clotho clothoObject) 
    {
        
        Map map = new HashMap();
        map.put("schema", CartItem.class.getCanonicalName());
        
        if (cartItem.getProductId()!= null){
            if (!cartItem.getProductId().equals("") && !cartItem.getProductId().equals("Not Set")){
                map.put("product", cartItem.getProductId());
            }
        }
        
       
        
        if (cartItem.getDateCreated() != null){
            map.put("dateCreated", cartItem.getDateCreated().toString());
        }
        
      
        if (cartItem.getDiscount()!= null){
            map.put("discount", cartItem.getDiscount().toString());
        }
        
        if (cartItem.getQuantity() != null){
            map.put("quantity", cartItem.getQuantity().toString());
        }
       
        String id = (String) clothoObject.set(map);
        cartItem.setId(id);
        makePublic(id, clothoObject);
        return id;
    }  
    /**
     * This method is to create a Vendor object in Clotho but It can also be used a SET method if the object that 
 gets passed in has a valid Clotho ID
     * @param container container object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createContainer(Container container, Clotho clothoObject)
    {
       
        Map map = new HashMap();
        map.put("schema", Container.class.getCanonicalName());
        if (container.getName() != null  ){ 
            if (!container.getName().isEmpty() && !container.getName().equals("Not Set"))
                map.put("name", container.getName());
        }
        
        if (container.getDescription() != null  ){
            if (!container.getDescription().isEmpty() &&!container.getDescription().equals("Not Set"))
                map.put("description", container.getDescription());
        }
        if (container.getId() != null){
            if (!container.getId().isEmpty() && !container.getId().equals("Not Set")){
                map.put("id", container.getId());
            }
        }
        
        String id = (String) clothoObject.set(map);
        container.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    /**
     * This method is to create an Entry object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param entry entry object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createEntry(Entry entry, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Entry.class.getCanonicalName());
        if (entry.getNotebookId() != null ){
            if (!entry.getNotebookId().equals("Not Set") && !entry.getNotebookId().isEmpty()){
                map.put("notebookId", entry.getNotebookId());
            }
        }
        
        if (entry.getDateCreated() != null){
            map.put("dateCreated", entry.getDateCreated().toString());
        }
        
        if (entry.getLastModified() != null){
            map.put("lastModified", entry.getLastModified().toString());
        }
        
        if ( entry.getText() != null  ){ 
            if (!entry.getText().isEmpty() && !entry.getText().equals("Not Set"))
                map.put("text", entry.getText());
        }
        
        if ( entry.getTitle() != null  ){  
            if (!entry.getTitle().isEmpty() &&!entry.getTitle().equals("Not Set"))
                map.put("title", entry.getTitle());
        }
        
        if (entry.getId() != null){
            if (!entry.getId().isEmpty() && !entry.getId().equals("Not Set")){
                map.put("id", entry.getId());
            }
        }
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        entry.setId(id);
        return id;
        
    }
    /**
     * This method is to create a fundingAgency object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param fundingAgency object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createFundingAgency(FundingAgency fundingAgency, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", FundingAgency.class.getCanonicalName());
        if (fundingAgency.getName() != null  ){
            if (!fundingAgency.getName().isEmpty() && !fundingAgency.getName().equals("Not Set"))
                map.put("name", fundingAgency.getName());
        }
        
        if (fundingAgency.getDescription() != null  ){
            if (!fundingAgency.getDescription().isEmpty() && !fundingAgency.getDescription().equals("Not Set"))
                map.put("description", fundingAgency.getDescription());
        }
        
        if (fundingAgency.getPhone() != null ) {
                if (!fundingAgency.getPhone().isEmpty() && !fundingAgency.getPhone().equals("Not Set"))
                    map.put("phone", fundingAgency.getPhone());
        }   
        
        if (fundingAgency.getUrl() != null ){
            if (!fundingAgency.getUrl().isEmpty() && !fundingAgency.getUrl().equals("Not Set")) {
                map.put("url", fundingAgency.getUrl());
            }
        }
        
        if (fundingAgency.getId() != null){
            if (!fundingAgency.getId().isEmpty() && !fundingAgency.getId().equals("Not Set")){
                map.put("id", fundingAgency.getId());
            }
        }
        String id = (String) clothoObject.set(map);
        fundingAgency.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
   
    /**
     * This method is to create a Good object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * NOTE: Good is a base class and this should only be used if for some reason we have not implemented the right create method.
     * it only cares about the Name, Description, and Clotho ID of whatever subclass object we pass in.
     * @param good object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createGood(Good good, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Good.class.getCanonicalName());
        if (good.getName() != null) {
            if(!good.getName().isEmpty() &&!good.getName().equals("Not Set" ))
                map.put("name", good.getName());
        }
        if (good.getDescription() != null ) {
            if (!good.getDescription().isEmpty() && !good.getDescription().equals("Not Set"))
                map.put("description", good.getDescription());
        }
          
        if (good.getId() != null){
            if (!good.getId().isEmpty() && !good.getId().equals("Not Set")){
                map.put("id", good.getId());
            }
        }
        String id = (String) clothoObject.set(map);
        good.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    /**
     * This method is to create a Grant object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param grant object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createGrant(Grant grant, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Grant.class.getCanonicalName());
        
        if ( grant.getName() != null)
        {
            if (!grant.getName().isEmpty() && !grant.getName().equals("Not Set"))
                map.put("name", grant.getName());
        }
        
        if (grant.getLeadPI() != null)
        {
            if (!grant.getLeadPI().equals("Not Set") && !grant.getLeadPI().isEmpty()){
                map.put("leadPI", grant.getLeadPI());
            }
        }
        
        if (grant.getCoPIs() != null)
        { 
            if (!grant.getCoPIs().isEmpty())
            {
                JSONArray coPIs = new JSONArray();

                for (String coPI: grant.getCoPIs() )
                {
                    if (coPI != null)
                    {
                        if (!coPI.equals("Not Set") && !coPI.isEmpty()){
                            coPIs.add(coPI);
                        }
                    }

                }

                map.put("coPIs", coPIs);
            }
        }
        
        if (grant.getProgramManagerId() != null )
        {
            if(!grant.getProgramManagerId().isEmpty() && !grant.getProgramManagerId().equals("Not Set") )
                map.put("programManagerId", grant.getProgramManagerId());
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
                for (String project : grant.getProjects())
                {
                    if (project != null){
                        if (!project.equals("Not Set") && !project.isEmpty()){
                            projects.add(project);
                        }
                    }
                }
                map.put("projects" , projects);
            }
        }
        
        
        if (grant.getDescription() != null)
        {
            if (!grant.getDescription().isEmpty() && !grant.getDescription().equals("Not Set")){
               map.put("description", grant.getDescription());
            }
        }
        
        if (grant.getId() != null){
            if (!grant.getId().isEmpty() && !grant.getId().equals("Not Set")){
                map.put("id", grant.getId());
            }
        }
        
        String id = (String) clothoObject.set(map);
        grant.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    /**
     * This method is to create an Institution object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param institution object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createInstiution(Institution institution, Clotho clothoObject)
    {
        Map map = new HashMap();

        map.put("schema", Institution.class.getCanonicalName());
        
        if (institution.getLabs()!= null)
        {
            if (!institution.getLabs().isEmpty()){
                JSONArray labs = new JSONArray();
                for (String lab : institution.getLabs()){
                    if (lab != null){
                        if (!lab.equals("Not Set") && !lab.isEmpty()){
                            labs.add(lab);
                        }
                    }
                }
                map.put("labs", labs);
            }
        }
        
        if ( institution.getName() != null){
              
            if (!institution.getName().isEmpty() && !institution.getName().equals("Not Set")){
                map.put("name", institution.getName());
            }
        
        }
        if (institution.getDescription() != null){
            if (!institution.getDescription().isEmpty() && !institution.getDescription().equals("Not Set")) {
            map.put("description", institution.getDescription());
            }
        }
        
        if (institution.getPhone() != null){
            if (!institution.getPhone().isEmpty() && !institution.getPhone().equals("Not Set")){
                map.put("phone", institution.getPhone());
            }
        }
        
        if (institution.getUrl() != null) {
            if (!institution.getUrl().isEmpty() && !institution.getUrl().equals("Not Set"))
            map.put("url", institution.getUrl());
        }
        
        if (institution.getId() != null){
            if (!institution.getId().isEmpty() && !institution.getId().equals("Not Set")){
                map.put("id", institution.getId());
            }
        }
        if (institution.getType() != null){
            map.put("type", institution.getType().toString());
        }
        
        String id = (String) clothoObject.set(map);
        institution.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    /**
     * This method is to create an Instrument object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param instrument object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createInstrument(Instrument instrument, Clotho clothoObject)
    {
  
        Map map = new HashMap();
        
        map.put("schema", Instrument.class.getCanonicalName());
        if (instrument.getName() != null){
            if (!instrument.getName().isEmpty() && !instrument.getName().equals("Not Set"))
                map.put("name", instrument.getName());
        }
        
        if (instrument.getDescription() != null) {
            if (!instrument.getDescription().isEmpty() && !instrument.getDescription().equals("Not Set"))
                map.put("description", instrument.getDescription());
        }
        
        if (instrument.getId() != null){
            if (!instrument.getId().isEmpty() && !instrument.getId().equals("Not Set")){
                map.put("id", instrument.getId());
            }
        }
        
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        instrument.setId(id);
        return id;
    }
    /**
     * This method is to create an Inventory object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param inventory object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createInventory(Inventory inventory, Clotho clothoObject)
    {
       
        Map map = new HashMap();
        map.put("schema", Inventory.class.getCanonicalName());
    
        JSONArray samples = new JSONArray();
        if (inventory.getSamples() != null) {
            if (!inventory.getSamples().isEmpty()){
                for (String sample : inventory.getSamples()){
                    if (sample != null) {
                        if (!sample.equals("Not Set") && !sample.isEmpty()){
                            samples.add(sample);
                        }
                    }
                }
                map.put("samples", samples);
            }
            
        }
        
        JSONArray instruments = new JSONArray(); 
        if (inventory.getInstruments() != null){
            if (!inventory.getInstruments().isEmpty()){
                for (String instrument : inventory.getInstruments()){
                    if (instrument != null){
                        if (!instrument.equals("Not Set") && !instrument.isEmpty()){
                            instruments.add(instrument);
                        }
                    }
                }
                map.put("instruments", instruments);
            }
        }
        
       
        if (inventory.getId() != null){
            if (!inventory.getId().isEmpty() && !inventory.getId().equals("Not Set")){
                map.put("id", inventory.getId());
            }
        }
      
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        inventory.setId(id);
        
        return id;
    }
    /**
     * This method is to create a Lab object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param lab object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createLab(Lab lab, Clotho clothoObject)
    {
        Map map = new HashMap();

        map.put("schema", Lab.class.getCanonicalName());
        
        if ( lab.getName() != null){
              
            if (!lab.getName().isEmpty() && !lab.getName().equals("Not Set")){
                map.put("name", lab.getName());
            }
        
        }
        if (lab.getDescription() != null){
            if (!lab.getDescription().isEmpty() && !lab.getDescription().equals("Not Set")) {
            map.put("description", lab.getDescription());
            }
        }
        
        if (lab.getPhone() != null){
            if (!lab.getPhone().isEmpty() && !lab.getPhone().equals("Not Set")){
                map.put("phone", lab.getPhone());
            }
        }
        
        if (lab.getUrl() != null) {
            if (!lab.getUrl().isEmpty() && !lab.getUrl().equals("Not Set"))
            map.put("url", lab.getUrl());
        }
        
        if (lab.getInstitution() != null) {
            if (!lab.getInstitution().isEmpty() && !lab.getInstitution().equals("Not Set"))
            map.put("institution", lab.getInstitution());
        }
        
        
        if (lab.getId() != null){
            if (!lab.getId().isEmpty() && !lab.getId().equals("Not Set")){
                map.put("id", lab.getId());
            }
        }
        
        if (lab.getLeadPIs() != null){
            
                JSONArray leadPIs = new JSONArray();
                for (String leadId : lab.getLeadPIs()){
                    if (leadId != null){
                        if (!leadId.equals("Not Set") && !leadId.isEmpty() ){
                           leadPIs.add(leadId);
                        }
                    }
                }
                map.put("leadPIs", leadPIs);
            
        }
        
        
        String id = (String) clothoObject.set(map);
        lab.setId(id);
        makePublic(id, clothoObject);
        return id;
        
    }
    /**
     * This method is to create a Notebook object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param notebook object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createNotebook(Notebook notebook, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Notebook.class.getCanonicalName());
        if (notebook.getOwnerId() != null){
                if (!notebook.getOwnerId().equals("Not Set") && !notebook.getOwnerId().isEmpty()){
                   map.put("owner", notebook.getOwnerId());
                }
        }
        
        JSONArray entries = new JSONArray();
        if (notebook.getEntries() != null){
            if (!notebook.getEntries().isEmpty()){
                for (String entry : notebook.getEntries()){
                    if (entry != null) {
                        if (!entry.equals("Not Set") && !entry.isEmpty()){
                            entries.add(entry);
                        }
                    }
                }
                map.put("entries", entries);
            }
        }
        
       
        if (notebook.getAffiliatedProjectId() != null) {
            if (notebook.getAffiliatedProjectId() != null){
                if (!notebook.getAffiliatedProjectId().equals("Not Set") && !notebook.getAffiliatedProjectId().isEmpty()){
                    map.put("affiliatedProject", notebook.getAffiliatedProjectId());
                }
            }
        }
        
        if (notebook.getDateCreated() != null){
            map.put("dateCreated", notebook.getDateCreated().toString());
        }
        if (notebook.getId() != null){
            if (!notebook.getId().isEmpty() && !notebook.getId().equals("Not Set")){
                map.put("id", notebook.getId());
            }
        }
       
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        notebook.setId(id);
        return id;
    }
    /**
     * This method is to create an Order object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param order object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createOrder(Order order, Clotho clothoObject)
    {
 
        Map map = new HashMap();
        map.put("schema", Order.class.getCanonicalName());
        
        if (order.getName() != null)
        {
            if (!order.getName().isEmpty() && !order.getName().equals("Not Set"))
                map.put("name", order.getName());
            
        }
        
        if (order.getDescription()!= null ){
            if (!order.getDescription().isEmpty() && !order.getDescription().equals("Not Set"))
                map.put("description", order.getDescription());
        }
        
        if (order.getDateCreated()!= null)
        {
            map.put("dateCreated", order.getDateCreated().toString());
        }
        
        if (order.getCreatedById()!= null){
            
                if (!order.getCreatedById().equals("Not Set") && !order.getCreatedById().isEmpty()){
                    map.put("createdById", order.getCreatedById());
                }
            
        }
        
        if (order.getProducts()!= null) //LIST OF CART ITEM ID's
        {
                JSONArray cartItemIds = new JSONArray();
                for (String cartItem : order.getProducts()){
                    if (cartItem != null){
                        if (!cartItem.equals("Not Set") && !cartItem.isEmpty()){
                            cartItemIds.add(cartItem);
                        }
                    }
                }
                System.out.println("CART ITEM ID's ARE : "+ cartItemIds.toString());
                map.put("products", cartItemIds);
            
        }
        
        
        if (order.getBudget() != null){
            map.put("budget", order.getBudget().toString());
        }
        
        if (order.getTaxRate()!= null){
            map.put("taxRate", order.getTaxRate().toString());
        }
        
        if (order.getMaxOrderSize() != null )
        {
            map.put("maxOrderSize", order.getMaxOrderSize().toString());
        }
        
        if (order.getApprovedById() != null)
        {
            if (!order.getApprovedById().equals("Not Set") && !order.getApprovedById().isEmpty())
            {
                map.put("approvedById", order.getApprovedById());
            }
        }
        if (order.getReceivedByIds()!= null)
        {
            if (!order.getReceivedByIds().isEmpty())
            {
                JSONArray receivedByIds = new JSONArray();
                for (String personId : order.getReceivedByIds()){
                    if (personId != null){
                        if (!personId.equals("Not Set") && !personId.isEmpty()){
                            receivedByIds.add(personId);
                        }
                    }
                }
                map.put("receivedByIds", receivedByIds);
            }
        }
        
        if (order.getStatus() != null)
        {
                map.put("status", order.getStatus().toString());
        }
        if (order.getId() != null){
            if (!order.getId().isEmpty() && !order.getId().equals("Not Set")){
                map.put("id", order.getId());
            }
        }
        String relatedProject = order.getRelatedProjectId();
        if (relatedProject != null){
            if (!relatedProject.isEmpty())
            {
                map.put("relatedProject", relatedProject);
            }
        }
        
        String affiliatedLab = order.getAffiliatedLabId();
        if (affiliatedLab != null){
            if (!affiliatedLab.isEmpty())
            {
                map.put("affiliatedLabId", affiliatedLab);
            }
        }
        
        String id = (String) clothoObject.set(map);
        order.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    /**
     * This method is to create an Organization object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param organization object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createOrganization(Organization organization, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Organization.class.getCanonicalName());
      
        if (organization.getName() != null) {
            if (!organization.getName().isEmpty() && !organization.getName().equals("Not Set"))
                map.put("name", organization.getName());
        }
        if (organization.getDescription() != null){
            if (!organization.getDescription().isEmpty() && !organization.getDescription().equals("Not Set"))
                map.put("description", organization.getDescription());
        }
        if (organization.getPhone() != null){
            if (!organization.getPhone().isEmpty() && !organization.getPhone().equals("Not Set"))
                map.put("phone", organization.getPhone());
        }
        if (organization.getUrl() != null) {
            if (!organization.getUrl().isEmpty() && !organization.getUrl().equals("Not Set"))
                map.put("url", organization.getUrl());
        }
        if (organization.getId() != null){
            if (!organization.getId().isEmpty() && !organization.getId().equals("Not Set")){
                map.put("id", organization.getId());
            }
        }
        
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        organization.setId(id);
        return id;
    }
    /**
     * This method is to create a Person object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * NOTE: You must be LOGGED OUT of Clotho when you call this Method because it logs in that person also you will end LOGGED OUT of Clotho once it returns. 
     * @param person object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createPerson(Person person, Clotho clothoObject)
    {
        
        Map map = new HashMap();
        map.put("schema", Person.class.getCanonicalName());


        if(person.getSalt() != null ){
            if (!person.getSalt().isEmpty() && !person.getSalt().equals("Not Set"))
                map.put("salt", person.getSalt());
        }
         
    
        if (person.getSaltedEmailHash() != null) 
        {
            map.put("saltedEmailHash", Arrays.toString(person.getSaltedEmailHash()) );
           
        }
        
        if (person.getProjects() != null)
        {
            if (!person.getProjects().isEmpty())
            {
                JSONArray projects = new JSONArray();
                for (String project : person.getProjects()){
                    if (project != null){
                        if (!project.equals("Not Set") && !project.isEmpty()){
                            projects.add(project);
                        }
                    }
                }
                map.put("projects", projects);
            }
        }
       
        if (person.getStatuses() != null)
        {
            if (!person.getStatuses().isEmpty()){
                JSONArray statuses = new JSONArray();
                for (String status : person.getStatuses()){
                    if (status != null){
                        if (!status.equals("Not Set") && !status.isEmpty() ){
                           statuses.add(status);
                        }
                    }
                }
                map.put("statuses", statuses);
            }
        }
        
        if (person.getNotebooks() != null)
        {
            if (!person.getNotebooks().isEmpty()){
                JSONArray notebooks = new JSONArray();
                for (String notebook : person.getNotebooks()){
                    if (notebook != null){
                        if (!notebook.equals("Not Set") && !notebook.isEmpty()){
                            notebooks.add(notebook);
                        }
                    }
                }
                map.put("notebooks", notebooks);
            }
        }
        if (person.getInstitutions()!= null)
        {
                JSONArray institutions = new JSONArray();
                for (String institutiton : person.getInstitutions()){
                    if (institutiton != null){
                        if (!institutiton.equals("Not Set") && !institutiton.isEmpty()){
                            institutions.add(institutiton);
                        }
                    }
                }
                map.put("institutions", institutions);
            System.out.println("INSTITS: " + institutions.toString());
        }
        
        if (person.getLabs() != null)
        {
            
                JSONArray labs = new JSONArray();
                JSONArray roles;
                Map rolesMap = new HashMap();
                for (String institution : person.getLabs())
                {
                    if (institution != null){
                        if (!institution.equals("Not Set") && !institution.isEmpty()){
                            labs.add(institution);
                        }
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
                        if (institution != null){
                            if (!institution.equals("Not Set") && !institution.isEmpty()){
                                rolesMap.put(institution, roles);
                            }
                        }
                    }

                }
                map.put("labs", labs);
                map.put("roles", rolesMap);
            
        }
        if (person.getColleagues() != null)
        {
            
                JSONArray colleagues = new JSONArray();
                for (String colleague : person.getColleagues())
                {
                    if (colleague != null){
                        if (!colleague.equals("Not Set") && !colleague.isEmpty()){
                            colleagues.add(colleague);
                        }
                    }

                }
                map.put("colleagues", colleagues);
            
        }
        
        if (person.getCreatedOrders() != null)
        {
            
                JSONArray orders = new JSONArray();
                for (String order : person.getCreatedOrders())
                {
                    if (order!= null)
                    {
                        if (!order.equals("Not Set") && !order.isEmpty()){
                            orders.add(order);
                        }
                    }
                }
                map.put("orders", orders);
            
        }
        if (person.getSubmittedOrders()!= null)
        {
            
                JSONArray orders = new JSONArray();
                for (String order : person.getSubmittedOrders())
                {
                    if (order!= null)
                    {
                        if (!order.equals("Not Set") && !order.isEmpty()){
                            orders.add(order);
                        }
                    }
                }
                map.put("submittedOrders", orders);
            
        }
        
        if (person.getApprovedOrders()!= null)
        {
           
                JSONArray orders = new JSONArray();
                for (String order : person.getApprovedOrders())
                {
                    if (order!= null)
                    {
                        if (!order.equals("Not Set") && !order.isEmpty()){
                            orders.add(order);
                        }
                    }
                }
                map.put("approvedOrders", orders);
            
        }
        
        
        if(person.getPublications() != null)
        {
            if (!person.getPublications().isEmpty())
            {
                JSONArray publications = new JSONArray();
                for (String publication : person.getPublications())
                {
                    if (publication != null){
                        if (!publication.equals("Not Set") && !publication.isEmpty()){
                            publications.add(publication);
                        }
                    }
                    
                }
                map.put("publications", publications);
            }
        }
        if (person.getFirstName() !=null)
            map.put("firstName", person.getFirstName());
        
        if(person.getLastName() != null)
            map.put("lastName", person.getLastName());
        
        if(person.getEmailId() != null){
            map.put("emailId", person.getEmailId());
            map.put("name", person.getEmailId());
        }
        if(person.getPassword() != null)
            map.put("password", person.getPassword());
        
        //defaults are false and "Not Set" for these two lines in the constructors
        //SHOULD BE
        map.put("activated", person.isActivated());
        map.put("activationString", person.getActivationString());
        
       
        String username = person.getEmailId()  ;
        String password = person.getPassword();
        
        Map createUserMap = new HashMap();
        createUserMap.put("username", username);
        createUserMap.put("password", password);
        
        clothoObject.createUser(createUserMap);
        
        Map loginUserMap = new HashMap();
        loginUserMap.put("username", username);
        loginUserMap.put("credentials", password);
        
        clothoObject.login(loginUserMap);
        
        if (person.getId() != null){
            if (!person.getId().equals("Not Set") && !person.getId().isEmpty()){
                map.put("id", person.getId());
            }
        }
        
        
       
        String id = (String) clothoObject.set(map);
        
        
        person.setId(id);
        makePublic(id, clothoObject);
        clothoObject.logout();
        return id;
        
    }
    /**
     * This method is to create a Product object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param product object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createProduct(Product product, Clotho clothoObject)
    {
       
        Map map = new HashMap();
        map.put("schema", Product.class.getCanonicalName());
      
       
        map.put("cost",product.getCost());
       
        if (product.getProductURL() != null){
            if (!product.getProductURL().isEmpty() && !product.getProductURL().equals("Not Set"))
                map.put("productURL", product.getProductURL());
        }
        if (product.getGoodType() != null) {
            map.put("goodType", product.getGoodType().toString());
        }
        
        if (product.getInventory() != null){
            map.put("inventory", product.getInventory());
        }
        
        if (product.getName() != null) {
            if (!product.getName().isEmpty() && !product.getName().equals("Not Set"))
                map.put("name", product.getName());
        }
        
        if (product.getDescription() != null) {
            if (!product.getDescription().isEmpty() && !product.getDescription().equals("Not Set"))
                map.put("description", product.getDescription());
        }
        if (product.getQuantity() != null) {
           
                map.put("quantity", product.getQuantity());
        }
  
        if (product.getCompanyId() != null){
            
                if (!product.getCompanyId().equals("Not Set") && !product.getCompanyId().isEmpty()){
                    map.put("company", product.getCompanyId());
                }
        }
        if (product.getUnitPrice() != null){
            map.put("unitPrice", product.getUnitPrice());
        }
        
        if (product.getId() != null){
            if (!product.getId().isEmpty() && !product.getId().equals("Not Set")){
                map.put("id", product.getId());
            }
        }
        
        String id = (String) clothoObject.set(map) ;
        product.setId(id);
        makePublic(id, clothoObject);
        return id;
       
    }
     /**
     * This method is to create a Project object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param project object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createProject(Project project, Clotho clothoObject)
    {
        Map map = new HashMap();
        map.put("schema", Project.class.getCanonicalName());
        
       
        if(project.getCreatorId() != null)
        {
           
                if (!project.getCreatorId().equals("Not Set") && !project.getId().isEmpty()){
                    map.put("creator", project.getCreatorId());
                }
               
        }
      
        if(project.getLeadId() != null)
        {
           
                if (!project.getLeadId().equals("Not Set") && !project.getLeadId().isEmpty()){
                    map.put("lead", project.getLeadId());
                }
           
        }

        if (project.getMembers() != null){
            if (!project.getMembers().isEmpty())
            {
               
                JSONArray members = new JSONArray();

                for (String member: project.getMembers() ){
                   
                        if (!member.equals("Not Set") && !member.isEmpty()){
                            members.add(member);
                        }
                    

                }
                map.put("members", members);
            }
        }
        if (project.getNotebooks() != null)
        {
            if (!project.getNotebooks().isEmpty()){
                JSONArray notebooks = new JSONArray();

                for (String notebook: project.getNotebooks() ){
                    if (notebook != null) {
                        if (!notebook.equals("Not Set") && !notebook.isEmpty()){
                            notebooks.add(notebook);
                        }
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

                for (String affiliatedLab: project.getAffiliatedLabs() ){
                    
                    if (affiliatedLab != null){
                        if (!affiliatedLab.equals("Not Set") && !affiliatedLab.isEmpty()){
                            affiliatedLabs.add(affiliatedLab);
                        }
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

                for (String update: project.getUpdates() )
                {
                    if (update != null){
                       if (!update.equals("Not Set") && !update.isEmpty()){
                           updates.add(update);
                       }
                    }

                }
                map.put("updates", updates);
            }
        }
        
        if (project.getName() != null )
        {
            if (!project.getName().isEmpty() && !project.getName().equals("Not Set"))
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
        
        if (project.getGrantId() != null)
        {
            if (project.getGrantId() != null){
                if (!project.getGrantId().equals("Not Set") && !project.getGrantId().isEmpty()){
                    map.put("grant", project.getGrantId());
                }
            }
        }
        if (project.getDescription() != null  )
        {
            if (!project.getDescription().isEmpty() && !project.getDescription().equals("Not Set"))
                map.put("description", project.getDescription());
        }
        
        if (project.getId() != null){
            if (!project.getId().isEmpty() && !project.getId().equals("Not Set")){
                map.put("id", project.getId());
            }
        }

        String id = (String) clothoObject.set(map) ;
        project.setId(id);
        makePublic(id, clothoObject);
        
        System.out.println("In CREate Project clotho id is");
        System.out.println(id);
        return id;
    }
     /**
     * This method is to create a Protocol object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param protocol object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createProtocol(Protocol protocol, Clotho clothoObject)
    {

        Map map = new HashMap();
        map.put("schema", Protocol.class.getCanonicalName());
        if (protocol.getCreatorId() != null){
            if (protocol.getCreatorId() != null) {
                if (!protocol.getCreatorId().equals("Not Set") && !protocol.getCreatorId().isEmpty()){
                    map.put("creator", protocol.getCreatorId());
                }
            }
        }
            
        if (protocol.getProtocolName() != null  ){
            if (!protocol.getProtocolName().isEmpty() && !protocol.getProtocolName().equals("Not Set"))
                map.put("protocolName", protocol.getProtocolName());
        }
        
        JSONArray equipment = new JSONArray();
        if (protocol.getEquipment() != null) {
            if (!protocol.getEquipment().isEmpty()){
                for (String instrument : protocol.getEquipment()){
                    if (instrument != null){
                        if (!instrument.equals("Not Set") && !instrument.isEmpty()){
                            equipment.add(instrument);
                        }
                    }
                }
                map.put("equipment", equipment);
            }
        }
        
        JSONArray samples = new JSONArray();
        if (protocol.getSamples() != null){
            if (!protocol.getSamples().isEmpty()){
            for (String sample : protocol.getSamples())
                {
                    if (sample != null) {
                        if (!sample.equals("Not Set") && !sample.isEmpty()){
                            samples.add(sample);
                        }
                    }
                }
                map.put("samples", samples);
            }
        }
        if (protocol.getId() != null){
            if (!protocol.getId().isEmpty() && !protocol.getId().equals("Not Set")){
                map.put("id", protocol.getId());
            }
        }
        
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        protocol.setId(id);
        return id;
    }
     /**
     * This method is to create a Publication object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param publication object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createPublication(Publication publication, Clotho clothoObject)
    {
  
        Map map = new HashMap();
        if (publication.getId() != null){
            if (!publication.getId().isEmpty() && !publication.getId().equals("Not Set")){
                map.put("id", publication.getId());
            }
        }
    
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        publication.setId(id);
        
        return id;
    }
     /**
     * This method is to create a Sample object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param sample object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createSample(Sample sample, Clotho clothoObject)
    {
     
        Map map = new HashMap();
        map.put("schema", Sample.class.getCanonicalName());
      
        if (sample.getName() != null  ){
            if (!sample.getName().isEmpty() && !sample.getName().equals("Not Set"))
                map.put("name", sample.getName());
        }
        if (sample.getDescription() != null  ){
            if (!sample.getDescription().isEmpty() && !sample.getDescription().equals("Not Set"))
                map.put("description", sample.getDescription());
        }
        
        if (sample.getId() != null){
            if (!sample.getId().isEmpty() && !sample.getId().equals("Not Set")){
                map.put("id", sample.getId());
            }
        }
        String id = (String) clothoObject.set(map);
        sample.setId(id);
        makePublic(id, clothoObject);
        
        return id;
    }
    /**
     * This method is to create a Status object in Clotho but It can also be used a SET method if the object that 
     * gets passed in has a valid Clotho ID
     * @param status object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createStatus(Status status, Clotho clothoObject)
    {
  
        Map map = new HashMap();
        map.put("schema", Status.class.getCanonicalName());
        if (status.getText() != null ){
            if (!status.getText().isEmpty() && !status.getText().equals("Not Set"))
                map.put("text", status.getText());
        }
        if (status.getUserId() != null){
            if (status.getUserId() != null){
                if (!status.getUserId().equals("Not Set") && !status.getUserId().isEmpty()){
                    map.put("user", status.getUserId());
                }
            }
        }
        
        if (status.getCreated() != null){
            map.put("created", status.getCreated().toString());
        }
        
        if (status.getId() != null){
            if (!status.getId().isEmpty() && !status.getId().equals("Not Set")){
                map.put("id", status.getId());
            }
        }
        String id = (String) clothoObject.set(map);
        makePublic(id, clothoObject);
        status.setId(id);
        return id;
    }
     /**
     * This method is to create a Vendor object in Clotho but It can also be used a SET method if the object that 
 gets passed in has a valid Clotho ID
     * @param company company object to create
     * @param clothoObject Instance of Clotho being used
     * @return ID value of the object in the Clotho Database
     */
    public static String createVendor(Vendor company, Clotho clothoObject)
    {
        
        Map map = new HashMap();
        map.put("schema", Vendor.class.getCanonicalName());
        
        if (company.getContact() != null ){  
            if (!company.getContact().equals("Not Set") && !company.getContact().isEmpty()) {
            map.put("contact", company.getContact());          
            }
        }
       
        if (company.getName() != null    ){
            if ( !company.getName().equals("Not Set") && !company.getName().isEmpty())
                map.put("name", company.getName());
        }
        if (company.getDescription() != null   ){
            if (!company.getDescription().isEmpty() && !company.getDescription().equals("Not Set"))
                map.put("description", company.getDescription());
        }
        if (company.getPhone() != null  ){
            if (!company.getPhone().isEmpty() && !company.getPhone().equals("Not Set"))
                map.put("phone", company.getPhone());
        }
        if (company.getUrl() != null ){
            if (!company.getUrl().isEmpty() && !company.getUrl().equals("Not Set") )
                map.put("url", company.getUrl());
        }
        if (company.getId() != null){
            if (!company.getId().isEmpty() && !company.getId().equals("Not Set")){
                map.put("id", company.getId());
            }
        }
        String id = (String) clothoObject.set(map);
        company.setId(id);
        makePublic(id, clothoObject);
        return id;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Get Methods">
    /**
     * This method is used to retrieve an instance of a Cart Item in clotho
     * make sure you are logged into Clotho before using it!
     * @param id id of the object in clotho
     * @param clothoObject
     * @return Cart Item object
     */
    public static CartItem        getCartItem(String id, Clotho clothoObject)
    {
        Map cartItemMap = (Map) clothoObject.get(id);
        CartItem cartItem = mapToCartItem(cartItemMap, clothoObject);
        return cartItem;
    }
    /**
     * This gets a Container object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Container       getContainer(String id, Clotho clothoObject)
    {
        Map containerMap = (Map) clothoObject.get(id);
        //container properties
        Container container = mapToContainer(containerMap, clothoObject);
        return container;
    } 
    /**
     * This gets a Entry object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Entry           getEntry(String id, Clotho clothoObject)
    {
        Map entryMap = (Map) clothoObject.get(id);
        //entry properties as strings
        Entry entry = mapToEntry(entryMap, clothoObject);  
        return entry;
      
    } 
    /**
     * This gets a FundingAgency object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static FundingAgency   getFundingAgency(String id, Clotho clothoObject)
    {
        Map fundingAgencyMap = (Map) clothoObject.get(id);
        FundingAgency fundingAgency = mapToFundingAgency(fundingAgencyMap, clothoObject);
        return fundingAgency;    
    } 
    //good is abstract, can't be gotten
    /**
     * This gets a Grant object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Grant           getGrant(String id, Clotho clothoObject)
    {
        Map grantMap = (Map) clothoObject.get(id);
        Grant grant = mapToGrant(grantMap, clothoObject);
        return grant;

    }
    /**
     * This gets an Institution object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Institution     getInstitution(String id, Clotho clothoObject)
    {
        Map institutionMap = (Map) clothoObject.get(id);
        Institution institution = new Institution();
        if (institutionMap != null)
        {       
            institution = mapToInstitution(institutionMap, clothoObject);
        
        }
        return institution;
    }
    /**
     * This gets an Instrument object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Instrument      getInstrument(String id, Clotho clothoObject)
    {
        Map instrumentMap = (Map) clothoObject.get(id);
        Instrument instrument = mapToInstrument(instrumentMap, clothoObject);
        return instrument;
        
    }
    /**
     * This gets an Inventory object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Inventory       getInventory (String id, Clotho clothoObject){
        Map inventoryMap = (Map) clothoObject.get(id);
        Inventory inventory = mapToInventory(inventoryMap, clothoObject);
        return inventory;
    }
    /**
     * This gets an Institution object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Lab     getLab(String id, Clotho clothoObject)
    {
        Map labMap = (Map) clothoObject.get(id);
        Lab lab = mapToLab(labMap, clothoObject);
        return lab;
    }
    /**
     * This gets a Notebook object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Notebook        getNotebook(String id, Clotho clothoObject)
    {
        Map notebookMap = (Map) clothoObject.get(id);
        Notebook notebook = mapToNotebook(notebookMap, clothoObject);
        return notebook;
    }
    /**
     * This gets an Order object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Order           getOrder(String id, Clotho clothoObject)
    {
        Map orderMap = (Map) clothoObject.get(id);
        Order order = mapToOrder(orderMap, clothoObject);
        return order;
    }
    /**
     * This gets a Person object from Clotho if it receives a valid ID, will give default values to properties that were not found. Ergo, if 
     * it doesn't exist check for an email ID of "". (e.g) emailId.isEmpty();
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Person          getPerson(String id, Clotho clothoObject)
    {
        Map personMap = (Map) clothoObject.get(id);
        Person person = mapToPerson(personMap, clothoObject);
        return person;
    }
    /**
     * This gets a Product object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Product         getProduct(String id, Clotho clothoObject)
    {
        Map productMap = (Map) clothoObject.get(id);
        Product product = new Product();
        if (productMap != null){
            product = mapToProduct(productMap, clothoObject);
        }
        return product;   
    }
    /**
     * This gets a Project object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Project         getProject(String id, Clotho clothoObject)
    {
        Map projectMap = (Map) clothoObject.get(id);
        Project project = mapToProject(projectMap, clothoObject);
        return project;     
    }
    /**
     * This gets a Protocol object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Protocol        getProtocol(String id, Clotho clothoObject)
    {
        Map protocolMap = (Map) clothoObject.get(id);
        Protocol protocol = mapToProtocol(protocolMap, clothoObject);
        return protocol;
    }
    /**
     * This gets a Publication object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Publication     getPublication(String id, Clotho clothoObject)
    {
        Map publicationMap = (Map) clothoObject.get(id);
        Publication publication = mapToPublication(publicationMap, clothoObject);
        return publication;
    }
    /**
     * This gets a Sample object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Sample          getSample(String id, Clotho clothoObject)
    {
        Map sampleMap = (Map) clothoObject.get(id);
        Sample sample = mapToSample(sampleMap, clothoObject);
        return sample;
        
    }
    /**
     * This gets a Status object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Status          getStatus(String id, Clotho clothoObject)
    {
        Map statusMap = (Map) clothoObject.get(id);
        Status status = mapToStatus(statusMap, clothoObject);
        return status;
    }  
    /**
     * This gets a Vendor object from Clotho if it receives a valid ID, will give default values to properties that were not found.
     * @param id Clotho ID
     * @param clothoObject Instance of Clotho being used
     * @return instance of object.
     */
    public static Vendor          getVendor(String id, Clotho clothoObject)
    {
        Map companyMap = (Map) clothoObject.get(id);
        Vendor company = new Vendor();
        if (companyMap != null){
            company = mapToVendor(companyMap, clothoObject);
        }
        return company;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Query Methods">

    /**
     * 
     *  
     * @param query         All possible query parameters (any property we want look at examples) you must populate this
     * @param clothoObject  must be logged in.
     * @param mode          Exact or Starts With... must pass in a valid query map object.
     * @return 
     */
    
    public static List<CartItem>      queryCartItem(Map query, Clotho clothoObject, QueryMode mode)
    {
        
        List<CartItem> cartItems = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                // map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(CartItem.class.getCanonicalName())){
                            cartItems.add(ClothoAdapter.getCartItem ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", CartItem.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    cartItems.add(mapToCartItem( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
       
        return cartItems;
        
    }  
    public static List<Container>     queryContainer(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Container> containers = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Container.class.getCanonicalName())){
                            containers.add(ClothoAdapter.getContainer ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Container.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    containers.add(mapToContainer( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        return containers;
        
    }
    public static List<Entry>         queryEntry(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Entry> entries = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Entry.class.getCanonicalName())){
                            entries.add(ClothoAdapter.getEntry ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Entry.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    entries.add(mapToEntry( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        
        
        
        return entries;
      
    }
    public static List<FundingAgency> queryFundingAgency(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<FundingAgency> fundingAgencies = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(FundingAgency.class.getCanonicalName())){
                            fundingAgencies.add(ClothoAdapter.getFundingAgency ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", FundingAgency.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    fundingAgencies.add(mapToFundingAgency( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        return fundingAgencies;
       
    }
    //good is abstract, can't be gotten
    public static List<Grant>         queryGrant(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Grant> grants = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Grant.class.getCanonicalName())){
                            grants.add(ClothoAdapter.getGrant ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Grant.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    grants.add(mapToGrant( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        return grants;

    }
    public static List<Institution>   queryInstitution(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Institution> institutions = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Institution.class.getCanonicalName())){
                            institutions.add(ClothoAdapter.getInstitution ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Institution.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    institutions.add(mapToInstitution( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        return institutions;
    }
    public static List<Instrument>    queryInstrument(Map query , Clotho clothoObject, QueryMode mode)
    {
        
        List<Instrument> instruments = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Instrument.class.getCanonicalName())){
                            instruments.add(ClothoAdapter.getInstrument ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Instrument.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    instruments.add(mapToInstrument( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        return instruments;
        
    }
    
    public static List<Lab> queryLab(Map query, Clotho clothoObject, QueryMode mode){
        List<Lab> labs = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Lab.class.getCanonicalName())){
                            labs.add(ClothoAdapter.getLab ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Lab.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    labs.add(mapToLab( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        return labs;
    }
    
    public static List<Notebook>      queryNotebook(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Notebook> notebooks = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Notebook.class.getCanonicalName())){
                            notebooks.add(ClothoAdapter.getNotebook ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Notebook.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    notebooks.add(mapToNotebook( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        return notebooks;
    }
    public static List<Order>         queryOrder(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Order> orders = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Order.class.getCanonicalName())){
                            orders.add(ClothoAdapter.getOrder ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Order.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    orders.add(mapToOrder( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        
        return orders;
    } 
    public static List<Person>        queryPerson(Map query , Clotho clothoObject, QueryMode mode)
    {
        
        List<Person> people = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                if (queryResults != null){
                    for (Object queryResult : queryResults){
                        if ( ((Map) queryResult).containsKey("schema")){
                            String schema = (String) ((Map) queryResult).get("schema");
                            if ( schema.equals(Person.class.getCanonicalName())){
                                people.add(ClothoAdapter.getPerson ( (String) ((Map) queryResult).get("id") , clothoObject));
                            }
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Person.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
                if (queryResults != null){
                    for (Object queryResult : queryResults)
                    {
                        people.add(mapToPerson( (Map) queryResult, clothoObject));
                    }
                }
                
                break;
            default: 
                break;
        }
        
        
        
        
        return people;
    }
    public static List<Product>       queryProduct(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Product> products = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Product.class.getCanonicalName())){
                            products.add(ClothoAdapter.getProduct ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Product.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    products.add(mapToProduct( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        
        return products;
        
    }
    public static List<Project>       queryProject(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Project> projects = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                //map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Project.class.getCanonicalName())){
                            projects.add(ClothoAdapter.getProject ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Project.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    projects.add(mapToProject( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        
        return projects;
           
    }
    public static List<Protocol>      queryProtocol(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Protocol> protocols = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Protocol.class.getCanonicalName())){
                            protocols.add(ClothoAdapter.getProtocol ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Protocol.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    protocols.add(mapToProtocol( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        
        return protocols;
    }
    public static List<Publication>   queryPublication(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Publication> publications = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Publication.class.getCanonicalName())){
                            publications.add(ClothoAdapter.getPublication ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Publication.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    publications.add(mapToPublication( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        
        
        
        return publications;
    } 
    public static List<Sample>        querySample(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Sample> samples = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Sample.class.getCanonicalName())){
                            samples.add(ClothoAdapter.getSample ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Sample.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    samples.add(mapToSample( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        return samples;
        
    }
    public static List<Status>        queryStatus(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Status> statuses = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Status.class.getCanonicalName())){
                            statuses.add(ClothoAdapter.getStatus ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Status.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    statuses.add(mapToStatus( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        return statuses;
    }  
    public static List<Vendor>        queryVendor(Map query , Clotho clothoObject, QueryMode mode)
    {
        List<Vendor> vendors = new ArrayList<>();
        JSONArray queryResults;
        
        switch (mode){
            case STARTSWITH:
                //EXAMPLE 
                //map.put("query", "Tel"); // the value for which we are querying.
                // map.put("key", "name"); // the key of the object we are querying
                queryResults = (JSONArray) clothoObject.startsWith(query); 
                
                for (Object queryResult : queryResults){
                    if ( ((Map) queryResult).containsKey("schema")){
                        String schema = (String) ((Map) queryResult).get("schema");
                        if ( schema.equals(Vendor.class.getCanonicalName())){
                            vendors.add(ClothoAdapter.getVendor ( (String) ((Map) queryResult).get("id") , clothoObject));
                        }
                    }
                }
                
                
                break;
            case EXACT:
                query.put("schema", Vendor.class.getCanonicalName());
                queryResults = (JSONArray) clothoObject.query(query);
        
                for (Object queryResult : queryResults)
                {
                    vendors.add(mapToVendor( (Map) queryResult, clothoObject));
                }
                
                break;
            default: 
                break;
        }
        
        return vendors;
        
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Map Methods">
    private static CartItem      mapToCartItem(Map map, Clotho clothoObject)
    {
        String id = "";
        if (map.containsKey("id")){
            id = (String) map.get("id");
        }
        
        Date dateCreated = new Date();
        if (map.containsKey("dateCreated"))
        {
        
            String dateCreatedText = (String) map.get("dateCreated");

            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); 

            try {
                dateCreated = df.parse(dateCreatedText);
            } catch (ParseException e) {
                
            }
        }
        
        Double discount = 1d;
        
        if (map.containsKey("discount")){
            discount = Double.parseDouble((String) map.get("discount"));
        }   
        
        String product = "";
        if (map.containsKey("product")){
            product = (String) map.get("product");
        }
        Integer quantity = 0;
        
        if (map.containsKey("quantity")){
            quantity = Integer.parseInt((String) map.get("quantity"));
        }
        
        
        
        
        CartItem cartItem = new CartItem();
        cartItem.setDateCreated(dateCreated);
        cartItem.setId(id);
        cartItem.setDiscount(discount);
        cartItem.setQuantity(quantity);
        cartItem.setProductId(product);
        
        
        
        
        
        return cartItem;
    }
    private static Container     mapToContainer(Map map, Clotho clothoObject)
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
        //new container object
        Container container = new Container(name);
        container.setDescription(description);
        container.setId(id);
        
        
        
        return container;
    }
    private static Entry         mapToEntry(Map map, Clotho clothoObject)
    {
       
        String notebook = "";
        if (map.containsKey("notebookId")){
            //The map would contain the ID of the notebook being pointed to so I need to call getNotebook to build its corresponding object
            notebook = (String) map.get("notebookId");
        
        }
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); // This is to parse dates
        Date dateCreated = null; //
        if (map.containsKey("dateCreated")){
            String dateCreatedText = (String) map.get("dateCreated");
            
            dateCreated = new Date(); //current date
            try {
                dateCreated = df.parse(dateCreatedText); //Tries to parse the date objecet, if successful we get the date stored in the DB, else we get the current date
            } catch (ParseException e) {
            }
        }
        //same process but we only need one date formatter.
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
        
        Entry entry = new Entry(notebook, dateCreated, title, text);
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
       
        entry.setLastModified(lastModified);
        entry.setId(id);
        entry.setNotebookId(notebook);
        entry.setText(text);
        entry.setTitle(title);
        entry.setDateCreated(dateCreated);
        
        return entry;
      
    }
    private static FundingAgency mapToFundingAgency(Map map, Clotho clothoObject)
    {
        //see map to company for a more indepth explanation
        //checking to see if the map I received has any of the properties i want, else I initialize dummy variables
        String name = "";
        if (map.containsKey("name")){
           name = (String) map.get("name");
        }
        
        String description = "";
        if (map.containsKey("description")){
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
        
        //set properties
        FundingAgency fundingAgency = new FundingAgency(name);
        fundingAgency.setPhone(phone);
        fundingAgency.setName(name);
        fundingAgency.setUrl(url);
        fundingAgency.setDescription(description);
        fundingAgency.setId(id);
        
    
        
        return fundingAgency;
       
    }
    //good is abstract, can't be gotten
    private static Grant         mapToGrant(Map map, Clotho clothoObject)
    {
        //See Map to Vendor for a more indepth explanation
        String name = "";
        if (map.containsKey("name")) {
            name = (String) map.get("name");
        }
        
        
        String leadPI = "";
        if (map.containsKey("leadPI")) {
           leadPI = (String) map.get("leadPI");
        
        }
        
        
        
        List<String> coPIs = new ArrayList<>() ;
        if (map.containsKey("coPIs")){
            JSONArray coPIids = (JSONArray) map.get("coPIs");
            //iterates through coPI ids and adds the getPerson result to my current coPIs var. 
            for (int i = 0; i < coPIids.size(); i++){
                coPIs.add(coPIids.getString(i));
            }
        }
        
        String programManager = "";
        if (map.containsKey("programManagerId")) {
            programManager = (String) map.get("programManagerId");
        }
        //see above for explanation
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); 
        Date startDate = new Date();
        if (map.containsKey("startDate")) {
            String startDateText = (String) map.get("startDate");


            try {
                startDate = df.parse(startDateText);
            } catch (ParseException e) {
            }
        }
        
        Date endDate = new Date();
        if (map.containsKey("endDate")){
            String endDateText = (String) map.get("endDate");
            try {
                endDate = df.parse(endDateText);
            } catch (ParseException e) {
               
            }
        }
        double budget = 0;
        if (map.containsKey("budget"))
        {
            budget = (double) map.get("budget");
        }
        double amountSpent = 0;
        if (map.containsKey("amountSpent")) {
            amountSpent = (double) map.get("amountSpent");
        }
        List<String> projects = new ArrayList<>();
        if (map.containsKey("projects")){
            JSONArray projectIds = (JSONArray) map.get("projects");
            for (int i = 0; i < projectIds.size(); i++){
                projects.add(projectIds.getString(i));
            }
        }
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
        String description = "";
        if (map.containsKey("description")){
            description = (String) map.get("description");
        }
        
        Grant grant = new Grant("");
        grant.setName(name);
        grant.setLeadPI(leadPI);
        grant.setCoPIs(coPIs);
        grant.setProgramManagerId(programManager);
        grant.setStartDate(startDate);
        grant.setEndDate(endDate);
        grant.setBudget(budget);
        grant.setAmountSpent(amountSpent);
        grant.setProjects(projects);
        grant.setDescription(description);
        grant.setId(id);
        
        //create new grant object and assign the variables
        
        return grant;

    }
    //********From here on out its pretty much the same process so I will only add new explanations where something completely new happens ******//
    private static Institution   mapToInstitution(Map map, Clotho clothoObject)
    {
        //id provided
        
        String name = "";
        if (map.containsKey("name")){   
            name =  (String) map.get("name");
        }
        String description = ""; 
        if (map.containsKey("description")) { description = (String) map.get("description"); }
        
        
        String phone = "";
        if (map.containsKey("phone")) { phone = (String) map.get("phone"); }
        
        
        String url = "";
        if (map.containsKey("url")) {url = (String) map.get("url");}
        
        
        
        Institution.InstitutionType type = Institution.InstitutionType.Independent;
        if (map.containsKey("type")) { type = Institution.InstitutionType.valueOf((String) map.get("type")); }
        
        List<String> labs = new ArrayList<>();
        if (map.containsKey("labs")){
            JSONArray labsJSON = (JSONArray) map.get("labs");
            for (int i = 0; i < labsJSON.size(); i++){
                labs.add(labsJSON.getString(i));
            }
        }
        
        Institution institution = new Institution(name);
        institution.setPhone(phone);
        institution.setUrl(url);
        institution.setDescription(description);
        institution.setType(type);
        institution.setLabs(labs);
        
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
        institution.setId(id);
        
        
        
        
        return institution;
    }
    private static Instrument    mapToInstrument(Map map, Clotho clothoObject)
    {
        
              
        //instrument properties
        String name = ""; 
        if (map.containsKey("name")) {name = (String) map.get("name");}
        
        String description = ""; 
        if (map.containsKey("description")) {description = (String) map.get("description");}
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
        
        Instrument instrument = new Instrument(name);
        instrument.setDescription(description);
        instrument.setId(id);
        
               
        return instrument;
        
    }
    private static Inventory     mapToInventory(Map map, Clotho clothoObject)
    {
        //List<Sample> samples
        //List<Instrument> instruments
        
        List<String> samples= new ArrayList<>();
        if (map.containsKey("samples"))
        {
            JSONArray sampleIds = (JSONArray) map.get("samples");
            for (int i = 0; i < sampleIds.size(); i++){
                samples.add(sampleIds.getString(i));
            }
            
        }
        List<String> instruments = new ArrayList<>();
        if (map.containsKey("instruments"))
        {
            JSONArray instrumentIds = (JSONArray) map.get("instruments");
            for (int i = 0; i < instrumentIds.size(); i++){
                instruments.add(instrumentIds.getString(i));
            }
            
        }
        
        Inventory inventory = new Inventory();
        inventory.setSamples(samples);
        inventory.setInstruments(instruments);
        String id = "";
        if (map.containsKey("id"))
        {
            id = (String) map.get("id");
        }
        inventory.setId(id);
        return inventory;
        
        
    }
    private static Lab           mapToLab(Map map, Clotho clothoObject){
        
        String name = "";
        if (map.containsKey("name")){   
            name =  (String) map.get("name");
        }
        String description = ""; 
        if (map.containsKey("description")) { description = (String) map.get("description"); }
        
        
        String phone = "";
        if (map.containsKey("phone")) { phone = (String) map.get("phone"); }
        
        String institution = "";
        if (map.containsKey("institution")) {institution = (String) map.get("institution");}
        
        String url = "";
        if (map.containsKey("url")) {url = (String) map.get("url");}
        
        List<String> leadPIs = new ArrayList<>();
        if (map.containsKey("leadPIs")){
                JSONArray leadPIids = (JSONArray) map.get("leadPIs");
                for (int i = 0; i < leadPIids.size(); i++){
                    leadPIs.add(leadPIids.getString(i));
                }
            
        }
        
           
        Lab lab = new Lab(name);
        lab.setPhone(phone);
        lab.setInstitution(institution);
        lab.setUrl(url);
        lab.setLeadPIs(leadPIs);
        lab.setDescription(description);
       
        
        
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
        lab.setId(id);
        
        
        
        
        return lab;
        
    }
    private static Notebook      mapToNotebook(Map map, Clotho clothoObject)
    {
        String owner = "";
        if (map.containsKey("owner")) {
            String ownerId = (String) map.get("owner");
            
            owner = ownerId;
        }
        List<String> entries = null;
        if (map.containsKey("entries")) {
            JSONArray entriesIds = (JSONArray) map.get("entries");
            entries = new ArrayList<>() ;
            for (int i = 0; i < entriesIds.size(); i++){
                entries.add(entriesIds.getString(i));
            }
        }
        String affiliatedProject = null;
        if (map.containsKey("affiliatedProject")){
            String affiliatedProjectId = (String) map.get("affiliatedProject");
            affiliatedProject = affiliatedProjectId;
        }
        //EEE MMM dd HH:mm:ss z yyyy is the same as THU DEC 20 12:30:23 EST 2015, aka it looks for these patterns when parsing
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); 
        Date dateCreated = new Date();
        
        if (map.containsKey("dateCreated")) {
            String dateCreatedText = (String) map.get("dateCreated");


            try {
                dateCreated = df.parse(dateCreatedText);
            } catch (ParseException e) {
             
            }
        }
        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
        Notebook notebook = new Notebook(owner, affiliatedProject, dateCreated);
        notebook.setEntries(entries);
        notebook.setId(id);
        
                
        return notebook;
    }
    private static Order         mapToOrder(Map map, Clotho clothoObject)
    {
        //Look above at the first 4 map methods for a better explanation of
        
        String name = "";
        String description = "";
        Date dateCreated = new Date();
        String createdBy = "";
        List<String> products = new ArrayList<>();
        String id = "";
        Double budget = -1.0d;
        Double taxRate = -1.0d;
        Integer maxOrderSize = -1;
        String approvedById = "";
        String affiliatedLabId = "";
        List<String> receivedByIds = new ArrayList<>();
        String relatedProject = "";
        OrderStatus orderStat = OrderStatus.DENIED;
        
        if (map != (Object) null)
        {
            if (map.containsKey("name")) {
                name = (String) map.get("name");
            }
            if (map.containsKey("description")){
                description = (String) map.get("description");
            }
            if (map.containsKey("dateCreated"))
            {

                String dateCreatedText = (String) map.get("dateCreated");

                DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); 

                try {
                    dateCreated = df.parse(dateCreatedText);
                } catch (ParseException e) {

                }
            }
            if (map.containsKey("createdById")) {
                String ownerId = (String) map.get("createdById");
                createdBy = ownerId;
            }
            //A MAP OF A PRODUCT AS A KEY AND AN INTEGER VALUE FOR THE VALUE FOR THAT KEY

            
            if (map.containsKey("products")){
                JSONArray productIds = (JSONArray) map.get("products");
                for (int i = 0; i < productIds.size(); i++){
                    products.add(productIds.getString(i));
                }
            }
            
            if (map.containsKey("id")){
                 id = (String) map.get("id");
            }
            if (map.containsKey("budget"))
            {
                budget = Double.parseDouble((String) map.get("budget"));
            }
            
            if (map.containsKey("taxRate")){
                taxRate = Double.parseDouble((String) map.get("taxRate"));
            }


            if (map.containsKey("maxOrderSize"))
            {
                maxOrderSize =  Integer.parseInt((String) map.get("maxOrderSize"));
            }


            if (map.containsKey("approvedById"))
            {
                approvedById = (String) map.get("approvedById");
            }
            if (map.containsKey("affiliatedLabId")){
                affiliatedLabId = (String) map.get("affiliatedLabId");
            }

            if (map.containsKey("receivedByIds")){
                JSONArray receivedIds = (JSONArray) map.get("receivedByIds");
                for (int i = 0; i < receivedIds.size(); i++){
                    receivedByIds.add(receivedIds.getString(i));
                }
            }



            if ( map.containsKey("relatedProject")){
                String projectId = (String) map.get("relatedProject");
                relatedProject=  projectId;
            }


            if (map.containsKey("status"))
            {
                orderStat = OrderStatus.valueOf((String) map.get("status"));
            }
        
        }
        
        Order order = new Order();
        order.setId(id);
        
        order.setName(name);
        order.setDescription(description);
        order.setDateCreated(dateCreated);
        order.setCreatedById(createdBy);
        order.setProducts(products);
        order.setBudget(budget);
        order.setTaxRate(taxRate);
        order.setMaxOrderSize(maxOrderSize);
        order.setApprovedById(approvedById);
        order.setReceivedByIds(receivedByIds);
        order.setAffiliatedLabId(affiliatedLabId);
        
        order.setRelatedProjectId(relatedProject);
        order.setStatus(orderStat);
        
        
        return order;
    }   
    public static Person         mapToPerson(Map map, Clotho clothoObject)
    {
        
        List<String> projects = new ArrayList<>() ;
       
        if ( map.containsKey("projects")){
            
            JSONArray projectIds = (JSONArray) map.get("projects");
            
            for (int i = 0; i < projectIds.size(); i++){
                projects.add(projectIds.getString(i));
            }
        }
      
        List<String> statuses = new ArrayList<>() ;
        if ( map.containsKey("statuses")){
            JSONArray statusIds = (JSONArray) map.get("statuses");
            
            for (int i = 0; i < statusIds.size(); i++){
                statuses.add(statusIds.getString(i));
            }
        }
        
        List<String> notebooks = new ArrayList<>() ;
        
        if ( map.containsKey("notebooks")){
            JSONArray notebookIds = (JSONArray) map.get("notebooks");
            
            for (int i = 0; i < notebookIds.size(); i++){
                notebooks.add(notebookIds.getString(i));
            }
        }
        //institutions
                
                
        List<String> institutions = new ArrayList<>();
        if (map.containsKey("institutions")){
            JSONArray institutionIds = (JSONArray) map.get("institutions");
            for (int i = 0; i < institutionIds.size(); i++){
                institutions.add(institutionIds.getString(i));
            }
        }
        
        List<String> labs = new ArrayList<>() ;
        if ( map.containsKey("labs")){
            JSONArray labIds = (JSONArray) map.get("labs");
            
            for (int i = 0; i < labIds.size(); i++){
                labs.add(labIds.getString(i));
            }
        }
        List<String> colleagues = new ArrayList<>() ;
        
        if ( map.containsKey("colleagues")){
            JSONArray colleagueIds = (JSONArray) map.get("colleagues");
            
            for (int i = 0; i < colleagueIds.size(); i++){
                colleagues.add(colleagueIds.getString(i));
            }
        }
        
        List<String> orders = new ArrayList<>() ;
        if ( map.containsKey("orders")){
            JSONArray orderIds = (JSONArray) map.get("orders");
            
            for (int i = 0; i < orderIds.size(); i++){
                orders.add(orderIds.getString(i));
            }
        }
        
        List<String> submittedOrders = new ArrayList<>();
        if (map.containsKey("submittedOrders")){
            JSONArray orderIds = (JSONArray) map.get("submittedOrders");
            
            for (int i = 0; i < orderIds.size(); i++){
                submittedOrders.add(orderIds.getString(i));
            }
        }
        
        List<String> approvedOrders = new ArrayList<>();
        if (map.containsKey("approvedOrders")){
            JSONArray orderIds = (JSONArray) map.get("approvedOrders");
            
            for (int i = 0; i < orderIds.size(); i++){
                approvedOrders.add(orderIds.getString(i));
            }
        }
       
        List<String> publications = new ArrayList<>() ;
        if ( map.containsKey("publications")){
            JSONArray publicationIds = (JSONArray) map.get("publications");
            
            for (int i = 0; i < publicationIds.size(); i++){
                publications.add(publicationIds.getString(i));
            }
        }

        String id = "";
        if (map.containsKey("id")){
             id = (String) map.get("id");
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
        person.setId(id);
        person.setProjects(projects);
        person.setStatuses(statuses);
        person.setNotebooks(notebooks);
        person.setInstitutions(institutions);
        person.setLabs(labs);
        person.setColleagues(colleagues);
        person.setCreatedOrders(orders);
        person.setPublications(publications);
        person.setSubmittedOrders(submittedOrders);
        person.setApprovedOrders(approvedOrders);
        
        
        
                
        person.setFirstName( map.containsKey("firstName") ? (String) map.get("firstName") : "");
        person.setLastName( map.containsKey("lastName") ? (String) map.get("lastName") : "");
        
       
        person.setEmailId( map.containsKey("emailId") ? (String) map.get("emailId") : "");
        person.setPassword( map.containsKey("password") ? (String) map.get("password"): "");
       
        person.setActivated( map.containsKey("activated") ? (boolean) map.get("activated") : false);
        System.out.println("IN MAP TO PERSON: "+ map.toString());
        person.setActivationString( map.containsKey("activationString") ? (String) map.get("activationString") : "Not Set");
    
        
        person.setSalt(map.containsKey("salt") ? (String) map.get("salt") : "");
        System.out.println("passed the salt");
        
        if (map.containsKey("saltedEmailHash")){
            JSONArray byteArrayAsJSONArray = (JSONArray) map.get("saltedEmailHash");
    
         
            byte[] dataByte = new byte[ byteArrayAsJSONArray.size() ];
       
            for ( int i = 0 ; i < byteArrayAsJSONArray.size() ; i++ ) 
            { 
               
                dataByte[ i ] = Byte.parseByte( byteArrayAsJSONArray.get( i ).toString()) ; 
            
            }
          
            person.setSaltedEmailHash(dataByte);
        } 
        

        
        
            
        
        if ( map.containsKey("roles")){
            Map rolesMap = (Map) map.get("roles");
            for (String lab : labs) {
                if (!lab.equals("Not Set")){
                    JSONArray labroles = (JSONArray) rolesMap.get(lab);
                    if (labroles != null){
                        for (Object labrole : labroles) {
                            person.addRole(lab, PersonRole.valueOf((String) labrole));
                        }
                    }
                }
            }
        }
        return person;
    }
    private static Product       mapToProduct(Map map, Clotho clothoObject)
    {
        
        String name = "";
        if (map.containsKey("name")){ name = (String) map.get("name"); }
        
        String description = "";
        if (map.containsKey("description")) { description = (String) map.get("description"); }
       
        String productURL = ""; 
        if (map.containsKey("description")) { productURL = (String) map.get("productURL"); }
        
        
        String company = "";
        if (map.containsKey("company")) { 
            String companyId =  (String) map.get("company");
            company = companyId;
        }
        GoodType goodType = null;
        if (map.containsKey("goodType")){
            goodType = GoodType.valueOf( (String) map.get("goodType"));
        }
        
        double cost = 0;
        if (map.containsKey("cost")){ cost = (Double) map.get("cost");}
        
        int inventory = 0;
        if (map.containsKey("inventory")) {
            inventory = (int)map.get("inventory");
        }
        
        int quantity = 0;
        if (map.containsKey("quantity")){
            quantity = (int) map.get("quantity");
        }
        
        String id = "";
        if (map.containsKey("id")){  id = (String) map.get("id"); }
        
        Double unitPrice = -1.0d;
        if (map.containsKey("unitPrice"))
        {
            unitPrice = (Double) map.get("unitPrice");
            
        }
         
        
        Product product = new Product(name, company, cost);
        product.setInventory(inventory);
        product.setUnitPrice(cost);
        product.setDescription(description);
        product.setProductURL(productURL);
        product.setQuantity(quantity);
        product.setGoodType(goodType);
        
        product.setId(id);
        
        
        
        return product;
        
    }
    private static Project       mapToProject(Map map, Clotho clothoObject)
    {
 
        
        String creator = "";
        if(map.containsKey("creator"))
        {
            String creatorId = (String) map.get("creator");
            if (!creatorId.equals("Not Set")){
                creator = creatorId;
            }
        
        }
        
       
        
        String lead = "";
        if(map.containsKey("lead")){
            String leadId = (String) map.get("lead");
            if (!leadId.equals("Not Set")){
                lead = leadId;
            }
        }
        
        
        
        List<String> members = new ArrayList<>();
        if (map.containsKey("members"))
        {
            JSONArray memberIds = (JSONArray) map.get("members");
            
                for (int i = 0; i < memberIds.size(); i++)
                {
                    if (!memberIds.getString(i).equals("Not Set")){
                        members.add(memberIds.getString(i));
                    }
                }
              
        }
        
        List<String> notebooks = new ArrayList<>();
        if (map.containsKey("notebooks")){
            JSONArray notebookIds = (JSONArray) map.get("notebooks");
            
            notebooks = new ArrayList<>();
            for (int i = 0; i < notebookIds.size(); i++){
                if (!notebookIds.getString(i).equals("Not Set")){
                    notebooks.add(notebookIds.getString(i));
                }
            }
        }
        
        
        List<String> affiliatedLabs = new ArrayList<>();
        if (map.containsKey("affiliatedLabs")){
           affiliatedLabs = new ArrayList<>() ;
           JSONArray affiliatedLabIds = (JSONArray) map.get("affiliatedLabs");
           for (int i = 0; i < affiliatedLabIds.size(); i++){
               if (!affiliatedLabIds.getString(i).equals("Not Set")){
                   affiliatedLabs.add(affiliatedLabIds.getString(i));
               }
           }
           
        }
        
        String name = "";
        if (map.containsKey("name"))  {  name = (String) map.get("name");}
        
       
        
        double budget = 0;
        if (map.containsKey("budget")) { budget = (double) map.get("budget"); }
        
        String description = "";
        if (map.containsKey("description"))
        {  description = (String) map.get("description"); }

        String grant = "";
        if (map.containsKey("grant"))
        {
            String grantId = (String) map.get("grant");
            grant = grantId;
        }
        
        
        
        
        
        Date dateCreated = new Date();
        if (map.containsKey("dateCreated"))
        {
        
            String dateCreatedText = (String) map.get("dateCreated");

            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); 

            try {
                dateCreated = df.parse(dateCreatedText);
            } catch (ParseException e) {
                
            }
        }
    

        List<String> updates = new ArrayList<>() ;
        if (map.containsKey("updates")){

            JSONArray updateIds = (JSONArray) map.get("updates");
            for (int i = 0; i < updateIds.size(); i++){
                updates.add(updateIds.getString(i));
            }
        }
        Project project = new Project();
        project.setCreatorId(creator);
        project.setLeadId(lead);
        project.setMembers(members);
        project.setNotebooks(notebooks);
        project.setAffiliatedLabs(affiliatedLabs);
        project.setName(name);
        project.setDateCreated(dateCreated);
        project.setUpdates(updates);
        project.setBudget(budget);
        project.setGrantId(grant);
        project.setDescription(description);
        
        
        
        String id = "";
        if (map.containsKey("id")){  id = (String) map.get("id"); }
        project.setId(id);
        
        return project;
     
    }
    private static Protocol      mapToProtocol(Map map, Clotho clothoObject)
    {
        /**
         * creator       : Person
         * protocolName  : String
         * equipment     : List<Instrument>
         * samples       : List<Samples>
         * id            : String
         */
        String creator = "";
        if (map.containsKey("creator")){
           String creatorId = (String) map.get("creator");
           if (!creatorId.equals("Not Set")){
             creator = creatorId;
           }
        }
        String protocolName = "";
        if (map.containsKey("protocolName")){
            protocolName = (String) map.get("protocolName");
        }
        
        List<String> equipment = new ArrayList<>() ;
        if (map.containsKey("equipment")){
            JSONArray equipmentIds = (JSONArray) map.get("equipment");
            
            for (int i = 0; i < equipmentIds.size(); i++){
                equipment.add(equipmentIds.getString(i));
            }

        }
        
        
        
        List<String> samples = new ArrayList<>() ;
        if (map.containsKey("samples")) 
        {
            JSONArray sampleIds = (JSONArray) map.get("samples");
            for (int i = 0; i < sampleIds.size(); i++){
                samples.add(sampleIds.getString(i));
            }
        }
        String id = "";
        if (map.containsKey("id")){ id = (String) map.get("id"); }
        
        Protocol protocol = new Protocol();
        protocol.setCreatorId(creator);
        protocol.setProtocolName(protocolName);
        protocol.setSamples(samples);
        protocol.setEquipment(equipment);
        protocol.setId(id);
        
        
        return protocol;
        
    }
    private static Publication   mapToPublication(Map map, Clotho clothoObject)
    {
        String id = "";
        if (map.containsKey("id")){  id = (String) map.get("id");  }
        Publication publication = new Publication();
        publication.setId(id);
        
        return publication;
    }
    private static Sample        mapToSample(Map map, Clotho clothoObject)
    {
        //sample properties
        
        String name = ""; 
        if (map.containsKey("name")) { name = (String) map.get("name"); }
        
        String description = "" ; 
        if (map.containsKey("description")) { description = (String) map.get("description"); }
        
        Sample sample = new Sample(name);
        sample.setDescription(description);
        String id = "";
        if (map.containsKey("id"))
        {
            id = (String) map.get("id");
        }
        sample.setId(id);
        
        
        
        
        return sample;
        
    }
    private static Status        mapToStatus(Map map, Clotho clothoObject)
    { 
       
        
        String id = "";
        if (map.containsKey("id")){ 
            id = (String) map.get("id");
        }
        
        String text = "";
        if (map.containsKey("text")) {
            text = (String) map.get("text");
        }
        
        String user = "";
        if (map.containsKey("user")) { 
            String userId = (String) map.get("user");
            if (!userId.equals("Not Set")){
                user = userId;
            }
        
        }
        
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); 
        Date created = new Date();
        if ( map.containsKey("created")){
            String dateCreatedText = (String) map.get("created");
            
            
            try 
            {
                created = df.parse(dateCreatedText);
            } catch (ParseException e) {
            }
        }
        
                
        Status status = new Status();
        status.setText(text);
        status.setUserId(user);
        status.setId(id);
        status.setCreated(created);
        
        return status;
               
    
    }
    private static Vendor        mapToVendor(Map map, Clotho clothoObject)
    {
    
        //I set each property of a Vendor object to a default value (that match the constructors)
        String contact = "";
        //I see if the map contains a key for that Vendor property
        if (map.containsKey("contact")){
            //if so then I assign that value to my local variable
             contact = (String) map.get("contact");
        }
        //Same thing here
        String name = "";
        
        if (map.containsKey("name"))
        {
             name = (String) map.get("name");
        }
        //Same
        String description = "";
        
        if (map.containsKey("name")){
             description = (String) map.get("description");
        }
        //Same
        String phone = "";
        
        if (map.containsKey("phone")){
             phone = (String) map.get("phone");
        }
        
        String url = "";
        
        if (map.containsKey("url")){
             url = (String) map.get("url");
        }
        
        String id = "";
        //I get the ID from the map because it should come with it and it especially makes sure I am parsing the right object
        if (map.containsKey("id")){
             id = (String) map.get("id");
        }
        
        //create a new Vendor object
        Vendor savedCompany = new Vendor(name);
        //assign it those values
        savedCompany.setContact(contact);
        savedCompany.setDescription(description);
        savedCompany.setPhone(phone);
        savedCompany.setUrl(url);
        savedCompany.setId(id);
               
        return savedCompany;
        
    }
    
    // </editor-fold>
    //  <editor-fold defaultstate="collapsed" desc="Set Methods ">
    public static String setVendor(Vendor company, Clotho clothoObject){
        return ClothoAdapter.createVendor(company, clothoObject);
    }
    public static String setContainer(Container container, Clotho clothoObject){
        return ClothoAdapter.createContainer(container, clothoObject);
    }
    public static String setEntry(Entry entry, Clotho clothoObject){
        return ClothoAdapter.createEntry(entry, clothoObject);
    }
    public static String setFundingAgency(FundingAgency fundingAgency, Clotho clothoObject){
        return ClothoAdapter.createFundingAgency(fundingAgency, clothoObject);
    }
    public static String setGrant(Grant grant, Clotho clothoObject){
        return ClothoAdapter.createGrant(grant, clothoObject);
    }
    public static String setInstitution(Institution institution, Clotho clothoObject){
        return ClothoAdapter.createInstiution(institution, clothoObject);
    }
    public static String setInstrument(Instrument instrument, Clotho clothoObject){
        return ClothoAdapter.createInstrument(instrument, clothoObject);
    }
    public static String setInventory(Inventory inventory, Clotho clothoObject){
        return ClothoAdapter.createInventory(inventory, clothoObject);
    }
    public static String setLab(Lab lab, Clotho clothoObject){
        return ClothoAdapter.createLab(lab, clothoObject);
    }
    public static String setNotebook(Notebook notebook, Clotho clothoObject){
        return ClothoAdapter.createNotebook(notebook, clothoObject);
    }
    public static String setOrder(Order order, Clotho clothoObject){
        return ClothoAdapter.createOrder(order, clothoObject);
    }
    public static String setPerson(Person person, Clotho clothoObject){
        /*add logic */ 
         Map map = new HashMap();
        map.put("schema", Person.class.getCanonicalName());


        if(person.getSalt() != null ){
            if (!person.getSalt().isEmpty() && !person.getSalt().equals("Not Set"))
                map.put("salt", person.getSalt());
        }
         
    
        if (person.getSaltedEmailHash() != null) 
        {
            map.put("saltedEmailHash", Arrays.toString(person.getSaltedEmailHash()) );
           
        }
        
        if (person.getProjects() != null)
        {
            
                JSONArray projects = new JSONArray();
                for (String project : person.getProjects()){
                    if (project != null){
                        if (!project.equals("Not Set") && !project.isEmpty()){
                            projects.add(project);
                        }
                    }
                }
                map.put("projects", projects);
            
        }
        
        if (person.getInstitutions() != null)
        {
            
                JSONArray institutions = new JSONArray();
                for (String institution : person.getInstitutions()){
                    if (institution != null){
                        if (!institution.equals("Not Set") && !institution.isEmpty()){
                            institutions.add(institution);
                        }
                    }
                }
                map.put("institutions", institutions);
            
        }
       
        if (person.getStatuses() != null)
        {
                JSONArray statuses = new JSONArray();
                for (String status : person.getStatuses()){
                    if (status != null){
                        if (!status.equals("Not Set") && !status.isEmpty() ){
                           statuses.add(status);
                        }
                    }
                }
                map.put("statuses", statuses);
            
        }
        
        if (person.getNotebooks() != null)
        {
                JSONArray notebooks = new JSONArray();
                for (String notebook : person.getNotebooks()){
                    if (notebook != null){
                        if (!notebook.equals("Not Set") && !notebook.isEmpty()){
                            notebooks.add(notebook);
                        }
                    }
                }
                map.put("notebooks", notebooks);
            
        }
        if (person.getLabs() != null)
        {
           
                JSONArray labs = new JSONArray();
                JSONArray roles;
                Map rolesMap = new HashMap();
                for (String institution : person.getLabs())
                {
                    if (institution != null){
                        if (!institution.equals("Not Set") && !institution.isEmpty()){
                            labs.add(institution);
                        }
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
                        if (institution != null){
                            if (!institution.equals("Not Set") && !institution.isEmpty()){
                                rolesMap.put(institution, roles);
                            }
                        }
                    }

                }
                map.put("labs", labs);
                map.put("roles", rolesMap);
            }
        
        if (person.getColleagues() != null)
        {
            
                JSONArray colleagues = new JSONArray();
                for (String colleague : person.getColleagues())
                {
                    if (colleague != null){
                        if (!colleague.equals("Not Set") && !colleague.isEmpty()){
                            colleagues.add(colleague);
                        }
                    }

                }
                map.put("colleagues", colleagues);
            
        }
        
        if (person.getCreatedOrders() != null)
        {
            
                JSONArray orders = new JSONArray();
                for (String order : person.getCreatedOrders())
                {
                    if (order!= null)
                    {
                        if (!order.equals("Not Set") && !order.isEmpty()){
                            orders.add(order);
                        }
                    }
                }
                map.put("orders", orders);
           
        }
        if (person.getSubmittedOrders()!= null)
        {
           
                JSONArray orders = new JSONArray();
                for (String order : person.getSubmittedOrders())
                {
                    if (order!= null)
                    {
                        if (!order.equals("Not Set") && !order.isEmpty()){
                            orders.add(order);
                        }
                    }
                }
                map.put("submittedOrders", orders);
            
        }
        
        if (person.getApprovedOrders()!= null)
        {
            
                JSONArray orders = new JSONArray();
                for (String order : person.getApprovedOrders())
                {
                    if (order!= null)
                    {
                        if (!order.equals("Not Set") && !order.isEmpty()){
                            orders.add(order);
                        }
                    }
                }
                map.put("approvedOrders", orders);
            
        }
        
        
        if(person.getPublications() != null)
        {
            
                JSONArray publications = new JSONArray();
                for (String publication : person.getPublications())
                {
                    if (publication != null){
                        if (!publication.equals("Not Set") && !publication.isEmpty()){
                            publications.add(publication);
                        }
                    }
                    
                }
                map.put("publications", publications);
            
        }
        if (person.getFirstName() !=null)
            map.put("firstName", person.getFirstName());
        
        if(person.getLastName() != null)
            map.put("lastName", person.getLastName());
        
        if(person.getEmailId() != null){
            map.put("emailId", person.getEmailId());
            map.put("name", person.getEmailId());
        }
        if(person.getPassword() != null)
            map.put("password", person.getPassword());
        
        //defaults are false and "Not Set" for these two lines in the constructors
        //SHOULD BE
        map.put("activated", person.isActivated());
        map.put("activationString", person.getActivationString());
        
        String username = person.getEmailId()  ;
        String password = person.getPassword();
        
        Map loginUserMap = new HashMap();
        loginUserMap.put("username", username);
        loginUserMap.put("credentials", password);
        
        
        
        
        if (person.getId() != null){
            if (!person.getId().equals("Not Set") && !person.getId().isEmpty()){
                map.put("id", person.getId());
            }
        }
       
        
        
        
        
        Map loginResult = (Map)(clothoObject.login(loginUserMap));
        
        String id = "Not Set";
        if (!loginResult.isEmpty()){
            id = (String) clothoObject.set(map);
        }else {
            System.out.println("NO USER FOUND OR INVALID CREDENTIALS IN PERSON OBJECT");
        }
        
        person.setId(id);
        makePublic(id, clothoObject);
        clothoObject.logout();
        return id;
    
    }
    public static String setProduct(Product product, Clotho clothoObject){
        return ClothoAdapter.createProduct(product, clothoObject);
    }
    public static String setProject(Project project, Clotho clothoObject){
        return ClothoAdapter.createProject(project, clothoObject);
    }
    public static String setProtocol(Protocol protocol, Clotho clothoObject){
        return ClothoAdapter.createProtocol(protocol, clothoObject);
    }
    public static String setPublication(Publication publication, Clotho clothoObject){
        return ClothoAdapter.createPublication(publication, clothoObject);
    }
    public static String setSample(Sample sample , Clotho clothoObject){
        return ClothoAdapter.createSample(sample, clothoObject);
    }
    public static String setStatus(Status status, Clotho clothoObject){
        return ClothoAdapter.createStatus(status, clothoObject);
    }
    // </editor-fold>
    //  <editor-fold defaultstate="collapsed" desc="Misc Methods">
    /**
     * Makes an object Public in Clotho
     * @param objectId Clotho ID
     * @param clothoObject 
     */
    public static void makePublic(String objectId, Clotho clothoObject){
        List<String> add = new ArrayList<>();
        List<String> remove = new ArrayList<>();
        
        add.add("public");
        
        Map grantMap = new HashMap();
        grantMap.put("id", objectId);
        grantMap.put("user", "none");
        grantMap.put("add", add);
        grantMap.put("remove", remove);
        
        Map grantResult = (Map)(clothoObject.grant(grantMap));
    }
    
    public static void clothoCreate(String username, String password)
    {       
       Map createUserMap = new HashMap();  
       createUserMap.put("username", username);
       createUserMap.put("password", password);
       
       ClothoAdapter.clothoObject.createUser(createUserMap);
    }
    
    public static void clothoLogin(String username, String password)
    {
       Map loginMap = new HashMap();
       loginMap.put("username", username);
       loginMap.put("credentials", password);  
       
       ClothoAdapter.clothoObject.login(loginMap);
       
    }
    
    public static void setUpRandomUser(){
       ClothoAdapter.conn = new ClothoConnection(Args.clothoLocation);
       ClothoAdapter.clothoObject = new Clotho(conn);
       
       ClothoAdapter.username = randomUsername();
       ClothoAdapter.password = "password";
       clothoCreate(ClothoAdapter.username, ClothoAdapter.password);
       clothoLogin(ClothoAdapter.username, ClothoAdapter.password);
    }
    
    public static String randomUsername(){
        return "test"+ System.currentTimeMillis();
    }
    //  </editor-fold>

    public static enum QueryMode { STARTSWITH, EXACT }
}

