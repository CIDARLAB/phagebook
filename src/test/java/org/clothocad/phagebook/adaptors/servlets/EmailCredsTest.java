/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.util.ArrayList;
import org.clothocad.phagebook.adaptors.EmailCredentials;
import org.junit.Test;

/**
 *
 * @author anna_g
 */
public class EmailCredsTest {
  
//   @Test
   public void testEmailStuff() throws Exception {
     ArrayList<String> emailAdr = new ArrayList();
     emailAdr.add("agonchar@bu.edu");
     emailAdr.add("anna@goncharova.com");
     emailAdr.add("anna.goncharova00@gmail.com");
     
      String subject = "Testing the new emailing class";
      String body = "<i>Testing HTML properties</i><br><b>This should be bold!</b>";      
//      EmailCredentials.logInAndSendMessage(body,subject, emailAdr);     
     
   }
  
  
}