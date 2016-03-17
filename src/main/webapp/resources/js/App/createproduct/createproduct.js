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

            var profitControl = {
                id: 'profitBlock'
            };
            var riskControl = {
                id: 'riskBlock'
            };
            var termControl = {
                id: 'timeBlock'
            };
            var sumControl = {
                id: 'subBlock'
            };

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
                $('#sum').text('От ' + value + ' до ' + $scope.toSum);
                $scope.fromSum = value;
            };
            $scope.setToSum = function (value) {
                $('#sum').text('От ' + $scope.fromSum + ' до ' + value);
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
                $('#time').text('От ' + value + ' до ' + $scope.toTime);
                $scope.fromTime = value;
            };
            $scope.setToTime = function (value) {
                $('#time').text('От ' + $scope.fromTime + ' до ' + value);
                $scope.toTime = value;
            };
            $scope.saveTime = function () {
                if ($scope.fromTime <= $scope.toTime) {
                    $scope.timeSaved = true;
                    $scope.timeLine = " от " + $scope.fromTime + "   до " + $scope.toTime + "  ";
                    $scope.sumDisabled = false;
                    $("#sumBlock").css("opacity","1");
                    termControl.inactive();
                    sumControl.turnOn();
                }
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
                    $scope.profitSaved = true;
                    $scope.profitLine = " от " + $scope.fromProfit + "   до " + $scope.toProfit + "  ";
                    $scope.riskDisabled = false;
                    $("#riskBlock").css("opacity","1");
                    profitControl.inactive();
                    riskControl.turnOn();
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


            var defaultParams = [{
                text: 'Доходность',
                stroke: '#91CF50',
                id: 'profit',
                boundedControl: profitControl,
                value: function() {
                    return 'От ' + $scope.fromProfit + ' до ' + $scope.toProfit;
                }
            }, {
                text: 'Уровень риска',
                stroke: '#FDBF01',
                id: 'risk',
                boundedControl: riskControl,
            }, {
                text: '+ параметр',
                stroke: '#BEBEBE',
                id: 'addParam1'
            }, {
                text: 'Сумма вложений',
                stroke: '#FD0001',
                boundedControl: sumControl,
                id: 'sum'
            }, {
                text: 'Срок вложений',
                stroke: '#4774AA',
                boundedControl: termControl,
                id: 'term'
            }, {
                text: '+ параметр',
                stroke: '#BEBEBE',
                id: 'addParam2'
            }];

            var optParams = [ {
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
                id: 'currency'
            }];

            new hexParams({radius: 72, defaultParams: defaultParams, optParams: optParams});
            profitControl.active();
        }]);
