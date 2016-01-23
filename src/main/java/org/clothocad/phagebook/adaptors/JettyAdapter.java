/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 *
 * @author prash
 */
public class JettyAdapter {
    public static void main(String[] args) {
      
        Server server = new Server(9090);
        try {
            
            WebAppContext context = new WebAppContext();
            context.setDescriptor(context+"/WEB-INF/web.xml");
            context.setResourceBase("../phagebook/src/main/webapp");
            context.setContextPath("/");
            context.setParentLoaderPriority(true);
 
            server.setHandler(context);
            
            
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(JettyAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
}
