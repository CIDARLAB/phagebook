/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

/**
 *
 * @author innaturshudzhyan
 */
public class Sample extends Good implements ClothoBaseObject{
    
    public Sample(String name)
    {
        super(name);
    }
    
    public Sample(){ super(); }

    @Override
    public String schemaForObject(Object self) {
        return Sample.class.getCanonicalName();
    }

    @Override
    public String idForObject() {
        return this.id;
    }
}
