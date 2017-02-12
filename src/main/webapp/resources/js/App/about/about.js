angular.module('App.about')
.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/about', {
            templateUrl: 'resources/js/App/about/about.html',
            controller: 'aboutCtrl'
        })
}])
.controller('aboutCtrl', ['$scope', '$anchorScroll', '$document', '$location',
		function ($scope, $anchorScroll, $document, $location) {

    	var selected = {
    		0: false,
    		1: false,
    		2: false,
    		3: false,
    		4: false,
    		5: false
		};

    	$scope.click = function (section) {
    		selected[section] = !selected[section];

    		//debugger;

    		//if (selected[section])
    		//{
    		//	var elemId = 'section' + section;

    		//	var divElement = angular.element(document.getElementById(elemId));

    		//	$document.scrollToElementAnimated(divElement);
    		//}
    	};

    	$scope.isSelected = function (section) {
    		return selected[section];
    	};

    	$scope.isNotSelected = function (section) {
    		return !selected[section];
    	};


    	$scope.gotoAnchorAnimated = function (id) {
    		if (id === 'kind') {
    			selected['3'] = true;
    		} else if (id === 'protection') {
    			selected['4'] = true;
    		}
    		$scope.gotoAnchorAnimatedWithOffset(id, 80);
    	};

    	$scope.gotoAnchorAnimatedWithOffset = function (id, offset) {
    		var section = angular.element(document.getElementById(id));

    		$document.scrollToElementAnimated(section, offset);
    	};

        //init app function
        angular.element(document).ready(function () {
        	App.init();

        	//jQuery('#accordion').on('shown.bs.collapse', function (e) {
        	//	var offset = $('.panel.panel-default > .panel-collapse.in').offset();
        	//	if (offset) {
        	//		$('html,body').animate({
        	//			scrollTop: $('.panel-title a').offset().top - 20
        	//		}, 500);
        	//	}
        	//});

        	jQuery('#aboutPartners').carouFredSel({
        		auto: {
        			timeoutDuration: 1
        		},
        		responsive: true,
        		width: '100%',
        		scroll: {
        			items: 1,
        			easing: 'linear',
        			fx: 'scroll',
        			duration: 5000
        		},
        		items: {
        			width: 330,
        			height: '82px',	//	optionally resize item-height
        			visible: {
        				min: 1,
        				max: 4
        			}
        		}
        	});


        });
    }
]);
