angular
        .module('projectsApp', ['ui.bootstrap'])
        .controller('tabsController', tabsController)
        .controller('projectController', projectController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar)
        .directive('footer',footer); 