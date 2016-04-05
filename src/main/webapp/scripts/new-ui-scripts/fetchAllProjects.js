// This file is responsible for getting all of the projects
// Author: Anna Goncharova (agonchar@bu.edu)

$(document).ready(function() {

    var userID = getCookie("clothoId");

    var data = {
        "userID": userID,
    }

    $.ajax({
        url: "/getAllProjects",
        type: "POST",
        dataType: "json",
        data: data,
        success: function(response) {
            // response is the array of projects
            console.log("response!!!");
            console.log(response);
        },
        error: function(err) {
            console.log("ERROR!!");
            console.log(err);
        }
    });
});