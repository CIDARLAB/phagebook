$(document).ready(function() {
    $('#createProfile').click( function () {
        
        var isValid = 1;
        var firstName = document.getElementById("inputFirstName").value;
        var lastName = document.getElementById("inputLastName").value;
        var emailId  = document.getElementById("emailAddress").value;
        var password = document.getElementById("password").value;
        
        if ( (firstName === "") || (lastName === "") || (emailId === "") || (password === "") )
        {
            alert("Fields cannot be blank!");
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
                        window.location.href = './html/validateEmail.html';
                    },
                    error: function (response) {
                        //THIS CAN BE DONE BETTER ONCE WE KNOW WHAT WE ARE DOING.
                        alert("\n" +  response.statusText + "!\n" + response.responseText);
                    }
                });
        }
    });
    
    $('#login').click( function () {
        var isValid  = 1;
        var loginId  = document.getElementById("loginEmailAddress").value;
        var password = document.getElementById("loginPassword").value;
        if ((loginId === "") || (password === ""))
        {
            isValid = 0;
        }
        
        
        if (isValid)
        {
            $.ajax({
                    url: "loginUser",
                    type: "POST",
                    async: false,
                    data: {
                           "email":   document.getElementById("loginEmailAddress").value,
                        "password":   document.getElementById("loginPassword").value
                    },
                    success: function (response) {
                        //alert(response);
                        //alert("Got Some Response" + response);
                       
                       
                        setCookie("clothoId", response.clothoId, 1);     //use cookies instead..
                        
                        setCookie("emailId", document.getElementById("loginEmailAddress").value, 1 );
                       
                        
                        if (response.activated  === "false")
                        {
                            setCookie("clothoId", response.clothoId , 1);
                            setCookie("emailId",  response.emailId  , 1);
                            
                            window.location.href = './html/validateEmail.html?id=' + response.clothoId ;
                        }
                        else 
                        {
                            window.location.href = './html/profile.html?user=' + response.clothoId;
                        }
                    },
                    error: function (response) {
                        var responseText = JSON.parse(response.responseText);
                        alert( responseText.message);
                    }
                });
        }
    });
    
    
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