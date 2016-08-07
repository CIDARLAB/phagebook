/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.adaptors.EmailHandler;
import org.clothocad.phagebook.adaptors.S3Adapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Lab;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Order.OrderColumns;
import static org.clothocad.phagebook.controller.OrderController.createOrderForm;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.security.EmailSaltHasher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author jacob
 */

@Controller
public class MiscControllers {
    @RequestMapping(value = "/createVendor", method = RequestMethod.POST)
    protected void createVendor(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        
        //get all necessary fields to create 
        //REQUIRING NAME & DESCRIPTION & CONTACT 
        boolean isValid = false; 
        
        String name = params.get("name") != null ? params.get("name") : "";
        
        String description = params.get("description") != null ? params.get("description") : "";
        
        String contact = params.get("contact") != null ? params.get("contact"):"";
        
        
        String phone = params.get("phone") != null ? params.get("phone") : "" ;
        String url = params.get("url") != null ? params.get("url") : "" ;
        
        
        if (!name.isEmpty() && !description.isEmpty() && !contact.isEmpty())
        {
            isValid = true;
            
            
        } else 
        {
            
        }
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            //TODO: we need to have an authentication token at some point

            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password); 
            clothoObject.login(loginMap);
        
            
            Vendor vendor = new Vendor();
            vendor.setName(name);
            vendor.setDescription(description);
            vendor.setContact(contact);
            
            if (!phone.isEmpty())
            {
                vendor.setPhone(phone);
            }
            if (!url.isEmpty())
            {
                vendor.setUrl(url);
            }
            
             //everything is set for that product
            ClothoAdapter.createVendor(vendor, clothoObject);
            JSONObject vendorJSON = new JSONObject();
            vendorJSON.put("id", vendor.getId());
            conn.closeConnection();
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(vendorJSON);
                out.flush();
                out.close();
                
