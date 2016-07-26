package org.clothocad.phagebook.adaptors.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cidarlab.citationsapi.PhagebookCitation;
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
public class addCustom extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Object pCreatedBy = request.getParameter("createdBy");
        String createdBy = pCreatedBy != null ? (String) pCreatedBy : "";
        
        String title = request.getParameter("title");
        String authors = request.getParameter("authors");
        int year = Integer.parseInt(request.getParameter("year"));
        String info = request.getParameter("info");
        String bibtex = "";
        
        bibtex = "@article{" + authors.substring(authors.indexOf(" "), authors.indexOf(",")).toLowerCase()
                + year;
        
        
        String temp = "";
        
        if (title.indexOf(' ') == 1) {
            temp = title.substring(2, title.length());
            bibtex += temp.substring(0, temp.indexOf(' ')).toLowerCase() + "}, title={";
        } else if(title.indexOf(' ') == 2) {
            temp = title.substring(3, title.length());
            bibtex += temp.substring(0, temp.indexOf(' ')).toLowerCase() + "}, title={";
        }
        else if (title.indexOf(' ') == 3) {
            temp = title.substring(4, title.length());
            bibtex += temp.substring(0, temp.indexOf(' ')).toLowerCase() + "}, title={";
        } else {
            if (title.replace(" ", "") == title) //if there are no spaces in title
                bibtex += title.substring(0, title.length()).toLowerCase() + "}, title={";
            else
                bibtex += title.substring(0, title.indexOf(' ')).toLowerCase() + "}, title={";
        }

        bibtex += title + "}, author{" + authors + "}, other information{" + info + "}}";

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map loginMap = new HashMap();

        loginMap.put("username", "phagebook");
        loginMap.put("credentials", "backend");
        clothoObject.login(loginMap);

        PhagebookCitation pC = new PhagebookCitation(title,authors,year,info,bibtex);
                
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
}
