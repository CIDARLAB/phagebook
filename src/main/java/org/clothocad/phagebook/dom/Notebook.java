/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import org.clothocad.model.Person;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author KatieLewis
 */
public class Notebook {
    @Getter @Setter private String        ownerId;
    @Getter @Setter private List<String>  entries;
    @Getter @Setter private String        affiliatedProjectId;
    @Getter @Setter private Date          dateCreated;
    @Getter @Setter private String        id;
    //Need to set privacy level of entries (read/write access)..
    public Notebook(String creator, String project, Date created){
        this.ownerId = creator;
        this.dateCreated = created;
        this.affiliatedProjectId = project;
        this.entries = new ArrayList<>();
    }
    
    private void addEntry(String toAdd){
        entries.add(toAdd);
    }
    private void deleteEntry(String toDelete){
        entries.remove(toDelete);
    }
    public Notebook()
    {
        this.ownerId             = "Not Set";
        this.entries             = new ArrayList<>();
        this.affiliatedProjectId = "Not Set";
        this.dateCreated         = new Date();
        this.id                  = "Not Set";
        
    }
}
