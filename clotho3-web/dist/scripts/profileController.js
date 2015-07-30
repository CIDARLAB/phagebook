//angular.module('pubmedRoot',[]).run(["pubmed",function(){}]);
angular.module('profileApp',['clothoRoot'/*,'pubmedRoot'*/]).controller('profileController',
function($scope, Clotho/*, pubmed*/){
    $scope.personObj = {};
    $scope.personID = sessionStorage.getItem("uniqueid");

    Clotho.login(sessionStorage.getItem("username"),sessionStorage.getItem("password")).then(function(result) {
        Clotho.get(result.id).then(function(person){
            $scope.personObj = person;
            $scope.personID = result.id;
            $scope.displayName = person.fullname;
            $scope.pictureName = person.givenname + person.surname;
            $scope.$apply();

            if (person.pubmedId == "") {
                $scope.idExist = false;
            }
            else {
                $scope.idExist = true;
                /*pubmed.getCitationsFromIds(person.pubmedId).then(function(result){
                    $scope.publications = result;
                });*/
            }

        });
    });

    $scope.editInfo = function(){
        //this function just turns the boxes into editable text boxes
        $scope.editBool = true;
    };

    $scope.save = function() {
        //This function needs to make the text boxes not editable
        //also needs to update the Clotho person object
        Clotho.get($scope.personID).then(function(){
            Clotho.set($scope.personObj);
        });
        console.log($scope.personObj);
        $scope.editBool = false;
    };

    $scope.cancel = function() {
        //function makes the text boxes not editable
        $scope.editBool = false;
    };

    $scope.createStatus = function() {
        $scope.timeStamp = new Date();
        //$scope.statusObj = "{ \"schema\": \"org.clothocad.phagebook.status\", \"time\" :\"" + $scope.timeStamp + "\" , \"text\": \"" + $scope.newStatus + "\" , \"person\": \"" + $scope.personID + "\" }";
        $scope.statusObj = {
            "statusMessaage" : $scope.newStatus,
            "timeStamp" : $scope.timeStamp
            //eventually there could be a location key value pair
        };
        //$scope.obj = JSON.parse($scope.statusObj);
        Clotho.create($scope.statusObj).then(function() {
            Clotho.get($scope.personID).then(function () {
                $scope.personObj['statusList'].push($scope.statusObj);
                Clotho.set($scope.personObj);
                //console.log(JSON.stringify($scope.personObj));
            });
        });
    };

    $scope.loadFriends = function() {
        if ($scope.personObj.friendsList == null){

        }
        else {

        }

    };

    $scope.loadStatuses = function(){
        console.log("WIEGHIWEGJO")
        for ($scope.i = 0; i < 3; i++){
            console.log(i);
        }

      //$scope.loadedStatuses = $scope.personObj.statusList[4]['statusMessage'];
    };

    $scope.displayPub = function() {

        Clotho.get($scope.personID).then(function(){
            $scope.personObj['pubmedId'] = $scope.pubmedId;
            console.log(JSON.stringify($scope.personObj));
            Clotho.set($scope.personObj);
        });

        $scope.idExist = true;
    };

});



