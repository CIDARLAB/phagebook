angular.module('profileApp', [])
        .controller('profileCtrl', profileCtrl)
        .directive('header', header)
        .directive('sidebar',sidebar)
        .directive('content', function() {
            return {
                restrict: 'E',
                templateUrl: './profileContentDirective.html'
                
            };
        })
        .directive('footer', footer);