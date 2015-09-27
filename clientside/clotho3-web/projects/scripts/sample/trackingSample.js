angular.module('Sample').service('TrackingObj', function (Clotho, PubSub) {

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
			PubSub.trigger('Sample.internalMessage',  result);
			return result;
		});
	}

	//an event triggered by the Ordering Service
	PubSub.on('Sample.orderPlaced', function (orderParams) {
		console.log('order was placed! tracking it', orderParams);
	});

	PubSub.on('Sample.orderSuccess', function (result) {
		console.log('order was placed successfully! result: ', result);
	});

	PubSub.on('Sample.orderFailure', function (err) {
		console.error('order was NOT placed: ', err);
	});

	//Listen for events to pick up
	PubSub.on('Sample.myTrackingEvent', function (params, evt) {
		sendTrackingEvent(params, evt).then(function (result) {
			//if you want to do something with the result of the function directly...
		});
	});

	//Listen for internal messages
	PubSub.on('Sample.internalMessage', function (result) {
		//or create your own listener logic here. Check ORdering.js for another one.
		console.log('trackingSample.js message received', result);
	});

	this.trackReset = function (partialOrder) {
		console.log('order cancelled', partialOrder);
	}

});