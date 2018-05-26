/**
 * Created by zhanga.fnst on 2015/7/6.
 */
'use strict';

goog.provide('jxmgrsec.manufactory.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.manufactory.Service.$inject = [
    "$rootScope",
    "$http",
    "$q"
];
jxmgrsec.manufactory.Service = function($rootScope,$http, $q) {
    var manufactory = this;
    var validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

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

    //3.3.3.2.2	POST /admins/db/manufactories
    manufactory.getManufactories = function(manufactory){
        if(!validParam(manufactory)){
            return '';
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/admins/db/manufactories',
            data: manufactory
        };
        return  sendRequest(req);
    }

    //3.3.6.4	PUT /admins/permissions/manufactories/{mid} 编辑厂商权限
    manufactory.editManPermission = function(mid,right_list){
        if(!validParam(right_list)){
            return '';
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/admins/permissions/manufactories/'+mid,
            data: right_list
        };
        return  sendRequest(req);
    }
    return manufactory;
};
