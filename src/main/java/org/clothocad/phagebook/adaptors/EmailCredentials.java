package org.clothocad.phagebook.adaptors;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static org.clothocad.phagebook.adaptors.EmailHandler.SENDER_DOMAIN_NAME;
import static org.clothocad.phagebook.adaptors.EmailHandler.SENDER_PASSWORD;

/**
 *
 * @author anna_g
 * 
 * This class is the hub for all emailing operations
 * 
 */
public class EmailCredentials {
    public final static String SENDER_DOMAIN_NAME = "phagebook.email@gmail.com";
    public final static String SENDER_PASSWORD = "Phagebook";
    
    private static EmailCredentials instance = null;
    protected EmailCredentials(){
        
    }
    public static EmailCredentials getEmailCredentials(){
        if (instance == null){
            instance = new EmailCredentials();
        }
        return instance; 
    }
    
    /**
    * @param messageBody Pass in the HTML version of the message body
    * @param messageSubject Pass in the subject for this email
    * 
    * Create a session object with the proper email credentials.
    * 
    * Return a boolean to signify success.
    * 
    */
    public static boolean logInAndSendMessage(String messageBody, String messageSubject, 
            String sendTo){
      Properties props = new Properties();

      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.socketFactory.port", "465");
      props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.port", "465");
      props.put("mail.smtp.starttls.enable", "true");
      
      // create a session object with information about Phagebook email address
      Session session = Session.getInstance(props,new javax.mail.Authenticator() {            
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(SENDER_DOMAIN_NAME, SENDER_PASSWORD);
        }
      });
      
      MimeMessage message;
      try{
        // create a message object with default properties
        message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_DOMAIN_NAME));
        message.setSubject(messageSubject);
        message.setContent(messageBody,"text/html");
               
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
        // Send message
        Transport.send(message);       

        return true;
      }catch (MessagingException mex) {
        mex.printStackTrace();
        return false;
      }
    
    }
  
}
