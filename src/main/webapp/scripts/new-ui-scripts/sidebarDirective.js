function sidebar(){

        return {
        restrict: 'E',
        //replace: true,
        templateUrl: '../html/sidebarDirective.html',
        scope: {
            user: '='
        },
        controller: function ($scope, $element) {
            $scope.profileBtn = function () {


            };
            //put code here if want the code to run before compilation
            //ajax call to servlet which will have the person info
        },
        link: function (scope, elem, attr) {
            //put code here if want code to run after compilation
            //pictureName has to link to the person
            //displayName is also a field that has to link
        }
    };
  
}