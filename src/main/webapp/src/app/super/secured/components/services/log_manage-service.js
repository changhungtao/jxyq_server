/**
 * Created by zhanga.fnst on 2015/7/16.
 */
'use strict';

goog.provide('jxsprsec.log_manage.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxsprsec.log_manage.Service.$inject = [
    "$rootScope",
    "$http",
    "$q"
];
jxsprsec.log_manage.Service = function($rootScope,$http, $q) {
    var log_manage = this;

    function sendRequest(req) {
        return $http(req)
            .then(success)
            .catch(fail);

        function success(response) {
            return response.data.success_message;
        }
        function fail(error) {
            return $q.reject("rejected");
        }
    };

    //3.3.11.1	GET /admins/users/operation_logs
        log_manage.getOperationLogs = function(params){
        if(params == undefined || params == ''){
            return null;
        }
        //var defer = $q.defer();
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/admins/users/operation_logs',
            params: params
        }
        return sendRequest(req);
    }

    return log_manage;
};

