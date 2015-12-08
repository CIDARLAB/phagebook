function orderingCtrl($scope){
    $scope.loggedUserId = sessionStorage.getItem("loggedUserId");

        $scope.productNameSearch = function(){
           var productQuery = $scope.productQuery;
           if (productQuery != ""){
                window.location.href = "/html/queryProduct.html?Name=" + productQuery;
            }
        
        };
        
        $scope.companyNameSearch = function (){
            var companyQuery = $scope.companyQuery;
            if (companyQuery != ""){
                window.location.href = "/html/queryCompanyProduct.html?Name=" + companyQuery;
            }
        };
        
        if (getCookie("Order") !== ""){
            var idList = getCookie("Order");
            
            $.ajax({
                    url: 'getProductById',
                    type: 'GET',
                    async: false,
                    data: {
                        'ids': idList
                    },
                    success: function (response) {
                        //Load Product Data'
                         sortProductsFromResponse(response);
                    },
                    error: function(){
                        //Nothing found or bad query
                        alert("NOPE");
                    }
                });
            }
     
     
//    $scope.createOrder = function(){
//         if (getCookie("Order") !== ""){
//            var ids = getCookie("Order");
//            var name = $("#orderNameBox").val();
//            var description = $("#orderDescriptionBox").val();
//          
//            
//            $.ajax({
//                url: 'newOrder',
//                type: 'POST',
//                async: false,
//                data :{
//                    'orderIds' : ids,
//                    'name' : name,
//                    'description' : description
//                    
//                },
//                success: function (response) {
//                    alert(response);
//                    id = response;
//                    window.location.href = "/html/SelectColumns.html?orderId=" + response;
//                },
//                error: function(){ } 
//            });
//            
//       }
//    };
//    
}

     
     
    function sortProductsFromResponse(response) {
            var ids = [];
            for (var i = 0; i < response.length; i++){
                //
                var productTable = document.getElementById("productTable");
                var row = productTable.insertRow(i+1);
                row.id = "product" + i.toString();
                
                var nameCell = row.insertCell(0);
                var companyCell = row.insertCell(1);
                var costCell = row.insertCell(2);
                var descriptionCell = row.insertCell(3);
                var goodTypeCell = row.insertCell(4);
                var productURLCell = row.insertCell(5);
                var quantityCell = row.insertCell(6);
                
                
                var name = response[i]['name'];
                //mkay
                var company = response[i]['company']['name'];
                var cost = response[i]['cost'];
                var description = response[i]['description'];
                var goodType = response[i]['goodType'];
                var productURL = response[i]['productURL'];
                var quantity = response[i]['quantity'];
                
                
                
              
                nameCell.innerHTML = name;
                companyCell.innerHTML = company;
                costCell.innerHTML = cost;
                descriptionCell.innerHTML = description;
                goodTypeCell.innerHTML = goodType;
                productURLCell.innerHTML = productURL;
                quantityCell.innerHTML = quantity;
                
            }
        }
        
     

function setCookie(cname, cvalue, exdays)
{
            var d = new Date();
            d.setTime(d.getTime() + (exdays*24*60*60*1000));
            var expires = "expires=" +d.toUTCString();
            document.cookie = cname + "=" + cvalue + "; " + expires;
}
function getCookie(cname) {
    //GET A DAMN COOKIE VALUE
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
    //
}
function appendToCookie(cname, cvalue, exdays){
    
    
    var currentCookie = getCookie(cname);
   
    
    var updatedCookie = "";
    if (currentCookie == ""){
        updatedCookie = cvalue;
    }else if(currentCookie != "") {
        updatedCookie = currentCookie + ','+  cvalue ;
    }
    alert(updatedCookie);
    setCookie("Order" , updatedCookie, exdays);
}

function delete_cookie( name ) {
  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getParameterByName(name) 
{
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

