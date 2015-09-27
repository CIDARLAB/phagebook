app.controller("projectsCTRL", function($scope){

    $scope.showProject = function(proj){
        alert(proj.text.toLowerCase());
    };



});
