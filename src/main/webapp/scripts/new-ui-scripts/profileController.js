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
    
    $http({
        method: 'POST',
        url: 'getPersonById',
        params: {
            "userId" : clothoId,
            "institution" : $scope.newInst
        }
    }).then(function successCallback(response) {
        
    }, function errorCallback(response){
       console.log("inside setPersonById ajax error")
    });

}


function getParameterByName(name){
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

//https://github.com/CIDARLAB/phoenix-core/blob/master/phoenix-core/src/main/webapp/javascript/upload.js
//https://github.com/CIDARLAB/phoenix-core/blob/master/phoenix-core/src/main/java/org/cidarlab/phoenix/core/servlets/ClientServlet.java#L56
