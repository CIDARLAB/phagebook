angular.module('NewProject').service('trackingNewProject', function(Clotho, PubSub){

var trackingFunctionId = 'org.clothocad.test.reverse';

function sendTrackingEvent(params, evt) {
		/*
		//you'd have to create this custom function on the server - you can do that via the editor on the main branch (this stripped down version does not have it)
		Clotho.run('myCustomTrackingFunctionID', [params, evt]).then(function (result) {
			//uncomment this section and add your own code
		});
		 */

		return Clotho.run(trackingFunctionId, [project.name]).then(function (result) {
			PubSub.trigger('NewProject.internalMessage',  result);
			return result;
		});
	}



	//an event triggered by the Ordering Service
	PubSub.on('NewProject.orderPlaced', function (orderParams) {
		console.log('order was placed! tracking it', orderParams);
	});

	PubSub.on('NewProject.orderSuccess', function (result) {
		console.log('order was placed successfully! result: ', result);
	});

	PubSub.on('NewProject.orderFailure', function (err) {
		console.error('order was NOT placed: ', err);
	});

	//Listen for events to pick up
	PubSub.on('NewProject.myTrackingEvent', function (params, evt) {
		sendTrackingEvent(params, evt).then(function (result) {
			//if you want to do something with the result of the function directly...
		});
	});

	//Listen for internal messages
	PubSub.on('NewProject.internalMessage', function (result) {
		//or create your own listener logic here. Check ORdering.js for another one.
		console.log('trackingSample.js message received', result);
	});

	this.trackReset = function (partialOrder) {
		console.log('order cancelled', partialOrder);
	}

});