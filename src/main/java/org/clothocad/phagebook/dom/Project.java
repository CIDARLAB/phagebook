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
   @Getter @Setter private Person projectCreator;
   @Getter @Setter private Person projectLead;
   @Getter @Setter private List<Person> projectMembers;
   @Getter @Setter private List<Notebook> labNotebooks;
   @Getter @Setter private List<Organization> affiliatedLabs;
   @Getter @Setter private String projectName;
   @Getter @Setter private Date dateCreated;
   @Getter @Setter private List<Status> projectUpdates;
   @Getter @Setter private Double budget;
   @Getter @Setter private Grant grant;
   @Getter @Setter private String description;
   
   
   public Project(Date createdDate,Person creator, String name, Organization lab, 
        Person lead, Double projectBudget, Grant projectGrant, String description){
       this.projectCreator = creator;
       this.dateCreated = createdDate;
       this.projectName = name;
       this.budget = projectBudget;
       this.description = description;
       this.projectUpdates = new ArrayList<Status>();
       //Not sure what grant information needs to be present. Can there be more than one?
       this.grant = projectGrant;
       this.projectMembers.add(creator);
       this.affiliatedLabs.add(lab);
       this.projectLead = lead;
       this.labNotebooks = new ArrayList<Notebook>();
       this.affiliatedLabs = new ArrayList<Organization>();
       this.projectMembers = new ArrayList<Person>();
       //Create new lab notebook for creator?
       Notebook creatorNotebook = new Notebook(creator, name, createdDate);
       labNotebooks.add(creatorNotebook);
   }
   
   private void addMember(Person newMember){
       if(projectMembers.getIndex(newMember) != -1){
           //Throw error
       }
       else{
           //Check if person has correct credentials to be added to project
           //Send approval request?
           projectMembers.add(newMember);
       }
   };
   
   private void removeMember(Person member){
       if(projectMembers.getIndex(member) == -1){
           //Throw error - person not in project
       }
       else{
           //Need approval or check credentials to delete member?
           projectMembers.remove(member);
       }
   };
   
   private void addOrganization(Organization newOrganization){
       if(affiliatedLabs.getIndex(newOrganization) != -1){
           //Throw error
       }
       else{
           affiliatedLabs.add(newOrganization);
       }
   };
   
   private void deleteOrganization(Organization Organization){
       if(affiliatedLabs.getIndex(Organization) == -1){
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
       labNotebooks.add(newNotebook);
   }
   
   private void deleteNotebook(Notebook toDelete){
       labNotebooks.remove(toDelete);
   }
   
   //Need function to display most recent statuses (e.g. top 10?)
   private void addStatus(Status toAdd){
       projectUpdates.add(toAdd);
   }
   
   private void deleteStatus(Status toDelete){
       projectUpdates.remove(toDelete);
   }
}
