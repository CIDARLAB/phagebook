// This file is responsible for getting the projects. 
// Author: Anna Goncharova (agonchar@bu.edu)

$(document).ready(function() {
    console.log("loaded document!");
    // LEGACY CODE BELOW!

    var userID = getCookie("clothoId");

    var data = {
        "userID": userID,
    };

    //console.log(data);
    var ul = $("#project-link-list");
    $.ajax({
        url: "../getAllProjects",
        type: "POST",
        dataType: "json",
        data: data,
        async: false,
        success: function(response) {
            // response is the array of projects
            // response = JSON.parse(response);
            // //console.log(typeof(response));
            console.log(response);
            for (var i = 0; i < response.length; i++) {
                link = "./project.html?id=" + response[i].projectId;
                var a = document.createElement('a');
                $(a).attr('href', link).text(response[i].projectName);
                //console.log(a);
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

});
