angular.module('App.structuredproducts')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/structuredproducts', {
                templateUrl: 'resources/js/App/structuredproducts/structuredproducts.html'
            })
        }]);
