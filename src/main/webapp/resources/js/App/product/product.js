 angular.module('App.product')
     .config(['$routeProvider',
         function($routeProvider){
             $routeProvider.when('/product', {
                 templateUrl: 'resources/js/App/product/product.html',
                 controller: 'product'
             })
         }])
.controller('product', ['$scope', '$log', 'restService', '$routeParams',
         function($scope, $log, restService, $routeParams) {
             restService.getProductWithParams(
                 $routeParams.id,
                 function(result) {
                     $scope.productParams = result;

                     if (result.chart) {
                         var chartJson = JSON.parse(result.chart);
                         var data = {
                             labels: chartJson.labels,
                             datasets: [
                                 {
                                     label: "Цена",
                                     fillColor: "rgba(220,220,220,0.2)",
                                     strokeColor: "rgba(220,220,220,1)",
                                     pointColor: "rgba(220,220,220,1)",
                                     pointStrokeColor: "#fff",
                                     pointHighlightFill: "#fff",
                                     pointHighlightStroke: "rgba(220,220,220,1)",
                                     data: chartJson.data
                                 }
                             ]
                         };
                         var ctx = document.getElementById("chart").getContext("2d");
                         var chart = new Chart(ctx).Line(data);
                     }
                 },
                 function() {$log.error("error while get product with params")}
             );
         }]);
