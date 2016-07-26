/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;

/**
 *
 * @author jacob
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Vendor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @RequestMapping(value = "addProducts", method = RequestMethod.GET)
    public void addProducts(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        System.out.println("addProducts");

        String companyName = params.get("CompanyName");

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

        Map companyMap = new HashMap();
        companyMap.put("name", companyName);
        //System.out.println("Vendor Name :: " + companyName);
        if (!ClothoAdapter.queryVendor(companyMap, clothoObject, ClothoAdapter.QueryMode.EXACT).isEmpty()) {
            writer.println(companyName);
        }

        conn.closeConnection();

        writer.flush();
        writer.close();

    }

    @RequestMapping(value = "createProduct", method = RequestMethod.POST)
    protected void createProduct(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        //TODO: we need to have an authentication token at some point

        String username = "phagebook";
        String password = "backend";
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        Object pProductUrl = params.get("productUrl");
        String productUrl = pProductUrl != null ? (String) pProductUrl : "";

        Object pCompanyId = params.get("company");
        String companyId = pCompanyId != null ? (String) pCompanyId : "";

        Object pGoodType = params.get("goodType");
        String goodType = pGoodType != null ? (String) pGoodType : "";

        Object pCost = params.get("cost");
        Double cost = pCost != null ? Double.parseDouble((String) pCost) : -1.0d;

        Object pQuantity = params.get("quantity");
        Integer quantity = pQuantity != null ? Integer.parseInt((String) pQuantity) : -1;

        Object pName = params.get("name");
        String name = pName != null ? (String) pName : "";

        Object pDescription = params.get("description");
        String description = pDescription != null ? (String) pDescription : "";

        Vendor comp = new Vendor();
        boolean isValidRequest = false;
        if ((!name.isEmpty() && (cost > 0) && (quantity > 0) && !companyId.isEmpty())) {

            isValidRequest = true;
            comp = ClothoAdapter.getVendor(companyId, clothoObject);
            if (comp.getId().equals("")) {
                isValidRequest = false;
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("message", "No Company with that id exists");
                out.print(responseJSON.toString());
                out.flush();
                out.close();
                clothoObject.logout();

                return;
            }
        }

        if (isValidRequest) {
            Product prod = new Product();
            prod.setName(name);
            prod.setCost(cost);
            prod.setInventory(quantity);
            prod.setCompanyId(comp.getId());
            prod.setDescription(description);
            prod.setProductURL(productUrl);
            prod.setGoodType(GoodType.valueOf((!goodType.equals("")) ? goodType : "INSTRUMENT"));

            //everything is set for that product
            ClothoAdapter.createProduct(prod, clothoObject);
            JSONObject product = new JSONObject();
            product.put("id", prod.getId());
            product.put("name", prod.getName());
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(product);
            out.flush();
            out.close();

            clothoObject.logout();
            conn.closeConnection();

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @RequestMapping(value = "uploadProductCSV", method = RequestMethod.POST)
    public void uploadProductCSV(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {

        JSONArray arr = new JSONArray(params.get("jsonArray"));

        List<String> productIds = new ArrayList<String>();
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);

        String username = "phagebook";
        String password = "backend";
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        productIds = org.clothocad.phagebook.controller.OrderController.getProducts(arr, clothoObject);

        PrintWriter writer = response.getWriter();
        conn.closeConnection();
        writer.println("created " + productIds);
        writer.flush();
        writer.close();

    }

    @RequestMapping(value = "getProductById", method = RequestMethod.GET)
    protected void doGetgetProductById(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        String ids = params.get("ids");

        boolean isValidRequest = false;
        if (ids != "" && ids != null) {
            isValidRequest = true;
        }
        // items = [565f5518d4c61fb21a163eac, 565f5518d4c61fb21a163eaa, 565f5518d4c61fb21a163eab, 565f5518d4c61fb21a163eac]

        //ESTABLISH CONNECTION
        if (isValidRequest) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "test" + System.currentTimeMillis();
            createUserMap.put("username", username);
            createUserMap.put("password", "password");
            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", "password");
            clothoObject.login(loginMap);
            //
            List<String> productsAsStrings = Arrays.asList(ids.split("\\s*,\\s*"));
            System.out.println(productsAsStrings);
            List<Product> products = new LinkedList<>();

            //Populate the number of Products in a JSONArray
            net.sf.json.JSONArray responseData = new net.sf.json.JSONArray();

            for (int i = 0; i < productsAsStrings.size(); i++) {
                JSONObject product = new JSONObject();
                JSONObject productAtIndex = new JSONObject();
                Product temp = ClothoAdapter.getProduct(productsAsStrings.get(i), clothoObject);
                productAtIndex.put("clothoID", temp.getId());
                productAtIndex.put("name", temp.getName());
                Vendor comp = ClothoAdapter.getVendor(temp.getCompanyId(), clothoObject);
                productAtIndex.put("company", comp.getName());
                productAtIndex.put("cost", temp.getCost());
                productAtIndex.put("description", temp.getDescription());
                productAtIndex.put("goodType", temp.getGoodType());
                productAtIndex.put("url", temp.getProductURL());
                productAtIndex.put("quantity", temp.getInventory());

                product.put("product" + i, productAtIndex);

                responseData.add(i, temp);
                System.out.println(responseData.toString());
                for (int j = 0; j < responseData.size(); j++) {
                    System.out.println(responseData.get(i));
                }

            }

            if (!responseData.isEmpty()) {

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(responseData.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            conn.closeConnection();
        }

    }

    @RequestMapping(value = "queryProductByCompany", method = RequestMethod.GET)
    protected void queryProductByCompany(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pCompanyName = params.get("name");
        String companyName = pCompanyName != null ? (String) pCompanyName : "";

        Object pSearchType = params.get("searchType");
        String searchType = pSearchType != null ? (String) pSearchType : "";

        boolean isValidRequest = false;
        if (!companyName.equals("") && !searchType.equals("")) {

            isValidRequest = true;
        }

        if (isValidRequest) {
            //create a clothoUser and Login to Query
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            ClothoAdapter.QueryMode Qmode = ClothoAdapter.QueryMode.valueOf(searchType);

            Map loginMap = new HashMap();

            loginMap.put("username", "phagebook");
            loginMap.put("credentials", "backend");
            clothoObject.login(loginMap);
            //Query for the company

            Map query = new HashMap();

            switch (Qmode) {
                case EXACT:
                    query.put("name", companyName);
                    break;
                case STARTSWITH:
                    query.put("query", companyName); // the value for which we are querying.
                    query.put("key", "name"); // the key of the object we are querying

                    break;
                default:
                    break;

            }

            List<Vendor> queryCompanyResults = ClothoAdapter.queryVendor(query, clothoObject, ClothoAdapter.QueryMode.valueOf(searchType));

            //To get Vendor Name and ID to query for products with that company...
            List<String> companyIDs = new LinkedList<>();
            for (Vendor company : queryCompanyResults) {
                companyIDs.add(company.getId());
            }

            net.sf.json.JSONArray results = new net.sf.json.JSONArray();
            for (String companyID : companyIDs) {
                Map queryForClotho = new HashMap();
                queryForClotho.put("company", companyID);
                List<Product> queryProductResults = ClothoAdapter.queryProduct(queryForClotho, clothoObject, ClothoAdapter.QueryMode.EXACT);

                for (Product product : queryProductResults) {
                    net.sf.json.JSONObject productAsJson = new net.sf.json.JSONObject();
                    productAsJson.put("clothoID", product.getId());
                    productAsJson.put("unitPrice", product.getCost());
                    productAsJson.put("productURL", (product.getProductURL() != null) ? product.getProductURL() : "");
                    productAsJson.put("goodType", product.getGoodType());
                    productAsJson.put("inventory", product.getInventory());
                    productAsJson.put("name", product.getName());
                    productAsJson.put("description", product.getDescription());
                    productAsJson.put("vendor", ClothoAdapter.getVendor(product.getCompanyId(), clothoObject));
                    results.add(productAsJson);
                }
            }

            if (!results.isEmpty()) {

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(results.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @RequestMapping(value = "queryProductByName", method = RequestMethod.POST)
    protected void doGet(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        Object pProductName = params.get("name");
        String productName = pProductName != null ? (String) pProductName : "";

        Object pSearchType = params.get("searchType");
        String searchType = pSearchType != null ? (String) pSearchType : "";

        boolean isValidRequest = false;
        if (!productName.equals("") && !productName.equals("")) {
            isValidRequest = true;
        }

        if (isValidRequest) {

            //create a clothoUser and Login to Query
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            ClothoAdapter.QueryMode Qmode = ClothoAdapter.QueryMode.valueOf(searchType);
            Map loginMap = new HashMap();
            loginMap.put("username", "phagebook");
            loginMap.put("credentials", "backend");
            clothoObject.login(loginMap);

            //Query for the products
            Map query = new HashMap();

            switch (Qmode) {
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
            System.out.println("search type: " + searchType);

            List<Product> queryProductResults = ClothoAdapter.queryProduct(query, clothoObject, ClothoAdapter.QueryMode.valueOf(searchType));

            //To get Vendor Name...
            net.sf.json.JSONArray results = new net.sf.json.JSONArray();
            for (Product product : queryProductResults) {
                net.sf.json.JSONObject productAsJson = new net.sf.json.JSONObject();
                System.out.println(product.getName());
                productAsJson.put("clothoID", product.getId());
                productAsJson.put("unitPrice", product.getCost());
                productAsJson.put("productURL", (product.getProductURL() != null) ? product.getProductURL() : "");
                productAsJson.put("goodType", product.getGoodType());
                productAsJson.put("inventory", product.getInventory());
                productAsJson.put("name", product.getName());
                productAsJson.put("description", product.getDescription());
                productAsJson.put("vendor", ClothoAdapter.getVendor(product.getCompanyId(), clothoObject));

                results.add(productAsJson);
            }

            if (!results.isEmpty()) {

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(results.toString());
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                net.sf.json.JSONObject responseJSON = new net.sf.json.JSONObject();
                responseJSON.put("message", "no results found at all");
                out.print(responseJSON);
                out.flush();
            }
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    
    
    

    
}
