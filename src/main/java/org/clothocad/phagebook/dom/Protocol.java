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
    @Getter @Setter Person creator;
    @Getter @Setter String  protocolName;
    @Getter @Setter List<Instrument> equipment = new ArrayList<>();
    @Getter @Setter List<Sample> samples = new ArrayList<>();
    @Getter @Setter private String id;
    
    
    public Protocol () {}
}


