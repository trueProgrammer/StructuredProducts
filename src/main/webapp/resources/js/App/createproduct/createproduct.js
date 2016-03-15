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
            var hexData = [{
                text: 'Доходность',
                stroke: '#91CF50'
            }, {
                text: 'Уровень риска',
                stroke: '#FDBF01'
            },{
                text: '+ параметр',
                stroke: '#BEBEBE'
            }, {
                text: 'Сумма вложений',
                stroke: '#FD0001'
            },{
                text: 'Срок вложений',
                stroke: '#4774AA'
            }, {
                text: '+ параметр',
                stroke: '#BEBEBE'
            }];

            hexParams.createParams({radius: 70, hexData: hexData});

}]);
