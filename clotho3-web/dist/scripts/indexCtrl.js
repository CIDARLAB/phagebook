angular.module('Index').controller('indexCtrl', function ($scope, Clotho, indexAPI, TrackingIndex, PubSub) {

    this.queryObj = function(){
        indexAPI.query($scope.item).then(function()
        {

        });
    };

});
