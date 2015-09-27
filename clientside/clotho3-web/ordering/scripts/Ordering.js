angular.module('LabOrdering')
.service('Ordering', function (Clotho, PubSub) {

	var orderingFunctionId = 'org.clothocad.test.reverse';

	var orders = this.orders = [];

	//Create your own logic - an aggregate of your Ordering functions

	this.order = function (orderParams) {
		//you can run something to a Clotho function, run something locally, access another API... whatever you want

		orders.push(orderParams);

		//Clotho.run() ...
		//console.log() ...
		//$http.post() ... (see the angular docs about this one - for making REST requests)
		PubSub.trigger('LabOrdering.orderPlaced', orderParams);

		return Clotho.run(orderingFunctionId, [orderParams.name]).then(function (result) {
			orderParams.success = true;
			PubSub.trigger('LabOrdering.orderSuccess', result);
		}, function (err) {
			orderParams.success = false;
			PubSub.trigger('LabOrdering.orderFailure', err);
			Clotho.say('error ordering!')
		});
	};

	//Listen for internal messages
	PubSub.on('LabOrdering.internalMessage', function (result) {
		//or create your own listener logic here. Check Tracking.js for another one.
		console.log('Ordering.js message received', result);
	});


});