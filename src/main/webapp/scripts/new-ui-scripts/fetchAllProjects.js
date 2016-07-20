// This file is responsible for getting the projects. 
// Author: Anna Goncharova (agonchar@bu.edu)

$(document).ready(function() {




    // LEGACY CODE BELOW!

    var userID = getCookie("clothoId");

    var data = {
        "userID": userID,
    }
    console.log(data);
    var ul = $("#project-link-list");
    $.ajax({
        url: "../getAllProjects",
        type: "POST",
        dataType: "json",
        data: data,
        success: function(response) {
            // response is the array of projects
            // response = JSON.parse(response);
            // console.log(typeof(response));
            console.log(response);
           
            for (var i = 0; i < response.length; i++) {
                 link = "./project.html?id=" + response[i].projectId;
                var a = document.createElement('a');
                $(a).attr('href', link).text(response[i].projectName);
                console.log(a);
                var tmpl = document.getElementById("project-link-template").content.cloneNode(true);
                tmpl.querySelector(".project-link").appendChild(a);
                ul.append(tmpl);
            
            }

        },
        error: function(err) {
            console.log("ERROR!!");
            console.log(err);
        }
    });

    // // this function creates a dropdown that then allows to select a project and
    // // edit it in a form below
    // var populateDropdown = function(data) {
    //     console.log("In populateDropdown");

    //     console.log("Data is");
    //     console.log(data)

    //     if (data.length > 0) {
    //         var select = document.getElementById("editProjectDropdown");
    //         console.log()
    //             // removeOptions(select);

    //         for (var i = 0; i < data.length; i++) {
    //             var opt = document.createElement('option');
    //             console.log(data[i].fullname);
    //             opt.value = data[i].projectId;
    //             opt.innerHTML = data[i].projectName;
    //             console.log(opt);
    //             select.appendChild(opt);
    //         }
    //     }

    // }

    // // this grabs the id value of a project from a dropdown 
    // // menu, then sends an ajax request 
    // // and populates the form with the values of the given project
    // $("#populateFormButton").click(function() {

    //     console.log(" populateFormButton was clicked!");
    //     console.log("inside populateForm function!");
    //     var selectedProject = $("#editProjectDropdown option:selected").val();
    //     console.log(selectedProject)
    //     var data = {
    //             "projectID": selectedProject
    //         }
    //         // an ajax request to get the project that has project id
    //     $.ajax({
    //         url: "../getProject",
    //         type: "POST",
    //         dataType: "json",
    //         data: data,
    //         success: function(response) {
    //             console.log(response);
    //             console.log("response!!!");
    //             // return to a form populating function
    //             return populateEditForm(response);
    //         },
    //         error: function(err) {
    //             console.log("ERROR!!");
    //             console.log(err);
    //         }
    //     });
    // });

    // $("#submitEditChange").click(function() {

    //     console.log("submitEditChange button was clicked!");
    //     console.log("in edit function!!");
    //     var selectedProject = $("#editProjectDropdown option:selected").val();
    //     console.log(selectedProject == '');
    //     if (selectedProject == '') {
    //         alert("Please select a project from the dropdown menu!");
    //     } else {
    //         var newName = "";
    //         var newDesc = "";
    //         var newBudget = "";

    //         if ($("#name").val() != null) {
    //             newName = $("#name").val()
    //         };
    //         if ($("#desc").val() != null) {
    //             newDesc = $("#desc").val();
    //         };
    //         if ($("#budget").val() != null) {
    //             newBudget = $("#budget").val();
    //         };

    //         console.log(selectedProject);
    //         console.log(userID);
    //         console.log(newDesc);
    //         console.log(newName);
    //         console.log(newBudget);

    //         var data = {
    //             "userID": userID,
    //             "projectID": selectedProject,
    //             "description": newDesc,
    //             "name": newName,
    //             "budget": newBudget
    //         }
    //         $.ajax({
    //             url: "../editProject",
    //             type: "POST",
    //             dataType: "json",
    //             data: data,
    //             success: function(response) {
    //                 console.log(response);
    //                 console.log("response!!!");
    //                 // this reloads the window
    //                 //return location.reload();
    //             },
    //             error: function(err) {
    //                 console.log("ERROR!!");
    //                 console.log(err);
    //             }
    //         });

    //     }

    // });
    // // this function adds an update to the selected project
    // $("#addUpdate").click(function() {
    //     var selectedProject = $("#editProjectDropdown option:selected").val();
    //     console.log("in addStatus function!!");
    //     var newStatus = "";
    //     var emailPeople = document.getElementById("emailPeople").checked;

    //     if ($("#newStatus").val() != null) {
    //         if (selectedProject != "") {
    //             newStatus = $("#newStatus").val();
    //             console.log("in add update function");
    //             var data = {
    //                 "userID": userID,
    //                 "projectID": selectedProject,
    //                 "newStatus": newStatus,
    //                 "emailPeople": emailPeople
    //             }
    //             console.log(data);

    //             $.ajax({
    //                 url: "../addUpdateToProject",
    //                 type: "POST",
    //                 dataType: "json",
    //                 data: data,
    //                 success: function(response) {
    //                     //console.log(dataSubmit);
    //                     console.log(response);
    //                     console.log("response!!!");
    //                 },
    //                 error: function(err) {
    //                     console.log("ERROR!!");
    //                     console.log(err);
    //                 }
    //             });
    //         }else{
    //             alert("Please select a project from the dropdown menu first!");
    //         }
    //     } else {
    //         alert("Write new status first!");
    //     }




    // })

    // // fills in the form values with the data from the returned object
    // var populateEditForm = function(project) {
    //     console.log("in populateEditForm, project is");
    //     console.log(project);
    //     $('#name').val(project.projectName);
    //     $('#desc').val(project.description);
    //     $('#budget').val(project.budget);
    // }


});