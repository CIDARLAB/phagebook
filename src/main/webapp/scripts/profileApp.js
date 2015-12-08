angular
        .module('profileApp', ['ui.bootstrap.modal'])
        .controller('profileCtrl', profileCtrl)
        .controller('profileWindowController', profileWindowController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('footer', footer);