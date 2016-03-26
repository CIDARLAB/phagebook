/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.security;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 *
 * @author anna_g
 */
public class Log {
    private static Logger logger = Logger.getLogger("Log");

    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();
    
    

}
