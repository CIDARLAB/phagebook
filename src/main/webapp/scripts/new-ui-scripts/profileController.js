function profileCtrl($scope, $http) {
    var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    $scope.clothoId = clothoId;
    $scope.profilePictureLink = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture.jpg";

    $("document").ready(function () {
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
                        var li = document.createElement("li");
                        var img = $('<img id="dynamic">');
                        img.attr('src', "../styles/img/mis/johan-pro-pic.jpg");
                        img.appendTo(li);
                        var a = document.createElement('a');
                        a.href = "#";
                        a.text = response[i].fullname;
                        li.appendChild(a);
                        var p1 = document.createElement("p");
                        li.appendChild(p1);

                        var p2 = document.createElement("p");
                        p2.innerHTML = "CIDAR Lab";
                        li.appendChild(p2);


                        var p3 = document.createElement("p");
                        p3.innerHTML = "Boston University";
                        li.appendChild(p3);


                        li.setAttribute('class', 'list-group-item');
                        ul.append(li);
                    }
                },
                error: {
                }

            });

        });

        $("#edit-profile-btn").click(function () {
            window.location = "../html/accountSettings.html";
        });

    });

    $http({
        method: 'GET',
        url: '../getPersonById',
        params: {
            "userId": clothoId
        }
    }).then(function successCallback(response) {
        var responseAsJSON = angular.fromJson(response.data);
        $scope.fullName = response.data.fullname;
        $scope.institution = response.data.institutions[0];
        $scope.department = response.data.department;
        $scope.title = response.data.title;
    }, function errorCallback(response) {
        console.log("inside getPersonById ajax error");
    });

    $("#save-edits-button").click(function () {
        $http({
            method: 'POST',
            url: '../getPersonById',
            params: {
                "userId": clothoId,
                "institution": $scope.newInst
            }
        }).then(function successCallback(response) {

        }, function errorCallback(response) {
            console.log("inside setPersonById ajax error")
        });
    })



    $(document).ready(function () {

        $('#institution').change(function () {
            //this is innefficient... make a servlet to make a call on change instead actually... its easier
            var selectedInstitution = $('#institution option').filter(':selected').text();

            var responseArray = JSON.parse(sessionStorage.getItem("index-institutions"));


            var selectLabs = document.getElementById('lab-name');


            var numberOfInstitutions = responseArray.length;


            for (var i = 0; i < numberOfInstitutions; i++) {

                if (responseArray[i].institutionName === selectedInstitution) {
                    removeOptions(selectLabs);
                    var labsArray = responseArray[i].labs;
                    var labsLength = labsArray.length;
                    for (var j = 0; j < labsLength; j++) {
                        var opt2 = document.createElement('option');
                        opt2.value = labsArray[j].labId;
                        opt2.innerHTML = labsArray[j].labName;
                        selectLabs.appendChild(opt2);
                    }
                } else if (selectedInstitution === "Institution...") {
                    removeOptions(selectLabs);
                    var opt2 = document.createElement('option');
                    opt2.value = "";
                    opt2.innerHTML = "Lab Name...";
                    selectLabs.appendChild(opt2);
                    return;
                }


            }
        });




        $.ajax({
            url: "../loadPhagebookInstitutions",
            type: "GET",
            async: false,
            data:
                    {
                    },
            success: function (response) {
                var responseArray = response.institutions; //array of JSONObjects with labs attached 

                var selectInstitution = document.getElementById('institution');


                sessionStorage.setItem("index-institutions", JSON.stringify(responseArray)); // stores in sess stor
                removeOptions(selectInstitution);

                var opt = document.createElement('option');
                opt.value = "";

                opt.innerHTML = "Institution...";
                selectInstitution.appendChild(opt);

                var numberOfInstitutions = responseArray.length;
                for (var i = 0; i < numberOfInstitutions; i++) {


                    var opt = document.createElement('option');
                    opt.value = responseArray[i].institutionId;

                    opt.innerHTML = responseArray[i].institutionName;
                    selectInstitution.appendChild(opt);


                }

            },
            error: function (response) {
                alert("No Institutions To Load");
            }
        });

    });

}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

//https://github.com/CIDARLAB/phoenix-core/blob/master/phoenix-core/src/main/webapp/javascript/upload.js
//https://github.com/CIDARLAB/phoenix-core/blob/master/phoenix-core/src/main/java/org/cidarlab/phoenix/core/servlets/ClientServlet.java#L56
