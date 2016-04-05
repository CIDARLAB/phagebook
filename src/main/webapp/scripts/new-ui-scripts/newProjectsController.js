/*
Author: Anna Goncharova
CIDAR Lab, Boston University
This file is responsible for processing and checking the data from new projects form.
*/

function newProjectsCtrl($scope, $http) {
    console.log("loaded");

    $scope.personId = getCookie("clothoId"); //retrieves the user id from session storage

    // form data object -- here are the results from the form are stored
    $scope.formData = {};

    $scope.saveData = function() {

        var createdDate = new Date().toJSON().slice(0, 10);
        var leadName = [];
        var membersArray = [];


        $scope.formData.date = createdDate;

        $scope.nameRequired = '';
        $scope.labsRequired = '';
        $scope.leadRequired = '';
        $scope.leadNameRequired = '';
        $scope.projectBudgetRequired = '';
        $scope.projectGrantRequired = '';
        $scope.descriptionRequired = '';
        $scope.passwordRequired = '';
        $scope.membersRequired = '';

        /** 
         *
         * This function validates the form.
         * The input has to meet a number of conditions before
         * approving the AJAX call to the server. 
         * Output is a boolean. 
         */
        var validateForm = function() {

            var count = 0;
            // membersCheck();
            // Condition 1: a new project has to have a name.
            if (!$scope.formData.name) {
                console.log("No name.");
                $scope.nameRequired = 'Please provide a valid title for your new project.';
            } else {
                count++;
            }

            // Condition 2: a new project has to have a description.
            if (!$scope.formData.description) {
                console.log("No description.");
                //console.log($scope.formData.description);
                $scope.descriptionRequired = 'Please provide a valid description.';
            } else {
                console.log($scope.formData.description);
                count++;
            }
            // Condition 3: valid input for lead's name: 2 words
            if (leadNameCheck()) {
                count++;
            }else{
                $scope.descriptionRequired = "Format invalid. Please enter as 'James Smith'";
            }
            //Condition 4: valid input for members' names: 2 words
            if (membersCheck()) {
                console.log("adding 1!")
                count++;
            }else{
                console.log("Members names invalid.");
            }


            // check if lead doesn't have either first OR last name

            if (count >= 4) {
                console.log("All conditions are met.");
                return true;
            } else {
                return false;
            }

        }

        // come up with this workaround for the issue with populating a drop down menu
        // in case if the lead's name in the form doesn't match the lead's name in the drop down
        // it gets called in the validateForm function above,
        // if both conditions pass, then we assign the values in the json var to the 
        // 1st and 0eth value in the name leadName array
        var leadNameCheck = function() {

                var leadFullName = $scope.formData.leadName;
                if (leadFullName  != undefined) {
                    var splitName = leadFullName.split(" ");
                    var leadNameSelected = $("#lead_selectDiv option:selected").text();
                    console.log(splitName.length);
                    if (splitName.length < 2) {
                        console.log("string not long enough");
                        return false;
                    }
                    if (leadFullName != leadNameSelected) {
                        console.log("oopsies!");
                        $("#lead_selectDiv option:selected").val(0);
                    }
                    leadName = splitName;
                    return true;
                }

            }
            // splits by comma and makes sure the vals are separated by spaces
        var membersCheck = function() {

            var members = $scope.formData.members;
            console.log("members******");
            console.log(members);
            console.log("members******");
            var membersArr;
            if (members != undefined) {

                membersArr = members.split(", ");
                console.log(membersArr);
                for (var i = 0; i < membersArr.length; i++) {
                    console.log(membersArr[i]);
                    if (membersArr[i].split(" ").length != 2) {
                        $scope.membersRequired = "Please reenter the members' names in a valid format";
                        return false;
                    }
                }
                membersArray = membersArr;
                console.log("about to return membersArray");
                console.log(membersArray);
                return true;
            }else{
                $scope.membersRequired = "Please enters members' names.";
                return false;
            }

        }

        var submit = validateForm();
        console.log(submit);

        // !!!! create a check that pr budget is an int !!!!!!
        console.log(membersArray);
        var dataSubmit = {
            name: $scope.formData.name,

            leadFirstName: leadName[0],
            leadLastName: leadName[1],

            // get id and name of lead from dropdown
            leadName: $("#lead_selectDiv option:selected").text(),
            leadID: $("#lead_selectDiv option:selected").val(),

            members: $scope.formData.members,

            labs: $scope.formData.labs,
            projectBudget: $scope.formData.projectBudget,
            grant: $scope.formData.grant,
            description: $scope.formData.description,
            date: $scope.formData.date,
            emailID: $scope.personId
        };

        if (submit) {
            // checkData(dataSubmit);
            //dataSubmit = JSON.stringify(dataSubmit);
            console.log(dataSubmit);
            $.ajax({
                url: "/processProject",
                type: "POST",
                dataType: "json",
                async: false,
                data: dataSubmit,
                success: function(response) {
                    console.log(dataSubmit);
                    console.log(response);
                    console.log("response!!!");
                    setCookie("projectId", response.projectId, 10);
                    console.log(document.cookie);
                    //location.assign("./html/displayProjects.html");

                },
                error: function(err) {
                    console.log("ERROR!!");
                    console.log(err);

                }
            });
        }
    };
};