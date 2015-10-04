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
import org.clothocad.phagebook.dom.Product;

//import java.util.List;
//import org.clothocad.phagebook.dom.Product;

/**
 *
 * @author innaturshudzhyan
 */
public class OrderController {
    
    
    public static List<Product> getProducts(String filename){
        
        BufferedReader br = null;
        List<String> csvLines = new ArrayList<String>();
        try{
                br = new BufferedReader(new FileReader(filename));
                String line = "";
                while ((line = br.readLine()) != null) {
                    csvLines.add(line);
                }  
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
            if (br != null) {
                try {
                    br.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return getProducts(csvLines);
    }
    
    public static List<Product> getProducts(List<String> csvLines){
        List<Product> products = new ArrayList<Product>();
        for(String line:csvLines){//for each line (of DataType String) in csvLines (a list of Strings)
            String[] pieces = line.split(","); 
                
                Product product = new Product(pieces[0]);
                product.setDescription(pieces[1]);
                product.setProductURL(pieces[2]);
                
                Company company1 = new Company(pieces[3]); 
                product.setCompany(company1);
                
                product.setGoodType(GoodType.valueOf(pieces[4]));
                product.setPrice(Double.parseDouble(pieces[5]));
                
                products.add(product);
        }
        return products;
    }
    
    public static List<Company> getCompanies(String filename){
        List<Company> companies = new ArrayList<Company>();
        
        return companies;
    }
    
    public static List<Company> getCompanies(List<String> csvLines){
        List<Company> companies = new ArrayList<Company>();
        
        return companies;
    }
    
    //funciton htat returns a list of products called getProducts
    //and the input argument will be a file
    //another function - argument will be a list of strings, callsed the same thing - return all lists of products
    // 
}


