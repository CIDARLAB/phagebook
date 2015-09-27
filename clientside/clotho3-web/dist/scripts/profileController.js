var profileModule = angular.module('profileApp',['clothoRoot','ui.bootstrap.tpls','ui.bootstrap.modal']);

profileModule.controller('profileController', function($scope, Clotho, $modal){
    $scope.personObj = {};
    $scope.personID = sessionStorage.getItem("uniqueid");


    Clotho.login(sessionStorage.getItem("username"),sessionStorage.getItem("password")).then(function(result) {
        Clotho.get(result.id).then(function(person){
            $scope.personObj = person;
            $scope.personID = result.id;
            $scope.displayName = person.fullname;
            $scope.pictureName = person.givenname + person.surname;
            $scope.statuses = person['statusList'];

            pubmed.getCitationsFromIds(person.pubmedIdList).then(function(result){
                console.log(JSON.stringify(result));
                $scope.publications = result;
                $scope.$apply();
            });
            $scope.$apply();
/*0: "26205025"
 1: "23651287"
 2: "3084710"
 3: "3317443"*/
        });
    });

    $scope.editInfo = function(){
        //this function just turns the boxes into editable text boxes
        $scope.editBool = true;
    };

    $scope.readProfilePic = function(){
      //save the uploaded file to google drive
    };

    $scope.save = function() {
        //This function needs to make the text boxes not editable
        //also needs to update the Clotho person object
        Clotho.get($scope.personID).then(function(){
            Clotho.set($scope.personObj);
            $scope.apply();
        });
        //console.log($scope.personObj);
        $scope.editBool = false;
    };

    $scope.cancel = function() {
        //function makes the text boxes not editable
        //restores personObj to the latest version of Clotho Obj
        Clotho.get($scope.personId).then(function(result){
           $scope.personObj = result;
        });
        $scope.editBool = false;
    };

    $scope.createStatus = function() {
        $scope.timeStamp = new Date();
        $scope.statusObj = {
            "statusMessage" : $scope.newStatus,
            "timeStamp" : $scope.timeStamp
            //eventually there could be a location key value pair
        };

        Clotho.get($scope.personID).then(function () {
            $scope.personObj['statusList'].push($scope.statusObj);
            Clotho.set($scope.personObj);
        });

        $scope.statuses = $scope.personObj['statusList'];
    };

    $scope.loadFriends = function() {

    };

    $scope.displayPub = function() {

        Clotho.get($scope.personID).then(function(){
            var idArrayLength = $scope.personObj['pubmedIdList'].length;
            if(idArrayLength == 0)
            {
                $scope.personObj['pubmedIdList'].push($scope.pubmedId);
            }
            else {
                for (var i = 0; i < idArrayLength; i++) {
                    var checkExist = false; //ng-repeat will not display the same publicaiton twice, so a check needs to exist
                    if ($scope.pubmedId == $scope.personObj['pubmedIdList'][i]) {
                        checkExist = true;
                        console.log('unable to use same pubmed ID twice!')
                    }
                    else {
                        $scope.personObj['pubmedIdList'].push($scope.pubmedId);
                    }
                }
            }
            //console.log(JSON.stringify($scope.personObj));
        });

        pubmed.getCitationsFromIds($scope.personObj['pubmedIdList']).then(function(result){
            //console.log(JSON.stringify(result9));
            $scope.publications = result;
        });
    };

    $scope.findFriends = function(size) {
            var myFriendSearch = $modal.open({
                templateUrl: 'friendFinder.html',
                controller: 'profileWindowController',
                size: size,
                resolve: {
                    items: function () {
                        //this object will get passed to the modal's controlller
                        return {myPerson: {name: "Bobby"}, myData: 42};
                    }
                }
            });
            myFriendSearch.result.then(function (items) {
                Clotho.query(items).then(function(result){
                    //return foundFriend
                    console.log(JSON.stringify(result));
                });
                //do stuff with returned data, like Clotho.set??
            });
        };
})

.controller('profileWindowController', function($scope, $modalInstance, items){
       $scope.colleagueFirstName = "";
        $scope.colleagueLastName = "";
        $scope.colleagueEmail = "";

    $scope.ok = function() {
        $modalInstance.close({'givenname' : $scope.colleagueFirstName,
            'surname' : $scope.colleagueLastName,
            'email' : $scope.colleagueEmail});
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
});