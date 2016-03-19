$(document).ready(function () {
    $("#lookupProducts").click( function () {
       
    });
    var timerVal;
    $("#productNameToSearch").keypress( keyPressHandler );
    
    
    function keyPressHandler(){
        clearTimeout(timerVal); // stops previous attempt.
        timerVal = setTimeout(doAjax, 1000);//after a second of no input flip the flag.
        

    }
    function doAjax()
    {
        var name = $("#productNameToSearch").val();
      
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
                    "searchType": "STARTSWITH"
                    
               },
               success: function (response) {
                   alert(response.responseText);
                   var select = document.getElementById('productResults');
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
                    alert(response.responseText);    
                 }
            });
        }
    }   
});
