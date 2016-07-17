function orderingSettingsController($scope){

    $scope.uploadCompanies = function(){

        var file = document.getElementById("inputCompany").files[0];

        var results = Papa.parse(file, {
                //header: true,
                complete: function(companyJSON) {
                    //console.log(JSON.stringify(companyJSON));
                    $.ajax({
                        url: "inputCompany",
                        type: "POST",
                        async: false,
                        data: {
                            "list" : JSON.stringify(companyJSON)
                        },
                        success: function (response) {
                            alert(response);
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
                //console.log(JSON.stringify(productJSON));
                $.ajax({
                    url: "inputProduct",
                    type: "POST",
                    data: {
                            "list" : JSON.stringify(productJSON)
                        },
                    success: function (response) {
                        alert(response);
                    },
                    error: function () {
                        alert("ERROR!!");
                    }
                });
            }
        });
    };


}