/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author innaturshudzhyan
 */
public class Product extends Good{
    
    @Getter
    @Setter
    private String productURL;
    
    @Getter
    @Setter
    private Company company;
    
    @Getter
    @Setter
    private GoodType goodType;
    
    @Getter
    @Setter
    private float price;

    /**
     *
     * @param name
     */
    public Product(String name) {
        super(name);
    }
    
    public Product(String name, Company company){
        super(name);
        this.company = company;
    }
    
    public Product(String name, Company company,GoodType goodType, float price){
        super(name);
        this.company = company;
        this.goodType = goodType;
        this.price = price;
    }
    
}
