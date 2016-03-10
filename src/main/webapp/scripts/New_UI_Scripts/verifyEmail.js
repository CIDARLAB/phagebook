


$("#resendButton").click(function ()
        { console.log(sessionStorage.getItem("username"));
            
        var user = getParameterByName('id');  //NOT NEEDED
        //Johan needs to help with the code hash send thing??
        $.ajax({
                    url: "resendVerification",
                    type: "GET",
                    async: false,
                    data: {
                        //need to get emailId from sessionStorage
                        //"emailId": code to get it from session Storage
                        "id" : getCookie("loggedInUserId")
                    },
                    success: function (response) {
                        alert(response["message"]);
                        //reload the page? HTML pop-up? 
                    },
                    error: function () {
                        alert("some sort of error occurred");
                    }
                });
                
        });
    
 function getParameterByName(name) {
            name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
            var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
        
