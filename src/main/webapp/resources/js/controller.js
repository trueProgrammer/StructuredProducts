var app = angular.module('App',
                        ['ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap', 'duScroll', 'ngCookies', 'App.services',
                            'ui.grid', 'ui.grid.edit', 'ui.grid.selection']);

app.config(['$routeProvider', '$httpProvider',
    function($routeProvider, $httpProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'views/template/main.html',
                controller: 'main'
            }).
            when('/about', {
                templateUrl: 'views/template/about.html',
                controller: 'empty'
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
                controller: 'empty'
            }).
            when('/structuredproducts', {
                templateUrl: 'views/template/structuredproducts.html',
                controller: 'empty'
            }).
            when('/login', {
                templateUrl: 'views/template/login.html',
                controller: 'login'
            }).
            when('/investproduct', {
                templateUrl: 'views/template/investproduct.html',
                controller: 'investproduct'
            }).
            otherwise({
                redirectTo: '/'
            });

        //$locationProvider.html5Mode(true);

        /* Register error provider that shows message on failed requests or redirects to login page on
         * unauthenticated requests */
        $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
                return {
                    'responseError': function(rejection) {
                        var status = rejection.status;
                        var config = rejection.config;
                        var method = config.method;
                        var url = config.url;

                        if (status == 401) {
                            $location.path( "/login" );
                        } else {
                            $rootScope.error = method + " on " + url + " failed with status " + status;
                        }

                        return $q.reject(rejection);
                    }
                };
            }
        );

        /* Registers auth token interceptor, auth token is either passed by header or by query parameter
         * as soon as there is an authenticated user */
        $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
                return {
                    'request': function(config) {
                        var isRestCall = config.url.indexOf('api') == 0;
                        if (isRestCall && angular.isDefined($rootScope.authToken)) {
                            var authToken = $rootScope.authToken;
                            //if (exampleAppConfig.useAuthTokenHeader) {
                                config.headers['X-Auth-Token'] = authToken;
/*                            } else {
                                config.url = config.url + "?token=" + authToken;
                            }*/
                        }
                        return config || $q.when(config);
                    }
                };
            }
        );

    }])
    .run(function($rootScope, $location, $cookieStore, UserService) {

        /!* Try getting valid user from cookie or go to login page *!/
        var originalPath = $location.path();
        //$location.path("/login");

        var authToken = $cookieStore.get('authToken');
        if (authToken !== undefined && originalPath === '/admin') {
            $rootScope.authToken = authToken;
            UserService.get(function(user) {
                $rootScope.user = user;
                //$location.path(originalPath);
            });
        }

    });

app.controller('login', [ '$scope', '$rootScope', '$log','$location', 'UserService', '$cookieStore',
    function($scope, $rootScope, $log, $location, UserService, $cookieStore) {

        $scope.error = false;

        //$scope.rememberMe = false;

        $scope.login = function() {
            UserService.authenticate($.param({username: $scope.username, password: $scope.password}),
                function(result) {
                    var authToken = result.token;
                    $rootScope.authToken = authToken;
                    //if ($scope.rememberMe) {
                        $cookieStore.put('authToken', authToken);
                    //}
                    UserService.get(function(user) {
                        $rootScope.user = user;
                        //$location.path("/");
                        $location.path("/admin");
                    });
                },
                function(result) {
                    $scope.error = true;
                    //alert('Bad result!');
                });
        };

}]);

var services = angular.module('App.services', ['ngResource']);

services.factory('UserService', function($resource) {

    return $resource('api/v1/user/', {},
        {
            authenticate: {
                method: 'POST',
                params: {'action' : 'authenticate'},
                headers : {'Content-Type': 'application/x-www-form-urlencoded'}
            },
        }
    );
});

app.controller('empty', [ '$scope',
    function($scope) {
}]);

