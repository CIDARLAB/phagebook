function createOrderCard(orderJSON) {
    var content = document.getElementById('content');
    var tmpl = document.getElementById('order-card-template').content.cloneNode(true);

    tmpl.querySelector('.order-nickname').value         = orderJSON.Name;
    tmpl.querySelector('.order-project-name').innerText = orderJSON.RelatedProjectName;
    tmpl.querySelector('.order-id').innerText           = orderJSON.ClothoId;
    tmpl.querySelector('.order-created-by').innerText   = orderJSON.CreatedByName;

    if (orderJSON.ReceivedByIds != ""){ //when it's not empty
        for (var j = 0 ; j < orderJSON.ReceivedByIds.length; j++){
            tmpl.querySelector('.order-received-by').innerText  = orderJSON.ReceivedByIds[j].receiverName;
        }
    }
    else {
            tmpl.querySelector('.order-received-by').innerText  = "N/A";
    }


    if (orderJSON.ApprovedByName != ""){ //when it's not empty
            tmpl.querySelector('.order-approved-by').innerText  = orderJSON.ApprovedByName;
    }
    else {
            tmpl.querySelector('.order-approved-by').innerText  = "N/A";
    }

    tmpl.querySelector('.order-limit').innerText  = orderJSON.Limit;

    tmpl.querySelector('.order-budget').innerText = orderJSON.Budget;

    tmpl.querySelector('.submit-order-btn').value = orderJSON.ClothoId;

    tmpl.querySelector('.export-csv-btn').value   = orderJSON.ClothoId;

    tmpl.querySelector('.delete-order-btn').value = orderJSON.ClothoId;
    tmpl.querySelector('.edit-order-btn').value   = orderJSON.ClothoId;



    var orderItemsTable = tmpl.querySelector('.order-items-table');

    var totalBeforeTax = 0;
    var TAX = orderJSON.TaxRate;

    if (orderJSON.Products != ""){ //when it's not empty
        var count = 1;

        for (var i = 0; i < orderJSON.Products.length; i++ ) {


                var response = doCartItemAjax(orderJSON.Products[i]);
                var itemQty = response.quantity;
                var rowCount = orderItemsTable.insertRow(i+1);
                var itemNameCell  = rowCount.insertCell(0);
                    itemNameCell.className = "item-name";
                    itemNameCell.style= "overflow: hidden; text-overflow: ellipsis";
                    itemNameCell.innerHTML = response.itemName;

                var itemQtyCell   = rowCount.insertCell(1);
                    itemQtyCell.className = "item-qty";
                    itemQtyCell.innerHTML = itemQty;
                var itemUnitPrice = rowCount.insertCell(2);
                    itemUnitPrice.className ="item-unit-price";
                    itemUnitPrice.innerHTML =   "$" + response.itemUnitPrice;
                var itemCustomPrice = rowCount.insertCell(3);
                    itemCustomPrice.className = "item-custom-unit-price";
                    itemCustomPrice.innerHTML = "$" + response.customUnitPrice;
                var itemTotalPrice = rowCount.insertCell(4);
                    itemTotalPrice.className = "item-total-price";
                    itemTotalPrice.innerHTML =  "$" +  response.totalPrice;


            totalBeforeTax += response.totalPrice;
        }

    }

    tmpl.querySelector('.total-before-tax-value').innerText = "$" + totalBeforeTax;
    tmpl.querySelector('.tax-value').innerText = "$"+ (TAX * totalBeforeTax) ;
    tmpl.querySelector('.total-after-tax-value').innerText = "$" + ( (TAX * totalBeforeTax) + totalBeforeTax);







    content.appendChild(tmpl);
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
            var percentage      = (response.discount / 100.00);
            var itemName        = response.productName;
            var itemUnitPrice   = response.productUnitPrice;
            var customUnitPrice = (percentage * response.productUnitPrice);

            var quantity        = response.quantity;
            alert(quantity);

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