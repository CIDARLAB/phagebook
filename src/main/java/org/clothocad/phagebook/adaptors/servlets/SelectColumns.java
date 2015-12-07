/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
import static org.clothocad.phagebook.controller.OrderController.createOrderForm;
import org.clothocad.phagebook.dom.Order;
import org.clothocad.phagebook.dom.OrderColumns;

/**
 *
 * @author innaturshudzhyan
 */
public class SelectColumns extends HttpServlet{
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    /*
        String SERIAL_NUMBER = request.getParameter("serialNumber");
        String PRODUCT_NAME = request.getParameter("productName");
        String PRODUCT_URL = request.getParameter("productUrl");
        String PRODUCT_DESCRIPTION = request.getParameter("productDescription");
        String QUANTITY = request.getParameter("quantity");
        String COMPANY_NAME = request.getParameter("companyName");
        String COMPANY_URL = request.getParameter("companyUrl");
        String COMPANY_DESCRIPTION = request.getParameter("companyDescription");
        String COMPANY_CONTACT = request.getParameter("companyContact");
        String COMPANY_PHONE = request.getParameter("companyPhone");
        String UNIT_PRICE = request.getParameter("unitPrice");
        String TOTAL_PRICE = request.getParameter("totalPrice");
    */
       System.out.println("Reached doPost");
       String id = request.getParameter("orderId");
       System.out.println(id);
       if ((id!=null) && (!id.equals("")))
       {    
            System.out.println("ID is not null");
            List<OrderColumns> orderColumns = new ArrayList<>();
            System.out.println("Serial Number " + request.getParameter("serialNumber"));
            System.out.println("Product Name :: " +request.getParameter("productName"));
            if("true".equals(request.getParameter("serialNumber")))
                
            {
                orderColumns.add(OrderColumns.SERIAL_NUMBER);
            }
            
            if("true".equals(request.getParameter("productName"))){
                orderColumns.add(OrderColumns.PRODUCT_NAME);
            }
            if("true".equals(request.getParameter("productUrl"))){
                orderColumns.add(OrderColumns.PRODUCT_URL);
            }
            if("true".equals(request.getParameter("productDescription"))){
                orderColumns.add(OrderColumns.PRODUCT_DESCRIPTION);
            }
            if("true".equals(request.getParameter("quantity"))){
                orderColumns.add(OrderColumns.QUANTITY);
            }
            if("true".equals(request.getParameter("companyName"))){
                orderColumns.add(OrderColumns.COMPANY_NAME);
            }
            if("true".equals(request.getParameter("companyUrl"))){
                orderColumns.add(OrderColumns.COMPANY_URL);
            }
            if("true".equals(request.getParameter("companyDescription"))){
                orderColumns.add(OrderColumns.COMPANY_DESCRIPTION);
            }
            if("true".equals(request.getParameter("companyContact"))){
                orderColumns.add(OrderColumns.COMPANY_CONTACT);
            }
            if("true".equals(request.getParameter("companyPhone"))){
                orderColumns.add(OrderColumns.COMPANY_PHONE);
            }
            if("true".equals(request.getParameter("unitPrice"))){
                orderColumns.add(OrderColumns.UNIT_PRICE);
            }
            if("true".equals(request.getParameter("totalPrice"))){
                orderColumns.add(OrderColumns.TOTAL_PRICE);
            }   
            
            System.out.println("Order Columns " + orderColumns);
            
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
            System.out.println("HERE AT SELECT 1");
            Order order = ClothoAdaptor.getOrder(id, clothoObject);
            System.out.println("HERE AT SELECT 2");
            List<String> orderFormLines = createOrderForm(order,orderColumns);
            System.out.println(orderFormLines);
            
            String filepath = SelectColumns.class.getClassLoader().getResource(".").getPath();
            System.out.println("File path ::" + filepath);
            filepath = filepath.substring(0, filepath.indexOf("/target/"));
            System.out.println("\nTHIS IS THE FILEPATH: " + filepath);
            
            String filepathOrderForm = filepath + "/orderForm.csv";
            File file = new File(filepathOrderForm);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(String line : orderFormLines)
            {
                writer.write(line);
                writer.newLine();
            }
            
            writer.flush();
            writer.close();
            
            PrintWriter reponseWriter = response.getWriter();
            reponseWriter.println(filepathOrderForm);
            reponseWriter.flush();
            reponseWriter.close();
       }
       else
       {
           response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
       }
                        
    }
}