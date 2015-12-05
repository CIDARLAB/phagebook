angular.module('projectsApp',['clothoRoot']).controller('projectsController',
    function($scope, Clotho){
        $scope.personId = sessionStorage.getItem("uniqueid");


    });


angular.module('tabsApp',[]).controller('tabsController',['$scope',function($scope){
    $scope.active = 1;
    $scope.selectTab = function(value){
        $scope.active = value;
    }

    $scope.isActive = function(value){
        if($scope.active==value){
            return true;
        }
        else{
            return false;
        }
    }
}]);

//}
//    $scope.tabs = [{title: 'About',
//    url:'about.tpl.html'},
//        {title:'Newsfeed',
//        url:'newsfeed.tpl.html'},
//        {title:'Notebooks',
//        url:'notebooks.tpl.html'}];
//    $scope.currentTab= 'about.tpl.html';
//    $scope.onClickTab = function(tab){
//        $scope.currentTab = tab.url;
//    };
//    $scope.isActiveTab = function(tabUrl){
//        return tabUrl == $scope.currentTab;
//    }
//}]);

//$(document).ready(function() {
//    var testButton = Document.getElementByID("testButton");
//    console.log(testButton);
//    console.log("loads@222");
//    var testObject = {
//        "name": "name_input",
//        "labs": ["CIDAR","HYNESS"],
//        "description": "project description"
//    };
//
//    $("testButton").click(function(){
//       console.log("button clicked!!!!");
//     $.post("processProject",testObject,
//        function(data, status)
//        {
//         console.log("Data: " + data + "\nStatus: " + status);
//        });
//    });
//});
