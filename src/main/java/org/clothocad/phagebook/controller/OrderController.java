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
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.dom.Vendor;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderColumns;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;

//import java.util.List;
//import org.clothocad.phagebook.dom.Product;
/**
 *
 * @author innaturshudzhyan
 */
public class OrderController {

    public static List<Product> getProducts(JSONArray list,Clotho clothoObject) {
        List<Product> products = new ArrayList<Product>();
        for (int i = 0; i < (list.length()); i++) {

            JSONArray productList = new JSONArray();
            productList = (JSONArray) list.get(i);
            if(productList.length() != 6){
                continue;
            }
            String productName = "";
            String description = "";
            String url = "";
            String companyName = "";
            String type = "";
            String price = "";
            for (int j = 0; j < productList.length(); j++) {
                productName = (String) productList.get(0);
                description = (String) productList.get(1);
                url = (String) productList.get(2);
                companyName = (String) productList.get(3);
                type = (String) productList.get(4);
                price = (String) productList.get(5);

            }
            Vendor company; 
            Map companyQuery = new HashMap();
            companyQuery.put("name",companyName);
            List<Vendor> companyList = ClothoAdapter.queryVendor(companyQuery, clothoObject, ClothoAdapter.QueryMode.EXACT);
            if(companyList.isEmpty()){
                company = new Vendor(companyName);
                ClothoAdapter.createVendor(company, clothoObject);
            }
            else{
               company = companyList.get(0);
            }
                
            Product product = new Product(productName, company.getId(), Double.valueOf(price));

            product.setProductURL(url);
            product.setGoodType(GoodType.valueOf(type));
            product.setDescription(description);
            products.add(product);
        }
        return products;
    }

    public static List<Vendor> getCompanies(JSONArray list,Clotho clothoObject) {
        List<Vendor> companies = new ArrayList<Vendor>();
        for (int i = 0; i < (list.length()); i++) {

            JSONArray companyList = new JSONArray();
            companyList = (JSONArray) list.get(i);
            if(companyList.length() != 5){
                continue;
            }
            String companyName = "";
            String description = "";
            String phone = "";
            String url = "";
            String contact = "";
            
            for (int j = 0; j < companyList.length(); j++) {
                companyName = (String) companyList.get(0);
                description = (String) companyList.get(1);
                phone = (String) companyList.get(2);
                url = (String) companyList.get(3);
                contact = (String) companyList.get(4);
                
            }
            
                Vendor company = new Vendor(companyName);

                Map companyQuery = new HashMap();
                companyQuery.put("name",companyName);
                List<Vendor> companyArray = ClothoAdapter.queryVendor(companyQuery, clothoObject, ClothoAdapter.QueryMode.EXACT);
                if(companyArray.isEmpty()){
                    company = new Vendor(companyName);
                    ClothoAdapter.createVendor(company, clothoObject);
                }
                else{
                   company = companyArray.get(0);
                }
                
                company.setDescription(description);
                company.setPhone(phone);
                company.setUrl(url);
                company.setContact(contact);

                companies.add(company);
             
        }
        return companies;
    }

    
    
    public static List<Product> getProducts(String filename) {

        BufferedReader br = null;
        List<String> csvLines = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(filename));
            String line = "";
            while ((line = br.readLine()) != null) {
                csvLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return getProducts(csvLines);
    }

    public static List<Product> getProducts(List<String> csvLines) {
        List<Product> products = new ArrayList<Product>();
        for (String line : csvLines) {//for each line (of DataType String) in csvLines (a list of Strings)
            String[] pieces = line.split(",");

            Vendor company1 = new Vendor(pieces[3]);
            Product product = new Product(pieces[0], company1.getId(), Double.parseDouble(pieces[5]));

            product.setDescription(pieces[1]);
            product.setProductURL(pieces[2]);
            product.setGoodType(GoodType.valueOf(pieces[4]));

            products.add(product);
        }
        return products;
    }

    public static List<Vendor> getCompanies(String filename) {

        BufferedReader br = null;
        List<String> csvLines = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(filename));
            String line = "";
            while ((line = br.readLine()) != null) {

                csvLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return getCompanies(csvLines);
    }

    public static List<Vendor> getCompanies(List<String> csvLines) {
        List<Vendor> companies = new ArrayList<Vendor>();
        for (String line : csvLines) {//for each line (of DataType String) in csvLines (a list of Strings)
            String[] pieces = line.split(",");

            Vendor company = new Vendor(pieces[0]);

            company.setDescription(pieces[1]);
            company.setPhone(pieces[2]);
            company.setUrl(pieces[3]);
            company.setContact(pieces[4]);

            companies.add(company);
        }
        return companies;
    }

    public static double getTotalPrice(Order order) {
        double total = 0.0;

        Iterator it = order.getProducts().entrySet().iterator();    
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Product product = (Product) pair.getKey();
            total = +product.getCost();
        }

        return total;
    }

    //inputs: order and list of enums
    //return a list of strings
    public static List<String> createOrderForm(Order order, List<OrderColumns> ColumnList) {
        List<String> orders = new ArrayList<String>();
        String orderString;
        System.out.println("HERE IN ORDERFORM");
        int count = 1;       
        System.out.println("The products= " + order.getProducts().toString());
        Iterator it = order.getProducts().entrySet().iterator();   
        
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            orderString = "";
            Product product = (Product) pair.getKey();
            Clotho clothoObject = ClothoAdapter.clothoObject;
            ClothoAdapter.setUpRandomUser();
            
            Vendor company = ClothoAdapter.getVendor(product.getCompanyId(), clothoObject);
            ClothoAdapter.clothoObject.logout();
            for (OrderColumns clist1 : ColumnList) {
                switch (clist1) {
                    case SERIAL_NUMBER:
                        orderString = orderString + count + ",";
                        count++;
                        break;
                    case PRODUCT_NAME:
                        orderString = orderString + product.getName() + ",";
                        break;                    
                    case PRODUCT_URL:
                        orderString = orderString + product.getProductURL() + ",";
                        break;
                    case PRODUCT_DESCRIPTION:
                        orderString = orderString + product.getDescription() + ",";
                        break;
                    case QUANTITY:
                        orderString = orderString + pair.getValue() + ",";
                        break;
                    case COMPANY_NAME:
                        orderString = orderString + company.getName() + ",";
                        break;
                    case COMPANY_URL:
                        orderString = orderString + company.getUrl() + ",";
                        break;
                    case COMPANY_DESCRIPTION:
                        orderString = orderString + company.getDescription() + ",";
                        break;
                    case COMPANY_CONTACT:
                        orderString = orderString + company.getContact() + ",";
                        break;
                    case COMPANY_PHONE:
                        orderString = orderString + company.getPhone() + ",";
                        break;
                    case UNIT_PRICE:
                        orderString = orderString + product.getCost() + ",";
                        break;
                    case TOTAL_PRICE:
                        orderString = orderString + (product.getCost() * (int)pair.getValue()) + ",";
                        break;
                }
            }
            orders.add(orderString);
            it.remove();
        }
        System.out.println(orders);
        return orders;
    }

}