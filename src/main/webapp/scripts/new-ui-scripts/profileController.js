function profileCtrl($scope, $http) {
    var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    $scope.clothoId = clothoId;
    $scope.profilePictureLink = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture.jpg";

    $http({
        method: 'GET',
        url: 'getPersonById',
        params: {
            "userId": clothoId
        }
    }).then(function successCallback(response) {
        var responseAsJSON = angular.fromJson(response.data);
        sessionStorage.setItem("loggedUserId", responseAsJSON['loggedUserId']);
        $scope.fullName = response.data.fullname;
        $scope.institution = response.data.institution;
        $scope.department = response.data.department;
        $scope.title = response.data.title;
    }, function errorCallback(response) {
        console.log("inside GET error");
    });

    $http({
        method: ''
    })
}



function getParameterByName(name)
{
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}