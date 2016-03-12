$(document).ready(function() {
    $.ajax({
        //do this for projects...
        url: "../loadPhagebookInstitutions",
        type: "GET",
        async: false,
        data: {

        },
        success: function (response) {
                
                sessionStorage.setItem("institutions", JSON.stringify(response));
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
        
        var name;
        var createdBy;
        var labId;
        var associatedProjectId;
        var budget;
        var orderLimit;
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
                        
                        
                    },
                    error: function (response) {
                        
                    }
                });
   
    });
    
});

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


