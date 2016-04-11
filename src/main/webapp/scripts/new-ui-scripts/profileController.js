function profileCtrl($scope, $http) {
    var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    $scope.clothoId = clothoId;
    var fileExt = ".jpg";
    var awsPath = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture";

    $scope.profilePictureLink = awsPath + fileExt;

    $("#createStatusBtn").click(function () {
        alert("Create New Status Button click" + $("#statusUpdateTextarea")[0].value);
        $http({
            method: 'POST',
            url: '../createStatus',
            params: {
                "clothoId": clothoId,
                "status": $("#statusUpdateTextarea")[0].value
            }
        }).then(function successCallback(response) {
            console.log("some success in getPersonById status ajax call");
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
                $scope.statuses = responseAsJSON.statuses;
            },
            error: {
                //console.log("inside GET error");
            }
        });

        console.log("after ajax");
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

                    for (var i = 0; i < response.length; i++){
                        var tmpl = document.getElementById("colleague-template").content.cloneNode(true);
                        tmpl.querySelector(".colleague-name").text = response[i].fullname;
                        tmpl.querySelector(".main-lab").innerHTML  = (response[i].labName == null) ? "" : response[i].labName;
                        tmpl.querySelector(".main-institution").innerHTML = response[i].institutionName;
                        tmpl.querySelector(".colleague-name").href  = "../html/colleague.html?user=" + response[i].clothoId;
                        ul.append(tmpl);
                    }
                },
                error: {
                }
            });
            return false;
        });
    });

    var tmpl = document.getElementById('status-template');
    document.body.appendChild(tmpl.content.cloneNode(true));

    var statusList = document.getElementById('statuses');
    var statuses = [{"date":"Sun Apr 10 22:36:48 EDT 2016","text":"hola"},
            {"date":"Sun Apr 10 22:48:50 EDT 2016","text":"blah blah"}]; 
    for (var i = 0; i < statuses.length; i++) {
        var status = statuses[i];
        var tmpl = document.getElementById('status-template').content.cloneNode(true);
        tmpl.querySelector('.status-date').innerText = status.date;
        tmpl.querySelector('.status-text').innerText = status.text;
        statusList.appendChild(tmpl);
    }
 

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

