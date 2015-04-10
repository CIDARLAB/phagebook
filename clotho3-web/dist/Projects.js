
    $("#CreateButton").click(function(){
        var id = document.getElementById("rightColumn");
        var secondID = document.getElementById("createProject");
        id.style.display='none';
        secondID.style.display = 'block';
    });


    $("#projTable").click(function() {
        Clotho.query("projName", this.innerText).then(function (data) {
            document.getElementById("currentLab").innerHTML = data.projLabs;
            document.getElementById("currentPI").innerHTML = data.pi;
        });
    });



    var personID = "clotho.developer.katielewis1";

    $("#create").click(function() {
        var str = '{';
        //alert("hello world!");
        //alert(document.getElementById("projName").value);
        var firstObj = str.concat('"schema":', '"org.clothocad.model.Projects"', ',"name":', '"', document.getElementById("projName").value, '"',',"projectLead":', '"', document.getElementById("projLead").value, '"');
        var obj = firstObj.concat(',"Lab":','"',document.getElementById("projLabs").value, '"', ',"PI":', '"', document.getElementById("pi").value, '"', ',"Description":', '"', document.getElementById("description").value,'"',',"advisor":','"',document.getElementById("advisor").value,'"', '}');
//            alert(obj);
        var jsonObj = JSON.parse(obj);
        Clotho.create(jsonObj).then(function(projectID){
            Clotho.get(personID).then(function(personObj){
                personObj.projects[personID.projects.length] = projectID;
                Clotho.set(personObj).then(function(ID){
                    alert(ID);
                });
            });
            //var tableRef = document.getElementById("projTable");
            //var newRow = tableRef.insertRow(tableRef.rows.length);
            //var newCell = newRow.insertCell(0);
            //newCell.innerHTML = '<a href="./Projects.html" style = "display:block">' + document.getElementById("projName").value + '</a>';
            //var secondID = document.getElementById("createProject");
            //secondID.style.display = 'none';
            //var thirdID = document.getElementById("newProjectMotif");
            //thirdID.style.display = 'block';
        });
    });
