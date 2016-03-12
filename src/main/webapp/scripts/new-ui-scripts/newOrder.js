$(document).ready(function() {
    
    
    $("#createOrder").click( function () {
        
        var name;
        var createdBy;
        var labId;
        var associatedProjectId;
        var budget;
        var orderLimit;
        $.ajax({
                    url: "../newOrder",
                    type: "POST",
                    async: false,
                    data: {
                        
                        "name": name,
                        "createdBy": createdBy,
                        "labId": labId,
                        "associatedProjectId": associatedProjectId,
                        "budget": budget,
                        "orderLimit": orderLimit
                    },
                    success: function (response) {
                        
                        
                    },
                    error: function (response) {
                        
                    }
                });
   
    });
    
});




