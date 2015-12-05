function profileCtrl($scope, $modal){
    $scope.personObj = {};
    $scope.personID = sessionStorage.getItem("uniqueid");
    
    Clotho.login(sessionStorage.getItem("username"),sessionStorage.getItem("password")).then(function(result) {
        Clotho.get(result.id).then(function(person){
            $scope.personObj = person;
            $scope.currentUser = person;//links the sidebar to the person who is logged in
            $scope.personID = result.id;
            $scope.displayName = person.fullname;
            $scope.pictureName = person.givenname + person.surname;//will need to change based on where pictures go
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
 3: "3317443"
 the numbers above are pubmedIDs that are Doug's publications used for testing purposes*/
        });
    });

    $scope.editInfo = function(){
        //this function just turns the boxes into editable text boxes
        $scope.editBool = true;
    };

    $scope.readProfilePic = function(){
      //save the uploaded file to google drive? or whereever we are going to put them
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
        //restores personObj to the latest (NOT SAVED) version of Clotho Obj
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


        $scope.personObj['statusList'].push($scope.statusObj);
        Clotho.set($scope.personObj);


        $scope.statuses = $scope.personObj['statusList'];
    };

    $scope.loadFriends = function() {

    };

    $scope.displayPub = function() {


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
                        window.alert('unable to use same pubmed ID twice!');
                        console.log('unable to use same pubmedID twice!!')
                    }
                    else {
                        $scope.personObj['pubmedIdList'].push($scope.pubmedId);
                    }
                }
            }
            //console.log(JSON.stringify($scope.personObj));


        pubmed.getCitationsFromIds($scope.personObj['pubmedIdList']).then(function(result){
            //console.log(JSON.stringify(result9));
            $scope.publications = result;
            $scope.$apply();
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
                        //need to probably send an ajax call to find the person/data stuff
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
}

function profileWindowCtrl($scope, $modalInstance, items){
       $scope.colleagueFirstName = "";
        $scope.colleagueLastName = "";
        $scope.colleagueEmail = "";

    $scope.ok = function() {
        //if all fields are blank then an error needs to appear, or a prompt to fill in some of the fields
        if (($scope.colleagueLastName == null) && ($scope.colleagueEmail == null) && ($scope.colleagueFirstName == null))
        {
            alert("Please fill in at least one field");
        }
        else{
            $modalInstance.close({'givenname' : $scope.colleagueFirstName,
                'surname' : $scope.colleagueLastName,
                'email' : $scope.colleagueEmail});
        }

    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}