angular.module('App',
    ['ngRoute', 'ngAnimate', 'ngTouch', 'ui.bootstrap', 'duScroll', 'ngCookies', 'App.services',
        'ui.grid', 'ui.grid.edit', 'ui.grid.selection', 'oc.lazyLoad',
        'App.main', 'App.about', 'App.investidea', 'App.investideas', 'App.investproduct', 'App.product', 'App.partners', 'App.structuredproducts',
        'App.createproduct',
        'App.login',
        'App.admin.products', 'App.admin.broker', 'App.admin.investidea', 'App.admin.topproducts', 'App.admin.productparams', 'App.admin.csv', 'App.admin.system'
    ]).config(['$httpProvider',
    function ($httpProvider) {
        $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
                return {
                    'request': function (config) {
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
    .run(function ($rootScope, $location, $cookieStore, UserService) {
        //Add string format
        if (!String.prototype.format) {
            String.prototype.format = function () {
                var args = arguments;
                return this.replace(/{(\d+)}/g, function (match, number) {
                    return typeof args[number] != 'undefined'
                        ? args[number]
                        : match
                        ;
                });
            };
        }

        var desktop = 992;
        var desktopShow = false;

        resize();
        function resize() {
            if (jQuery(window).width() >= desktop) {
                desktopShow = true;
            } else {
                desktopShow = false;
            }
        };

        $rootScope.isDesktopShow = function () {
            return desktopShow;
        }
        $rootScope.isMobileShow = function () {
            return !$rootScope.isDesktopShow();
        }

        jQuery(window).resize(function () {
            resize();
            $rootScope.$apply();
        });


        /!* Try getting valid user from cookie or go to login page *!/
        var originalPath = $location.path();
        //$location.path("/login");

        var authToken = $cookieStore.get('authToken');
        var adminUriRegexp = /\/admin\/?.*/;
        if (authToken !== undefined && originalPath.match(adminUriRegexp)) {
            $rootScope.authToken = authToken;
            UserService.get(function (user) {
                $rootScope.user = user;
                //$location.path(originalPath);
            });
        }

    });
angular.module('App.main', [])
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'resources/js/App/main/main.html',
                controller: 'mainCtrl',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/main/main.js').then(function () {
                            return $ocLazyLoad.load('resources/lib/jquery.maskedinput-1.3.min.js').then(function () {

                            })
                        });
                    }]
                }
            })
        }]);
angular.module('App.about', [])
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/about', {
                templateUrl: 'resources/js/App/about/about.html',
                controller: 'aboutCtrl',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/about/about.js');
                    }]
                }
            })
        }]);
angular.module('App.investidea', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/investidea', {
                templateUrl: 'resources/js/App/investidea/investidea.html',
                controller: 'investidea',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/investidea/investidea.js');
                    }]
                }
            })
        }]);
angular.module('App.investideas', [])
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/investideas', {
                templateUrl: 'resources/js/App/investideas/investideas.html',
                controller: 'investideas',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/investideas/investideas.js');
                    }]
                }
            })
        }]);
angular.module('App.investproduct', [])
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/investproduct', {
                templateUrl: 'resources/js/App/investproduct/investproduct.html',
                controller: 'investproduct',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/investproduct/investproduct.js');
                    }]
                }
            })
        }]);
angular.module('App.product', [])
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/product', {
                templateUrl: 'resources/js/App/product/product.html',
                controller: 'product',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/product/product.js').then(function() {
                            $ocLazyLoad.load('resources/lib/Chart.min.js').then(function() {});
                        });
                    }]
                }
            })
        }]);
angular.module('App.partners', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/partners', {
                templateUrl: 'resources/js/App/partners/partners.html',
                controller: 'partnersCtrl',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/partners/partners.js');
                    }]
                }
            })
        }]);
angular.module('App.structuredproducts', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/structuredproducts', {
                templateUrl: 'resources/js/App/structuredproducts/structuredproducts.html',
                controller: 'structuredproductsCtrl',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/structuredproducts/structuredproducts.js');
                    }]
                }
            })
        }]);
angular.module('App.login', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/login', {
                templateUrl: 'resources/js/App/login/login.html',
                controller: 'login',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/login/login.js');
                    }]
                }
            })
        }]);
angular.module('App.admin', []);
angular.module('App.admin.products', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin', {
                templateUrl: 'resources/js/App/admin/products/admin-products.html',
                controller: 'admin-products',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/admin/products/admin-products.js').then(function() {});
                    }]
                }
            })
        }]);
angular.module('App.admin.broker', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/broker', {
                templateUrl: 'resources/js/App/admin/broker/admin-broker.html',
                controller: 'admin-broker',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/admin/broker/admin-broker.js');
                    }]
                }
            })
        }]);
angular.module('App.admin.investidea', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/investidea', {
                templateUrl: 'resources/js/App/admin/investidea/admin-investidea.html',
                controller: 'admin-investidea',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/admin/investidea/admin-investidea.js');
                    }]
                }
            })
        }]);
