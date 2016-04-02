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

    tmpl.querySelector('.order-limit').innerText = orderJSON.Limit;

    tmpl.querySelector('.order-budget').innerText = orderJSON.Budget;


    var orderItemsTable = tmpl.querySelector('.order-items-table');

    var totalBeforeTax = 0;
    var TAX = .0725;

    if (orderJSON.Products != ""){ //when it's not empty
        var count = 0;
        for (var cartItem in orderJSON.Products) {
            if (orderJSON.Products.hasOwnProperty(cartItem)) {
                var response = doCartItemAjax(cartItem, orderJSON.Products[cartItem]);

                var itemQty = orderJSON.Products[cartItem];
                var rowCount = orderItemsTable.insertRow(count);
                var itemNameCell  = rowCount.insertCell(0);
                    itemNameCell.className = "item-name";
                    itemNameCell.style= "overflow: hidden; text-overflow: ellipsis";
                    itemNameCell.innerHTML = response.itemName;

                var itemQtyCell   = rowCount.insertCell(1);
                    itemQtyCell.className = "item-qty";
                    itemQtyCell.innerHTML = itemQty;
                var itemUnitPrice = rowCount.insertCell(2);
                    itemUnitPrice.className ="item-unit-price";
                    itemUnitPrice.innerHTML = response.itemUnitPrice;
                var itemCustomPrice = rowCount.insertCell(3);
                    itemCustomPrice.className = "item-custom-unit-price";
                    itemCustomPrice.innerHTML = response.customUnitPrice;
                var itemTotalPrice = rowCount.insertCell(3);
                    itemTotalPrice.className = "item-total-price";
                    itemTotalPrice.innerHTML = response.totalPrice;
                count++;

            }
        }

    }

    tmpl.querySelector('.total-before-tax-value').innerText = totalBeforeTax;
    tmpl.querySelector('.tax-value').innerText = "$"+ TAX * totalBeforeTax;
    tmpl.querySelector('.total-after-tax-value').innerText = "$" + ( (TAX * totalBeforeTax) + totalBeforeTax);







    content.appendChild(tmpl);
}

function doCartItemAjax(cartItemId, quantity){
    var responseObject = {};
    $.ajax({
        //do this for projects...
        url: "../parseCartItem",
        type: "GET",
        async: false,
        data: {
            "user": user,
            "cartItem": cartItemId
        },
        success: function (response) {
            var itemName        = response.productName;
            var itemUnitPrice   = "$"+ response.productUnitPrice;
            var customUnitPrice = "$"+ (response.discount * response.productUnitPrice);

            totalBeforeTax += quantity * (response.discount * response.productUnitPrice);

            var totalPrice      = "$" + quantity * response.productUnitPrice;


            responseObject["itemName"] = itemName;
            responseObject["itemUnitPrice"] = itemUnitPrice;
            responseObject["customUnitPrice"] = customUnitPrice;
            responseObject["totalPrice"] = totalPrice;


        },
        error: function (response){
            console.log("cart item querying failed");
        }
    });

    return responseObject;

}