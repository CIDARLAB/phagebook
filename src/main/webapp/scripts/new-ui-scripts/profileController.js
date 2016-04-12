function profileCtrl($scope, $http) {
    var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    $scope.clothoId = clothoId;
    var fileExt = ".jpg";
    var awsPath = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture";

    $scope.profilePictureLink = awsPath + fileExt;

    $("#createStatusBtn").click(function () {
        //alert("Create New Status Button click" + $("#statusUpdateTextarea")[0].value);
        $http({
            method: 'POST',
            url: '../createStatus',
            params: {
                "clothoId": clothoId,
                "status": $("#statusUpdateTextarea")[0].value
            }
        }).then(function successCallback(response) {
            console.log("some success in getPersonById status ajax call");
            $("#statusUpdateTextarea")[0].value = "";
            console.log(response.message);
        }, function errorCallback(response) {
            console.log("inside getPersonById status ajax error");
            console.log(JSON.stringify(response));
        });
    });

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
                $("#fullName").text(responseAsJSON.fullname);
                $("#dept").text(responseAsJSON.department);
                $("#institution").text(responseAsJSON.institution);
                $("#title").text(responseAsJSON.title);
                $("#profileDescription").text(responseAsJSON.profileDescription);
                //$scope.statuses = responseAsJSON.statuses;
            },
            error: {
                //console.log("inside GET error");  
            }
        });

        $.ajax({
            type: 'GET',
            url: '../loadColleagues',
            async: false,
            data: {
                "userId": clothoId
            },
            success: function (response) {
                var responseSucks = angular.fromJson(response);
                var ul = $("#list-colleagues");
                ul.empty();
                console.log(JSON.stringify(responseSucks));
                for (var i = 0; i < response.length; i++) {
                    var tmpl = document.getElementById("colleague-display-template").content.cloneNode(true);
                    //tmpl.querySelector(".colleague-display-picture-link").src = "http://s3.amazonaws.com/phagebookaws/" + response[i].clothoId + "/profilePicture.jpg";
                    //tmpl.querySelector(".colleague-display-fullname").text = response[i].fullname;
                    //tmpl.querySelector(".colleague-display-lab").innerHTML = (response[i].labName == null) ? "" : response[i].labName;
                    //tmpl.querySelector(".colleague-display-institution").innerHTML = response[i].institutionName;
                    //tmpl.querySelector(".colleague-display-fullname").href = "../html/colleague.html?user=" + response[i].clothoId;

                    ul.append(tmpl);
                }
            },
            error: {
                //console.log("inside GET error");
            }
        });

        $.ajax({
            type: 'GET',
            url: '../loadUserStatuses',
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
    });

    $("#search-colleagues-btn").click(function () {

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
            success: function (response) {
                var ul = $("#search-colleagues-list");
                ul.empty();
                for (var i = 0; i < response.length; i++) {
                    var tmpl = document.getElementById("colleague-template").content.cloneNode(true);
                    tmpl.querySelector(".colleague-name").text = response[i].fullname;
                    tmpl.querySelector(".main-lab").innerHTML = (response[i].labName == null) ? "" : response[i].labName;
                    tmpl.querySelector(".main-institution").innerHTML = response[i].institutionName;
                    tmpl.querySelector(".colleague-name").href = "../html/colleague.html?user=" + response[i].clothoId;
                    ;
                    ul.append(tmpl);
                }
            },
            error: {
            }
        });
        return false;
    });

    $("#edit-profile-btn").click(function () {

        window.location = "../html/accountSettings.html";
    });
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

