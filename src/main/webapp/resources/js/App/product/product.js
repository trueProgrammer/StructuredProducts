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
                 function(result) {
                     result.product.termName = rangeFunction(result.product.minTerm, result.product.maxTerm)+ " месяцев";
                     result.product.investName = rangeFunction(result.product.minInvest, result.product.maxInvest);
                     $scope.productParams = result;
                     if(!$scope.productParams.img) {
                         $scope.productParams.showImg = false;
                     } else {
                         $scope.productParams.showImg = true;
                     }
                     /*if(!$scope.productParams.showChart) {
                         return;
                     }*/
                     restService.getUnderlayingHistoricalQuotes(result.product.id,
                         function(result) {
                             var config = {
                                 type: 'line',
                                 data: {
                                     datasets: []
                                 },
                                 options: {
                                     scaleLabel: "<%=value%> $",
                                     responsive: true,
                                     scales: {
                                         xAxes: [{
                                             /*display: true,
                                             ticks: {
                                                 maxTicksLimit: 12,
                                                 callback: function(value) {
                                                     return '' + value;
                                                 }
                                             },*/
                                             /*ticks: {
                                                 maxTicksLimit: 10,
                                             },*/
                                             type: 'time',
                                             time: {
                                                 //maxTicksLimit: 12,
                                                 displayFormats: {
                                                     quarter: 'MMM YYYY'
                                                 }
                                             }
                                             /*time: {
                                                 maxTicksLimit: 12,
                                                 unit: 'month'
                                             }*/
                                         }],
                                         yAxes: [{
                                             display: true,
                                             beginAtZero: false,
                                         }]
                                     }
                                 }
                             };

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
                             var ctx = document.getElementById("chart").getContext("2d");
                             var chart = new Chart(ctx, config);
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
                     modalService.show(undefined, $scope.productParams.product.broker.id);
                 } else {
                     modalService.show();
                 }
             };
         }]);