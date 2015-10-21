/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.ws;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
/**
 *
 * @author KatieLewis
 */
public class PhagebookSocketServer {
    public static void main(String[] args){
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(9090);
        server.addConnector(connector);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        ServletHolder holderEvents = new ServletHolder("ws-events", PhagebookServlet.class);
        context.addServlet(holderEvents, "/phagebook/*");
        
        try
        {
            server.start();
            server.join();
        }
        catch(Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
}
