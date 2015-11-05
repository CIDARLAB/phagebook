/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.ws;
import javax.servlet.annotation.WebServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
@SuppressWarnings("serial")
/**
 *
 * @author KatieLewis
 */
public class PhagebookServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory factory){
        factory.register(PhagebookSocket.class);
    }
}
