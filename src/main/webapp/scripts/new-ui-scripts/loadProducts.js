$(document).ready(function () {
   
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
        var searchType = "EXACT"; //USE JQUERY TO FIND FIXES
        
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