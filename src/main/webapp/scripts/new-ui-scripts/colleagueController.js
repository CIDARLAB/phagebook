function colleagueCtrl($scope, $http) {
    // var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    var clothoId = getParameterByName("user");
    console.log(clothoId);
    $scope.clothoId = clothoId;
    var fileExt = ".jpg";
    var awsPath = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture";

    $scope.profilePictureLink = awsPath + fileExt;

    angular.element(document).ready(function ($scope) {

        $("#add-colleague-btn").click(function () {
            console.log(this.value);
            $.ajax({
                type: 'POST',
                url: 'addColleagueRequest',
                data: {
                    "colleagueClothoId": this.value,
                    "loggedInClothoId": getCookie("clothoId")
                },
                success: function (response) {

                },
                error: function (response) {

                }
            });

        });

        $.ajax({
            type: 'GET',
            url: 'loadColleagues',
            async: false,
            data: {
                "userId": clothoId
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
            },
            error: {
                //console.log("inside GET error");
            }
        });

        $.ajax({
            type: 'GET',
            url: 'loadUserStatuses',
            async: false,
            data: {
                "clothoId": clothoId
            },
            success: function (response) {
                var responseAsJSON = angular.fromJson(response);
                console.log(JSON.stringify(responseAsJSON));

                var ul = $("#status-list");
                ul.empty();
                for (var i = response.length - 1; i >= 0; i--) {
                    var tmpl = document.getElementById("status-template").content.cloneNode(true);
                    var now = new Date(response[i].dateCreated);
                    tmpl.querySelector(".status-date").innerText = "Created On: " + response[i].dateCreated;
                    tmpl.querySelector(".status-text").innerText = response[i].statusText;
                    ul.append(tmpl);
                }
            },
            error: {
                //console.log("inside GET error");
            }
        });
        
            $("#load-more-pub-btn").click(function () {
                $.ajax({
                    type: 'GET',
                    url: 'loadPublications',
                    async: false,
                    data: {
                        "clothoId": clothoId
                    },
                    success: function (response) {

                        var ul = $("#publications-list");
                        ul.empty();

                        for (var i = 0; i < response.length; i++) {
                            var tmpl = document.getElementById("publication-template").content.cloneNode(true);
                            tmpl.querySelector(".publication-year").innerText = "Year : " + response[i].pubYear;
                            tmpl.querySelector(".publication-title").innerText = "Title : " + response[i].pubTitle;
                            tmpl.querySelector(".publication-author").innerText = "Authors : " + response[i].pubAuthors;
                            tmpl.querySelector(".publication-information").innerText = "Other Information : " + response[i].pubInfo;
                            tmpl.querySelector(".publication-bibtex").innerText = "Bibtex : " + response[i].pubBibtex;
                            ul.append(tmpl);
                        }
                    },
                    error: {
                    }
                });
            });

    });

$(document).ready(function () {
    var clothoId = getParameterByName("user");
    console.log("before ajax but inside page onload.`");
    $.ajax({
        type: 'GET',
        url: 'getPersonById',
        data: {
            "userId": clothoId
        },
        success: function (response) {
            var responseAsJSON = angular.fromJson(response);
            console.log(JSON.stringify(responseAsJSON));
            $("#profile-name").text(responseAsJSON.fullname);
            $("#profile-dept").text(responseAsJSON.department);
            $("#profile-institution").text(responseAsJSON.institution);
            $("#profile-title").text(responseAsJSON.title);
            $("#profile-description").text(responseAsJSON.profileDescription);
            document.getElementById('add-colleague-btn').value = clothoId;
            //$scope.statuses = responseAsJSON.statuses;
        },
        error: {
            //console.log("inside GET error");
        }
    });
});
}
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


