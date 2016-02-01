var app = angular.module('App',
                        ['ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap', 'duScroll', 'ui.grid', 'ui.grid.edit']);

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
            when('/partners', {
                templateUrl: 'views/template/partners.html',
                controller: 'partners'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);

app.controller('partners', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {

}]);

app.controller('admin', [ '$scope', '$log', 'restService',
    function($scope, $log, restService) {

        $scope.saveButtons = [
            productType = false,
        ];

        $scope.productType = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'name', displayName: 'Тип структурного продукта', width: "100%" },
            ],
        };

        $scope.productType.onRegisterApi = function(gridApi){
            //set gridApi on scope
            $scope.gridApi = gridApi;
            gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
                $scope.saveButtons.productType = true;
                $scope.apply();
/*                restService.updateInstrumentType(
                    rowEntity,
                    'productType',
                    function(response) {
                        $log.info("Update product type success.");
                    },
                    function() {
                        $log.error("Update product type failed.");
                    }
                );*/
            });
        };

        $scope.productType.data = [
            {
                "name": "100% защита капитала плюс гарантированная доходность",
            },
            {
                "name": "100% защита капитала без гарантированной доходности",
            }
        ];

        $scope.term = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'term', displayName: 'Срок', width: "100%" },
            ],
        };

        $scope.term.data = [
            {
                "id": 1,
                "term": "100% защита капитала плюс гарантированная доходность",
            },
            {
                "id": 2,
                "term": "100% защита капитала без гарантированной доходности",
            }
        ];


        $scope.minInvestment = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'minInvestment', displayName: 'Минимальная сумма инвестиций', width: "100%" },
            ],
        };

        $scope.issuer = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'issuer', displayName: 'Провайдер продукта', width: "100%" },
            ],
        };

        $scope.addData = function() {
            var length = $scope.productType.data.length + 1;
            $scope.productType.data.push({});
        };

        $scope.saveData = function() {
            restService.updateInstrumentType(
                rowEntity,
                'productType',
                function(response) {
                    $log.info("Update product type success.");
                },
                function() {
                    $log.error("Update product type failed.");
                }
            );
        };

/*    $scope.instrumentsType = {
        enableColumnResize : true,
        enableColumnMenus : false,
        columnDefs: [
            { field: 'type', displayName: 'Тип структурного продукта', width: 350 },
            { field: 'term', displayName: 'Срок', width: 150 },
            { field: 'minInvestment', displayName: 'Минимальная сумма инвестиций', width: 200},
            { field: 'issuer', displayName: 'Провайдер продукта', width: 200},
            { field: 'return', displayName: 'Доходность', width: 100},
            { field: 'strategy', displayName: 'Стратегия', width: 200},
            { field: 'legalType', displayName: 'Юридическая форма', width: 200},
            { field: 'payoff', displayName: 'Размер выплаты', width: 300},
            { field: 'risks', displayName: 'Риски', width: 150},
            { field: 'currency', displayName: 'Валюта', width: 100},
            { field: 'paymentPeriodicity', displayName: 'Периодичность выплаты дохода', width: 250},
        ],
    };*/

    /*$scope.getTableHeight = function() {
        var rowHeight = 30; // your row height
        var headerHeight = 30; // your header height
        return {
            height: (3 * rowHeight + headerHeight) + "px"
        };
    };*/

    //$scope.instrumentsType.enableVerticalScrollbar = 2;
    //$scope.instrumentsType.enableHorizontalScrollbar = 2;
    //$scope.instrumentsType.enableScrollbars = false;
/*    $scope.instrumentsType.data = [
        {
            "type": "100% защита капитала плюс гарантированная доходность",
            "term": "от 3 до 6 месяцев",
            "minInvestment": "До 200 тысяч рублей",
            "issuer": "БКС",
            "return": "15%",
            "strategy": "Рост цены базового актива",
            "legalType": " Все",
            "payoff": "Без ограничения максимальной доходности",
            "risks": "Все",
            "currency": "RUR",
            "paymentPeriodicity": "ежегодно",
        },
        {
            "type": "100% защита капитала без гарантированной доходности",
            "term": "свыше 12 месяцев",
            "minInvestment": "От 1 миллиона рублей",
            "issuer": "Открытие",
            "return": "19%",
            "strategy": "Барьерные стратегии",
            "legalType": " Все",
            "payoff": "С ограниченным размером максимальной доходности",
            "risks": "Все",
            "currency": "USD",
            "paymentPeriodicity": "ежегодно",
        }
    ];*/

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

app.controller('main', [ '$scope', '$log', 'restService', '$anchorScroll', '$document',
    function($scope, $log, restService, $anchorScroll, $document) {

    var selected = {};
    var currentPage = 0;
    var pages = ['page1','page2','page3', 'footer'];

    $scope.data = {};
    $scope.accordion = {};
    $scope.topProducts = [];

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

    $scope.predicate = 'name';
    $scope.reverse = true;
    $scope.order = function(predicate) {
        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
        $scope.predicate = predicate;
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

    $scope.gotoAnchorAnimated = function(id) {
        var section = angular.element(document.getElementById(id));
        $document.scrollToElementAnimated(section);
    };

    $scope.gotoAnchorAnimatedWithPage = function(page, id) {
        currentPage = id;
        $scope.gotoAnchorAnimated(page);
    };

    $scope.gotoAnchorAnimatedWithOffset = function(id, offset) {
        var section = angular.element(document.getElementById(id));
        $document.scrollToElementAnimated(section, offset);
    };

    $scope.gotoAnchorAnimatedWithPageAndOffset = function(page,id, offset) {
        currentPage = id;
        $scope.gotoAnchorAnimatedWithOffset(page, offset);
    };

    var lastScrollTime = 0;

    function MouseWheelHandler() {
        return function (e) {

            var now = new Date().getTime();

            if(now - lastScrollTime < 100) {
                lastScrollTime = now;
                return;
            }

            lastScrollTime = now;

            var direction = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));

            if (direction < 0) {
                if(currentPage < pages.length - 1) {
                    currentPage++;
                    if(currentPage == 1) {
                        $scope.gotoAnchorAnimatedWithOffset(pages[currentPage], 60);
                    } else {
                        $scope.gotoAnchorAnimated(pages[currentPage]);
                    }
                }
            } else {
                if(currentPage > 0) {
                    currentPage--;
                    $scope.gotoAnchorAnimated(pages[currentPage]);
                }
            }

        }
    }

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
        if (document.addEventListener) {
            document.addEventListener("mousewheel", MouseWheelHandler(), false);
            document.addEventListener("DOMMouseScroll", MouseWheelHandler(), false);
        } else {
            document.attachEvent("onmousewheel", MouseWheelHandler());
        }
    });

}]).value('duScrollOffset', 0).value("duScrollDuration",100);