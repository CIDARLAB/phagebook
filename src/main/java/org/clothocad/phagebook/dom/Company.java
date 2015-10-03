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
public class Company {
    
    @Getter
    @Setter
    private String name, description;
    
    @Getter
    @Setter
    private String phone, url, contact;
    
    /**
     *
     * @param name
     */
    public Company(String name)
    {
        this.name = name;
    }
    
 
}
