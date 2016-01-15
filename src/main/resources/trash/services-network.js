MASS.service('principalService', ['$http', '$log', function ($http, $log) {
    var currentPrincipal;

    var fetchPrincipal = function () {
        $log.info("fetching securityService.fetchPrincipal...");

        return $http.get('app/security/principal').
            then(function (response) {
                currentPrincipal = response.data;
                $log.info("current principal: " + angular.fromJson(currentPrincipal));
            }, function () {
                $log.info("unable to get principal");
            });
    };

    var getPrincipal = function () {
        return currentPrincipal;
    };

    return {
        getPrincipal: getPrincipal,
        fetchPrincipal: fetchPrincipal
    };
}]);

MASS.service('configurationService', ['$http', '$log', function ($http, $log) {
    var applications;

    var fetchApplications = function () {
        $log.info("fetching configurationService.fetchApplications...");
        return $http.get('app/configuration/applications').
            then(function (response) {
                applications = response.data;
            }, function () {
                $log.info("unable to get applications");
            });
    };

    var getApplications = function () {
        return applications;
    };

    return {
        fetchApplications: fetchApplications,
        getApplications: getApplications
    };
}]);

MASS.service('catalogService', ['$http', '$log', '$location', 'appSelectorService',
    function ($http, $log, $location, appSelectorService) {
        var injectResponseDelivery = function(promise, onSuccess, onFail) {
            return promise.then(function (response) {
                if(!response.data
                        || angular.isDefined(response.data.exceptionMessage)
                        || angular.isDefined(response.data.cause)) {
                    $log.warn(response.data);
                    onFail(response.data);
                } else {
                    onSuccess(response.data)
                }
            }, function (response) {
                onFail(response.data);
            })
        };

        var fetchInfoById = function (id, elementType, onSuccess, onFail) {
            $log.info("Fetching " + elementType + " Info ...");
            return injectResponseDelivery($http.get('app/new/' + elementType + '/' + id),
                onSuccess, onFail);
        };

        return {
            updateInfo: function (urlPattern, data, onSuccess, onFail) {
                var url = urlPattern;
                var selectedAppKeys = appSelectorService.getSelectedAppKeys();
                if (selectedAppKeys) {
                    url = url + "?selectedKeys=" + selectedAppKeys.join();
                }
                $log.info("Update for url = " + url + ", data = " + data);
                return injectResponseDelivery($http.post(url, data),
                    onSuccess, onFail);
            },

            update: function(objectToUpdate, elementType, fieldName) {
                $log.info(objectToUpdate);
                this.updateInfo(
                    "app/new/" + elementType + "/update/" + fieldName,
                    {id: objectToUpdate.id, newValue: objectToUpdate[fieldName]},
                    function (data) {
                        $location.path('/pipeline/' + data);
                    },
                    function (data) {
                        alert(data);
                    })
            },

            fetchCategoryInfo: function (id, onSuccess, onFail) {
                return fetchInfoById(id, "category", onSuccess, onFail);
            },

            fetchProductInfo: function (id, onSuccess, onFail) {
                return fetchInfoById(id, "product", onSuccess, onFail);
            },

            fetchUpcInfo: function (id, onSuccess, onFail) {
                return fetchInfoById(id, "upc", onSuccess, onFail);
            }
        };
    }]);

MASS.service('pipelineService', ['$http', '$log', function ($http, $log) {
    return {

        fetchByKey: function (key, onSuccess, onFail) {
            $log.info("Fetching pipeline details key = "+key);
            return $http.get('app/new/pipeline/track/' + key).
                then(function (response) {
                    onSuccess(response.data)
                }, function (response) {
                    onFail(response.data);
                });
        },

        fetchAll: function (onSuccess, onFail) {
            $log.info("Fetching all pipelines...");
            return $http.get('app/new/pipeline/list').
                then(function (response) {
                    onSuccess(response.data)
                }, function (response) {
                    onFail(response.data);
                });
        }
    };
}]);


/**
 * Service responsible for resource management of smart monkey.
 */
MASS.service('smResourceService', [
    '$http', '$log',
    function ($http, $log) {
        var resourceUri = 'psm/api/v1/resource/',
            jobUri = 'psm/api/v1/job/';

    return {
        getResourceList: function (resourceType, onSuccess, onFail) {
            $log.info("SM Recource Service (Network)");
            return $http.get(resourceUri + resourceType)
            .then(function (response) {
                onSuccess(response.data);
            }, function (response) {
                onFail(response.data);
            });
        },

        uploadFile: function (resourceType, onSuccess, onFail, file) {
            $log.info("SM Recource Service upload file" + file.name);
            var url = resourceUri + resourceType + '/' + file.name;
            $http.put(url, file,
                {
                    headers:{'Content-Type':'text/plain'}
                })
                .then(function (response) {
                    onSuccess(response);
                }, function (response) {
                    onFail(response);
                });
        },
        
        deleteResource: function (resourceToDelete, onSuccess, onFail) {
            $log.info("SM Recource Service delete resource " + resourceToDelete);
            return $http.delete(resourceUri + resourceToDelete)
            .then(function (response) {
                onSuccess(response.data);
            }, function (response) {
                onFail(response.data);
            });
        },

        launchConfiguration: function(resourceName, onSuccess, onFail) {
            $log.info("SM Resource Service: configuration '" + resourceName + "' launched.");
            $http.post(
                jobUri,
                {
                    configuration: resourceName
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        }
    }
}]);
MASS.service('smJobsService', ['$http', '$log', function ($http, $log) {
    var jobUri = 'psm/api/v1/job/';

    return {
        getJobs: function (page, limit, sortType, sortOrder, onSuccess, onFail) {
            $log.info("Fetching SM jobs");
            $http.get(
                jobUri, {
                    params: {
                        page: page,
                        limit: limit,
                        sortType:sortType,
                        sortOrder:sortOrder
                    }
                })
                .then(function (response) {
                    onSuccess(response.data)
                }, function (response) {
                    onFail(response.data);
                });
        },
        getJobInfo: function (jobId, onSuccess, onFail) {
            $log.info("Fetching SM jobinfo");
            $http.get(jobUri + jobId)
                .then(function (response) {
                    onSuccess(response.data)
                }, function (response) {
                    onFail(response.data);
                });
        },
        cancelJob: function (jobId, onSuccess, onFail) {
            $log.info('Stopping job ' + jobId);
            $http.delete(jobUri + jobId)
                .then(function (response) {
                    onSuccess(response.data)
                }, function (response) {
                    onFail(response);
                });
        },
        getPageUpdatePeriod: function(onSuccess) {
            $http.get('app/new/sm/jobs/pageUpdatePeriod')
                .then(function (response) {
                    onSuccess(response.data);
                }, function() {
                    onSuccess(10000);
                })
        }
    }
}]);
