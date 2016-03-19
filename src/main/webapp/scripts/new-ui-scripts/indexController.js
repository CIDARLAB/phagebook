function indexCtrl($scope, $modal) {
    
    $scope.login = function(){
        alert("power");
        /*$.ajax({
            url: "loginUser",
            type: "POST",
            async: false,
            data: {
                "email": document.getElementById("returnEmail").value,
                "password": document.getElementById("returnPassword").value
            },
            success: function (response) {
                //alert(response);
                //alert("Got Some Response" + response);
                sessionStorage.setItem("response", response);
                sessionStorage.setItem("username", document.getElementById("returnEmail").value);
                sessionStorage.setItem("password", document.getElementById("password").value);

                window.location.href = './html/profile.html?user=' + response;
            },
            error: function () {
                alert("some sort of error occurred");
            }
        });*/
    }
    $("addPerson").click(function(){
        $.ajax({
            url: "createPerson",
            type: "POST",
            async: false,
            data: {
                //"id": loginResult.id,
                "firstName": document.getElementById("firstname").value,
                "lastName": document.getElementById("lastname").value,
                //"fullname": document.getElementById("firstname").value + " " + document.getElementById("lastname").value,
                //"lab": document.getElementById("lab").value,
                "emailId": document.getElementById("email").value,
                //"institution": document.getElementById("institution").value,
                //"position": document.getElementById("positionList").value,
                //"permissions": document.getElementById("permissionsList").value,
                //"friendsList": [],
                //"statusList": [],
                //"pubmedIdList": [],
                //"activated": false,
                //"activationString": ""
                "password": document.getElementById("password").value
            },
            success: function (response) {

                // sessionStorage.setItem("uniqueid", loginResult.id);
                sessionStorage.setItem("username", document.getElementById("email").value);
                //sessionStorage.setItem("password", document.getElementById("password").value);

                window.location.href = './html/validateEmail.html';
            },
            error: function () {
                alert("some sort of error occurred");
            }
        });
    });

}