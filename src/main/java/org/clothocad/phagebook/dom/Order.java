/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

//experiment, sample, institution=

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.clothocad.model.Person;
/**
 *
 * @author prash,innaturshudzhyan
 */

public class Order {
    
    
    @Getter
    @Setter
    private String id, name, description,createdOn;
    
    @Getter
    @Setter
    private Person createdBy;
    
    @Getter
    @Setter
    private Map<Product, Integer> products;
    
    /**
     *
     * @param name
     */
    public Order(String name) {
        this.name = name;
        this.products = new HashMap<Product, Integer>();
        
    };   
    
}
