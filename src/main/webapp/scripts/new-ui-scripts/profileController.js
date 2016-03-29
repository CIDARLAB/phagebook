function profileCtrl($scope, $http) {
    var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    var editBool = 0;
    $scope.clothoId = clothoId;
    var editValue = document.getElementById("edit-info-btn").value;

    angular.element(document).ready(function () {
        console.log("before ajax but inside page onload.`");
        $http({
            method: 'GET',
            url: '../getPersonById',
            params: {
                "userId": clothoId
            }
        }).then(function successCallback(response) {
            console.log("inside successCall of get person GET");
            var responseAsJSON = angular.fromJson(response.data);
            $scope.fullName = response.data.fullname;
            $scope.editFirstName = response.data.firstName;
            $scope.editLastName = response.data.lastName;
            $scope.editEmail = response.data.name;
            $scope.editPassword = "need help here";
            $scope.editInstitution = response.data.institution;
            $scope.editDepartment = response.data.department;
            $scope.editTitle = response.data.title;
            $scope.editLab = response.data.lab;
            $scope.institution = response.data.institutions[0];
            $scope.department = response.data.department;
            $scope.title = response.data.title;
        }, function errorCallback(response) {
            console.log("inside GET error");
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
        var fileExt = ".jpg";
        var awsPath = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture";

        UrlExists(awsPath + fileExt, function (status) {
            if (status === 200) {
                // file was found
                console.log("it is a jpeg");
            } else if (status === 404) {
                // 404 not found
                fileExt = ".png";
            }
        });
        $scope.profilePictureLink = awsPath + fileExt;
        console.log(awsPath + fileExt);

    });

    $("#edit-profile-btn").click(function () {
        window.location = "../html/accountSettings.html";
    });

    var save = false;
    $scope.edit = true;
    $scope.$watch('edit', function () {
        $scope.editText = $scope.edit ? 'Edit Information' : 'Save Changes';
        if ($scope.edit) {
            $scope.editText = 'Edit Information';
            if (save) {
                $http({
                    method: 'POST',
                    url: '../getPersonById',
                    params: {
                        "userId": clothoId,
                        "institution": $scope.newInst
                    }
                }).then(function successCallback(response) {
                    console.log("some success in setPersonById ajax call");
                }, function errorCallback(response) {
                    console.log("inside setPersonById ajax error");
                });
            }
            save = false;
        } else {
            $scope.editText = 'Save Changes';
            save = true;
        }
    })

}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function UrlExists(url, cb) {
    jQuery.ajax({
        url: url,
        dataType: 'text',
        type: 'GET',
        complete: function (xhr) {
            if (typeof cb === 'function')
                cb.apply(this, [xhr.status]);
        }
    });
}
//https://github.com/CIDARLAB/phoenix-core/blob/master/phoenix-core/src/main/webapp/javascript/upload.js
//https://github.com/CIDARLAB/phoenix-core/blob/master/phoenix-core/src/main/java/org/cidarlab/phoenix/core/servlets/ClientServlet.java#L56
