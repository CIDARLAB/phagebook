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
    // every time the keypress event gets triggered, we send an 
    // ajax call to the server with the values in the fields
    $("#inputLeadFirstName3").keypress(keyPressHandler);
    $("#inputLeadLastName3").keypress(keyPressHandler);

    $("#inputMemberFirstName3").keypress(keyPressHandler);
    $("#inputMemberLastName3").keypress(keyPressHandler);
    var firstName = "";
    var lastName = "";
    
    var doAjax = function(id) {
        // pass in the id of the form value being used
        //determine whether the input is last or first name

        // get parents class and get child first or last name

        if(id.indexOf("First") > -1){
          console.log("the input is the first name");
          firstName = $("#"+id).val();
          // check the class of the present element
          if($("#" + id).hasClass("leadForm")){
            // if it is lead, also grab the data from 
            // the last name form input
            lastName = $("#inputLeadLastName3").val();
            console.log(lastName);
          }else if($("#" + id).hasClass("memberForm")){
            lastName = $("#inputMemberLastName3").val();
          }
        }   

        if(id.indexOf("Last") > -1){
          console.log("the input is the last name");
          lastName = $("#"+id).val();

          if($("#" + id).hasClass("leadForm")){
            // if it is lead, also grab the data from 
            // the last name form input
            firstName = $("#inputLeadFirstName3").val();
            console.log(lastName);
          }else if($("#" + id).hasClass("memberForm")){
            firstName = $("#inputMemberFirstName3").val();
            console.log(lastName);
          }
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
                    // if(res.data)
                    console.log(res[0]);
                    console.log(res.data);
                    console.log(res.clothoId);

                },
                error: function(res) {
                    console.log("unable to find anything");
                }
            });
        }
    };
});