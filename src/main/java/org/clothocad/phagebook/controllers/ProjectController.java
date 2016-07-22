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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.adaptors.sendEmails;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Project;
import org.clothocad.phagebook.dom.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProjectController {

    @RequestMapping(value = "getAllProjects", method = RequestMethod.GET)
    protected void getAllProjectsGet(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map createUserMap = new HashMap();
        String username = "username";
        String password = "password";

        createUserMap.put("username", username);
        createUserMap.put("password", password);

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);
        System.out.println("in getAllProjects!");

        // get userId from the cookie
        Object userIDC = params.get("userID");
        String userID = userIDC != null ? (String) userIDC : "";
        System.out.println(userID);

        // get person 
        Person user = ClothoAdapter.getPerson(userID, clothoObject);
        System.out.println("user is");
        System.out.println(user.getFirstName() + " " + user.getLastName());

        // get the projects array
        List<String> projectsList = user.getProjects();
        System.out.println(projectsList);
        JSONArray listOfProjects = new JSONArray();

        // get and add each project to the response
        if (projectsList.size() > 0) {

            for (int i = 0; i < projectsList.size(); i++) {
                JSONObject projectObject = new JSONObject();

                System.out.println("Person has a bunch of projects");
                String projectId = projectsList.get(i);

                Project proj = ClothoAdapter.getProject(projectId, clothoObject);
                projectObject.put("projectId", projectId);

                projectObject.put("description", proj.getDescription());
                System.out.println("\n");
                System.out.println("Description is: \n" + proj.getDescription());

                projectObject.put("budget", proj.getBudget());
                System.out.println("\n");
                System.out.println("budget is " + proj.getBudget());

                projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
                List<String> afflLabs = proj.getAffiliatedLabs();
                for (int k = 0; k < proj.getAffiliatedLabs().size(); k++) {
                    System.out.println("\n");
                    System.out.println(afflLabs.get(k));
                }

                projectObject.put("projectName", proj.getName());
                System.out.println("\n");
                System.out.println("projectName is " + proj.getName());

                //projectObject.put("notebooks", proj.getNotebooks());
                System.out.println("\n");
                projectObject.put("updates", proj.getUpdates());
                System.out.println("\n");
                System.out.println("updates are " + proj.getUpdates());

                Grant grant = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);
                projectObject.put("grant", grant.getId());
                System.out.println("\n");
                System.out.println("grant is " + grant.getId());

                if (proj.getDateCreated() != null) {
                    String delims = "[ ]+";
                    String stringDate = proj.getDateCreated().toString();
                    System.out.println("\n");
                    System.out.println(stringDate);
                    String[] tokens = stringDate.split(delims);
                    projectObject.put("dateCreated", tokens[1] + " " + tokens[2] + " " + tokens[5]);
                }
                if (proj.getMembers() != null) {
                    // just return the size of the array
                    int membersSize = proj.getMembers().size();
                    List<String> members = proj.getMembers();
                    List<String> membersNames = new ArrayList<String>();
                    // get members' names
                    for (int j = 0; j < members.size(); j++) {
                        Person member = ClothoAdapter.getPerson(members.get(j), clothoObject);
                        membersNames.add(member.getFirstName() + " " + member.getLastName());
                    }
                    projectObject.put("membersNames", membersNames);
                    projectObject.put("membersNum", membersSize);
                    projectObject.put("membersIds", members);
                    System.out.println("\n");
                    System.out.println("membersNum is " + membersSize);
                    System.out.println("membersNames is " + members);
                    System.out.println("members are: " + members);

                }
                if (!(proj.getCreatorId().equals(""))) {
                    System.out.println("CREATOR ID ISN'T NULL!");
                    Person creator = ClothoAdapter.getPerson(proj.getCreatorId(), clothoObject);
                    System.out.println(creator.getFirstName() + " " + creator.getLastName());
                    projectObject.put("creatorId", proj.getCreatorId());
                    projectObject.put("creator", creator.getFirstName() + " " + creator.getLastName());
                }
                //projectObject.put("members", proj.getMembers());

                if (!(proj.getLeadId().equals(""))) {
                    System.out.println("LEAD ID ISN'T NULL!");
                    System.out.println("getting lead");
                    String leadtId = proj.getLeadId();
                    System.out.println(leadtId);
                    Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);

                    if (lead.getFirstName() != null && lead.getLastName() != null) {
                        System.out.println("LEAD'S NAME IS:");
                        System.out.println(lead.getFirstName() + " " + lead.getLastName());
                        projectObject.put("leadId", proj.getLeadId());
                        projectObject.put("lead", lead.getFirstName() + " " + lead.getLastName());
                    }
                    // add the project to the json object of all projects
                    listOfProjects.put(projectObject);
                }
                System.out.println("***");
                System.out.println("RESULT IS:");
                System.out.println(listOfProjects);
                System.out.println("***");
                //projectMap = (Map) ClothoAdapter.getProject(id, clothoObject);
                System.out.println("after getProject");
                System.out.println(projectObject);
            }
        }
        conn.closeConnection();

        writer.println(listOfProjects); //Send back stringified JSON object
        writer.flush();
        writer.close();

    }

    @RequestMapping(value = "getAllProjects", method = RequestMethod.POST)
    protected void getAllProjectsPost(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map createUserMap = new HashMap();
        String username = "username";
        String password = "password";

        createUserMap.put("username", username);
        createUserMap.put("password", password);

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);
        System.out.println("in getAllProjects!");

        // get userId from the cookie
        Object userIDC = params.get("userID");
        String userID = userIDC != null ? (String) userIDC : "";
        System.out.println(userID);

        // get person 
        Person user = ClothoAdapter.getPerson(userID, clothoObject);
        System.out.println("user is");
        System.out.println(user.getFirstName() + " " + user.getLastName());

        // get the projects array
        List<String> projectsList = user.getProjects();
        System.out.println(projectsList);
        JSONArray listOfProjects = new JSONArray();

        // get and add each project to the response
        if (projectsList.size() > 0) {

            for (int i = 0; i < projectsList.size(); i++) {
                JSONObject projectObject = new JSONObject();

                System.out.println("Person has a bunch of projects");
                String projectId = projectsList.get(i);

                Project proj = ClothoAdapter.getProject(projectId, clothoObject);
                projectObject.put("projectId", projectId);

                projectObject.put("description", proj.getDescription());
                System.out.println("\n");
                System.out.println("Description is: \n" + proj.getDescription());

                projectObject.put("budget", proj.getBudget());
                System.out.println("\n");
                System.out.println("budget is " + proj.getBudget());

                projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
                List<String> afflLabs = proj.getAffiliatedLabs();
                for (int k = 0; k < proj.getAffiliatedLabs().size(); k++) {
                    System.out.println("\n");
                    System.out.println(afflLabs.get(k));
                }

                projectObject.put("projectName", proj.getName());
                System.out.println("\n");
                System.out.println("projectName is " + proj.getName());

                //projectObject.put("notebooks", proj.getNotebooks());
                System.out.println("\n");
                projectObject.put("updates", proj.getUpdates());
                System.out.println("\n");
                System.out.println("updates are " + proj.getUpdates());

                Grant grant = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);
                projectObject.put("grant", grant.getId());
                System.out.println("\n");
                System.out.println("grant is " + grant.getId());

                if (proj.getDateCreated() != null) {
                    String delims = "[ ]+";
                    String stringDate = proj.getDateCreated().toString();
                    System.out.println("\n");
                    System.out.println(stringDate);
                    String[] tokens = stringDate.split(delims);
                    projectObject.put("dateCreated", tokens[1] + " " + tokens[2] + " " + tokens[5]);
                }
                if (proj.getMembers() != null) {
                    // just return the size of the array
                    int membersSize = proj.getMembers().size();
                    List<String> members = proj.getMembers();
                    List<String> membersNames = new ArrayList<String>();
                    // get members' names
                    for (int j = 0; j < members.size(); j++) {
                        Person member = ClothoAdapter.getPerson(members.get(j), clothoObject);
                        membersNames.add(member.getFirstName() + " " + member.getLastName());
                    }
                    projectObject.put("membersNames", membersNames);
                    projectObject.put("membersNum", membersSize);
                    projectObject.put("membersIds", members);
                    System.out.println("\n");
                    System.out.println("membersNum is " + membersSize);
                    System.out.println("membersNames is " + members);
                    System.out.println("members are: " + members);

                }
                if (!(proj.getCreatorId().equals(""))) {
                    System.out.println("CREATOR ID ISN'T NULL!");
                    Person creator = ClothoAdapter.getPerson(proj.getCreatorId(), clothoObject);
                    System.out.println(creator.getFirstName() + " " + creator.getLastName());
                    projectObject.put("creatorId", proj.getCreatorId());
                    projectObject.put("creator", creator.getFirstName() + " " + creator.getLastName());
                }
                //projectObject.put("members", proj.getMembers());

                if (!(proj.getLeadId().equals(""))) {
                    System.out.println("LEAD ID ISN'T NULL!");
                    System.out.println("getting lead");
                    String leadtId = proj.getLeadId();
                    System.out.println(leadtId);
                    Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);

                    if (lead.getFirstName() != null && lead.getLastName() != null) {
                        System.out.println("LEAD'S NAME IS:");
                        System.out.println(lead.getFirstName() + " " + lead.getLastName());
                        projectObject.put("leadId", proj.getLeadId());
                        projectObject.put("lead", lead.getFirstName() + " " + lead.getLastName());
                    }
                    // add the project to the json object of all projects
                    listOfProjects.put(projectObject);
                }
                System.out.println("***");
                System.out.println("RESULT IS:");
                System.out.println(listOfProjects);
                System.out.println("***");
                //projectMap = (Map) ClothoAdapter.getProject(id, clothoObject);
                System.out.println("after getProject");
                System.out.println(projectObject);
            }
        }
        conn.closeConnection();

        writer.println(listOfProjects); //Send back stringified JSON object
        writer.flush();
        writer.close();

    }

    @RequestMapping(value = "getProject", method = RequestMethod.POST)
    public void getProject(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();

        Object projectIdObj = params.get("projectID");
        String projectId = projectIdObj != null ? (String) projectIdObj : "";
        System.out.println("projectId is:");
        System.out.println(projectId);
        System.out.println("inside Get Project");

        // possibly do not need this here
        // *****
        Map createUserMap = new HashMap();
        String username = "username";
        String password = "password";

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        createUserMap.put("username", username);
        createUserMap.put("password", password);

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);
        // *****
        // possibly take the code above out

        JSONObject projectObject = new JSONObject();
        System.out.println("before getProject");
        Project proj = ClothoAdapter.getProject(projectId, clothoObject);
        System.out.println("After Clotho Adapter get project");

        String desc = proj.getDescription();
        System.out.println("Description is:");
        System.out.println(desc);

        Grant grant = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);

        if (proj.getDateCreated() != null) {
            String delims = "[ ]+";
            String stringDate = proj.getDateCreated().toString();
            System.out.println(stringDate);
            String[] tokens = stringDate.split(delims);
            projectObject.put("dateCreated", tokens[1] + " " + tokens[2] + " " + tokens[5]);
        }
        if (proj.getMembers() != null) {

        }
        if (!(proj.getCreatorId().equals(""))) {
            Person creator = ClothoAdapter.getPerson(proj.getCreatorId(), clothoObject);
            System.out.println(creator.getFirstName() + " " + creator.getLastName());
            projectObject.put("creator", creator.getFirstName() + " " + creator.getLastName());
            projectObject.put("creatorId", proj.getCreatorId());
            if (creator.getLabs().size() > 0) {
                // if a person has labs - get 'em!
                System.out.println("creator has more than 0 labs");
                projectObject.put("creatorLabs", creator.getLabs());

            }
        }

        if (!(proj.getLeadId().equals(""))) {
            System.out.println("Getting Lead person");
            System.out.println("Lead Id is");
            String leadId = proj.getLeadId();
            projectObject.put("leadId", leadId);
            System.out.println(leadId);
            Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);
            if (lead.getFirstName() != null && lead.getLastName() != null) {
                System.out.println("Getting Lead name, it is:");
                System.out.println(lead.getFirstName() + " " + lead.getLastName());
                projectObject.put("lead", lead.getFirstName() + " " + lead.getLastName());
            }
            if (lead.getLabs().size() > 0) {
                System.out.println("lead has more than 0 labs");
                // if a person has labs - get 'em!
                projectObject.put("leadLabs", lead.getLabs());

            }
        }

        projectObject.put("description", desc);
        projectObject.put("budget", proj.getBudget());
        projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
        projectObject.put("projectName", proj.getName());
        //projectObject.put("notebooks", proj.getNotebooks());
        projectObject.put("updates", proj.getUpdates());
        projectObject.put("grantName", grant.getName());
        projectObject.put("grant", grant.getId());
        //projectMap = (Map) ClothoAdapter.getProject(id, clothoObject);

        System.out.println("after getProject");
        System.out.println(projectObject);
        String project = projectObject.toString();
        System.out.println("stringified :: " + project);
        conn.closeConnection();
        writer.println(project); //Send back stringified JSON object
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "processProject", method = RequestMethod.POST)
    protected void processProject(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map createUserMap = new HashMap();
        String username = "username";
        String password = "password";

        createUserMap.put("username", username);
        createUserMap.put("password", password);

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        JSONObject result = new JSONObject();

        Object prName = params.get("name");
        String projectName = prName != null ? (String) prName : "";
        System.out.println("New Project name is:");
        System.out.println(projectName);

        // create project first to modify at the end according to 
        // the other objects' ids
        Project project = new Project();
        project.setName(projectName);
        String projectID = ClothoAdapter.createProject(project, clothoObject);
        clothoObject.logout();

        Object membersObj = params.get("members");
        String membersString = membersObj != null ? (String) membersObj : "";
        String[] membersNames = membersString.split(", ");

        System.out.println(membersString);

        // parse the string into an array of names
        ArrayList<String> membersIDs = new ArrayList<String>();

        for (int i = 0; i < membersNames.length; i++) {
            // get first and last names
            String fullName = membersNames[i];
            System.out.println(fullName);
            String[] splitted = fullName.split("\\s+");
            String memberFirstName = splitted[0];
            String memberLastName = splitted[1];
            System.out.println("Splitted and joined name is: " + memberFirstName
                    + " " + memberLastName);
//        // at some point may want to query for 
//        // the members
            Person member = new Person();
            member.setFirstName(memberFirstName);
            member.setLastName(memberLastName);
            // create a project list for every member and add our
            // project's id to that list
            List<String> projectList = new ArrayList<String>();
            projectList.add(projectID);
            member.setProjects(projectList);

            String memberID = ClothoAdapter.createPerson(member, clothoObject);
            System.out.println("New member has been created and his ID is:");
            System.out.println(memberID);
            // add member's own Id to its object and update
            member.setId(memberID);
            memberID = ClothoAdapter.setPerson(member, clothoObject);
            // now add the member's id to the array of member ids to be
            // later attached to the project
            membersIDs.add(memberID);

        }
        // check the contents of the member IDs list
        for (int i = 0; i < membersIDs.size(); i++) {
            System.out.println("The ID " + i + " in membersIDs is: " + membersIDs.get(i));
        }
        System.out.println("Now getting lead!");
        Object leadIdObj = params.get("leadID");
        String leadId = leadIdObj != null ? (String) leadIdObj : "";
        System.out.println("Lead ID is: ");
        System.out.println(leadId);

        if (!leadId.equals("0")) {
            System.out.println("Lead exists in the database!");
            // we know that this person exists in the database
            Person lead = ClothoAdapter.getPerson(leadId, clothoObject);
            System.out.println(lead.getFirstName() + " " + lead.getLastName());
            String leadFullName = params.get("leadName");
            System.out.println(leadFullName);
            System.out.println(lead.getFirstName() + " " + lead.getLastName());

            List<String> leadProjects = lead.getProjects();
            leadProjects.add(projectID);
            lead.setProjects(leadProjects);

            String leadId2 = ClothoAdapter.setPerson(lead, clothoObject);
            project.setLeadId(leadId2);
        } else {
            // we want to use lead's name passed in from the form!
            Object leadFName = params.get("leadFirstName");
            String leadStringFirstName = leadFName != null ? (String) leadFName : "";
            Object leadLName = params.get("leadLastName");
            String leadStringLastName = leadLName != null ? (String) leadLName : "";
            // gets the lead's name
            System.out.println("lead's FirstName is:");
            System.out.println(leadStringFirstName);
            System.out.println("lead's LastName is:");
            System.out.println(leadStringLastName);
            // now create a new person lead and attach the project to him/her
            // and vice versa
            Person lead = new Person();
            lead.setFirstName(leadStringFirstName);
            lead.setLastName(leadStringLastName);
//          clothoObject.logout();
            String leadPersonID = ClothoAdapter.createPerson(lead, clothoObject);
            lead.setId(leadPersonID);
            List<String> projectList = new ArrayList<String>();
            projectList.add(projectID);
            lead.setProjects(projectList);
            leadPersonID = ClothoAdapter.setPerson(lead, clothoObject);
            project.setLeadId(leadPersonID);
        }

        Object labNameObj = params.get("lab");
        String labName = labNameObj != null ? (String) labNameObj : "";

        Object labIdObj = params.get("labID");
        String labId = labIdObj != null ? (String) labIdObj : "";

        System.out.println("Lab is:");
        System.out.println(labName);
        System.out.println("Lab Id is:");
        System.out.println(labId);

        String projectBudgetVal = params.get("projectBudget");
        System.out.println((String) projectBudgetVal);
        double projectBudget = 0.0;
        if (!projectBudgetVal.equals("") && projectBudgetVal != null) {
            result.put("budget", "1");
            projectBudget = Double.parseDouble((String) projectBudgetVal);
            if (projectBudget < 0) {
                result.put("budget", "0");
                projectBudget = 0;
            }
        }
        System.out.println("Budget is:");
        System.out.println(projectBudget);

        Object grantName = params.get("grant");
        String grant = grantName != null ? (String) grantName : "";
        System.out.println("Grant is");
        System.out.println(grant);

        Object descriptionObj = params.get("description");
        String description = descriptionObj != null ? (String) descriptionObj : "";
        System.out.println("Description is");
        System.out.println(description);

//      Object leadEmailAdr = params.get("leadEmailId");
//      String leadEmailId  = leadEmailAdr != null ? (String) leadEmailAdr : "" ;
//        System.out.println("leadEmailId is"); 
//        System.out.println(leadEmailId);
        Date date1 = new java.util.Date();
        String date = new Timestamp(date1.getTime()).toString();

        Person creator;
        String creatorID;

        if (params.get("emailID") != null) {
            System.out.println("creator exists");
            creatorID = params.get("emailID");
            System.out.println("Creator's Id is: " + creatorID);
            creator = ClothoAdapter.getPerson(creatorID, clothoObject);
        } else {
            // should never reach this, but here for debugging purposes
            System.out.println("(180) Creator does not exist, so creating a new one");
            creator = new Person();
            creator.setFirstName("Anna");
            creator.setLastName("Goncharova");
            creator.setEmailId("anna@gmail.com");
            creator.setPassword("1234567890");
            creator.setActivated(true);
            creatorID = ClothoAdapter.createPerson(creator, clothoObject);
            creator = ClothoAdapter.getPerson(creatorID, clothoObject);

        }
        clothoObject.login(loginMap);

        // create the Grant object 
        // TODO: check whether grant exists.
        Grant grantObject = new Grant();
        grantObject.setName(grant);
        String grantID = ClothoAdapter.createGrant(grantObject, clothoObject);
        System.out.println("Grant ID is: " + grantID);
        grantObject.setId(grantID);
        grantID = ClothoAdapter.setGrant(grantObject, clothoObject);

        // need to add support for creating and adding a number of labs/organizations
        // format of passed in lab? 
        // ** Send the input data as an array.
        // for now assume there is only one organization/lab
        // TODO: add options for a bunch of labs
        List<String> labsList = new ArrayList<String>();
        if (labId != "") {
            System.out.println("Found a lab!");
            Organization lab = ClothoAdapter.getLab(labId, clothoObject);
            System.out.println("\n");
            System.out.println(lab.getName());
            System.out.println("\n");

            labsList.add(lab.getName());
            System.out.println(labsList);
            System.out.println("\n");
        }

        System.out.println("About to create a new project.");
        // create and set the fields for a new project
        project.setBudget(projectBudget);
        project.setCreatorId(creatorID);
        project.setGrantId(grantID);
        project.setAffiliatedLabs(labsList);
        project.setDescription(description);
        project.setMembers(membersIDs);

        projectID = ClothoAdapter.setProject(project, clothoObject);

        clothoObject.logout();

        List<String> creatorProjects = creator.getProjects();

        if (!creatorID.equals(leadId)) {
            // this is to make sure that the creator and the lead aren't
            // the same people since the project ID has already been 
            // added to the lead's list of projects
            creatorProjects.add(projectID);
        }

        // for debugging; display all projects
        for (int i = 0; i < creatorProjects.size(); i++) {
            System.out.println(creatorProjects.get(i));
        }

        creatorID = ClothoAdapter.setPerson(creator, clothoObject);

        System.out.println("New Project ID is " + projectID);
        System.out.println(creator.getProjects());

        clothoObject.login(loginMap);

        // CODE BELOW IS NOT ESSENTIAL
        // now print all of the things in the project
        Project test = ClothoAdapter.getProject(projectID, clothoObject);

        System.out.println("Name is " + test.getName());
        System.out.println("Budget is " + test.getBudget());
        System.out.println("Description is " + test.getDescription());
        System.out.println("Lead ID is " + test.getLeadId());
        System.out.println("Creator ID is " + test.getCreatorId());
        System.out.println("Grant ID is " + test.getGrantId());

        Project checkProject = ClothoAdapter.getProject(projectID, clothoObject);

        String checkProjectCreatorId = checkProject.getCreatorId();
        String checkProjectLeadId = checkProject.getLeadId();
        List<String> checkProjectMemberIds = checkProject.getMembers();
        System.out.println("\n");
        System.out.println("checkProjectMemberIds");
        System.out.println(checkProjectMemberIds);
        System.out.println("checkProjectLeadId");
        System.out.println(checkProjectLeadId);
        System.out.println("checkProjectCreatorId");
        System.out.println(checkProjectCreatorId);
        System.out.println("\n");
        Person checkProjectPerson = ClothoAdapter.getPerson(checkProjectCreatorId, clothoObject);
        System.out.println(checkProjectPerson.getFirstName() + " " + checkProjectPerson.getLastName());
        System.out.println("\n");
        List<String> checkProjectPersonProjects = checkProjectPerson.getProjects();
        System.out.println("checkProjectPersonProjects");
        System.out.println(checkProjectPersonProjects);
        // CODE ABOVE IS NOT ESSENTIAL

        conn.closeConnection();

        if (projectID != null) {
            result.put("success", 1);
            result.put("projectId", projectID);
            System.out.println("successful");
        } else {
            result.put("error", 1);
            System.out.println("not successful!");
        }

        PrintWriter writer = response.getWriter();
        writer.println(result);
        writer.flush();
        writer.close();
        conn.closeConnection();
    }

    @RequestMapping(value = "findMembersForNewProject", method = RequestMethod.GET)
    protected void findMembersForNewProject(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("in doGet of findMemberForNewProject");
        Object pFirstName = params.get("firstName");
        String firstName = pFirstName != null ? (String) pFirstName : "";

        Object pLastName = params.get("lastName");
        String lastName = pLastName != null ? (String) pLastName : "";

        boolean isValid = false;
        if (!firstName.equals("") || !lastName.equals("")) {
            isValid = true;
        }

        if (isValid) {
            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            String username = "phagebook";
            String password = "backend";
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);
            clothoObject.login(loginMap);
            // able to query now. 
            Map query = new HashMap();
            if (!firstName.equals("")) {
                System.out.println("in query first name is:");
                System.out.println(firstName);
                query.put("firstName", firstName); // the value for which we are querying.
            }
            if (!lastName.equals("")) {
                System.out.println("in query last name is:");
                System.out.println(lastName);
                query.put("lastName", lastName); // the key of the object we are querying
            }

            List<Person> people = ClothoAdapter.queryPerson(query, clothoObject, ClothoAdapter.QueryMode.EXACT);
            JSONArray peopleJSONArray = new JSONArray();

            for (Person retrieve : people) {
                JSONObject retrievedAsJSON = new JSONObject();
                retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
                //get position? role?? we will look into this
                retrievedAsJSON.put("firstName", retrieve.getFirstName());
                retrievedAsJSON.put("lastName", retrieve.getLastName());
                retrievedAsJSON.put("email", retrieve.getEmailId());
                retrievedAsJSON.put("clothoId", retrieve.getId());
                System.out.println(retrieve.getFirstName());
                System.out.println(retrieve.getLastName());
//                JSONObject statusList = new JSONObject();
//                if (retrieve.getStatuses() != null){
//                    for (String status:retrieve.getStatuses()){
//                        Status stat = ClothoAdapter.getStatus(status, clothoObject);
//
//                        statusList.put("text", stat.getText());
//                        statusList.put("date", stat.getCreated().toString());
//                    }
//                }

//                JSONObject publicationList = new JSONObject();
//                if (retrieve.getPublications() != null){
//
//                    for (String publication:retrieve.getPublications()){
//                        Publication pub = ClothoAdapter.getPublication(publication, clothoObject);
//                        publicationList.put("id", pub.getId());
//                    }
//                }
                /*
                JSONObject labList = new JSONObject();
                if (retrieve.getLabs() != null){
                    for (String lab:retrieve.getLabs()){
                        Institution inst = ClothoAdapter.getInstitution(lab, clothoObject);
                        labList.put("name", inst.getName());
                        Set<Person.PersonRole> rolesAtInstitution = retrieve.getRole(lab);
                        JSONObject positions = new JSONObject();
                        Iterator <Person.PersonRole> it = rolesAtInstitution.iterator();
                        while(it.hasNext()){
                            positions.put(inst.getName(), it.next());
                        }
                        labList.put("roles", positions);
                    }
                }
                 */
//                retrievedAsJSON.put("statusList", statusList);
//                retrievedAsJSON.put("publicationList", publicationList);
                //retrievedAsJSON.put("labList", labList);
                peopleJSONArray.put(retrievedAsJSON);
            }

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = response.getWriter();
            out.print(peopleJSONArray);
            out.flush();

            conn.closeConnection();

        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("message", "need parameters to query with");
            PrintWriter out = response.getWriter();
            out.print(responseJSON);
            out.flush();
        }

    }

    @RequestMapping(value = "editProject", method = RequestMethod.POST)
    protected void editProject(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "username";
            String password = "password";

            createUserMap.put("username", username);
            createUserMap.put("password", password);

            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);

            clothoObject.login(loginMap);
            System.out.println("got request in EditProject");

            System.out.println(params);

            // these will be in a cookie?
            String userID = params.get("userID");
            String projectID = params.get("projectID");

            Person editor = ClothoAdapter.getPerson(userID, clothoObject);
            Project project = ClothoAdapter.getProject(projectID, clothoObject);

            System.out.println(editor.getEmailId());
            System.out.println(project.getName());

            // get all of the parameters in the request
            List<String> l = new ArrayList<String>(params.keySet());
            Enumeration e = Collections.enumeration(l);

            // loop through the enumeration to get values from the request and add to
            // the hashmap
            HashMap reqHashMap = new HashMap();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = params.get(key);
                reqHashMap.put(key, value);
            }
            Map result = editProjectHelper(project, reqHashMap, clothoObject);
            System.out.println("resulting map is: " + result);
            System.out.println(result);
            // create a result object and send it to the frontend
            JSONObject json = new JSONObject(result);
