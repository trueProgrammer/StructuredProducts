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
        var defaultStrCellTemplate = "<div id={{(rowRenderIndex+'-'+col.name)}} class='ui-grid-cell-content'>{{row.entity[col.field]}}</div>";
        var columns = {
            productType : [
                { field: 'name', displayName: 'Тип структурного продукта', width: "94%",
                    notNull: true,
                    cellTemplate: defaultStrCellTemplate
                }
            ],
            investment : [
                { field: 'min', displayName: 'Минимум', width: "47%",
                    notNull: true,
                    cellTemplate: defaultStrCellTemplate

                },
                { field: 'max', displayName: 'Максимум', width: "47%", notNull: true, cellTemplate: defaultStrCellTemplate},
            ],
            strategy : [
                { field: 'name', displayName: 'Стратегия', width: "94%", notNull: true, cellTemplate: defaultStrCellTemplate },
            ],
            legalType: [
                { field: 'name', displayName: 'Юридическая форма', width: "94%", notNull: true, cellTemplate: defaultStrCellTemplate },
            ],
            payoff: [
                { field: 'name', displayName: 'Размер выплаты', width: "94%", notNull: true, cellTemplate: defaultStrCellTemplate },
            ],
            risks: [
                { field: 'name', displayName: 'Риски', width: "94%", notNull: true, cellTemplate: defaultStrCellTemplate },
            ],
            currency: [
                { field: 'name', displayName: 'Валюта', width: "94%", notNull: true, cellTemplate: defaultStrCellTemplate },
            ],
            paymentPeriodicity: [
                { field: 'name', displayName: 'Периодичность выплаты дохода', width: "94%", notNull: true, cellTemplate: defaultStrCellTemplate  },
            ],
            underlayingType: [
                { field: 'name', displayName: 'Тип базового актива', width: "94%", notNull: true, cellTemplate: defaultStrCellTemplate  },
            ],
            underlaying: [
                { field: 'name', displayName: 'Базовый актив', width: "47%", notNull: true, cellTemplate: defaultStrCellTemplate  },
                { field: 'type', displayName: 'Тип базового актива', width: "47%",
                    cellFilter: "griddropdown:this",
                    notNull: true,
                    cellTemplate: "<div id={{(rowRenderIndex+'-'+col.name)}} class='ui-grid-cell-content'>{{row.entity[col.field].name}}</div>",
                    editableEntity: 'underlayingType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            ],
            product: [
                { field: 'name',  displayName: 'Название', width: 200, notNull: true, cellTemplate: defaultStrCellTemplate},
                { field: 'description',  displayName: 'Описание', width: 200, notNull: true, cellTemplate: defaultStrCellTemplate},
                { field: 'productType', displayName: 'Тип структурного продукта', width: 250,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'productType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'minTerm',  displayName: 'Минимальный срок', width: 50, notNull: true, cellTemplate: defaultStrCellTemplate},
                { field: 'maxTerm',  displayName: 'Максимальный срок', width: 50, notNull: true, cellTemplate: defaultStrCellTemplate},
                { field: 'underlaying', displayName: 'Базовый актив', width: 200,
                    notNull: true,
                    cellTemplate: "<div id={{(rowRenderIndex+'-'+col.name)}} class='ui-grid-cell-content'>{{row.entity[col.field].name}}</div>",
                    cellFilter: "griddropdown:this",
                    editableEntity: 'underlaying', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'investment', displayName: 'Минимальная сумма инвестиций', width: 250,
                    notNull: true,
                    cellTemplate: "<div id={{(rowRenderIndex+'-'+col.name)}} class='ui-grid-cell-content'>{{row.entity[col.field].name}}</div>",
                    cellFilter: "griddropdown:this",
                    editableEntity: 'investment', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'broker', displayName: 'Провайдер продукта', width: 200,
                    notNull: true,
                    cellTemplate: "<div id={{(rowRenderIndex+'-'+col.name)}} class='ui-grid-cell-content'>{{row.entity[col.field].name}}</div>",
                    cellFilter: "griddropdown:this",
                    editableEntity: 'broker', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'returnValue',  displayName: 'Доходность', width: 50, notNull: true, cellTemplate: defaultStrCellTemplate},
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
                gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, event) {
                    var elem = $(event.toElement);
                    $scope.closeAlert();
                    if (elem.children().length == 0) {
                        $scope.currentEditElement = elem.parent();
                    } else {
                        $scope.currentEditElement = elem;
                    }
                });
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue, row) {
                    $scope.saveButtonsDisabled = false;
                    if (validateColumn(colDef, newValue)) {
                        $scope.currentEditElement.removeClass('invalid');
                    } else {
                        $scope.currentEditElement.addClass('invalid');
                    }
                    $scope.currentEditElement = null;
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

        var validateColumn = function(colDef, newVal) {
            if (colDef.notNull) {
                if (!newVal || (newVal.trim && (newVal === '' || newVal.trim() === ''))) {
                    return false;
                }
            }
            return true;
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

            if (validate()) {
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
            }
        };


        var validate = function() {
            var invalidColumns = [];
            for (var i in $scope.table.columnDefs) {
                var columnDef = $scope.table.columnDefs[i];
                if (columnDef.notNull) {
                    for (var j in $scope.table.data) {
                        var data = $scope.table.data[j];
                        if (data[columnDef.name]) {
                            if (data[columnDef.name].trim) {
                                if(data[columnDef.name] === '' || data[columnDef.name].trim() === '') {
                                    invalidColumns.push({row: j, name: columnDef.name});
                                }
                            }
                        } else {
                            invalidColumns.push({row: j, name: columnDef.name});
                        }
                    }
                }
            }

            for (var i in invalidColumns) {
                var invalidColumn = invalidColumns[i];
                var invalidElem = $('#' + invalidColumn.row + '-' + invalidColumn.name);
                invalidElem.parent().addClass('invalid');
                $scope.showFailAlert('Не все необходимые поля заполнены')
            }

            return invalidColumns.length === 0;
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
