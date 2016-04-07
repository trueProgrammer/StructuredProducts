 angular.module('App.product')
     .config(['$routeProvider',
         function($routeProvider){
             $routeProvider.when('/product', {
                 templateUrl: 'resources/js/App/product/product.html',
                 controller: 'product'
             })
         }])
.controller('product', ['$scope', '$log', 'restService', '$routeParams', 'modalService',
         function($scope, $log, restService, $routeParams, modalService) {

             var colors = [
                 "rgba(220,220,220,1)",
                 "rgba(151,187,205,1)",
                 "rgba(255,255,102,1)",
                 "rgba(153,153,51,1)",
                 "rgba(102,255,51,1)",
                 "rgba(153,204,153,1)",
                 "rgba(0,204,153,1)",
                 "rgba(204,153,204,1)"
             ];

             var selected = {
                 desc : false,
                 otherParams: false
             };

             var show = {
                desc: false,
                mainParams: true,
                otherParams: false
             };

             $scope.click = function(section) {
                 selected[section] = !selected[section];
             };
             $scope.isSelected = function(section) {
                 return selected[section];
             };
             $scope.isNotSelected = function(section) {
                 return !selected[section];
             };
             $scope.isShow = function(section) {
                 return show[section];
             };
             $scope.click = function(section) {
                 selected[section] = !selected[section];
                 if(show[section]) {
                     show.mainParams = true;
                     show[section] = false;
                 } else {
                     show.mainParams = false;
                     show[section] = true;
                 }
             };
             function rangeFunction(min, max) {
                 var result;
                 if(min != 0 && max != 0) {
                     return "От " + min + " до " + max;
                 } else if(min == 0 && max != 0) {
                     return "До " +  max;
                 } else if (min != 0 && max == 0) {
                     return "Свыше " + min;
                 }
             }
             (function() {
             restService.getProductWithParams(
                 $routeParams.id,
                 /*'20',*/
                 function(result) {
                     result.product.termName = rangeFunction(result.product.minTerm, result.product.maxTerm)+ " месяцев";
                     result.product.investName = rangeFunction(result.product.investment.min, result.product.investment.max);
                     $scope.productParams = result;
                     restService.getUnderlayingHistoricalQuotes(result.product.id,
                         function(result) {
                             var data = {
                                 datasets: []
                             };

                             var index = 0;

                             result.forEach(function(hist){
                                if(!data.labels) {
                                    data.labels = hist.labels;
                                }
                                 var dataset = {
                                     label: hist.name,
                                     fillColor: "rgba(0,0,0,0)",
                                     strokeColor: colors[index],
                                     pointColor: colors[index],
                                     pointStrokeColor: "#fff",
                                     pointHighlightFill: "#fff",
                                     pointHighlightStroke: colors[index],
                                     //multiTooltipTemplate: "<%= datasetLabel %> - <%= value %>",
                                     data: hist.dataset
                                 };
                                 data.datasets.push(dataset);
                             });
                             var ctx = document.getElementById("chart").getContext("2d");
                             var chart = new Chart(ctx).Line(data, {
                                 // make enough space on the right side of the graph
                                 scaleLabel: "<%=value%> $"
                             });
                             document.getElementById("legendDiv").innerHTML = chart.generateLegend();
                             index++;
                         },
                         function(error) {

                         }
                     );
                 },
                 function() {$log.error("error while get product with params")}
             );
             }());

             $scope.openModal = function () {
                 modalService.show();
             };

         }]);
