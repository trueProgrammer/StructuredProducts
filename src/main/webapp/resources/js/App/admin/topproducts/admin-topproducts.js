angular.module('App.admin.topproducts')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/topproducts', {
                templateUrl: 'resources/js/App/admin/topproducts/admin-topproducts.html',
                controller: 'admin-topproducts'
            })
        }])
.controller('admin-topproducts', [ '$scope', '$log', 'restService', '$rootScope', '$location', '$routeParams',
    function($scope, $log, restService, $rootScope, $location, $routeParams) {
        $scope.page = 'topproduct';
        var columns = [
            { field: 'name', displayName: 'Название', width: 200 },
            {field: 'top', displayName:'Лучший', width: 80},
            { field: 'productType', displayName: 'Тип структурного продукта', width: 250,
                cellFilter: "griddropdown:this",
                editableEntity: 'productType', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'term', displayName: 'Срок', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'term', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'underlaying', displayName: 'Базовый актив', width: 100,
                cellFilter: "griddropdown:this",
                editableEntity: 'underlaying', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'investment', displayName: 'Минимальная сумма инвестиций', width: 250,
                cellFilter: "griddropdown:this",
                editableEntity: 'investment', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'broker', displayName: 'Провайдер продукта', width: 200,
                cellFilter: "griddropdown:this",
                editableEntity: 'broker', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'returnValue', displayName: 'Доходность', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'return', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'count', editDropdownOptionsArray: []},
            { field: 'strategy', displayName: 'Стратегия', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'strategy', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'legalType', displayName: 'Юридическая форма', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'legalType', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'payoff', displayName: 'Размер выплат', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'payoff', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'risks', displayName: 'Риски', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'risks', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'currency', displayName: 'Валюта', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'currency', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            { field: 'paymentPeriodicity', displayName: 'Периодичность выплаты дохода', width: 150,
                cellFilter: "griddropdown:this",
                editableEntity: 'paymentPeriodicity', editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownValueLabel: 'name', editDropdownOptionsArray: []}
        ];
        var returnValues = function(entity, array) {
            restService.getInstrumentType(
                entity,
                function(values) {
                    array.editDropdownOptionsArray = values;
                },
                function(response) {
                    $log.error("Get instrument values for " + entity + " failed.");
                    return [];
                }
            );
        };
        $scope.selection = [];
        $scope.table = {
            enableVerticalScrollbar : 2,
            enableHorizontalScrollbar : 2,
            enableColumnMenus : false,
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        $scope.selection.push(row.entity);
                    } else {
                        var index = $scope.selection.indexOf(row.entity);
                        $scope.selection.splice(index, 1);
                    }
                });
            }
        };
        $scope.table.data = [];
        $scope.table.columnDefs = columns;
        for(field in columns) {
            if (columns[field].editDropdownOptionsArray) {
                returnValues(columns[field].editableEntity, columns[field]);
            }
        }

        $scope.addToTop = function() {
            var ids = [];
            for(var i in $scope.selection) {
                ids.push($scope.selection[i].id);
            }

            $scope.selection = [];
            restService.addToTop(ids, $scope.selectedTime, function() {
                $scope.loadTop($scope.selectedTime);
            }, function() {console.log("Some error occurs")});
        };

        $scope.removeFromTop = function() {
            var ids = [];
            for(var i in $scope.selection) {
                ids.push($scope.selection[i].id);
            }
            $scope.selection = [];

            restService.removeFromTop(ids, $scope.selectedTime, function() {
                $scope.loadTop($scope.selectedTime);
            }, function() {console.log("Some error occurs")});
        };

        $scope.loadTop = function(time) {
            $scope.selectedTime = time;
            restService.getTopProducts(
                time,
                function(values) {
                    for (var i in values) {
                        if (values[i]['top']) {
                            values[i]['top'] = 'Да';
                        } else {
                            values[i]['top'] = 'Нет';
                        }
                    }
                    $scope.table.data = values;
                },
                function(response) {
                    $log.error("Get top products failed.");
                }
            )
        };

        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            } else {
                $scope.loadTop('week');
            }
        }());
    }]);
