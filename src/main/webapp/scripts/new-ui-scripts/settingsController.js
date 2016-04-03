function settingsCtrl($scope, $http){

    var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
    var editBool = 0;
    $scope.clothoId = clothoId;
    var editValue = document.getElementById("edit-info-btn").value;
    $scope.fixLater = true;
    
    angular.element(document).ready(function () {
        $http({
            method: 'GET',
            url: '../getPersonById',
            params: {
                "userId": clothoId
            }
        }).then(function successCallback(response) {
            console.log("inside successCall of get person GET");
            var responseAsJSON = angular.fromJson(response.data);
            $scope.editFirstName = response.data.firstName;
            $scope.editLastName = response.data.lastName;
            $scope.editEmail = response.data.name;
            $scope.editPassword = "need help here";
            $scope.editInstitution = response.data.institution;
            $scope.editDepartment = response.data.department;
            $scope.editTitle = response.data.title;
            $scope.editLab = response.data.lab;
            $scope.institution = response.data.institutions;
            $scope.department = response.data.department;
            $scope.title = response.data.title;
        }, function errorCallback(response) {
            console.log("inside GET error");
        });
    });

    var fileExt = ".jpg";
    var awsPath = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture";

    $scope.profilePictureLink = awsPath + fileExt;
    console.log(awsPath + fileExt);

    var save = false;
    $scope.edit = true;
    $scope.$watch('edit', function () {
        $scope.editText = $scope.edit ? 'Edit Information' : 'Save Changes';
        if ($scope.edit) {
            $scope.editText = 'Edit Information';
            if (save) {
                $http({
                    method: 'POST',
                    url: '../getPersonById',
                    params: {
                        "userId": clothoId,
                        "institution": $scope.newInst
                    }
                }).then(function successCallback(response) {
                    console.log("some success in setPersonById ajax call");
                }, function errorCallback(response) {
                    console.log("inside setPersonById ajax error");
                });
            }
            save = false;
        } else {
            $scope.editText = 'Save Changes';
            save = true;
        }
    });
}
