/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.clothocad.model.Person;

/**
 *
 * @author KatieLewis
 */
public class Status{
    @Getter @Setter private String id;
    @Getter @Setter private String text;
    @Getter @Setter private String userId;
    @Getter @Setter private Date created;
    
//    public Status(String message, Person creator){
//    this.text = message;
//    this.userId = "Not Set";
//    Date today = Calendar.getInstance().getTime();
//    this.created = today;
//    }
//    
    public Status()
    {
        this.id       = "Not Set";
        this.text     = "Not Set"; 
        this.userId   = "Not Set";
        Date today = Calendar.getInstance().getTime();
        this.created = today;
    }
    
    public String toString(){
      String result = "";
      
      if(!(this.text.compareTo("Not Set") == 0)){ 
        result += "Status is '"+ this.text +"'. ";
      }
      if(!(this.userId.compareTo("Not Set") == 0)){ 
        result += "Made by User '"+ this.userId +"'. ";
      }
      
      result += "Date of the status is: " + this.created;
      
      return result;
    }
}

