angular.module('App.investidea')

.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/investidea', {
            templateUrl: 'resources/js/App/investidea/investidea.html',
            controller: 'investidea'
        })
    }])
.controller('investidea', [ '$scope', '$log', 'restService','$routeParams',
    function($scope, $log, restService, $routeParams) {
        (function() {
            restService.getInvestIdeaById(
                $routeParams.id,
                function(response) {
                    $scope.investidea = response;
                },
                function() {
                    $log.error("Get invest idea failed.");
                }
            );
        }());
}]);

