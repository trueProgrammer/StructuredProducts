angular.module('App.admin.broker')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/broker', {
                templateUrl: 'resources/js/App/admin/broker/admin-broker.html',
                controller: 'admin-broker'
            })
        }])
.controller('admin-broker', [ '$scope', '$log', 'restService', '$location', '$rootScope',
    function($scope, $log, restService, $location, $rootScope) {
        $scope.page = 'broker';
        $scope.mode = 'add';
        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            }
        }());
        var loadBrokers = function() {
            restService.getAllBrokers(function(data) {
                $scope.brokers = data;
            }, function () {
                $log.error("Can't load brokers");
            });
        };
        loadBrokers();
        $scope.onImgChanged = function(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function(e) {
                    $("#broker-logo-preview").attr('src', e.target.result);
                    $scope.logo = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            }
        };
        $scope.modifyBroker = function(broker) {
            $('#broker-name').val(broker.name);
            $('#broker-logo-preview').attr('src', broker.logo);
            $('#actionbtn').html('Обновить');
            $scope.brokerForAdd = broker;
            $scope.logo = broker.logo;
            $scope.mode = 'modify';

        };
        $scope.cancel = function() {
            $scope.mode = 'add';
            $('#broker-form')[0].reset();
            $('#broker-logo-preview').attr('src', "");
            $('#actionbtn').html('Добавить');
            $scope.brokerForAdd = null;
        };
        $scope.removeBroker = function(broker) {
            restService.removeBroker(broker.id, function () {
                loadBrokers();
            }, function () {
                loadBrokers();
            })
        };
        $scope.onAddClick = function() {
            var img = $scope.logo;
            var name = $('#broker-name').val();
            var id;
            if ($scope.brokerForAdd) {
                id = $scope.brokerForAdd.id;
            }
            restService.addBroker(id, name, img, function() {
                $('#broker-form')[0].reset();
                $scope.mode = 'add';
                $scope.brokerForAdd = null;
                $('#actionbtn').html('Добавить');
                loadBrokers();
            }, function() {
                $('#broker-form')[0].reset();
                $scope.brokerForAdd = null;
                $('#actionbtn').html('Добавить');
                loadBrokers();
            });
        }
    }]);
