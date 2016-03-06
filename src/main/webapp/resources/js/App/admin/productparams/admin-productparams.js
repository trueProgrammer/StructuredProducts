angular.module('App.admin.productparams')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/productparams', {
                templateUrl: 'resources/js/App/admin/productparams/admin-productparams.html',
                controller: 'admin-productparams'
            })
        }])
.controller('admin-productparams', [ '$scope', '$log', 'restService', '$rootScope', '$location',
    function($scope, $log, restService, $rootScope, $location) {
        $scope.page = 'productparams';
        $scope.products = [{name: 'tbd'}];
        $scope.productClick = function(product) {
            $scope.selectedProduct = product;
        }
    }]
);