app.controller('investproduct', ['$scope', '$log', 'restService',
    function($scope, $log, restService) {
        restService.getAllProducts(
            function (response) {
                $log.info("Get all products success.");
                $scope.products = response;

                $scope.highRiskProducts = 0;
                $scope.middleRiskProducts = 0;
                $scope.lowRiskProducts = 0;

                response.forEach(function(item) {
                    if (item.risk === 'Высокий'){
                        $scope.highRiskProducts++;
                    }
                    else if(item.risk === 'Средний') {
                        $scope.middleRiskProducts++;
                    } else {
                        $scope.lowRiskProducts++;
                    }
                });
            },
            function () {
                $log.error("Get all products failure.");
            }
        );

        var typesList = [];

        angular.element(document).ready(function () {
            var greenEllipse = angular.element(document.getElementById("green-ellipse"));
            var redEllipse = angular.element(document.getElementById("red-ellipse"));
            var blueEllipse = angular.element(document.getElementById("blue-ellipse"));

            var greenText = angular.element(document.getElementById("green-text"));
            var redText = angular.element(document.getElementById("red-text"));
            var blueText = angular.element(document.getElementById("blue-text"));

            var blueArc = angular.element(document.getElementById("blue-arc"));
            var redArc = angular.element(document.getElementById("red-arc"));
            var greenArc = angular.element(document.getElementById("green-arc"));

            var greenButton = angular.element(document.getElementById("green-button"));
            var blueButton = angular.element(document.getElementById("blue-button"));
            var redButton = angular.element(document.getElementById("red-button"));

            var green = $("#green-ellipse");
            var red = $("#red-ellipse");
            var blue = $("#blue-ellipse");
            var main = $("#main-ellipse");

            var mainEllipse = angular.element(document.getElementById("main-ellipse"));

            var clicked = {};

            function mouseOverLine(type, line, arc, button) {
                line.css("visibility", "visible");
                arc.attr("stroke-width", "9");
                button.css("box-shadow", "0px 0px 5px 3px #aead95");
                ellipsesFill();
            };
            function mouseOutLine(type, line, arc, button) {
                if(clicked[type]) {
                    return
                }
                line.css("visibility", "hidden");
                arc.attr("stroke-width", "5");
                button.css("box-shadow", "none");
                ellipsesUnfill();
            };
            function ellipsesFill() {
                main.attr("fill", "#EDEFF3");
                green.attr("fill", "#EDEFF3");
                red.attr("fill", "#EDEFF3");
                blue.attr("fill", "#EDEFF3");
            };
            function ellipsesUnfill() {
                if(clicked['green'] === true || clicked['red'] === true || clicked['blue'] === true) {
                    return;
                }
                main.attr("fill", "white");
                green.attr("fill", "white");
                red.attr("fill", "white");
                blue.attr("fill", "white");
            };

            mainEllipse.on("mouseover", function(event) {
                ellipsesFill();
            });
            mainEllipse.on("mouseout", function(event) {
                ellipsesUnfill();
            });

            function mouseOverGreen() {
                mouseOverLine('green',greenLine, greenArc, greenButton);
            };
            function mouseOutGreen() {
                mouseOutLine('green',greenLine, greenArc, greenButton);
            };
            function mouseOverRed() {
                mouseOverLine('red',redLine, redArc, redButton);
            };
            function mouseOutRed() {
                mouseOutLine('red',redLine, redArc, redButton);
            };
            function mouseOverBlue() {
                mouseOverLine('blue',blueLine, blueArc, blueButton);
            };
            function mouseOutBlue() {
                mouseOutLine('blue',blueLine, blueArc, blueButton);
            };
            $scope.click = function(type, over, out) {
                if(clicked[type]) {
                    clicked[type] = false;
                    out();
                } else {
                    clicked[type] = true;
                    over();
                }
            };
            function greenClick() {
                $scope.filterByRisk('Низкий');
                //$scope.click('green',mouseOverGreen, mouseOutGreen);
            };
            function redClick() {
                $scope.filterByRisk('Средний');
                //$scope.click('green',mouseOverGreen, mouseOutGreen);
            };
            function blueClick() {
                $scope.filterByRisk('Высокий');
                //$scope.click('green',mouseOverGreen, mouseOutGreen);
            };
            $scope.filterByType = function(risk, event) {
                if(risk === 'Низкий') {
                    $scope.click('green', mouseOverGreen, mouseOutGreen);
                } else if(risk === 'Средний') {
                    $scope.click('red',mouseOverRed, mouseOutRed);
                } else if(risk === 'Высокий') {
                    $scope.click('blue',mouseOverBlue, mouseOutBlue);
                }

                var index = typesList.indexOf(risk);
                if (index == -1) {
                    typesList.push(risk);
                } else {
                    typesList.splice(index, 1);
                }
                if(typeof event !== 'undefined') {
                    var target = event.currentTarget;
                    if (index == -1) {
                        target.style.boxShadow = "0px 0px 5px 3px #aead95";
                    } else {
                        target.style.boxShadow = "none";
                    }
                }
                restService.getProductsByType(typesList, function(response) {
                        $log.info("Get products by risk success.");
                        $scope.products = response;
                    },
                    function() {
                        $log.error("Get products by risk failure.");
                    })
            };
            var greenLine = $("#green-line");
            var greenArc = $("#green-arc");
            greenEllipse.on("mouseover", function () {
                mouseOverGreen();
            });
            greenEllipse.on("click", function () {
                greenClick()
            });
            greenEllipse.on("mouseout",function() {
                mouseOutGreen();
            });
            greenArc.on("mouseover", function () {
                mouseOverGreen();
            });
            greenArc.on("click", function () {
                greenClick()
            });
            greenArc.on("mouseout",function() {
                mouseOutGreen();
            });
            greenText.on("mouseover", function(){
                mouseOverGreen();
            });
            greenText.on("click", function(){
                greenClick()
            });
            greenText.on("mouseout", function() {
                mouseOutGreen();
            });
            greenButton.on("mouseover", function(){
                mouseOverGreen();
            });
            greenButton.on("mouseout", function() {
                mouseOutGreen();
            });

            var redLine = $("#red-line");
            var redArc = $("#red-arc");
            redEllipse.on("mouseover", function(){
                mouseOverRed();
            });
            redEllipse.on("click", function(){
                redClick();
            });
            redEllipse.on("mouseout", function() {
                mouseOutRed();
            });
            redText.on("mouseover", function(){
                mouseOverRed();
            });
            redText.on("click", function(){
                redClick();
            });
            redText.on("mouseout", function() {
                mouseOutRed();
            });
            redArc.on("mouseover", function(){
                mouseOverRed();
            });
            redArc.on("click", function(){
                redClick();
            });
            redArc.on("mouseout", function() {
                mouseOutRed();
            });
            redButton.on("mouseover", function(){
                mouseOverRed();
            });
            redButton.on("mouseout", function() {
                mouseOutRed();
            });

            var blueLine = $("#blue-line");
            var blueArc = $("#blue-arc");
            blueEllipse.on("mouseover", function(){
                mouseOverBlue()
            });
            blueEllipse.on("click", function(){
                blueClick();
            });
            blueEllipse.on("mouseout", function(){
                mouseOutBlue();
            });
            blueText.on("mouseover", function(){
                mouseOverBlue();
            });
            blueText.on("click", function(){
                blueClick();
            });
            blueText.on("mouseout", function(){
                mouseOutBlue();
            });
            blueArc.on("mouseover", function(){
                mouseOverBlue();
            });
            blueArc.on("click", function(){
                blueClick();
            });
            blueArc.on("mouseout", function(){
                mouseOutBlue();
            });
            blueButton.on("mouseover", function(){
                mouseOverBlue();
            });
            blueButton.on("mouseout", function(){
                mouseOutBlue();
            });

        });

        $scope.showArea = function(event){
            var width = parseFloat(event.target.getAttributeNS(null,"width"));
            var height = parseFloat(event.target.getAttributeNS(null,"height"));
            alert("Area of the rectangle is: " +width +"x"+ height);
        }

        $scope.predicate = 'name';
        $scope.reverse = true;
        $scope.order = function(predicate) {
            $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
            $scope.predicate = predicate;
        };
}]);

