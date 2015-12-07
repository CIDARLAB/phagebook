function newOrderController($scope){
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
            },
            error: function () {
                alert("ERROR!!");
            }
        });
    };
}

