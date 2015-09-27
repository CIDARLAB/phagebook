var newProject = angular.module('newProject', []);
newProject.controller('createProjCtrl', function($scope, Clotho){

    this.createProject = function()
    {
        var str = '{';
        str.concat('""id"":', 'clotho.delevloper.', projname.toLowerCase(), '"",schema"":', '""org.clothocad.model.Projects""', ',""name"":', $scope.projname, ',""PI"":', pi, ',""Description"":', description, '}');
        var obj = JSON.parse(projectParams.obj);
        Clotho.create(obj);

    };
});