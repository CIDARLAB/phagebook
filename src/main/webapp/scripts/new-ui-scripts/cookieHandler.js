/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function setCookie(cname, cvalue, exdays)
{
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    var path = ";path=/";
    document.cookie = cname + "=" + cvalue + "; " + expires + path;
}
function getCookie(cname) {
    //GET A DAMN COOKIE VALUE
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ')
            c = c.substring(1);
        if (c.indexOf(name) == 0)
            return c.substring(name.length, c.length);
    }
    return "";
    //
}

function clearCredentialsFromCookie(){

    setCookie("clothoId", "", 1);
    setCookie("emailId", "" , 1);
}
function appendToCookie(cname, cvalue, exdays) {


    var currentCookie = getCookie(cname);


    var updatedCookie = "";
    if (currentCookie == "") {
        updatedCookie = cvalue;
    } else if (currentCookie != "") {
        updatedCookie = currentCookie + ',' + cvalue;
    }
    setCookie("Order", updatedCookie, exdays);
}

function delete_cookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getParameterByName(name)
{
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}