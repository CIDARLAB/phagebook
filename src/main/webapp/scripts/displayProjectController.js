/**
 * Created by KatieLewis on 12/5/15.
 */

function tabsController($scope) {
    $scope.personId = sessionStorage.getItem("uniqueid");    
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
        var id = getCookie("projectID");
        $.ajax({
            url: "getProject",
            type: "POST",
            data: {
                "id": id //put id here
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


};


