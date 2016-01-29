/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Institution;
import org.clothocad.model.Person;
import org.clothocad.model.Person.PersonRole;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Publication;
import org.clothocad.phagebook.dom.Status;
import org.eclipse.jetty.websocket.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author KatieLewis
 */
public class PhagebookSocket
        implements WebSocket.OnTextMessage {

    private WebSocket.Connection connection;

    @Override
    public void onMessage(String data) {
        try {
            connection.sendMessage(handleIncomingMessage(data).toString());
        } catch (IOException ex) {
            Logger.getLogger(PhagebookSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onOpen(Connection connection) {
        this.connection = connection;
        connection.setMaxIdleTime(7 * 24 * 3600000);
        connection.setMaxBinaryMessageSize(999999);
        connection.setMaxTextMessageSize(999999);
        System.out.println("New Connection opened :: " + connection.getProtocol());

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClose(int closeCode, String message) {
        System.out.println("Connection Closed");
    }

    private JSONObject handleIncomingMessage(String message) {
        JSONObject messageObject = new JSONObject();
        JSONParser parser = new JSONParser();

        try {
            messageObject = (JSONObject) parser.parse(message);
        } catch (ParseException ex) {
            Logger.getLogger(PhagebookSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("INCOMING MESSAGE Object" + messageObject.toString());
        JSONObject result = new JSONObject();
        result.put("channel", (String) messageObject.get("channel"));
        result.put("requestId", messageObject.get("requestId"));
        if (isValidMessage(messageObject)) {
            switch (getChannel((String) messageObject.get("channel"))) {
                case createStatus:
                    break;
                case updateOrderStatus:
                    break;
                case login:
                    break;
                default:
                    result.put("data", "");
                    break;
            }
        }
        return result;
    }

    private boolean isValidMessage(JSONObject messageObject) {
        boolean isValid = true;

        return isValid;
    }

    private Channel getChannel(String messageChannel) {
        Channel channel = null;
        channel = Channel.valueOf(messageChannel);
        return channel;
    }

}
