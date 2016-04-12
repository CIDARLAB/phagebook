function colleagueCtrl($scope, $http) {
    // var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    var clothoId = getParameterByName("user");
    console.log(clothoId);
    $scope.clothoId = clothoId;
    var fileExt = ".jpg";
    var awsPath = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture";

    $scope.profilePictureLink = awsPath + fileExt;

    angular.element(document).ready(function ($scope) {
        console.log("before ajax but inside page onload.`");
        $.ajax({
            type: 'GET',
            url: '../getPersonById',
            data: {
                "userId": clothoId
            },
            success: function (response) {
                var responseAsJSON = angular.fromJson(response);
                console.log(JSON.stringify(responseAsJSON));
                $("#profile-name").text(responseAsJSON.fullname);
                $("#profile-department").text(responseAsJSON.department);
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

        $("#add-colleague-btn").click(function () {
            console.log(this.value);  
            $.ajax({
                type: 'POST',
                url: '../addColleagueRequest',
                data: {
                    "colleagueClothoId" : this.value,
                    "loggedInClothoId" : getCookie("clothoId")
                },
                success: function(response) {
                    
                },
                error: function(response) {
                    
                }
            });

        }); 
        
        
        $("#load-more-btn").click(function () {
            $.ajax({
                type: 'GET',
                url: '../loadUserStatuses',
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
        });
    });
}


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


