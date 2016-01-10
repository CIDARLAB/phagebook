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
    
    @Getter @Setter private String   productURL;
    @Getter @Setter private Company  company;
    @Getter @Setter private GoodType goodType;
    @Getter @Setter private double   cost;
    @Getter @Setter private int      quantity;

    
    public Product(){
        super();
        this.productURL = "Not Set";
        this.company    = new Company();
        this.goodType   = GoodType.INSTRUMENT;
        this.cost       = 0.0d;
        this.quantity   = 0;
    }
    /**
     *
     * @param name
     * @param company
     * @param cost
     */
    public Product(String name, Company company,double cost){
        super(name,"");
        this.company = company;
        this.cost = cost;
    }
    
    public Product(String name, Company company,GoodType goodType, double cost){
        super(name,"");
        this.company = company;
        this.goodType = goodType;
        this.cost = cost;
        
    }
    
    
}
