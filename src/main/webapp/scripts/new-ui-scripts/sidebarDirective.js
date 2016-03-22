function sidebar($window){

        return {
        restrict: 'E',
        //replace: true,
        templateUrl: '../html/sidebarDirective.html',
        scope: {
            user: '='
        },
        controller: function ($scope, $window) {
            
            var currentPathName = $window.location.pathname;
            var tabs = new Object();
            tabs["0"] = "profile.html";
            tabs["1"] = "newProject.html";
            tabs["2"] = "myProjects.html";
            tabs["3"] = "newOrder.html";
            tabs["4"] = "currentOrders.html";
            tabs["5"] = "orderHistory.html";
            tabs["6"] = "addVendorsAndProducts.html";
      
                var listElement;
                for (var i = 0; i < 7; i++){
                    var url = "/html/" + tabs[i];
                    if (url === currentPathName ){
                        listElement = document.getElementById(i.toString());
                        listElement.setAttribute("class", "active");
                    }
                }       
            
            //class="active"
            //put code here if want the code to run before compilation
            //ajax call to servlet which will have the person info
        },
        link: function (scope, elem, attr) {
            //put code here if want code to run after compilation
            //pictureName has to link to the person
            //displayName is also a field that has to link
        }
    };
  
}
