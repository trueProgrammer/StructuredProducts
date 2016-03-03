angular.module('App.partners')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/partners', {
                templateUrl: 'resources/js/App/partners/partners.html'
            })
        }]);
