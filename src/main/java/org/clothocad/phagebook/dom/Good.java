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
 * @author prash,innaturshudzhyan
 */
public abstract class Good implements ClothoBaseObject{
    @Getter @Setter protected String name;
    @Getter @Setter protected String description;
    @Getter @Setter protected String id;
    
    /**
     *
     * @param name
     */
    public Good(String name)
    {
        this.name = name;
    }
    
    public Good(String name,String description){
        this.name = name;
        this.description = description;
    }
    public Good()
    {
        this.name        = "Not Set";
        this.description = "Not Set";
        this.id          = "Not Set";
        
    }
}
