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
            $log.info($routeParams);
            restService.getInvestIdeaById(
                $routeParams.id,
                function(response) {
                    $log.info("Get invest idea success.");
                    $scope.investidea = response;
                },
                function() {
                    $log.error("Get invest idea failed.");
                }
            );
        }());
}]);

