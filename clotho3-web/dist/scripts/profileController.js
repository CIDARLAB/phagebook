angular.module('profileApp',['clothoRoot'])
    .controller('profileController', function($scope, Clotho){
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
            });
            $scope.$apply();

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
        //console.log($scope.personObj);
        $scope.editBool = false;
    };

    $scope.cancel = function() {
        //function makes the text boxes not editable
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
            $scope.personObj['pubmedIdList'].push($scope.pubmedId);
            //console.log(JSON.stringify($scope.personObj));
            Clotho.set($scope.personObj);
        });

        $scope.publications = pubmed.getCitationsFromIds($scope.personObj['pubmedIdList']);
    };

    $scope.findFriends = function(size) {
            var myFriendSearch = $modal.open({
                templateURL: 'friendFinder.html',
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
                //do stuff with returned data, like Clotho.set??
            });
        };
});