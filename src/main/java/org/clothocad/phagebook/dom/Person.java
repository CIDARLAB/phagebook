/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
    @Getter @Setter private List<Institution> labs;
    @Getter @Setter private List<Person> colleagues;
    @Getter @Setter private List<Order> orders;
    //When sign up -- how do you affiliate position with Organization? e.g. PI at BU but affiliate researcher at berkeley
    //profile pic? text link
    @Getter @Setter private List<Publication> publications;
    @Getter @Setter private String id;
    Map<String,Set<PersonRole>> roles;
    

    public Person(){
        this.colleagues = new ArrayList<Person>();
        this.notebooks = new ArrayList<Notebook>();
        this.statuses = new ArrayList<Status>();
        this.labs = new ArrayList<Institution>();
        this.projects = new ArrayList<Project>();
        this.publications = new ArrayList<Publication>();
        this.roles = new HashMap<String,Set<PersonRole>>();
        this.orders = new ArrayList<Order>();
    }
    
    public void addRole(Institution institution, PersonRole role){
        if(this.roles.containsKey(institution.getId())){
            if(!this.roles.get(institution.getId()).contains(role)){
                this.roles.get(institution.getId()).add(role);
            }
        }
        else{
            Set<PersonRole> roleList = new HashSet<PersonRole>();
            roleList.add(role);
            this.roles.put(institution.getId(), roleList);
        }
    }
    
    
    public Set<PersonRole> getRole(Institution institution){
           if(this.roles.containsKey(institution.getId())){
               if(this.roles.get(institution.getId()).isEmpty()){
                   return null;
               }
               else{
                   return this.roles.get(institution.getId());
               }
           }
           else{
               return null;
           }
    }
    
    public void removeRole(Institution institution, PersonRole role){
        if(this.roles.containsKey(institution.getId())){
            if(!this.roles.get(institution.getId()).contains(role)){
                Set<PersonRole> roleList = this.roles.get(institution.getId());
                roleList.remove(role);
                this.roles.put(institution.getId(), roleList);
            }
        }
        else{
        }
    }
    
    public void addInstitution(Institution institution){
        boolean exists = false;
       for(Institution institutions: this.labs){
           if(institutions.getId() == institution.getId()){
               exists = true;
           }
       }
       if(exists == true){
           //Throw error
       }
       else{
           this.labs.add(institution);
           Set<PersonRole> roleList = new HashSet<PersonRole>();
           roleList.add(PersonRole.MEMBER);
           this.roles.put(institution.getId(),roleList);
       }
    }
    
    public void addInstitution(Institution institution, Set<PersonRole> roles ){
        boolean exists = false;
       for(Institution institutions: this.labs){
           if(institutions.getId() == institution.getId()){
               exists = true;
           }
       }
       if(exists == true){
           //Throw error
       }
       else{
           this.labs.add(institution);
           this.roles.put(institution.getId(),roles);
       }
    }
    
    
    public static enum PersonRole{
       MEMBER, PI, LABMANAGER, POSTDOC, GRADSTUDENT, UNDERGRADUATE,LABADMIN, VISITINGRESEARCHER,RAPROFESSOR
    }
}
