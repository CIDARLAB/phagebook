/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

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
    @Getter @Setter List<Entry> notebookEntries;
    @Getter @Setter private Project affiliatedProject;
    @Getter @Setter Date dateCreated;
    //Need to set privacy level of entries (read/write access)..
    public Notebook(Person creator, Project project, Date created){
        this.owner = creator;
        this.dateCreated = created;
        this.affiliatedProject = project;
        this.notebookEntries = new ArrayList<Entry>();
    }
    
    private void addEntry(Entry toAdd){
        notebookEntries.add(toAdd);
    }
    private void deleteEntry(Entry toDelete){
        notebookEntries.remove(toDelete);
    }
}
