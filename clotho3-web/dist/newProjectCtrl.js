angular.module('NewProject').controller('newProjectCtrl', function($scope, Clotho, projectAPI, PubSub){

this.createProject = function(){
	projectAPI.create($scope.project).then(function(){

	});
};


});