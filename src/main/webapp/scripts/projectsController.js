<<<<<<< HEAD
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
=======
'use strict';



var projectsApp = angular.module('projectsApp',['clothoRoot']);

projectsApp.controller('projectsController',['$scope', '$http', function($scope, $http ,Clotho){

  $scope.greeting = 'Hello!';
  $scope.personId = sessionStorage.getItem("uniqueid");

  // form data object -- here are the results from the form are stored
  $scope.formData = {};
  
  $scope.saveData = function(){

    var createdDate = new Date().toJSON().slice(0,10);
    
    $scope.formData.date = createdDate;

    $scope.nameRequired = '';
    //$scope.emailRequired = '';
    $scope.labsRequired = '';
    $scope.leadRequired = '';
    $scope.projectBudgetRequired = '';
    $scope.projectGrantRequired = '';
    $scope.descriptionRequired = '';
    $scope.passwordRequired = '';

    var validateForm = function(){
      var count = 0; // has to meet a number of conditions before sending the ajax call

      if(!$scope.formData.name ){
        $scope.nameRequired = 'Please provide a valid title for your new project.';
      
      }else{
        $scope.nameRequired = '';
        count++;
      }


      if(!$scope.formData.description){
        $scope.descriptionRequired = 'Please provide a valid description.';
      }else{
        $scope.formData.description = '';
        count++;
      }

      if(count >=2){
        return true;
      }else{
        return false;
      }

    }

    var submit = validateForm();
    console.log(submit);

  // !!!! create a check that pr budget is an int !!!!!!


  var dataSubmit =  {
    name: $scope.formData.name,
    lead:  $scope.formData.lead,
    labs:  $scope.formData.labs,
    projectBudget: $scope.formData.projectBudget,
    grant: $scope.formData.grant,
    description: $scope.formData.description,
    date: $scope.formData.date
   };
   //dataSubmit = JSON.stringify(dataSubmit);
   console.log(dataSubmit);
   if(submit){
     $.ajax({
      url: "processProject",
      type: "POST",
      dataType: "json",
      async: false,
      data: dataSubmit,
      success: function (response) {
        console.log(dataSubmit); 
        console.log(response);
        console.log("response!!!");
      },
      error: function () {
        alert("ERROR!!");
      }
    }); 
   }
  
  };

}]);

>>>>>>> 9628bc6baee656df83c5c3eb0787e6f32f60ed50
