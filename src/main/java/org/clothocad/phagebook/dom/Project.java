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
   @Getter @Setter private String creatorId;
   @Getter @Setter private String leadId;
   @Getter @Setter private List<String> members;
   @Getter @Setter private List<String> notebooks;
   @Getter @Setter private List<String> affiliatedLabs;
   @Getter @Setter private String name;
   @Getter @Setter private Date dateCreated;
   @Getter @Setter private List<String> updates;
   @Getter @Setter private Double budget;
   @Getter @Setter private String grantId;
   @Getter @Setter private String description;
   @Getter @Setter private String id;
   
   public Project()
   {
       this.creatorId      = "Not Set";
       this.leadId         = "Not Set";
       this.members        = new ArrayList<>();
       this.notebooks      = new ArrayList<>();
       this.affiliatedLabs = new ArrayList<>();
       this.name           = "Not Set";
       this.dateCreated    = new Date();
       this.updates        = new ArrayList<>();
       this.budget         = 0.0d;
       this.grantId        = "Not Set";
       this.description    = "Not Set";
       this.id             = "Not Set";
   }
   
   public Project(String creator, String name, Organization lab, String description){
       System.out.println("start of constructor");
       this.dateCreated = new Date();
       this.creatorId = creator;
       this.name = name;
       this.description = description;
<<<<<<< HEAD
       this.updates = new ArrayList<>();
       this.notebooks = new ArrayList<>();
       this.affiliatedLabs = new ArrayList<>();
       this.members = new ArrayList<>();
       //System.out.println("created a new project?? 1");
=======
       this.notebooks = new ArrayList<Notebook>();
       this.affiliatedLabs = new ArrayList<Organization>();
       this.members = new ArrayList<Person>();
       this.updates = new ArrayList<Status>();
       this.affiliatedLabs.add(lab);
>>>>>>> serverside
       
       System.out.println("end of constructor");
    
   }
<<<<<<< Updated upstream
      public Project(String creator, String name, String description){
=======
   
   public Project(Person creator, String name, String description){
>>>>>>> Stashed changes
       this.dateCreated = new Date();
       this.creatorId = creator;
       this.name = name;
       this.description = description;
<<<<<<< HEAD
       this.updates = new ArrayList<>();
       this.notebooks = new ArrayList<>();
       this.affiliatedLabs = new ArrayList<>();
       this.members = new ArrayList<>();

=======
       this.notebooks = new ArrayList<Notebook>();
       this.affiliatedLabs = new ArrayList<Organization>();
       this.members = new ArrayList<Person>();

       this.updates = new ArrayList<Status>();


      
>>>>>>> serverside
   }
   
   
   public Project(String creator, String name, Organization lab, 
        String lead, Double projectBudget, String projectGrant, String description){
       this.creatorId = creator;
       this.dateCreated = new Date();
       this.name = name;
       this.budget = projectBudget;
       this.description = description;
       this.updates = new ArrayList<>();
       this.grantId = projectGrant;
       this.leadId = lead;
       this.notebooks = new ArrayList<>();
       this.affiliatedLabs = new ArrayList<>();
       this.members = new ArrayList<>();
       //Create new lab notebook for creator?
       
       System.out.println("created a new project??");
       Notebook creatorNotebook = new Notebook(creator, this.getId(), dateCreated);
       this.affiliatedLabs.add(lab.getId());
       this.members.add(creator);            
        //Create new lab notebook for creator?
       notebooks.add(creatorNotebook.getId());
   }
      
   
   private void addMember(Person newMember){
       boolean exists = false;
       for(String member: this.members){
           if(member.equals(newMember.getId())){
               exists =true;
           }
       }
       
       if(exists == true){
           //Throw error
       }
       else{
           //Check if person has correct credentials to be added to project
           //Send approval request?
           members.add(newMember.getId());
       }
   };
   
   private void removeMember(Person toRemove){
       //Iterate through and compare unique ids
       boolean exists = false;
       for(String member: this.members){
           if(member.equals(toRemove.getId())){
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
           members.remove(toRemove.getId());
       }
   };
   
   private void addOrganization(Organization newOrganization){
       boolean exists = false;
       for(String organization: this.affiliatedLabs){
           if(organization.equals(newOrganization.getId())){
               exists = true;
           }
       }
       
       if(exists == true){
           //Throw error
       }
       else{
           affiliatedLabs.add(newOrganization.getId());
       }
   };
   
   private void deleteOrganization(Organization toDelete){
       boolean exists = false;
       for(String organization: this.affiliatedLabs){
           if(organization.equals(toDelete.getId())){
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
       Notebook newNotebook = new Notebook(notebookPerson.getId(), this.getId(), today);
       notebooks.add(newNotebook.getId());
   }
   
   public void deleteNotebook(Notebook toDelete){
       notebooks.remove(toDelete.getId());
   }
   
   //Need function to display most recent statuses (e.g. top 10?)
   public void addStatus(Status toAdd){
       System.out.println("add status");
       updates.add(toAdd.getId());
       System.out.println("finish status");
   }
   
   public void deleteStatus(Status toDelete){
       updates.remove(toDelete.getId());
   }
    private void addMember(String newMember){
       boolean exists = false;
       for(String member: this.members){
           if(member.equals(newMember)){
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
   
   private void removeMember(String toRemove){
       //Iterate through and compare unique ids
       boolean exists = false;
       for(String member: this.members){
           if(member.equals(toRemove)){
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
   
   private void addOrganization(String newOrganization){
       boolean exists = false;
       for(String organization: this.affiliatedLabs){
           if(organization.equals(newOrganization)){
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
   
   private void deleteOrganization(String toDelete){
       boolean exists = false;
       for(String organization: this.affiliatedLabs){
           if(organization.equals(toDelete)){
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
   
   public void addNotebok(String notebookPerson){
       //how do I pass this project?
       Date today = new Date();
       Notebook newNotebook = new Notebook(notebookPerson, this.id, today);
       notebooks.add(newNotebook.getId());
   }
   
   public void deleteNotebook(String toDelete){
       notebooks.remove(toDelete);
   }
   
   //Need function to display most recent statuses (e.g. top 10?)
   public void addStatus(String toAdd){
       System.out.println("add status");
       updates.add(toAdd);
       System.out.println("finish status");
   }
   
   public void deleteStatus(String toDelete){
       updates.remove(toDelete);
   }
}
