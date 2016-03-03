angular.module('App.about')
.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/about', {
            templateUrl: 'resources/js/App/about/about.html'
        })
}]);
