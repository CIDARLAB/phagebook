function profileCtrl($scope, $modal){
    $("document").ready(function () {
    $.ajax({
        url: 'getPersonById',
        type: 'GET',
        async: false,
        data: {
            "userId": getParameterByName("user")
        },
        success: function (response) {
            var responseAsJSON = JSON.parse(response);
            $scope.personID = responseAsJSON['loggedUserId'];
            sessionStorage.setItem("loggedUserId", responseAsJSON['loggedUserId']);
            $scope.displayName = responseAsJSON['fullname'];
            $scope.pictureName = responseAsJSON["fullname"];
            $scope.statuses = responseAsJSON["statusList"];
            $scope.publications = responseAsJSON["publicationList"];
        },
        error: {
        }

    });
    });



/*0: "26205025"
 1: "23651287"
 2: "3084710"
 3: "3317443"
 the numbers above are pubmedIDs that are Doug's publications used for testing purposes*/
   
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

function profileWindowController($scope, $modalInstance){
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

function getParameterByName(name) 
{
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}