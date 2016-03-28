 angular.module('App.product')
     .config(['$routeProvider',
         function($routeProvider){
             $routeProvider.when('/product', {
                 templateUrl: 'resources/js/App/product/product.html',
                 controller: 'product'
             })
         }])
.controller('product', ['$scope', '$log', 'restService', '$routeParams', '$uibModal',
         function($scope, $log, restService, $routeParams, $uibModal) {

             var selected = {
                 desc : false,
                 otherParams: false
             };

             var show = {
                desc: false,
                mainParams: true,
                otherParams: false,
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
             (function() {
             restService.getProductWithParams(
                 $routeParams.id,
                 /*'20',*/
                 function(result) {
                     var termName;
                     var minTerm = result.product.minTerm;
                     var maxTerm = result.product.maxTerm;
                     if(minTerm != 0 && maxTerm != 0) {
                         termName = "От " + minTerm + " до " + maxTerm + " месяцев";
                     } else if(minTerm == 0 && maxTerm != 0) {
                         termName = "До " +  maxTerm + " месяцев";
                     } else if (minTerm != 0 && maxTerm == 0) {
                         termName = "Свыше " + minTerm + " месяцев";
                     }
                     result.product.termName = termName;
                     $scope.productParams = result;
                     restService.getUnderlayingHistoricalQuotes(result.product.id,
                         function(result) {
                             var data = {
                                 datasets: []
                             };

                             result.forEach(function(hist){
                                if(!data.labels) {
                                    data.labels = hist.labels;
                                }
                                 var dataset = {
                                     label: hist.name,
                                     fillColor: "rgba(220,220,220,0.2)",
                                     strokeColor: "rgba(220,220,220,1)",
                                     pointColor: "rgba(220,220,220,1)",
                                     pointStrokeColor: "#fff",
                                     pointHighlightFill: "#fff",
                                     pointHighlightStroke: "rgba(220,220,220,1)",
                                     data: hist.dataset
                                 };
                                 data.datasets.push(dataset);
                             });
                             var ctx = document.getElementById("chart").getContext("2d");
                             var chart = new Chart(ctx).Line(data);
                         },
                         function(error) {

                         }
                     );
                 },
                 function() {$log.error("error while get product with params")}
             );
             }());

             $scope.openModal = function () {

                 var modalInstance = $uibModal.open({
                     templateUrl: 'sendingRequestModal.html',
                     controller: 'sendRequestCtrl',
                 });

                 modalInstance.rendered.then(function () {
                     $("#phone").mask("+7 (999) 999-9999");
                 }, function () {
                 });
             };

         }]);
