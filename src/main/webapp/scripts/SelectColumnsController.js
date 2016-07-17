var SelectColumnsModule = angular.module('SelectColumnsApp', ['clothoRoot', 'ui.bootstrap.tpls', 'ui.bootstrap.modal']);

SelectColumnsModule.controller('SelectColumnsController',function($scope, Clotho, $modal){
    $scope.CreateSelectedColumns = function(){
        //console.log($scope.serialNumber+" :: "+$scope.productName+" :: "+$scope.productId
               +" :: "+$scope.productUrl+" :: "+$scope.productDescription+" :: "+$scope.quantity
               +" :: "+$scope.companyName+" :: "+$scope.companyId+" :: "+$scope.companyUrl
               +" :: "+$scope.companyDescription+" :: "+$scope.companyContact+" :: "+$scope.companyPhone
               +" :: "+$scope.unitPrice+" :: "+$scope.totalPrice);
       
        $.ajax({
            url: "SelectColumns",
            type: "POST",
            async: false,
            data: {
                "serialNumber" : $scope.serialNumber,
                "productName" : $scope.productName,
                "productUrl" : $scope.productUrl,
                "productDescription" : $scope.productDescription,
                "quantity" : $scope.quantity,
                "companyName" : $scope.companyName,
                "companyUrl" : $scope.companyUrl,
                "companyDescription" : $scope.companyDescription,
                "companyContact" : $scope.companyContact,
                "companyPhone" : $scope.companyPhone,
                "unitPrice" : $scope.unitPrice,
                "totalPrice" : $scope.totalPrice,
                "orderId" : getParameterByName("orderId")
            },
            success: function (response) {
                alert(response); 
                window.location.href = "file:///" + response;
            },
            error: function () {
                alert("ERROR!!" +getParameterByName("orderId"));
            }

        });
    };   
})


