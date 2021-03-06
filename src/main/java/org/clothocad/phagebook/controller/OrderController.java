/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.dom.CartItem;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
import org.json.JSONObject;

//import java.util.List;
//import org.clothocad.phagebook.dom.Product;
/**
 *
 * @author innaturshudzhyan
 */
public class OrderController {
    
    public static List<String> getProducts(JSONArray list,Clotho clothoObject) {
        List<String> productIds = new ArrayList<String>();
        
        for (int i = 0; i < (list.length()); i++) {
                
                JSONObject obj = new JSONObject();
                obj = (JSONObject)list.get(i);
                if(!obj.has("URL") || !obj.has("Company Name") || !obj.has("Type") || !obj.has("Cost") || !obj.has("Quantity") || !obj.has("Name") || !obj.has("Description")){
                    continue;
                }
                
                String productUrl = list.getJSONObject(i).get("URL").toString();
                String companyName = list.getJSONObject(i).get("Company Name").toString();
                String goodType = list.getJSONObject(i).get("Type").toString();
                String cost = list.getJSONObject(i).get("Cost").toString();
                String quantity = list.getJSONObject(i).get("Quantity").toString();
                String name = list.getJSONObject(i).get("Name").toString();
                String description = list.getJSONObject(i).get("Description").toString();

                Vendor company; 
                Map companyQuery = new HashMap();
                companyQuery.put("name",companyName);
                List<Vendor> companyList = ClothoAdapter.queryVendor(companyQuery, clothoObject, ClothoAdapter.QueryMode.EXACT);
                if(companyList.isEmpty()){
                    company = new Vendor(companyName);
                    String companyId = ClothoAdapter.createVendor(company, clothoObject);
                }
                else{
                   company = companyList.get(0);
                }

                Product product = new Product(name, company.getId(), Double.valueOf(cost));

                product.setProductURL(productUrl);
                product.setGoodType(GoodType.valueOf(goodType));
                product.setDescription(description);

                String id = ClothoAdapter.createProduct(product, clothoObject);

                productIds.add(id);
            
        }
        return productIds;
    }

    public static List<String> getVendors(JSONArray list,Clotho clothoObject) {
        List<String> companiesIds = new ArrayList<String>();
        for (int i = 0; i < (list.length()); i++) {
            
            JSONObject obj = new JSONObject();
            obj = (JSONObject)list.get(i);
            if(!obj.has("Name") || !obj.has("Description") || !obj.has("Contact") || !obj.has("Phone") || !obj.has("URL")){
                continue;
            }
                
            String companyName = list.getJSONObject(i).get("Name").toString();
            String description = list.getJSONObject(i).get("Description").toString();
            //System.out.println("i = " + i+" description = " + description);
            String contact = list.getJSONObject(i).get("Contact").toString();
            String phone = list.getJSONObject(i).get("Phone").toString();
            String url = list.getJSONObject(i).get("URL").toString();

            Vendor company = new Vendor(companyName);

            Map companyQuery = new HashMap();
            companyQuery.put("name",companyName);
                
            List<Vendor> companyArray = ClothoAdapter.queryVendor(companyQuery, clothoObject, ClothoAdapter.QueryMode.EXACT);
            if(companyArray.isEmpty()){
                System.out.println("vendor doesn't exist");
                company = new Vendor(companyName);
                company.setDescription(description);
                company.setContact(contact);
                company.setPhone(phone);
                company.setUrl(url);
                String id = ClothoAdapter.createVendor(company, clothoObject);
                System.out.println("id = "+ i + " = "+ id);
                companiesIds.add(id);
                }
                else{
                System.out.println("vendor already exists");
                   company = companyArray.get(0);
                }            
        }
        return companiesIds;
    }

//    public static List<Product> getProducts(String filename) {
//
//        BufferedReader br = null;
//        List<String> csvLines = new ArrayList<String>();
//        try {
//            br = new BufferedReader(new FileReader(filename));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                csvLines.add(line);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return getProducts(csvLines);
//    }
//
//    public static List<Product> getProducts(List<String> csvLines) {
//        List<Product> products = new ArrayList<Product>();
//        for (String line : csvLines) {//for each line (of DataType String) in csvLines (a list of Strings)
//            String[] pieces = line.split(",");
//
//            Vendor company1 = new Vendor(pieces[3]);
//            Product product = new Product(pieces[0], company1.getId(), Double.parseDouble(pieces[5]));
//
//            product.setDescription(pieces[1]);
//            product.setProductURL(pieces[2]);
//            product.setGoodType(GoodType.valueOf(pieces[4]));
//
//            products.add(product);
//        }
//        return products;
//    }
//
//    //input a file path
//    public static List<JSONObject> getVendors(String filename) {
//        
//        BufferedReader br = null;
//        List<String> csvLines = new ArrayList<String>();
//        try {
//            br = new BufferedReader(new FileReader(filename));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                csvLines.add(line);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return getVendors(csvLines);
//    }
//    //returns a list of vendors
//    public static List<JSONObject> getVendors(List<String> csvLines) {
//        List<Vendor> companies = new ArrayList<Vendor>();
//        List<JSONObject> JSONCompanies = new ArrayList<JSONObject>();
//        
//        for (String line : csvLines) {//for each line (of DataType String) in csvLines (a list of Strings)
//            String[] pieces = line.split(",");
//            if (!"Name".equals(pieces[0]))
//            {
//                Vendor company = new Vendor(pieces[0]);
//                company.setDescription(pieces[1]);
//                company.setContact(pieces[2]);
//                company.setPhone(pieces[3]);
//                company.setUrl(pieces[4]);
//                companies.add(company);
//                
//                JSONObject JSONCompany = new JSONObject();
//                JSONCompany.put("name",company.getName());
//                JSONCompany.put("description",company.getDescription());
//                JSONCompany.put("contact",company.getContact());
//                JSONCompany.put("phone",company.getPhone());
//                JSONCompany.put("url",company.getUrl());
//                JSONCompanies.add(JSONCompany);
//            }
//            
//        }
//        return JSONCompanies;
//    }

