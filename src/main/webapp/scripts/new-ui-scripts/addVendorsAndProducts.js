$(document).ready(function() {
    $("#createVendor").click(function()
        {
            var name        =  $("#vendorName").val();
            var description =  $("#vendorDescription").val();
            var contact     =  $("#vendorContact").val();
            var phone       =  $("#vendorPhone").val();
            var url         =  $("#vendorWebsite").val();
            
           
            createVendorAjax(name, description, contact, phone, url);
        }
    );
    
    $("#createProduct").click(function()
        {   var productUrl  = $("#productUrl").val();
            var company     = $("#companyResults").val();
            var goodType    = $("#productType").val();
            var cost        = $("#productPrice").val();
            var quantity    = $("#productQuantity").val();
            var name        = $("#productName").val();
            var description = $("#productDescription").val();
            
            createProductAjax(productUrl, company, goodType, cost, quantity, name, description);
        }
    );
        
    $("#lookupCompanies").click(function () 
        {
            //need to do an ajax call to that servlet I need...
            
       var name = $("#companyName").val();
       var isValid = 0;
       if (name !== ''){
           isValid = 1;
       }
      
        
        if (isValid){
            $.ajax({
               //do this for projects...
               url: "../autoCompleteVendors",
               type: "GET",
               async: false,
               data: {
                    "name": name
               },
               success: function (response) {
                   var select = document.getElementById('companyResults');
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
                   alert("unable to find anything");
               }
            });
        }
    });
        
    }
);
   
function createVendorAjax(name, description, contact, phone, url){
    $.ajax({
            //do this for projects...
            url: "../createVendor",
            type: "POST",
            async: false,
            data: {
                 "name": name,
                 "description": description,
                 "contact": contact,
                 "phone": phone, 
                 "url": url
                 
            },
            success: function (response) {
                alert("Vendor created, ID is " + response.id);
            },
            error: function (response) {

            }
         });
}

function createProductAjax(productUrl, company, goodType, cost, quantity, name, description){
    $.ajax({
            //do this for projects...
            url: "../createProduct",
            type: "POST",
            async: false,
            data: {
                "productUrl": productUrl,
                "company": company,
                "goodType": goodType,
                "cost": cost,
                "quantity": quantity,
                "name": name,
                "description": description
                
            },
            success: function (response) {
               alert("Product created\n" + 
                       "Named: " + response.name +'\n' +
                       "id "  + response.id );

            },
            error: function (response) {

            }
         });
}

function removeOptions(selectbox)
{
    var i;
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}
    
