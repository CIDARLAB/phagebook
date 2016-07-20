$(document).ready(documentReady);

function documentReady() {
    $("#success-lab-alert").hide();

    //$("#remove-pi-btn").click(removePIBtnHandler);

    $("#search-colleagues-btn").click(searchBtnHandler);
    $("#add-to-lab-btn").click(addPIBtnHandler);

    $.ajax({
        //do this for projects...
        url: "../loadPhagebookInstitutions",
        type: "GET",
        async: false,
        data: {
        },
        success: loadPhagebookInstitutions,
        error: function (response) {

        }
    });

    $('.institution-drop-down').change(function () {


        var selectedInstitution = this.options[this.selectedIndex].innerHTML;

        var selectInstitutions = document.getElementsByClassName('institution-drop-down');

        for (var select = 0; select < selectInstitutions.length; select++) {
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
                for (var i2 = 0; i2 < selectsLabs.length; i2++) {


                    removeOptions(selectsLabs[i2]);

                    for (var j = 0; j < labsLength; j++) {

                        var opt2 = document.createElement('option');
                        opt2.value = labsArray[j].labId;

                        opt2.innerHTML = labsArray[j].labName;
                        selectsLabs[i2].appendChild(opt2);
                    }
                }


            else if (selectedInstitution == "...") {


                for (var i3 = 0; i3 < selectsLabs.length; i3++) {
                    removeOptions(selectsLabs[i3]);
                    var opt2 = document.createElement('option');
                    opt2.value = "";
                    opt2.innerHTML = "...";
                    selectsLabs[i3].appendChild(opt2);

                }
            }



        }
    });

    $('#load-pis').click(function () {
        var selectedLab = $('#pi-lab-results option').filter(':selected')[0].value;
        if (selectedLab != "...") {
            doLabAjaxCall(selectedLab);
        }

    });

    $("#submit-lab-btn").click(function () {

        //WHY IS THIS AN ARRAY .. JQUERY EXPLAIN
        var name = document.getElementById("lab-name").value;
        var description = document.getElementById("lab-description").value;
        var phone = document.getElementById("lab-phone").value;
        var url = document.getElementById("lab-website").value;
        var institutionId = $("#create-lab-institution").val();
        $.ajax({
            //do this for projects...
            url: "../createLab",
            type: "POST",
            async: false,
            data: {
                "user": getCookie("clothoId"),
                "name": name,
                "description": description,
                "phone": phone,
                "url": url,
                "institution": institutionId


            },
            success: function (response) {
                $("#success-lab-alert").fadeIn();
                $.ajax({
                    //do this for projects...
                    url: "../loadPhagebookInstitutions",
                    type: "GET",
                    async: false,
                    data: {
                    },
                    success: loadPhagebookInstitutions,
                    error: function (response) {

                    }
                });

            },
            error: function (response) {

            }

        })
    });
    $("#go-btn").click(goBtnHandler);
}


function removeOptions(selectbox)
{
    var i;

    for (i = selectbox.options.length - 1; i >= 0; i--)
    {
        selectbox.remove(i);
    }
}

function doLabAjaxCall(labId) {
    $.ajax({
        //do this for projects...

        url: "../listPIsOfLab",
        type: "GET",
        async: false,
        data: {
            "lab": labId
        },
        success: function (response) {



            for (var i = 0; i < response.length; i++) {
                personResultRectCreate(personJSON);
            }

        },
        error: function (response) {

        }
    });
}

function loadPhagebookInstitutions(response) {
    // POSSIBLE WAY TO GET THE RESPONSE AGAIN?
    //sessionStorage.setItem("institutions", JSON.stringify(response));
    var selects = document.getElementsByClassName('institution-drop-down');
    sessionStorage.setItem("accountSettings-institutions", JSON.stringify(response.institutions));
    var length = response.institutions.length; //has institutions and labs
    for (var k = 0; k < selects.length; k++) {

        removeOptions(selects[k]);

        var opt = document.createElement('option');
        opt.value = "";
        opt.innerHTML = "...";
        selects[k].appendChild(opt);
        for (var i = 0; i < length; i++) {

            opt = document.createElement('option');
            opt.value = response.institutions[i].institutionId;

            opt.innerHTML = response.institutions[i].institutionName;
            selects[k].appendChild(opt);


        }
    }

}

