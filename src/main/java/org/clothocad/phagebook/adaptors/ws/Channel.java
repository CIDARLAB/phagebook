/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.phagebook.adaptors.ws;

/**
 *
 * @author KatieLewis
 */
public enum Channel {
    /*
    createStatus (show on project newsfeed & profile?),
sendResult,
getData?,
send part/sample,
send protocol,
add to notebook entry?
    */
    createStatus,
    updateOrderStatus,
    addToOrder,
    createProject,
    addFriend,
    addTeamMember,
    login,
    sendResult,
    sendSample,
    sendProtocol,
    addEntry
}
