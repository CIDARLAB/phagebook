/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdaptor;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.controller.OrderController;
import org.clothocad.phagebook.dom.Product;
import org.json.JSONArray;
import org.json.JSONObject;
//import net.sf.json.JSONArray;

/**
 *
 * @author innaturshudzhyan
 */
public class inputProduct extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        //System.out.println("Request :: " + request.getParameter("list"));
        JSONObject papaParseData = new JSONObject(request.getParameter("list"));
        JSONArray data = new JSONArray();
        data = (JSONArray) papaParseData.get("data");
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map createUserMap = new HashMap();
        String username = "phagebook";
        createUserMap.put("username", username);
        createUserMap.put("password", "password");

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", "password");

        clothoObject.login(loginMap);
        
        List<Product> products = new ArrayList<Product>();
        products = OrderController.getProducts(data, clothoObject);

        
        for(Product product:products){
            ClothoAdaptor.createProduct(product, clothoObject);
        }
        conn.closeConnection();
        //writer.println("Done!");
        writer.println(products.size() + " products created");
        writer.flush();
        writer.close();

    }
}
