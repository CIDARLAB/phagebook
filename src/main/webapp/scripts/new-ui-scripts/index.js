$(document).ready(function() {
    $('#createProfile').click( function () {
        
        var isValid = 1;
        var firstName = document.getElementById("inputFirstName").value;
        var lastName = document.getElementById("inputLastName").value;
        var emailId  = document.getElementById("emailAddress").value;
        var password = document.getElementById("password").value;
        
        if ( (firstName === "") || (lastName === "") || (emailId === "") || (password === "") )
        {
            alert("sssl");
            isValid = 0;
        }
        if (isValid && checkPasswordMatch()){
         $.ajax({
                    url: "createPerson",
                    type: "POST",
                    async: false,
                    data: {
                        //"id": loginResult.id,
                        "firstName": firstName,
                        "lastName": lastName,
                        "emailId": emailId,
                        "password": password
                    },
                    success: function (response) {
                        
                         var responseJSON = JSON.parse(response);
                         setCookie("emailId" , responseJSON.emailId, 1);
                         setCookie("clothoId", responseJSON.clothoId, 1);
                    },
                    error: function (response) {
                        //THIS CAN BE DONE BETTER ONCE WE KNOW WHAT WE ARE DOING.
                        alert("\n" +  response.statusText + "!\n" + response.responseText);
                    }
                });
        }
    })
});

function checkPasswordMatch() {
    var password = $("#password").val();
    var confirmPassword = $("#reenterPassword").val();

    if (password !== confirmPassword)
    {
       console.log("Passwords do not match");
       return false;
    }
    else
    {
       console.log("Passwords match");
       return true;
    }
}

function isEmpty( el ){
      return !$.trim(el.html());
  }