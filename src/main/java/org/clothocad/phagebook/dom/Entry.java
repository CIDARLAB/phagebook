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
 * @author KatieLewis
 */
public class Entry {
    @Getter @Setter private String notebookId;
    @Getter @Setter private Date dateCreated;
    @Getter @Setter private Date lastModified;
    //how would I incorporate photos?
    @Getter @Setter private String text;
    @Getter @Setter private String title;
    @Getter @Setter private String id;
   
    public Entry(String owner, Date created, String title, String text){
        this.notebookId = owner;
        this.dateCreated = created;
        this.title = title;
        this.text = text;
    };
    
    //Need add text or can I just set text to total text after done editing?
    //How to set read/write permissions?
    
    public Entry()
    {
        this.notebookId   = "Not Set";
        this.dateCreated  = new Date();
        this.lastModified = new Date();
        this.text         = "Not Set";
        this.title        = "Not Set";
        this.id           = "Not Set";
        
    }
}
