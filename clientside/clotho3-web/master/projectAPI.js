angular.module('NewProject').service('projectAPI', function(Clotho){

	this.create = function(projectParams){
		var str;
		str.concat('{','""id"":','clotho.delevloper.',projectParams.name.toLowerCase(),'"",schema"":','""org.clothocad.model.Projects""',',""name"":', projectParams.name, ',""PI"":',projectParams.PI, ',""Description"":', projectParams.description, '}');
		obj = JSON.parse(str);
		alert(str);
		Clotho.create(obj);
	};
});