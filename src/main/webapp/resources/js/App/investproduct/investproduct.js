angular.module('App.investproduct')
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/investproduct', {
                templateUrl: 'resources/js/App/investproduct/investproduct.html',
                controller: 'investproduct'
            })
        }])
.controller('investproduct', ['$scope', '$log', 'restService',
    function($scope, $log, restService) {
        restService.getAllProducts(
            function (response) {
                $log.info("Get all products success.");
                $scope.products = response;

                $scope.highRiskProducts = 0;
                $scope.mediumRiskProducts = 0;
                $scope.lowRiskProducts = 0;

                response.forEach(function(item) {
                    if (item.productType.name === '100% защита капитала без гарантированной доходности'){
                        $scope.highRiskProducts++;
                    }
                    else if(item.productType.name === 'С участием (ограниченный риск)') {
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
            (function() {
                var height = 33;var addition = 15;
                var blueOffset = $("#blue-line").offset().top;
                var redOffset = $("#red-line").offset().top;
                if(redOffset - blueOffset < 10) {
                    return;
                }
                var greenOffset = $("#green-line").offset().top;
                if(redOffset - blueOffset < 70) {
                    $("#red-button").offset({top: redOffset - height});
                    $("#blue-button").offset({top: redOffset - height*2 - height - addition});
                    $("#green-button").offset({top: redOffset + height*2 - height + addition});
                } else {
                    $("#green-button").offset({top: greenOffset - height});
                    $("#blue-button").offset({top: blueOffset - height});
                    $("#red-button").offset({top: blueOffset + (greenOffset - blueOffset) / 2 - height});
                }
            }());

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

            var mainEllipse = angular.element(document.getElementById("main-ellipse"));

            var clicked = {};

            function mouseOverLine(type, line, arc, button) {
                line.css("visibility", "visible");
                arc.attr("stroke-width", "9");
                //button.css("box-shadow", "0px 0px 5px 3px #aead95");
                button.attr("stroke-width", "9");
            };
            function mouseOutLine(type, line, arc, button) {
                if(clicked[type]) {
                    return
                }
                line.css("visibility", "hidden");
                arc.attr("stroke-width", "5");
                //button.css("box-shadow", "none");
                button.attr("stroke-width", "3");
            };

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
                $scope.filterByType('Low');
            };
            function redClick() {
                $scope.filterByType('High');
            };
            function blueClick() {
                $scope.filterByType('Medium');
            };
            $scope.filterByType = function(risk, event) {
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
                            $log.info("Get all products success.");
                            $scope.products = response;
                        },
                        function () {
                            $log.error("Get all products failure.");
                        }
                    );
                } else {
                    restService.getProductsByType(typesList, function (response) {
                            $log.info("Get products by risk " + typesList + " success.");
                            $scope.products = response;
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
        }

        $scope.predicate = 'name';
        $scope.reverse = true;
        $scope.order = function(predicate) {
            $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
            $scope.predicate = predicate;
        };
    }]);
