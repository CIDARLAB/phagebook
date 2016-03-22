function newProjectsCtrl($scope, $http){
console.log("loaded");

  $scope.greeting = 'Hello!';
  $scope.personId = getCookie("clothoId");//retrieves the user id from session storage

  // form data object -- here are the results from the form are stored
  $scope.formData = {};
  
  $scope.saveData = function(){

    var createdDate = new Date().toJSON().slice(0,10);
    
    $scope.formData.date = createdDate;

    $scope.nameRequired = '';
    //$scope.emailRequired = '';
    $scope.labsRequired = '';
    $scope.leadRequired = '';
    $scope.leadFirstNameRequired = '';
    $scope.leadLastNameRequired = '';
    $scope.projectBudgetRequired = '';
    $scope.projectGrantRequired = '';
    $scope.descriptionRequired = '';
    $scope.passwordRequired = '';

    /** 
     *
     * This function validates the form.
     * The input has to meet a number of conditions before
     * approving the AJAX call to the server. 
     * Output is a Boolean. 
     */
    var validateForm = function(){

      var count = 0; 

      // Condition 1: a new project has to have a name.
      if(!$scope.formData.name ){
        console.log("Condition 1 is not met.");
        $scope.nameRequired = 'Please provide a valid title for your new project.';
      }else{
        count++;
      }

      // Condition 2: a new project has to have a description.
      if(!$scope.formData.description){
        console.log("Condition 2 is not met.");
        //console.log($scope.formData.description);
        $scope.descriptionRequired = 'Please provide a valid description.';
      }else{
        console.log($scope.formData.description);
        count++;
      }
      console.log("$scope.formData.lead.firstName");

      console.log($scope.formData.lead.firstName);

      // Condition 3: lead person has to have a first AND a last name.
      if(!$scope.formData.lead.firstName && !$scope.formData.lead.lastName ){
        console.log("Condition 3 is not met.");
        if(!$scope.formData.lead.firstName){
          console.log("Lead does not have a first name.");
          $scope.leadFirstNameRequired = 'Please provide first name.';
        }
        if(!$scope.formData.lead.lastName){
          console.log("Lead does not have a last name.");          
          $scope.leadLastNameRequired = 'Please provide last name.';
        }
      }else{
        console.log($scope.formData.lead.firstName);
        console.log($scope.formData.lead.lastName);
        count++;
      }

      if(count >=3){
        console.log("All conditions are met.");
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
    leadFirstName:  $scope.formData.lead.firstName,
    leadLastName:  $scope.formData.lead.lastName,
    labs:  $scope.formData.labs,
    projectBudget: $scope.formData.projectBudget,
    grant: $scope.formData.grant,
    description: $scope.formData.description,
    date: $scope.formData.date,
    emailId: $scope.personId
   };
   //dataSubmit = JSON.stringify(dataSubmit);
   console.log(dataSubmit);
   if(submit){
     $.ajax({
      url: "/processProject",
      type: "POST",
      dataType: "json",
      async: false,
      data: dataSubmit,
      success: function (response) {
        console.log(dataSubmit); 
        console.log(response);
        console.log("response!!!");
        setCookie("projectId", response.projectId, 10);
        console.log(document.cookie);
        //location.assign("./html/displayProjects.html");

      },
      error: function (err) {
        console.log("ERROR!!");
        console.log(err);

      }
    }); 
   }
  };
};