            clothoObject.logout();
            
            
        }else
        {
            JSONObject msg = new JSONObject();
            msg.put("message", "Need to send name, description, and contact");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(msg);
                out.flush();
                out.close();
        }
        
    }
    
    @RequestMapping(value = "/resendVerification", method = RequestMethod.GET)
    protected void resendVerification(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        String userId = params.get("id");
        boolean isValid = false;
        if (!userId.isEmpty()){
            isValid = true;
        }
        if (isValid){
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            

            Map loginMap = new HashMap();
            loginMap.put("username", "phagebook");
            loginMap.put("credentials", "backend");

            clothoObject.login(loginMap);
            
            //operating under the assumption that we will have the saved clotho ID of the user

            Person person1 = ClothoAdapter.getPerson(userId, clothoObject);
            
            if (person1 != null){
                String link = Args.phagebookBaseURL + "/html/validateEmail.html?emailId=" + person1.getEmailId() + "&salt=" + person1.getSalt();
                EmailHandler handly = EmailHandler.getEmailHandler();
                handly.sendEmailVerification(person1, link);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "Email Sent!");
                out.print(responseJSON.toString());
                out.flush();
                out.close();
            } 
            clothoObject.logout();
            conn.closeConnection();
        }
        else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "Cannot find the user. Try again later!");
            out.print(responseJSON.toString());
            out.flush();
            out.close();
        }
        
        
        
    }
    
    @RequestMapping(value = "/uploadVendorCSV", method = RequestMethod.POST)
    public void uploadVendorCSV(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {
        
        JSONArray arr = new JSONArray(params.get("jsonArray"));

        List<String> vendorIds = new ArrayList<String>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = "phagebook";
        String password = "backend";
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);  

        clothoObject.login(loginMap);
        
        vendorIds = org.clothocad.phagebook.controller.OrderController.getVendors(arr, clothoObject);
        
        PrintWriter writer = response.getWriter();
        conn.closeConnection();
        writer.println("created " + vendorIds);
        writer.flush();
        writer.close();
    } 
    
    @RequestMapping(value = "/verifyEmail", method = RequestMethod.POST)
    protected void doPost(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        String emailId ;
        String salt ;
        boolean hasValidParameters = false;
        salt = params.get("salt");
        emailId = params.get("emailId");
        System.out.println("salt: " + salt + "  email" + emailId);
        if (!salt.isEmpty() && !emailId.isEmpty()){
            hasValidParameters = true;
        }
        if (hasValidParameters){
            
            EmailSaltHasher salty = EmailSaltHasher.getEmailSaltHasher();
            Map query = new HashMap();
            List<Person> queryPersons = new LinkedList<>();
            query.put("emailId", emailId);
            
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
          

            Map loginMap = new HashMap();
            loginMap.put("username", "ClothoBackend");
            loginMap.put("credentials", "phagebook");


            clothoObject.login(loginMap);
           
           
          
            queryPersons = ClothoAdapter.queryPerson(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
            System.out.println("I leave that method");
            byte[] recreatedHash = salty.hash(emailId.toCharArray(), salt.getBytes("UTF-8"));
           
            boolean isValidated = salty.isExpectedPassword(emailId.toCharArray(), salt.getBytes("UTF-8"), queryPersons.get(0).getSaltedEmailHash());
            
       
            
            if (isValidated){
                Person pers = queryPersons.get(0);

                System.out.println("User "  + queryPersons.get(0).getEmailId() + " has been validated");
               
                pers.setActivated(true);
                clothoObject.logout();
                ClothoAdapter.setPerson(pers, clothoObject);  
                System.out.println("HERE AT VERIFY EMAIL: "+ ClothoAdapter.getPerson(pers.getId(), clothoObject).isActivated());
                S3Adapter.initializeUserFolder(pers);//queryPersons.get(0).getId()
                
                
                
            } else if (!isValidated){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            clothoObject.logout();
            
            
        }
      
       
    }
    
    @RequestMapping(value="/loadPhagebookInstitutions", method=RequestMethod.GET)
    public void getPhagebookInstitutions(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        
        //always valid 
        //login
        System.out.println("loadphagebook");
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        String username = "phagebook";
        String password = "backend";
        
        
        /*

            DIRECT ASSUMPTION THAT USER: phagebook exists and their 
                               PASSWORD: backend
        */
        Map loginMap = new HashMap();
                
        loginMap.put("username"   , username);
        loginMap.put("credentials", password);
                
        clothoObject.login(loginMap);
                

        Map query = new HashMap(); // blank map to get all objects of that class
        
        List<Institution> institutions = ClothoAdapter.queryInstitution(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
        
        JSONArray institutionsInClotho = new JSONArray();
        int countInstits = 0;
        int countLabs=0;
        for (Institution institute : institutions){
            countInstits++;
            JSONArray labs = new JSONArray();
            for (String labId : institute.getLabs()){
                countLabs++;
                Lab lab = ClothoAdapter.getLab(labId, clothoObject);
                JSONObject obj = new JSONObject();
                obj.put("labName", lab.getName());
                obj.put("labId", lab.getId());
                labs.put(obj);
            }
            JSONObject institution = new JSONObject();
            institution.put("institutionId", institute.getId());
            institution.put("institutionName", institute.getName());
            institution.put("labs", labs);
            institutionsInClotho.put(institution);
            
        }
        
        
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();
        obj.put("message", "found " + countLabs + " labs and " + countInstits +" institutions" );
        obj.put("institutions", institutionsInClotho);
        out.print(obj);
        out.flush();
        out.close();
        clothoObject.logout();
        conn.closeConnection();
    }
    
    @RequestMapping(value="/selectColumns", method=RequestMethod.POST)
    public void selectColumns(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {
    /*
        String SERIAL_NUMBER = params.get("serialNumber");
        String PRODUCT_NAME = params.get("productName");
        String PRODUCT_URL = params.get("productUrl");
        String PRODUCT_DESCRIPTION = params.get("productDescription");
        String QUANTITY = params.get("quantity");
        String COMPANY_NAME = params.get("companyName");
        String COMPANY_URL = params.get("companyUrl");
        String COMPANY_DESCRIPTION = params.get("companyDescription");
        String COMPANY_CONTACT = params.get("companyContact");
        String COMPANY_PHONE = params.get("companyPhone");
        String UNIT_PRICE = params.get("unitPrice");
        String TOTAL_PRICE = params.get("totalPrice");
    */
       System.out.println("Reached doPost");
       String id = params.get("orderId");
       System.out.println(id);
       if ((id!=null) && (!id.equals("")))
       {    
            System.out.println("ID is not null");
            List<OrderColumns> orderColumns = new ArrayList<>();
            System.out.println("Serial Number " + params.get("serialNumber"));
            System.out.println("Product Name :: " +params.get("productName"));
            if("true".equals(params.get("serialNumber")))
                
            {
                orderColumns.add(OrderColumns.SERIAL_NUMBER);
            }
            
            if("true".equals(params.get("productName"))){
                orderColumns.add(OrderColumns.PRODUCT_NAME);
            }
            if("true".equals(params.get("productUrl"))){
                orderColumns.add(OrderColumns.PRODUCT_URL);
            }
            if("true".equals(params.get("productDescription"))){
                orderColumns.add(OrderColumns.PRODUCT_DESCRIPTION);
            }
            if("true".equals(params.get("quantity"))){
                orderColumns.add(OrderColumns.QUANTITY);
            }
            if("true".equals(params.get("companyName"))){
                orderColumns.add(OrderColumns.COMPANY_NAME);
            }
            if("true".equals(params.get("companyUrl"))){
                orderColumns.add(OrderColumns.COMPANY_URL);
            }
            if("true".equals(params.get("companyDescription"))){
                orderColumns.add(OrderColumns.COMPANY_DESCRIPTION);
            }
            if("true".equals(params.get("companyContact"))){
                orderColumns.add(OrderColumns.COMPANY_CONTACT);
            }
            if("true".equals(params.get("companyPhone"))){
                orderColumns.add(OrderColumns.COMPANY_PHONE);
            }
            if("true".equals(params.get("unitPrice"))){
                orderColumns.add(OrderColumns.UNIT_PRICE);
            }
            if("true".equals(params.get("totalPrice"))){
                orderColumns.add(OrderColumns.TOTAL_PRICE);
            }   
            
            System.out.println("Order Columns " + orderColumns);
            
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "phagebook";
            createUserMap.put("username", username);
            createUserMap.put("password", "password");

            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");

            clothoObject.login(loginMap);
            System.out.println("HERE AT SELECT 1");
            Order order = ClothoAdapter.getOrder(id, clothoObject);
            System.out.println("HERE AT SELECT 2");
            List<String> orderFormLines = createOrderForm(order,orderColumns);
            System.out.println(orderFormLines);
            
            
            String filepath = MiscControllers.class.getClassLoader().getResource(".").getPath();
            System.out.println("File path ::" + filepath);
            filepath = filepath.substring(0, filepath.indexOf("/target/"));
            System.out.println("\nTHIS IS THE FILEPATH: " + filepath);
            
            
            
            String filepathOrderForm = filepath + "/orderForm.csv";
            File file = new File(filepathOrderForm);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(String line : orderFormLines)
            {
                writer.write(line);
                writer.newLine();
            }
            
            writer.flush();
            writer.close();
            
            PrintWriter reponseWriter = response.getWriter();
            reponseWriter.println(filepathOrderForm);
            reponseWriter.flush();
            reponseWriter.close();
            clothoObject.logout();
            conn.closeConnection();
       }
       else
       {
           response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
       }
                        
    }
}
