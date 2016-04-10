/*
Author: Anna Goncharova
CIDAR Lab, Boston University
This file is responsible for processing and checking the data from new projects form.
*/

function newProjectsCtrl($scope, $http) {

    $.ajax({
        url: "../loadPhagebookInstitutions",
        type: "GET",
        async: false,
        data: {},
        success: function(response) {
            var responseArray = response.institutions; //array of JSONObjects with labs attached 
            console.log(responseArray);
            var selectInstitution = document.getElementById('institution');


            sessionStorage.setItem("index-institutions", JSON.stringify(responseArray)); // stores in sess stor
            // removeOptions1(selectInstitution);

            var opt = document.createElement('option');
            opt.value = "";

            opt.innerHTML = "Institution...";
            selectInstitution.appendChild(opt);

            var numberOfInstitutions = responseArray.length;
            for (var i = 0; i < numberOfInstitutions; i++) {


                var opt = document.createElement('option');
                opt.value = responseArray[i].institutionId;

                opt.innerHTML = responseArray[i].institutionName;
                selectInstitution.appendChild(opt);


            }

        },
        error: function(response) {
            alert("No Institutions To Load");
        }
    });

    $('#institution').change(function() {
        //this is innefficient... make a servlet to make a call on change instead actually... its easier
        var selectedInstitution = $('#institution option').filter(':selected').text();

        var responseArray = JSON.parse(sessionStorage.getItem("index-institutions"));


        var selectLabs = document.getElementById('lab-name');


        var numberOfInstitutions = responseArray.length;


        for (var i = 0; i < numberOfInstitutions; i++) {

            if (responseArray[i].institutionName === selectedInstitution) {
                removeOptions(selectLabs);
                var labsArray = responseArray[i].labs;
                var labsLength = labsArray.length;
                for (var j = 0; j < labsLength; j++) {
                    var opt2 = document.createElement('option');
                    opt2.value = labsArray[j].labId;
                    opt2.innerHTML = labsArray[j].labName;
                    selectLabs.appendChild(opt2);
                }
            } else if (selectedInstitution === "Institution...") {
                removeOptions(selectLabs);
                var opt2 = document.createElement('option');
                opt2.value = "";
                opt2.innerHTML = "Lab Name...";
                selectLabs.appendChild(opt2);
                return;
            }


        }
    });


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
            } else {
                $scope.leadNameRequired = "Format invalid. Please enter as 'James Smith'";
            }
            //Condition 4: valid input for members' names: 2 words
            if (membersCheck()) {
                console.log("adding 1!")
                count++;
            } else {
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
                if (leadFullName != undefined) {
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
            } else {
                $scope.membersRequired = "Please enters members' names.";
                return false;
            }

        }

        var submit = validateForm();
        console.log(submit);

        // !!!! create a check that pr budget is an int !!!!!!
        console.log(membersArray);
        // f it
        if($("#lab_selectDiv option:selected").text() == "Lab Name..." || $("#lab_selectDiv option:selected").val() == "Lab Name..."){
            $("#lab_selectDiv option:selected").text() = "";
             $("#lab_selectDiv option:selected").val() = "";
        }
        var dataSubmit = {
            name: $scope.formData.name,

            leadFirstName: leadName[0],
            leadLastName: leadName[1],

            // get id and name of lead from dropdown
            leadName: $("#lead_selectDiv option:selected").text(),
            leadID: $("#lead_selectDiv option:selected").val(),

            members: $scope.formData.members,

            lab: $("#lab_selectDiv option:selected").text(),
            labID: $("#lab_selectDiv option:selected").val(),

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
                    alert("A new project has been created!");
                    //location.assign("./html/displayProjects.html");

                },
                error: function(err) {

                    console.log("Error!");
                    console.log(err);

                }
            });
        }
    };

};