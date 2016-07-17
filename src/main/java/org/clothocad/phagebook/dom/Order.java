/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.dom;

//experiment, sample, institution=
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash,innaturshudzhyan
 */
public class Order implements ClothoBaseObject {

    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private Date dateCreated;
    @Getter
    @Setter
    private Date dateApproved;
    @Getter
    @Setter
    private String createdById;
    @Getter
    @Setter
    private List<String> products; //CART ITEM ID
    @Getter
    @Setter
    private Double budget;
    @Getter
    @Setter
    private Integer maxOrderSize;
    @Getter
    @Setter
    private String approvedById;
    @Getter
    @Setter
    private List<String> receivedByIds;
    @Getter
    @Setter
    private String relatedProjectId;
    @Getter
    @Setter
    private OrderStatus status;
    @Getter
    @Setter
    private String affiliatedLabId;
    @Getter
    @Setter
    private Double taxRate;

//    public Map<Product, Integer> getProducts(){
//        return this.products;
//    }
//    public void setProducts(Map<Product, Integer> products){
//        this.products = products;
//    }
    /**
     *
     * @param name
     */
    public Order(String name) {
        this.name = name;

        this.products = new ArrayList<>();
    }

    ;   
    
    public Order() {
        this.id = "Not Set";
        this.name = "Not Set";
        this.description = "Not Set";
        this.dateCreated = new Date();
        this.dateApproved = new Date();

        this.createdById = "Not Set";
        this.products = new ArrayList<>();
        this.budget = 0.0d;
        this.maxOrderSize = 1;
        this.approvedById = "Not Set";
        this.receivedByIds = new ArrayList<>();
        this.relatedProjectId = "Not Set";
        this.status = OrderStatus.INPROGRESS;
        this.taxRate = 0.0d;
        this.affiliatedLabId = "Not Set";

    }

    @Override
    public String schemaForObject(Object self) {
        return Order.class.getCanonicalName();
    }

    @Override
    public String idForObject() {
        return this.id;
    }

    public enum OrderStatus {
        INPROGRESS,
        APPROVED,
        SUBMITTED,
        DENIED,
        RECEIVED
    }

    public enum OrderColumns {
        SERIAL_NUMBER,
        PRODUCT_NAME,
        PRODUCT_ID,
        PRODUCT_URL,
        PRODUCT_DESCRIPTION,
        QUANTITY,
        COMPANY_NAME,
        COMPANY_ID,
        COMPANY_URL,
        COMPANY_DESCRIPTION,
        COMPANY_CONTACT,
        COMPANY_PHONE,
        UNIT_PRICE,
        CUSTOM_UNIT_PRICE,
        TOTAL_PRICE
    }

}
