$(document).ready(function () {
    
    $('#institution').change( function () {

        //this is innefficient... make a servlet to make a call on change instead actually... its easier
        var selectedInstitution =  $('#institution option').filter(':selected').text();
        var responseArray = JSON.parse(sessionStorage.getItem("index-institutions"));
        var selectLabs = document.getElementById('lab-name');
        var numberOfInstitutions = responseArray.length;
        
        for (var i = 0; i < numberOfInstitutions; i++) {
           
            if (responseArray[i].institutionName === selectedInstitution ){
               removeOptions(selectLabs);
               var labsArray = responseArray[i].labs;
               var labsLength = labsArray.length;
               for (var j = 0 ; j < labsLength ; j++){
                   var opt2 = document.createElement('option');
                   opt2.value = labsArray[j].labId;
                   opt2.innerHTML = labsArray[j].labName;
                   selectLabs.appendChild(opt2);
               }
            } else if ( selectedInstitution === "Institution...") {
                removeOptions(selectLabs);
                var opt2 = document.createElement('option');
                   opt2.value = "";
                   opt2.innerHTML = "Lab Name...";
                   selectLabs.appendChild(opt2);
                return;
            }
        }
    });

    $.ajax({
        url: "../loadPhagebookInstitutions",
        type: "GET",
        async: false,
        data: {},
        success: function (response) {
            var responseArray = response.institutions; //array of JSONObjects with labs attached 

            var selectInstitution = document.getElementById('institution');

            sessionStorage.setItem("index-institutions", JSON.stringify(responseArray)); // stores in sess stor
            removeOptions(selectInstitution);
            
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
        error: function (response) {
            alert("No Institutions To Load");
        }
    });

    $('#createProfile').click(function () {

        var isValid = 1;
        var firstName = document.getElementById("inputFirstName").value;
        var lastName =  document.getElementById("inputLastName").value;
        var emailId =   document.getElementById("emailAddress").value;
        var password =  document.getElementById("password").value;
        
        if ((firstName === "") || (lastName === "") || (emailId === "") || (password === "")) {
            $("#fields-required-alert").fadeIn();
            isValid = 0;
        }

        var institution = document.getElementById("institution").value; // selected institution id
        var lab         = document.getElementById("lab-name").value;

        if (isValid && checkPasswordMatch()) {
            $.ajax({
                url: "../createPerson",
                type: "POST",
                async: false,
                data: {
                    //"id": loginResult.id,
                    "firstName": firstName,
                    "lastName": lastName,
                    "emailId": emailId,
                    "password": password,
                    "institution": institution,
                    "lab" : lab
                },
                success: function (response) {
                    var responseJSON = JSON.parse(response);
                    setCookie("emailId", responseJSON.emailId, 1);
                    setCookie("clothoId", responseJSON.clothoId, 1);
                    window.location.href = '../html/resendEmailVerification.html';
                },
                error: function (response) {
                    //THIS CAN BE DONE BETTER ONCE WE KNOW WHAT WE ARE DOING.
                    alert("\n" + response.statusText + "!\n" + response.responseText);
                    window.location.href = '../';
                }
            });
        }
    });

    $('#login').click(function () {
        var isValid = 1;
        var loginId = document.getElementById("loginEmailAddress").value;
        var password = document.getElementById("loginPassword").value;
        if ((loginId === "") || (password === "")) {
            isValid = 0;
        }

        if (isValid) {
            $.ajax({
                url: "../loginUser",
                type: "POST",
                async: false,
                data: {
                    "email": document.getElementById("loginEmailAddress").value,
                    "password": document.getElementById("loginPassword").value
                },
                success: function (response) {
                    //alert(response);
                    //alert("Got Some Response" + response);
                    setCookie("clothoId", response.clothoId, 1);
                    setCookie("emailId", response.emailId, 1);

                    if (response.activated === "false") {
                        window.location.href = '../html/resendEmailVerification.html';
                    }
                    else {
                        window.location.href = '../html/profile.html';
                    }
                },
                error: function (response) {
                    var responseText = JSON.parse(response.responseText);
                    $("#invalid-combo-alert").modal('show');
                }
            });
        }
    });


});

function checkPasswordMatch() {
    var password = $("#password").val();
    var confirmPassword = $("#reenterPassword").val();

    if (password !== confirmPassword) {
        $("#password-match-alert").fadeIn();
        return false;
    }
    else {
        $("#password-match-alert").fadeOut();
        console.log("Passwords match");
        return true;
    }
}

function isEmpty(el) {
    return !$.trim(el.html());
}

function removeOptions(selectbox) {
    var i;
    for(i=selectbox.options.length-1;i>=0;i--) {
        selectbox.remove(i);
    }
}