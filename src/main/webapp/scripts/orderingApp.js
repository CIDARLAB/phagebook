angular
        .module('orderingApp', ['clothoRoot', 'ui.bootstrap'])
        .controller('orderingCtrl', orderingCtrl)
        .controller('newOrderController', newOrderController)
        .controller('orderingSettingsController', orderingSettingsController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar)
        .directive('footer', footer); 