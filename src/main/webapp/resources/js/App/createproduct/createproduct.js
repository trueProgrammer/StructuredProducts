angular.module('App.createproduct')

.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/createproduct', {
            templateUrl: 'resources/js/App/createproduct/createproduct.html',
            controller: 'createproduct'
        })
    }])
.controller('createproduct', [ '$scope', '$log',
        function($scope, $log) {

    $scope.profitSaved = false;
    $scope.profitLine = "";
    $scope.profits = ['10%', '20%', '30%', '40%'];
    $scope.fromProfit = $scope.profits[0];
    $scope.toProfit = $scope.profits[0];

    $scope.setFromProfit = function(value) {
        $scope.fromProfit = value;
    };
    $scope.setToProfit = function(value) {
        $scope.toProfit = value;
    };

    $scope.saveProfit = function() {
        if($scope.fromProfit <= $scope.toProfit) {
            $scope.profitSaved = true;
            $scope.profitLine = " от " + $scope.fromProfit + "   до " + $scope.toProfit;
        }
    };
            var defaultParams = [{
                text: 'Доходность',
                stroke: '#91CF50'
            }, {
                text: 'Уровень риска',
                stroke: '#FDBF01'
            },{
                text: '+ параметр',
                stroke: '#BEBEBE',
                id: 'addParam1',
                onclick: 'onParamClick()'
            }, {
                text: 'Сумма вложений',
                stroke: '#FD0001'
            },{
                text: 'Срок вложений',
                stroke: '#4774AA'
            }, {
                text: '+ параметр',
                stroke: '#BEBEBE',
                id: 'addParam2',
                onclick: 'onParamClick()'
            }];

            var optParams = [{
                text: 'Тип продукта',
                stroke: '#91CF50'
            }, {
                text: 'Валюта',
                stroke: '#FDBF01'
            },{
                text: 'Размер выплат',
                stroke: '#4774AA',
            }, {
                text: 'Тип базового актива',
                stroke: '#FDBF01'
            },{
                text: 'Риски',
                stroke: '#4774AA'
            }, {
                text: 'Периодичность выплат',
                stroke: '#FDBF01',
            }, {
                text: 'Стратегия',
                stroke: '#4774AA',
            }];

            new hexParams({radius: 70, defaultParams: defaultParams, optParams: optParams});

}]);
