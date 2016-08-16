/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.controllers;

/**
 *
 * @author jacob
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cidarlab.citationsapi.CrossRef;
import org.cidarlab.citationsapi.PhagebookCitation;
import org.cidarlab.citationsapi.Pubmed;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.phagebook.dom.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReferenceController {
    private final String backendPhagebookUser = Args.defaultPhagebookUsername;
    private final String backendPhagebookPassword = Args.defaultPhagebookPassword;

    @RequestMapping(value = "/addCrossRef", method = RequestMethod.POST)
    public void addCrossRef(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {

        Object pCreatedBy = params.get("createdBy");
        String createdBy = pCreatedBy != null ? (String) pCreatedBy : "";

        String id = params.get("DOI");

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map loginMap = new HashMap();

        String username = this.backendPhagebookUser;
        String password = this.backendPhagebookPassword;
        
        loginMap.put("username", username);
        loginMap.put("credentials", password);
        clothoObject.login(loginMap);

        PhagebookCitation pC = CrossRef.getPhagebookCitation(id);
        pC.setUser(createdBy);

        ClothoAdapter.createPhagebookCitation(pC, clothoObject);
        PhagebookCitation returs = ClothoAdapter.getPhagebookCitation(pC.getId(), clothoObject);

        Person user = ClothoAdapter.getPerson(pC.getUser(), clothoObject);

        user.getPhagebookCitations().add(pC.getId());

        ///////////ADD SORT BY YEAR FUNCTIONALITY
//        int numberOfPC = user.getPhagebookCitations().size();
//        for (int i = 0; i<numberOfPC; i++)
//        {
//            
//        }
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
        conn.closeConnection();
        writer.println(json.toString(2));
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "/addCustom", method = RequestMethod.POST)
    public void addCustom(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {

        Object pCreatedBy = params.get("createdBy");
        String createdBy = pCreatedBy != null ? (String) pCreatedBy : "";

        String title = params.get("title");
        String authors = params.get("authors");
        int year = Integer.parseInt(params.get("year"));
        String info = params.get("info");
        String bibtex = "";

        bibtex = "@article{" + authors.substring(authors.indexOf(" "), authors.indexOf(",")).toLowerCase()
                + year;

        String temp = "";

        if (title.indexOf(' ') == 1) {
            temp = title.substring(2, title.length());
            bibtex += temp.substring(0, temp.indexOf(' ')).toLowerCase() + "}, title={";
        } else if (title.indexOf(' ') == 2) {
            temp = title.substring(3, title.length());
            bibtex += temp.substring(0, temp.indexOf(' ')).toLowerCase() + "}, title={";
        } else if (title.indexOf(' ') == 3) {
            temp = title.substring(4, title.length());
            bibtex += temp.substring(0, temp.indexOf(' ')).toLowerCase() + "}, title={";
        } else if (title.replace(" ", "") == title) //if there are no spaces in title
        {
            bibtex += title.substring(0, title.length()).toLowerCase() + "}, title={";
        } else {
            bibtex += title.substring(0, title.indexOf(' ')).toLowerCase() + "}, title={";
        }

        bibtex += title + "}, author{" + authors + "}, other information{" + info + "}}";

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map loginMap = new HashMap();
        String username = this.backendPhagebookUser;
        String password = this.backendPhagebookPassword;
        loginMap.put("username", username);
        loginMap.put("credentials", password);
        clothoObject.login(loginMap);

        PhagebookCitation pC = new PhagebookCitation(title, authors, year, info, bibtex);

        pC.setUser(createdBy);

        ClothoAdapter.createPhagebookCitation(pC, clothoObject);
        PhagebookCitation returs = ClothoAdapter.getPhagebookCitation(pC.getId(), clothoObject);

        Person user = ClothoAdapter.getPerson(pC.getUser(), clothoObject);

        user.getPhagebookCitations().add(pC.getId());

        clothoObject.logout();
        ClothoAdapter.setPerson(user, clothoObject);
        clothoObject.login(loginMap);

        user = ClothoAdapter.getPerson(user.getId(), clothoObject);

        PrintWriter writer = response.getWriter();

        JSONObject json = new JSONObject();
        json.append("title", pC.getTitle());
        json.append("year", pC.getYear());
        json.append("authors", pC.getAuthors());
        json.append("otherInformation", pC.getOtherInformation());
        json.append("bibtex", pC.getBibtex());
        conn.closeConnection();
        writer.println(json.toString(2));
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "/addPubMed", method = RequestMethod.POST)
    public void addPubMed(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {

        Object pCreatedBy = params.get("createdBy");
        String createdBy = pCreatedBy != null ? (String) pCreatedBy : "";

        String id = params.get("PMID");

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map loginMap = new HashMap();
        
        String username = this.backendPhagebookUser;
        String password = this.backendPhagebookPassword;
        loginMap.put("username", username);
        loginMap.put("credentials", password);
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

        conn.closeConnection();

        writer.println(json.toString(2));
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "/loadPublications", method = RequestMethod.GET)
    protected void loadPublications(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        String clothoId = (String) params.get("clothoId");

        boolean isValid = false;
        if (clothoId != null && clothoId != "") {
            isValid = true;
        }

        if (isValid) {

//            //ESTABLISH CONNECTION
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = this.backendPhagebookUser;
            String password = this.backendPhagebookPassword;
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);

            Person person = ClothoAdapter.getPerson(clothoId, clothoObject);

            JSONObject phagebookCitationsAsJSON = new JSONObject();
            phagebookCitationsAsJSON.put("phagebookCitationIdJSON", person.getPhagebookCitations());

            JSONArray citations = new JSONArray();

            Map<Integer, List<PhagebookCitation>> yearCitationsMap = new HashMap<Integer, List<PhagebookCitation>>();
            List<Integer> years = new ArrayList<Integer>();
            for (String pubId : person.getPhagebookCitations()) {
                PhagebookCitation pC = ClothoAdapter.getPhagebookCitation(pubId, clothoObject);
                if (!years.contains(pC.getYear())) {
                    years.add(pC.getYear());
                }
                if (!yearCitationsMap.containsKey(pC.getYear())) {
                    yearCitationsMap.put(pC.getYear(), new ArrayList<PhagebookCitation>());

                }
                yearCitationsMap.get(pC.getYear()).add(pC);
                /*JSONObject pubJSON = new JSONObject();
                
                pubJSON.put("pubTitle", pC.getTitle());
                pubJSON.put("pubAuthors", pC.getAuthors());
                pubJSON.put("pubYear", pC.getYear());
                pubJSON.put("pubInfo", pC.getOtherInformation());
                pubJSON.put("pubBibtex", pC.getBibtex());
                citations.put(pubJSON);
                 */
            }

            for (int i = 0; i < years.size(); i++) {
                for (int j = 0; j < years.size(); j++) {
                    if (years.get(i) > years.get(j)) {
                        int temp = years.get(i);
                        years.set(i, years.get(j));
                        years.set(j, temp);
                    }
                }
            }

            List<PhagebookCitation> allCitations = new ArrayList<PhagebookCitation>();
            for (int i = 0; i < years.size(); i++) {
                allCitations.addAll(yearCitationsMap.get(years.get(i)));
            }

            for (PhagebookCitation pC : allCitations) {
                JSONObject pubJSON = new JSONObject();

                pubJSON.put("pubTitle", pC.getTitle());
                pubJSON.put("pubAuthors", pC.getAuthors());
                pubJSON.put("pubYear", pC.getYear());
                pubJSON.put("pubInfo", pC.getOtherInformation());
                pubJSON.put("pubBibtex", pC.getBibtex());
                citations.put(pubJSON);
            }

            response.setContentType("application/json");
//            
//            String id = person.getPhagebookCitations().get(0);
//            
//            PhagebookCitation p = ClothoAdapter.getPhagebookCitation(id, clothoObject);

            PrintWriter writer = response.getWriter();

            writer.println(citations);
            writer.flush();
            writer.close();

//            Person retrieve = ClothoAdapter.getPerson(clothoId, clothoObject);
//            
//            JSONObject statusesAsJSON = new JSONObject();
//        
//            statusesAsJSON.put("statusIdJSON", retrieve.getStatuses());
//            JSONArray statuses = new JSONArray();
//            for ( String statId : retrieve.getStatuses()){
//                Status tempStat = ClothoAdapter.getStatus(statId, clothoObject);
//                
//                JSONObject statJSON = new JSONObject();
//                
//                statJSON.put("statusText", tempStat.getText());
//                statJSON.put("dateCreated", tempStat.getCreated().toString());
//                
//                statuses.put(statJSON);
//            }
//            response.setContentType("application/json");
//            PrintWriter out = response.getWriter();
//            out.print(statuses);
//            out.flush();
//            out.close();
            conn.closeConnection();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
    }
}
