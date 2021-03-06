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
        $scope.emails = [{num: 1, email: ""}];
        $scope.page = 'broker';
        $scope.mode = 'add';
        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            }
        }());
        var loadBrokers = function(onSuccess, onFail) {
            restService.getAllBrokers(function(data) {
                $scope.brokers = data;
                if (onSuccess !== undefined) {
                    onSuccess();
                }
            }, function () {
                $log.error("Can't load brokers");
                if (onFail !== undefined) {
                    onFail();
                }
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
            if (broker.emails && broker.emails.length > 0) {
                $scope.emails = broker.emails.map(function (e, i) {e.num = i+1; return e;});
            } else {
                $scope.emails = [{num: 1, email: ""}];
            }
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
            var emails = $scope.emails.filter(function(e) {return !!e.email});
            var id;
            if ($scope.brokerForAdd) {
                id = $scope.brokerForAdd.id;
            }
            restService.addBroker(id, name, img, emails, function() {
                var broker = $scope.brokerForAdd;
                $('#broker-form')[0].reset();
                $scope.mode = 'add';
                $scope.brokerForAdd = null;
                $scope.emails = [{num: 1, email: ""}];
                $('#actionbtn').html('Добавить');
                loadBrokers(function() {
                    for(var i = 0; i < $scope.brokers.length; i++) {
                        if (broker.id === $scope.brokers[i].id) {
                            broker = $scope.brokers[i];
                            break;
                        }
                    }
                    $scope.modifyBroker(broker);
                });

            }, function() {
                $('#broker-form')[0].reset();
                $scope.brokerForAdd = null;
                $scope.emails = [{num: 1, email: ""}];
                $('#actionbtn').html('Добавить');
                loadBrokers();
            });
        };
        $scope.onAddEmailClick = function() {
            $scope.emails.push({num: $scope.emails.length + 1, email: ""});
        };
        $scope.onModifyEmail = function(num, email) {
            $scope.emails[num - 1].email = email;
        }
    }]);
