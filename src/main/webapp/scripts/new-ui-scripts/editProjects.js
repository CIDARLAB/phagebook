// make a dummy project here
var constructAString = function(project){
    //console.log("in Construct A string");
    //console.log(project);
    
    var projectStr = "Creator is: " + project.creator + 
    ". Date of Creation is:  " + project.dateCreated +
    ". Description is: " + project.description +
    ". The name of the Project is: " + project.projectName +
    ". Budget is: " + project.budget +
    ". Grant ID is: "  + project.grant +
    ". Lead is: " + project.lead +".";
    //console.log(projectStr);
    alert(projectStr);
    // this fills in the form with values
    $('#name').val(project.projectName);
    $('#desc').val(project.description);
    $('#budget').val(project.budget);
}   

var appendRow = function() {
    var userID = getCookie("clothoID");
    var projectID = getCookie("projectId");
    var project;
    //console.log(userID);
    //console.log(projectID);
    
    var data = {
        "projectID": projectID
    }
    //console.log(data);
    // an ajax request to get the project that has project id
    $.ajax({
        url: "../getProject",
        type: "POST",
        dataType: "json",
        data: data,
        success: function(response) {
            ////console.log(dataSubmit);
            //console.log(response);
            project = response;
            //console.log("response!!!");
            // use the following function to alert with data from the servler
            return constructAString(project);
        },
        error: function(err) {
            //console.log("ERROR!!");
            //console.log(err);
        }
    });
}


$(document).ready(function() {



    $("#loadProject").click(function() {
        appendRow();

    });

    $("#editProject").click(function() {
        edit();
    });

    


    var editProject = function() {
        //console.log("in editProject");
        // var name = "Phagebook";
        // var leadFirstName = "Awesome";
        // var leadLastName = "Ugrads"
        // var leadEmailId = "agonchar@bu.edu"
        // var labs = "CIDAR";
        // var projectBudget = 100;
        // var grant = "Grant"
        // var desc = "This is Phagebook.";
        // var date = new Date("October 13, 2014 11:13:00");


        //console.log("in addStatus function!!");
        var newStatus = "";

        if ($("#status").val() != null) {
            newStatus = $("#status").val();
        };

        //console.log(newStatus);
        // how new projects should be passed in
        var data = {
            name: name,
            projectBudget: projectBudget,
            description: desc,
        };

        $.ajax({
            url: "../processProject",
            type: "POST",
            dataType: "json",
            data: data,
            async: true,
            success: function(response) {
                //console.log(response);
                projectID = response.projectID;
                userID = response.userID
                $('#projectID').html(projectID + " " + userID);
                return edit(projectID, userID);
            },
            error: function(err) {
                //console.log("ERROR!!");
                //console.log(err);
            }
        });
    }


});