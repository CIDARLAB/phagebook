/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.HashMap;
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
public class queryProductByNameTest {
    
    public queryProductByNameTest() {
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
     * Test of doGet method, of class queryProductByName.
     */
    @Test
    public void testSearchByName() throws Exception {
           //create a clothoUser and Login to Query
            String searchType = "STARTSWITH";
            String productName = "Tel";
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            ClothoAdapter.QueryMode Qmode = ClothoAdapter.QueryMode.valueOf(searchType);
            Map loginMap = new HashMap();
            loginMap.put("username", "phagebook");
            loginMap.put("credentials", "backend");
            clothoObject.login(loginMap);

            //Query for the products
            
             Map query = new HashMap();
            
            switch (Qmode){
                case EXACT:
                    query.put("name", productName);
                    break;
                case STARTSWITH:
                     query.put("query", productName); // the value for which we are querying.
                     query.put("key", "name"); // the key of the object we are querying
       
                    break;
                default:
                    break;
                    
            }
            
            List<Product> queryProductResults = ClothoAdapter.queryProduct(query, clothoObject, ClothoAdapter.QueryMode.valueOf(searchType));

            //To get Vendor Name...
            JSONArray results = new JSONArray();
            for (Product product : queryProductResults){
                JSONObject productAsJson = new JSONObject();
                productAsJson.put("clothoID", product.getId());
                productAsJson.put("cost", product.getCost());
                productAsJson.put("productURL", (product.getProductURL() != null) ? product.getProductURL() : "");
                productAsJson.put("goodType", product.getGoodType());
                productAsJson.put("inventory", product.getInventory());
                productAsJson.put("name", product.getName());
                productAsJson.put("description", product.getDescription());
                productAsJson.put("vendor", ClothoAdapter.getVendor(product.getCompanyId(), clothoObject));
                
                results.add(productAsJson);
            }
    }

    
    
}
