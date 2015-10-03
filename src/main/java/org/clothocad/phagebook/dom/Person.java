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
 * @author prash
 * @author KatieLewis
 */
public class Person {
    @Getter @Setter private List<Project> projects;
    @Getter @Setter private List<Status> statuses;
    @Getter @Setter private List<Notebook> notebooks;
    @Getter @Setter private List<Organization> labs;
    @Getter @Setter private List<Person> colleagues;
    //When sign up -- how do you affiliate position with Organization? e.g. PI at BU but affiliate researcher at berkeley
    //profile pic? text link
    //Permissions / security
    //Need function to send approval request for lab?
    @Getter @Setter private List<Publication> publications;
    @Getter @Setter private String id;

    public Person(){
        this.colleagues = new ArrayList<Person>();
        this.notebooks = new ArrayList<Notebook>();
        this.statuses = new ArrayList<Status>();
        this.labs = new ArrayList<Organization>();
        this.projects = new ArrayList<Project>();
        this.publications = new ArrayList<Publication>();
    }
}
