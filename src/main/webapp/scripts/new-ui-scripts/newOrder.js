$(document).ready(function() {
    
    var timerVal;
    $("#projectName").keypress( keyPressHandler );

    function keyPressHandler(){
        clearTimeout(timerVal); // stops previous attempt.
        timerVal = setTimeout(doAjax, 500);//after a second of no input flip the flag.

    }
    function doAjax(){
        
       var name = $("#projectName").val();
       var isValid = 0;
       if (name !== ''){
           isValid = 1;
       }
        
        if (isValid){
            $.ajax({
               //do this for projects...
               url: "../autoCompleteProjects",
               type: "GET",
               async: false,
               data: {
                    "name": name
               },
               success: function (response) {
                   var select = document.getElementById('projectResults');
                   removeOptions(select);
                   var lengthOfResponse = response.length;
                   for (var i = 0; i < lengthOfResponse; i++){
                        var opt = document.createElement('option');
                            opt.value = response[i].id;

                            opt.innerHTML = response[i].name;
                            select.appendChild(opt);
                   }

               },
               error: function (response) {

               }
            });
        }
    }

    $.ajax({
        //do this for projects...
        url: "../loadPhagebookInstitutions",
        type: "GET",
        async: false,
        data: {

        },
        success: function (response) {
            // POSSIBLE WAY TO GET THE RESPONSE AGAIN?  
            //sessionStorage.setItem("institutions", JSON.stringify(response));
                var select = document.getElementById('lab-name');
                var length = response.institutions.length;
                var count = 0;
                for (var i = 0; i < length; i++) {
                    for (var j = 0; j < response.institutions[i].labs.length ; j++  ){
                        var opt = document.createElement('option');
                        opt.value = response.institutions[i].labs[j].labId;
                        count++;
                        opt.innerHTML = response.institutions[i].labs[j].labName;
                        select.appendChild(opt);
                    }
                }
        },
        error: function (response) {
                
        }
    });

    $("#createOrder").click( function () {
        
        var name = document.getElementById('orderNickname').value;
        var createdBy = getCookie("clothoId");
        var labId = document.getElementById('lab-name').value;
        var associatedProjectId = document.getElementById('projectResults').value;
        var budget = document.getElementById('orderBudget').value;
        var orderLimit = document.getElementById('orderLimit').value;
        var taxRate = document.getElementById('taxRate').value;

        var isValid = 1;

        if ((name === "") || (labId === "") || (associatedProjectId === "") || (budget === "") || (orderLimit === "")) {
            $("#order-fields-required-alert").fadeIn();
            isValid = 0;
        }
        else if (validatePrice(budget) == false) {
            $("#invalid-budget-alert").fadeIn();
            isValid = 0;
        }
        else if (!validateOrderLimit(orderLimit)) {
            $("#invalid-order-limit-alert").fadeIn();
            isValid = 0;
        }
        else if (!validateTax(taxRate)) {
            $("#invalid-tax-rate-alert").fadeIn();
            isValid = 0;
        }
        else {
            isValid = 1;
        }

        if (isValid && validatePrice(budget) && validateOrderLimit(orderLimit) && validateTax(taxRate)) {
            doAjaxCallToCreateOrder(name, createdBy, labId, associatedProjectId, budget, orderLimit);
        }
    });
    
});

function doAjaxCallToCreateOrder(name, createdBy, labId, associatedProjectId, budget, orderLimit) {
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
                //alert("order created!");
                window.location.href = "../html/currentOrders.html";
            },
            error: function (response) {
                alert(response.message);
            }
        });
}

function loadSelectElementOptions(){
    var min = 12,
    max = 100,
    select = document.getElementById('lab-name');

    for (var i = min; i<=max; i++){
        var opt = document.createElement('option');
        opt.value = i;
        opt.innerHTML = i;
        select.appendChild(opt);
    }
}

function removeOptions(selectbox) {
    var i;
    for(i=selectbox.options.length-1;i>=0;i--) {
        selectbox.remove(i);
    }
}

function validatePrice(budget) {
    return /^\d+(?:\.\d{0,2})$/.test(budget);
}

function validateOrderLimit(limit) {
    var value = parseInt(limit);
    return (!isNaN(value) && value >= 1 && value <= 13);
}

function validateTax(tax) {
    var parts = tax.split(".");
    if (typeof parts[1] == "string" && (parts[1].length == 0 || parts[1].length > 2)) {
        return false;
    }
    var n = parseFloat(tax);
    if (isNaN(n)) {
        return false;
    }
    if (n < 0 || n > 100) {
        return false;
    }
    return true;
}
