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

    $scope.getProject = function(id){
        $.ajax({
            url: "getProject",
            type: "GET",
            async: false,
            data: {
                "id": id //put id here
            },
            success: function (response) {
                alert(response);

                //parse string response into JSON, send data to correct fields
            },
            error: function () {
                alert("ERROR!!");
            }
        });
    };


}]);


