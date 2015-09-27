//Creates JSON objects from a create new project form and stores it in the Clotho database
var personID = "clotho.developer.katielewis1";
$(document).ready(function() {
    $("#create").click(function () {
        var str = '{';
        var firstObj = str.concat('"schema":', '"org.clothocad.model.Projects"', ',"name":', '"', document.getElementById("projName").value, '"', ',"projectLead":', '"', document.getElementById("projLead").value, '"');
        var obj = firstObj.concat(',"Lab":', '"', document.getElementById("projLabs").value, '"', ',"PI":', '"', document.getElementById("pi").value, '"', ',"Description":', '"', document.getElementById("description").value, '"', ',"advisor":', '"', document.getElementById("advisor").value, '"', '}');
        var jsonObj = JSON.parse(obj);
        Clotho.create(jsonObj).then(function (result) {
            Clotho.get(personID).then(function (personObj) {
                personObj.projects[personObj.projects.length] = result;
                Clotho.set(personObj).then(function () {
                    Clotho.get(personObj.projects[personObj.projects.length -1]).then(function(projectObj){
                        var tableRef = document.getElementById("projTable");
                        var newRow = tableRef.insertRow(tableRef.rows.length);
                        var newCell = newRow.insertCell(0);
                        newCell.innerHTML = '<a href="./Projects.html" style = "display:block"" onclick = "projectDetails.call(this)">' + projectObj.name + '</a>';
                        alert(projectObj.id);
                        alert(newCell.id);
                        var id = document.getElementById("rightColumn");
                        var secondID = document.getElementById("createProject");
                        id.style.display = 'block';
                        secondID.style.display = 'none';
                        document.getElementById("projTitle").innerHTML = projectObj.name;
                        document.getElementById("currentLab").innerHTML = projectObj.Lab;
                        document.getElementById("currentLead").innerHTML = projectObj.projectLead;
                        document.getElementById("currentPI").innerHTML = projectObj.PI;
                        document.getElementById("currentAdvisor").innerHTML = projectObj.advisor;
                        document.getElementById("projectDescription").innerHTML = projectObj.Description;

                    });




                });
            });

        });
    });
});

/*When a user goes to the projects section of Phagebook, the first project will automatically be displayed.
 If the user doesn't have any projects yet, a create new project form will be displayed
 The left column of the page will have a list of all the projects associated with that user. Clicking on
 one of the tabs will display the details of that project and the user's project notebook (different function)*/
function displayProjects() {
    var personID = "clotho.developer.katielewis1";
    Clotho.get(personID).then(function (personObj) {
        var tableRef = document.getElementById("projTable");
        if (personObj.projects.length > 0) {
            var newRow = tableRef.insertRow(0);
            var newCell = newRow.insertCell(0);
            Clotho.get(personObj.projects[0]).then(function (proj) {
                newCell.innerHTML = '<a href="./Projects.html" style = "display:block"+ value =' + proj.id + ' onclick = "projectDetails.call(this)">' + proj.name + '</a>';
            });
            for (var i = 1; i < personObj.projects.length; i++) {
                Clotho.get(personObj.projects[i]).then(function (projectObj) {
                    newRow = tableRef.insertRow(tableRef.rows.length);
                    newCell = newRow.insertCell(0);
                    newCell.innerHTML = '<a href="./Projects.html" style = "display:block" value =' + projectObj.id + ' onclick = "projectDetails.call(this)">' + projectObj.name + '</a>';
                });

            }

            Clotho.get(personObj.projects[0]).then(function (projObj) {
                document.getElementById("projTitle").innerHTML = projObj.name;
                document.getElementById("currentLab").innerHTML = projObj.Lab;
                document.getElementById("currentLead").innerHTML = projObj.projectLead;
                document.getElementById("currentPI").innerHTML = projObj.PI;
                document.getElementById("currentAdvisor").innerHTML = projObj.advisor;
                document.getElementById("projectDescription").innerHTML = projObj.Description;
            });
        }
        else if(personObj.projects.length == 0){
            var id = document.getElementById("rightColumn");
            var secondID = document.getElementById("createProject");
            var thirdID = document.getElementById("notebookEntry");
            thirdID.style.display = 'none';
            id.style.display='none';
            secondID.style.display = 'block';
        }


    });
};

//Takes inputs from a "create new notebook entry" form and creates a JSON object for that entry. It is stored in Clotho.
function createEntry(){
    var personID = "clotho.developer.katielewis1";
    var obj = {"schema":"org.clothocad.model.Notebook","entryData":document.getElementById("entryDate").value,"entryText":document.getElementById("textEntry").value, "entryPics":[]};
    Clotho.get(personID).then(function (personObj) {
        Clotho.create(obj).then(function (result) {
            personObj.notebookEntries[personObj.notebookEntries.length] = result;
            Clotho.set(personObj);

        });
    });

    var id = document.getElementById("rightColumn");
    var secondID = document.getElementById("createProject");
    var thirdID = document.getElementById("notebookEntry");
    id.style.display = 'block';
    secondID.style.display = 'none';
    thirdID.style.display = 'none';
};

//Displays the next project notebook entry
function nextEntry(){
    personID = "clotho.developer.katielewis1";
    var count = 0;
    var current = document.getElementById("entryGoesHere").innerText;
    Clotho.get(personID).then(function(result){
        for(var i=0;i < result.notebookEntries.length;i++){
            Clotho.get(result.notebookEntries[i]).then(function(res){
                if(current == res.entryText && res.id != result.notebookEntries[result.notebookEntries.length-1]){
                    Clotho.get(result.notebookEntries[1+count]).then(function(re){
                        document.getElementById("entryGoesHere").innerText = re.entryText;
                    });
                }
                count++;
            });
        };
    });
};

//Displays the previous notebook entry
function prevEntry(){
    personID = "clotho.developer.katielewis1";
    var count = 0;
    var current = document.getElementById("entryGoesHere").innerText;
    Clotho.get(personID).then(function(result){
        for(var i=0;i < result.notebookEntries.length;i++){
            Clotho.get(result.notebookEntries[i]).then(function(res){
                if(current == res.entryText && res.id != result.notebookEntries[0]){
                    Clotho.get(result.notebookEntries[count-1]).then(function(re){
                        document.getElementById("entryGoesHere").innerText = re.entryText;
                    });
                }
                count++;
            });
        };
    });

};

//Allows items to be dragged and dropped
function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("image", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("image");
    ev.target.appendChild(document.getElementById(data));
}

