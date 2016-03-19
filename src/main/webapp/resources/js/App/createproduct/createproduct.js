angular.module('App.createproduct')

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/createproduct', {
                templateUrl: 'resources/js/App/createproduct/createproduct.html',
                controller: 'createproduct'
            })
        }])
    .controller('createproduct', ['$scope',
        function ($scope) {

            $scope.outParams = [];

            $scope.profitDisabled = false;
            $scope.timeDisabled = true;
            $scope.sumDisabled = true;
            $scope.riskDisabled = true;

            $scope.sumSaved = false;
            $scope.sumLine = "";
            $scope.sums = ['200 тыс','300 тыс','500 тыс','1 млн','и больше'];
            $scope.fromSum = $scope.sums[0];
            $scope.toSum = $scope.sums[1];
            $scope.setFromSum = function (value) {
                $('#sum-text').text('От ' + value + ' до ' + $scope.toSum);
                $scope.fromSum = value;
            };
            $scope.setToSum = function (value) {
                $('#sum-text').text('От ' + $scope.fromSum + ' до ' + value);
                $scope.toSum = value;
            };
            $scope.saveSum = function () {
                //if ($scope.sums[$scope.fromSum] <= $scope.sums[$scope.toSum]) {
                    $scope.sumSaved = true;
                    $scope.sumLine = " от " + $scope.fromSum + "   до " + $scope.toSum + "  ";
                    sumControl.inactive();
                //}
            };
            $scope.editSum = function () {
                $scope.sumSaved = false;
                $scope.sumLine = "";
                sumControl.active();
            };



            $scope.timeSaved = false;
            $scope.timeLine = "";
            $scope.times = ['2 мес', '3 мес', '4 мес', '5 мес', '6 мес', '8 мес',
                '10 мес', '1 год', '1 год и 3 мес', '1 год и 6 мес'];
            $scope.fromTime = $scope.times[0];
            $scope.toTime = $scope.times[1];
            $scope.setFromTime = function (value) {
                $('#term-text').text('От ' + value + ' до ' + $scope.toTime);
                $scope.fromTime = value;
            };
            $scope.setToTime = function (value) {
                $('#term-text').text('От ' + $scope.fromTime + ' до ' + value);
                $scope.toTime = value;
            };
            $scope.saveTime = function () {
                //if ($scope.fromTime <= $scope.toTime) {
                    $scope.timeSaved = true;
                    $scope.timeLine = " от " + $scope.fromTime + "   до " + $scope.toTime + "  ";
                    $scope.sumDisabled = false;
                    $("#sumBlock").css("opacity","1");
                    termControl.inactive();
                    sumControl.turnOn();
                //}
            };
            $scope.editTime = function () {
                $scope.timeSaved = false;
                $scope.timeLine = "";
                termControl.active();
            };

            $scope.profitSaved = false;
            $scope.profitLine = "";
            $scope.profits = ['10%', '20%', '30%', '40%'];
            $scope.fromProfit = $scope.profits[0];
            $scope.toProfit = $scope.profits[1];
            $scope.setFromProfit = function (value) {
                $('#profit-text').text('От ' + value + ' до ' + $scope.toProfit);
                $scope.fromProfit = value;
            };
            $scope.setToProfit = function (value) {
                $('#profit-text').text('От ' + $scope.fromProfit + ' до ' + value);
                $scope.toProfit = value;
            };
            $scope.saveProfit = function () {
                if ($scope.fromProfit <= $scope.toProfit) {
                }
            };
            $scope.editProfit = function () {
                $scope.profitSaved = false;
                $scope.profitLine = "";
                profitControl.active();
            };

            $scope.riskSaved = false;
            $scope.riskLine = "";
            $scope.fromRisk = $scope.profits[0];
            $scope.toRisk = $scope.profits[1];
            $scope.setFromRisk = function (value) {
                $('#risk-text').text('От ' + value + ' до ' + $scope.toRisk);
                $scope.fromRisk = value;
            };
            $scope.setToRisk = function (value) {
                $('#risk-text').text('От ' + $scope.fromRisk + ' до ' + value);
                $scope.toRisk = value;
            };
            $scope.saveRisk = function () {
                if ($scope.fromRisk <= $scope.toRisk) {
                    $scope.riskSaved = true;
                    $scope.riskLine = " от " + $scope.fromRisk + "   до " + $scope.toRisk + "  ";
                    $scope.timeDisabled = false;
                    $("#timeBlock").css("opacity","1");
                    riskControl.inactive();
                    termControl.turnOn();
                }
            };
            $scope.editRisk = function () {
                $scope.riskSaved = false;
                $scope.riskLine = "";
                riskControl.active();
            };

            var riskControl = {
                id: 'riskBlock',
                edit: function() {
                    $scope.$apply(function() {
                        $scope.editRisk();
                    });
                },
                save: function() {
                    $scope.$apply(function() {
                        $scope.saveRisk();
                    });
                }
            };
            var termControl = {
                id: 'timeBlock',
                edit: function() {
                    $scope.$apply(function() {
                        $scope.editTime();
                    });
                },
                save: function() {
                    $scope.$apply(function() {
                        $scope.saveTime();
                    });
                }
            };
            var sumControl = {
                id: 'sumBlock',
                edit: function() {
                    $scope.$apply(function() {
                        $scope.editSum();
                    });
                },
                save: function() {
                    $scope.$apply(function() {
                        $scope.saveSum();
                    });
                }
            };

            var defaultParams = [{
                text: 'Доходность',
                stroke: '#91CF50',
                id: 'profit',
            }, {
                text: 'Уровень риска',
                stroke: '#FDBF01',
                id: 'risk',
                boundedControl: riskControl,
                value: function() {
                    return 'От ' + $scope.fromRisk + ' до ' + $scope.toRisk;
                }
            }, {
                text: 'Сумма вложений',
                stroke: '#FD0001',
                boundedControl: sumControl,
                id: 'sum',
                value: function() {
                    return 'От ' + $scope.fromSum + ' до ' + $scope.toSum;
                }
            }, {
                text: 'Срок вложений',
                stroke: '#4774AA',
                boundedControl: termControl,
                id: 'time',
                value: function() {
                    return 'От ' + $scope.fromTime + ' до ' + $scope.toTime;
                }
            }, ];

            $scope.optParams = [ {
                text: 'Размер выплат',
                stroke: '#4774AA',
                id: 'returnValue'
            }, {
                text: 'Тип базового актива',
                stroke: '#FDBF01',
                id: 'baseActiveType'
            },{
                text: 'Тип продукта',
                stroke: '#91CF50',
                id: 'type'
            }, {
                text: 'Риски',
                stroke: '#4774AA',
                id: 'risks'
            }, {
                text: 'Периодичность выплат',
                stroke: '#FDBF01',
                id: 'paymentsPeriod'
            }, {
                text: 'Стратегия',
                stroke: '#4774AA',
                id: 'strategy'
            }, {
                text: 'Валюта',
                stroke: '#FDBF01',
                id: 'currency',
            }];


            var mapControls = function(controls, hexControls) {
                var controlsIds = controls.map(function(control) {
                    return control.id.substr(0, control.id.length - 5);
                });
                var hexControlsIds = hexControls.map(function(hexControl) {return hexControl.id;});
                controlsIds.forEach(function(controlId, index) {
                    var hexIndex = hexControlsIds.indexOf(controlId);
                    if (hexIndex != -1) {
                        controls[index].hexControl = hexControls[hexIndex];
                        hexControls[hexIndex].boundedControl = controls[index];
                    }
                });
            }

            var extendControls = function (controls) {
                controls.forEach(function(control){
                    control.setValue = function(value) {
                        this.value = value;
                        $('#' + this.hexControl.id + '-text').text(value);
                    };
                    control.save = function() {
                        this.isSaved = true;
                        this.line = this.lineFormat.format(this.value);
                        this.hexControl.inactive();
                    };
                    control.edit = function() {
                        this.isSaved = false;
                        this.line = '';
                        this.hexControl.active();
                    }
                });
            };

            var extendDefaultControls = function (controls) {
                controls.forEach(function(control, index){
                    control.value = control.lineFormat.format(control.fromValue, control.toValue);
                    control.setFromValue = function(fromValue) {
                        control.fromValue = fromValue;
                        this.value = this.lineFormat.format(this.fromValue, this.toValue);
                        $('#' + this.hexControl.id + '-text').text(this.value);
                    };
                    control.setToValue = function(toValue) {
                        control.toValue = toValue;
                        this.value = this.lineFormat.format(this.fromValue, this.toValue);
                        $('#' + this.hexControl.id + '-text').text(this.value);
                    };
                    control.save = function() {
                        this.isSaved = true;
                        this.line = this.lineFormat.format(this.fromValue, this.toValue);
                        this.hexControl.inactive();
                        if(this.next) this.next.hexControl.turnOn();
                    };
                    control.edit = function() {
                        this.isSaved = false;
                        this.line = '';
                        this.hexControl.active();
                    };
                    if (index > 0) {
                        controls[index - 1].next = control;
                    }
                });
            };


            $scope.defaultControls = [{
                id: 'profitBlock',
                header: 'Доходность',
                buttonText: 'Применить доходность',
                fromValues: ['10%', '20%', '30%', '40%'],
                toValues: ['10%', '20%', '30%', '40%'],
                fromValue: '10%',
                toValue: '20%',
                lineFormat: 'От {0} до {1}'
            },{
                id: 'riskBlock',
                header: 'Уровень риска',
                buttonText: 'Применить уровень риска',
                fromValues: ['10%', '20%', '30%', '40%'],
                toValues: ['10%', '20%', '30%', '40%'],
                fromValue: '10%',
                toValue: '20%',
                lineFormat: 'От {0} до {1}'
            },{
                id: 'sumBlock',
                header: 'Сумма вложений',
                buttonText: 'Применить сумму вложений',
                fromValues: ['200 тыс','300 тыс','500 тыс','1 млн','и больше'],
                toValues: ['200 тыс','300 тыс','500 тыс','1 млн','и больше'],
                fromValue: '200 тыс',
                toValue: '300 тыс',
                lineFormat: 'От {0} до {1}'
            },{
                id: 'timeBlock',
                header: 'Срок вложений',
                buttonText: 'Применить срок вложений',
                fromValues: ['2 мес', '3 мес', '4 мес', '5 мес', '6 мес', '8 мес',
                '10 мес', '1 год', '1 год и 3 мес', '1 год и 6 мес'],
                toValues: ['2 мес', '3 мес', '4 мес', '5 мес', '6 мес', '8 мес',
                '10 мес', '1 год', '1 год и 3 мес', '1 год и 6 мес'],
                fromValue: '2 мес',
                toValue: '3 мес',
                lineFormat: 'От {0} до {1}'
            }];

            $scope.controls = [{
                id: 'currencyBlock',
                header: 'Валюта',
                buttonText: 'Применить валюту',
                values: ['RUB', 'USD', 'EUR'],
                value: 'RUB',
                lineFormat: '{0}'
            }];

            mapControls($scope.defaultControls, defaultParams);
            extendDefaultControls($scope.defaultControls);
            mapControls($scope.controls, $scope.optParams);
            extendControls($scope.controls);


            new hexParams({$scope: $scope, radius: 72, defaultParams: defaultParams, optParams: $scope.optParams});
            defaultParams[0].active();
            //generateControls([{id: 'currencyBlock'}]);



        }]);
