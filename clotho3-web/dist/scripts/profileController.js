"use strict";angular.module("pubmedRoot").run(["pubmed",function(){}]);

angular.module('profileApp',['clothoRoot','pubmedRoot']).controller('profileController',
function($scope, Clotho, pubmed){
    $scope.personObj = {};
    $scope.personID = sessionStorage.getItem("uniqueid");

    Clotho.login(sessionStorage.getItem("username"),sessionStorage.getItem("password")).then(function(result) {
        Clotho.get(result.id).then(function(person){
            //console.log(JSON.stringify(person));
            $scope.personObj = person;
            $scope.displayName = person.fullname;
            $scope.pictureName = person.givenname + person.surname;
            $scope.$apply();

            if (person.pubmedId == null) {
                $scope.idExist = false;
            }
            else {
                $scope.idExist = true;
                pubmed.getCitationsFromIds(person.pubmedId);
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
        Clotho.set($scope.personObj);
        console.log($scope.personObj);
        $scope.editBool = false;
    };

    $scope.cancel = function() {
        //function makes the text boxes not editable
        $scope.editBool = false;
    };

    $scope.createStatus = function() {
        $scope.timeStamp = new Date();
        $scope.statusObj = "{ \"schema\": \"org.clothocad.phagebook.status\", \"time\" :\"" + $scope.timeStamp + "\" , \"text\": \"" + $scope.newStatus + "\" , \"person\": \"" + $scope.personID + "\" }";
        $scope.obj = JSON.parse($scope.statusObj);
        Clotho.create($scope.obj).then(function(result) {
            Clotho.get($scope.personID).then(function (personStatusObj) {
                personStatusObj.statusList.push(result);
                //Clotho.set(personObj);
            });
        });
    };

    $scope.loadFriends = function() {
        if ($scope.personObj.friendsList == null){

        }
        else {

        }

    };

    $scope.displayPub = function() {
        $scope.idExist = true;
        $scope.personObj.pubmedId = $scope.publications;
        pubmed.getCitationsFromIds($scope.publications);
    };
});



