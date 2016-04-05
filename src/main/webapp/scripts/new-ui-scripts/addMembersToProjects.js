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

    // first name, last name separation is a potential legacy and could be 
    // reduced to just name over time
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

        // output is responsible for either attaching the result 
        // of the querying ajax call to member or lead selection boxes
        var output ="";

        if(id.indexOf("First") > -1){ // for grabbing input from the first name box

          console.log("the input is the first name");
          firstName = $("#"+id).val();
          // check the class of the present element
          if($("#" + id).hasClass("leadForm")){  // check whether input is lead 
            output = "lead";
            // if it is lead, also grab the data from 
            // the last name form input
            lastName = $("#inputLeadLastName3").val();
            console.log(lastName);
          }else if($("#" + id).hasClass("memberForm")){  // check whether input is member 
             output = "member";
            lastName = $("#inputMemberLastName3").val();
          }
        }   

        if(id.indexOf("Last") > -1){  // for grabbing input from the last name box
          console.log("the input is the last name");
          lastName = $("#"+id).val();

          if($("#" + id).hasClass("leadForm")){ // check whether input is lead 
             output = "lead";
            // if it is lead, also grab the data from 
            // the last name form input
            firstName = $("#inputLeadFirstName3").val();
            console.log(lastName);
          }else if($("#" + id).hasClass("memberForm")){  // check whether input is member 
             output = "member";
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
                success: function(response) {
                    // console.log(response)
                    // console.log(output); 
                    // console.log("Response is: ");
                    // console.log(response);
                    // console.log("****");
                    if(response.length>0){
                        var select = document.getElementById(output + "_Results");
                        // console.log(select);
                        removeOptions(select);
                        // console.log("Response is: ");
                        // console.log(response);
                        // console.log("****");
                        for (var i = 0; i < response.length; i++){
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
    };
});

function loadSelectElementOptions(){
    var min = 12,
    max = 100,
    select = document.getElementById('lab-name');

    for (var i = min; i<=max; i++){
        var opt = document.createElement('option');
        opt.value = i;
        opt.innerHTML = i;
        select.appendChild(opt);
    }
}

var removeOptions = function(selectbox)
{
    var i;
    console.log("IN REMOVE OPTIONS");
    console.log(selectbox);
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}