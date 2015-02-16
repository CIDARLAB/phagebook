angular.module('Profile').controller('profileCtrl', function ($scope, Clotho, basicAPI, TrackingObj, PubSub) {



    this.createObj = function() {
        //alert('Say something?');
        basicAPI.create($scope.item).then(function () {
            //trigger reset only after item has been successfully or unsuccessfully sent
            //alert("Done");
        });
    };


    this.queryObj = function(){
        basicAPI.query($scope.item).then(function()
        {

        });
    };

    //$scope.orders = Ordering.orders;
});/**
 * Created by karalefort on 2/10/15.
 */
