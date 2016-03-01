app.service('restService', [
    '$http', '$log',
    function ($http, $log) {
        var dataUri = 'api/v1/data/',
            serviceUri = 'api/v1/service/',
            adminUri = 'api/v1/admin/';

    return {
        sendEmail: function (json, onSuccess, onFail) {
            $log.info("Send email");
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

        getNews: function (onSuccess, onFail) {
            $log.info("Get news");
            $http.get(
                dataUri + "news"
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

        getInvestIdeas: function (showOnMainPage, onSuccess, onFail) {
            $log.info("Get invest ideas");
            $http.get(
                dataUri + "investideas",
                {
                    params: {
                        showOnMainPage: showOnMainPage,
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

        getInvestIdeaById: function (id, onSuccess, onFail) {
            $log.info("Get invest idea by id");
            $http.get(
                dataUri + "investidea",
                {
                    params: {
                        id: id,
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

        getProductTypes: function (timeType, productType, onSuccess, onFail) {
            $log.info("Get product types");
            $http.get(
                dataUri + "topproducts"
            )
                .then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        getTopProducts: function (timeType, productType, onSuccess, onFail) {
            $log.info("Get top products");
            $http.get(
                dataUri + "topproducts",
                {
                    params: {
                        timeType: timeType,
                        productType: productType,
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

        getAllProducts: function(onSuccess, onFail) {
            $log.info("Get all products");
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

        getProductsByType: function(types, onSuccess, onFail) {
            $log.info("Get products by type");
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
            $log.info("Get product types");
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
            $log.info("Get time types");
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

        uploadProductsCsv: function(file, onSuccess, onFail) {
            $log.info("Get time types");
            var formData = new FormData();
            formData.append('file', file);
            $http(
                {
                    method: 'POST',
                    url: adminUri + "products/csv",
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
                    onFail(response.data);
                }
            );
        },

        addBroker: function(name, img, onSuccess, onFail){
            $log.info("Add broker");
            var json = JSON.stringify({
                name: name,
                img: img
            });
            $http.post(
                adminUri + "brokerAdd",
                json
            ).then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        getAllBrokers: function(onSuccess, onFail) {
            $log.info("Get brokers");
            $http.get(
                adminUri + "brokerGet"
            ).then(
                function (response) {
                    onSuccess(response.data);
                }, function (response) {
                    onFail(response.data);
                }
            );
        },

        addIdea: function(ideaJson, onSuccess, onFail) {
            $log.info("Save idea");
            $http.post(
                adminUri + "investIdeaAdd",
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
            $log.info("Get instrument type");
            $http.get(
                adminUri + "instrumentType",
                {
                    params: {
                        entityType: entityType,
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
            $log.info("Delete instrument type");
            $http.post(
                adminUri + "instrumentType/delete",
                json,
                {
                    params: {
                        entityType: entityType,
                    },
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
            $log.info("Update instrument type");
            $http.post(
                adminUri + "instrumentType/update",
                json,
                {
                    params: {
                        entityType: entityType,
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
            $log.info("Download csv");
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

        login: function (name, pass, onSuccess, onFail) {
            $log.info("LOGIN");
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
        },

    }

}]);