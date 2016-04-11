/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.phagebook.dom.Notebook;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Status;

/**
 *
 * @author prash
 * @author KatieLewis
 * @author AllisonDurkan
 * @author JohanOspina
 */
public class Person {
    
    //ADDED properties
    @Getter @Setter private String                      id;
    @Getter @Setter private String                      salt;
    @Getter @Setter private byte[]                      saltedEmailHash;
    @Getter @Setter private String                      emailId;
    @Getter @Setter private String                      firstName;
    @Getter @Setter private String                      lastName;
    @Getter @Setter private String                      password;
    @Getter @Setter private boolean                     activated;
    @Getter @Setter private String                      activationString;
    @Getter @Setter private List<String>                colleagues;
    @Getter @Setter private List<String>                notebooks;
    @Getter @Setter private List<String>                statuses;
    @Getter @Setter private String                      institution;
    @Getter @Setter private String                      department;
    @Getter @Setter private String                      title;
    @Getter @Setter private List<String>                institutions;
    @Getter @Setter private List<String>                labs;
    @Getter @Setter private List<String>                projects;
    @Getter @Setter private List<String>                publications;
    @Getter @Setter private Map<String,Set<PersonRole>> roles;
    @Getter @Setter private List<String>                createdOrders;
    @Getter @Setter private List<String>                submittedOrders;
    @Getter @Setter private List<String>                approvedOrders;
    @Getter @Setter private String                      profileDescription;
    @Getter @Setter private List<String>                deniedOrders;
    
   
   
    //When sign up -- how do you affiliate position with Organization? e.g. PI at BU but affiliate researcher at berkeley
    //profile pic? text link
  

    public Person(){
        this.id                     = "Not Set";
        this.salt                   = "Not Set";
        this.saltedEmailHash        =  new byte[1];
        this.emailId                = "Not set";
        this.firstName              = "Jane";
        this.lastName               = "Doe";
        this.password               = "Not Set";
        this.activated              = false;
        this.activationString       = "Not Set";
        this.colleagues             = new ArrayList<>();
        this.notebooks              = new ArrayList<>();
        this.statuses               = new ArrayList<>();
        this.institution            = "Not Set";
        this.department             = "Not Set";
        this.title                  = "Not Set";
        this.institutions           = new ArrayList<>();
        this.labs                   = new ArrayList<>();
        this.projects               = new ArrayList<>();
        this.publications           = new ArrayList<>();
        this.roles                  = new HashMap<>();
        this.createdOrders          = new ArrayList<>();
        this.submittedOrders        = new ArrayList<>();
        this.approvedOrders         = new ArrayList<>();
        this.profileDescription     = "Not Set";
    }
    
    public void addRole(Institution institution, PersonRole role){
        if(this.roles.containsKey(institution.getId())){
            if(!this.roles.get(institution.getId()).contains(role)){
                this.roles.get(institution.getId()).add(role);
            }
        }
        else{
            Set<PersonRole> roleList = new HashSet<>();
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
       for(String institutions: this.labs){
           if(institutions.equals(institution.getId())){
               exists = true;
           }
       }
       if(exists == true){
           //Throw error
       }
       else{
           this.labs.add(institution.getId());
           Set<PersonRole> roleList = new HashSet<>();
           roleList.add(PersonRole.MEMBER);
           this.roles.put(institution.getId(),roleList);
       }
    }
    
    public void addInstitution(Institution institution, Set<PersonRole> roles ){
        boolean exists = false;
       for(String institutions: this.labs){
           if(institutions.equals(institution.getId())){
               exists = true;
           }
       }
       if(exists == true){
           //Throw error
       }
       else{
           this.labs.add(institution.getId());
           this.roles.put(institution.getId(),roles);
       }
    }
    
    public void addStatus(Status newStatus){
       // Date today = Calendar.getInstance().getTime();
        //Status newStatus = new Status(text, this);
        this.statuses.add(newStatus.getId());
    }
    
    
    public void addRole(String institution, PersonRole role){
        if(this.roles.containsKey(institution)){
            if(!this.roles.get(institution).contains(role)){
                this.roles.get(institution).add(role);
            }
        }
        else{
            Set<PersonRole> roleList = new HashSet<>();
            roleList.add(role);
            this.roles.put(institution, roleList);
        }
    }
    
    
    public Set<PersonRole> getRole(String institution){
           if(this.roles.containsKey(institution)){
               if(this.roles.get(institution).isEmpty()){
                   return null;
               }
               else{
                   return this.roles.get(institution);
               }
           }
           else{
               return null;
           }
    }
    
    public void removeRole(String institution, PersonRole role){
        if(this.roles.containsKey(institution)){
            if(!this.roles.get(institution).contains(role)){
                Set<PersonRole> roleList = this.roles.get(institution);
                roleList.remove(role);
                this.roles.put(institution, roleList);
            }
        }
        else{
        }
    }
    
    public void addInstitution(String institution){
       boolean exists = false;
       for(String institutions: this.labs){
           if(institutions.equals(institution)){
               exists = true;
           }
       }
       if(exists == true){
           //Throw error
       }
       else{
           this.labs.add(institution);
           Set<PersonRole> roleList = new HashSet<>();
           roleList.add(PersonRole.MEMBER);
           this.roles.put(institution,roleList);
       }
    }
    
    public void addInstitution(String institution, Set<PersonRole> roles ){
        boolean exists = false;
       for(String institutions: this.labs){
           if(institutions.equals(institution)){
               exists = true;
           }
       }
       if(exists == true){
           //Throw error
       }
       else{
           this.labs.add(institution);
           this.roles.put(institution,roles);
       }
    }
    
    /*public void addStatus(String newStatus){
       // Date today = Calendar.getInstance().getTime();
        //Status newStatus = new Status(text, this);
        this.statuses.add(newStatus);
    
        THIS DOESNT WORK DEPRECATED
    }*/
    
    public static enum PersonRole{
       MEMBER, PI, LABMANAGER, POSTDOC, GRADSTUDENT, UNDERGRADUATE,LABADMIN, VISITINGRESEARCHER,RAPROFESSOR
    }
    
    @Override public String toString(){
       String result = "Person is ";
       
       if(!this.firstName.equals("") && !this.lastName.equals("")){
        result += this.firstName + " " + this.lastName +"\n";
       }
       if(!this.id.equals("")){
        result += "ID is: " + this.id +"\n";
       }
       
       return result;
      
    }
}
