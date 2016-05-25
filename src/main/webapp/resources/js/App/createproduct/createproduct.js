var savedControls = {};
angular.module('App.createproduct')
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/createproduct', {
                templateUrl: 'resources/js/App/createproduct/createproduct.html',
                controller: 'createproduct'
            })
        }])
    .controller('createproduct', ['$scope', 'modalService', 'restService',
        function ($scope, modalService, restService) {
            $scope.optParamsControl = {
                isDisabled: true
            };

            $scope.sendRequestDisabled = true;

            var defaultParams = [{
                text: 'Доходность',
                stroke: '#91CF50',
                id: 'profit'
            }, {
                text: 'Срок',
                stroke: '#4774AA',
                id: 'time'
            }, {
                text: 'Сумма',
                stroke: '#FD0001',
                id: 'sum'
            }, {
                text: 'Защита',
                stroke: '#FDBF01',
                id: 'risk'
            }];

            $scope.optParams = [{
                text: 'Актив',
                stroke: '#FDBF01',
                id: 'baseActiveType'
            }, {
                text: 'Брокер',
                stroke: '#91CF50',
                id: 'broker'
            }, {
                text: 'Выплаты',
                stroke: '#FDBF01',
                id: 'paymentsPeriod'
            }, {
                text: 'Стратегия',
                stroke: '#4774AA',
                id: 'strategy'
            }];

            $scope.defaultControls = [{
                id: 'profitBlock',
                header: 'Доходность (% годовых)',
                buttonText: 'Применить доходность',
                fromValues: ['10%', '20%', '30%', '40%'],
                toValues: ['10%', '20%', '30%', '40%'],
                fromValue: '10%',
                toValue: '20%',
                lineFormat: 'От {0} до {1}',
                type: 'diapason'
            }, {
                id: 'timeBlock',
                header: 'Срок инвестирования',
                buttonText: 'Применить срок инвестирования',
                fromValues: ['2 мес', '3 мес', '4 мес', '5 мес', '6 мес', '8 мес',
                    '10 мес', '12 мес', '15 мес', '18 мес'],
                toValues: ['2 мес', '3 мес', '4 мес', '5 мес', '6 мес', '8 мес',
                    '10 мес', '12 мес', '15 мес', '18 мес'],
                fromValue: '2 мес',
                toValue: '3 мес',
                lineFormat: 'От {0} до {1}',
                type: 'diapason'
            }, {
                id: 'sumBlock',
                header: 'Сумма инвестиций',
                buttonText: 'Применить сумму инвестиций',
                fromValues: ['200 тыс', '300 тыс', '500 тыс', '1 млн', 'и больше'],
                toValues: ['200 тыс', '300 тыс', '500 тыс', '1 млн', 'и больше'],
                fromValue: '200 тыс',
                toValue: '300 тыс',
                lineFormat: 'От {0} до {1} {2}',
                dropdown: {
                    header: 'Валюта',
                    values: [{dropdownName: 'Рубль', lineName: 'рублей'}, {dropdownName: 'Доллар', lineName: 'долларов'},{dropdownName: 'Евро', lineName: 'евро'}],
                    value: {dropdownName: 'Рубль', lineName: 'рублей'}
                },
                type: 'dropdownAndDiapason'
            }, {
                id: 'riskBlock',
                header: 'Уровень защиты инвестиций',
                buttonText: 'Применить уровень риска',
                fromValues: ['10%', '20%', '30%', '40%'],
                toValues: ['10%', '20%', '30%', '40%'],
                fromValue: '10%',
                toValue: '20%',
                lineFormat: 'От {0} до {1}',
                activateDiapasonValue : 'Задать уровень защиты самостоятельно',
                dropdown: {
                    header: '',
                    values: [{value: '100% гарантия защиты инвестиций', shortValue: '100% гарантия '}, {value: 'Без гарантий защиты инвестиций', shortValue: 'Без гарантий'}, {value: 'Задать уровень защиты самостоятельно'}],
                    value: {value: '100% гарантия защиты инвестиций', shortValue: '100% гарантия '}
                },
                type: 'optDiapason'
            }];

            $scope.controls = [{
                id: 'brokerBlock',
                header: 'Провайдер продукта (брокер)',
                buttonText: 'Применить брокера',
                values: [],
                value: undefined,
                lineFormat: '{0}'
            }, {
                id: 'baseActiveTypeBlock',
                header: 'Базовый актив',
                buttonText: 'Применить тип базового актива',
                values: [
                    {value: 'Фондовые индексы', shortValue: 'Индексы'},
                    {value: 'Акции', shortValue: 'Акции'},
                    {value: 'Драгоценные металлы', shortValue: 'Драгоценные металлы'},
                    {value: 'Товары', shortValue: 'Товары'},
                    {value: 'Валюта', shortValue: 'Валюта'},
                    {value: 'Другое', shortValue: 'Другое'}
            ],
                value: {value:'Фондовые индексы', shortValue: 'Индексы'},
                lineFormat: '{0}'
            }, {
                id: 'paymentsPeriodBlock',
                header: 'Периодичность выплат',
                buttonText: 'Применить периодичность выплат',
                values: [
                    {value: 'В конце срока', shortValue: 'В конце срока'},
                    {value: 'Ежегодно', shortValue: 'Ежегодно'},
                    {value: '2 раза в год', shortValue: '2 раза в год'},
                    {value: 'Ежеквартально', shortValue: 'Ежеквартально'},
                    {value: 'Любая', shortValue: 'Любая'}
                ],
                value: {value: 'В конце срока', shortValue: 'В конце срока'},
                lineFormat: '{0}'
            }, {
                id: 'strategyBlock',
                header: 'Стратегия',
                buttonText: 'Применить применить стратегию',
                values: [
                    {value: 'Рост цены базового актива', shortValue: 'Рост цены'},
                    {value:'Падение цены базового актива', shortValue: 'Падение цены'},
                    {value: 'Барьерная стратегия', shortValue: 'Барьерная'},
                    {value: 'Диапазонная стратегия', shortValue: 'Диапазонная'}],
                value: {value: 'Рост цены базового актива', shortValue: 'Рост цены'},
                lineFormat: '{0}'
            }];

            restService.getAllBrokers(function(response) {
                var brokers = response.map(function(b) {return {value: b.name, shortValue: b.name}});
                if (brokers) {
                    $scope.controls[0].values = brokers;
                    $scope.controls[0].value = brokers[0];
                }
            }, function() {
            });

            var mapControls = function (controls, hexControls) {
                var controlsIds = controls.map(function (control) {
                    return control.id.substr(0, control.id.length - 5);
                });
                var hexControlsIds = hexControls.map(function (hexControl) {
                    return hexControl.id;
                });
                controlsIds.forEach(function (controlId, index) {
                    var hexIndex = hexControlsIds.indexOf(controlId);
                    if (hexIndex != -1) {
                        controls[index].hexControl = hexControls[hexIndex];
                        hexControls[hexIndex].boundedControl = controls[index];
                    }
                });
            };

            var extendControls = function (controls) {
                controls.forEach(function (control) {
                    control.setValue = function (value) {
                        this.value = value;
                        $('#' + this.hexControl.id + '-text').text(value.shortValue);
                    };
                    control.save = function () {
                        this.isSaved = true;
                        this.line = this.lineFormat.format(this.value.value);
                        this.hexControl.inactive();
                        savedControls[this.id] = {header: this.header, value: this.line};
                    };
                    control.edit = function () {
                        this.isSaved = false;
                        this.line = '';
                        this.hexControl.active();
                        delete savedControls[this.id];
                    };
                    control.show = function () {
                        $('#' + this.id).appendTo('#optParamsControlBlock');
                    };
                });
            };

            function validateDiapason(control) {
                if(control.type === 'diapason' || control.type === 'dropdownAndDiapason' || control.diapasonOn) {
                    if(control.toValue > control.fromValue) {
                        control.error = false;
                        return true;
                    } else {
                        control.error = true;
                        return false;
                    }
                } else {
                    control.error = false;
                    return true;
                }
            }

            var extendDefaultControls = function (controls) {
                controls.forEach(function(control, index){
                    if (control.type === 'diapason') {
                        control.value = control.lineFormat.format(control.fromValue, control.toValue);
                        control.shortValue = control.value;
                    } else if(control.type === 'dropdownAndDiapason'){
                        control.value = control.lineFormat.format(control.fromValue, control.toValue, control.dropdown.value.lineName);
                        control.shortValue = control.lineFormat.format(control.fromValue, control.toValue, '');
                    } else if (control.type === 'optDiapason') {
                        if (control.diapasonOn) {
                            control.value = control.lineFormat.format(control.fromValue, control.toValue);
                            control.shortValue = control.value;
                        } else {
                            control.value = control.dropdown.value;
                            control.shortValue = control.dropdown.value.shortValue;
                        }
                    }
                    control.setFromValue = function(fromValue) {
                        if (control.active) {
                            control.fromValue = fromValue;
                            this.value = this.lineFormat.format(this.fromValue, this.toValue, '');
                            $('#' + this.hexControl.id + '-text').text(this.value);
                        }
                    };
                    control.setToValue = function (toValue) {
                        if (control.active) {
                            control.toValue = toValue;
                            this.value = this.lineFormat.format(this.fromValue, this.toValue, '');
                            $('#' + this.hexControl.id + '-text').text(this.value);
                        }
                    };
                    control.setDropdownValue = function(dropdownValue) {
                        if (control.active) {
                            control.dropdown.value = dropdownValue;
                            control.value = dropdownValue;
                        }
                    };
                    control.setOptDiapasonDropdownValue = function(dropdownValue) {
                        if (control.active) {
                            control.dropdown.value = dropdownValue;
                            control.diapasonOn = dropdownValue.value === control.activateDiapasonValue;
                            if (control.diapasonOn) {
                                this.value = this.lineFormat.format(this.fromValue, this.toValue, '');
                                $('#' + this.hexControl.id + '-text').text(this.value);
                            } else {
                                this.value = dropdownValue;
                                $('#' + this.hexControl.id + '-text').text(this.value.shortValue);
                            }
                        }
                    };
                    control.save = function () {
                        if(!validateDiapason(control)) {
                            return;
                        }
                        if (control.active) {
                            this.isSaved = true;
                            if (control.type === 'diapason') {
                                control.line = control.lineFormat.format(control.fromValue, control.toValue);
                            } else if(control.type === 'dropdownAndDiapason'){
                                control.line = control.lineFormat.format(control.fromValue, control.toValue, control.dropdown.value.lineName);
                            } else if (control.type === 'optDiapason') {
                                if (control.diapasonOn) {
                                    control.line = control.lineFormat.format(control.fromValue, control.toValue);
                                } else {
                                    control.line = control.dropdown.value.value;
                                }
                            }
                            this.hexControl.inactive();
                            if (this.next) {
                                this.next.hexControl.turnOn();
                            } else {
                                $scope.optParamsControl.isDisabled = false;
                                $scope.sendRequestDisabled = false;
                                $('#optParamsControlBlock').css('opacity', '1');
                                $('#sendRequest').css('opacity', '1');
                                hex.switchAddParams();
                            }
                            savedControls[this.id] = {header: this.header, value: this.line};
                        }
                    };
                    control.edit = function () {
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

            $scope.controlFilter = function (item) {
                return !item.added;
            };
            $scope.addHex = function (control) {
                control.hexControl.show();
                control.show();
                $scope.controls[$scope.controls.map(function (control) {
                    return control.id;
                }).indexOf(control.id)].added = true;
            };

            mapControls($scope.defaultControls, defaultParams);
            extendDefaultControls($scope.defaultControls);
            mapControls($scope.controls, $scope.optParams);
            extendControls($scope.controls);


            var hex = new hexParams({
                $scope: $scope,
                radius: 93,
                defaultParams: defaultParams,
                optParams: $scope.optParams
            });
            defaultParams[0].active();
            //generateControls([{id: 'currencyBlock'}]);


            $scope.openModal = function () {
                if(!$scope.sendRequestDisabled) {
                    modalService.show();
                }
            };

        }]);
