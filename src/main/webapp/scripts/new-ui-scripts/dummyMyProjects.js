// make a dummy project here
$(document).ready(function() {

    $("#bang").click(function() {
        makeDummy();
        //console.log(id);
    });
    
    $("#editProject").click(function() {
        makeDummy();
        //console.log(id);
    });
    
    $("#addUpdate").click(function() {
    	console.log("clicked on addUpdate")
      console.log( $("#projectID").text() );
      var arrOfIds =  $("#projectID").text().split(" ");
      console.log(arrOfIds);
      var projectId = arrOfIds[0];
      var userId = arrOfIds[1];
      var newStatus = $("#newStatus").val();
      console.log(newStatus);
      var emailPeople = document.getElementById("emailPeople").checked;
      console.log(emailPeople);
      if(projectId != null && userId !=null && newStatus != null){
      	 addUpdate(projectId, userId, newStatus, emailPeople, emailPeople);
      }else{
      	//alert("Something is missing!");
      }
     
    });

    var addUpdate = function(projectID, userID, newStatus, emailPeople) {
      	
        var data = {
            "userID": userID,
            "projectID": projectID,
            "newStatus": newStatus,
            "emailPeople": emailPeople
        }

        $.ajax({
            url: "/addUpdateToProject",
            type: "POST",
            dataType: "json",
            data: data,
            success: function(response) {
                //console.log(dataSubmit);
                console.log(response);
                alert(response.updates);
                console.log("response!!!");
            },
            error: function(err) {
                console.log("ERROR!!");
                console.log(err);
            }
        });
    }


    var makeDummy = function() {
        console.log("in makeDummy");
        var name = "Phagebook";
        var leadFirstName = "Awesome";
        var leadLastName = "Ugrads"
        var leadEmailId = "agonchar@bu.edu"
        var labs = "CIDAR";
        var projectBudget = 100;
        var grant = "Grant"
        var desc = "This is Phagebook.";
        var date = new Date("October 13, 2014 11:13:00");

        console.log("in addStatus function!!");
        var newStatus = "";

        if ($("#status").val() != null) {
            newStatus = $("#status").val();
        };

        console.log(newStatus);
        // how new projects should be passed in
        var data = {
            name: name,
            leadFirstName: leadFirstName,
            leadLastName: leadLastName,
            labs: labs,
            leadEmailId: leadEmailId,
            projectBudget: projectBudget,
            grant: grant,
            description: desc,
            date: date
        };

        $.ajax({
            url: "/processProject",
            type: "POST",
            dataType: "json",
            data: data,
            async:true,
            success: function(response) {
                console.log(response);
                projectID = response.projectID;
                userID = response.userID
                $('#projectID').html(projectID+" "+userID);
                return edit(projectID, userID);
            },
            error: function(err) {
                console.log("ERROR!!");
                console.log(err);
            }
        });
    }

    var edit = function(projectID, userID) {
        console.log("in edit function!!");
        var newName = "";
        var newDesc = "";
        var newBudget = "";

        if ($("#name").val() != null) {
            newName = $("#name").val()
        };
        if ($("#desc").val() != null) {
            newDesc = $("#desc").val();
        };
        if ($("#budget").val() != null) {
            newBudget = $("#budget").val();
        };
        console.log(projectID);
        console.log(userID);
        console.log(newDesc);
        console.log(newName);
        console.log(newBudget);
        var data = {
            "userID": userID,
            "projectID": projectID,
            "description": newDesc,
            "name": newName,
            "budget": newBudget

        }

        $.ajax({
            url: "/editProject",
            type: "POST",
            dataType: "json",
            data: data,
            success: function(response) {
                //console.log(dataSubmit);
                console.log(response);
                console.log("response!!!");
            },
            error: function(err) {
                console.log("ERROR!!");
                console.log(err);
            }
        });
    }
});