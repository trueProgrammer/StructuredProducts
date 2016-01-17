var app = angular.module('App',
                        ['ngRoute', 'ui.bootstrap']);

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
            when('/investidea', {
                templateUrl: 'views/template/investidea.html',
                controller: 'investidea'
            }).
            when('/investideas', {
                templateUrl: 'views/template/investideas.html',
                controller: 'investideas'
            }).
            when('/admin', {
                templateUrl: 'views/template/admin.html',
                controller: 'admin'
            }).
            when('/products', {
                templateUrl: 'views/template/products.html',
                controller: 'products'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);

app.controller('admin', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {

    $scope.uploadFile = function(){
       restService.uploadFileToUrl($scope.myFile);
    };

}]);

app.controller('products', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {

    var selected = {};

    $scope.data = {};
    $scope.accordion = {};
    $scope.topProducts = [];

    $scope.accordion.select = function (id, tab) {
            if (selected[id] === undefined) {
                selected[id] = tab;
            } else {
                if (selected[id] === tab) {
                    selected[id] = undefined;
                } else {
                    selected[id] = tab;
                }
            }
        }

        $scope.accordion.isSelected = function (id, tab) {
            if (selected[id] === undefined) {
                return false;
            } else {
                if( selected[id] === tab ) {
                    return true;
                }
            }
            return false;
        }

        $scope.topProductsChange = function () {
                restService.getTopProducts(
                    $scope.data.timeType,
                    $scope.data.productType,
                    function(response) {
                        $log.info("Get top products success.");
                        $scope.topProducts = response;
                    },
                    function() {
                        $log.error("Get top products success.");
                    }
                )
            };

        //get invest ideas
        (function() {
            restService.getInvestIdeas(
                true,
                function(investideas) {
                    $log.info("Get invest ideas.");
                    $scope.data.investideas = investideas;
                },
                function(response) {
                    $log.error("Get invest ideas failed.");
                }
            )
        }());

        //get time types
        (function() {
            restService.getTimeTypes(
                function(timeTypes) {
                    $log.info("Get time types success.");
                    $scope.data.timeTypes = timeTypes;
                    $scope.data.timeType = timeTypes[0];

                    //load data to table
                    $scope.topProductsChange();
                },
                function(response) {
                    $log.error("Get time types failed.");
                }
            )
        }());

        //get product types
        (function() {
            restService.getProductTypes(
                function(productTypes) {
                    $log.info("Get product types success.");
                    $scope.data.productTypes = productTypes;
                    $scope.data.productType = productTypes[0];

                    //load data to table
                    $scope.topProductsChange();
                },
                function(response) {
                    $log.error("Get product types failed.");
                }
            )
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

app.controller('investideas', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {

        (function() {
             restService.getInvestIdeas(
                false,
                function(response) {
                    $log.info("Get invest idea success.");
                    $scope.investideas = response;
                },
                function() {
                    $log.error("Get invest idea failed.");
                }
             );
        }());

}]);

app.controller('investidea', [ '$scope', '$log', 'restService','$routeParams',
    function($scope, $log, restService, $routeParams) {

        (function() {
             $log.info($routeParams);
             restService.getInvestIdeaById(
                $routeParams.id,
                function(response) {
                    $log.info("Get invest idea success.");
                    $scope.investidea = response;
                },
                function() {
                    $log.error("Get invest idea failed.");
                }
             );
        }());

}]);

app.controller('news', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {

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

app.controller('main', [ '$scope', '$log', 'restService', '$location',
    function($scope, $log, restService, $location) {

    $scope.go = function ( path ) {
      $location.path(path);
    };

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