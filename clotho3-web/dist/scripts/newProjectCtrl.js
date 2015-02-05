angular.module('NewProject').controller('newProjectCtrl', function($scope, Clotho, projectAPI){

this.createProject = function(){
	projectAPI.create($scope.project).then(function(){

	});
};


});