    public static double getTotalPrice(Order order) {
        double total = 0.0;

        //Iterator it = order.getProducts().entrySet().iterator();    
        //while (it.hasNext()) {
        //    Map.Entry pair = (Map.Entry)it.next();
        //    Product product = (Product) pair.getKey();
        //    total = +product.getCost();
        //}

        return total;
    }

    //inputs: order and list of enums
    //return a list of strings
    public static List<String> createOrderForm(Order order, List<Order.OrderColumns> ColumnList) {
        
        List<String> orders = new ArrayList<String>();
        String header = "";
        int headerCount = 0; //to make sure the header only appears once
        
        //System.out.println("HERE IN ORDERFORM");
        int count = 1;       
        //System.out.println("The products= " + order.getProducts().toString());
        List<String>  cartItemIds = order.getProducts();
        ClothoConnection conn = new ClothoConnection (Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        List<CartItem> cartItems = new ArrayList<>();
        for (int i = 0; i <cartItemIds.size() ; i++ ){
                cartItems.add(ClothoAdapter.getCartItem(cartItemIds.get(i), clothoObject));
        }
        
        
        for (CartItem cartItem : cartItems ){
            String orderString = "";
            Product product = ClothoAdapter.getProduct(cartItem.getProductId(), clothoObject);
            Vendor vendor = ClothoAdapter.getVendor(product.getCompanyId(), clothoObject);
            
            for (Order.OrderColumns clist1 : ColumnList) {
                switch (clist1) {
                    case SERIAL_NUMBER:
                        orderString = orderString + count + ",";
                        header = header + ",";
                        count++;
                        break;
                    case PRODUCT_NAME:
                        orderString = orderString + product.getName() + ",";
                        header = header + "ITEM,";
                        break;                    
                    case PRODUCT_URL:
                        orderString = orderString + product.getProductURL() + ",";
                        header = header + "URL,";
                        break;
                    case PRODUCT_DESCRIPTION:
                        orderString = orderString + product.getDescription() + ",";
                        header = header + "DESCRIPTION,";
                        break;
                    case QUANTITY:
                        orderString = orderString + cartItem.getQuantity() + ",";
                        header = header + "QUANTITY,";
                        break;
                    case COMPANY_NAME:
                        orderString = orderString + vendor.getName() + ",";
                        header = header + "COMPANY NAME,";
                        break;
                    case COMPANY_URL:
                        orderString = orderString + vendor.getUrl() + ",";
                        header = header + "COMPANY URL,";
                        break;
                    case COMPANY_DESCRIPTION:
                        orderString = orderString + vendor.getDescription() + ",";
                        header = header + "COMPANY DESCRIPTION,";
                        break;
                    case COMPANY_CONTACT:
                        orderString = orderString + vendor.getContact() + ",";
                        header = header + "CONTACT,";
                        break;
                    case COMPANY_PHONE:
                        orderString = orderString + vendor.getPhone() + ",";
                        header = header + "PHONE NUMBER,";
                        break;
                    case UNIT_PRICE:
                        orderString = orderString + product.getCost() + ",";
                        header = header + "UNIT PRICE,";
                        break;
                    case CUSTOM_UNIT_PRICE:
                        orderString = orderString + (product.getCost() * cartItem.getDiscount())/100.0d + ",";
                        header = header + "CUSTOM UNIT PRICE,";
                        break;
                    case TOTAL_PRICE: 
                        orderString = orderString + (product.getCost() * cartItem.getDiscount())/100.0d *  cartItem.getQuantity() + ",";

                        header = header + "TOTAL PRICE,";
                        break;
                }
            }
            //orderString = orderString.substring(0, orderString.length()-1) + "\n" + "new line here";
            if (headerCount == 0)
            {
                orders.add(header.substring(0, header.length()-1) + "\n");
                headerCount ++;
            }
                
            orders.add(orderString.substring(0, orderString.length()-1) + "\n");
        }
        
        //ClothoAdapter.clothoObject.logout();
            
        return orders;
    }
    
//    public static void main(String[] args) {
////        JSONArray arrV = new JSONArray();
////        JSONObject ven1 = new JSONObject();
////        JSONObject ven2 = new JSONObject();
////        ven1.put("Name", "Apple");
////        ven1.put("Description", "Prashant's Future Company");
////        ven1.put("Contact", "Meh");
////        ven1.put("Phone", "7778887878");
////        ven1.put("URL", "www.banana.com");
////        
////        ven2.put("Name", "Microsoft");
////        ven2.put("Description", "Best company in the world");
////        ven2.put("Contact", "Inna");
////        ven2.put("Phone", "6178179898");
////        ven2.put("URL", "www.microsoft.com");
////        
////        arrV.put(ven1);
////        arrV.put(ven2);
////        
////        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
////        Clotho clothoObject = new Clotho(conn);
////        
////        String username = "innatur@bu.edu";
////        String password = "12345";
////        
////        clothoObject.login(username, password);
////        
////        List<String> vendorsIds = new ArrayList<String>();
////        vendorsIds = getVendors(arrV, clothoObject);
//        
//        JSONArray arrC = new JSONArray();
//        JSONObject pr1 = new JSONObject();
//        JSONObject pr2 = new JSONObject();
//        
//        pr1.put("URL", "plate.com");
//        pr1.put("Company Name", "Apple");
//        pr1.put("Type", GoodType.INSTRUMENT.toString());
//        pr1.put("Cost", 123);
//        pr1.put("Quantity", 1);
//        pr1.put("Name", "plate");
//        pr1.put("Description", "plate");
//        
//        pr2.put("URL", "plate1.com");
//        pr2.put("Company Name", "Apple1");
//        pr2.put("Type", GoodType.INSTRUMENT.toString());
//        pr2.put("Cost", 1231);
//        pr2.put("Quantity", 1);
//        pr2.put("Name", "plate1");
//        pr2.put("Description", "plate1");
//        
//        arrC.put(pr1);
//        arrC.put(pr2);
//        
//        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
//        Clotho clothoObject = new Clotho(conn);
//        
//        String username = "innatur@bu.edu";
//        String password = "12345";
//        
//        clothoObject.login(username, password);
//        
//        List<String> productsIds = new ArrayList<String>();
//        productsIds = getProducts(arrC, clothoObject);
//        
//        conn.closeConnection();
//        
//    }

}