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
            $('#brokers-dropdown').val(idea.broker.id);
            $('#broker-logo').attr('src', idea.broker.logo);
            $('#actionbtn').html('Обновить');
            $scope.mode = 'modify';
            $scope.ideaId = idea.id;
        };

        $scope.cancel = function() {
            $('#investidea-form')[0].reset();
            $scope.mode = 'add';
            $('#actionbtn').html('Добавить');
        };

        $scope.removeIdea = function(idea) {
            alert(JSON.stringify(idea));
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
                    broker: $scope.selectedBroker.id
                };
                restService.addIdea(obj, function(data) {
                    console.log("idea successfully saved");
                }, function(data) {
                    console.log("idea not saved")
                    $('#investidea-form')[0].reset();
                })
            }
        }
    }]);

