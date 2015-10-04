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
    
    
    public static void main(String[] args)
    {
           Product p = run("/home/prash/cidar/template.csv");
           System.out.println("Product Name :: " + p.getName());
           System.out.println("Product Description ::"+p.getDescription());
    
    }
    
    /**
     *
     * @param csvFile
     */
    public static Product run(String csvFile){
        
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Product product1 = null;
        try{
            
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {
                
                String[] product = line.split(cvsSplitBy); 
                
                product1 = new Product(product[0]);
                product1.setDescription(product[1]);
                product1.setProductURL(product[2]);
                
                Company company1 = new Company(product[3]); 
                product1.setCompany(company1);
                
                product1.setGoodType(GoodType.valueOf(product[4]));
                product1.setPrice(Double.parseDouble(product[5]));
                
                System.out.println(product[0] + ", " 
                    + product[1] + ", " + product[2] + ", " 
                    + product[3] + ", " + product[4] + ", " + product[5]);
                
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
        return product1;
    }
    
    //funciton htat returns a list of products called getProducts
    //and the input argument will be a file
    //another function - argument will be a list of strings, callsed the same thing - return all lists of products
    // 
}


