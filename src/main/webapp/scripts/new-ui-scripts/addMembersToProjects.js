/*
Author: Anna Goncharova
CIDAR Lab, Boston University
This file is responsible for looking up Phagebook's members to be added to the project.
*/

$(document).ready(function() {




    var keyPressHandler = function() {
        console.log("IN ADD MEMBERS TO PROJECTS");
        clearTimeout(timerVal); // stops previous attempt.
        timerVal = setTimeout(doAjax(this.id), 500); //after a second of no input flip the flag.
    }

    var timerVal;

    // add handlers for keypresses on first names of people
    // every time the keypress event gets triggered, we send an 
    // ajax call to the server with the values in the fields

    $("#inputLeadName").keypress(keyPressHandler);

    var firstName = "";
    var lastName = "";

    var doAjax = function(id) {

        var output = "lead";

        fullName = $("#inputLeadName").val();
        console.log(fullName);
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
        console.log(data);

        var isValid = 0;
        if (firstName !== '' || lastName !== '') {
            isValid = 1;
        }

        if (isValid) {
            console.log("about to send ajax req");
            $.ajax({
                //do this for projects...
                url: "../findMemberForNewProject",
                type: "GET",
                async: false,
                data: data,
                success: function(response) {

                    if (response.length > 0) {
                        var select = document.getElementById(output + "_Results");
                        removeOptions(select);

                        for (var i = 0; i < response.length; i++) {
                            var opt = document.createElement('option');
                            console.log(response[i].fullname);
                            opt.value = response[i].clothoId;
                            opt.innerHTML = response[i].fullname;
                            console.log(opt);
                            select.appendChild(opt);
                        }
                    }
                },
                error: function(res) {
                    console.log("unable to find anything");
                }
            });
        }
    }


});

function loadSelectElementOptions() {
    var min = 12,
        max = 100,
        select = document.getElementById('lab-name');

    for (var i = min; i <= max; i++) {
        var opt = document.createElement('option');
        opt.value = i;
        opt.innerHTML = i;
        select.appendChild(opt);
    }
}


var removeOptions = function(selectbox) {
    var i;
    console.log("IN REMOVE OPTIONS");
    console.log(selectbox);
    for (i = selectbox.options.length; i >= 0; i--) {
        console.log(selectbox);
        console.log("about to remove a box");
        console.log(selectbox.i);
        selectbox.remove(i);
    }
}