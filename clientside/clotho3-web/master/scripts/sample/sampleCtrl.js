angular.module('Sample').controller('sampleCtrl', function ($scope, Clotho, basicAPI, TrackingObj, PubSub) {

	

	this.createObj = function() {
		//alert('Say something?');
		basicAPI.create($scope.item).then(function () {

			//trigger reset only after item has been successfully or unsuccessfully sent
			alert("Done");
		});
	};


	this.queryObj = function(){
		basicAPI.query($scope.item).then(function()
		{
			
		});
	};

	//$scope.orders = Ordering.orders;
});