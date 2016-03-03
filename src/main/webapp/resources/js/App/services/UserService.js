angular.module('App.services')

.factory('UserService', function($resource) {
    return $resource('api/v1/user/', {},
        {
            authenticate: {
                method: 'POST',
                params: {'action' : 'authenticate'},
                headers : {'Content-Type': 'application/x-www-form-urlencoded'}
            }
        }
    );
});
