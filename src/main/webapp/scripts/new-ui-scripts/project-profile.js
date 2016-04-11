$(document).ready(function() {


    // after we get the queried project, we edit the html 
    // {"creator":"Anna Goncharova","dateCreated":"Apr 09 2016",
    // "description":"This is a project description for an awesome project named \"Project\"!",
    // "projectName":"Project","updates":[],"grant":"5709a9bad4c60ab7f5242f02",
    // "lead":"Anna Goncharova","budget":12345}
    var editHtml = function(data) {
        data = JSON.parse(data);
        console.log(data);

        // porting code here    
        $("#project-name").val(data["projectName"]);
        $("#project-description").text(data["description"]);
        $("#project-dateCreated").text(data["dateCreated"]);
        $("#project-affiliatedLabs").val(data["affiliatedLabs"]);
        $("#project-grant").val(data["grantName"]);
        $("#project-budget").val(data["budget"]);


        $("#creator-name ").text(data["creator"]);
        $("#project-people-person-creator-lab").text(data["creatorLabs"])
        $("#lead-name").text(data["lead"]);
        $("#project-people-person-lead-lab").text(data["leadLabs"])

        // $(".project-people-person-creator-lab").text(data["creatorLabs"])
        // $(".project-description-content").text(data["description"]);
    }

    var params = location.search;

    var qs = (function(a) {
        if (a == "") return {};
        var b = {};
        for (var i = 0; i < a.length; ++i) {
            var p = a[i].split('=', 2);
            if (p.length == 1)
                b[p[0]] = "";
            else
                b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
        }
        return b;
    })(window.location.search.substr(1).split('&'));

    var id = qs["id"];
    console.log(id);

    $.ajax({
        //do this for projects...
        url: "../getProject",
        type: "POST",
        async: false,
        data: {
            "projectID": id
        },
        success: function(response) {
            console.log("response!")
            console.log(response);
            return editHtml(response);
        },
        error: function(response) {
            console.log("project quering failed!");
        }
    });

    // following code is for all project updates
    dataUpd = {
        "projectId":id
    };

    $.ajax({
        url: "../getAllProjectUpdates",
        type: "GET",
        dataType: "json",
        data: dataUpd,
        success: function(response) {
            console.log(response);
            console.log("response!!!");
            // return checkAddUpdateResponse(response);
        },
        error: function(err) {
            console.log("ERROR!!");
            console.log(err);
        }
    });

    // EDIT PROJECT CODE

    $("#edit-project-btn").click(function() {
        console.log("click!");
        $(".edit").prop('readonly', false);
        $('#submit-project-btn').show();

    });

    // after user edits the areas, and clicks on the submit button, send the ajax 
    // call and refresh the page

    $("#submit-project-btn").click(function() {
        console.log($("#project-description").text());
        edit();
    });

    var edit = function() {
        console.log("in edit function!!");
        var newName = "";
        var newDesc = "";
        var newBudget = "";
        var userID = getCookie("clothoId");
        var projectID = getCookie("projectId");

        if ($("#project-name").val() != null) {
            newName = $("#project-name").val()
        };
        if ($("#project-description").val() != null) {
            newDesc = $("#project-description").val();
        };
        if ($("#project-budget").val() != null) {
            newBudget = $("#project-budget").val();
        };
        console.log("Project ID");
        console.log(id);
        console.log("User ID");
        console.log(userID);
        console.log(newDesc);
        console.log(newName);
        console.log(newBudget);

        var data = {
            "userID": userID,
            "projectID": id,
            "description": newDesc,
            "name": newName,
            "budget": newBudget
        }
        console.log(data);
        $.ajax({
            url: "/editProject",
            type: "POST",
            dataType: "json",
            data: data,
            success: function(response) {
                //console.log(dataSubmit);
                console.log(response);
                console.log("response!!!");
                // this reloads the window
                return location.reload();
            },
            error: function(err) {
                console.log("ERROR!!");
                console.log(err);
            }
        });
    }

    $("#create-status-btn").click(function() {
        console.log("clicked on addUpdate")

        var userID = getCookie("clothoId");
        var projectID = id;
        var newStatus = $("#status-update-textarea").val();
        console.log(newStatus);
        var emailPeople = document.getElementById("emailPeople").checked;
        console.log(emailPeople);
        if (projectID != null && userID != null && newStatus != null) {
            console.log("about to add update");
            addUpdate(projectID, userID, newStatus, emailPeople);
        } else {
            //alert("Something is missing!");
        }

    });

    var checkAddUpdateResponse = function(response) {
        // if the update was too short -- alerts?
        if (response["short"] == 1) {
            alert("The update was too short! Please input a longer string.");
        }
    }

    var addUpdate = function(projectID, userID, newStatus, emailPeople) {

        console.log("in add update function");
        var data = {
            "userID": userID,
            "projectID": projectID,
            "newStatus": newStatus,
            "emailPeople": emailPeople
        };
        console.log(data);
        $.ajax({
            url: "/addUpdateToProject",
            type: "POST",
            dataType: "json",
            data: data,
            success: function(response) {
                console.log(response);
                console.log("response!!!");
                return checkAddUpdateResponse(response);
            },
            error: function(xhr, ajaxOptions, thrownError){
                    alert(xhr.status)
                }
        });
    }

});