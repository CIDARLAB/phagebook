angular.module('profileApp', [])
        .controller('profileCtrl', profileCtrl)
        .controller('settingsCtrl', settingsCtrl)
        .directive('header', header)
        .directive('sidebar',sidebar)
        .directive('footer', footer);
