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
import java.util.List;
import org.clothocad.phagebook.dom.Company;
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

    public static List<Product> getProducts(JSONArray list) {
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
            Company company = new Company(companyName);
            Product product = new Product(productName, company, Double.valueOf(price));

            product.setProductURL(url);
            product.setGoodType(GoodType.valueOf(type));
            product.setDescription(description);
            products.add(product);
        }
        return products;
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

            Company company1 = new Company(pieces[3]);
            Product product = new Product(pieces[0], company1, Double.parseDouble(pieces[5]));

            product.setDescription(pieces[1]);
            product.setProductURL(pieces[2]);
            product.setGoodType(GoodType.valueOf(pieces[4]));

            products.add(product);
        }
        return products;
    }

    public static List<Company> getCompanies(String filename) {

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

    public static List<Company> getCompanies(List<String> csvLines) {
        List<Company> companies = new ArrayList<Company>();
        for (String line : csvLines) {//for each line (of DataType String) in csvLines (a list of Strings)
            String[] pieces = line.split(",");

            Company company = new Company(pieces[0]);

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

        for (Product product : order.getProducts()) {
            total = +product.getCost();
        }

        return total;
    }

    //inputs: order and list of enums
    //return a list of strings
    public static List<String> createOrder(Order order, List<OrderColumns> ColumnList) {
        List<String> orders = new ArrayList<String>();
        String orderString;
        int count = 1;

        for (Product product : order.getProducts()) {
            orderString = "";

            for (OrderColumns clist1 : ColumnList) {
                switch (clist1) {
                    case SERIAL_NUMBER:
                        orderString += count;
                        count++;
                        break;
                    case PRODUCT_NAME:
                        orderString += product.getName();
                        break;
                    case PRODUCT_URL:
                        orderString += product.getProductURL();
                        break;
                    case PRODUCT_DESCRIPTION:
                        orderString += product.getDescription();
                        break;
                    case QUANTITY:
                        orderString += product.getQuantity();
                        break;
                    case COMPANY_NAME:
                        orderString += product.getCompany().getName();
                        break;
                    case COMPANY_URL:
                        orderString += product.getCompany().getUrl();
                        break;
                    case COMPANY_DESCRIPTION:
                        orderString += product.getCompany().getDescription();
                        break;
                    case COMPANY_CONTACT:
                        orderString += product.getCompany().getContact();
                        break;
                    case COMPANY_PHONE:
                        orderString += product.getCompany().getPhone();
                        break;
                    case UNIT_PRICE:
                        orderString += product.getCost();
                        break;
                    case TOTAL_PRICE:
                        orderString += (product.getCost() * product.getQuantity());
                        break;
                }
            }
            orders.add(orderString);
        }
        return orders;
    }

}
