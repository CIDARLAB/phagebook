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
    @Getter @Setter private Vendor   company;
    @Getter @Setter private GoodType goodType;
    @Getter @Setter private double   cost;
    @Getter @Setter private int      quantity;
    @Getter @Setter private double   unitPrice;

    
    public Product(){
        super();
        this.productURL = "Not Set";
        this.company    = new Vendor();
        this.goodType   = GoodType.INSTRUMENT;
        this.cost       = 0.0d;
        this.quantity   = 0;
        this.unitPrice  = 0;
    }
    /**
     *
     * @param name
     * @param company
     * @param cost
     */
    public Product(String name, Vendor company,double cost){
        super(name,"");
        this.company = company;
        this.cost = cost;
    }
    
    public Product(String name, Vendor company,GoodType goodType, double cost){
        super(name,"");
        this.company = company;
        this.goodType = goodType;
        this.cost = cost;
   
        
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
        hash = 97 * hash + Objects.hashCode(this.company);
        hash = 97 * hash + Objects.hashCode(this.goodType);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.cost) ^ (Double.doubleToLongBits(this.cost) >>> 32));
        hash = 97 * hash + this.quantity;
        return hash;
    }
    
    
    
   
    
    
    
}
