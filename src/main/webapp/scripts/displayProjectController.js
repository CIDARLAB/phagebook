/**
 * Created by KatieLewis on 12/5/15.
 */
angular.module('projectsApp',['clothoRoot']).controller('projectsController',
    function($scope, Clotho){
        $scope.personId = sessionStorage.getItem("uniqueid");
    });


angular.module('tabsApp',[]).controller('tabsController',['$scope',function($scope) {

    $scope.projectName = '';
    $scope.description = '';
    $scope.lead = '';
    $scope.labs = '';
    $scope.CoPIs = '';
    $scope.members = '';
    $scope.creator = '';
    $scope.dateCreated = '';

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
                console.log(response);
                var object = JSON.parse(response);
                console.log("object");
                console.log(object.projectName);
                console.log(object.description);
                $scope.$apply(function(){ //necessary to $apply the changes
                    if(object.projectName != undefined){
                        $scope.projectName = object.projectName;
                    }
                    if(object.description != undefined){
                        $scope.description = object.description;
                    }

                    if(object.members.length > 0){
                        $scope.members = object.members;
                    }
                    if(object.creator !=null){
                        $scope.creator = object.creator;
                    }
                    if(object.dateCreated != null){
                        $scope.dateCreated = object.dateCreated;
                    }

                });

            },
            error: function () {
                alert("ERROR!!");
            }
        });
    };


}]);


