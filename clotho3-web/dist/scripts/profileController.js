var phagebookProfileApp = angular.module('profileApp', ['clothoRoot']);

phagebookProfileApp.controller('profileController',
function($scope){
    $scope.load = function() {
        alert("ALERT");
    };

    var personObj = Clotho.get({id: sessionStorage.getItem("uniqueid")});
    alert(JSON.stringify(personObj));
    $scope.displayName = "personObj.fullname";
    $scope.institution = "personObj.institution";
    $scope.position = "personObj.position";
    $scope.lab = "personObj.lab";

});


