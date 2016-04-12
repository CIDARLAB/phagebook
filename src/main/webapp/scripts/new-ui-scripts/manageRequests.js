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

    $(".decline-order-btn").click(declineOrderBtnHandler);
    $(".approve-order-btn").click(approveOrderBtnHandler);


    $.ajax({
        //do this for projects...

        url: "../listColleagueRequests",
        type: "GET",
        async: false,
        data: {
            "userId": getCookie("clothoId")
        },
        success: function (response) {

            for (var i=0; i < response.length; i++){
                parseColleagueRequest(response[i]);
            }


        },
        error: function (response) {

        }
    });


    $(".decline-colleague-btn").click(declineColleagueBtnHandler);
    $(".approve-colleague-btn").click(approveColleagueBtnHandler);

}

function parseColleagueRequest(colleagueJSON){

    var content = $("#content");
    var template = document.getElementById('friend-request-template').content.cloneNode(true);
    template.querySelector('.colleague-name').text  = colleagueJSON.firstName + " " + colleagueJSON.lastName;
    template.querySelector('.colleague-name').href  =  "../html/colleague.html?user=" + colleagueJSON.clothoId;
    template.querySelector('.approve-colleague-btn').value = colleagueJSON.clothoId;
    template.querySelector('.decline-colleague-btn').value = colleagueJSON.clothoId;

    content.append(template);



}

function parseOrderRequest(orderJSON){
    var content = $("#content");
    var template = document.getElementById('submitted-order-request-template').content.cloneNode(true);
    template.querySelector('.request-btn').innerHTML=  "ORDER #" + orderJSON.clothoId + "&nbsp; ' " + orderJSON.name + "'";
    template.querySelector('.order-nickname').value         = orderJSON.name;
    template.querySelector('.order-project-name').innerText = orderJSON.relatedProjectName;
    var received = orderJSON.receivedByIds[0]["0"] + "\n";

    for (var count = 1; count < orderJSON.receivedByIds.length; count++){
        received +=  orderJSON.receivedByIds[count][count] +"\n";

    }

    template.querySelector('.order-received-by').innerText  = received;


    template.querySelector('.order-id').innerText           = orderJSON.clothoId;


    template.querySelector('.order-created-by').innerText   = orderJSON.createdByName;


    template.querySelector('.order-approved-by').innerText = (orderJSON.approvedById != null) ? orderJSON.approvedById: "N/A" ;
    template.querySelector('.order-budget').innerText = orderJSON.budget;
    template.querySelector('.order-limit').innerText = "$" +orderJSON.orderLimit;

    var orderItemsTable = template.querySelector('.order-items-table');

    var totalBeforeTax = 0;
    var TAX = orderJSON.taxRate;

    if (orderJSON.products != ""){ //when it's not empty
        var count = 1;

        for (var i = 0; i < orderJSON.products.length; i++ ) {



            //<td class="item-name">
            //        <a type="button" href="">
            //            <img alt="Delete item" src="../styles/img/icons/remove-item.png"/>
            //        </a>
            //    </td>
            var response = doCartItemAjax(orderJSON.products[i]);
            var itemQty = response.quantity;
            var rowCount = orderItemsTable.insertRow(i+1);
            var itemNameCell  = rowCount.insertCell(0);
            itemNameCell.className = "item-name";
            var a = document.createElement('a');
            a.type="button";
            a.className ="delete-product-button";
            a.href ="";
            a.name = orderJSON.ClothoId;

            var img = document.createElement('img');
            img.src ="../styles/img/icons/remove-item.png";
            img.className="delete-icon";
            img.name = orderJSON.products[i];
            a.appendChild(img);
            itemNameCell.style = "overflow: hidden; text-overflow: ellipsis";
            itemNameCell.appendChild(a);
            itemNameCell.innerHTML += response.itemName;


            var itemQtyCell   = rowCount.insertCell(1);
            itemQtyCell.className = "item-qty";
            itemQtyCell.innerHTML = itemQty;
            var itemUnitPrice = rowCount.insertCell(2);
            itemUnitPrice.className ="item-unit-price";
            itemUnitPrice.innerHTML =   "$" + response.itemUnitPrice.toFixed(2);

            var itemCustomPrice = rowCount.insertCell(3);
            itemCustomPrice.className = "item-custom-unit-price";
            itemCustomPrice.innerHTML = "$" + response.customUnitPrice.toFixed(2);
            var itemTotalPrice = rowCount.insertCell(4);
            itemTotalPrice.className = "item-total-price";
            itemTotalPrice.innerHTML =  "$" +  response.totalPrice.toFixed(2);


            totalBeforeTax += response.totalPrice;
        }

    }
    if (orderJSON.products == ""){
        var tr=  document.createElement('tr');
        tr.innerHTML = "There are currently no items in this order";
        tr.className += "no-items-message";
        template.querySelector('.submit-order-btn').disabled = true;

        orderItemsTable.appendChild(tr);
    }
    template.querySelector('.total-before-tax-value').innerText = "$" + totalBeforeTax.toFixed(2);

    template.querySelector('.tax-value').innerText = "$"+ ( (TAX - 1) * totalBeforeTax).toFixed(2) ;
    if (orderJSON.Budget < ( (TAX * totalBeforeTax) + totalBeforeTax)){
        template.querySelector('.total-after-tax-value').style = "color: red";
        template.querySelector('.submit-order-btn').disabled = true;
    }
    template.querySelector('.total-after-tax-value').innerText = "$" + (TAX * totalBeforeTax).toFixed(2);





    template.querySelector('.approve-order-btn').value = orderJSON.clothoId;
    template.querySelector('.decline-order-btn').value = orderJSON.clothoId;



    content.append(template);
}

