angular.module('Ordering', ['clotho.foundation'])
.run(function () {
	console.log('running');
});

//we need to manually bootstrap - only one ng-app allowed for each page
angular.element(document).ready(function() {
	angular.bootstrap(document.querySelector('[ordering-app]'), ['Ordering']);
});