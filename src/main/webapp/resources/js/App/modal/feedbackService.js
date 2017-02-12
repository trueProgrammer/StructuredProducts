angular.module('App.services').service('feedbackService', ['$uibModal',
    function ($uibModal) {

        var modalDefaults = {
            templateUrl: 'resources/js/App/modal/feedbackModal.html',
            // controller: 'sendRequestCtrl',
        };

        this.show = function (customModalDefaults, productId) {
            //Create temp objects to work with since we're in a singleton service
        	var tempModalDefaults = {};

            //Map angular-ui modal custom defaults to modal defaults defined in service
            angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

            if (!tempModalDefaults.controller) {
            	tempModalDefaults.controller = function ($scope, $modalInstance, restService) {

            		$scope.agree = false;

            		$scope.successState = false;

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
                        $scope.msg = "Ваша запрос успешно отправлен.";

                        $scope.successState = true;
                    };
                    var invalidFunc = function() {
                        $scope.invalidFormMessage = "";
                        $('input').removeClass('invalid-control');
                        $scope.msg = "Проблема при отправке запроса."
                    };


                    $scope.sendEmail = function (form) {

                    	var phoneCheck = checkPhone();

                    	if ($scope.emailForm.$valid && phoneCheck)
                    	{
                                $scope.email.phone = $('#phone').val();
                    		restService.sendEmail($scope.email, successFunc, invalidFunc);
                    	}
                    	else
                    	{
                    		$scope.invalidFormMessage = "Пожалуйста, заполните все необходимые поля корректно";

                    		if ($scope.emailForm.$error && $scope.emailForm.$error.required) {
                    			$scope.emailForm.$error.required.forEach(function (req) {
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