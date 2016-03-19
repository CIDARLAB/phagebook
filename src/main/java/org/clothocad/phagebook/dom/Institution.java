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
 * @author KatieLewis
 */
public class Institution extends Organization {
   
    @Getter @Setter InstitutionType type;
    @Getter @Setter List<String>    labs;
    
    public Institution(String name){
       super(name);
    }
    
    public Institution(){
        super();
        this.type = InstitutionType.University;
        this.labs = new ArrayList<>();
    }
    public static enum InstitutionType {
        University, Corporation, Independent
    }
}
