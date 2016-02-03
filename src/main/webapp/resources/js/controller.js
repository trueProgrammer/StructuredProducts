var app = angular.module('App',
                        ['ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap', 'duScroll', 'ui.grid', 'ui.grid.edit', 'ui.grid.selection']);

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

        var selection = {
            productType : [],
            term : [],
            investment : [],
            issuer : [],
            return : [],
            strategy : [],
            legal : [],
            payoff : [],
        };

        $scope.productTypeAlert = { visible: false };
        $scope.termAlert = { visible: false };
        $scope.investmentAlert = { visible: false };
        $scope.issuerAlert = { visible: false };
        $scope.returnAlert = { visible: false };
        $scope.strategyAlert = { visible: false };
        $scope.legalTypeAlert = { visible: false };
        $scope.payoffAlert = { visible: false };

        $scope.closeAlert = function() {
            $scope.productTypeAlert.visible = false;
            $scope.termAlert.visible = false;
            $scope.investmentAlert.visible = false;
            $scope.issuerAlert.visible = false;
            $scope.returnAlert.visible = false;
            $scope.strategyAlert.visible = false;
            $scope.legalTypeAlert.visible = false;
            $scope.payoffAlert.visible = false;
        };

        $scope.showFailAlert = function(msg, id) {
            $scope[id].msg = msg;
            $scope[id].visible = true;
        };

        $scope.saveButtonsDisabled = {
            productType : true,
            term : true,
            investment : true,
            issuer : true,
            return : true,
            strategy : true,
            legal : true,
            payoff : true,
        };

        $scope.productType = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'name', displayName: 'Тип структурного продукта', width: "94%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.productType = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['productType'].push(row.entity);
                    } else {
                        var index = selection['productType'].indexOf(row.entity);
                        selection['productType'].splice(index, 1);
                    }
                });
            }
        };

        $scope.term = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'min', displayName: 'Минимум', width: "47%" },
                { field: 'max', displayName: 'Максимум', width: "47%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.term = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['term'].push(row.entity);
                    } else {
                        var index = selection['term'].indexOf(row.entity);
                        selection['term'].splice(index, 1);
                    }
                });
            }
        };

        $scope.investment = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'min', displayName: 'Минимум', width: "47%" },
                { field: 'max', displayName: 'Максимум', width: "47%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.investment = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['investment'].push(row.entity);
                    } else {
                        var index = selection['investment'].indexOf(row.entity);
                        selection['investment'].splice(index, 1);
                    }
                });
            }
        };

        $scope.issuer = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'name', displayName: 'Провайдер продукта', width: "94%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.issuer = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['issuer'].push(row.entity);
                    } else {
                        var index = selection['issuer'].indexOf(row.entity);
                        selection['issuer'].splice(index, 1);
                    }
                });
            }
        };

        $scope.return = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'count', displayName: 'Доходность', width: "94%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.return = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['return'].push(row.entity);
                    } else {
                        var index = selection['return'].indexOf(row.entity);
                        selection['return'].splice(index, 1);
                    }
                });
            }
        };

        $scope.strategy = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'name', displayName: 'Стратегия', width: "94%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.strategy = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['strategy'].push(row.entity);
                    } else {
                        var index = selection['strategy'].indexOf(row.entity);
                        selection['strategy'].splice(index, 1);
                    }
                });
            }
        };

        $scope.legalType = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'name', displayName: 'Юридическая форма', width: "94%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.legalType = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['legalType'].push(row.entity);
                    } else {
                        var index = selection['legalType'].indexOf(row.entity);
                        selection['legalType'].splice(index, 1);
                    }
                });
            }
        };

        $scope.payoff = {
            enableColumnMenus : false,
            columnDefs: [
                { field: 'name', displayName: 'Размер выплаты', width: "94%" },
            ],
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled.payoff = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        selection['payoff'].push(row.entity);
                    } else {
                        var index = selection['payoff'].indexOf(row.entity);
                        selection['payoff'].splice(index, 1);
                    }
                });
            }
        };

        $scope.addData = function(id) {
            var length = $scope[id].data.length + 1;
            $scope[id].data.push({});
        };

        $scope.saveData = function(id) {
            $scope.saveButtonsDisabled[id] = true;
            restService.updateInstrumentType(
                $scope[id].data,
                id,
                function(response) {
                    $log.info("Update " + id + " success.");
                },
                function(response) {
                    $scope.showFailAlert(response, id+'Alert');
                    getInstrumentType(id);
                    $log.error("Update " + id + " failed.");
                }
            );
        };

        $scope.removeData = function(id) {
            restService.deleteInstrumentType(
                selection[id],
                id,
                function(response) {
                    $log.info("Delete " + id + " success.");
                    selection[id] = [];
                    getInstrumentType(id);
                },
                function(response) {
                    $scope.showFailAlert(response, id+'Alert');
                    $log.error("Delete " + id + " failed.");
                    selection[id] = [];
                    getInstrumentType(id);
                }
            );
        };

        function getInstrumentType(entity) {
            restService.getInstrumentType(
                entity,
                function(values) {
                    $scope[entity].data = values;
                },
                function(response) {
                    $log.error("Get instrument values for "+entity+" failed.");
                }
            )
        }

        (function() {
            getInstrumentType('productType');
            getInstrumentType('term');
            getInstrumentType('investment');
            getInstrumentType('issuer');
            getInstrumentType('return');
            getInstrumentType('strategy');
            getInstrumentType('legalType');
            getInstrumentType('payoff');
        }());

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

    var selected = {};
    var currentPage = 0;
    var pages = ['page1','page2','page3', 'footer'];

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

        $scope.$on('mousewheel', MouseWheelHandler());
        $scope.$on('DOMMouseScroll', MouseWheelHandler());

        if (document.addEventListener) {
            document.addEventListener("mousewheel", MouseWheelHandler(), false);
            document.addEventListener("DOMMouseScroll", MouseWheelHandler(), false);
        } else {
            document.attachEvent("onmousewheel", MouseWheelHandler());
        }
    });

}]).value('duScrollOffset', 0).value("duScrollDuration",100);