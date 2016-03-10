$(document).ready(function() {
    
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