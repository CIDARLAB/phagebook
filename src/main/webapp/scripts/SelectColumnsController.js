var SelectColumnsModule = angular.module('SelectColumsApp', ['clothoRoot', 'ui.bootstrap.tpls', 'ui.bootstrap.modal']);

SelectColumsModule.controller('SelectColumsController',function($scope, Clotho, $modal){
    $scope.CreateSelectedColumns = function(){
       console.log($scope.serialNumber+" :: "+$scope.productName+" :: "+$scope.productId
               +" :: "+$scope.productUrl+" :: "+$scope.productDescription+" :: "+$scope.quantity
               +" :: "+$scope.companyName+" :: "+$scope.companyId+" :: "+$scope.companyUrl
               +" :: "+$scope.companyDescription+" :: "+$scope.companyContact+" :: "+$scope.companyPhone
               +" :: "+$scope.unitPrice+" :: "+$scope.totalPrice);
        $.ajax({
            url: "SelectColums",
            type: "POST",
            async: false,
            data: {
                "serialNumber" : $scope.serialNumber,
                "productName" : $scope.productName,
                "productId" : $scope.productId,
                "productUrl" : $scope.productUrl,
                "productDescription" : $scope.productDescription,
                "quantity" : $scope.quantity,
                "companyName" : $scope.companyName,
                "companyId" : $scope.companyId,
                "companyUrl" : $scope.companyUrl,
                "companyDescription" : $scope.companyDescription,
                "companyContact" : $scope.companyContact,
                "companyPhone" : $scope.companyPhone,
                "unitPrice" : $scope.unitPrice,
                "totalPrice" : $scope.totalPrice
            },
            success: function (response) {
                alert(response);  
            },
            error: function () {
                alert("ERROR!!");
            }

        });
    };   
})


