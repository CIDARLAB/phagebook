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
    @Getter @Setter private Person owner;
    @Getter @Setter private List<Entry> entries;
    @Getter @Setter private Project affiliatedProject;
    @Getter @Setter private Date dateCreated;
    @Getter @Setter private String id;
    //Need to set privacy level of entries (read/write access)..
    public Notebook(Person creator, Project project, Date created){
        this.owner = creator;
        this.dateCreated = created;
        this.affiliatedProject = project;
        this.entries = new ArrayList<Entry>();
    }
    
    private void addEntry(Entry toAdd){
        entries.add(toAdd);
    }
    private void deleteEntry(Entry toDelete){
        entries.remove(toDelete);
    }
    public Notebook(){}
}
