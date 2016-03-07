angular.module('App.about')
.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/about', {
            templateUrl: 'resources/js/App/about/about.html',
            controller: 'aboutCtrl'
        })
}])
.controller('aboutCtrl', [
    function() {
        //init app function
        angular.element(document).ready(function () {
            App.init();
        });
    }
]);
