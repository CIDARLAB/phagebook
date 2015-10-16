var orderingModule = angular.module('orderingApp',['clothoRoot','ui.bootstrap.tpls','ui.bootstrap.modal']);


orderingModule.controller('orderingController',function($scope,Clotho,$modal){

    $scope.uploadCompanies = function(){

        var file = document.getElementById("inputCompany").files[0];

        var results = Papa.parse(file, {
                //header: true,
                complete: function(companyJSON) {
                    console.log(JSON.stringify(companyJSON));
                    $.ajax({
                        url: "inputCompany",
                        type: "POST",
                        data: companyJSON,
                        success: function (response) {
                            result = JSON.parse(response);
                        },
                        error: function () {
                            alert("ERROR!!");
                        }
                    });
                }
            });
    };

    $scope.uploadProducts = function(){

        var file = document.getElementById("inputProduct").files[0];

        var results = Papa.parse(file, {
            //header: true,
            complete: function(productJSON) {
                $.ajax({
                    url: "inputProduct",
                    type: "POST",
                    data: productJSON,
                    success: function (response) {
                        result = JSON.parse(response);
                    },
                    error: function () {
                        alert("ERROR!!");
                    }
                });
            }
        });
    };


})