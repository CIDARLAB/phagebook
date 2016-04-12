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
import org.cidarlab.citationsapi.PhagebookCitation;
import org.cidarlab.citationsapi.Pubmed;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.json.JSONObject;

/**
 *
 * @author innaturshudzhyan
 */
public class addPubMed extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Object pCreatedBy = request.getParameter("createdBy");
        String createdBy = pCreatedBy != null ? (String) pCreatedBy : "";
        
        String id = request.getParameter("PMID");

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map loginMap = new HashMap();

        loginMap.put("username", "phagebook");
        loginMap.put("credentials", "backend");
        clothoObject.login(loginMap);

        PhagebookCitation pC = Pubmed.getPhagebookCitation(id);
        pC.setUser(createdBy);

        ClothoAdapter.createPhagebookCitation(pC, clothoObject);
        PhagebookCitation returs = ClothoAdapter.getPhagebookCitation(pC.getId(), clothoObject);

        Person user = ClothoAdapter.getPerson(pC.getUser(), clothoObject);

        user.getPhagebookCitations().add(pC.getId());
        
        clothoObject.logout();
        ClothoAdapter.setPerson(user, clothoObject);
        clothoObject.login(loginMap);
        
        user = ClothoAdapter.getPerson(user.getId(), clothoObject);
        
        
        System.out.println("The id of ph citations " + pC.getTitle() + " " + user.getPhagebookCitations());

        PrintWriter writer = response.getWriter();

        JSONObject json = new JSONObject();
        json.append("title", pC.getTitle());
        json.append("year", pC.getYear());
        json.append("authors", pC.getAuthors());
        json.append("otherInformation", pC.getOtherInformation());
        json.append("bibtex", pC.getBibtex());

        writer.println(json.toString(2));
        writer.flush();
        writer.close();
    }
}
