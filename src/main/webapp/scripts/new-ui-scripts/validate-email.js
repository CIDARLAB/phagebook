$(document).ready(function() {
    
    
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



