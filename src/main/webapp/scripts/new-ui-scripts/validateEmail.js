$(document).ready(function() {
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
                    url: "../loginUser",
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
                            
                            window.location.href = '../html/resendEmailVerification.html';
                        }
                        else 
                        {
                            window.location.href = '../html/profile.html?user=' + response.clothoId;
                        }
                    },
                    error: function (response) {
                        var responseText = JSON.parse(response.responseText);
                        alert(responseText.message);
                    }
                });
        }
    });
});

function verifyEmail(){
        var emailId = getParameterByName('emailId');
        var salt = getParameterByName('salt');
        
        
        if (emailId && salt){
            $.ajax({
                url: '../verifyEmail',
                type: 'POST',
                async: false,
                data: {
                    'emailId': emailId,
                    'salt': salt
                },
                success: function (response) {
                    
                    alert("email has been validated, show some HTML here or something");
                    
                },
                
                error: function(){
                    alert("email has not been validated, there was an error. play a sad sound :(")
                }
            });
                
        }
    }

function getParameterByName(name) 
{
           name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
           var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
               results = regex.exec(location.search);
           return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}



