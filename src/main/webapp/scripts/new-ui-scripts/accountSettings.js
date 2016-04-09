$(document).ready( documentReady);

function documentReady(){
    $("#success-lab-alert").hide();
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
            var selects = document.getElementsByClassName('institution-drop-down');
            sessionStorage.setItem("accountSettings-institutions", JSON.stringify(response.institutions));
            var length = response.institutions.length; //has institutions and labs
            for (var k=0; k < selects.length ; k++){

                removeOptions(selects[k]);

                var opt = document.createElement('option');
                opt.value ="";
                opt.innerHTML = "...";
                selects[k].appendChild(opt);
                for (var i = 0; i < length; i++){

                    opt = document.createElement('option');
                    opt.value = response.institutions[i].institutionId;

                    opt.innerHTML = response.institutions[i].institutionName;
                    selects[k].appendChild(opt);


                }
            }

        },
        error: function (response) {

        }
    });

    $('.institution-drop-down').change( function () {


        var selectedInstitution = this.options[this.selectedIndex].innerHTML;

        var selectInstitutions =  document.getElementsByClassName('institution-drop-down');

        for (var select =0; select < selectInstitutions.length ; select++ ){
            selectInstitutions[select].selectedIndex = this.selectedIndex;
        }

        var responseArray = JSON.parse(sessionStorage.getItem("accountSettings-institutions"));


        var selectsLabs = document.getElementsByClassName('lab-drop-down');


        var numberOfInstitutions = responseArray.length;


        for (var i = 0; i < numberOfInstitutions; i++)
        {
            var labsArray = responseArray[i].labs;
            var labsLength = labsArray.length;

                if (responseArray[i].institutionName == selectedInstitution)
                    for (var i2 = 0; i2 < selectsLabs.length ; i2++) {


                        removeOptions(selectsLabs[i2]);

                        for (var j = 0; j < labsLength; j++) {

                            var opt2 = document.createElement('option');
                            opt2.value = labsArray[j].labId;

                            opt2.innerHTML = labsArray[j].labName;
                            selectsLabs[i2].appendChild(opt2);
                        }
                    }


            else if ( selectedInstitution === "..."){

                    for (var i3 =0; i3 < selectsLabs.length ; i3++) {

                        var opt2 = document.createElement('option');
                        opt2.value = "";
                        opt2.innerHTML = "...";
                        selectsLabs[i3].appendChild(opt2);
                        return;
                    }
                }



        }
    });

    $('#load-pis').click(function (){
        var selectedLab = $('#pi-lab-results option').filter(':selected')[0].value;
        if (selectedLab != "..."){
            doLabAjaxCall(selectedLab);
        }

    });

    $("#submit-lab-btn").click(function (){

        //WHY IS THIS AN ARRAY .. JQUERY EXPLAIN
        var name           = document.getElementById("lab-name").value;
        var description    = document.getElementById("lab-description").value;
        var phone          = document.getElementById("lab-phone").value;
        var url            = document.getElementById("lab-website").value;
        var institutionId  = $("#create-lab-institution").val();
        $.ajax({
            //do this for projects...
            url: "../createLab",
            type: "POST",
            async: false,
            data: {
                "user"       : getCookie("clothoId"),
                "name"       : name,
                "description": description,
                "phone"      : phone,
                "url"        : url,
                "institution": institutionId


            },
            success: function (response) {
                $("#success-lab-alert").fadeIn();
            },
            error: function (response) {

            }

        })
    });
}


function removeOptions(selectbox)
{
    var i;

    for( i = selectbox.options.length-1 ; i>=0 ; i--)
    {
        selectbox.remove(i);
    }
}

function doLabAjaxCall(labId){
    $.ajax({
        //do this for projects...

        url: "../listPIsOfLab",
        type: "GET",
        async: false,
        data: {
                "lab":labId
        },
        success: function (response) {

            for (var i =0; i  <  response.length; i++){
               alert("PI" + i + ": " + response[i].name + " Email: " + response[i].email + " ClothoId: " + response[i].clothoId );
            }

        },
        error: function (response) {

        }
    });
}

