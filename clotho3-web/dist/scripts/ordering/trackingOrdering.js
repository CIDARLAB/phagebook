angular.module('Ordering').service('TrackingObj', function (Clotho, PubSub) {

	//use the ID once you write a function
	//to demonstrate, let's run a simple function
	var trackingFunctionId = 'org.clothocad.test.reverse';


	function sendTrackingEvent(params, evt) {
		/*
		//you'd have to create this custom function on the server - you can do that via the editor on the main branch (this stripped down version does not have it)
		Clotho.run('myCustomTrackingFunctionID', [params, evt]).then(function (result) {
			//uncomment this section and add your own code
		});
		 */

		return Clotho.run(trackingFunctionId, [params.name]).then(function (result) {
			PubSub.trigger('Ordering.internalMessage',  result);
			return result;
		});
	}

	//an event triggered by the Ordering Service
	PubSub.on('Ordering.orderPlaced', function (orderParams) {
		console.log('order was placed! tracking it', orderParams);
	});

	PubSub.on('Ordering.orderSuccess', function (result) {
		console.log('order was placed successfully! result: ', result);
	});

	PubSub.on('Ordering.orderFailure', function (err) {
		console.error('order was NOT placed: ', err);
	});

	//Listen for events to pick up
	PubSub.on('Ordering.myTrackingEvent', function (params, evt) {
		sendTrackingEvent(params, evt).then(function (result) {
			//if you want to do something with the result of the function directly...
		});
	});

	//Listen for internal messages
	PubSub.on('Ordering.internalMessage', function (result) {
		//or create your own listener logic here. Check ORdering.js for another one.
		console.log('trackingOrdering.js message received', result);
	});

	this.trackReset = function (partialOrder) {
		console.log('order cancelled', partialOrder);
	}

});