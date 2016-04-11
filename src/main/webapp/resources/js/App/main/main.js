angular.module('App.main')

.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/', {
            templateUrl: 'resources/js/App/main/main.html',
            controller: 'mainCtrl'
        })
    }])
.controller('mainCtrl', [ '$scope', '$log', 'restService', '$anchorScroll', '$document', '$location',
    function($scope, $log, restService, $anchorScroll, $document, $location) {

        var contactShow = false;

        $scope.isContactFormShow = function() {
            return contactShow;
        };

        $scope.showContactForm = function() {
            contactShow = true;
            $scope.gotoAnchorAnimatedWithPageAndOffset('page3', 2, 30)
        };

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
            $scope.emailAlert.type = 'danger';
            $scope.emailAlert.msg = msg;
            $scope.emailAlert.visible = true;
        };

        $scope.showSuccessAlert = function(msg) {
            $scope.emailAlert.type = 'success';
            $scope.emailAlert.msg = msg;
            $scope.emailAlert.visible = true;
        };

        $scope.validateForm = function(form) {
            return form.$dirty
                && form.$valid;
        };

        var checkPhone = function () {
            var phoneEl = $('#phone');
            var val = phoneEl.val();
            if (val && val !== '') {
                return true;
            }
            phoneEl.addClass('invalid-control');
            return false;
        };

        $scope.sendEmail = function (form) {
            var phoneCheck = checkPhone();
            if(!($scope.validateForm(form) && phoneCheck)) {
                $scope.showFailAlert("Пожалуйста, заполните все необходимые поля");
                return;
            } else {
                $scope.email.phone = $('#phone').val();
            }
            restService.sendEmail(
                $scope.email,
                function() {
                    $scope.showSuccessAlert("Message sent successfully.");
                    $scope.email = '';
                    contactShow = false;
                    $scope.gotoAnchorAnimatedWithOffset('page1', 55);
                },
                function(response) {
                    $log.error("Email sending failed.");
                    $scope.showFailAlert("Не возможно отправить сообщение.");
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
        };

        $scope.accordion.isSelected = function (id, tab) {
            if (selected[id] === undefined) {
                return false;
            } else {
                if( selected[id] === tab ) {
                    return true;
                }
            }
            return false;
        };

        $scope.topProductsChange = function () {
            restService.getTopProductsByTimeAndType(
                $scope.data.timeType.value,
                $scope.data.productType,
                function(response) {
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
                    $scope.data.timeTypes = timeTypes;
                    $scope.data.timeType = timeTypes[0];

                    //load data to table
                    (function() {
                        restService.getProductTypes(
                            function(productTypes) {
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
                },
                function(response) {
                    $log.error("Get time types failed.");
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
                            $scope.gotoAnchorAnimatedWithOffset(pages[currentPage], 110);
                        } else if(currentPage == 2){
                            $scope.gotoAnchorAnimatedWithOffset(pages[currentPage], 30);
                        } else {
                            $scope.gotoAnchorAnimated(pages[currentPage]);
                        }
                    }
                } else {
                    if(currentPage > 0) {
                        currentPage--;
                        if(currentPage == 1) {
                            $scope.gotoAnchorAnimatedWithOffset(pages[currentPage], 55);
                        } else {
                            $scope.gotoAnchorAnimated(pages[currentPage]);
                        }
                    }
                }

            }
        }

        $scope.goToProductPage = function(id) {
            $location.path("/product").search("id",id);
        };

        var handler = MouseWheelHandler();

        //init app function
        angular.element(document).ready(function () {
            var sliderHeight = $("#page1").height() - $("#main-buttons").height();
            App.init();
            jQuery("#layerslider").layerSlider({
                navStartStop: false,
                skin: 'fullwidth',
                responsive : true,
                responsiveUnder : 960,
                layersContainer : 960,
                height: sliderHeight,
                autoPlayVideos: true,
                skinsPath: 'resources/assets/plugins/layer-slider/layerslider/skins/'
            });
            //jQuery("#layerslider").height(570);
            $("#phone").mask("+7 (999) 999-9999");
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
    }]).value('duScrollOffset', 0).value("duScrollDuration",100);
