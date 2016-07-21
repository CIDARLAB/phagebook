/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author Herb
 */
public class CartItem implements ClothoBaseObject {
    @Getter @Setter private Double discount; //ID of a PRODUCT
    @Getter @Setter private String id;
    @Getter @Setter private Date dateCreated;
    @Getter @Setter private String productId;
    @Getter @Setter private Integer quantity;
    
    public CartItem()
    {
        this.id = "Not Set"; 
        this.productId = "Not Set";
        this.dateCreated = new Date();
        this.discount = 1d;
        this.quantity = 0;
    }

    @Override
    public String schemaForObject(Object self){
        return CartItem.class.getCanonicalName();
    }

    @Override
    public String idForObject() {
        return this.id;
    }
    
}
