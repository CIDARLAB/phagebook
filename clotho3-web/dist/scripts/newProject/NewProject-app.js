angular.module('NewProject',['clotho.foundation']);

//we need to manually bootstrap - only one ng-app allowed for each page
angular.element(document).ready(function() {
    angular.bootstrap(document.querySelector('[NewProject-app]'), ['NewProject']);
});