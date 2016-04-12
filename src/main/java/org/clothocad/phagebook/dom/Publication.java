/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author KatieLewis
 */
public class Publication 
{
    @Getter @Setter private String  id;
    @Getter @Setter private String  title;
    @Getter @Setter private String  authors;
    @Getter @Setter private int     year;
    @Getter @Setter private String  info;
    @Getter @Setter private String  bibtex;
    
    public Publication()
    {
        this.id = "Not Set";
        this.title = "Not Set";
    }
}
