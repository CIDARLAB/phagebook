angular
        .module('projectsApp', ['clothoRoot'])
        .module('tabsApp',[])
        .controller('tabsController', tabsController)
        .controller('projectsController', projectsController)
        .controller('orderingSettingsController', orderingSettingsController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar); 