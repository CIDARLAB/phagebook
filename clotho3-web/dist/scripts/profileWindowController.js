angular.module('profileApp',['clothoRoot'])
    .controller('profileWindowController',
function($scope, $modalInstance, items){
   $scope.ok = function() {
       $modalInstance.close(items);
   };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

    $scope.someOtherFunction = function(){
      //other stuff here
    };
});