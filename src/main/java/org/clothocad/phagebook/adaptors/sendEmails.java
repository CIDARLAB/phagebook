/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
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
import org.clothocad.model.Person;
/**
 *
 * @author anna_g
 */
public class sendEmails {

  
  // fuck it lets make these public for now
  final static String SENDER_DOMAIN_NAME = "phagebook.email@gmail.com";
  final static String password = "Phagebook";
  
 /**
 * Access this function first if you are sending a message. 
 * 
 */
  public static void sendMessagesTo(Map people, String text){
   logIn(people, text);
  }
  

 /**
 * This function logs in to phagebook.email@gmail.com and sends emails to the 
 * people in the input map of people.
 * 
 */
  private static void logIn(Map people, String text){
    
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
        String person = (String)entry.getKey();
        String email = (String)entry.getValue();
        System.out.println("Person = " + person + ", Email = " + email);
      
        Message message = createMessage(SENDER_DOMAIN_NAME, person, email, session, text);
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
          Session session, String text) throws MessagingException{
    Message message = new MimeMessage(session);
    try{
      message.setFrom(new InternetAddress(senderDomain));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
      message.setSubject("Changes made to a project.");
      message.setText("Hi, " + pers + ". There were changes made to one "
              + "of your projects."+text);
    } catch (MessagingException e){
      throw e;
    }
    return message;
  }
  
  
  public static void main(String[] args) {
    
    HashMap test = new HashMap();
    test.put("Anna", "agonchar@bu.edu");
    sendMessagesTo(test, " These changes were minor");

  }
}


