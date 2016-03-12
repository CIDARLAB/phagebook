/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author innaturshudzhyan
 */
public class Product extends Good{
    
    @Getter @Setter private String   productURL;
    @Getter @Setter private String   companyId;
    
    @Getter @Setter private GoodType goodType;
    @Getter @Setter private Double   cost;
    //Look into a static member of this class that each instance can access..
    //best way...
    
    @Getter @Setter private Integer  inventory;
    @Getter @Setter private Double   unitPrice;

    
    public Product(){
        super();
        this.productURL = "Not Set";
        this.companyId  = "Not Set";
        this.goodType   = GoodType.INSTRUMENT;
        this.cost       = 0.0d;
        this.inventory  = 0;
        this.unitPrice  = 0.0d;
    }
    /**
     *
     * @param name
     * @param company
     * @param cost
     */
    public Product(String name, String company,double cost){
        super(name,"");
        this.companyId = company;
        this.cost = cost;
    }
    
    public Product(String name, String company,GoodType goodType, double cost){
        super(name,"");
        this.companyId = company;
        this.goodType = goodType;
        this.cost = cost;
   
        
    }
    public int decreaseInventory(){
        this.inventory--;
        return this.inventory;
    }
    
    public int decreaseInventory(int n){
        this.inventory = this.inventory - n;
        return this.inventory;
    }
    
    public int increaseInventory(){
        this.inventory++;
        return this.inventory;
    }
    public int increaseInventory(int n){
        this.inventory = this.inventory + n;
        return this.inventory;
    }
//    @Override
//    public boolean equals(Object other){
//    if (other == null) return false;
//    if (other == this) return true;
//    if (!(other instanceof Product))return false;
//    Product product = (Product) other;
//    return product.getId().equals(this.getId());
//    }
    @Override
    public boolean equals(Object o){
      if (o == null) return false;
      if (o == this) return true;
      if (! (o instanceof Product))return false;
      Product product = (Product) o;
      return product.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.productURL);
        hash = 97 * hash + Objects.hashCode(this.companyId);
        hash = 97 * hash + Objects.hashCode(this.goodType);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.cost) ^ (Double.doubleToLongBits(this.cost) >>> 32));
        hash = 97 * hash + this.inventory;
        return hash;
    }
    
    
    
   
    
    
    
}