angular.module('App.admin.topproducts', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/topproducts', {
                templateUrl: 'resources/js/App/admin/topproducts/admin-topproducts.html',
                controller: 'admin-topproducts',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/admin/topproducts/admin-topproducts.js');
                    }]
                }
            })
        }]);
angular.module('App.admin.productparams', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/productparams', {
                templateUrl: 'resources/js/App/admin/productparams/admin-productparams.html',
                controller: 'admin-productparams',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/admin/productparams/admin-productparams.js');
                    }]
                }
            })
        }]);
angular.module('App.admin.csv', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/csv', {
                templateUrl: 'resources/js/App/admin/csv/admin-csv.html',
                controller: 'adminCsvCtrl',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/admin/csv/admin-csv.js');
                    }]
                }
            })
        }]);
angular.module('App.createproduct', [])
    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.when('/createproduct', {
                templateUrl: 'resources/js/App/createproduct/createproduct.html',
                controller: 'createproduct',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                            return $ocLazyLoad.load('resources/js/App/createproduct/createproduct.js').then(function () {
                                return $ocLazyLoad.load('resources/lib/d3.min.js').then(function () {
                                    return $ocLazyLoad.load('resources/lib/hexbin.js').then(function () {
                                        return $ocLazyLoad.load('resources/lib/hexParams.js').then(function() {
                                            return $ocLazyLoad.load('resources/lib/jquery.maskedinput-1.3.min.js');
                                        });
                                    });
                                });
                            });
                    }]
                }
            })
        }]);
angular.module('App.admin.system', [])
    .config(['$routeProvider',
        function($routeProvider){
            $routeProvider.when('/admin/system', {
                templateUrl: 'resources/js/App/admin/systemproperties/admin-system.html',
                controller: 'admin-system',
                resolve: {
                    load: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load('resources/js/App/admin/systemproperties/admin-system.js');
                    }]
                }
            })
        }]);

