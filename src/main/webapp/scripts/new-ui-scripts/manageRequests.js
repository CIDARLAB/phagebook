/**
 * Created by Herb on 4/9/16.
 */
$(document).ready(documentReady);

function documentReady(){
    $.ajax({
        //do this for projects...

        url: "../listSubmittedOrdersOfPerson",
        type: "GET",
        async: false,
        data: {
            "user": getCookie("clothoId")
        },
        success: function (response) {

            for (var i=0; i < response.length; i++){
                parseOrderRequest(response[i]);
            }


        },
        error: function (response) {

        }
    });

}

function parseOrderRequest(orderJSON){
    console.log(orderJSON);
    var content = $("#content");
    var template = document.getElementById('submitted-order-request-template').content.cloneNode(true);
    template.querySelector('.request-btn').innerHTML=  "ORDER#" + orderJSON;

    content.append(template);
}

