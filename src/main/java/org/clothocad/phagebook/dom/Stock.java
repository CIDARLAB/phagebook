/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;
/**
 *
 * @author Herb
 */
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class Stock 
{
    @Getter @Setter Map<Product, Integer> stock;
    @Getter @Setter String id; 
    
    
    Stock()
    {
        this.stock = new HashMap<>();
    }
}