app.controller('admin', [ '$scope', '$log', 'restService', '$rootScope', '$location',
    function($scope, $log, restService, $rootScope, $location) {

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
            risks: [
                { field: 'name', displayName: 'Риски', width: "94%" },
            ],
            currency: [
                { field: 'name', displayName: 'Валюта', width: "94%" },
            ],
            paymentPeriodicity: [
                { field: 'name', displayName: 'Периодичность выплаты дохода', width: "94%" },
            ],
            underlayingType: [
                { field: 'name', displayName: 'Тип базового актива', width: "94%" },
            ],
            underlaying: [
                { field: 'name', displayName: 'Базовый актив', width: "47%" },
                { field: 'type', displayName: 'Тип базового актива', width: "47%",
                    cellFilter: "griddropdown:this",
                    editableEntity: 'underlayingType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
            ],
            product: [
                { field: 'name', displayName: 'Название', width: 200 },
                { field: 'productType', displayName: 'Тип структурного продукта', width: 250,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'productType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'term', displayName: 'Срок', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'term', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'underlaying', displayName: 'Базовый актив', width: 100,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'underlaying', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'investment', displayName: 'Минимальная сумма инвестиций', width: 250,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'investment', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'issuer', displayName: 'Провайдер продукта', width: 200,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'issuer', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'returnValue', displayName: 'Доходность', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'return', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'count', editDropdownOptionsArray: []},
                { field: 'strategy', displayName: 'Стратегия', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'strategy', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'legalType', displayName: 'Юридическая форма', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'legalType', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'payoff', displayName: 'Размер выплат', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'payoff', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'risks', displayName: 'Риски', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'risks', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'currency', displayName: 'Валюта', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'currency', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'paymentPeriodicity', displayName: 'Периодичность выплаты дохода', width: 150,
                    cellFilter: "griddropdown:this",
                    editableEntity: 'paymentPeriodicity', editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'name', editDropdownOptionsArray: []},
                { field: 'description', displayName: 'Описание', width: 400 }
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
            enableVerticalScrollbar : 2,
            enableHorizontalScrollbar : 2,
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

        $scope.uploadCsv = function(file) {
            restService.uploadProductsCsv(file,
                function(){
                    console.log('Successfully load csv');
                    getValues("product");
                    $scope.selectTable("product");
                },
                function(){console.log('Error occurs during load csv')}
            );
        };

        $scope.clickUploadButton = function() {
            $('#uploadFile').trigger('click');
        };
        $scope.selectTable = function(id) {
            $scope.selected = id;
            $scope.selection = [];
            $scope.saveButtonsDisabled = true;
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

        $scope.showFailAlert = function(msg) {
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
                    $scope.showFailAlert(response);
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
                    $scope.showFailAlert(response);
                    $log.error("Delete " + $scope.selected + " failed.");
                    $scope.selection = [];
                    getValues($scope.selected);
                }
            );
        };

        (function() {
            if(typeof $rootScope.user === 'undefined') {
                $location.path("/login");
            } else {
                $scope.selectTable('product');
            }
        }());


}])
    .filter('griddropdown', function() {
        return function (input, context) {
            if(typeof input === 'undefined') {
                return '';
            }
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

    var handler = MouseWheelHandler();

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
            document.addEventListener("mousewheel", handler, false);
            document.addEventListener("DOMMouseScroll", handler, false);
        } else {
            document.attachEvent("onmousewheel", MouseWheelHandler());
        }
    });

    $scope.$on("$destroy", function() {
        document.removeEventListener("DOMMouseScroll", handler, false);
        document.removeEventListener("mousewheel", handler, false);
    });

/*        $scope.$on('mousewheel', MouseWheelHandler());
        $scope.$on('DOMMouseScroll', MouseWheelHandler());*/

}]).value('duScrollOffset', 0).value("duScrollDuration",100);