angular.module('NewProject').service('projectAPI', function(Clotho){

	this.create = function(projectParams){
		var str;
		str.concat('{','""id"":','clotho.delevloper.',projectParams.name.toLowerCase(),'"",schema"":','""org.clothocad.model.Projects""',',""name"":', projectParams.name, ',""PI"":',projectParams.PI, ',""Description"":', projectParams.Description, '}');
		var obj = JSON.parse(str);
		Clotho.create(obj);
	};
});