function doCartItemAjax(cartItemId){
    var responseObject = {};

    $.ajax({
        //do this for projects...
        url: "../parseCartItem",
        type: "GET",
        async: false,
        data: {
            "user": getCookie("clothoId"),
            "cartItem": cartItemId
        },
        success: function (response) {
            var percentage      = 1 - (response.discount / 100.00);
            var itemName        = response.productName;
            var itemUnitPrice   = response.productUnitPrice;
            var customUnitPrice = (percentage * response.productUnitPrice);

            var quantity        = response.quantity;
            var totalPrice      =  quantity * response.productUnitPrice  * percentage;


            responseObject["itemName"] = itemName;
            responseObject["itemUnitPrice"] = itemUnitPrice;
            responseObject["customUnitPrice"] = customUnitPrice;
            responseObject["totalPrice"] = totalPrice;
            responseObject["quantity"] = quantity;


        },
        error: function (response){
            console.log("cart item querying failed");
        }
    });

    return responseObject;

}

function approveOrderBtnHandler(){

    $.ajax({
        //do this for projects...
        url: "../approveOrder",
        type: "POST",
        async: false,
        data: {
            "userId": getCookie("clothoId"),
            "orderId": this.value
        },
        success: function (response) {
            alert("order approved");
            window.location.href="";
        },
        error: function (response){
            window.location.href="";
        }
    });



}
function declineOrderBtnHandler(){


    if (confirm("Really delete this order?")) {

        $.ajax({
            //do this for projects...
            url: "../denyOrder",
            type: "POST",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
                "orderId": this.value
            },
            success: function (response) {
                alert("order denied");
                window.location.href="";

            },
            error: function (response){
                window.location.href="";
            }
        });

    } else {
        //do nothing
    }



}


function approveColleagueBtnHandler(){
    alert(this.value);
    $.ajax({
        //do this for projects...
        url: "../approveColleagueRequest",
        type: "POST",
        async: false,
        data: {
            "userId": getCookie("clothoId"),
            "orderId": this.value
        },
        success: function (response) {
            alert("order approved");
            window.location.href="";
        },
        error: function (response){
            window.location.href="";
        }
    });
}

function declineColleagueBtnHandler(){
    if (confirm("Really deny this request?")){
        $.ajax({
            //do this for projects...
            url: "../denyColleagueRequest",
            type: "POST",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
                "colleagueId": this.value
            },
            success: function (response) {

                window.location.href="";

            },
            error: function (response){
                window.location.href="";
            }
        });

    }
    else {

    }

}



