angular.module('App.admin.csv')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/csv', {
                templateUrl: 'resources/js/App/admin/csv/admin-csv.html',
                controller: 'adminCsvCtrl'
            })
        }])
.controller('adminCsvCtrl', [ '$scope', '$log', 'restService', '$location', '$rootScope',
    function($scope, $log, restService, $location, $rootScope) {
        $scope.page = 'csv';
        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            }
        }());

        $scope.broker;
        var loadBrokers = function() {
            restService.getAllBrokers(function(data) {
                data.unshift({id:-1, name:""});
                $scope.brokers = data;
                $scope.broker = data[0];
            }, function () {
                $log.error("Can't load brokers");
            });
        };
        loadBrokers();


        $scope.clickUploadButton = function() {
            $('#uploadFile').trigger('click');
        };
        $scope.clickDownloadButton = function() {
            restService.downloadCsv(function(data) {
                var blob = new Blob([data], {type: "text/plain; charset=utf-8"});
                saveAs(blob, "product.csv");
            }, function() {
                $log.error("error");
            });
        };
        $scope.uploadCsv = function(file, e) {
            restService.uploadProductsCsv(file,
                $scope.broker.name,
                function(){
                    console.log('Successfully load csv');
                    /*$scope.selectTable("product");*/
                    $location.path("/admin");
                },
                function(data){
                    var msg = 'Error occurs during load csv:' + data;
                    $scope.showFailAlert(msg);
                    console.log(msg);
                }
            );
            e.wrap('<form>').closest('form').get(0).reset();
            e.unwrap();

        };

        $scope.alert = {
            visible: false
        };
        $scope.closeAlert = function() {
            $scope.alert.visible = false;
        };
        $scope.showFailAlert = function(msg) {
            $scope.alert.msg = msg;
            $scope.alert.visible = true;
        };
}]);
