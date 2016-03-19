angular.module('profileApp', [])
        .controller('profileCtrl', profileCtrl)
        .directive('header', function() {
            return {
                restrict: 'E',
                templateUrl: './headerDirective.html'
            };
        })
        .directive('sidebar', function() {
            return {
                restrict: 'E',
                //replace: true,
                templateUrl: './sidebarDirective.html',
                scope: {
                    user: '=',
                },
                controller: function($scope, $element){
                    $scope.profileBtn = function(){
                    var response = sessionStorage.getItem("loggedUserId");
                    window.location.href = './html/profile.html?user=' + response;;
                };
                //put code here if want the code to run before compilation
                //ajax call to servlet which will have the person info
                },
                link: function(scope, elem, attr){
                //put code here if want code to run after compilation
                //pictureName has to link to the person
                //displayName is also a field that has to link
                }
            };
        })
        .directive('content', function() {
            return {
                restrict: 'E',
                templateUrl: './profileContentDirective.html'
            };
        })
        .directive('footer', footer);