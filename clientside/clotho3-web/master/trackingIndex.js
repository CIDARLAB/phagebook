angular.module('Sample').service('TrackingIndex', function (Clotho, PubSub) {


	//Ask Prashant about these functions -- what is being tracked?

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



};