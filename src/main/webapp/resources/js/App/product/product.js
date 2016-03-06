 angular.module('App.product')
     .config(['$routeProvider',
         function($routeProvider){
             $routeProvider.when('/product', {
                 templateUrl: 'resources/js/App/product/product.html',
                 controller: 'product'
             })
         }])
.controller('product', ['$scope', '$log', 'restService',
         function($scope, $log, restService) {
             var chart = new Chart("#chart");
             chart.drawChartOxis(800, 300, 5, 15);
         }]);
