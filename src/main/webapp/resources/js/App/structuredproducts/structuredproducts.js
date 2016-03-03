angular.module('App.structuredproducts')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/structuredproducts', {
                templateUrl: 'resources/js/App/structuredproducts/structuredproducts.html',
                controller: 'structuredproductsCtrl'
            })
        }])
.controller('structuredproductsCtrl', [ '$scope',
    function($scope) {

        var selected = {};

        $scope.select = function (id, tab) {
            if (selected[id] === undefined) {
                selected[id] = tab;
            } else {
                if (selected[id] === tab) {
                    selected[id] = undefined;
                } else {
                    selected[id] = tab;
                }
            }
        };

        $scope.isSelected = function (id, tab) {
            if (selected[id] === undefined) {
                return false;
            } else {
                if( selected[id] === tab ) {
                    return true;
                }
            }
            return false;
        };

    }]);
