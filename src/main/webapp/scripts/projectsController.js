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
    var submit = true;
    if(!$scope.formData.name){
      $scope.nameRequired = 'Please provide a title for your new project.';
      submit = false;
    }
    //submit = true;
    /*if(!$scope.formData.labs){
      $scope.labsRequired = 'Please input the associated organizations.';
      submit = false;
    }*/
    submit = true;
    if(!$scope.formData.description){
      $scope.descriptionRequired = 'Please provide a description.'
      submit = false;
    }
  
  //console.log($scope.formData);
  
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





// $(document).ready(function() {
// var testButton = Document.getElementByID("testButton");
// console.log(testButton);
// console.log("loads@222");
// var testObject = {
// "name": "name_input",
// "labs": ["CIDAR","HYNESS"],
// "description": "project description"
// };

// $("testButton").click(function(){
// console.log("button clicked!!!!");
// $.post("processProject",testObject,
// function(data, status)
// {
// console.log("Data: " + data + "\nStatus: " + status);
// });
// });
// });
