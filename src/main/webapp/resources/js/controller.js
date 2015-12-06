angular.module('app',[])
    .controller('main', function($scope, $http) {
        var url = "api/v1/data";
        $http.get(url).success(function(data) {
            $scope.message = data.name;
        })
    });