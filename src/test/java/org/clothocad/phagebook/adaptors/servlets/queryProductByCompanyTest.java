/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.dom.Product;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Herb
 */
public class queryProductByCompanyTest {
    
    public queryProductByCompanyTest() {
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
    }

    /**
     * Test of processRequest method, of class queryProductByCompany.
     */
    @Test
    public void testProcessRequest() throws Exception {
      
        
    }

    /**
     * Test of doGet method, of class queryProductByCompany.
     */
    @Test
    public void testDoGet() throws Exception {
        //
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map createUserMap = new HashMap();
        createUserMap.put("username", "ClothoBackend");
        createUserMap.put("password", "phagebook");
        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", "ClothoBackend");
        loginMap.put("credentials", "phagebook");
        clothoObject.login(loginMap);
        //
        Map query = new HashMap();
        query.put("name", "Amazon");
            
        List<Vendor> queryCompanyResults = ClothoAdapter.queryVendor(query, clothoObject);
        //To get Vendor Name and ID to query for products with that company...
        List<String> companyIDs = new LinkedList<>();
        for (Vendor company : queryCompanyResults ){
            companyIDs.add(company.getId());
        }
        System.out.println(companyIDs.toString());

        JSONArray results = new JSONArray();
        for (String companyID : companyIDs)
        {
            Map queryForClotho = new HashMap();
            queryForClotho.put("company", companyID);
            List<Product> queryProductResults = ClothoAdapter.queryProduct(queryForClotho, clothoObject);

            for (Product product : queryProductResults){
                JSONObject productAsJson = new JSONObject();
                productAsJson.put("cost", product.getCost());
                productAsJson.put("productURL", (product.getProductURL() != null) ? product.getProductURL() : "");
                productAsJson.put("goodType", (product.getGoodType() != null) ? product.getGoodType() : "");
                productAsJson.put("quantity", product.getQuantity());
                productAsJson.put("name", product.getName());
                productAsJson.put("description", product.getDescription());
                productAsJson.put("company", product.getCompany().getName());

                results.add(productAsJson);
            }
        }


       
        
    }

    /**
     * Test of doPost method, of class queryProductByCompany.
     */
    @Test
    public void testDoPost() throws Exception {
        
    }

    /**
     * Test of getServletInfo method, of class queryProductByCompany.
     */
    @Test
    public void testGetServletInfo() {
        
    }
    
}
