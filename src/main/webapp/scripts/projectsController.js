angular.module('projectsApp',['clothoRoot']).controller('projectsController',
    function($scope, Clotho){
        $scope.personId = sessionStorage.getItem("uniqueid");


    });

$(document).ready(function() {
    var testButton = Document.getElementByID("testButton");
    console.log(testButton);
    console.log("loads@222");
    var testObject = {
        "name": "name_input",
        "labs": ["CIDAR","HYNESS"],
        "description": "project description"
    };

    $("testButton").click(function(){
       console.log("button clicked!!!!");
     $.post("processProject",testObject,
        function(data, status)
        {
         console.log("Data: " + data + "\nStatus: " + status);
        });
    });
});
