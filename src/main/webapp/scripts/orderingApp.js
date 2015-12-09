angular
        .module('orderingApp', ['ui.bootstrap'])
        .controller('orderingCtrl', orderingCtrl)
        .controller('newOrderController', newOrderController)
        .controller('orderingSettingsController', orderingSettingsController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('footer', footer); 