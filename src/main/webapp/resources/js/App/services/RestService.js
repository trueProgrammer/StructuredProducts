angular.module('App.services', ['ngResource']).service('restService', [
    '$http', '$log',
    function ($http, $log) {
        var dataUri = 'api/v1/data/',
            serviceUri = 'api/v1/service/',
            adminUri = 'api/v1/admin/';

    return {
        sendEmail: function (json, onSuccess, onFail) {
            $http.post(
                serviceUri + "email",
                json
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        uploadFileToUrl: function(file){
            var fd = new FormData();
            fd.append('file', file);

            $http.post(
                dataUri + "fileupload",
                fd,
                {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }
            )
            .success(function(){
            })
            .error(function(){
            });
        },

        getProductParams: function(onSuccess, onFail) {
            $http.get(
                dataUri + "productparams"
            )
                .success(onSuccess)
                .error(onFail);
        },

        getProductUnderlayings: function(onSuccess, onFail) {
            $http.get(
                adminUri + "productparams/underlaying"
            )
                .success(onSuccess)
                .error(onFail);

        },

        addProductParam: function(param, onSuccess, onFail) {
            $http.post(
                adminUri + "productparams/add",
                param
            )
                .success(onSuccess)
                .error(onFail);
        },

        getProductWithParams: function(id, onSuccess, onFail) {
            $http.get(
                dataUri + "productwithparams",
                {
                    params: {
                        id: id
                    }
                }
            ).success(onSuccess).error(onFail);
        },

        getInvestIdeas: function (showOnMainPage, onSuccess, onFail) {
            $http.get(
                dataUri + "investideas",
                {
                    params: {
                        showOnMainPage: showOnMainPage
                    }
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        removeIdea: function(id, onSuccess, onFail) {
            $http.post(
                adminUri + "removeIdea",
                {
                    id: id
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                });
        },
        getInvestIdeaById: function (id, onSuccess, onFail) {
            $http.get(
                dataUri + "investidea",
                {
                    params: {
                        id: id
                    }
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        getTopProducts: function (time, onSuccess, onFail) {
            $http.get(
                adminUri + "getTopProducts",
                {
                    params: {
                        time: time
                    }
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        addToTop: function(ids, time, onSuccess, onFail) {
            $http.post(
                adminUri + "addToTop",
                {ids: ids, time: time}
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                });
        },

        removeFromTop: function(ids, time, onSuccess, onFail) {
            $http.post(
                adminUri + "removeFromTop",
                {ids: ids, time: time}
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                });
        },

        getAllProducts: function(onSuccess, onFail) {
            $http
                .get(dataUri + "allproducts", {

                })
                .then(
                function(response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            )
        },

        getTopProductsByTimeAndType: function(time, type, onSuccess, onFail) {
            $http.get(dataUri + "topproducts", {
                params: {
                    time: time,
                    type: type
                }
            })
                .then(
                function(response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            )
        },
        getProductsByType: function(types, onSuccess, onFail) {
            $http
                .get(dataUri + "productsbytype", {
                    params: {
                        types: types
                    }
                })
                .then(
                function(response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            )
        },

        getProductTypes: function (onSuccess, onFail) {
            $http.get(
                dataUri + "producttypes"
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        getTimeTypes: function (onSuccess, onFail) {
            $http.get(
                dataUri + "timetypes"
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        uploadProductsCsv: function(file, broker, onSuccess, onFail) {
            var formData = new FormData();
            formData.append('file', file);
            $http(
                {
                    method: 'POST',
                    url: adminUri + "products/csv?broker=" + broker,
                    headers: {
                        'Content-Type': undefined
                    },
                    data: formData
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data.message);
                }
            );
        },

        removeBroker: function(id, onSuccess, onFail) {
            $http.post(
                adminUri + 'brokerRemove',
                {
                    id: id
                }
            ).then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                });
        },

        addBroker: function(id, name, img, onSuccess, onFail){
            $http.post(
                adminUri + "brokerAdd",
                {
                    id: id,
                    name: name,
                    img: img
                }).then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        getAllBrokers: function(onSuccess, onFail) {
            $http.get(
                dataUri + "brokerGet"
            ).then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        addIdea: function(ideaJson, onSuccess, onFail) {
            $http.post(
                adminUri + "investIdeaAddOrUpdate",
                ideaJson
            ).then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        getInstrumentType: function (entityType, onSuccess, onFail) {
            $http.get(
                adminUri + "instrumentType",
                {
                    params: {
                        entityType: entityType
                    }
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        deleteInstrumentType: function (json, entityType, onSuccess, onFail) {
            $http.post(
                adminUri + "instrumentType/delete",
                json,
                {
                    params: {
                        entityType: entityType
                    }
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data.message);
                }
            );
        },

        updateInstrumentType: function (json, entityType, onSuccess, onFail) {
            $http.post(
                adminUri + "instrumentType/update",
                json,
                {
                    params: {
                        entityType: entityType
                    }
                }
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data.message);
                }
            );
        },

        downloadCsv: function(onSuccess, onFail) {
            $http.get(
                adminUri + "instrument/download"
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data.message);
                }
            );
        },

        createProductRequest: function(data, onSuccess, onFail) {
            $http.post(
                dataUri + "createProductRequest",
                data
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data.message);
                }
            );
        },

        getUnderlayingHistoricalQuotes: function(id, onSuccess, onFail) {
            $http.get(
                dataUri + "historicalquotes",
                {
                    params: {
                        id: id
                    }
                }
            ).success(onSuccess).error(onFail);
        },

        login: function (name, pass, onSuccess, onFail) {
            $http.post(
                "api/v1/user/instrumentType"
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data.message);
                }
            );
        }
    }
}]);