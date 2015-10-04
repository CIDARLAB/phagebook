/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author innaturshudzhyan
 */
public class Company extends Organization {
    
    
    @Getter
    @Setter
    private String contact;
    
    /**
     *
     * @param name
     */
    public Company(String name)
    {
        super(name,"","","");
    }
    
 
}
