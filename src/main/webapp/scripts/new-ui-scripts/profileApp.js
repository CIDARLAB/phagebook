angular.module('profileApp', [])
        .controller('profileCtrl', profileCtrl)
        .directive('header', function() {
            return {
                restrict: 'E',
                templateUrl: './headerDirective.html'
            };
        })
        .directive('sidebar',sidebar)
        .directive('content', function() {
            return {
                restrict: 'E',
                templateUrl: './profileContentDirective.html'
                
            };
        })
        .directive('footer', footer);