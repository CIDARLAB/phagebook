angular.module('NewProject').controller('projectCtrl', function ($scope, Clotho, projectAPI, TrackingObj, PubSub) {

    this.createProj = function() {
        //alert('Say something?');
        projectAPI.create($scope.item).then(function () {
            //trigger reset only after item has been successfully or unsuccessfully sent
            //alert("Done");
        });
    };


    //$scope.orders = Ordering.orders;
});