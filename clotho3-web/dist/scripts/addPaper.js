/**
 * Created by karalefort on 3/3/15.
 */
angular.module('Profile').controller('paperCtrl', function ($scope, Clotho, basicAPI, paperObj, personId, papers, paperId) {


    this.createObj = function() {
        //alert('paper object created');
        Clotho.create(paperObj).then(function (paperId) {
            Clotho.get("personId")
            then(function (personObj) {
                personObj.papers[personObj.papers.length] = paperObj;
                Clotho.set(personObj)
            })

        })

    }

})