/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controller;

/**
 *
 * @author azula
 */
public class Utilities {
    
    public static String getFilepath() {
        String filepath = Utilities.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0, filepath.indexOf("/target/"));
        //System.out.println("\n\nTHIS IS THE FILEPATH: " + filepath + "\n\n");
        return filepath;
    }
    
}
