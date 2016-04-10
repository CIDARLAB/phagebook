$(document).ready(function() {

	// after we get the queried project, we edit the html 
	// {"creator":"Anna Goncharova","dateCreated":"Apr 09 2016",
	// "description":"This is a project description for an awesome project named \"Project\"!",
	// "projectName":"Project","updates":[],"grant":"5709a9bad4c60ab7f5242f02",
	// "lead":"Anna Goncharova","budget":12345}
	var editHtml = function(data){
		console.log("in editHtml");
		data = JSON.parse(data)

		$(".project-dateCreated-content").text(data["dateCreated"]);
		$(".creator-name ").text(data["creator"]);
		$(".lead-name").text(data["lead"]);


		$(".project-people-person-lead-lab").text(data["leadLabs"])
		$(".project-people-person-creator-lab").text(data["creatorLabs"])
		$(".project-description-content").text(data["description"]);
		$(".project-grant-content").val(data["grantName"]);
		$(".project-affiliatedLabs-content").val(data["affiliatedLabs"]);
	}

    alert("hello!")
    var params = location.search;

    var qs = (function(a) {
        if (a == "") return {};
        var b = {};
        for (var i = 0; i < a.length; ++i) {
            var p = a[i].split('=', 2);
            if (p.length == 1)
                b[p[0]] = "";
            else
                b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
        }
        return b;
    })(window.location.search.substr(1).split('&'));

    var id = qs["id"];
    console.log(id);

    $.ajax({
        //do this for projects...
        url: "../getProject",
        type: "POST",
        async: false,
        data: {
            "projectID": id
        },
        success: function(response) {
        	  console.log("response!")
            console.log(response);
            return editHtml(response);
        },
        error: function(response) {
            console.log("project quering failed!");
        }
    })

});