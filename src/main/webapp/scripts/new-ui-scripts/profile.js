$(document).ready(function () {
    $("#add-pubmed-btn").click(function () {
        var PMID = $("#pubmed-id-input").val();
        var createdBy = getCookie("clothoId");
        //alert(PMID);

        $.ajax({
            url: "../addPubMed",
            type: "POST",
            async: false,
            data: {
                "createdBy": createdBy,
                "PMID": PMID
            },
            success: function (response) {
               alert("The PubMed publication has been added");
               alert(response);
            },
            error: function (response) {
                alert("An error occurred with adding a publication.");
            }
        });
    });
    
    $("#add-doi-btn").click(function () {
        var DOI = $("#doi-input").val();
        var createdBy = getCookie("clothoId");
        $.ajax({
            url: "../addCrossRef",
            type: "POST",
            async: false,
            data: {
                "createdBy": createdBy,
                "DOI": DOI
            },
            success: function (response) {
               alert("The CrossRef publication has been added");
               alert(response);
            },
            error: function (response) {
                alert("An error occurred with adding a publication.");
            }
        });
    });
    
    $("#add-custom-pub-btn").click(function () {
        var title = $("#pub-title").val();
        var authors = $("#pub-authors").val();
        var year = $("#pub-year").val();
        var info = $("#pub-info").val();
        var createdBy = getCookie("clothoId");
        $.ajax({
            url: "../addCustom",
            type: "POST",
            async: false,
            data: {
                "createdBy": createdBy,
                "title": title,
                "authors": authors,
                "year": year,
                "info": info
            },
            success: function (response) {
               alert("The custom publication has been added");
               alert(response);
            },
            error: function (response) {
                alert("An error occurred with adding a publication.");
            }
        });
    });
    
    
});

