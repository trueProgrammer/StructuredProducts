angular.module('App.admin.products')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin', {
                templateUrl: 'resources/js/App/admin/products/admin-products.html',
                controller: 'admin-products'
            })
        }])
.controller('admin-products', [ '$scope', '$log', 'restService', '$rootScope', '$location',
    function($scope, $log, restService, $rootScope, $location) {
        $scope.page = 'product';

        $scope.saveButtonsDisabled = true;
        $scope.selection = [];

        var dropDownValues = {};
        var returnValues = function(entity, array) {
            restService.getInstrumentType(
                entity,
                function(values) {
                    //process underlaying entities for correct display in drop down menu of table
                    if(entity === "underlaying") {
                        for(var value in values) {
                            var underlaying = values[value];
                            underlaying.dropDownName = underlaying.name + ' ' + underlaying.type.name;
                        }
                    }
                    dropDownValues[entity] = values;
                    array.editDropdownOptionsArray = values;
                },
                function(response) {
                    $log.error("Get instrument values for " + entity + " failed.");
                    return [];
                }
            );
        };

        $scope.selected;
        var columns = {
            productType : [
                { field: 'name', displayName: 'Тип структурного продукта', width: "94%" },
            ],
            term : [
                { field: 'min', displayName: 'Минимум', width: "47%" },
                { field: 'max', displayName: 'Максимум', width: "47%" },
            ],
            investment : [
                { field: 'min', displayName: 'Минимум', width: "47%" },
                { field: 'max', displayName: 'Максимум', width: "47%" },
            ],
            return : [
                { field: 'count', displayName: 'Доходность', width: "94%" },
            ],
            strategy : [
                { field: 'name', displayName: 'Стратегия', width: "94%" },
            ],
            legalType: [
                { field: 'name', displayName: 'Юридическая форма', width: "94%" },
            ],
            payoff: [
                { field: 'name', displayName: 'Размер выплаты', width: "94%" },
            ],
            risks: [
                { field: 'name', displayName: 'Риски', width: "94%" },
            ],
            currency: [
                { field: 'name', displayName: 'Валюта', width: "94%" },
            ],
            paymentPeriodicity: [
                { field: 'name', displayName: 'Периодичность выплаты дохода', width: "94%" },
            ],
            underlayingType: [
                { field: 'name', displayName: 'Тип базового актива', width: "94%" },
            ],
            underlaying: [
                { field: 'name', displayName: 'Базовый актив', width: "47%" },
                { field: 'type', displayName: 'Тип базового актива', width: "47%",
                    cellFilter: "griddropdown:this",
                    editableEntity: 'underlayingType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            ],
            product: [
                { field: 'name', displayName: 'Название', width: 200 },
                { field: 'productType', displayName: 'Тип структурного продукта', width: 250,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'productType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'term', displayName: 'Срок', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'term', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'underlaying', displayName: 'Базовый актив', width: 200,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'underlaying', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'dropDownName', editDropdownOptionsArray: []},
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
            ],
        };

        function getValues(entity) {
            restService.getInstrumentType(
                entity,
                function(values) {
                    $scope.table.data = values;
                },
                function(response) {
                    $log.error("Get instrument values for "+entity+" failed.");
                }
            )
        };

        $scope.table = {
            enableVerticalScrollbar : 2,
            enableHorizontalScrollbar : 2,
            enableColumnMenus : false,
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled = false;
                });
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

        $scope.uploadCsv = function(file, e) {
            restService.uploadProductsCsv(file,
                function(){
                    console.log('Successfully load csv');
                    getValues("product");
                    $scope.selectTable("product");
                },
                function(){console.log('Error occurs during load csv')}
            );
            e.wrap('<form>').closest('form').get(0).reset();
            e.unwrap();

        };

        $scope.clickUploadButton = function() {
            $('#uploadFile').trigger('click');
        };

        $scope.clickDownloadButton = function() {
            restService.downloadCsv(function(data) {
                var blob = new Blob([data], {type: "text/plain; charset=utf-8"});
                saveAs(blob, "product.csv");
            }, function() {
                $log.error("error");
            });
        };
        $scope.selectTable = function(id) {
            $scope.selected = id;
            $scope.selection = [];
            $scope.saveButtonsDisabled = true;
            for(field in columns[id]) {
                if (columns[id][field].editDropdownOptionsArray) {
                    returnValues(columns[id][field].editableEntity, columns[id][field]);
                }
            }
            $scope.table.columnDefs = columns[id];
            getValues(id);
        };

        $scope.alert = { visible: false };

        $scope.closeAlert = function() {
            $scope.alert.visible = false;
        };

        $scope.showFailAlert = function(msg) {
            $scope.alert.msg = msg;
            $scope.alert.visible = true;
        };

        $scope.addData = function() {
            var length = $scope.table.data.length + 1;
            $scope.table.data.push({});
        };

        $scope.saveData = function() {
            $scope.saveButtonsDisabled = true;
            restService.updateInstrumentType(
                $scope.table.data,
                $scope.selected,
                function(response) {
                    $log.info("Update " + $scope.selected + " success.");
                    getValues($scope.selected);
                },
                function(response) {
                    $scope.showFailAlert(response);
                    getValues($scope.selected);
                    $log.error("Update " + $scope.selected + " failed.");
                }
            );
        };

        $scope.removeData = function() {
            restService.deleteInstrumentType(
                $scope.selection,
                $scope.selected,
                function(response) {
                    $log.info("Delete " + $scope.selected + " success.");
                    $scope.selection = [];
                    getValues($scope.selected);
                },
                function(response) {
                    $scope.showFailAlert(response);
                    $log.error("Delete " + $scope.selected + " failed.");
                    $scope.selection = [];
                    getValues($scope.selected);
                }
            );
        };

        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            } else {
                $scope.selectTable('product');
            }
        }());


    }])
    .filter('griddropdown', function() {
        return function (input, context) {
            if(typeof input === 'undefined') {
                return '';
            }
            var map = context.col.colDef.editDropdownOptionsArray;
            var valueField = context.col.colDef.editDropdownValueLabel;
            var initial = context.row.entity[context.col.field];
            if (typeof map !== "undefined") {
                for (var i = 0; i < map.length; i++) {
                    if (map[i]['id'] == input['id']) {
                        return map[i][valueField];
                    }
                }
            } else if (initial) {
                return initial;
            }
            return input;
        };
    });
