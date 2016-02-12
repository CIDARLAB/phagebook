/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author Herb
 */
public class CartItem {
    @Getter @Setter private Map<Product, Double> productWithDiscount;
    @Getter @Setter private String id;
    
    public CartItem()
    {
    
        this.id = "Not Set"; 
        this.productWithDiscount = new HashMap();
    }
    
}
