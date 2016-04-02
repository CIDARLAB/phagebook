$(document).ready(function() {

	var userID = getCookie("clothoID");
	var projectID = getCookie("projectId");

	var data = {
	    "userID": userID,
	}

	$.ajax({
		url: "/getAllProjects",
		type: "POST",
		dataType: "json",
		data: data,
		success: function(response) {
		  console.log(response);
		  console.log("response!!!");
		},
		error: function(err) {
		  console.log("ERROR!!");
		  console.log(err);
		}
	});

});