function searchBtnHandler() {
    var firstName = $("#search-first-name").val();
    var lastName = $("#search-last-name").val();


    $.ajax({
        url: '../queryFirstLastName',
        type: 'GET',
        async: false,
        data: {
            "firstName": firstName,
            "lastName": lastName
        },
        success: function (response) {


            var ul = $("#pi-add-list");

            ul.empty();


            for (var i = 0; i < response.length; i++) {
                var tmpl = document.getElementById('person-results-template').content.cloneNode(true);

                tmpl.querySelector('.pi-profile-pic').src = "http://s3.amazonaws.com/phagebookaws/" + response[i].clothoId + "/profilePicture.jpg";
                tmpl.querySelector('.pi-name').innerText = response[i].fullname;
                tmpl.querySelector('.pi-lab-name').innerText = (response[i].labName == null) ? "" : response[i].labName;
                tmpl.querySelector('.pi-institution-name').innerText = response[i].institutionName;

                tmpl.querySelector('.pi-profile-link').href = "html/colleague.html?user=" + response[i].clothoId;
                tmpl.querySelector('.pi-id').value = response[i].clothoId;


                ul.append(tmpl);
            }
        },
        error: {
        }

    });
    return false;


}

function goBtnHandler() {
    var dropdown = document.getElementById("remove-pi-container").querySelector(".lab-drop-down");
    var labId = dropdown.options[dropdown.selectedIndex].value;
    var content = $("#pi-remove-list");
    content.empty();
    event.preventDefault();



    $.ajax({
        //do this for projects...

        url: "../listPIsOfLab",
        type: "GET",
        async: false,
        data: {
            "lab": labId
        },
        success: function (response) {


            for (var i = 0; i < response.length; i++) {
                var name = response[i].name;
                var email = response[i].email;
                var clothoId = response[i].clothoId;

                personResultRectCreate(response[i]);



            }

        },
        error: function (response) {

        }
    });
    return false;


}


function personResultRectCreate(personJSON) {
    var content = $("#pi-remove-list");
    var tmpl = document.getElementById('person-results-template').content.cloneNode(true);
    tmpl.querySelector('.pi-profile-pic').src = "http://s3.amazonaws.com/phagebookaws/" + personJSON.clothoId + "/profilePicture.jpg";

    tmpl.querySelector('.pi-name').innerText = personJSON.name;
    tmpl.querySelector('.pi-lab-name').innerText = (personJSON.labName == null) ? "" : personJSON.labName;
    tmpl.querySelector('.pi-institution-name').innerText = personJSON.institutionName;

    tmpl.querySelector('.pi-profile-link').href = "html/colleague.html?user=" + personJSON.clothoId;
    tmpl.querySelector('.pi-id').value = personJSON.clothoId;

    content.append(tmpl);


}

function removePIBtnHandler() {
    var container = document.getElementById("pi-remove-results");
    var peopleBoxes = container.getElementsByClassName("pi-id");

    var dropdown = container.parentNode.querySelector(".lab-drop-down");
    var labId = dropdown.options[dropdown.selectedIndex].value;
    event.preventDefault();

    for (var i = 0; i < peopleBoxes.length; i++) {


        if (peopleBoxes[i].checked) {
            $.ajax({
                //do this for projects...

                url: "../removePIFromLab",
                type: "POST",
                async: false,
                data: {
                    "lab": labId,
                    "userId": peopleBoxes[i].value
                },
                success: function (response) {
                    alert(response.message);

                    goBtnHandler();

                },
                error: function (response) {

                }
            });



        }


    }


}

function addPIBtnHandler() {
    var container = document.getElementById("pi-add-results");
    var peopleBoxes = container.getElementsByClassName("pi-id");

    var dropdown = this.parentNode.querySelector('.lab-drop-down');
    var labId = dropdown.options[dropdown.selectedIndex].value;
    event.preventDefault();



    for (var i = 0; i < peopleBoxes.length; i++) {


        if (peopleBoxes[i].checked) {



            $.ajax({
                //do this for projects...

                url: "../addPIToLab",
                type: "POST",
                async: false,
                data: {
                    "lab": labId,
                    "userId": peopleBoxes[i].value
                },
                success: function (response) {
                    alert(response.message);

                },
                error: function (response) {

                }
            });

        }

    }

}
