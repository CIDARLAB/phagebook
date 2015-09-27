angular.module('Index')
.service('indexAPI', function (Clotho, PubSub) {


	this.query = function (orderParams) {
		var obj = {};
        obj[orderParams.userName] = orderParams.password;
		Clotho.query(obj).then(function(data) 
		{
			if(data.length > 0)
			{
				//alert(data[0]["name"]);	
				for (var key in data) 
            	{
                	if (data.hasOwnProperty(key)) 
                	{
						if(typeof data[key] == 'object')
                    	{
                    		var longStr = "";
                    		for(var subkey in data[key])
                        	{
                    			longStr += (subkey + " : " + data[key][subkey]+"\n");
                    		}
                    		orderParams.results = longStr;
                    		//$location.path("https://localhost:8443/Profile.html")
                    	}
                    	else
                    	{
                        	alert(key + " ::: " + data[key]);	   
                    	}
                    }
                }
			}
			
			//alert(data);
		},function (err) {
			
			Clotho.say('error!')
		});
		;

		
	};

});