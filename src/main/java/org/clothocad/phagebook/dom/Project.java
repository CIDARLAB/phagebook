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
   
   
   public Project(Date createdDate,Person creator, String name, Organization lab, 
        Person lead, Double projectBudget, Grant projectGrant, String description){
       this.creator = creator;
       this.dateCreated = createdDate;
       this.name = name;
       this.budget = projectBudget;
       this.description = description;
       this.updates = new ArrayList<Status>();
       //Not sure what grant information needs to be present. Can there be more than one?
       this.grant = projectGrant;
       this.members.add(creator);
       this.affiliatedLabs.add(lab);
       this.lead = lead;
       this.notebooks = new ArrayList<Notebook>();
       this.affiliatedLabs = new ArrayList<Organization>();
       this.members = new ArrayList<Person>();
       //Create new lab notebook for creator?
       Notebook creatorNotebook = new Notebook(creator, this, createdDate);
       notebooks.add(creatorNotebook);
   }
   
   private void addMember(Person newMember){
       if(members.indexOf(newMember) != -1){
           //Throw error
       }
       else{
           //Check if person has correct credentials to be added to project
           //Send approval request?
           members.add(newMember);
       }
   };
   
   private void removeMember(Person member){
       if(members.indexOf(member) == -1){
           //Throw error - person not in project
       }
       else{
           //Need approval or check credentials to delete member?
           members.remove(member);
       }
   };
   
   private void addOrganization(Organization newOrganization){
       if(affiliatedLabs.indexOf(newOrganization) != -1){
           //Throw error
       }
       else{
           affiliatedLabs.add(newOrganization);
       }
   };
   
   private void deleteOrganization(Organization Organization){
       if(affiliatedLabs.indexOf(Organization) == -1){
           //Throw error
       }
       else{
           affiliatedLabs.remove(Organization);
       }
   };
   
   private void addNotebok(Person notebookPerson){
       //how do I pass this project?
       Date today = new Date();
       Notebook newNotebook = new Notebook(notebookPerson, this, today);
       notebooks.add(newNotebook);
   }
   
   private void deleteNotebook(Notebook toDelete){
       notebooks.remove(toDelete);
   }
   
   //Need function to display most recent statuses (e.g. top 10?)
   private void addStatus(Status toAdd){
       updates.add(toAdd);
   }
   
   private void deleteStatus(Status toDelete){
       updates.remove(toDelete);
   }
}
