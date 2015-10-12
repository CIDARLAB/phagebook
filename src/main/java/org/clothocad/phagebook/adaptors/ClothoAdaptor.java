/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.util.HashMap;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.phagebook.dom.Company;
import org.clothocad.phagebook.dom.Container;
import org.clothocad.phagebook.dom.Entry;
import org.clothocad.phagebook.dom.FundingAgency;
import org.clothocad.phagebook.dom.Good;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Instrument;
import org.clothocad.phagebook.dom.Inventory;
import org.clothocad.phagebook.dom.Notebook;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Sample;
import org.json.JSONArray;

/**
 *
 * @author prash
 */
public class ClothoAdaptor {
    // <editor-fold defaultstate="collapsed" desc="Create Methods">
     
    public static String createCompany(Company company, Clotho clothoObject){
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
        return id;
    }
    public static String createContainer(Container container, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("name", container.getName());
        map.put("id", container.getId());
        map.put("description", container.getDescription());
        id = (String) clothoObject.set(map);
        return id;
    }
    
    public static String createEntry(Entry entry, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("notebook", entry.getNotebook());
        map.put("dateCreated", entry.getDateCreated());
        map.put("lastModified", entry.getLastModified());
        map.put("text", entry.getText());
        map.put("title", entry.getTitle());
        map.put("id", entry.getId());
        
        id = (String) clothoObject.set(map);
        return id;
        
    }
    
    public static String createFundingAgency(FundingAgency fundingAgency, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("id", fundingAgency.getId());
        map.put("name", fundingAgency.getName());
        map.put("description", fundingAgency.getDescription());
        map.put("phone", fundingAgency.getPhone());
        map.put("url", fundingAgency.getUrl());
        id = (String) clothoObject.set(map);
        return id;
    }
    
    public static String createGood(Good good, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("id", good.getId());
        map.put("name", good.getName());
        map.put("description", good.getDescription());
                
        id = (String) clothoObject.set(map);
        return id;
    }
    
    public static String createGrant(Grant grant, Clotho clothoObject){
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
        map.put("startDate", grant.getStartDate());
        map.put("endDate", grant.getEndDate());
        map.put("budget", grant.getBudget());
        map.put("amountSpent", grant.getAmountSpent());
        
        JSONArray projects = new JSONArray();
        for (Project project : grant.getProjects()){
            projects.put(project.getId());
        }
        map.put("projects" , projects);
        map.put("description", grant.getDescription());
        map.put("id", grant.getId());
        
        
        id = (String) clothoObject.set(map);
        return id;
        
    }
    
    public static String createInstiution(Institution institution, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("id", institution.getId());
        map.put("name", institution.getName());
        map.put("description", institution.getDescription());
        map.put("phone", institution.getPhone());
        map.put("url", institution.getUrl());
        
        id = (String) clothoObject.set(map);
        return id;
    }
    
    public static String createInstrument(Instrument instrument, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("id", instrument.getId());
        map.put("name", instrument.getName());
        map.put("description", instrument.getDescription());
        
        id = (String) clothoObject.set(map);
        return id;
    }
    
    public static String createInventory(Inventory inventory, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        int i = 0;
        for (Sample samples : inventory.getSamples()){
            map.put("samples"+i, samples.getId());
            i++;
        }
        i = 0;
        for (Instrument instrument : inventory.getInstruments()){
            map.put("instruments", instrument.getId());
            i++;
        }
        
        id = (String) clothoObject.set(map);
        return id;
    }
    public static String createNotebook(Notebook notebook, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("owner", notebook.getOwner().getId());
        int i = 0;
        for (Entry entries : notebook.getEntries()){
            map.put("entries" + i , map);
            i++;
        }
        map.put("affiliatedProject", notebook.getAffiliatedProject().getId());
        map.put("dateCreated", notebook.getDateCreated());
        map.put("id", notebook.getId());
        
        id = (String) clothoObject.set(map);
        return id;
    }
    
    public static String createOrder(Order order, Clotho clothoObject){
        String id = "";
        Map map = new HashMap();
        map.put("id", order.getId());
        int i = 0;
        for (Product products : order.getProducts()){
            map.put("products" + i, products.getId());
        }
        map.put("name", order.getName());
        
        id = (String) clothoObject.set(map);
        return id;
    }
    
//    public static String createOrganization(Organization organization, Clotho clothoObject){
//        
//    }
    
    public static String createProduct(Product product, Clotho clothoObject){
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
        return id;
       
    }
    
    
    // </editor-fold>
    /* query
       queryOne
       get
       getAll
       create
       createAll
       submit
    */
    // <editor-fold defaultstate="collapsed" desc="Get Methods">
    
    // </editor-fold>
}
