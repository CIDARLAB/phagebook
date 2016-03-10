$(document).ready(function() {
    $('#createProfile').click( function () {
        
        var isValid = 1;
        
        if (isValid){
         $.ajax({
                    url: "createPerson",
                    type: "POST",
                    async: false,
                    data: {
                        //"id": loginResult.id,
                        "firstName": document.getElementById("inputFirstName").value,
                        "lastName": document.getElementById("inputLastName").value,
                        "emailId":  document.getElementById("emailAddress").value,
                        "password": document.getElementById("password").value
                    },
                    success: function (response) {
                        
                        

                        window.location.href = './html/validateEmail.html';
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