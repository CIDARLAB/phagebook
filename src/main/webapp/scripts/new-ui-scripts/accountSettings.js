$(document).ready( documentReady);

function documentReady(){
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
            var select = document.getElementById('institution-drop-down');
            sessionStorage.setItem("accountSettings-institutions", JSON.stringify(response.institutions));
            removeOptions(select);
            var length = response.institutions.length; //has institutions and labs
            var count = 0;
            var opt = document.createElement('option');
            opt.value ="";
            opt.innerHTML = "...";
            select.appendChild(opt);
            for (var i = 0; i < length; i++){

                opt = document.createElement('option');
                opt.value = response.institutions[i].institutitonId;

                opt.innerHTML = response.institutions[i].institutionName;
                select.appendChild(opt);


            }
        },
        error: function (response) {

        }
    });

    $('#institution-drop-down').change( function () {


        var selectedInstitution =  $('#institution-drop-down option').filter(':selected').text();

        var responseArray = JSON.parse(sessionStorage.getItem("index-institutions"));


        var selectLabs = document.getElementById('pi-lab-results');


        var numberOfInstitutions = responseArray.length;


        for (var i = 0; i < numberOfInstitutions; i++) {

            if (responseArray[i].institutionName === selectedInstitution ){
                removeOptions(selectLabs);
                var labsArray = responseArray[i].labs;
                var labsLength = labsArray.length;
                for (var j = 0 ; j < labsLength ; j++){
                    var opt2 = document.createElement('option');
                    opt2.value = labsArray[j].labId;

                    opt2.innerHTML = labsArray[j].labName;
                    selectLabs.appendChild(opt2);
                }
            } else if ( selectedInstitution === "..."){
                removeOptions(selectLabs);
                var opt2 = document.createElement('option');
                opt2.value = "";
                opt2.innerHTML = "...";
                selectLabs.appendChild(opt2);
                return;
            }


        }
    });

    $('#load-pis').click(function (){
        var selectedLab = $('#pi-lab-results option').filter(':selected')[0].value;
        if (selectedLab != "..."){
            doLabAjaxCall(selectedLab);
        }

    })
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

