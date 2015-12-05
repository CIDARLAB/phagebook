/**
 * Created by KatieLewis on 12/5/15.
 */
angular.module('projectsApp',['clothoRoot']).controller('projectsController',
    function($scope, Clotho){
        $scope.personId = sessionStorage.getItem("uniqueid");


    });


angular.module('tabsApp',[]).controller('tabsController',['$scope',function($scope){
    $scope.active = 1;
    $scope.selectTab = function(value){
        $scope.active = value;
    }

    $scope.isActive = function(value){
        if($scope.active==value){
            return true;
        }
        else{
            return false;
        }
    }
}]);
