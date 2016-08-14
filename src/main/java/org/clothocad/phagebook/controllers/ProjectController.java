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
import org.clothocad.phagebook.dom.Institution;
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

  @RequestMapping(value = "/getAllProjects", method = {RequestMethod.GET, RequestMethod.POST})
  protected void getAllProjects(@RequestParam Map<String, String> params, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter writer = response.getWriter();

    /*
     ** This gets called by myProjects.html to gather all projects related 
     ** to the logged in user.
     ** 
     */
    System.out.println("Get all projects servlet!");
    System.out.println("in getAllProjects!");

    /* Phagebook credentials */
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
    /* Phagebook credentials */

    /* Get user ID from the cookie. */
    Object userIDObj = params.get("userID");
    String userID = userIDObj != null ? (String) userIDObj : "";
    System.out.println("User Id is: " + userID);

    /* Get the person object from Clotho */
    Person user = ClothoAdapter.getPerson(userID, clothoObject);
    System.out.println("Logged-in user is " + user.getFirstName() + " " + user.getLastName());

    /* Get user ID from the cookie. */
    List<String> projectsList = user.getProjects();
    System.out.println(projectsList);

    /* Create a JSON array object */
    JSONArray listOfProjects = new JSONArray();

    /* 
     Loop through the list of the projects to create a 
     JSON Object for all the projects found in the 
     list and add them to listOfProjects array.
     */
    if (projectsList.size() > 0) {
      System.out.println("The user has a positive number of projects.");
      for (int i = 0; i < projectsList.size(); i++) {
        JSONObject projectObject = new JSONObject();
        Grant grant;
        String projectId = projectsList.get(i);
        System.out.println("In the loop with i="+i);
        System.out.println("Project Id is "+projectId);
        Project proj = ClothoAdapter.getProject(projectId, clothoObject);
        projectObject.put("projectId", projectId);
        projectObject.put("projectName", proj.getName());
        projectObject.put("description", proj.getDescription());
        projectObject.put("budget", proj.getBudget());
        projectObject.put("affiliatedLabs", proj.getAffiliatedLabs());
        projectObject.put("updates", proj.getUpdates());

        System.out.println("Project ID is: " + proj.getDescription());
        System.out.println("Description is: " + proj.getDescription());
        System.out.println("Budget is: " + proj.getBudget());
        System.out.println("Project Name is " + proj.getName());
        System.out.println("Updates are " + proj.getUpdates());

        /* Loop and display information about labs */
        List<String> afflLabs = proj.getAffiliatedLabs();
        for (int k = 0; k < proj.getAffiliatedLabs().size(); k++) {
          System.out.println(afflLabs.get(k));
        }

        if (!proj.getGrantId().equals("")) {
          grant = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);
          projectObject.put("grant", grant.getId());
          System.out.println("Grant is " + grant.getId());
        } else {
          projectObject.put("grant", "");
        }

        /* We probably do not need this */
        if (proj.getDateCreated() != null) {
          String delims = "[ ]+";
          String stringDate = proj.getDateCreated().toString();
          String[] tokens = stringDate.split(delims);
          projectObject.put("dateCreated", tokens[1] + " " + tokens[2] + " " + tokens[5]);
        }

        if (proj.getMembers() != null) {
          /* Get the number of people attached to the project */
          int membersSize = proj.getMembers().size();
          List<String> members = proj.getMembers();
          List<String> membersNames = new ArrayList<String>();
          /* Loop through the memebrs' names and attach them to the array */
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

        if (!(proj.getCreatorId().equals("Not Set"))) {
          System.out.println("Project has a creator");
          Person creator = ClothoAdapter.getPerson(proj.getCreatorId(), clothoObject);
          System.out.println(creator.getFirstName() + " " + creator.getLastName());
          projectObject.put("creatorId", proj.getCreatorId());
          projectObject.put("creator", creator.getFirstName() + " " + creator.getLastName());
        }
        //projectObject.put("members", proj.getMembers());

        if (!(proj.getLeadId().equals("Not Set")) || !(proj.getLeadId().equals(""))) {
          System.out.println("Project has a lead");
          String leadtId = proj.getLeadId();
          System.out.println("Lead's ID is " + leadtId);
          Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);

          if (lead.getFirstName() != null && lead.getLastName() != null) {
            System.out.println("Lead's name is: " + lead.getFirstName() + " " + lead.getLastName());
            projectObject.put("leadId", proj.getLeadId());
            projectObject.put("lead", lead.getFirstName() + " " + lead.getLastName());
          }
        }
        System.out.println("The final object is:");
        System.out.println(projectObject);

        // add the project to the json object of all projects
        listOfProjects.put(projectObject);

      }
    }
    System.out.println("\n");
    System.out.println("About to pass the listOfProjects back to the webpage: ");
    System.out.println(listOfProjects);
    System.out.println("\n");

    writer.println(listOfProjects); //Send back stringified JSON object
    writer.flush();
    writer.close();
    conn.closeConnection();

  }

  @RequestMapping(value = "/getProject", method = RequestMethod.POST)
  public void getProject(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException, ServletException {
    PrintWriter writer = response.getWriter();

    Object projectIdObj = params.get("projectID");
    String projectId = projectIdObj != null ? (String) projectIdObj : "";
    System.out.println("projectId is:");
    System.out.println(projectId);
    System.out.println("inside Get Project");

    //ESTABLISH CONNECTION
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

    System.out.println("getting project from Clotho");

    JSONObject projectObject = new JSONObject();
    Project proj = ClothoAdapter.getProject(projectId, clothoObject);

    System.out.println("Got project from Clotho");

    String desc = proj.getDescription();
    System.out.println("Description is: " + desc);

    Grant grant;
    if (!proj.getGrantId().equals("")) {
      System.out.println(proj.getGrantId());
      grant = ClothoAdapter.getGrant(proj.getGrantId(), clothoObject);
      projectObject.put("grantName", grant.getName());
      projectObject.put("grant", grant.getId());
    }

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
      String leadId = proj.getLeadId();
      System.out.println("Lead Id is " + leadId);
      projectObject.put("leadId", leadId);
      Person lead = ClothoAdapter.getPerson(proj.getLeadId(), clothoObject);
      if (lead.getFirstName() != null && lead.getLastName() != null) {
        System.out.println("Getting Lead name: " + lead.getFirstName() + " " + lead.getLastName());
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

    //projectMap = (Map) ClothoAdapter.getProject(id, clothoObject);
    String project = projectObject.toString();

    System.out.println("after getProject");
    System.out.println("stringified :: " + project);

    conn.closeConnection();
    writer.println(project); //Send back stringified JSON object
    writer.flush();
    writer.close();
  }

  @RequestMapping(value = "/createNewProject", method = RequestMethod.POST)
  protected void createNewProject(@RequestParam Map<String, String> params, HttpServletResponse response)
          throws ServletException, IOException {
    //ESTABLISH CONNECTION
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

      // create project first to modify as progress 
    // through the server
    Project project = new Project();
    String projectId = ClothoAdapter.createProject(project, clothoObject);
    clothoObject.logout();

    System.out.println("in processProject servlet");

    JSONObject result = new JSONObject();

    Object prName = params.get("name");
    String projectName = prName != null ? (String) prName : "";
    System.out.println("New Project name is:");
    System.out.println(projectName);

    project.setName(projectName);

    Object creatorId = params.get("emailID");
    String creatorIdStr = creatorId != null ? (String) creatorId : "";
    System.out.println("Creator's ID is:");
    System.out.println(creatorIdStr);

    Object prDescription = params.get("description");
    String prDescriptionStr = prDescription != null ? (String) prDescription : "";
    System.out.println("New Project description is:");
    System.out.println(prDescriptionStr);

    project.setDescription(prDescriptionStr);

    Object leadId = params.get("leadID");
    String leadIdStr = leadId != null ? (String) leadId : "";
    System.out.println("Lead's ID is:");
    System.out.println(leadIdStr);

    Person creator = ClothoAdapter.getPerson(creatorIdStr, clothoObject);
    System.out.println("Creator is " + creator.getFirstName() + " "
            + creator.getLastName());
    List<String> projectsCreator = creator.getProjects();
    projectsCreator.add(projectId);
    project.setCreatorId(creatorIdStr);
    ClothoAdapter.setPerson(creator, clothoObject);
    System.out.println("Successfully set creator.");

    if (leadIdStr.compareTo("0") != 0) {
      if (leadIdStr.compareTo(creatorIdStr) != 0) {
        System.out.println("Lead is not the same as creator.");
        Person lead = ClothoAdapter.getPerson(leadIdStr, clothoObject);
        System.out.println("Lead is " + lead.getFirstName() + " "
                + lead.getLastName());
        List<String> projectsLead = lead.getProjects();
        projectsLead.add(projectId);
        project.setLeadId(leadIdStr);
        ClothoAdapter.setPerson(lead, clothoObject);
        System.out.println("Successfully set lead.");
      }

    }

    Object members = params.get("members");
    String membersStr = members != null ? (String) members : "";
    if (membersStr.compareTo("") != 0) {
      String[] membersArr = membersStr.split(",");
      for (int i = 0; i < membersArr.length; i++) {

        System.out.println(membersArr[i]);
        if (membersArr[i].compareTo("0") != 0) {
          String memberId = membersArr[i];
          if (memberId.compareTo(leadIdStr) != 0 && memberId.compareTo(creatorIdStr) != 0) {
            System.out.println("Neither lead nor creator have been selected as members of"
                    + " the project.");
            Person member = ClothoAdapter.getPerson(memberId, clothoObject);
            List<String> projectsMember = member.getProjects();
            projectsMember.add(projectId);
            List<String> projectMembers = project.getMembers();
            projectMembers.add(memberId);
            ClothoAdapter.setPerson(member, clothoObject);
          }
        }
      }
    }

    Object projectBudget = params.get("projectBudget");
    String projectBudgetStr = projectBudget != null ? (String) projectBudget : "0";
    System.out.println("Project's budget is:");
    System.out.println(projectBudgetStr);
    project.setBudget(Double.parseDouble(projectBudgetStr));

    Object grantObj = params.get("grant");
    String grantStr = grantObj != null ? (String) grantObj : "";
    System.out.println("Grant is:");
    System.out.println(grantStr);

    // TODO: add option for searching grants
    if (!grantStr.equals("")) {
      Grant grant = new Grant();
      grant.setName(grantStr);
      String grantId = ClothoAdapter.createGrant(grant, clothoObject);
      project.setGrantId(grantId);
    }

    clothoObject.login(loginMap);
    ClothoAdapter.setProject(project, clothoObject);

    PrintWriter writer = response.getWriter();
    writer.println(result);
    writer.flush();
    writer.close();
    conn.closeConnection();
  }

  @RequestMapping(value = "/findMemberForNewProject", method = RequestMethod.GET)
  protected void findMemberForNewProject(@RequestParam Map<String, String> params, HttpServletResponse response)
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
        String institutionId = retrieve.getInstitution();
        Institution institution = ClothoAdapter.getInstitution(institutionId, clothoObject);
        
        JSONObject retrievedAsJSON = new JSONObject();
        retrievedAsJSON.put("fullname", retrieve.getFirstName() + " " + retrieve.getLastName());
        //get position? role?? we will look into this
        retrievedAsJSON.put("firstName", retrieve.getFirstName());
        retrievedAsJSON.put("lastName", retrieve.getLastName());
        retrievedAsJSON.put("email", retrieve.getEmailId());
        retrievedAsJSON.put("institution", institution.getName());
        retrievedAsJSON.put("clothoId", retrieve.getId());
        System.out.println(retrieve.getFirstName());
        System.out.println(retrieve.getLastName());
        peopleJSONArray.put(retrievedAsJSON);
      }

      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_OK);

      PrintWriter out = response.getWriter();
      out.print(peopleJSONArray);
      out.flush();
      clothoObject.logout();
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

  @RequestMapping(value = "/editProject", method = RequestMethod.POST)
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
      clothoObject.logout();
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
    if (projectID.length() > 0) {
      result.put("success", 1);
      return result;
    } else {
      result.put("success", 0);
      return result;
    }
    //sendEmails(request);
  }

  @RequestMapping(value = "/getAllProjectUpdates", method = RequestMethod.GET)
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
    conn.closeConnection();
    result.put("success", 1);
    result.put("updates", listOfUpdates);
    System.out.println(result);

    PrintWriter writer = response.getWriter();
    writer.println(result);
    writer.flush();
    writer.close();
  }

  @RequestMapping(value = "/addUpdateToProject", method = RequestMethod.POST)
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
      clothoObject.logout();
      conn.closeConnection();
      PrintWriter writer = response.getWriter();
      writer.println(result);
      writer.flush();
      writer.close();

    }
  }

  protected static List<String> addUpdateToProjectHelper(String userID, String projectID,
          String newStatus, boolean emailPeople, Clotho clothoObject) {

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

  @RequestMapping(value = "/getAllProjectMembers", method = RequestMethod.GET)
  protected void getAllProjectMembers(@RequestParam Map<String, String> params, HttpServletResponse response)
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
      System.out.println("in Process Request function");
      JSONObject result = new JSONObject();
      Object prId = params.get("projectId");
      String projectId = prId != null ? (String) prId : "";
      System.out.println("The name of the project is:");
      System.out.println(projectId);

      Project project = ClothoAdapter.getProject(projectId, clothoObject);
      List<String> members = project.getMembers();
      System.out.println(members);

      List res = new ArrayList();
      for (int i = 0; i < members.size(); i++) {

        String personId = members.get(i);
        // get each member and grab their name
        Person member = ClothoAdapter.getPerson(personId, clothoObject);
        String name = member.getFirstName() + " " + member.getLastName();
        System.out.println(name);
        HashMap personMap = new HashMap();
        personMap.put("personId", personId);
        personMap.put("personName", name);
        res.add(personMap);
      }
      clothoObject.logout();
      conn.closeConnection();
      System.out.println("About to leave add members to projects");
      System.out.println(res);
      result.put("result", res);
      out.print(result);
      out.flush();
    }
  }

  @RequestMapping(value = "/processProject", method = RequestMethod.POST)
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

    // create project first to modify as progress 
    // through the server
    Project project = new Project();
    String projectId = ClothoAdapter.createProject(project, clothoObject);
    clothoObject.logout();

    System.out.println("in processProject servlet");

    JSONObject result = new JSONObject();

    Object prName = params.get("name");
    String projectName = prName != null ? (String) prName : "";
    System.out.println("New Project name is:");
    System.out.println(projectName);

    project.setName(projectName);

    Object creatorId = params.get("emailID");
    String creatorIdStr = creatorId != null ? (String) creatorId : "";
    System.out.println("Creator's ID is:");
    System.out.println(creatorIdStr);

    Object prDescription = params.get("description");
    String prDescriptionStr = prDescription != null ? (String) prDescription : "";
    System.out.println("New Project description is:");
    System.out.println(prDescriptionStr);

    project.setDescription(prDescriptionStr);

    Object leadId = params.get("leadID");
    String leadIdStr = leadId != null ? (String) leadId : "";
    System.out.println("Lead's ID is:");
    System.out.println(leadIdStr);

    Person creator = ClothoAdapter.getPerson(creatorIdStr, clothoObject);
    System.out.println("Creator is " + creator.getFirstName() + " "
            + creator.getLastName());
    List<String> projectsCreator = creator.getProjects();
    if(!projectsCreator.contains(projectId)){
      projectsCreator.add(projectId);
      project.setCreatorId(creatorIdStr);
      ClothoAdapter.setPerson(creator, clothoObject);
      System.out.println("Successfully set creator.");
    }
    

    if (leadIdStr.compareTo("0") != 0) {
      Person lead = ClothoAdapter.getPerson(leadIdStr, clothoObject);
      System.out.println("Lead is " + lead.getFirstName() + " "
              + lead.getLastName());
      List<String> projectsLead = lead.getProjects();
      if(!projectsLead.contains(projectId)){
        projectsLead.add(projectId);
        project.setLeadId(leadIdStr);
        ClothoAdapter.setPerson(lead, clothoObject);
        System.out.println("Successfully set lead.");
      }
    }

    Object members = params.get("members");
    String membersStr = members != null ? (String) members : "";
    if (membersStr.compareTo("") != 0) {
      String[] membersArr = membersStr.split(",");
      for (int i = 0; i < membersArr.length; i++) {
        System.out.println(membersArr[i]);
        if (membersArr[i].compareTo("0") != 0) {
          Person member = ClothoAdapter.getPerson(membersArr[i], clothoObject);
          List<String> projectsMember = member.getProjects();
          if(!projectsMember.contains(projectId)){
            projectsMember.add(projectId);
            List<String> projectMembers = project.getMembers();
            projectMembers.add(membersArr[i]);
            ClothoAdapter.setPerson(member, clothoObject);
          }
        }
      }
    }

    Object projectBudget = params.get("projectBudget");
    String projectBudgetStr = projectBudget != null ? (String) projectBudget : "0";
    System.out.println("Project's budget is:");
    System.out.println(projectBudgetStr);
    project.setBudget(Double.parseDouble(projectBudgetStr));

    Object grantObj = params.get("grant");
    String grantStr = grantObj != null ? (String) grantObj : "";
    System.out.println("Grant is:");
    System.out.println(grantStr);

    // TODO: add option for searching grants
    if (!grantStr.equals("")) {
      Grant grant = new Grant();
      grant.setName(grantStr);
      String grantId = ClothoAdapter.createGrant(grant, clothoObject);
      project.setGrantId(grantId);
    }

    clothoObject.login(loginMap);
    ClothoAdapter.setProject(project, clothoObject);

    PrintWriter writer = response.getWriter();
    writer.println(result);
    writer.flush();
    writer.close();
    conn.closeConnection();
  }
}
