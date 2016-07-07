$(document).ready(function() {
   
       $("#resendVerification").click( function() {
        
        $.ajax({
                    url: "resendVerification",
                    type: "GET",
                    async: false,
                    data: {
                        //need to get emailId from sessionStorage
                        //"emailId": code to get it from session Storage
                        "id" : getCookie("clothoId")
                    },
                    success: function (response) {
                        
                        alert(response["message"]);
                        //reload the page? HTML pop-up? 
                    },
                    error: function () {
                        alert("unable to find person with id that was passed in from cookie");
                    }
                });
                
        });
        
        
        $("#returnIndex").click(function() {
            window.location.href = "";
        }
    );
 });
 
 



