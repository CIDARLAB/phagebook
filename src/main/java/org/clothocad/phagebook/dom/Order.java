/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;


import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author prash,innaturshudzhyan
 */

public class Order {
    
    @Getter
    @Setter
    private List<Company> CompanyList;
    
    @Getter
    @Setter
    private List<Good> GoodList;
    
    @Getter
    @Setter
    private String id; 
    
    /**
     *
     * @param name
     */
    public Order(String name) {};   
    
}
