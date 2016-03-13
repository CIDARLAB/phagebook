$(document).ready(function() {
     $("#lookupProjects").click( function () {
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
    });
    
    
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
                for (var i = 0; i < length; i++){
                    
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
        
        alert(name);
        alert(createdBy);
        alert(labId);
        alert(associatedProjectId);
        alert(budget);
        alert(orderLimit);
        doAjaxCallToCreateOrder(name, createdBy, labId, associatedProjectId, budget, orderLimit);
        
        
   
    });
    
});
function doAjaxCallToCreateOrder(name, createdBy, labId, associatedProjectId, budget, orderLimit){
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
                        alert("order created!");
                        sessionStorage.setItem("orderResponse", response.toString());
                        
                    },
                    error: function (response) {
                        
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

function removeOptions(selectbox)
{
    var i;
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}

