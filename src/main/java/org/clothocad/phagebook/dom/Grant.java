/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import org.clothocad.model.Person;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author KatieLewis
 */
public class Grant {
    @Getter @Setter private String name;
    @Getter @Setter private Person leadPI;
    @Getter @Setter private List<Person> coPIs;
    @Getter @Setter private String programManager;
    @Getter @Setter private Date startDate;
    @Getter @Setter private Date endDate;
    @Getter @Setter private Double budget;
    @Getter @Setter private Double amountSpent;
    @Getter @Setter private List<Project> projects;
    @Getter @Setter private String description;
    @Getter @Setter private String id;
    
    public Grant(String name){
        this.name = name;    
    }
    
    public Grant(){}
}
