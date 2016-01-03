var app = angular.module('App',
                        ['ngRoute', 'ui.bootstrap', 'duScroll']);

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
            when('/news', {
                templateUrl: 'views/template/news.html',
                controller: 'news'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);

app.controller('news', [ '$scope', '$log', 'restService', '$anchorScroll', '$location','$routeParams',
    function($scope, $log, restService, $anchorScroll, $location, $routeParams) {

        (function() {
                 $log.info($routeParams);
            }());

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

app.controller('main', [ '$scope', '$log', 'restService', '$anchorScroll', '$location', '$document',
    function($scope, $log, restService, $anchorScroll, $location, $document) {

    $scope.emailAlert = {
        visible: false
    };

    $scope.closeAlert = function() {
        $scope.emailAlert.visible = false;
    };

    $scope.showFailAlert = function(msg) {
       $scope.emailAlert.type = 'danger',
       $scope.emailAlert.msg = msg;
       $scope.emailAlert.visible = true;
    };

    $scope.showSuccessAlert = function(msg) {
        $scope.emailAlert.type = 'success',
        $scope.emailAlert.msg = msg;
        $scope.emailAlert.visible = true;
    }

    $scope.validateForm = function(form) {
        return form.$dirty
            && form.$valid;
    };

    $scope.sendEmail = function (form) {
        if(!$scope.validateForm(form)) {
            return;
        }
        restService.sendEmail(
            $scope.email,
            function() {
                $log.info("Email success sent.");
                $scope.showSuccessAlert("Message sent successfully.");
                $scope.email = '';
            },
            function(response) {
                $log.error("Email sending failed.");
                $scope.showFailAlert("Email sending failed.");
            }
        )
    };

    (function() {
        restService.getNews(
            function(news) {
                $log.info("Get news success.");
                $scope.news = news;
            },
            function(response) {
                $log.error("Get news failed.");
            }
        )
    }());

    $scope.gotoAnchorAnimated = function(id) {
        var section = angular.element(document.getElementById(id));
        $document.scrollToElementAnimated(section);
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

}]).value('duScrollOffset', 35);