var App = function () {

    //Fixed Header
    function handleHeader() {
        jQuery(window).scroll(function () {
            if (jQuery(window).scrollTop()) {
                jQuery(".header-fixed .header-sticky").addClass("header-fixed-shrink");
            }
            else {
                jQuery(".header-fixed .header-sticky").removeClass("header-fixed-shrink");
            }
        });
    }

    //Header Mega Menu
    function handleMegaMenu() {
        jQuery(document).on('click', '.mega-menu .dropdown-menu', function (e) {
            e.stopPropagation();
        })
    }

    //Search Box (Header)
    function handleSearch() {
        jQuery('.search').click(function () {
            if (jQuery('.search-btn').hasClass('fa-search')) {
                jQuery('.search-open').fadeIn(500);
                jQuery('.search-btn').removeClass('fa-search');
                jQuery('.search-btn').addClass('fa-times');
            } else {
                jQuery('.search-open').fadeOut(500);
                jQuery('.search-btn').addClass('fa-search');
                jQuery('.search-btn').removeClass('fa-times');
            }
        });
    }

    //Search Box v1 (Header v5)
    function handleSearchV1() {
        jQuery('.header-v5 .search-button').click(function () {
            jQuery('.header-v5 .search-open').slideDown();
        });

        jQuery('.header-v5 .search-close').click(function () {
            jQuery('.header-v5 .search-open').slideUp();
        });

        jQuery(window).scroll(function () {
            if (jQuery(this).scrollTop() > 1) jQuery('.header-v5 .search-open').fadeOut('fast');
        });
    }

    // Search Box v2 (Header v8)
    function handleSearchV2() {
        $(".blog-topbar .search-btn").on("click", function () {
            if (jQuery(".topbar-search-block").hasClass("topbar-search-visible")) {
                jQuery(".topbar-search-block").slideUp();
                jQuery(".topbar-search-block").removeClass("topbar-search-visible");
            } else {
                jQuery(".topbar-search-block").slideDown();
                jQuery(".topbar-search-block").addClass("topbar-search-visible");
            }
        });
        $(".blog-topbar .search-close").on("click", function () {
            jQuery(".topbar-search-block").slideUp();
            jQuery(".topbar-search-block").removeClass("topbar-search-visible");
        });
        jQuery(window).scroll(function () {
            jQuery(".topbar-search-block").slideUp();
            jQuery(".topbar-search-block").removeClass("topbar-search-visible");
        });
    }

    // TopBar (Header v8)
    function handleTopBar() {
        $(".topbar-toggler").on("click", function () {
            if (jQuery(".topbar-toggler").hasClass("topbar-list-visible")) {
                jQuery(".topbar-menu").slideUp();
                jQuery(this).removeClass("topbar-list-visible");
            } else {
                jQuery(".topbar-menu").slideDown();
                jQuery(this).addClass("topbar-list-visible");
            }
        });
    }

    // TopBar SubMenu (Header v8)
    function handleTopBarSubMenu() {
        $(".topbar-list > li").on("click", function (e) {
            if (jQuery(this).children("ul").hasClass("topbar-dropdown")) {
                if (jQuery(this).children("ul").hasClass("topbar-dropdown-visible")) {
                    jQuery(this).children(".topbar-dropdown").slideUp();
                    jQuery(this).children(".topbar-dropdown").removeClass("topbar-dropdown-visible");
                } else {
                    jQuery(this).children(".topbar-dropdown").slideDown();
                    jQuery(this).children(".topbar-dropdown").addClass("topbar-dropdown-visible");
                }
            }
            //e.preventDefault();
        });
    }

    //Sidebar Navigation Toggle
    function handleToggle() {
        jQuery('.list-toggle').on('click', function () {
            jQuery(this).toggleClass('active');
        });

        /*
         jQuery('#serviceList').on('shown.bs.collapse'), function() {
         jQuery(".servicedrop").addClass('glyphicon-chevron-up').removeClass('glyphicon-chevron-down');
         }

         jQuery('#serviceList').on('hidden.bs.collapse'), function() {
         jQuery(".servicedrop").addClass('glyphicon-chevron-down').removeClass('glyphicon-chevron-up');
         }
         */
    }

    //Equal Height Columns    
    function handleEqualHeightColumns() {
        var EqualHeightColumns = function () {
            $(".equal-height-columns").each(function () {
                heights = [];
                $(".equal-height-column", this).each(function () {
                    $(this).removeAttr("style");
                    heights.push($(this).height()); // write column's heights to the array
                });
                $(".equal-height-column", this).height(Math.max.apply(Math, heights)); //find and set max
            });
        }

        EqualHeightColumns();
        $(window).resize(function () {
            EqualHeightColumns();
        });
        $(window).load(function () {
            EqualHeightColumns("img.equal-height-column");
        });
    }

    //Hover Selector
    function handleHoverSelector() {
        $('.hoverSelector').on('hover', function (e) {
            $('.hoverSelectorBlock', this).toggleClass('show');
            e.stopPropagation();
        });
    }

    //Bootstrap Tooltips and Popovers
    function handleBootstrap() {
        /*Bootstrap Carousel*/
        jQuery('.carousel').carousel({
            interval: 15000,
            pause: 'hover'
        });

        /*Tooltips*/
        jQuery('.tooltips').tooltip();
        jQuery('.tooltips-show').tooltip('show');
        jQuery('.tooltips-hide').tooltip('hide');
        jQuery('.tooltips-toggle').tooltip('toggle');
        jQuery('.tooltips-destroy').tooltip('destroy');

        /*Popovers*/
        jQuery('.popovers').popover();
        jQuery('.popovers-show').popover('show');
        jQuery('.popovers-hide').popover('hide');
        jQuery('.popovers-toggle').popover('toggle');
        jQuery('.popovers-destroy').popover('destroy');
    }

    return {
        init: function () {
            handleBootstrap();
            handleSearch();
            handleSearchV1();
            handleSearchV2();
            handleTopBar();
            handleTopBarSubMenu();
            handleToggle();
            handleHeader();
            handleMegaMenu();
            handleHoverSelector();
            handleEqualHeightColumns();
        },

        //Counters 
        initCounter: function () {
            jQuery('.counter').counterUp({
                delay: 10,
                time: 1000
            });
        },

        //Parallax Backgrounds
        initParallaxBg: function () {
            jQuery(window).load(function () {
                jQuery('.parallaxBg').parallax("50%", 0.2);
                jQuery('.parallaxBg1').parallax("50%", 0.4);
            });
        },

        //Scroll Bar 
        initScrollBar: function () {
            jQuery('.mCustomScrollbar').mCustomScrollbar({
                theme: "minimal",
                scrollInertia: 200,
                scrollEasing: "linear"
            });
        },

        // Sidebar Menu Dropdown
        initSidebarMenuDropdown: function () {
            function SidebarMenuDropdown() {
                jQuery('.header-v7 .dropdown-toggle').on('click', function () {
                    jQuery('.header-v7 .dropdown-menu').stop(true, false).slideUp();
                    jQuery('.header-v7 .dropdown').removeClass('open');

                    if (jQuery(this).siblings('.dropdown-menu').is(":hidden") == true) {
                        jQuery(this).siblings('.dropdown-menu').stop(true, false).slideDown();
                        jQuery(this).parents('.dropdown').addClass('open');
                    }
                });
            }

            SidebarMenuDropdown();
        },


        //Animate Dropdown
        initAnimateDropdown: function () {
            function MenuMode() {
                jQuery('.dropdown').on('show.bs.dropdown', function () {
                    jQuery(this).find('.dropdown-menu').first().stop(true, true).slideDown();
                });
                jQuery('.dropdown').on('hide.bs.dropdown', function () {
                    jQuery(this).find('.dropdown-menu').first().stop(true, true).slideUp();
                });
            }

            jQuery(window).resize(function () {
                if (jQuery(window).width() > 768) {
                    MenuMode();
                }
            });

            if (jQuery(window).width() > 768) {
                MenuMode();
            }
        },

    };
}();