/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.clothocad.phagebook.adaptors.S3Adapter;

/**
 *
 * @author azula
 */
@WebServlet(name = "uploadProfilePicture", urlPatterns = {"/uploadProfilePicture"})
@MultipartConfig
public class uploadProfilePicture extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet uploadProfilePicture</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet uploadProfilePicture at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    private static String getValue(Part part) throws IOException {
        System.out.println("Reached get Value");
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            System.out.print(".");
            value.append(buffer, 0, length);
        }
        System.out.println("Value :: " + value.toString());
        return value.toString();
    }
    
     public static String getFilepath()
    {
        String filepath = uploadProfilePicture.class.getClassLoader().getResource(".").getPath();
        System.out.println("File path " + filepath);
        if(filepath.contains("WEB-INF/classes/")){
            filepath = filepath.substring(0,filepath.indexOf("WEB-INF/classes/"));
        }
        else if(filepath.contains("target/classes/"))
        {
            filepath = filepath.substring(0,filepath.indexOf("target/classes/"));
        }
        filepath += "upload/";
        return filepath;
    }
    
    public File partConverter(Part part, String fileName) throws IOException {
        String pathAndName = getFilepath() + fileName;
        
        OutputStream out = null;
        InputStream filecontent = null;
        
        try {
            out = new FileOutputStream(new File(pathAndName));
            filecontent = part.getInputStream();

            int read;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException fne) {
            Logger.getLogger(uploadProfilePicture.class.getName()).log(Level.SEVERE, null, fne);
        }
        
        return new File(pathAndName);
    }
    
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processPostRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(uploadProfilePicture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        // Disable the cache once and for all
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache");                                   // HTTP 1.0.
        response.setDateHeader("Expires", 0);
        System.out.println("Reached the Do post part.. Wohhoo");
        String filename = getValue(request.getPart("profilePictureName"));
        File profilePic = partConverter(request.getPart("profilePicture"),filename);
        String clothoId = getValue(request.getPart("clothoId"));
        S3Adapter.uploadProfilePicture(clothoId, profilePic);
        
        System.out.println(filename);
    
    }
    
    
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
