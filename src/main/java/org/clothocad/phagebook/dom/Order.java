/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

//experiment, sample, institution=


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.clothocad.model.Person;
import org.clothocad.phagebook.dom.CartItem;
/**
 *
 * @author prash,innaturshudzhyan
 */

public class Order {
    
    
    @Getter @Setter private String               id;
    @Getter @Setter private String               name; 
    @Getter @Setter private String               description;
    @Getter @Setter private Date                 dateCreated;
    @Getter @Setter private String               createdById; 
    @Getter @Setter private Map<String, Integer> products; //CART ITEM ID AND INTEGER...
    @Getter @Setter private Double               budget;
    @Getter @Setter private Integer              maxOrderSize;
    @Getter @Setter private String               approvedById;
    @Getter @Setter private List<String>         receivedByIds;
    @Getter @Setter private String               relatedProjectId;
    @Getter @Setter private OrderStatus          status;
    @Getter @Setter private String               affiliatedLabId;
    
    
//    public Map<Product, Integer> getProducts(){
//        return this.products;
//    }
//    public void setProducts(Map<Product, Integer> products){
//        this.products = products;
//    }
    
    /**
     *
     * @param name
     */
    public Order(String name) {
        this.name = name;
        
        this.products = new HashMap<>();
    };   
    
    public Order()
    {
        this.id          = "Not Set";
        this.name        = "Not Set";
        this.description = "Not Set";
        this.dateCreated = new Date();
        this.createdById   = "Not Set";
        this.products    = new HashMap<>();
        this.budget      = 0.0d;
        this.maxOrderSize = 1;
        this.approvedById  = "Not Set";
        this.receivedByIds  = new ArrayList<>();
        this.relatedProjectId = "Not Set";
        this.status = OrderStatus.INPROGRESS;
        this.affiliatedLabId = "Not Set";
     
    }
}
