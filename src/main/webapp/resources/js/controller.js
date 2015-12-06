var app = angular.module('app',['ngRoute'])
    .controller('main', function($scope, $http) {
        var url = "api/v1/data";
        $http.get(url).success(function(data) {
            $scope.message = data.name;
        })
    })
    .controller('main', [function() {
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
