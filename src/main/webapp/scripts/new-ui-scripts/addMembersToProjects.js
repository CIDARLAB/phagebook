/*
Author: Anna Goncharova
CIDAR Lab, Boston University
Query Phagebook backend for people to be added to the project. 
Lead and members forms
*/


function loadSelectElementOptions() {
    var min = 12,
        max = 100,
        select = document.getElementById('lab-name');

    for (var i = min; i <= max; i++) {

        var opt = document.createElement('option');
        opt.value = i;
        opt.innerHTML = i;
        opt.type = "checkbox";
        select.appendChild(opt);
    }
}

var removeOptions = function(selectbox) {
    var i;
    for (i = selectbox.options.length; i >= 0; i--) {
        if(! ($( selectbox[i] ).val() == 0 )){
            selectbox.remove(i);  
        };
             
    }
}

$(document).ready(function() {

    var timerVal;
    var firstName = "";
    var lastName = "";

    var keyPressHandler = function() {
        // console.log("IN ADD MEMBERS TO PROJECTS");
        clearTimeout(timerVal); // stops previous attempt
        timerVal = setTimeout(doAjax(this.id), 250); // send continuoius ajax requests using doAjax function
    }

    // /*
    // input: ontype events in lead search or member search forms
    // output: array of person names and attached institutions
    // */
    var doAjax = function(id) {
        // console.log(id);

        var fullName = $("#" + id).val();
        // console.log(fullName);
        firstName = fullName;
        if (fullName.indexOf(' ') >= 0) {
            fullNameArr = fullName.split(" ");
            firstName = fullNameArr[0];
            lastName = fullNameArr[1];
        }

        var data = {
            "firstName": firstName,
            "lastName": lastName
        }

        var isValid = 0;
        if (firstName !== '' || lastName !== '') {
            isValid = 1;
        }

        if (isValid) {
            // console.log("about to send ajax req");
            $.ajax({
                url: "../findMemberForNewProject",
                type: "GET",
                async: false,
                data: data,
                success: function(response) {

                    if (response.length > 0) {
                        var select = document.getElementById(id + "_Results");
                        removeOptions(select);

                        for (var i = 0; i < response.length; i++) {
                            // loop through the response to create the array of people
                            var opt = document.createElement('option');
                            opt.value = response[i].clothoId;
                            opt.innerHTML = response[i].fullname + " - " + response[i].institution;
                            select.appendChild(opt);
                        }
                    }
                },
                error: function(res) {
                    // console.log("unable to find anything");
                }
            });
        }
    }

    // add handlers for keypresses in inputs for member and lead forms
    $("#inputLeadName").keypress(keyPressHandler);
    $("#inputMemberName").keypress(keyPressHandler);
});