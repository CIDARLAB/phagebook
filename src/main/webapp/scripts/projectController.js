
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
        count++;
      }
      console.log("1");
      console.log($scope.formData.description);
      if(!$scope.formData.description){
        console.log("2");
        console.log($scope.formData.description);
        $scope.descriptionRequired = 'Please provide a valid description.';
      }else{
        console.log("3");
        console.log($scope.formData.description);
        count++;
      }

      if(count >=2){
        return true;
      }else{
        return false;
      }

    }

    console.log($scope.formData.description);

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
