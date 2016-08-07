/*
Author: Anna Goncharova
CIDAR Lab, Boston University
This file is responsible for processing and checking the data from new projects form.
To look at search and querying functions go to addMembersToProjects.js file.
*/

/*
 ** HELPERS
 */
function isInArray(value, array) {
    console.log(array.indexOf(value) > -1);
    return array.indexOf(value) > -1;
}

/*
 ** HELPERS
 */

function newProjectsCtrl($scope, $http) {
    $scope.membersIds;
    $scope.personId = getCookie("clothoId"); //retrieves the user id from session storage

    // form data object -- here are the results from the form are stored
    $scope.formData = {};

    /*
     ** Loops through the list of members and 
     ** only saves the options that have been selected.
     */
    $scope.getMembers = function() {
        var listBox = document.getElementById('membersList');
        var listItem = listBox.getElementsByTagName("li");
        var membersArr = [];

        for (var i = 0; i < listItem.length; i++) {
            if ($(listItem[i]).find('input').is(':checked')) {
                var option = $(listItem[i]).find('input');
                console.log(option.text());
                var obj = {
                    'memberName': $(listItem[i]).text(),
                    'memberId': option.val()
                };
                membersArr.push(obj);
            }
        }
        console.log(membersArr);
    }

    /*
     ** This function gets called when the user clicks on the submit 
     ** project button.
     */
    $scope.saveData = function() {

        var createdDate = new Date().toJSON().slice(0, 10);

        $scope.formData.date = createdDate;

        $scope.nameRequired = '';
        $scope.labsRequired = '';
        $scope.leadRequired = '';
        $scope.leadNameRequired = '';
        $scope.projectBudgetRequired = '';
        $scope.projectGrantRequired = '';
        $scope.descriptionRequired = '';
        $scope.passwordRequired = '';
        $scope.membersNameRequired = '';

        /** 
         *
         * This function validates the form.
         * The input has to meet a number of conditions before
         * approving the AJAX call to the server. 
         * Output is a boolean. 
         */
        var validateForm = function() {

            var count = 0;

            // Condition 1: a new project has to have a name.
            if (!$scope.formData.name) {
                console.log("No name.");
                $scope.nameRequired = 'Please provide a valid title for your new project.';
            } else {
                count++;
            }

            // Condition 2: a new project has to have a description.
            if (!$scope.formData.description) {
                $scope.descriptionRequired = 'Please provide a valid description.';
            } else {
                // console.log($scope.formData.description);
                count++;
            }
            var checkLead = validateLead();
            console.log(checkLead);
            if (validateLead != null) {
                console.log("lead not null");
                count += 1;
            } else {
                $scope.leadNameRequired = 'Please select an option.';
            }

            var checkMembers = validateMembers();
            var membersList = processMemberSelections();
            console.log(membersList);
            if (checkMembers != null) {
                console.log("members not null");
                if(membersList !=undefined && membersList.length != 0){
                    count += 1;
                }else{
                    $scope.membersNameRequired = 'Please select an option.';
                }
            } else {
                $scope.membersNameRequired = 'Please select an option.';
            }

            if (count >= 4) {
                return true;
            } else {
                return false;
            }

        }


        /*
         ** Validates the lead search bar and select box.
         ** If the user does not select lead, prompt to 
         ** search and select lead. 
         ** If the user does not find lead, prompt to
         ** invite lead to Phagebook.
         ** TODO: add support for inviting po
         */
        var validateLead = function() {

            var leadId = null;

            if ($("#inputLeadName_Results option:selected").val() != "") {
                leadId = $("#inputLeadName_Results option:selected").val();
                // if (leadId == 0) {
                //     // what should be return value?
                //     alert("No lead is in there!");
                // }
            } else {
                $scope.leadNameRequired = 'Please select an option.';
            }
            console.log(leadId);
            return leadId;
        }
        validateLead();

        /*
         ** Validates the members options and select box.
         */
        var validateMembers = function() {

            var memberVal = null;

            if ($("#inputMemberName_Results option:selected").val() != "") {
                if ($("#inputMemberName_Results option:selected").val().localeCompare("default") == 0) {
                    $scope.membersNameRequired = 'Please select an option.';
                } else {
                    memberVal = $("#inputMemberName_Results option:selected").val();
                }
            } else {
                $scope.membersNameRequired = 'Please select an option.';
            }
            console.log(memberVal);
            return memberVal;
        }

        /*
         ** Loops over the list of input selections and and returns an array of 
         ** selected members' ids. If none selected displays a reminder to make
         ** selections.
         */
        var processMemberSelections = function() {
            var selectedMembers = []
            var listItems = $("#membersList li");
            listItems.each(function(idx, li) {
                var input = $(li).children('input').eq(0);

                if (input.is(':checked')) {
                    var memberId = input.val();
                    selectedMembers.push(memberId);
                }
            });
            if (selectedMembers.length == 0) {
                $scope.membersNameRequired = 'Please select an option.';
            } else {
                return selectedMembers.toString();
            }
        }

        var submit = validateForm();
        if (submit) {

            // The form has been validated -- ok to get values.  
            var dataSubmit = {
                name: $scope.formData.name,

                leadID: $("#inputLeadName_Results option:selected").val(),

                members: processMemberSelections(),
                projectBudget: $scope.formData.projectBudget,
                grant: $scope.formData.grant,
                description: $scope.formData.description,
                date: $scope.formData.date,
                emailID: $scope.personId
            };
            console.log(dataSubmit);
            // dataSubmit = JSON.stringify(dataSubmit);
            // console.log(dataSubmit);

            $.ajax({
                url: "../processProject",
                type: "POST",
                dataType: "json",
                async: true,
                data: dataSubmit,
                success: function(response) {

                    console.log(dataSubmit);
                    // console.log(response);
                    // console.log("response!!!");
                    setCookie("projectId", response.projectId, 10);
                    // console.log(document.cookie);
                    alert("A new project has been created!");
                    //location.assign("./html/displayProjects.html");

                },
                error: function(err) {
                    console.log(dataSubmit);
                    console.log("Error!");
                    console.log(err);

                }
            });
        }
    };

    var nameArr = [];
    var idArr = [];
    /*
    allows to select a member from the search result bar and add it into an array
    */
    $("#inputMemberName_Results").on('click', function() {
        var memberNameSelected = $("#inputMemberName_Results option:selected").text();
        var memberIdSelected = $("#inputMemberName_Results option:selected").val();
        if (!(memberIdSelected == 'default')) {
            if (nameArr.length + idArr.length == 0) {
                $(".dropdown dd ul").slideToggle('fast');
            }
            if (!isInArray(memberNameSelected, nameArr) && !isInArray(memberIdSelected, idArr)) {
                idArr.push(memberIdSelected);
                nameArr.push(memberNameSelected);
                var html = '<li class="members_li"> <input type="checkbox" value="' + memberIdSelected + '" />' + memberNameSelected + '</li>';
                $("#membersList").append(html);
            }
        }

    });

    var updateSpan = function(arr) {
        $('.multiSel').html('<span title="' + arr + '">' + arr + '</span>');
    }

};