/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.websocket;

import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.controller.Args;
import org.junit.Test;

/**
 *
 * @author innaturshudzhyan
 */
public class clothoTest {
    
    @Test
    public void getPerson(){
        ClothoConnection clothoConn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(clothoConn);
        
        System.out.println("Person :: \n" +clothoObject.get("56fffd01d4c6f8a98ae2fab9"));
    }
    
}
