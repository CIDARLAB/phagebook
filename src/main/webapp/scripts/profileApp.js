angular
        .module('profileApp', ['ui.bootstrap'])
        .controller('profileCtrl', profileCtrl)
        .controller('profileWindowController', profileWindowController)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar)
        .directive('footer', footer);