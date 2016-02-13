/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author innaturshudzhyan
 */
public class Inventory{
    
    @Getter @Setter private List<String>      samples;
    @Getter @Setter private List<String>      instruments;
    @Getter @Setter private String            id;
    
    public Inventory()
    {
        this.samples     = new ArrayList<>();
        this.instruments = new ArrayList<>();
        this.id          = "Not Set";
    }
    
}
