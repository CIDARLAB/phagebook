angular
        .module('orderingApp', ['clothoRoot', 'ui.bootstrap.tpls', 'ui.bootstrap.modal'])
        .controller('newOrderController', newOrderController)
        .controller('orderingSettingsController', orderingSettingsController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar)
        .directive('footer', footer); 