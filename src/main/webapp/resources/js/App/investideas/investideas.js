angular.module('App.investideas')

.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/investideas', {
            templateUrl: 'resources/js/App/investideas/investideas.html',
            controller: 'investideas'
        })
}])
.controller('investideas', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {
        (function() {
            restService.getInvestIdeas(
                false,
                function(response) {
                    $scope.investideas = response;
                },
                function() {
                    $log.error("Get invest idea failed.");
                }
            );
        }());

    }]);
