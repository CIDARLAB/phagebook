/*
Author: Anna Goncharova
CIDAR Lab, Boston University
This file is responsible for looking up Phagebook's members to be added to the project.
*/

$(document).ready(function() {

    var keyPressHandler = function() {
        clearTimeout(timerVal); // stops previous attempt.
        timerVal = setTimeout(doAjax(this.id), 500); //after a second of no input flip the flag.
    }

    var timerVal;
    // add handlers for keypresses on first names of people
    $("#inputLeadFirstName3").keypress(keyPressHandler);
    $("#inputLeadLastName3").keypress(keyPressHandler);

    $("#inputMemberFirstName3").keypress(keyPressHandler);
    $("#inputMemberLastName3").keypress(keyPressHandler);


    var doAjax = function(id) {

        console.log("name is");
        console.log(name);
        var firstName = "";
        var lastName = "";
        //determine whether the input is last or first name
        if(id.indexOf("First") > -1){
          console.log("the input is the first name");
          firstName = $("#"+id).val();

        }else if(id.indexOf("Last") > -1){
          console.log("the input is the last name");
          lastName = $("#"+id).val();
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
                success: function(res) {
                    
                    console.log(res);

                },
                error: function(res) {
                    consoel.log("unable to find anything");
                }
            });
        }
    };
});