angular.module('App.services').service('modalService', ['$uibModal',
    function ($uibModal) {
        var modalDefaults = {
            templateUrl: 'resources/js/App/modal/sendingRequestModal.html',
            // controller: 'sendRequestCtrl',
        };
        this.show = function (customModalDefaults, productId) {
            //Create temp objects to work with since we're in a singleton service
            var tempModalDefaults = {};

            //Map angular-ui modal custom defaults to modal defaults defined in service
            angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);


            if (!tempModalDefaults.controller) {
                tempModalDefaults.controller = function ($scope, $modalInstance, restService) {
                    var checkPhone = function () {
                        var phoneEl = $('#phone');
                        var val = phoneEl.val();
                        if (val && val !== '') {
                            phoneEl.removeClass('invalid-control');
                            return true;
                        } else {
                            phoneEl.addClass('invalid-control');
                            return false;
                        }
                    };
                    var successFunc = function() {
                        $scope.invalidFormMessage = "";
                        $('input').removeClass('invalid-control');
                        $scope.msg = "Ваша заявка успешно отправлена. Ожидайте звонка от брокера.";
                    };
                    var invalidFunc = function() {
                        $scope.invalidFormMessage = "";
                        $('input').removeClass('invalid-control');
                        $scope.msg = "Проблема при обработке заявки."
                    };
                    $scope.sendParams = function () {
                        var phoneCheck = checkPhone();
                        if ($scope.sendform.$valid && phoneCheck) {
                            $scope.user.phone = $('#phone').val();
                            if (productId == null) {
                                $scope.user.controls = savedControls;
                                restService.createProductRequest($scope.user, successFunc, invalidFunc);
                            } else {
                                $scope.user.productId = productId;
                                restService.productRequest($scope.user, successFunc, invalidFunc);
                            }
                        } else {
                            $scope.invalidFormMessage = "Пожалуйста, заполните все необходимые поля корректно";
                            if ($scope.sendform.$error && $scope.sendform.$error.required) {
                                $scope.sendform.$error.required.forEach(function (req) {
                                    $("input[name='" + req.$name + "']").addClass('invalid-control')
                                });
                            }
                        }
                    };
                    $scope.closeModal = function () {
                        $modalInstance.close();
                    }
                }
            }
            var modalInstance = $uibModal.open(tempModalDefaults);
            modalInstance.rendered.then(function () {
                $("#phone").mask("+7 (999) 999-9999");
            }, function () {
            });
            return modalInstance.result;
        };

    }]);