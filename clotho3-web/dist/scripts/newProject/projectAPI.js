angular.module('NewProject').service('projectAPI', function (Clotho, PubSub) {

        //var orderingFunctionId = 'org.clothocad.test.reverse';


        //Create your own logic - an aggregate of your Ordering functions

        this.create = function (projectParams) {
            //Clotho.say('Something');
            //you can run something to a Clotho function, run something locally, access another API... whatever you want
            //alert(orderParams.obj);
            var str = '{';
                str.concat('""id"":','clotho.delevloper.',projectParams.name.toLowerCase(),'"",schema"":','""org.clothocad.model.Projects""',',""name"":', projectParams.name, ',""PI"":',projectParams.PI, ',""Description"":', projectParams.description, '}');
            obj = JSON.parse(projectParams.obj);
            Clotho.create(obj);


        };
        //Listen for internal messages
        PubSub.on('NewProject.internalMessage', function (result) {
            //or create your own listener logic here. Check Tracking.js for another one.
            console.log('projectAPI.js message received', result);
        });

    });