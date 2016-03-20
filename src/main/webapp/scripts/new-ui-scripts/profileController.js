function profileCtrl($scope) {
   var clothoId = getCookie("clothoId"); //this will get the clothoId from the cookie
   $scope.clothoId = clothoId;
   $scope.profilePictureLink = "http://s3.amazonaws.com/phagebookaws/" + clothoId + "/profilePicture.jpg";

    
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

    $scope.displayPub = function () {


        var idArrayLength = $scope.personObj['pubmedIdList'].length;
        if (idArrayLength == 0)
        {
            $scope.personObj['pubmedIdList'].push($scope.pubmedId);
        } else {
            for (var i = 0; i < idArrayLength; i++) {
                var checkExist = false; //ng-repeat will not display the same publicaiton twice, so a check needs to exist
                if ($scope.pubmedId == $scope.personObj['pubmedIdList'][i]) {
                    checkExist = true;
                    window.alert('unable to use same pubmed ID twice!');
                    console.log('unable to use same pubmedID twice!!');
                } else {
                    $scope.personObj['pubmedIdList'].push($scope.pubmedId);
                }
            }
        }
        //console.log(JSON.stringify($scope.personObj));


        pubmed.getCitationsFromIds($scope.personObj['pubmedIdList']).then(function (result) {
            //console.log(JSON.stringify(result9));
            $scope.publications = result;
            $scope.$apply();
        });
    };

}



function getParameterByName(name)
{
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}