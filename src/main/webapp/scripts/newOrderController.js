var newOrderModule = angular.module('newOrderApp', ['clothoRoot', 'ui.bootstrap.tpls', 'ui.bootstrap.modal']);

newOrderModule.controller('newOrderController',function($scope, Clotho, $modal){
    $scope.createOrder = function(){
       console.log($scope.orderName +" :: "+ $scope.orderDescription);

        $.ajax({
            url: "newOrder",
            type: "POST",
            async: false,
            data: {
                "name" : $scope.orderName,
                "description" : $scope.orderDescription,
                "timeStamp" : new Date()
            },
            success: function (response) {
                alert(response);
               var project =  response.toJSON();
                if(project.contains("lead")){
                    $scope.lead = project.lead;
                }
                if(project.contains("description")){
                    $scope.description = project.description;
                }
            },
            error: function () {
                alert("ERROR!!");
            }
        });
    };
});

