/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.model.Person;
import static org.clothocad.phagebook.adaptors.ClothoAdapter.clothoObject;
import org.clothocad.phagebook.dom.Project;
/**
 *
 * @author anna_g
 */
public class sendEmails {

  private final static String SENDER_DOMAIN_NAME = "phagebook.email@gmail.com";
  private final static String password = "Phagebook";
  
 /**
 ** Access this function first if you are sending a message. 
 ** 
 */
  public static void sendMessagesTo(Map people, String projectName, String changer){
   logIn(people, projectName, changer);
  }
  
 /**
 * This function logs in to phagebook.email@gmail.com and sends emails to the 
 * people in the input map of people.
 * 
 */
  private static void logIn(Map people, String projectName, String changer){
    
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

    Session session = Session.getInstance(props,
    new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(SENDER_DOMAIN_NAME, password);
      }
    });
    
    try {
      
      // loop through the people Hashmap to message each person
      Iterator entries = people.entrySet().iterator();
      while (entries.hasNext()) {
        // reset the value if it is diff from the one in the project object
        Map.Entry entry = (Map.Entry) entries.next();
        String email = (String)entry.getKey();
        String person = (String)entry.getValue();
        System.out.println("Person = " + person + ", Email = " + email);
      
        Message message = createMessage(SENDER_DOMAIN_NAME, person, email, session, projectName, changer);
        Transport.send(message);
        System.out.println("Done");
      }

    } catch (MessagingException e) {
      //if you can't send it from the info stored in the person class then
      //get HANGRY
      e.printStackTrace();
    }  
  }

  public static Message createMessage(String senderDomain, String pers, String email,
          Session session, String projectName, String changer) throws MessagingException{
    Message message = new MimeMessage(session);
    try{
      System.out.println("About to send an email to " + email);
      message.setFrom(new InternetAddress(senderDomain));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
      message.setSubject("New Project Update");
      System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
      String imgSource = "http://cidarlab.org/wp-content/uploads/2015/09/phagebook_AWH.png" ;
      System.out.println(imgSource);
      String foo ="src= \""+ imgSource+ "\">";
            System.out.println(foo);

      String messageTxt = "<img height=\"50\" width=\"200\" src=\""+ imgSource+ "\">" + 
                "<p>Hi " + pers +",</p>" + " <p>A new update was added to project " + projectName + 
                " by " + changer + ".</p>" +
                " <p>Have a great day,</p>" +
                " <p>The Phagebook Team</p>";

      message.setContent(messageTxt, "text/html");
    } catch (MessagingException e){
      throw e;
    }
    return message;
  }
 
  /*
  ** This function gets the people associated with a project
  ** and sends emails to them. 
  ** @param String changer -- name of the person who created the update
  ** @param String projectId -- Id of the project that was updateds
  */
  public static void sendEmails(String projectId, String changer, Clotho clothoObject){
    System.out.println("In sendEmails function projectID is:");
    System.out.println(projectId);
    Project project = ClothoAdapter.getProject(projectId, clothoObject);

    // get the project name from project id to be used in the email
    String projectName= project.getName();
    
    // get the list of people associate with the project
    List<String> members = project.getMembers();
    // create a hashmap of people and add members that are not in the list of members
    Person creator = ClothoAdapter.getPerson(project.getCreatorId(), clothoObject);
    Person lead = ClothoAdapter.getPerson(project.getLeadId(), clothoObject);
    System.out.println("In sendEmails, person lead is "+lead.getFirstName() + ' ' + lead.getLastName());
    System.out.println("In sendEmails, lead's email is "+lead.getEmailId());
    Map people = new HashMap();
    
    if(!(creator.getEmailId().equals("Not set"))){
      System.out.println("I will be sending an email to creator!");
      people.put(creator.getEmailId(), creator.getFirstName() + ' ' + creator.getLastName());
    }
    if(!(lead.getEmailId().equals("Not set"))){
      System.out.println("I will be sending an email to lead!");
      people.put(lead.getEmailId(), lead.getFirstName() + ' ' + lead.getLastName());
    }
    // go through the list of members to add them to the hashmap
    for(int i = 0; i<members.size(); i++){
      String personId = members.get(i);
      Person member = ClothoAdapter.getPerson(personId, clothoObject);
      String memberEmail = member.getEmailId();
      if(memberEmail.equals("Not set")){
        break;
      }
      String memberName = member.getFirstName() + ' ' + member.getLastName();
//      System.out.println("/n");
//      System.out.println(memberName);
//      System.out.println(memberEmail);
//      System.out.println("/n");
      people.put(memberEmail, memberName);

    }
    // inputs are hashmap of people, project name and person who added update
    sendMessagesTo(people, projectName, changer);
      
    }
  
//  public static void main(String[] args) {
//    
//    HashMap test = new HashMap();
//    test.put("agonchar@bu.edu", "Anna Goncharova");
//    test.put("anna@goncharova.com", "Anna");
//    test.put("prash@bu.edu", "Prashant");
//
//    sendMessagesTo(test, " These changes were minor.");
//
//  }
}


