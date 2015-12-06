/* global $location */

var addProductModule = angular.module('addProductApp', ['clothoRoot', 'ui.bootstrap.tpls', 'ui.bootstrap.modal']);

addProductModule.controller('addProductController',function($scope, Clotho, $modal){
    $scope.addCompany = function(){
        //console.log($scope.CompanyName);
        $.ajax({
            url: "addProducts",
            type: "POST",
            async: false,
            data: {
                "CompanyName":$scope.CompanyName
            },
            success: function (response) {
               $('#compName').html(response);
            },
            error: function () {
                alert("ERROR!!");
            }
        });
    };    
})

