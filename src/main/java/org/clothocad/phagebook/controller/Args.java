/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controller;

/**
 *
 * @author prash
 */
public class Args {
    
    public static final String clothoLocation = "wss://localhost:8443/websocket";
    
    public static String getFilepath()
    {
        String filepath = Args.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.lastIndexOf("/target/"));
        filepath += "/";
        return filepath;
    }
    
    public static String getResourcesFilepath(){
        return getFilepath() + "resources/";
    }
}
