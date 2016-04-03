$(document).ready(function() {

    var timerVal;
    $("#productNameToSearch").keypress( keyPressHandler );


    function keyPressHandler(){
        clearTimeout(timerVal); // stops previous attempt.
        timerVal = setTimeout(doAjax, 500);//after a second of no input flip the flag.

        //add a little clear button

    }

    function doAjax()
    {
        var name = $("#productNameToSearch").val();
        var searchType = "STARTSWITH"; //USE JQUERY TO FIND FIXES

        var isValid = 0;
        if (name !== ''){
            isValid = 1;
        }

        if (isValid){
            $.ajax({
                //do this for projects...
                url: "../queryProductByName",
                type: "GET",
                async: false,
                data: {
                    "name": name,
                    "searchType": searchType

                },
                success: function (response) {

                    var table = $("#product-result-table");
                    table.empty();

                    for (var i = 0; i < response.length; i++) {

                        var tr = document.createElement("tr");
                        tr.id = "product"+i;
                        var checkbox = document.createElement("td");
                        var checkboxTableData = document.createElement("input");
                        checkboxTableData.type = "checkbox";
                        checkboxTableData.value = response[i].clothoID;

                        checkbox.appendChild(checkboxTableData);

                        tr.appendChild(checkbox);


                        var image = document.createElement("td");
                        var imageTableData = $('<img id="dynamic">');
                        imageTableData.attr('src', "../styles/img/mis/test-item.jpg");
                        imageTableData.appendTo(image);
                        imageTableData.attr('style', 'height:70px;');
                        tr.appendChild(image);



                        var anchor = document.createElement("td");
                        var a = document.createElement('a');
                        a.href = "#";
                        a.text = response[i].name;
                        a.setAttribute("style", "color:#1E714A");
                        anchor.appendChild(a);

                        tr.appendChild(anchor);


                        var unitPrice = document.createElement("td");
                            unitPrice.innerHTML = "Unit Price: " + response[i].unitPrice;

                        tr.appendChild(unitPrice);

                        var quantity = document.createElement("td");
                            var quantityTableData = document.createElement("input");
                                quantityTableData.type = "number";
                                quantityTableData.name = "quantity";
                                quantityTableData.placeholder = 1;
                                quantityTableData.value = 1;
                            quantity.appendChild(quantityTableData);

                        tr.appendChild(quantity);

                        table.append(tr);

                    }

                },
                error: function (response) {
                    alert(response.responseJSON.message);


                }
            });
        }
    }

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

            //alert("this user has " +response.length + " created orders");
            for (var i = 0; i < response.length; i++){
                var values = new Object();
                values["AffiliatedLabName"] = response[i].affiliatedLabName;
                values["AffiliatedLabId"]   = response[i].affiliatedLabId;
                values["DateCreated"]       = response[i].dateCreated;
                values["Name"]              = response[i].name;
                values["Limit"]             = response[i].limit;
                values["CreatedByName"]     = response[i].creatorName;
                values["RelatedProjectId"]  = response[i].relatedProjectId;
                values["RelatedProjectName"]= response[i].relatedProjectName;
                values["Description"]       = response[i].description;
                values["ReceivedByIds"]     = response[i].receivedByIds;
                values["ApprovedById"]      = response[i].approvedById;
                values["ApprovedByName"]    = response[i].approvedByName;
                values["CreatedByEmailId"]  = response[i].createdById;
                values["Products"]          = response[i].products;
                values["Budget"]            = response[i].budget;
                values["Status"]            = response[i].status;
                values["ClothoId"]          = response[i].id;
                values["TaxRate"]           = response[i].taxRate;


                createOrderCard(values);

                var select = document.getElementById("list-of-orders");
                removeOptions(select);
                var lengthOfResponse = response.length;
                for (var j = 0; j < lengthOfResponse; j++){
                    var opt = document.createElement('option');
                    opt.value = response[j].id;

                    opt.innerHTML = response[j].name;
                    select.appendChild(opt);
                }

            }
            $('.submit-order-btn').click(submitButtonHandler);
            $('.delete-order-btn').click(deleteButtonHandler);
            $('.export-csv-btn').click(exportCSVHandler);
            $('.edit-order-btn').click(editButtonHandler);


        },
        error: function (response) {
            alert(response.responseJSON.message);
        }
    });


    $("#add-to-order-btn").click( function (){

        var orderToAddTo  = document.getElementById("list-of-orders").value; // the clotho Id of that order

        var i = 0;
        var tableRow = document.getElementById("product0");
        var productsToAdd = [];

        while (tableRow != null) {
            var tableRowKids = tableRow.childNodes;
            var checkbox  = tableRowKids[0].childNodes[0]; // this is the checkbox
            var unitPrice = tableRowKids[3].innerHTML.slice(12); //removes the UNIT PRICE string
            var quantity  = tableRowKids[4].childNodes[0];

            if (checkbox.checked){

                var cartItemJSON = {};
                cartItemJSON["productId"] = checkbox.value;
                cartItemJSON["quantity"]  = quantity.value;
                cartItemJSON["discount"]  = 100;
                productsToAdd.push(cartItemJSON);

            }
            i++;
            tableRow = document.getElementById("product"+i);
        }

        alert(JSON.stringify(productsToAdd));


        $.ajax({
            url: '../addProductsToOrder',
            type: 'POST',
            dataType: 'JSON',
            async: false,
            data: {
                "CartItems"     : JSON.stringify(productsToAdd),
                "loggedInUserId": getCookie("clothoId"),
                "orderId"       : orderToAddTo
            },
            success: function (response) {
                alert("Products Added!");
                window.location.href = "../html/currentOrders.html";
            },
            error: function (response) {
                alert("error adding product to order");
            }
        });







    });

});



function removeOptions(selectbox)
{
    var i;

    for( i = selectbox.options.length-1 ; i>=0 ; i--)
    {
        selectbox.remove(i);
    }
}

function deleteButtonHandler(){

    var orderId = this.value;

    alert(orderId);

}

function submitButtonHandler(){

    var orderId = this.value;
    alert(orderId);

}

function exportCSVHandler(){

    var orderId = this.value;
    alert(orderId);

}

function editButtonHandler(){
    var orderId = this.value;
    alert(orderId);
}