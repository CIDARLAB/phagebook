function header($window) {
    return {
        restrict: 'E',
        templateUrl: '../html/headerDirective.html',
        scope: {
            user: '='
        },
        controller: function ($scope, $window) {

            document.getElementById("signout").onclick = function () {
                // add code here
                //alert("clicked");
                //need to clear the cookie and take the user back to the login screen...
                clearCredentialsFromCookie();
                window.location.href = "/";
            };

        },
        link: function ($scope, $log) {
            $scope.items = [
                'The first choice!',
                'And another choice for you.',
                'but wait! A third!'
            ];

            $scope.status = {
                isopen: false
            };

            $scope.toggled = function (open) {
                $log.log('Dropdown is now: ', open);
            };



            $scope.toggleDropdown = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.status.isopen = !$scope.status.isopen;
            };

            $scope.appendToEl = angular.element(document.querySelector('#dropdown-long-content'));

            var clothoId = getParameterByName("user");
            $scope.clothoId = clothoId;

            $.ajax({
                type: 'GET',
                url: '../getPersonById',
                async: false,
                data: {
                    "userId": getCookie("clothoId")
                },
                success: function (response) {
                    var responseAsJSON = angular.fromJson(response);
                    console.log(JSON.stringify(responseAsJSON));
                    var numRequests = response.listColleagueRequests.length;
                    if (numRequests > 0){
                        $scope.friendRequests = numRequests;
                    }
                },
                error: {
                    //console.log("inside GET error");
                }
            });

        }
    };
}


