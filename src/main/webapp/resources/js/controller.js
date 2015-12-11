var app = angular.module('App',
                        ['ngRoute']);

app.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'views/template/main.html',
                controller: 'main'
            }).
            when('/about', {
                templateUrl: 'views/template/about.html',
                controller: 'main'
            }).
            when('/contacts', {
                templateUrl: 'views/template/contacts.html',
                controller: 'main'
            }).
            when('/partners', {
                templateUrl: 'views/template/partners.html',
                controller: 'main'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);

app.controller('main', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {

    $scope.emailAlert = {
        type: 'danger',
        msg: 'Email was send.',
        visible: false
    };

    $scope.closeAlert = function() {
        $scope.emailAlert.visible = false;
    };

    $scope.validateForm = function(form) {
        return form.$dirty
            && form.$valid;
    };

    $scope.sendEmail = function () {
        restService.sendEmail(
            $scope.email,
            function() {
                $log.info("Email success sended.");
                $scope.emailAlert.visible = true;
            },
            function(response) {
                $log.error("Email sending failed." + response.statusText);
                $scope.emailAlert.visible = true;
            }
        )
    };

    //init app function
    angular.element(document).ready(function () {
        App.init();
        jQuery("#layerslider").layerSlider({
            navStartStop: false,
            skin: 'fullwidth',
            responsive : true,
            responsiveUnder : 960,
            layersContainer : 960,
            autoPlayVideos: true,
            skinsPath: 'resources/assets/plugins/layer-slider/layerslider/skins/'
        });
    });

}]);