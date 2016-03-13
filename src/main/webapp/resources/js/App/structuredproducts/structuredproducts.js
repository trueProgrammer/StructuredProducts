angular.module('App.structuredproducts')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/structuredproducts', {
                templateUrl: 'resources/js/App/structuredproducts/structuredproducts.html',
                controller: 'structuredproductsCtrl'
            })
        }])
.controller('structuredproductsCtrl', [ '$scope', '$anchorScroll', '$document',
    function($scope,  $anchorScroll, $document) {

        var selected = {
            0 : false,
            1 : false,
            2 : false,
            3 : false,
        };

        $scope.click = function(section) {
            selected[section] = !selected[section];
        };

        $scope.isSelected = function(section) {
            return selected[section];
        };

        $scope.isNotSelected = function(section) {
            return !selected[section];
        };

        $scope.gotoAnchorAnimated = function(id) {
            if(id === 'kind') {
                selected['3'] = true;
            } else if (id === 'protection') {
                selected['4'] = true;
            }
            $scope.gotoAnchorAnimatedWithOffset(id, 80);
        };

        $scope.gotoAnchorAnimatedWithOffset = function(id, offset) {
            var section = angular.element(document.getElementById(id));
            $document.scrollToElementAnimated(section, offset);
        };

        //init app function
        angular.element(document).ready(function () {
            App.init();
        });

    }]).value("duScrollDuration",100);
