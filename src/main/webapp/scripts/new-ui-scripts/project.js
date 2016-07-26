$(document).ready(function() {

    // after we get the queried project, we edit the html 
    // {"creator":"Anna Goncharova","dateCreated":"Apr 09 2016",
    // "description":"This is a project description for an awesome project named \"Project\"!",
    // "projectName":"Project","updates":[],"grant":"5709a9bad4c60ab7f5242f02",
    // "lead":"Anna Goncharova","budget":12345}
    $("#submit-member-btn").click(submitMemberButtonHandler);



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
    //console.log(id);

    var constructHTML = function(name, date, status) {
        var update = "<div class='update'> <div class='panel panel-default'> <div class='panel-heading'>" + "Name: " + name + " Date: " + date + "</div> " +
            "<div class='panel-body'>" + status + " </div> </div> </div>";
        return update;
    };


    var appendUpdate = function(updates) {
        //console.log("appendUpdate");
        for (var i = 0; i < updates.length; i++) {
            var u = constructHTML(updates.person, updates.date, updates.status);
            //console.log(u);
            $("#previous-posts-append").append(updates);
        }
    };
    var loadStatuses = function(){
        //console.log("button clicked")
        var ul = $("#project-status-list");
        ul.empty();
        $.ajax({
            type: 'GET',
            url: '../getAllProjectUpdates',
            data: {
                "projectId": id
            },
            success: function(response) {
                // var responseAsJSON = angular.fromJson(response);
                // //console.log(JSON.stringify(response));
                // //console.log(JSON.parse(response));
                //console.log("response is");
                //console.log(typeof(response));
                response = JSON.parse(response);
                //console.log(typeof(response));
                //console.log(response);
                updates = response.updates;
                if (updates.length == 0) {
                    alert("This project has no statuses!");
                }
                for (var i = 0; i < updates.length; i++) {
                    var tmpl = document.getElementById("status-template").content.cloneNode(true);
                    var now = new Date(updates[i].date);
                    tmpl.querySelector(".status-date").innerText = "Created On: " + updates[i].date;
                    tmpl.querySelector(".status-person").innerText = "Created By: " + updates[i].userName;
                    //$scope.statusDate = response[i].dateCreated;
                    ////console.log(response[i].dateCreated);
                    tmpl.querySelector(".status-text").innerText = updates[i].text;
                    ////console.log(response[i].statusText);
                    ul.append(tmpl);
                }
            },
            error: {
                ////console.log("inside GET error");
            }
        });

    };
    $("#load-project-statuses").click(function() {
        loadStatuses();
    });

    // updates = [{"person":"Allie", "date":"03/10","status":"ABC"}];
    // appendUpdate(updates);
    var editHtml = function(data) {
        data = JSON.parse(data);
        //console.log(data);

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

    $.ajax({
        //do this for projects...
        url: "../getProject",
        type: "POST",
        async: false,
        data: {
            "projectID": id
        },
        success: function(response) {
            //console.log("response!")
            //console.log(response);
            return editHtml(response);
        },
        error: function(response) {
            //console.log("project quering failed!");
        }
    });


    // EDIT PROJECT CODE

    $("#edit-project-btn").click(function() {
        //console.log("click!");
        $(".edit").prop('readonly', false);
        $('#submit-project-btn').show();

    });

    $("#search-colleagues-btn").click(searchBtnHandler);

    // after user edits the areas, and clicks on the submit button, send the ajax 
    // call and refresh the page

    $("#submit-project-btn").click(function() {
        //console.log($("#project-description").text());
        edit();
    });

    var edit = function() {
        //console.log("in edit function!!");
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
        //console.log("Project ID");
        //console.log(id);
        //console.log("User ID");
        //console.log(userID);
        //console.log(newDesc);
        //console.log(newName);
        //console.log(newBudget);

        var data = {
            "userID": userID,
            "projectID": id,
            "description": newDesc,
            "name": newName,
            "budget": newBudget
        }
        //console.log(data);
        $.ajax({
            url: "/editProject",
            type: "POST",
            dataType: "json",
            data: data,
            success: function(response) {
                ////console.log(dataSubmit);
                //console.log(response);
                //console.log("response!!!");
                // this reloads the window
                return location.reload();
            },
            error: function(err) {
                //console.log("ERROR!!");
                //console.log(err);
            }
        });
    }

    $("#create-status-btn").click(function() {
        //console.log("clicked on addUpdate");

        var userID = getCookie("clothoId");
        var projectID = id;
        var newStatus = $("#status-update-textarea").val();
        //console.log(newStatus);
        var emailPeople = document.getElementById("emailPeople").checked;
        //console.log(emailPeople);
        if (projectID != null && userID != null && newStatus != null) {
            //console.log("about to add update");
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
        loadStatuses();
    }

    var addUpdate = function(projectID, userID, newStatus, emailPeople) {

        //console.log("in add update function");
        var data = {
            "userID": userID,
            "projectID": projectID,
            "newStatus": newStatus,
            "emailPeople": emailPeople
        };
        //console.log(data);
        $.ajax({
            url: "/addUpdateToProject",
            type: "POST",
            dataType: "json",
            data: data,
            success: function(response) {
                //console.log(response);
                //console.log("response!!!");
                return checkAddUpdateResponse(response);
            },
            error: function(xhr, ajaxOptions, thrownError) {
                alert(xhr.status)
            }
        });
    }

    function searchBtnHandler() {
        var firstName = $("#search-first-name").val();
        var lastName = $("#search-last-name").val();


        $.ajax({
            url: '../queryFirstLastName',
            type: 'GET',
            async: false,
            data: {
                "firstName": firstName,
                "lastName": lastName
            },
            success: function(response) {


                var ul = $("#pi-add-list");

                ul.empty();


                for (var i = 0; i < response.length; i++) {
                    var tmpl = document.getElementById('person-results-template').content.cloneNode(true);


                    tmpl.querySelector('.member-name').innerText = response[i].fullname;
                    tmpl.querySelector('.member-lab-name').innerText = (response[i].labName == null) ? "" : response[i].labName;
                    tmpl.querySelector('.member-institution-name').innerText = response[i].institutionName;

                    tmpl.querySelector('.member-profile-link').href = "../html/colleague.html?user=" + response[i].clothoId;
                    tmpl.querySelector('.member-id').value = response[i].clothoId;


                    ul.append(tmpl);
                }
            },
            error: {}

        });
        return false;
    }

    var data = {
        "projectId": id
    }
    console.log(data);

    var ul = $("#member-link-list");
    console.log(ul);
    var getAllMembers = function() {
        console.log("in getAllMembers function");
        $.ajax({
            url: "../getAllProjectMembers",
            type: "GET",
            dataType: "json",
            data: data,
            success: function(response) {
                // response is the array of projects
                // response = JSON.parse(response);
                // console.log(typeof(response));
                console.log(response);

                for (var i = 0; i < response.result.length; i++) {
                    link = "./profile.html?id=" + response.result[i].personId;
                    var a = document.createElement('a');
                    $(a).attr('href', link).text(response.result[i].personName);
                    console.log(a);
                    var tmpl = document.getElementById("member-template").content.cloneNode(true);
                    tmpl.querySelector(".project-link").appendChild(a);
                    console.log(tmpl);
                    ul.append(tmpl);

                }

            },
            error: function(err) {
                console.log("ERROR!!");
                console.log(err);
            }
        });
    }



    function submitMemberButtonHandler() {
        console.log("YOOOOOOO");
        var container = document.getElementById("member-add-results");
        var peopleBoxes = container.getElementsByClassName("member-id");

        for (var i = 0; i < peopleBoxes.length; i++) {

            if (peopleBoxes[i].checked) {

                alert(peopleBoxes[i].value);
                $.ajax({
                    //do this for projects...
                    url: "../addMemberToProject",
                    type: "POST",
                    async: false,
                    data: {
                        "memberId": peopleBoxes[i].value,
                        "projectId": id
                    },
                    success: function(response) {
                        console.log(response);
                        alert("Successfully added a member to the project!");

                    },
                    error: function(response) {

                    }
                });
            }
        }
    }
    // get all members here!
    getAllMembers();

});