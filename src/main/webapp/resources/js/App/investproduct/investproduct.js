angular.module('App.investproduct')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/investproduct', {
                templateUrl: 'resources/js/App/investproduct/investproduct.html',
                controller: 'investproduct'
            })
        }])
.controller('investproduct', ['$scope', '$log', 'restService', '$document', '$location', '$cookieStore',
    function($scope, $log, restService, $document, $location, $cookieStore) {

        $scope.viewby = 5;
        $scope.totalItems = 0;
        $scope.currentPage = 1;
        $scope.itemsPerPage = $scope.viewby;
        $scope.maxSize = 5;

        $scope.itemsPerPageOptions = ['5', '10', '20', '40'];

        $scope.filter = {};

        $scope.brokers = [];
        $scope.selectedBroker = [];

        $scope.unders = [];
        $scope.selectedUnder = [];

        $scope.isCheckedForSpan = function(val, type) {
            if ($scope[type].indexOf(val) !== -1) {
                return 'float-right fa fa-check fa-2';
            } else {
                return false;
            }
        }

        $scope.isCheckedForHref = function(val, type) {
            if ($scope[type].indexOf(val) !== -1) {
                return 'font-bold';
            } else {
                return 'font-initial';
            }
        }

        $scope.setSelectedBroker = function(broker){
            var index = $scope.selectedBroker.indexOf(this.broker);
            if (index === -1) {
                $scope.selectedBroker.push(this.broker);
            } else {
                $scope.selectedBroker.splice(index, 1);
            }
            $scope.filterProducts();
        }

        $scope.setSelectedUnder = function(under){
            var index = $scope.selectedUnder.indexOf(this.under);
            if (index === -1) {
                $scope.selectedUnder.push(this.under);
            } else {
                $scope.selectedUnder.splice(index, 1);
            }
            $scope.filterProducts();
        }

        $scope.setItemsPerPage = function(num) {
            $scope.itemsPerPage = num;
            $scope.currentPage = 1; //reset to first page
            $cookieStore.put('itemsPerPage', $scope.itemsPerPage);
            $cookieStore.put('currentPage', $scope.currentPage);
        };
        $scope.$on('$locationChangeStart', function( event ) {
            $cookieStore.put('currentPage', $scope.currentPage);
        });
        function saveFilters() {
            $cookieStore.put('nameFilter', $scope.nameFilter);
            $cookieStore.put('profitFilter', $scope.profitFilter);
            $cookieStore.put('sumFilter', $scope.sumFilter);
            $cookieStore.put('termsFilter', $scope.termsFilter);
            $cookieStore.put('selectedBroker', $scope.selectedBroker);
            $cookieStore.put('selectedUnder', $scope.selectedUnder);
            $cookieStore.put('filter', $scope.filter);
        }
        function cookiesDataLoad() {
            var itemsPerPage = $cookieStore.get('itemsPerPage');
            var currentPage = $cookieStore.get('currentPage');
            if (itemsPerPage != null) {
                $scope.itemsPerPage = itemsPerPage;
            }
            if (currentPage != null) {
                $scope.currentPage = currentPage;
            }
            $scope.nameFilter = $cookieStore.get('nameFilter');
            $scope.profitFilter = $cookieStore.get('profitFilter');
            $scope.sumFilter = $cookieStore.get('sumFilter');
            $scope.termsFilter = $cookieStore.get('termsFilter');
            var selectedBroker = $cookieStore.get('selectedBroker');
            if (selectedBroker != null) {
                $scope.selectedBroker = selectedBroker;
            }
            var selectedUnder = $cookieStore.get('selectedUnder');
            if (selectedUnder != null) {
                $scope.selectedUnder = selectedUnder;
            }
            var filter = $cookieStore.get('filter');
            if (filter != null) {
                $scope.filter = filter;
            }
        }

        function setProducts(products) {
            $scope.allProducts = products;
            setTableProducts(products);
        }

        function setTableProducts(products) {
            for (var i = 0; i < products.length; i++) {
                products[i].ser = i + 1;
            }
            $scope.products = products;
            $scope.totalItems = products.length;
        }

        restService.getAllProducts(
            function (response) {

            	setProducts(response);
                $scope.highRiskProducts = 0;
                $scope.mediumRiskProducts = 0;
                $scope.lowRiskProducts = 0;

                cookiesDataLoad();
                $scope.filterProducts();

                response.forEach(function(item) {
                    if ($scope.brokers.indexOf(item.broker.name) === -1) {
                        $scope.brokers.push(item.broker.name);
                    }
                    for (index = 0; index < item.underlayingList.length; ++index) {
                        if (item.underlayingList[index].type) {
                            var name = item.underlayingList[index].type.name;
                            if ($scope.unders.indexOf(name) === -1) {
                                $scope.unders.push(name);
                            }
                        }
                    }
                    if (item.productType.riskType === 'High'){
                        $scope.highRiskProducts++;
                    }
                    else if(item.productType.riskType === 'Medium') {
                        $scope.mediumRiskProducts++;
                    } else {
                        $scope.lowRiskProducts++;
                    }
                });

                $("#red-value").text($scope.highRiskProducts);
                $("#blue-value").text($scope.mediumRiskProducts);
                $("#green-value").text($scope.lowRiskProducts);
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

            var greenButton = angular.element(document.getElementById("green-button"));

            var blueButton = angular.element(document.getElementById("blue-button"));
            var redButton = angular.element(document.getElementById("red-button"));

            var greenButtonCircle = angular.element(document.getElementById("green-button-circle"));
            var blueButtonCircle = angular.element(document.getElementById("blue-button-circle"));
            var redButtonCircle = angular.element(document.getElementById("red-button-circle"));

            var greenButtonText = angular.element(document.getElementById("green-button-text"));
            var blueButtonText = angular.element(document.getElementById("blue-button-text"));
            var redButtonText = angular.element(document.getElementById("red-button-text"));

            var green = $("#green-ellipse");
            var red = $("#red-ellipse");
            var blue = $("#blue-ellipse");
            var main = $("#main-ellipse");

            var clicked = {};


            function mouseOverLine(type, line, arc, button) {
                line.css("visibility", "visible");
                arc.attr("stroke-width", "6");

                //button.css("box-shadow", "0px 0px 5px 3px #aead95");
                //button.attr("stroke-width", "9");

                if(type === 'red') {
                	button.addClass('red-button-selected');
                } else if (type === 'blue') {
                	button.addClass('blue-button-selected');
                } else {
                	button.addClass('green-button-selected');
                }
            }


            function mouseOutLine(type, line, arc, button) {
                if(clicked[type]) {
                    return
                }
                line.css("visibility", "hidden");
                arc.attr("stroke-width", "2");

				
                if (type === 'red') {
                	button.removeClass('red-button-selected');
                } else if (type === 'blue') {
                	button.removeClass('blue-button-selected');
                } else {
                	button.removeClass('green-button-selected');
                }
            }

            function mouseOverGreen() {
                mouseOverLine('green',greenLine, greenArc, greenButton);
            }
            function mouseOutGreen() {
                mouseOutLine('green',greenLine, greenArc, greenButton);
            }
            function mouseOverRed() {
                mouseOverLine('red',redLine, redArc, redButton);
            }
            function mouseOutRed() {
                mouseOutLine('red',redLine, redArc, redButton);
            }
            function mouseOverBlue() {
                mouseOverLine('blue',blueLine, blueArc, blueButton);
            }
            function mouseOutBlue() {
                mouseOutLine('blue',blueLine, blueArc, blueButton);
            }

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
                $scope.filterByType('Low');
            }
            function redClick() {
                $scope.filterByType('High');
            }
            function blueClick() {
                $scope.filterByType('Medium');
            }
            $scope.filterByMobileType = function(risk, event){
                var section = angular.element(document.getElementById('table'));
                $document.scrollToElementAnimated(section);
                $scope.filterByType(risk, event);
            };
            $scope.filterByType = function (risk, event) {

                if(risk === 'Low') {
                    $scope.click('green', mouseOverGreen, mouseOutGreen);
                } else if(risk === 'Medium') {
                    $scope.click('blue',mouseOverBlue, mouseOutBlue);
                } else if(risk === 'High') {
                    $scope.click('red',mouseOverRed, mouseOutRed);
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

                if(typesList.length === 0) {
                    restService.getAllProducts(
                        function (response) {
                            setProducts(response);
                        },
                        function () {
                            $log.error("Get all products failure.");
                        }
                    );
                } else {
                    restService.getProductsByType(typesList, function (response) {
                            setProducts(response);
                        },
                        function () {
                            $log.error("Get products by risk failure.");
                        });
                }
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
            greenButtonCircle.on("mouseover", function(){
                mouseOverGreen();
            });
            greenButtonCircle.on("mouseout", function() {
                mouseOutGreen();
            });
            greenButtonText.on("mouseover", function(){
                mouseOverGreen();
            });
            greenButtonText.on("mouseout", function() {
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
            redButtonCircle.on("mouseover", function(){
                mouseOverRed();
            });
            redButtonCircle.on("mouseout", function() {
                mouseOutRed();
            });
            redButtonText.on("mouseover", function(){
                mouseOverRed();
            });
            redButtonText.on("mouseout", function() {
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
            blueButtonCircle.on("mouseover", function(){
                mouseOverBlue();
            });
            blueButtonCircle.on("mouseout", function(){
                mouseOutBlue();
            });
            blueButtonText.on("mouseover", function(){
                mouseOverBlue();
            });
            blueButtonText.on("mouseout", function(){
                mouseOutBlue();
            });

        });

        $scope.showArea = function(event){
            var width = parseFloat(event.target.getAttributeNS(null,"width"));
            var height = parseFloat(event.target.getAttributeNS(null,"height"));
            alert("Area of the rectangle is: " +width +"x"+ height);
        };

        $scope.predicate = 'name';
        $scope.reverse = true;
        $scope.order = function(predicate) {
            $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
            $scope.predicate = predicate;
            if ($scope.reverse) {
                //desc
                setTableProducts($scope.allProducts.sort(
                    function (a, b) {
                        if (a[predicate] < b[predicate]) {
                            return 1;
                        }
                        if (a[predicate] > b[predicate]) {
                            return -1;
                        }
                        return 0;
                    }
                ));
            } else {
                //asc
                setTableProducts($scope.allProducts.sort(
                    function (a, b) {
                        if (a[predicate] < b[predicate]) {
                            return -1;
                        }
                        if (a[predicate] > b[predicate]) {
                            return 1;
                        }
                        return 0;
                    }
                ));
            }
        };


        $scope.filterName = function(products) {
            if ($scope.nameFilter) {
                return products.filter(function(p) {return p.name.toLowerCase().indexOf($scope.nameFilter.toLowerCase()) !== -1;})
            }
            return products;
        };
        function resetNameFilter() {
            $scope.nameFilter = null;
        }
        $scope.filterProfit = function(products) {
            if ($scope.profitFilter) {
                return products.filter(function(p) {
                    if ($scope.profitFilter.from && $scope.profitFilter.to) {
                        return p.returnValue >= $scope.profitFilter.from && p.returnValue <= $scope.profitFilter.to;
                    }
                    if ($scope.profitFilter.from) {
                        return p.returnValue >= $scope.profitFilter.from ;
                    }
                    if ($scope.profitFilter.to) {
                        return p.returnValue <= $scope.profitFilter.to ;
                    }
                    return true;
                });
            }
            return products;
        };
        function resetProfitFilter() {
            $scope.profitFilter = null;
        }

        $scope.filterBrokers = function(products) {
            if ($scope.selectedBroker.length > 0) {
                return products.filter(function(e) {
                    if ($scope.selectedBroker.indexOf(e.broker.name) === -1) {
                        return false;
                    } else {
                        return true;
                    }
                });
            }
            return products;
        }

        $scope.filterUnder = function(products) {
            if ($scope.selectedUnder.length > 0) {
                    return products.filter(function(e) {
                        var i;
                        for (i = 0; i < e.underlayingList.length; ++i) {
                            if (e.underlayingList[i].type) {
                                if ($scope.selectedUnder.indexOf(e.underlayingList[i].type.name) !== -1) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    });
            }
            return products;
        };
        function periodFilter(val,filter) {
            if (filter.from && filter.to) {
                if(val.min == 0) {
                    return val.max >= filter.from && val.max <= filter.to
                } else if(val.max == 0) {
                    return val.min >= filter.from && val.min <= filter.to;
                } else {
                    return val.min >= filter.from && val.max <= filter.to;
                }
            }
            if (filter.from) {
                if(val.min == 0) {
                    return val.max >= filter.from;
                } else {
                    return val.min >= filter.from;
                }
            }
            if (filter.to) {
                if(val.max == 0) {
                    return val.min <= filter.to;
                } else {
                    return val.max <= filter.to;
                }
            }
            return true;
        };
        $scope.filterSum = function(products) {
            if ($scope.sumFilter) {
                return products.filter(function(p) {
                    return periodFilter({min:p.minInvest ,max: p.maxInvest}, $scope.sumFilter);
                });
            }
            return products;
        };
        function resetSumFilter() {
            $scope.sumFilter = null;
        }
        $scope.filterTerms = function(products) {
            if ($scope.termsFilter) {
                return products.filter(function(p) {
                    return periodFilter({min:p.minTerm, max:p.maxTerm}, $scope.termsFilter);
                });
            }
            return products;
        };
        function resetTermsFilter() {
            $scope.termsFilter = null;
        }
        $scope.filterProducts = function() {
            setTableProducts($scope.filterSum($scope.filterTerms($scope.filterProfit($scope.filterName($scope.filterUnder($scope.filterBrokers($scope.allProducts)))))));
            saveFilters();
        };
        $scope.setUnderFilter = function() {
            $scope.filterProducts();
        };
        $scope.clickFilter = function(filter) {
            if($scope.filter[filter]) {
                eval('reset'+filter+'Filter')();
                $scope.filterProducts();
            }
            $scope.filter[filter] = !$scope.filter[filter];
        };
        $scope.getPeriodValue = function(min, max) {
            var minStr = min.toLocaleString();
            var maxStr = max.toLocaleString();

            if(min != 0 && max != 0) {
                return "От " + minStr + " до " + maxStr;
            } else if(min == 0 && max != 0) {
                return "До " +  maxStr;
            } else if (min != 0 && max == 0) {
                return "От " + minStr;
            }
        };
        $scope.goToProductPage = function(id) {
            $location.path("/product").search("id",id);
        };

        $scope.getClassByRiskType = function(risk) {
            if(risk == 'Medium') {
                return 'blue-style';
            } else if(risk == 'Low') {
                return 'green-style';
            } else {
                return 'red-style'
            }
        }
    }]).value("duScrollDuration",100);