angular.module('LabOrdering').controller('OrderingCtrl', function ($scope, Clotho, Ordering, Tracking, PubSub) {

	function resetItem () {
		$scope.item = {};
	}

	this.placeOrder = function() {
		Ordering.order($scope.item).then(function () {
			//trigger reset only after item has been successfully or unsuccessfully sent
			resetItem();
			$scope.$digest();
		});
	};

	this.resetOrder = function (form) {
		Tracking.trackReset($scope.item);
		form.$setPristine();
		resetItem();
	};

	$scope.orders = Ordering.orders;
});