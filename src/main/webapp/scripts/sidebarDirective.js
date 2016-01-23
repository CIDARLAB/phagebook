angular.module('ngPhagebookSidebar').directive('ngPhagebookSidebar', function(){
    return {
        restrict: 'E',
        replace: true,
        template: '../html/phagebookSidebar.html',
        controller: function($scope, $element){
            //put code here if want the code to run before compilation
            //log the user in?
        },
        link: function(scope){
            //put code here if want code to run after compilation
            //pictureName has to link to the person
            //displayName is also a field that has to link
        }
    };
});