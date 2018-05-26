/**
 * Created by zhanga.fnst on 2015/7/3.
 */
'use strict';

goog.provide('jxmgrsec.doctor.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.doctor.Service.$inject = [
    "$rootScope",
    "$http",
    "$q"
];
jxmgrsec.doctor.Service = function($rootScope,$http, $q) {
    var doctor = this;
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

    //3.3.3.4.2	POST /admins/db/doctors
    doctor.getDoctors = function(dactor){
        if(!validParam(dactor)){
            return '';
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/admins/db/doctors',
            data: dactor
        };
       return  sendRequest(req);
    }

    //3.3.6.8	GET /admins/permissions/doctors/{did}
    doctor.getDoctorPermissions = function(did,page){
        if(!validParam(did)){
            return '';
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/admins/permissions/doctors/'+did,
            params: page
        };
        return  sendRequest(req);
    }

    //3.3.6.9	POST /admins/permissions/doctors/{did}
    doctor.addDoctorPermission = function(did,permission){
        if(!validParam(did) && !validParam(permission)){
            return '';
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/admins/permissions/doctors/'+did,
            data: permission
        };
        return  sendRequest(req);
    }

    //3.3.6.10	PUT /admins/permissions/doctors/{did}/{pid}
    doctor.updateDoctorPermission = function(did,pid,permission){
        if(!validParam(did) && !validParam(permission)){
            return '';
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/admins/permissions/doctors/'+did+'/'+pid,
            data: permission
        };
        return  sendRequest(req);
    }
    return doctor;
};
