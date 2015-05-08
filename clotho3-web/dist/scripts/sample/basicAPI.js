angular.module('Sample')
.service('basicAPI', function (Clotho, PubSub) {

        //var orderingFunctionId = 'org.clothocad.test.reverse';

        var orders = this.orders = [];

        //Create your own logic - an aggregate of your Ordering functions

        this.create = function (orderParams) {
            //Clotho.say('Something');
            //you can run something to a Clotho function, run something locally, access another API... whatever you want
            //alert(orderParams.obj);
            obj = JSON.parse(orderParams.obj);
            Clotho.set(obj);


        };


        this.query = function (orderParams) {
            var obj = {};
            obj[orderParams.param] = orderParams.val;
            Clotho.query(obj).then(function (data) {

                orderParams.results = JSON.stringify(data);
            });
        }
    });


			/*if(data.length > 0)
			{
				//alert(data[0]["name"]);	
<<<<<<< HEAD
				orderParams.results = JSON.stringify(data,undefined,4);

			}

<<<<<<< HEAD
=======
	this.query = function (orderParams) {
		var obj = {};
        obj[orderParams.param] = orderParams.val;
		Clotho.query(obj).then(function(data) 
		{
			orderParams.results = JSON.stringify(data);

>>>>>>> 3f86d16826f453fcfdb789f3dec8ae63524ff776
			//alert(data);
		},function (err) {
			
			Clotho.say('error!')
		});
		;

		
	};


	//Listen for internal messages
	PubSub.on('Sample.internalMessage', function (result) {
		//or create your own listener logic here. Check Tracking.js for another one.
		console.log('basicAPI.js message received', result);
	});


});*/
