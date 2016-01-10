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
 * @author prash
 * @author KatieLewis
 */
public class Project {
   @Getter @Setter private Person creator;
   @Getter @Setter private Person lead;
   @Getter @Setter private List<Person> members;
   @Getter @Setter private List<Notebook> notebooks;
   @Getter @Setter private List<Organization> affiliatedLabs;
   @Getter @Setter private String name;
   @Getter @Setter private Date dateCreated;
   @Getter @Setter private List<Status> updates;
   @Getter @Setter private Double budget;
   @Getter @Setter private Grant grant;
   @Getter @Setter private String description;
   @Getter @Setter private String id;
   
   public Project(){}
   
   public Project(Person creator, String name, Organization lab, String description){
       System.out.println("start of constructor");
       this.dateCreated = new Date();
       this.creator = creator;
       this.name = name;
       this.description = description;
       this.updates = new ArrayList<>();
       this.notebooks = new ArrayList<>();
       this.affiliatedLabs = new ArrayList<>();
       this.members = new ArrayList<>();
       //System.out.println("created a new project?? 1");
       
   }
      public Project(Person creator, String name, String description){
       this.dateCreated = new Date();
       this.creator = creator;
       this.name = name;
       this.description = description;
       this.updates = new ArrayList<>();
       this.notebooks = new ArrayList<>();
       this.affiliatedLabs = new ArrayList<>();
       this.members = new ArrayList<>();

   }
   
   
   public Project(Person creator, String name, Organization lab, 
        Person lead, Double projectBudget, Grant projectGrant, String description){
       this.creator = creator;
       this.dateCreated = new Date();
       this.name = name;
       this.budget = projectBudget;
       this.description = description;
       this.updates = new ArrayList<>();
       this.grant = projectGrant;
       this.lead = lead;
       this.notebooks = new ArrayList<>();
       this.affiliatedLabs = new ArrayList<>();
       this.members = new ArrayList<>();
       //Create new lab notebook for creator?
       
       System.out.println("created a new project??");
       Notebook creatorNotebook = new Notebook(creator, this, dateCreated);
       this.affiliatedLabs.add(lab);
       this.members.add(creator);            
        //Create new lab notebook for creator?
       notebooks.add(creatorNotebook);
   }
      
   
   private void addMember(Person newMember){
       boolean exists = false;
       for(Person member: this.members){
           if(member.getId().equals(newMember.getId())){
               exists =true;
           }
       }
       
       if(exists == true){
           //Throw error
       }
       else{
           //Check if person has correct credentials to be added to project
           //Send approval request?
           members.add(newMember);
       }
   };
   
   private void removeMember(Person toRemove){
       //Iterate through and compare unique ids
       boolean exists = false;
       for(Person member: this.members){
           if(member.getId().equals(toRemove.getId())){
               exists =true;
           }
       }
       //If person doesn't exist in project, throw an error
       if(exists == false){
           //Throw error - person not in project
       }
       //If they do exist, remove them from project
       else{
           //Need approval or check credentials to delete member?
           members.remove(toRemove);
       }
   };
   
   private void addOrganization(Organization newOrganization){
       boolean exists = false;
       for(Organization organization: this.affiliatedLabs){
           if(organization.getId().equals(newOrganization.getId())){
               exists = true;
           }
       }
       
       if(exists == true){
           //Throw error
       }
       else{
           affiliatedLabs.add(newOrganization);
       }
   };
   
   private void deleteOrganization(Organization toDelete){
       boolean exists = false;
       for(Organization organization: this.affiliatedLabs){
           if(organization.getId().equals(toDelete.getId())){
               exists = true;
           }
       }
       if(exists == false){
           //Throw error
       }
       else{
           affiliatedLabs.remove(toDelete);
       }
   };
   
   public void addNotebok(Person notebookPerson){
       //how do I pass this project?
       Date today = new Date();
       Notebook newNotebook = new Notebook(notebookPerson, this, today);
       notebooks.add(newNotebook);
   }
   
   public void deleteNotebook(Notebook toDelete){
       notebooks.remove(toDelete);
   }
   
   //Need function to display most recent statuses (e.g. top 10?)
   public void addStatus(Status toAdd){
       System.out.println("add status");
       updates.add(toAdd);
       System.out.println("finish status");
   }
   
   public void deleteStatus(Status toDelete){
       updates.remove(toDelete);
   }
}