//      System.out.printf( "JSON: %s", json.toString(2) );

            if ((int) result.get("success") == 1) {
                json.put("success", 1);
            } else if ((int) result.get("success") == 0) {
                json.put("success", 0);
            }

            PrintWriter writer = response.getWriter();
            writer.println(json);
            writer.flush();
            writer.close();
            conn.closeConnection();
        }

    }

    static Map<String, Object> editProjectHelper(Project project, HashMap params, Clotho clothoObject) {

        // params is the hashmap of new values
        System.out.println("In Edit Project function");

        Iterator entries = params.entrySet().iterator();
        // result hashmap would let the user know whether there have
        // been unedited values.
        Map result = new HashMap();

        while (entries.hasNext()) {
            // reset the value if it is diff from the one in the project object
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value);

            String[] keyValue = new String[4];
            keyValue[0] = key; // type of new value (like "description")
            keyValue[1] = value; // the actual new value (like "This is a new desciption")

            if (key.equals("editorId")) {
                Person editor = ClothoAdapter.getPerson(value, clothoObject);
                System.out.println();
            }
            if (key.equals("description")) {
                keyValue[2] = "description";
                keyValue[3] = project.getDescription();
                //helperMsg(project.getDescription(), value);
                if (value != "") {
                    project.setDescription(value);
                    result.put("desc", 1);
                } else {
                    result.put("desc", 0);
                }

            }
            if (key.equals("name")) {
                keyValue[2] = "name";
                keyValue[3] = project.getName();
                //helperMsg(project.getName(), value);
                if (value != "") {
                    project.setName(value);
                    result.put("name", 1);
                } else {
                    result.put("name", 0);
                }

            }
            if (key.equals("leadId")) {
                //helperMsg(project.getLeadId(), value);
                System.out.println("Can't edit lead yet, sorry!");
                // TODO: add lead editing capabilities
            }
            if (key.equals("budget")) {
                keyValue[2] = "budget";
                keyValue[3] = project.getBudget().toString();
                //helperMsg(Double.toString(project.getBudget()), value);
                if (value != "") {
                    project.setBudget(Double.parseDouble(value));
                    result.put("budget", 1);
                } else {
                    result.put("budget", 0);
                }
            }
            if (key.equals("projectGrant")) {
                if (value != "") {
                    String oldGrantId = project.getGrantId();
                    Grant newGrant = new Grant(value);
                    String newGrantId = ClothoAdapter.createGrant(newGrant, clothoObject);
                    keyValue[2] = "projectGrant";
                    keyValue[3] = oldGrantId;

                    project.setGrantId(newGrantId);
                    result.put("grant", 1);
                } else {
                    result.put("grant", 0);
                }
            }
        }
        String projectID = project.getId();
        System.out.println("in Edit Project Function Project ID is");
        System.out.println(projectID);
        System.out.println(clothoObject);
        String foo = ClothoAdapter.setProject(project, clothoObject);
        System.out.println(foo);

        // FOR TESTING -- prints the result hashmap    
