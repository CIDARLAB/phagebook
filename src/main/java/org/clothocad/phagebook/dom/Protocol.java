/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import org.clothocad.model.Person;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author KatieLewis
 */
public class Protocol {
    @Getter @Setter String           creatorId;
    @Getter @Setter String           protocolName;
    @Getter @Setter List<String>     equipment;
    @Getter @Setter List<String>     samples;
    @Getter @Setter private String   id;
    
    
    public Protocol () 
    {
        this.creatorId       = "Not Set";
        this.protocolName  = "Not Set";
        this.equipment     = new ArrayList<>();
        this.samples       = new ArrayList<>();
        this.id            = "Not Set";
    }
}


