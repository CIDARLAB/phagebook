/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.clothocad.model.Person;

/**
 *
 * @author KatieLewis
 */
public class Status{
    @Getter @Setter private String id;
    @Getter @Setter private String text;
    @Getter @Setter private Person user;
    @Getter @Setter private Date created;
    
    public Status(String message, Person creator, Date created){
    this.text = message;
    this.user = creator;
    this.created = created;
    }
}