//      Iterator iterator = result.keySet().iterator();
//      while (iterator.hasNext()) {
//         String key = iterator.next().toString();
//         Integer value = (Integer)result.get(key);
//
//         System.out.println(key + " " + value);
//      }
        //
        System.out.println("got here!");
        if (projectID.length() > 0) {
            result.put("success", 1);
            return result;
        } else {
            result.put("success", 0);
            return result;
        }
        //sendEmails(request);
    }

    @RequestMapping(value = "getAllProjectUpdates", method = RequestMethod.GET)
    protected void getAllProjectUpdates(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("in the doGet of getAllProjectUpdates!!");
        Object projectIdObj = params.get("projectId");
        String projectId = projectIdObj != null ? (String) projectIdObj : "";
        System.out.println(projectId);

        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        Map createUserMap = new HashMap();
        String username = "username";
        String password = "password";

        createUserMap.put("username", username);
        createUserMap.put("password", password);

        clothoObject.createUser(createUserMap);
        Map loginMap = new HashMap();
        loginMap.put("username", username);
        loginMap.put("credentials", password);

        clothoObject.login(loginMap);

        // get this project
        Project pr = ClothoAdapter.getProject(projectId, clothoObject);
        List<String> allUpdates = pr.getUpdates();
        List<Map<String, String>> listOfUpdates = new ArrayList<Map<String, String>>();
        JSONObject result = new JSONObject();

        for (String s : allUpdates) {
//            clothoObject.logout();
            System.out.println("in the loop!");
            System.out.println(s);
            Status update = ClothoAdapter.getStatus(s, clothoObject);
            System.out.println(update.getText());
            Map u = new HashMap();
            u.put("date", update.getCreated());
            u.put("userId", update.getUserId());
            // get a person's first and last name
            clothoObject.logout();
            Person p = ClothoAdapter.getPerson(update.getUserId(), clothoObject);
            u.put("userName", p.getFirstName() + " " + p.getLastName());
            u.put("text", update.getText());
            listOfUpdates.add(u);
        }
        System.out.println("");

        result.put("success", 1);
        result.put("updates", listOfUpdates);
        System.out.println(result);

        PrintWriter writer = response.getWriter();
        writer.println(result);
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "addUpdateToProject", method = RequestMethod.POST)
    protected void addUpdateToProject(@RequestParam Map<String, String> params, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            JSONObject result = new JSONObject();

            ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
            Clotho clothoObject = new Clotho(conn);
            Map createUserMap = new HashMap();
            String username = "username";
            String password = "password";

            createUserMap.put("username", username);
            createUserMap.put("password", password);

            clothoObject.createUser(createUserMap);
            Map loginMap = new HashMap();
            loginMap.put("username", username);
            loginMap.put("credentials", password);

            clothoObject.login(loginMap);
            System.err.println("Got a new Update request in addUpdateToProject ");
            // New Update will be a string.
            // declare these here 
            String userID = "";
            String projectID = "";
            String newStatus = "";
            boolean emailPeople = false;
            if (params.get("userID") != null) {
                userID = params.get("userID");
            }
            if (params.get("projectID") != null) {
                projectID = params.get("projectID");
            }
            if (params.get("newStatus") != null) {
                newStatus = params.get("newStatus");
            }
            if (params.get("emailPeople") != null) {
                // what if it is not a boolean?
                emailPeople = Boolean.parseBoolean(params.get("emailPeople"));
            }

            // if there is a status
            if (newStatus.length() != 0) {
                List<String> allUpdates = addUpdateToProjectHelper(userID, projectID, newStatus, emailPeople, clothoObject);
                List<Map<String, String>> listOfUpdates = new ArrayList<Map<String, String>>();
                Person per = ClothoAdapter.getPerson(userID, clothoObject);
                System.out.println(per.getEmailId());
                System.out.println(per.getProjects());

                for (String s : allUpdates) {
                    Status update = ClothoAdapter.getStatus(s, clothoObject);
                    Map u = new HashMap();
                    u.put("date", update.getCreated());
                    u.put("userId", update.getUserId());
                    // get a person's first and last name
                    Person p = ClothoAdapter.getPerson(update.getUserId(), clothoObject);
                    u.put("userName", p.getFirstName() + " " + p.getLastName());
                    u.put("text", update.getText());
                    listOfUpdates.add(u);
                }
                result.put("success", 1);
                result.put("updates", listOfUpdates);
            } else {
                System.out.println("Update was too short -- letting the user know!");

                result.put("short", 1);
            }
            conn.closeConnection();
            PrintWriter writer = response.getWriter();
            writer.println(result);
            writer.flush();
            writer.close();

        }
    }

    protected static List<String> addUpdateToProjectHelper(String userID, String projectID, String newStatus, boolean emailPeople, Clotho clothoObject) {

        // create a new status object
        Status newUpdate = new Status();
        newUpdate.setText(newStatus);
        newUpdate.setUserId(userID);
        System.out.println(newUpdate);
        System.out.println("About to create a Status in Clotho");
        String statusID = ClothoAdapter.createStatus(newUpdate, clothoObject);

        System.out.println("Status has been created in Clotho and ID is: " + statusID);

        // get the objects associated with the passed in IDS from clotho
        Person editor = ClothoAdapter.getPerson(userID, clothoObject);
        System.out.println("User Id is: ");
        System.out.println(userID);

        Project project = ClothoAdapter.getProject(projectID, clothoObject);
        System.out.println("Project Id is: ");
        System.out.println(projectID);

        String editorName = editor.getFirstName() + " " + editor.getLastName();
        System.out.println(editorName);
        System.out.println(project.getName());

        // get the existing list of project updates and add the id of the  new update
        List<String> projectUpdates = project.getUpdates();
        projectUpdates.add(statusID);

        // update the update lists in the project object
        project.setUpdates(projectUpdates);

        List<String> allUpdates = project.getUpdates();
        // change the project in clotho
        String foo = ClothoAdapter.setProject(project, clothoObject);
        System.out.println("In addProjectUpdate function projectID is:");
        System.out.println(foo);
        // TODO: email the peeps associate with the project what update was added
        if (emailPeople) {
            System.out.println();
            System.out.println("I will email the people now");
            System.out.println();
            sendEmails.sendEmails(foo, editorName, clothoObject);
        }

        return allUpdates;
    }
}
