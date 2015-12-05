angular
        .module('profileApp', ['ui.bootstrap.modal'])
        .controller('profileCtrl', profileCtrl)
        .controller('profileWindowCtrl', profileWindowCtrl)
        .directive('phagebookSidebar', phagebookSidebar)
        .directive('searchBar', searchBar); 