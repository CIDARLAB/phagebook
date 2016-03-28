function createOrderCard(orderJSON){

    var contentDiv = document.getElementById("content"); //where i'm adding to
    var columnDiv  = document.createElement("div"); //<div class="col-sm-9" style="margin-top: 20px">
        columnDiv.className = "col-sm-9";
        columnDiv.style = "margin-top: 20px";

    contentDiv.appendChild(columnDiv);

    var panelDefaultDiv = document.createElement("div"); //<div class="panel panel-default" class="order-card">
        panelDefaultDiv.className += "panel panel-default";
        columnDiv.appendChild(panelDefaultDiv);

    var panelHeadingDiv = document.createElement("div"); //<div class="panel-heading order-heading" style="height:60px">
        panelHeadingDiv.className += "panel-heading order-heading";
        panelHeadingDiv.style = "height:60px";
        panelDefaultDiv.appendChild(panelHeadingDiv);
    //panelHeadingDiv heading has 3 kids

    var labelForPanelHeadingDiv = document.createElement("label"); //<label class="col-xs-2 form-control-label" style="margin-top:10px">Order Name: </label>;
        labelForPanelHeadingDiv.className += "col-xs-2 form-control-label";
        labelForPanelHeadingDiv.style = "margin-top:10px";
        labelForPanelHeadingDiv.innerHTML = "Order Name: ";
        panelHeadingDiv.appendChild(labelForPanelHeadingDiv);

    var divForPanelHeadingDiv = document.createElement("div");
        divForPanelHeadingDiv.className += "col-sm-5";
        divForPanelHeadingDiv.style = "margin-left:-25px; margin-top:10px";

        var inputForDiv = document.createElement("input");
            inputForDiv.type = "text";
            inputForDiv.className += "order-nickname";
            inputForDiv.name = "type";
            inputForDiv.disabled = true;
            inputForDiv.value = orderJSON.Name;
            divForPanelHeadingDiv.appendChild(inputForDiv);
        panelHeadingDiv.appendChild(divForPanelHeadingDiv);

    var buttonForPanelHeadingDiv = document.createElement("button");
        buttonForPanelHeadingDiv.type= "button";
        buttonForPanelHeadingDiv.className += "btn btn-default edit-order-btn";

        var imageForButton = $('<img id="dynamic">');
            imageForButton.attr('src', "../styles/img/icons/white-pencil.png");
            imageForButton.attr('alt', "Edit Order");
            imageForButton.attr('style', 'height:20px');
            imageForButton.appendTo(buttonForPanelHeadingDiv);
    panelHeadingDiv.appendChild(buttonForPanelHeadingDiv);

    //done with these kids

    //BODY


    var panelBodyDiv = document.createElement("div");//<div class="panel-body order-top-panel">
        panelBodyDiv.className += "panel-body order-top-panel";

    panelDefaultDiv.appendChild(panelBodyDiv);

        var break1 = document.createElement("br");
    panelBodyDiv.appendChild(break1);

    var pForPanelBody = document.createElement("p");//<p>Associated Project:&nbsp;<a href="#" style="color:#1E714A;">Project Name</a></p>
        pForPanelBody.innerHTML = "Associated Project:&nbsp;";

        var anchorForPForPanelBody = document.createElement("a");
            anchorForPForPanelBody.href = "#" + orderJSON.RelatedProjectId;
            anchorForPForPanelBody.style = "color:#1E714A;"
            anchorForPForPanelBody.text = orderJSON.RelatedProjectName;
        pForPanelBody.appendChild(anchorForPForPanelBody);
    panelBodyDiv.appendChild(pForPanelBody);

        var break2 = document.createElement("br");
    panelBodyDiv.appendChild(break2);

    var tableForPanelBodyDiv = document.createElement("table");//<table class="table order-property-table">
        tableForPanelBodyDiv.className += "table order-property-table";
        panelBodyDiv.appendChild(tableForPanelBodyDiv);

    var trForTable = tableForPanelBodyDiv.insertRow(0);//<tr class="order-property-headers">
        trForTable.className += "order-property-headers";
        var orderId     = trForTable.insertCell(0);
            orderId.innerHTML = "ORDER ID #";
        var createdBy   = trForTable.insertCell(1);
            createdBy.innerHTML = "CREATED BY";
        var receivedBy  = trForTable.insertCell(2);
            receivedBy.innerHTML = "RECEIVED BY";
        var approvedBy  = trForTable.insertCell(3);
            approvedBy.innerHTML = "APPROVED BY";
        var orderLimit  = trForTable.insertCell(4);
            orderLimit.innerHTML = "ORDER LIMIT";
        var orderBudget = trForTable.insertCell(5);
            orderBudget.innerHTML = "ORDER BUDGET";

    var tr2ForTable = tableForPanelBodyDiv.insertRow(1);
        tr2ForTable.className += "order-property-data";

        var orderId2     =  tr2ForTable.insertCell(0);
            orderId2.className += "order-id";
            orderId2.innerHTML = orderJSON.Id;
        var createdBy2   =  tr2ForTable.insertCell(1);
            createdBy2.className += "order-created-by";
            createdBy2.innerHTML = orderJSON.CreatedByName;
        var receivedBy2  =  tr2ForTable.insertCell(2);
            receivedBy2.className += "order-received-by";
            if (orderJSON.ReceivedByIds === ""){
                receivedBy2.innerHTML = orderJSON.ReceivedByIds[0].receiverName;
            }
            else {
                receivedBy2.innerHTML = "N/A";
            }

        var approvedBy2  =  tr2ForTable.insertCell(3);
            approvedBy2.className += "order-approved-by";
            if (orderJSON.ApprovedBy === "")
            {
                approvedBy2.innerHTML = orderJSON.ApprovedByName;
            }
            else
            {
                approvedBy2.innerHTML = "N/A";
            }


        var orderLimit2  =  tr2ForTable.insertCell(4);
            orderLimit2.className += "order-limit";
            orderLimit2.innerHTML = orderJSON.Limit;
        var orderBudget2  =  tr2ForTable.insertCell(5);
            orderBudget2.innerHTML = "$"+ orderJSON.Budget;

        var tableForHover = document.createElement("table");//<table class="table table-hover order-items-table">
            tableForHover.className += "table table-hover order-items-table";
            panelDefaultDiv.appendChild(tableForHover);
            var row1 = tableForHover.insertRow(0);
                row1.className += "order-items-headers";
            var item            = row1.insertCell(0);
                item.innerHTML = "ITEM";
            var qty             = row1.insertCell(1);
                qty.innerHTML  = "QTY.";
            var unitPrice       = row1.insertCell(2);
                unitPrice.innerHTML = "UNIT PRICE";
            var customUnitPrice = row1.insertCell(3);
                customUnitPrice.innerHTML = "CUSTOM UNIT PRICE";
            var totalPrice      = row1.insertCell(4);
                totalPrice.innerHTML  = "TOTAL PRICE";

            var row2 = tableForHover.insertRow(1);

            var item2            = row1.insertCell(0);
            item2.innerHTML = orderJSON.Products;
            alert(JSON.stringify(orderJSON.Products));
            var qty2             = row1.insertCell(1);
            qty2.innerHTML  = "QTY.";
            var unitPrice2       = row1.insertCell(2);
            unitPrice2.innerHTML = "UNIT PRICE";
            var customUnitPrice2 = row1.insertCell(3);
            customUnitPrice2.innerHTML = "CUSTOM UNIT PRICE";
            var totalPrice2      = row1.insertCell(4);
            totalPrice2.innerHTML  = "TOTAL PRICE";























}