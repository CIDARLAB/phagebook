angular.module('Profile').controller('profileCtrl', function ($scope, Clotho, basicAPI, TrackingObj, PubSub) {



    this.loadProfile = function () {

        alert("load Profile")
        //var x = document.createElement("INPUT");
       // x.setAttribute("type", "file");
        var personID = window.name;
        var statusObj;
        var notNullArray = [];
        Clotho.get(personID).then(function (personObj) {


            document.getElementById("name").innerHTML = personObj.firstname + " " + personObj.lastname;
            document.getElementById("universityName").innerHTML = personObj.institution;
            document.getElementById("positionName").innerHTML = personObj.position;


            Clotho.get(personObj.statuses).then(function (statusObj) {
                    if (personObj.statuses.length == null) {
                    }
                    else {
                        for (var i = statusObj.length - 1; i >= 0; i--) {
                            if (statusObj[i] == null) {
                                personObj.statuses[i] = [];
                            }
                            else {

                                notNullArray.push(i);
                                var paperCite = '';
                                var newStatus = statusObj[i];
                                $('#keyValueFormProfile').append("<tr style='margin-top: 10px;'> <td>" + "<p>" + newStatus.text + "</p></td> <td>" + newStatus.time + "</td> < td > < button onclick = 'deletePaper(" + JSON.stringify(statusObj) + ") ' class= 'btn btn-primary'" + "> x </button></td></tr>");
                            }
                        }
                    }
                }
            );
        });
    };


    this.imageupload = function () {
        var tgt = evt.target || window.event.srcElement,
            files = tgt.files;

        // FileReader support
        if (FileReader && files && files.length) {
            var fr = new FileReader();
            fr.onload = function () {
                //alert("ping");
                document.getElementById('imagesource').src = fr.result;
            }
            fr.readAsDataURL(files[0]);
        }

        // Not supported
        else {

            // fallback -- perhaps submit the input to an iframe and temporarily store
            // them on the server until the user's session ends.
        }
    };


    this.createStatus = function () {
        alert("create status");
        //alert(window.name);
        var personID = window.name;
        timeStamp = new Date();
        //timeStamp = Date.now();
        //alert(timeStamp)
        // var StatusObj = str.concat('"schema":', '"org.clothocad.phagebook.status"');//,', "entryDate":', '",document.getElementById("statusText").value' );
        var StatusObj = "{ \"schema\": \"org.clothocad.phagebook.status\", \"time\" :\"" + timeStamp + "\" , \"text\": \"" + document.getElementById("newStatus").value + "\" , \"person\": \"" + personID + "\" }";
        //alert(StatusObj);
        var obj = JSON.parse(StatusObj);
        // alert(obj);
        // alert(StatusObj);
        Clotho.create(obj).then(function (result) {
            Clotho.get(personID).then(function (personObj) {
                personObj.statuses.push(result);
                Clotho.set(personObj);
            })
        });
    };
});