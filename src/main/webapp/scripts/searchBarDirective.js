angular.module('ngphagebookSearchBar').directive('ngphagebookSearchBar', function(){
    return{
        restrict: 'E',
        replace: true,
        template: '<div class="form">Search Bar</div>',
        scope: {
            
        },
        controller: {
            //the functionality needs to load for this to work
        },
        link: {
            
        }
    }
});


//do the html elements that are bootstrap need the classes declared here?
//or does this have to be hard regular HTML? then is it easier to link to element? It's just a search bar though..