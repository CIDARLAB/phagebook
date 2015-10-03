/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author innaturshudzhyan
 */
public class Inventory{
    
    @Getter
    @Setter
    private List<Sample> samples;
    
    @Getter
    @Setter
    private List<Instrument> instruments;
    
}
