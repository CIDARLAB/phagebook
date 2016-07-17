/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function(){
    $('#loginUser').click(function () {

        $.ajax({
            url: "loginUser",
            type: "POST",
            data: {
                "email": "phagebook",
                "password": "backend"
            },

            success: function (response) {
                $("#loginUser").css("background-color", "green");

                setCookie("clothoId", response.clothoId, 1);
                setCookie("emailId", response.emailId, 1);
            },

            error: function() {
                $("#loginUser").css("background-color", "red");
            }
        });
    });
    
    
    
    //Colleague Controller
    $('#addColleagueRequest').click(function () {

        $.ajax({
            url: "addColleagueRequest",
            type: "POST",
            data: {
                "colleagueClothoId": this.value,
                "loggedInClothoId": getCookie("clothoId")
            },

            success: function () {
                $("#addColleagueRequest").css("background-color", "green");
            },

            error: function() {
                $("#addColleagueRequest").css("background-color", "red");
            }
        });
    });
    
    $('#approveColleagueRequest').click(function () {

        $.ajax({
            url: "approveColleagueRequest",
            type: "POST",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
                "colleagueId": this.value
            },

            success: function () {
                $("#approveColleagueRequest").css("background-color", "green");
            },

            error: function() {
                $("#approveColleagueRequest").css("background-color", "red");
            }
        });
    });
    
    $('#denyColleagueRequest').click(function () {

        $.ajax({
            url: "denyColleagueRequest",
            type: "POST",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
                "colleagueId": this.value
            },

            success: function () {
                $("#denyColleagueRequest").css("background-color", "green");
            },

            error: function() {
                $("#denyColleagueRequest").css("background-color", "red");
            }
        });
    });
    
    $('#listColleagueRequests').click(function () {

        $.ajax({
            url: "listColleagueRequests",
            type: "GET",
            async: false,
            data: {
                "userId": getCookie("clothoId")
            },

            success: function () {
                $("#listColleagueRequests").css("background-color", "green");
                
                var alrt = colleagueJSON.firstName + " " + colleagueJSON.lastName;
                alert(alrt);
            },

            error: function() {
                $("#listColleagueRequests").css("background-color", "red");
            }
        });
    });
    
    $('#loadColleagues').click(function () {

        $.ajax({
            url: "loadColleagues",
            type: "GET",
            async: false,
            data: {
                "userId": getCookie("clothoId"),
            },

            success: function (response) {
                
                
                var ul = $("#colleagues-full-list");
                ul.empty();
                console.log(JSON.stringify(response));
                for (var i = 0; i < response.length; i++) {
                    var tmpl = document.getElementById("colleague-display-template").content.cloneNode(true);
                    // var othertmpl = document.getElementById("colleague-page-template").content.cloneNode(true);
                    tmpl.querySelector(".colleague-page-picLink").src = "http://s3.amazonaws.com/phagebookaws/" + response[i].clothoId + "/profilePicture.jpg";
                    tmpl.querySelector(".colleague-page-picLink").alt = response[i].fullname;
                    tmpl.querySelector(".colleague-display-fullname").text = response[i].fullname;
                    tmpl.querySelector(".colleague-display-lab").innerHTML = (response[i].labName == null) ? "" : response[i].labName;
                    tmpl.querySelector(".colleague-display-institution").innerHTML = response[i].institutionName;
                    tmpl.querySelector(".colleague-display-fullname").href = "html/colleague.html?user=" + response[i].clothoId;
                    ul.append(tmpl);
                }
                
                $("#loadColleagues").css("background-color", "green");
            },

            error: function() {
                $("#loadColleagues").css("background-color", "red");
            }
        });
    });
    
    
    //Lab Controller
    $('#addPIToLab').click(function () {

        $.ajax({
            url: "addPIToLab",
            type: "POST",
            async: false,
            
            data: {
                "lab": labId,
                "userId": peopleBoxes[i].value
            },

            success: function () {
                $("#addPIToLab").css("background-color", "green");
            },

            error: function() {
                $("#addPIToLab").css("background-color", "red");
            }
        });
    });

    $('#listPIsOfLab').click(function () {

        $.ajax({
            url: "listPIsOfLab",
            type: "GET",
            async: false,
            data: {
                "lab": labId
            },

            success: function (response) {
                $("#listPIsOfLab").css("background-color", "green");
            },

            error: function() {
                $("#listPIsOfLab").css("background-color", "red");
            }
        });
    });

    $('#removePIFromLab').click(function () {

        $.ajax({
            url: "removePIFromLab",
            type: "POST",
            async: false,
            data: {
                "lab": labId,
                "userId": peopleBoxes[i].value
            },

            success: function (response) {
                alert(response.message);
                $("#removePIFromLab").css("background-color", "green");
            },

            error: function() {
                $("#removePIFromLab").css("background-color", "red");
            }
        });
    });
    
    $('#createLab').click(function () {

        var name = "Lab1";
        var description = "lab desc";
        var phone = "000";
        var url = "";
        var institutionId = $("#create-lab-institution").val();
        $.ajax({
            //do this for projects...
            url: "createLab",
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
                $("#createLab").css("background-color", "green");
            },

            error: function() {
                $("#createLab").css("background-color", "red");
            }
        });
    });
    
    
    //Product Controller
    $('#addProducts').click(function () {

        $.ajax({
            url: "addProducts",
            type: "GET",
            async: false,
            data: {
                "CompanyName":"comp"
            },

            success: function () {
                $("#addProducts").css("background-color", "green");
            },

            error: function() {
                $("#addProducts").css("background-color", "red");
            }
        });
    });
    
    $('#uploadProductCSV').click(function () {

        $.ajax({
            url: "uploadProductCSV",
            type: "GET",
            async: false,
            data: {
                "jsonArray": JSON.stringify(results.data)
            },

            success: function (response) {
                $("#uploadProductCSV").css("background-color", "green");
            },

            error: function() {
                $("#uploadProductCSV").css("background-color", "red");
            }
        });
    });
    
    $('#createProduct').click(function () {

        $.ajax({
            url: "createProduct",
            type: "POST",
            async: false,
            data: {
                "productUrl": productUrl,
                "company": company,
                "goodType": goodType,
                "cost": cost,
                "quantity": quantity,
                "name": name,
                "description": description
            },

            success: function (response) {
                alert("Product created\n" +
                        "Named: " + response.name + '\n' +
                        "id " + response.id);

                $("#createProduct").css("background-color", "green");
            },

            error: function() {
                $("#createProduct").css("background-color", "red");
            }
        });
    });
    
    $('#getProductById').click(function () {
        var idList = getCookie("Order");
        
        $.ajax({
            url: "getProductById",
            type: "GET",
            async: false,
            data: {
                'ids': idList
            },

            success: function (response) {
                $("#getProductById").css("background-color", "green");
            },

            error: function() {
                $("#getProductById").css("background-color", "red");
            }
        });
    });
    
    $('#queryProductByCompany').click(function () {

        $.ajax({
            url: "queryProductByCompany",
            type: "GET",
            async: false,
            data: {
                'Name': companyQueryName
            },

            success: function (response) {
                alert(document.getElementById("productTable"));
                $("#queryProductByCompany").css("background-color", "green");
            },

            error: function() {
                $("#queryProductByCompany").css("background-color", "red");
            }
        });
    });
    
    $('#queryProductByName').click(function () {

        $.ajax({
            url: "queryProductByName",
            type: "GET",
            async: false,
            data: {
                 'Name': productQueryName
            },

            success: function (response) {
                $("#queryProductByName").css("background-color", "green");
            },

            error: function() {
                $("#queryProductByName").css("background-color", "red");
            }
        });
    });
    
    
    
    //Order Controller
//    $('#queryProductByName').click(function () {
//
//        $.ajax({
//            url: "queryProductByName",
//            type: "GET",
//            async: false,
//            data: {
//                
//            },
//
//            success: function (response) {
//                $("#queryProductByName").css("background-color", "green");
//            },
//
//            error: function() {
//                $("#queryProductByName").css("background-color", "red");
//            }
//        });
//    });

    $('#loadPhagebookInstitutions').click(function () {
        $.ajax({
            url: "loadPhagebookInstitutions",
            type: "GET",
            async: false,
            data: {},
            success: function (response) {
                var responseArray = response.institutions; //array of JSONObjects with labs attached 

                var selectInstitution = document.getElementById('institution');

                sessionStorage.setItem("index-institutions", JSON.stringify(responseArray)); // stores in sess stor

                var opt = document.createElement('option');
                opt.value = "";

                opt.innerHTML = "Institution...";

                var numberOfInstitutions = responseArray.length;

                $("#loadPhagebookInstitutions").css("background-color", "green");
            },
            error: function (response) {
                alert("No Institutions To Load");
                $("#loadPhagebookInstitutions").css("background-color", "red");
            }
        });
    });
});
    
    
    
    
    
    
