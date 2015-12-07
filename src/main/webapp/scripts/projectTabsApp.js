angular
        .module('tabsApp', ['clothoRoot'])
        .controller('tabsController', tabsController)
        .controller('projectsController', projectsController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar)
        .directive('footer',footer); 