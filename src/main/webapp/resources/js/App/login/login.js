angular.module('App.login')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/login', {
                templateUrl: 'resources/js/App/login/login.html',
                controller: 'login'
            })
        }])
.controller('login', [ '$scope', '$rootScope', '$log','$location', 'UserService', '$cookieStore',
    function($scope, $rootScope, $log, $location, UserService, $cookieStore) {

        $scope.error = false;

        //$scope.rememberMe = false;

        $scope.login = function() {
            UserService.authenticate($.param({username: $scope.username, password: $scope.password}),
                function(result) {
                    var authToken = result.token;
                    $rootScope.authToken = authToken;
                    //if ($scope.rememberMe) {
                    $cookieStore.put('authToken', authToken);
                    //}
                    UserService.get(function(user) {
                        $rootScope.user = user;
                        //$location.path("/");
                        $location.path("/admin");
                    });
                },
                function(result) {
                    $scope.error = true;
                    //alert('Bad result!');
                });
        };
    }]);
