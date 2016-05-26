angular.module('App.admin.system')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/system', {
                templateUrl: 'resources/js/App/admin/systemproperties/admin-system.html',
                controller: 'admin-system'
            })
        }])
.controller('admin-system', [ '$scope', '$log', 'restService', '$location', '$rootScope',
    function($scope, $log, restService, $location, $rootScope) {
        restService.getSystemProperties(function(data) {
            $scope.properties = data;
        }, function(err){ console.error(err);});
        
        $scope.updateProperty = function(key, value) {
            restService.modifySystemProperty({key: key, value: value}, function (data) {
                console.log("successfuly modified");
            }, function(err){ console.error(err);});
        }
    }]);
