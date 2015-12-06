angular
        .module('projectsApp', ['clothoRoot'])
        .controller('newOrderController', newOrderController)
        .controller('orderingSettingsController', orderingSettingsController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar); 