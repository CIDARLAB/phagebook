/**
 * Created by KatieLewis on 12/5/15.
 */
angular.module('projectsApp',['clothoRoot']).controller('projectsController',
    function($scope, Clotho){
        $scope.personId = sessionStorage.getItem("uniqueid");
    });


angular.module('tabsApp',[]).controller('tabsController',['$scope',function($scope) {
    $scope.active = 1;
    $scope.selectTab = function (value) {
        $scope.active = value;
    };

    $scope.isActive = function (value) {
        if ($scope.active == value) {
            return true;
        }
        else {
            return false;
        }
    };

    $scope.getProject = function(){
        console.log("Get Project ajax call");
        $.ajax({
            url: "getProject",
            type: "POST",
            data: {
                "id": "56634a6b3004006cd61abf16" //put id here
            },
            success: function (response) {
                alert(response);
                var object = JSON.parse(response);
                $scope.projectName = object.projectName;
                $scope.description = object.description;
                //$scope.lead = object.lead;

            },
            error: function () {
                alert("ERROR!!");
            }
        });
    };


}]);


