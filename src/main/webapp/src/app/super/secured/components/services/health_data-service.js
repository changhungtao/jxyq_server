/**
 * Created by zhanga.fnst on 2015/6/19.
 */
'use strict';

goog.provide('jxsprsec.health_data.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxsprsec.health_data.Service.$inject = [
    "$rootScope",
    "$http",
    "$q"
];
jxsprsec.health_data.Service = function($rootScope,$http, $q) {
    var health_data = this;

    function sendRequest(req) {
        return $http(req)
            .then(success)
            .catch(fail);

        function success(response) {
            return response.data.success_message;
        }
        function fail(error) {
            console.log(error);
            return $q.reject("rejected");
        }
    };
    //3.3.4.3	PUT /common/measurements/wristbands/{wid}
    health_data.saveWristbands = function(wid,wristband){
        if(wid == undefined || wid == ''){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/common/measurements/wristbands/' + wid,
            data: wristband
        }
        return sendRequest(req);
    }
    //3.3.4.5	PUT /common/measurements/sphygmomanometers/{sid}
    health_data.saveSphygmomanometers = function(sid,sphygmomanometer){
        if(sid == undefined || sid == ''){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/common/measurements/sphygmomanometers/' + sid,
            data: sphygmomanometer
        }
        return sendRequest(req);
    }
    //3.3.4.7	PUT /common/measurements/oximeter/{oid}
    health_data.saveOximeters = function(oid,oximeter){
        if(oid == undefined || oid == ''){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/common/measurements/oximeter/' + oid,
            data: oximeter
        }
        return sendRequest(req);
    }
    //3.3.4.9	PUT /common/measurements/glucosemeters/{gid}
    health_data.saveGlucosemeters = function(gid,glucosemeter){
        if(gid == undefined || gid == ''){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/common/measurements/glucosemeters/' + gid,
            data: glucosemeter
        }
        return sendRequest(req);
    }
    //3.3.4.11	PUT /common/measurements/thermometers/{tid}
    health_data.saveThermometers = function(tid,thermometer){
        if(tid == undefined || tid == ''){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/common/measurements/thermometers/' + tid,
            data: thermometer
        }
        return sendRequest(req);
    }

    //3.3.4.13	PUT /common/measurements/fat/{fid}
    health_data.saveFats = function(fid,fat){
        if(fid == undefined || fid == ''){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/common/measurements/fat/' + fid,
            data: fat
        }
        return sendRequest(req);
    }
    //3.4.4.1	POST /doctors/permissions
    health_data.getPermissions = function(permission_list){
        if(permission_list == undefined || permission_list == ''){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/permissions',
            data: permission_list
        }
        return sendRequest(req);
    }

    //3.4.4.3	POST /doctors/permissions/{pid}/query
    health_data.getPermissionData =function(pid,page_params){
        if(pid == undefined || pid == ''){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/permissions/'+pid+'/query',
            data: page_params
        }
        return sendRequest(req);
    }

    return health_data;
};

