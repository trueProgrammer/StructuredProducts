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

        $scope.saveButtonsDisabled = true;
        $scope.selection = [];

        var dropDownValues = {};
        var returnValues = function(entity, array) {
                restService.getInstrumentType(
                entity,
                function(values) {
                    dropDownValues[entity] = values;
                    array.editDropdownOptionsArray = values;
                },
                function(response) {
                    $log.error("Get instrument values for " + entity + " failed.");
                    return [];
                }
            );
        };


        $scope.selected;
        var columns = {
            productType : [
                { field: 'name', displayName: 'Тип структурного продукта', width: "94%" },
            ],
            term : [
                { field: 'min', displayName: 'Минимум', width: "47%" },
                { field: 'max', displayName: 'Максимум', width: "47%" },
            ],
            investment : [
                    { field: 'min', displayName: 'Минимум', width: "47%" },
                    { field: 'max', displayName: 'Максимум', width: "47%" },
            ],
            issuer : [
                    { field: 'name', displayName: 'Провайдер продукта', width: "94%" },
            ],
            return : [
                { field: 'count', displayName: 'Доходность', width: "94%" },
            ],
            strategy : [
                { field: 'name', displayName: 'Стратегия', width: "94%" },
            ],
            legalType: [
                { field: 'name', displayName: 'Юридическая форма', width: "94%" },
            ],
            payoff: [
                { field: 'name', displayName: 'Размер выплаты', width: "94%" },
            ],
            underlayingType: [
                { field: 'name', displayName: 'Тип базового актива', width: "94%" },
            ],
            underlaying: [
                { field: 'name', displayName: 'Базовый актив', width: "47%" },
                { field: 'type', editField: 'type', displayName: 'Тип базового актива', width: "47%",
                    cellFilter: "griddropdown:this",
                    editableEntity: 'underlayingType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            ],
        };

        function getValues(entity) {
            restService.getInstrumentType(
                entity,
                function(values) {
                    $scope.table.data = values;
                },
                function(response) {
                    $log.error("Get instrument values for "+entity+" failed.");
                }
            )
        };

        $scope.table = {
            enableColumnMenus : false,
            onRegisterApi : function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    $scope.saveButtonsDisabled = false;
                });
                gridApi.selection.on.rowSelectionChanged($scope,function(row){
                    if(row.isSelected) {
                        $scope.selection.push(row.entity);
                    } else {
                        var index = $scope.selection.indexOf(row.entity);
                        $scope.selection.splice(index, 1);
                    }
                });
            }
        };

        $scope.selectTable = function(id) {
            $scope.selected = id;
            $scope.selection = [];
            for(field in columns[id]) {
                if (columns[id][field].editDropdownOptionsArray) {
                    returnValues(columns[id][field].editableEntity, columns[id][field]);
                }
            }
            $scope.table.columnDefs = columns[id];
            getValues(id);
        };

        $scope.alert = { visible: false };

        $scope.closeAlert = function() {
            $scope.alert.visible = false;
        };

        $scope.showFailAlert = function(msg, id) {
            $scope.alert.msg = msg;
            $scope.alert.visible = true;
        };

        $scope.addData = function() {
            var length = $scope.table.data.length + 1;
            $scope.table.data.push({});
        };

        $scope.saveData = function() {
            $scope.saveButtonsDisabled = true;
            restService.updateInstrumentType(
                $scope.table.data,
                $scope.selected,
                function(response) {
                    $log.info("Update " + $scope.selected + " success.");
                    getValues($scope.selected);
                },
                function(response) {
                    $scope.showFailAlert(response, id+'Alert');
                    getValues($scope.selected);
                    $log.error("Update " + $scope.selected + " failed.");
                }
            );
        };

        $scope.removeData = function() {
            restService.deleteInstrumentType(
                $scope.selection,
                $scope.selected,
                function(response) {
                    $log.info("Delete " + $scope.selected + " success.");
                    $scope.selection = [];
                    getValues($scope.selected);
                },
                function(response) {
                    $scope.showFailAlert(response, $scope.selected+'Alert');
                    $log.error("Delete " + $scope.selected + " failed.");
                    $scope.selection = [];
                    getValues($scope.selected);
                }
            );
        };

        (function() {
            $scope.selectTable('productType');
        }());


}])
    .filter('griddropdown', function() {
        return function (input, context) {
            var map = context.col.colDef.editDropdownOptionsArray;
            var valueField = context.col.colDef.editDropdownValueLabel;
            var initial = context.row.entity[context.col.field];
            if (typeof map !== "undefined") {
                for (var i = 0; i < map.length; i++) {
                    if (map[i]['id'] == input['id']) {
                        return map[i][valueField];
                    }
                }
            } else if (initial) {
                return initial;
            }
            return input;
        };
    });


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