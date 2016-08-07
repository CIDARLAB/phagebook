///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.clothocad.phagebook.adaptors.ws;
//import javax.servlet.MultipartConfigElement;
//import javax.servlet.http.HttpServletRequest;
//import org.clothocad.phagebook.adaptors.servlets.uploadProfilePicture;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.handler.HandlerList;
//
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.eclipse.jetty.webapp.WebAppContext;
//import org.eclipse.jetty.websocket.WebSocket;
//import org.eclipse.jetty.websocket.WebSocketServlet;
///**
// *
// * @author KatieLewis
// */
//public class PhagebookSocketServer {
//    public static void main(String[] args){
//        Server server = new Server(9090);
//        
//        WebSocketServlet wsServlet = new WebSocketServlet() {
//            @Override
//            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
//                return new PhagebookSocket(); 
//            }
//        }; 
//        
//        
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        
//        ServletHolder fileUploadServletHolder = new ServletHolder(new uploadProfilePicture());
//        fileUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
//        context.addServlet(fileUploadServletHolder, "/uploadProfilePicture");
//        
//        
//        ServletHolder holderEvents = new ServletHolder("ws-events", PhagebookServlet.class);
//        context.addServlet(new ServletHolder(wsServlet), "/websocket/");        
//        
//        WebAppContext contextWeb = new WebAppContext();
//        contextWeb.setDescriptor(context + "/WEB-INF/web.xml");
//        contextWeb.setResourceBase("../phagebook/src/main/webapp");
//        contextWeb.setContextPath("/");
//        contextWeb.setParentLoaderPriority(true);
//        
//        HandlerList handlers = new HandlerList();
//        handlers.addHandler(context);
//        handlers.addHandler(contextWeb);
//        server.setHandler(handlers);
//        
//        try
//        {
//            server.start();
//            server.join();
//        }
//        catch(Throwable t)
//        {
//            t.printStackTrace(System.err);
//        }
//    }
//}
