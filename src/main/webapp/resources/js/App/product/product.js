 angular.module('App.product')
     .config(['$routeProvider',
         function($routeProvider){
             $routeProvider.when('/product', {
                 templateUrl: 'resources/js/App/product/product.html',
                 controller: 'product',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.1.6/Chart.min.js');
                    }]
                }
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
                 desc : true,
                 otherParams: false
             };

             var show = {
                desc: true,
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
                 var minStr = min.toLocaleString();
                 var maxStr = max.toLocaleString();

                 if(min != 0 && max != 0) {
                     return "От " + minStr + " до " + maxStr;
                 } else if(min == 0 && max != 0) {
                     return "До " +  maxStr;
                 } else if (min != 0 && max == 0) {
                     return "От " + minStr;
                 }
             }
             (function() {
             restService.getProductWithParams(
                 $routeParams.id,
                 function(result) {
                     result.product.termName = rangeFunction(result.product.minTerm, result.product.maxTerm)+ " месяцев";
                     result.product.investName = rangeFunction(result.product.minInvest, result.product.maxInvest);
                     $scope.productParams = result;
                     if(!$scope.productParams.img) {
                         $scope.productParams.showImg = false;
                     } else {
                         $scope.productParams.showImg = true;
                     }
                     restService.getUnderlayingHistoricalQuotes(result.product.id,
                         function(result) {
                             if ( result.length == 0) {
                                 return;
                             }
                             var config = {
                                 type: 'line',
                                 data: {
                                     datasets: []
                                 },
                                 options: {
                                     scaleLabel: "<%=value%> $",
                                     responsive: true,
                                     scales: {
                                         xAxes: [/*{
                                             /!*display: true,
                                             ticks: {
                                                 maxTicksLimit: 5,
                                             },*!/
                                             /!*type: 'time',
                                             time: {
                                                 //maxTicksLimit: 12,
                                                 displayFormats: {
                                                     quarter: 'MMM YYYY'
                                                 }
                                             }*!/
                                         }*/],
                                         yAxes: [{
                                             display: true,
                                             beginAtZero: false,
                                         }]
                                     }
                                 }
                             };

                             if (result[0]) {
                                 if (result[0].period && result[0].period === '5d') {
                                     config.options.scales.xAxes.push({
                                         display: true,
                                         ticks: {
                                             maxTicksLimit: 5,
                                         }
                                     });
                                 } else {
                                     config.options.scales.xAxes.push({
                                         display: true,
                                         type: 'time',
                                         time: {
                                             displayFormats: {
                                                 quarter: 'MMM YYYY'
                                             }
                                         }
                                     });
                                 }
                             }

                             var index = 0;
                             result.forEach(function(hist){
                                if(!config.data.labels) {
                                    config.data.labels = hist.labels;
                                }
                                 var dataset = {
                                     label: hist.name,
                                     pointRadius: 0,
                                     fillColor: "rgba(0,0,0,0)",
                                     strokeColor: colors[index],
                                     pointColor: colors[index],
                                     pointStrokeColor: "#fff",
                                     pointHighlightFill: "#fff",
                                     pointHighlightStroke: colors[index],
                                     pointBorderWidth: 1,
                                     //multiTooltipTemplate: "<%= datasetLabel %> - <%= value %>",
                                     data: hist.dataset
                                 };
                                 config.data.datasets.push(dataset);
                                 index++;
                             });
                             var elementById = document.getElementById("chart");
                             if (elementById){
                                 var ctx = elementById.getContext("2d");
                                 var chart = new Chart(ctx, config);
                             }
                         },
                         function(error) {
                             $log.error("error get product's historical data")
                         }
                     );
                 },
                 function() {$log.error("error get product with params")}
             );
             }());

             $scope.openModal = function () {
                 if ($scope.productParams.product.broker) {
                     modalService.show(undefined, $scope.productParams.product.id);
                 } else {
                     modalService.show();
                 }
             };

             angular.element(document).ready(function () {
                 $("#phone").mask("+7 (999) 999-9999");
             });
         }]);