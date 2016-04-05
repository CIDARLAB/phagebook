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
 * @author Herb
 */
public class Lab extends Organization {
    @Getter @Setter List<String> leadPIs;
    //TODO ADD MEMBERS
    
    //@Getter @Setter List<String> members;
    public Lab(String name){
       super(name);
    }
    
    public Lab(){
        super();
        this.leadPIs = new ArrayList<>();
        //this.members = new ArrayList<>();
    }
}
