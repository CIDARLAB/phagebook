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
    $("#inputMemberFirstName3").keypress(keyPressHandler);
    

    var doAjax = function(id) {

        var name = $("#"+id).val();
        console.log("name is");
        console.log(name);

        var isValid = 0;
        if (name !== '') {
            isValid = 1;
        }

        if (isValid) {
          console.log("about to send ajax req");
            $.ajax({
                //do this for projects...
                url: "../findMemberForNewProject",
                type: "GET",
                async: false,
                data: {
                    "name": "test"
                },
                success: function(res) {
                    // var select = document.getElementById('companyResults');
                    // removeOptions(select);
                    // var lengthOfResponse = response.length;
                    // for (var i = 0; i < lengthOfResponse; i++) {
                    //     var opt = document.createElement('option');
                    //     opt.value = response[i].id;

                    //     opt.innerHTML = response[i].name;
                    //     select.appendChild(opt);
                    // }
                    console.log(res);

                },
                error: function(res) {
                    alert("unable to find anything");
                }
            });
        }
    };
});