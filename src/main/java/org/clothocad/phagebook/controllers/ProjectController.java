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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Person;
import org.clothocad.phagebook.adaptors.ClothoAdapter;
import org.clothocad.phagebook.controller.Args;
import org.clothocad.phagebook.dom.Grant;
import org.clothocad.phagebook.dom.Organization;
import org.clothocad.phagebook.dom.Project;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProjectController {

    @RequestMapping(value = "getAllProjectsGet", method = RequestMethod.GET)
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

    @RequestMapping(value = "getAllProjectsPost", method = RequestMethod.POST)
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
}
