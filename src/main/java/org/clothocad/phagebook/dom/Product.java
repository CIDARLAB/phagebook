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
 * @author innaturshudzhyan
 */
public class Product extends Good{
    
    @Getter
    @Setter
    private String ProductLink, Company;
    
    @Getter
    @Setter
    private Good GoodType;
    
    @Getter
    @Setter
    private float price;
    
}
