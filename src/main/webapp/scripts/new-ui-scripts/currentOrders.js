$(document).ready(function() {
    
    var user = getCookie("clothoId");
    //clotho ID of logged in user...
    
    $.ajax({
               //do this for projects...
               url: "../listCreatedOrdersOfPerson",
               type: "GET",
               async: false,
               data: {
                    "user": user
               },
               success: function (response) {
                   
                   alert("this user has " +response.length + " created orders");
                   for (var i = 0; i < response.length; i++){
                       alert( "THIS order has an Affiliated Lab Name of " +response[i].affiliatedLabName +
                               
                               " Date created of " + response[i].dateCreated + 
                               
                               " Name "  + response[i].name +
                               
                               " and other properties that you can use in the response object");
                       
                   }

               },
               error: function (response) {
                        alert(response.responseJSON.message);
               }
            });
    
    
});