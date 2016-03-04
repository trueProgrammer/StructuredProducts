angular.module('App.admin.investidea')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/investidea', {
                templateUrl: 'resources/js/App/admin/investidea/admin-investidea.html',
                controller: 'admin-investidea'
            })
        }])
.controller('admin-investidea', [ '$scope', '$log', 'restService', '$rootScope', '$location',
    function($scope, $log, restService, $rootScope, $location) {
        $scope.page = 'investidea';
        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            }
        }());
        var updateInvestIdeas = function() {
            restService.getInvestIdeas(
                true,
                function(investideas) {
                    $log.info("Get invest ideas.");
                    $scope.data = {};
                    $scope.data.investideas = investideas;
                },
                function(response) {
                    $log.error("Get invest ideas failed.");
                }
            );
        };
        var loadBrokers = function() {
            restService.getAllBrokers(function(data) {
                $scope.brokersData = data;
            }, function () {
                $log.error("Can't load brokers");
            });
        };
        updateInvestIdeas();
        loadBrokers();

        $scope.mode = 'add';
        $scope.updateBrokerImg = function() {
            if($scope.selectedBroker){
                $("#broker-logo").attr("src", $scope.selectedBroker.logo);
            } else{
                $("#broker-logo").attr("src", "");
            }
        };

        $scope.modifyIdea = function(idea) {
            $('#title').val(idea.title);
            $('#content').val(idea.content);
            $('#broker-logo').attr('src', idea.broker.logo);
            $('#actionbtn').html('Обновить');
            $scope.selectedBroker = idea.broker;
            $scope.onMainPage = idea.mainPage;
            $scope.mode = 'modify';
            $scope.ideaId = idea.id;
        };

        $scope.cancel = function() {
            $('#investidea-form')[0].reset();
            $scope.mode = 'add';
            $('#broker-logo').attr('src', '');
            $scope.ideaId = null;
            $scope.selectedBroker = null;
            $('#actionbtn').html('Добавить');
        };

        $scope.removeIdea = function(idea) {
            restService.removeIdea(idea.id, function () {
                updateInvestIdeas();
            }, function(data) {console.log(data)});
        };
        $scope.addIdea = function() {
            if ($scope.selectedBroker) {
                var obj = {
                    id: $scope.ideaId,
                    title: $('#title').val(),
                    content: $('textarea#content').val(),
                    broker: $scope.selectedBroker.id,
                    onMainPage: !!$scope.onMainPage
                };
                restService.addIdea(obj, function(data) {
                    console.log("idea successfully saved");
                    $('#investidea-form')[0].reset();
                    $('#broker-logo').attr('src', '');
                    $scope.ideaId = null;
                    $scope.selectedBroker = null;
                    updateInvestIdeas();
                }, function(data) {
                    console.log("idea not saved");
                    $scope.ideaId = null;
                    $scope.selectedBroker = null;
                    $('#investidea-form')[0].reset();
                    $('#broker-logo').attr('src', '');
                    updateInvestIdeas();
                })
            }
        }
    }]);

