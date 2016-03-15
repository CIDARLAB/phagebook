/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.util;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.GoodType;
import org.clothocad.phagebook.dom.Product;
import org.clothocad.phagebook.dom.Vendor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Herb
 */
public class ClothoAutoCompleteTester {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
     @Test
    public void autocomplete(){
       System.out.println("------------- TESTING AUTO COMPLETE ----------------");
       ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
       Clotho clothoObject = new Clotho(conn);
       String query = "Tel";
    
       System.out.println("testing autocomplete with query: " + query);
       Object ret = clothoObject.autocomplete(query);
       JSONArray replies = (JSONArray) ret;
       System.out.println("Autocomplete Result :: " + ret.toString());
       
       conn.closeConnection();
       System.out.println("-----------------------------");
    }
    
    
    @Test
    public void startsWith(){
       System.out.println("------------- TESTING STARTS WITH ----------------");
       ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
       Clotho clothoObject = new Clotho(conn);
       Map map = new HashMap();
       map.put("query", "T");
       map.put("key", "name");
       Object ret = clothoObject.startsWith(map);
       System.out.println("Result :: " + ret.toString());
       
       conn.closeConnection();
       System.out.println("--------------------------");
    }
}
