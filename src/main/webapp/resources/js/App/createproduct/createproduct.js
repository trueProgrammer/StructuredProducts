var savedControls = {

};
angular.module('App.createproduct')
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/createproduct', {
                templateUrl: 'resources/js/App/createproduct/createproduct.html',
                controller: 'createproduct'
            })
        }])
    .controller('createproduct', ['$scope', '$uibModal',
        function ($scope, $uibModal) {

            $scope.optParamsControl = {
                isDisabled : true
            };

            var defaultParams = [{
                text: 'Доходность',
                stroke: '#91CF50',
                id: 'profit'
            }, {
                text: 'Уровень риска',
                stroke: '#FDBF01',
                id: 'risk'
            }, {
                text: 'Сумма вложений',
                stroke: '#FD0001',
                id: 'sum'
            }, {
                text: 'Срок вложений',
                stroke: '#4774AA',
                id: 'time'
            }];

            $scope.optParams = [ {
                text: 'Размер выплат',
                stroke: '#4774AA',
                id: 'returnValue'
            }, {
                text: 'Тип базового актива',
                stroke: '#FDBF01',
                id: 'baseActiveType'
            },{
                text: 'Тип продукта',
                stroke: '#91CF50',
                id: 'type'
            }, {
                text: 'Периодичность выплат',
                stroke: '#FDBF01',
                id: 'paymentsPeriod'
            }, {
                text: 'Стратегия',
                stroke: '#4774AA',
                id: 'strategy'
            }, {
                text: 'Валюта',
                stroke: '#FDBF01',
                id: 'currency'
            }];

            $scope.defaultControls = [{
                id: 'profitBlock',
                header: 'Доходность',
                buttonText: 'Применить доходность',
                fromValues: ['10%', '20%', '30%', '40%'],
                toValues: ['10%', '20%', '30%', '40%'],
                fromValue: '10%',
                toValue: '20%',
                lineFormat: 'От {0} до {1}',
            },{
                id: 'riskBlock',
                header: 'Уровень риска',
                buttonText: 'Применить уровень риска',
                fromValues: ['10%', '20%', '30%', '40%'],
                toValues: ['10%', '20%', '30%', '40%'],
                fromValue: '10%',
                toValue: '20%',
                lineFormat: 'От {0} до {1}'
            },{
                id: 'sumBlock',
                header: 'Сумма вложений',
                buttonText: 'Применить сумму вложений',
                fromValues: ['200 тыс','300 тыс','500 тыс','1 млн','и больше'],
                toValues: ['200 тыс','300 тыс','500 тыс','1 млн','и больше'],
                fromValue: '200 тыс',
                toValue: '300 тыс',
                lineFormat: 'От {0} до {1}'
            },{
                id: 'timeBlock',
                header: 'Срок вложений',
                buttonText: 'Применить срок вложений',
                fromValues: ['2 мес', '3 мес', '4 мес', '5 мес', '6 мес', '8 мес',
                    '10 мес', '1 год', '1 год и 3 мес', '1 год и 6 мес'],
                toValues: ['2 мес', '3 мес', '4 мес', '5 мес', '6 мес', '8 мес',
                    '10 мес', '1 год', '1 год и 3 мес', '1 год и 6 мес'],
                fromValue: '2 мес',
                toValue: '3 мес',
                lineFormat: 'От {0} до {1}'
            }];

            $scope.controls = [{
                id: 'currencyBlock',
                header: 'Валюта',
                buttonText: 'Применить валюту',
                values: ['RUB', 'USD', 'EUR'],
                value: 'RUB',
                lineFormat: '{0}'
            },{
                id: 'returnValueBlock',
                header: 'Размер выплат',
                buttonText: 'Применить размер выплат',
                values: ['Купон', 'Без ограничения', 'С ограничением'],
                value: 'Купон',
                lineFormat: '{0}'
            },{
                id: 'typeBlock',
                header: 'Тип продукта',
                buttonText: 'Применить тип продукта',
                values: ['100% защита капитала с гарантированной доходностью', '100% защита капитала без гарантированной доходности', 'С участием', 'Рисковый'],
                value: '100% защита капитала с гарантированной доходностью',
                lineFormat: '{0}'
            },{
                id: 'baseActiveTypeBlock',
                header: 'Тип базового актива',
                buttonText: 'Применить тип базового актива',
                values: ['Акции', 'Индексы', 'Валюта'],
                value: 'Акции',
                lineFormat: '{0}'
            },{
                id: 'paymentsPeriodBlock',
                header: 'Периодичность выплат',
                buttonText: 'Применить периодичность выплат',
                values: ['1 месяц', '6 месяцев', 'год'],
                value: '6 месяцев',
                lineFormat: '{0}'
            },{
                id: 'strategyBlock',
                header: 'Стратегия',
                buttonText: 'Применить применить стратегию',
                values: ['Рост цены', 'Падение цены', 'Барьерная стратегия', 'Диапазонная стратегия'],
                value: 'Рост цены',
                lineFormat: '{0}'
            }];


            var mapControls = function(controls, hexControls) {
                var controlsIds = controls.map(function(control) {
                    return control.id.substr(0, control.id.length - 5);
                });
                var hexControlsIds = hexControls.map(function(hexControl) {return hexControl.id;});
                controlsIds.forEach(function(controlId, index) {
                    var hexIndex = hexControlsIds.indexOf(controlId);
                    if (hexIndex != -1) {
                        controls[index].hexControl = hexControls[hexIndex];
                        hexControls[hexIndex].boundedControl = controls[index];
                    }
                });
            };

            var extendControls = function (controls) {
                controls.forEach(function(control){
                    control.setValue = function(value) {
                        this.value = value;
                        $('#' + this.hexControl.id + '-text').text(value);
                    };
                    control.save = function() {
                        this.isSaved = true;
                        this.line = this.lineFormat.format(this.value);
                        this.hexControl.inactive();
                        savedControls[this.id] = {header: this.header, value: this.line};
                    };
                    control.edit = function() {
                        this.isSaved = false;
                        this.line = '';
                        this.hexControl.active();
                        delete savedControls[this.id];
                    };
                    control.show = function() {
                        $('#' + this.id).appendTo('#optParamsControlBlock');
                    };
                });
            };

            var extendDefaultControls = function (controls) {
                controls.forEach(function(control, index){
                    control.value = control.lineFormat.format(control.fromValue, control.toValue);
                    control.setFromValue = function(fromValue) {
                        if (control.active) {
                            control.fromValue = fromValue;
                            this.value = this.lineFormat.format(this.fromValue, this.toValue);
                            $('#' + this.hexControl.id + '-text').text(this.value);
                        }
                    };
                    control.setToValue = function(toValue) {
                        if (control.active) {
                            control.toValue = toValue;
                            this.value = this.lineFormat.format(this.fromValue, this.toValue);
                            $('#' + this.hexControl.id + '-text').text(this.value);
                        }
                    };
                    control.save = function() {
                        if (control.active) {
                            this.isSaved = true;
                            this.line = this.lineFormat.format(this.fromValue, this.toValue);
                            this.hexControl.inactive();
                            if(this.next) {
                                this.next.hexControl.turnOn();
                            } else {
                                $scope.optParamsControl.isDisabled = false;
                                $('#optParamsControlBlock').css('opacity', '1');
                                hex.switchAddParams();
                            }
                            savedControls[this.id] = {header: this.header, value: this.line};
                        }
                    };
                    control.edit = function() {
                        if (control.active) {
                            this.isSaved = false;
                            this.line = '';
                            this.hexControl.active();
                            delete savedControls[this.id];
                        }
                    };
                    if (index > 0) {
                        controls[index - 1].next = control;
                    }
                });
            };

            $scope.controlFilter = function(item) {
                return !item.added;
            };
            $scope.addHex = function(control) {
                control.hexControl.show();
                control.show();
                $scope.controls[$scope.controls.map(function(control) {return control.id;}).indexOf(control.id)].added = true;
            };

            mapControls($scope.defaultControls, defaultParams);
            extendDefaultControls($scope.defaultControls);
            mapControls($scope.controls, $scope.optParams);
            extendControls($scope.controls);


            var hex = new hexParams({$scope: $scope, radius: 88, defaultParams: defaultParams, optParams: $scope.optParams});
            defaultParams[0].active();
            //generateControls([{id: 'currencyBlock'}]);


            $scope.openModal = function () {

                var modalInstance = $uibModal.open({
                    templateUrl: 'sendingRequestModal.html',
                    controller: 'sendRequestCtrl',
                });

                modalInstance.rendered.then(function () {
                    $("#phone").mask("+7 (999) 999-9999");
                }, function () {
                });
            };


        }])
        .controller('sendRequestCtrl', ['$scope', 'restService',
        function ($scope, restService) {
            var checkPhone = function() {
                var phoneEl = $('#phone');
                var val = phoneEl.val();
                if (val && val !== '') {
                    return true;
                }
                phoneEl.addClass('invalid-control');
                return false;
            };
            $scope.sendParams = function() {
                var phoneCheck = checkPhone();
                if ($scope.sendform.$valid && phoneCheck) {
                    $scope.user.controls = savedControls;
                    restService.createProductRequest($scope.user,
                    function() {
                        $scope.invalidFormMessage = "";
                        $('input').removeClass('invalid-control');
                        $scope.msg = "Ваша заявка успешно отправлена. Ожидайте звонка от брокера.";
                    },
                        function() {
                            $scope.invalidFormMessage = "";
                            $('input').removeClass('invalid-control');
                            $scope.msg = "Проблема при обработке заявки."
                        })
                } else {
                    $scope.invalidFormMessage = "Пожалуйста, заполните все необходимые поля";
                    if ($scope.sendform.$error && $scope.sendform.$error.required) {
                        $scope.sendform.$error.required.forEach(function(req) {
                            $("input[name='" +req.$name +"']").addClass('invalid-control')
                        });
                    }
                }
            }

        }]);
