function phagebookSidebar(){
    return {
        restrict: 'E',
        //replace: true,
        templateUrl: './html/phagebookSidebar.html',
        scope: {
            user: '='
        },
        controller: function($scope, $element){
            //put code here if want the code to run before compilation
            //log the user in? pass the person object in?
            //ajax call to servlet which will have the person info
        },
        link: function(scope, elem, attr){
            //put code here if want code to run after compilation
            //pictureName has to link to the person
            //displayName is also a field that has to link
        }
    };
};

//<phageboook-sidebar user = "currentUser"> on the eg profile page with 
//$scope.currentUser = {the person object thats logged in} which should happen in the controller