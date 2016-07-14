/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function(){
    
    //Colleague Controller
    $('#addColleagueRequest').click(function () {

        $.ajax({
            url: "addColleagueRequest",
            type: "POST",
            data: {
                "colleagueClothoId": this.value,
                "loggedInClothoId": getCookie("clothoId")
            },

            success: function () {
                $("#addColleagueRequest").css("background-color", "green");
            },

            error: function() {
                $("#addColleagueRequest").css("background-color", "red");
            }
        });
    });
    
    $('#approveColleagueRequest').click(function () {

        $.ajax({
            url: "approveColleagueRequest",
            type: "POST",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
                "colleagueId": this.value
            },

            success: function () {
                $("#approveColleagueRequest").css("background-color", "green");
            },

            error: function() {
                $("#approveColleagueRequest").css("background-color", "red");
            }
        });
    });
    
    $('#denyColleagueRequest').click(function () {

        $.ajax({
            url: "denyColleagueRequest",
            type: "POST",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
                "colleagueId": this.value
            },

            success: function () {
                $("#denyColleagueRequest").css("background-color", "green");
            },

            error: function() {
                $("#denyColleagueRequest").css("background-color", "red");
            }
        });
    });
    
    $('#listColleagueRequests').click(function () {

        $.ajax({
            url: "listColleagueRequests",
            type: "GET",
            async: false,
            data: {
                "userId": getCookie("clothoId")
            },

            success: function () {
                $("#listColleagueRequests").css("background-color", "green");
                
                var alrt = colleagueJSON.firstName + " " + colleagueJSON.lastName;
                alert(alrt);
            },

            error: function() {
                $("#listColleagueRequests").css("background-color", "red");
            }
        });
    });
    
    $('#loadColleagues').click(function () {

        $.ajax({
            url: "loadColleagues",
            type: "GET",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
            },

            success: function (response) {
                
                
                var ul = $("#colleagues-full-list");
                ul.empty();
                console.log(JSON.stringify(response));
                for (var i = 0; i < response.length; i++) {
                    var tmpl = document.getElementById("colleague-display-template").content.cloneNode(true);
                    // var othertmpl = document.getElementById("colleague-page-template").content.cloneNode(true);
                    tmpl.querySelector(".colleague-page-picLink").src = "http://s3.amazonaws.com/phagebookaws/" + response[i].clothoId + "/profilePicture.jpg";
                    tmpl.querySelector(".colleague-page-picLink").alt = response[i].fullname;
                    tmpl.querySelector(".colleague-display-fullname").text = response[i].fullname;
                    tmpl.querySelector(".colleague-display-lab").innerHTML = (response[i].labName == null) ? "" : response[i].labName;
                    tmpl.querySelector(".colleague-display-institution").innerHTML = response[i].institutionName;
                    tmpl.querySelector(".colleague-display-fullname").href = "html/colleague.html?user=" + response[i].clothoId;
                    ul.append(tmpl);
                }
                
                $("#loadColleagues").css("background-color", "green");
            },

            error: function() {
                $("#loadColleagues").css("background-color", "red");
            }
        });
    });
    
    
    
    
    
    
    $('#loginUser').click(function () {

        $.ajax({
            url: "loginUser",
            type: "POST",
            data: {
                "email": "jkozol@bu.edu",
                "password": "password"
            },

            success: function (response) {
                $("#loginUser").css("background-color", "green");

                setCookie("clothoId", response.clothoId, 1);
                setCookie("emailId", response.emailId, 1);
            },

            error: function() {
                $("#loginUser").css("background-color", "red");
            }
        });
    });
});
    
    
    
    
    
    
