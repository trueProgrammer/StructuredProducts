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
        var getProductParams = function() {
            restService.getProductParams(function(params) {
                $scope.productParams = params;
            }, function(error) {
                console.error(error)
            })
        };
        getProductParams();

        $scope.productClick = function(product) {
            $scope.selectedProduct = product;
        };

        $scope.addParam = function() {
            restService.addProductParam({
                id: $scope.selectedProduct.id,
                product_id: $scope.selectedProduct.product.id,
                forecast: $scope.selectedProduct.forecast,
                img: $scope.selectedProduct.img,
                chart: $scope.selectedProduct.chart
            }, function() {
                $log.info("Successfully saved");
            }, function() {
                $log.error("Error during saving");
            });
        };
        $scope.clickChartUpload = function() {
            $('#uploadChart').trigger('click');
        };

        $scope.clickImgUpload = function() {
            $('#imgUpload').trigger('click');
        };

        $scope.setImgData = function(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function(e) {
                    $("#product-logo-preview").attr('src', e.target.result);
                    $scope.selectedProduct.img = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            }
        };

        $scope.setChartData = function(file, e) {
            var r = new FileReader();
            r.onload = function(e) {
                var contents = e.target.result;
                $scope.selectedProduct.chart = contents;
            };
            r.readAsText(file);
            e.wrap('<form>').closest('form').get(0).reset();
            e.unwrap();
        };
        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            }
        }());
    }]
);

