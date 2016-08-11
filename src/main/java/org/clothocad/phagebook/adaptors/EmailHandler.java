/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.lang.Exception;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.clothocad.model.Person;

/**
 *
 * @author Herb
 */
//Singleton Handler
public class EmailHandler {
    // <editor-fold defaultstate="collapsed" desc="Johan's Email info">
    public static String SENDER_DOMAIN_NAME = "phagebook.email@gmail.com";
    public static String SENDER_PASSWORD = "Phagebook";
    // </editor-fold>
    private static EmailHandler instance = null;
    protected EmailHandler(){
        
    }
    public static EmailHandler getEmailHandler(){
        if (instance == null){
            instance = new EmailHandler();
        }
        return instance; 
    }
    //TODO: Implement a person type object.
    

    public boolean sendEmailVerification(Person pers, String link){       
            //try to send an email and stuff
            //eventually check if person is verified and then throw an exception or something
            //so it doesn't look like you ain't got your shit together
            if (!checkVerificationStatus(pers)){
                Properties props = new Properties();
		
                props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
                props.put("mail.smtp.starttls.enable", "true");
                
                Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(SENDER_DOMAIN_NAME, SENDER_PASSWORD);
                            }
                        });
                try {

                    Message message = createMessage(SENDER_DOMAIN_NAME, pers, session, link);
                    Transport.send(message);
                    System.out.println("An Email Was Sent");

                } catch (MessagingException e) {
                //if you can't send it from the info stored in the person class then
                //get HANGRY
                    e.printStackTrace();
                    return false;
                }
            //all is good if we get here and therefore we can return true
            return true;
            } else {
                System.out.println("User is already verified.");
                return false;
            }
   }
  public Message createMessage(String senderDomain, Person pers, Session session, String link) throws MessagingException{
      Message message = new MimeMessage(session);
                try{
                    message.setFrom(new InternetAddress(SENDER_DOMAIN_NAME));
                    message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(pers.getEmailId()));
                    message.setSubject("Phagebook Activation Email");
                    message.setText("Hi " + pers.getFirstName() + " \n\n Please Validate"
                        + " Your email by following this link: " + link );
                } catch (MessagingException e){
                    throw e;
                }
        return message;
  }
  
  public void startVerification(Person pers, String code){
        //do stuff to begin checking verification dates
        //pers.verify(code);
    }
    
    public boolean checkVerificationStatus(Person pers){
        if (pers.isActivated()){
            return true;
        }
        return false;
    }
   


}