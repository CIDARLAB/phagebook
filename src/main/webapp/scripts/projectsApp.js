angular
        .module('projectsApp', ['clothoRoot'])
        .controller('tabsController', tabsController)
        .controller('projectsController', projectsController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar)
        .directive('footer',footer); 