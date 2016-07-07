function profileCtrl($scope, $http) {
    var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    $scope.clothoId = clothoId;
    var fileExt = ".jpg";
    var awsPath = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture";
    //alert("dumb" + clothoId);
    $scope.profilePictureLink = awsPath + fileExt;

    $("#createStatusBtn").click(function () {
        //alert("Create New Status Button click" + $("#statusUpdateTextarea")[0].value);
        $http({
            method: 'POST',
            url: 'createStatus',
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
            url: 'getPersonById',
            async: false,
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

                var ul2 = $("#list-colleagues");
                ul2.empty();
                console.log("hihihi: " + JSON.stringify(response));
                if (response.length > 0) {
                    for (var i = 0; i < 1; i++) { //change bounds when we can figure out css 
                        var tmpl = document.getElementById("colleague-display-template").content.cloneNode(true);
                        //alert("response: " + response[i]);
                        // var othertmpl = document.getElementById("colleague-page-template").content.cloneNode(true);
                        tmpl.querySelector(".colleague-page-picLink").src = "http://s3.amazonaws.com/phagebookaws/" + response[i].clothoId + "/profilePicture.jpg";
                        tmpl.querySelector(".colleague-page-picLink").alt = response[i].fullname;
                        tmpl.querySelector(".colleague-display-fullname").text = response[i].fullname;
                        tmpl.querySelector(".colleague-display-lab").innerHTML = (response[i].labName == null) ? "" : response[i].labName;
                        tmpl.querySelector(".colleague-display-institution").innerHTML = response[i].institutionName;
                        tmpl.querySelector(".colleague-display-fullname").href = "html/colleague.html?user=" + response[i].clothoId;
                        ul2.append(tmpl);
                    }
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
                //console.log("statuses: " + JSON.stringify(responseAsJSON));

                var ul = $("#status-list");
                ul.empty();
                for (var i = response.length - 1; i >= 0; i--) {
                    var tmpl = document.getElementById("status-template").content.cloneNode(true);
                    var now = new Date(response[i].dateCreated);
                    tmpl.querySelector(".status-date").innerText = "Created On: " + chooseMonth(now.getUTCMonth()) + " " + now.getUTCDay() + ", " + now.getFullYear() + " " + now.getHours() + ":" + now.getMinutes();
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
            url: 'queryFirstLastName',
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
                    tmpl.querySelector(".colleague-name").innerHTML = response[i].fullname;
                    tmpl.querySelector(".main-link").alt = response[i].fullname;
                    tmpl.querySelector(".main-lab").innerHTML = (response[i].labName == null) ? "" : response[i].labName;
                    tmpl.querySelector(".main-institution").innerHTML = response[i].institutionName;
                    tmpl.querySelector(".colleague-name").href = "html/colleague.html?user=" + response[i].clothoId;
                    tmpl.querySelector(".main-link").src = "http://s3.amazonaws.com/phagebookaws/" + response[i].clothoId + "/profilePicture.jpg";
                    ul.append(tmpl);
                }


            },
            error: function (response) {

                return false;
                //console.log("inside GET error");
            }
        });
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

    $("#load-more-btn").click(function () {
        location.reload();
    });

    $("#edit-profile-btn").click(function () {
        window.location = "html/accountSettings.html";
    });
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


function chooseMonth(number) {
    switch (number) {
        case 1:
            return "January";
            break;
        case 2:
            return "February";
            break;
        case 3:
            return "March";
            break;
        case 4:
            return "April";
            break;
        case 5:
            return "May";
            break;
        case 6:
            return "June";
            break;
        case 7:
            return "July";
            break;
        case 8:
            return "August";
            break;
        case 9:
            return "September";
            break;
        case 10:
            return "October";
            break;
        case 11:
            return "November";
            break;
        case 12:
            return "December";
            break;
        default:
            return "";
            break;
    }
}
