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
public class Vendor extends Organization {
    
    
    @Getter @Setter private String contact;
    
    /**
     *
     * @param name
     */
    public Vendor(String name)
    {
        super(name);
    }
    
    public Vendor()
    {
        super();
        this.contact = "Not Set";
        
    } 
   
 
}
