angular.module('App.createproduct')

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/createproduct', {
                templateUrl: 'resources/js/App/createproduct/createproduct.html',
                controller: 'createproduct'
            })
        }])
    .controller('createproduct', ['$scope', '$log',
        function ($scope, $log) {

            $scope.profitSaved = false;
            $scope.profitLine = "";
            $scope.profits = ['10%', '20%', '30%', '40%'];
            $scope.fromProfit = $scope.profits[0];
            $scope.toProfit = $scope.profits[0];

            $scope.setFromProfit = function (value) {
                $('#profit').text('От ' + value + ' до ' + $scope.toProfit);
                $scope.fromProfit = value;
            };
            $scope.setToProfit = function (value) {
                $('#profit').text('От ' + $scope.fromProfit + ' до ' + value);
                $scope.toProfit = value;
            };

            $scope.saveProfit = function () {
                if ($scope.fromProfit <= $scope.toProfit) {
                    $scope.profitSaved = true;
                    $scope.profitLine = " от " + $scope.fromProfit + "   до " + $scope.toProfit;
                }
            };

            var defaultParams = [{
                text: 'Доходность',
                stroke: '#91CF50',
                id: 'profit'
            }, {
                text: 'Уровень риска',
                stroke: '#FDBF01',
                id: 'riskLevel'
            }, {
                text: '+ параметр',
                stroke: '#BEBEBE',
                id: 'addParam1'
            }, {
                text: 'Сумма вложений',
                stroke: '#FD0001',
                id: 'sum'
            }, {
                text: 'Срок вложений',
                stroke: '#4774AA',
                id: 'term'
            }, {
                text: '+ параметр',
                stroke: '#BEBEBE',
                id: 'addParam2'
            }, {
                text: 'Валюта',
                stroke: '#FDBF01',
                id: 'currency'
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
            }];

            new hexParams({radius: 72, defaultParams: defaultParams, optParams: optParams});

            $('#profit').text('От ' + $scope.fromProfit + ' до ' + $scope.toProfit);
        }]);
