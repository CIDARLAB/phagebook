package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;

/**
 *
 * @author innaturshudzhyan
 */

public class AddProducts extends HttpServlet {

        @Override
       
        public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        
        String companyName = request.getParameter("CompanyName");
        
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
        
        

        
        Map companyMap = new HashMap();
        companyMap.put("name",companyName);
        //System.out.println("Vendor Name :: " + companyName);
        if (!ClothoAdapter.queryVendor(companyMap,clothoObject, ClothoAdapter.QueryMode.EXACT).isEmpty()){
           writer.println(companyName);
        }
        conn.closeConnection();
         
        writer.flush();
        writer.close();
        
        }
}
