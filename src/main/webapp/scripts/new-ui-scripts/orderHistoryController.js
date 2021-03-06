function orderHistoryCtrl($scope) {
    $(document).ready(function () {



        $.ajax({
            url: "../listOrdersOfPerson",
            type: "GET",
            async: false,
            data: {
                "user": getCookie("clothoId")
            },
            success: function (response)
            {

                for (var i = 0; i < response.length; i++) {
                    generateOrderCard(response[i]);
                }

//                resubmitOrderBtn(response[0]);

            },
            error: function (response)
            {

            }
        });

        $('.export-csv-btn').click(exportCSVbtnHanlder);
        $('.resub-order-btn').click(resubmitOrderBtn);


    });

}

function generateOrderCard(orderJSON) {
    //console.log(orderJSON);
    var content = $("#content");
    var template = document.getElementById('order-card-history-template').content.cloneNode(true);

    template.querySelector('.order-nickname').value = orderJSON.name;
    template.querySelector('.order-project-name').innerText = orderJSON.relatedProjectName;
    var received = orderJSON.receivedByIds[0]["0"] + "\n";

    for (var count = 1; count < orderJSON.receivedByIds.length; count++) {
        received += orderJSON.receivedByIds[count][count] + "\n";

    }


    template.querySelector('.order-received-by').innerText = received;


    template.querySelector('.order-id').innerText = orderJSON.clothoId;


    template.querySelector('.order-created-by').innerText = orderJSON.createdByName;


    template.querySelector('.order-approved-by').innerText = (orderJSON.approvedById != null) ? orderJSON.approvedById : "N/A";
    template.querySelector('.order-budget').innerText = orderJSON.budget;
    template.querySelector('.order-limit').innerText = "$" + orderJSON.orderLimit;
    template.querySelector('.order-enum-status').innerText = orderJSON.status;
    template.querySelector('.export-csv-btn').value = orderJSON.clothoId;
    template.querySelector('.resub-order-btn').value = orderJSON.clothoId;



    switch (orderJSON.status) {
        case "SUBMITTED":
            template.querySelector('.status').className = "received-status";
            template.querySelector('.order-date').innerText = "Date Created: " + orderJSON.dateCreated;
            break;
        case "DENIED":
            template.querySelector('.status').className = "denied-status";
            template.querySelector('.order-date').innerText = "Date Created: " + orderJSON.dateCreated;

            break;
        case "APPROVED":
            template.querySelector('.status').className = "approved-status";
            template.querySelector('.order-date').innerText = "Date Approved: " + orderJSON.dateApproved;

            break;
        default:
            template.querySelector('.status').className = "recieved-status";
            break;

    }


    var orderItemsTable = template.querySelector('.order-items-table');

    var totalBeforeTax = 0;
    var TAX = orderJSON.taxRate;

    if (orderJSON.products != "") { //when it's not empty
        var count = 1;

        for (var i = 0; i < orderJSON.products.length; i++) {



            //<td class="item-name">
            //        <a type="button" href="">
            //            <img alt="Delete item" src="../styles/img/icons/remove-item.png"/>
            //        </a>
            //    </td>
            var response = doCartItemAjax(orderJSON.products[i]);
            var itemQty = response.quantity;
            var rowCount = orderItemsTable.insertRow(i + 1);
            var itemNameCell = rowCount.insertCell(0);
            itemNameCell.className = "item-name";
            var a = document.createElement('a');
            a.type = "button";
            a.className = "delete-product-button";
            a.href = "";
            a.name = orderJSON.ClothoId;

            var img = document.createElement('img');
            img.src = "../styles/img/icons/remove-item.png";
            img.className = "delete-icon";
            img.name = orderJSON.products[i];
            a.appendChild(img);
            itemNameCell.style = "overflow: hidden; text-overflow: ellipsis";
            itemNameCell.appendChild(a);
            itemNameCell.innerHTML += response.itemName;


            var itemQtyCell = rowCount.insertCell(1);
            itemQtyCell.className = "item-qty";
            itemQtyCell.innerHTML = itemQty;
            var itemUnitPrice = rowCount.insertCell(2);
            itemUnitPrice.className = "item-unit-price";
            itemUnitPrice.innerHTML = "$" + response.itemUnitPrice.toFixed(2);

            var itemCustomPrice = rowCount.insertCell(3);
            itemCustomPrice.className = "item-custom-unit-price";
            itemCustomPrice.innerHTML = "$" + response.customUnitPrice.toFixed(2);
            var itemTotalPrice = rowCount.insertCell(4);
            itemTotalPrice.className = "item-total-price";
            itemTotalPrice.innerHTML = "$" + response.totalPrice.toFixed(2);


            totalBeforeTax += response.totalPrice;
        }

    }
    if (orderJSON.products == "") {
        var tr = document.createElement('tr');
        tr.innerHTML = "There are currently no items in this order";
        tr.className += "no-items-message";
        template.querySelector('.submit-order-btn').disabled = true;

        orderItemsTable.appendChild(tr);
    }
    template.querySelector('.total-before-tax-value').innerText = "$" + totalBeforeTax.toFixed(2);

//removed
//    template.querySelector('.tax-value').innerText = "$" + ((TAX - 1) * totalBeforeTax).toFixed(2);
//    if (orderJSON.Budget < ((TAX * totalBeforeTax) + totalBeforeTax)) {
//        template.querySelector('.total-after-tax-value').style = "color: red";
//        template.querySelector('.submit-order-btn').disabled = true;
//    }
//    template.querySelector('.total-after-tax-value').innerText = "$" + (TAX * totalBeforeTax).toFixed(2);

//added
    template.querySelector('.tax-value').innerText = "$" + 0.00;
    template.querySelector('.total-after-tax-value').innerText = "$" + totalBeforeTax.toFixed(2);

    content.append(template);
}

function doCartItemAjax(cartItemId) {
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
            var percentage = 1 - (response.discount / 100.00);
            var itemName = response.productName;
            var itemUnitPrice = response.productUnitPrice;
            var customUnitPrice = (percentage * response.productUnitPrice);

            var quantity = response.quantity;
            var totalPrice = quantity * response.productUnitPrice * percentage;


            responseObject["itemName"] = itemName;
            responseObject["itemUnitPrice"] = itemUnitPrice;
            responseObject["customUnitPrice"] = customUnitPrice;
            responseObject["totalPrice"] = totalPrice;
            responseObject["quantity"] = quantity;


        },
        error: function (response) {
            //console.log("cart item querying failed");
        }
    });

    return responseObject;

}

function exportCSVbtnHanlder() {

    var orderId = this.value;
    console.log(orderId);

    $.ajax({
        url: "../exportOrderCSV",
        type: "GET",
        async: false,
        data: {
            "orderId": orderId
        },
        success: function (response) {
            window.open("../resources/OrderSheets/Order_" + orderId + ".csv", '_blank');
        },
        error: function (response) {
            alert("An error occurred with exporting the CSV.");
        }
    });
}


function resubmitOrderBtn() {

//    orderId = orderJSON.clothoId;
    var orderId = this.value;
    console.log(orderId);

    $.ajax({
        url: "../resubmitOrder",
        type: "POST",
        async: false,
        data: {
            "orderId": orderId
        },
        success: function (response) {
            alert("success");
        },
        error: function (response) {
            alert("An error occurred.");
        }
    });
}
