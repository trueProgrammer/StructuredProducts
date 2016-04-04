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
                restService.getProductUnderlayings(function(underlyings) {
                    $scope.underlyings = underlyings;
                }, function() {

                })
            }, function(error) {
                console.error(error)
            })
        };
        getProductParams();

        var columns = [
                { field: 'name', displayName: 'Базовый актив'},
                { field: 'officialName', displayName: 'Биржевое имя'},
                { field: 'type', displayName: 'Тип базового актива'}
            ];
        $scope.selectionChoosen = [];
        $scope.choosenUnderlying = {
            enableVerticalScrollbar : 2,
            enableHorizontalScrollbar : 2,
            enableColumnMenus : false,
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        $scope.selectionChoosen.push(row.entity);
                    } else {
                        var index = $scope.selectionChoosen.indexOf(row.entity);
                        $scope.selectionChoosen.splice(index, 1);
                    }
                });
            }
        };
        $scope.choosenUnderlying.data = [{name: 'choosen'}];
        $scope.choosenUnderlying.columnDefs = columns;


        $scope.selectionAvailable = [];
        $scope.availableUnderlying = {
            enableVerticalScrollbar : 2,
            enableHorizontalScrollbar : 2,
            enableColumnMenus : false,
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        $scope.selectionAvailable.push(row.entity);
                    } else {
                        var index = $scope.selectionAvailable.indexOf(row.entity);
                        $scope.selectionAvailable.splice(index, 1);
                    }
                });
            }
        };
        $scope.availableUnderlying.data = [{name: 'available'}];
        $scope.availableUnderlying.columnDefs = columns;

        $scope.toChoosen = function() {
            $scope.choosenUnderlying.data = $scope.choosenUnderlying.data.concat($scope.selectionAvailable);

            var selectedIds = $scope.selectionAvailable.map(function(d) {return d.id;});
            $scope.availableUnderlying.data = $scope.availableUnderlying.data.filter(function(d) {
                return selectedIds.indexOf(d.id) === -1;
            });

            $scope.selectionAvailable = [];
            $scope.selectionChoosen = [];
        };

        $scope.toAvailable = function() {
            $scope.availableUnderlying.data = $scope.availableUnderlying.data.concat($scope.selectionChoosen);

            var selectedIds = $scope.selectionChoosen.map(function(d) {return d.id;});
            $scope.choosenUnderlying.data = $scope.choosenUnderlying.data.filter(function(d) {
                return selectedIds.indexOf(d.id) === -1;
            });
            $scope.selectionAvailable = [];
            $scope.selectionChoosen = [];
        };

        $scope.productClick = function(product) {
            $scope.selectedProduct = product;
            $scope.availableUnderlying.data = $scope.underlyings;
            $scope.choosenUnderlying.data = product.product.underlayingList;

            var choosenIds = $scope.choosenUnderlying.data.map(function(d) {return d.id});
            $scope.availableUnderlying.data = $scope.availableUnderlying.data.filter(function(d) {
                return choosenIds.indexOf(d.id) === -1;
            })

        };

        $scope.addParam = function() {
            restService.addProductParam({
                id: $scope.selectedProduct.id,
                product_id: $scope.selectedProduct.product.id,
                forecast: $scope.selectedProduct.forecast,
                img: $scope.selectedProduct.img,
                chart: $scope.selectedProduct.chart,
                underlaying: $scope.choosenUnderlying.data
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

