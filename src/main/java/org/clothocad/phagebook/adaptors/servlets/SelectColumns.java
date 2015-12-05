/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author innaturshudzhyan
 */
public class SelectColumns extends HttpServlet{
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    
        String serialNumber = request.getParameter("serialNumber");
        String productName = request.getParameter("productName");
        String productId = request.getParameter("productId");
        String productUrl = request.getParameter("productUrl");
        String productDescription = request.getParameter("productDescription");
        String quantity = request.getParameter("quantity");
        String companyName = request.getParameter("companyName");
        String companyId = request.getParameter("companyId");
        String companyUrl = request.getParameter("companyUrl");
        String companyDescription = request.getParameter("companyDescription");
        String companyContact = request.getParameter("companyContact");
        String companyPhone = request.getParameter("companyPhone");
        String unitPrice = request.getParameter("unitPrice");
        String totalPrice = request.getParameter("totalPrice");
    }
}