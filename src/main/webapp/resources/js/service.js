app.service('restService', [
    '$http', '$log',
    function ($http, $log) {
        var dataUri = 'api/v1/data/',
            serviceUri = 'api/v1/service/';

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
    }

}]);