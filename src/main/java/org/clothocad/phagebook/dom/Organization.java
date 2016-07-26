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
 * @author prash
 */
public class Organization implements ClothoBaseObject {

    @Getter
    @Setter
    protected String id;
    @Getter
    @Setter
    protected String name;
    @Getter
    @Setter
    protected String description;
    @Getter
    @Setter
    protected String phone;
    @Getter
    @Setter
    protected String url;

    public Organization(String name) {
        this.name = name;
    }

    public Organization(String name, String description, String phone, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.phone = phone;
    }

    public Organization() {
        this.id = "Not Set";
        this.name = "Not Set";
        this.description = "Not Set";
        this.phone = "Not Set";
        this.url = "Not Set";
    }

    @Override
    public String toString() {

        String result = "Organization is ";

        if (!this.name.equals("")) {
            result += this.name;
        }

        return result;

    }

    @Override
    public String schemaForObject(Object self) {
        return Organization.class.getCanonicalName();
    }

    @Override
    public String idForObject() {
        return this.id;
    }

}
