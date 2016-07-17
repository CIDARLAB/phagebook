/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




$("document").ready(function () {
    var productQueryName = getParameterByName("Name");
    //console.log("ready");
    $.ajax({
        url: 'queryProductByName',
        type: 'GET',
        async: false,
        data: {
            'Name': productQueryName
        },
        success: function (response) {
            //Load Product Data'
            sortProductsFromResponse(response);
        },
        error: function () {
            //Nothing found or bad query
            alert("No Products found");
        }
    });
});



function sortProductsFromResponse(response) {
    var ids = [];
    for (var i = 0; i < response.length; i++) {
        //
        var productTable = document.getElementById("productTable");
        var row = productTable.insertRow(i + 1);
        row.id = "product" + i.toString();
        var selectCell = row.insertCell(0);
        var nameCell = row.insertCell(1);
        var companyCell = row.insertCell(2);
        var costCell = row.insertCell(3);
        var descriptionCell = row.insertCell(4);
        var goodTypeCell = row.insertCell(5);
        var productURLCell = row.insertCell(6);
        var quantityCell = row.insertCell(7);


        var name = response[i]['name'];
        var company = response[i]['company'];
        var cost = response[i]['cost'];
        var description = response[i]['description'];
        var goodType = response[i]['goodType'];
        var productURL = response[i]['productURL'];
        var quantity = response[i]['quantity'];


        selectCell.innerHTML = "<input type='checkbox' id='productCheck" + i + "'>";
        nameCell.innerHTML = name;
        companyCell.innerHTML = company;
        costCell.innerHTML = cost;
        descriptionCell.innerHTML = description;
        goodTypeCell.innerHTML = goodType;
        productURLCell.innerHTML = productURL;
        quantityCell.innerHTML = quantity;


        ids.push(response[i]['clothoID']);

        setCookie("LoadedOrders", "", 1);
        populateSortedOrderCookies(ids);
    }
}

function populateSortedOrderCookies(ids) {
    var idString = ids[0];
    //console.log("populate");

    for (var i = 1; i < ids.length; i++) {

        idString = idString + "," + ids[i];
    }


    setCookie("LoadedOrders", idString, 1);
}

function selectProducts() {
    var numberOfCheckBoxes = totalNumberOfCheckBoxesWithName('productCheck');
    //console.log("selectProducts");
    var OrderIds = []; // create an empty array
    var loadedOrderString = getCookie("LoadedOrders");
    var loadedOrdersOnPage = loadedOrderString.split(',');
    var selectedBoxCount = 0;
    for (var i = 0; i < numberOfCheckBoxes; i++) {
        if (document.getElementById('productCheck' + i).checked) {

            OrderIds.push(loadedOrdersOnPage[i]);
            selectedBoxCount++;
        } else {

        }
    }
    if (selectedBoxCount !== 0) {
        appendToCookie("Order", OrderIds, 1);
        window.location.href = "./html/ordering.html";
    } else {
        alert("Nothing Selected");
    }
    //should not need to delete LoadedOrder cookie


}




function totalNumberOfCheckBoxesWithName(name) {
    var finished = false;

    var productNumber = 0;

    while (!finished) {
        var checkbox = document.getElementById(name + productNumber);
        if (checkbox === null) {
            finished = true;
        } else {
            //for clarity
            finished = false;
            productNumber++;
        }

    }

    return productNumber;
}
        