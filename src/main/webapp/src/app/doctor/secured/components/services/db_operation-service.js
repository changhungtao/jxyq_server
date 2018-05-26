'use strict';

goog.provide('jxdctsec.db_operation.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxdctsec.db_operation.Service.$inject = [
    "$rootScope",
    "$http",
    "$q"
];
jxdctsec.db_operation.Service = function($rootScope,$http, $q) {
    var db_operation = this;

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

    db_operation.getFiles = function(file){
        if(file == undefined || file == ''){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/files',
            data: file
        }
        return sendRequest(req);
    }
    //3.4.2.2	GET /doctors/patients/{pid}/files
    db_operation.getPatFiles = function(pid,file){
        if(!validParam(pid)){
            return null;
        }
        console.log('service');
        console.log(file);
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/files',
            params: file
        }
        return sendRequest(req);
    }

    //3.4.2.4	GET /doctors/patients/{pid}/wristbands/{wid}
    db_operation.getPatWrist = function(pid,wid){
        if(!validParam(pid) || !validParam(wid)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/wristbands/'+wid
            //data: file
        }
        return sendRequest(req);
    }

    //3.4.2.5	PUT /doctors/patients/{pid}/wristbands/{wid}
    db_operation.putPatWrist = function(pid,wid,patwrist){
        if(!validParam(pid) || !validParam(wid)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/wristbands/'+wid,
            data: patwrist
        }
        return sendRequest(req);
    }

    //3.4.2.6	GET /doctors/patients/{pid}/sphygmomanometers/{sid}
    db_operation.getPatSphyg = function(pid,sid){
        if(!validParam(pid) || !validParam(sid)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/sphygmomanometers/'+sid,
            //data: patwrist
        }
        return sendRequest(req);
    }

    //3.4.2.7	PUT /doctors/patients/{pid}/sphygmomanometers/{sid}
    db_operation.putPatSphyg = function(pid,sid,patsphy){
        if(!validParam(pid) || !validParam(sid)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/sphygmomanometers/'+sid,
            data: patsphy
        }
        return sendRequest(req);
    }

    //3.4.2.8	GET /doctors/patients/{pid}/oximeters/{oid}
    db_operation.getPatOxi = function(pid,oid){
        if(!validParam(pid) || !validParam(oid)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/oximeters/'+oid,
            //data: patwrist
        }
        return sendRequest(req);
    }

    //3.4.2.9	PUT /doctors/patients/{pid}/oximeters/{oid}
    db_operation.putPatOxi = function(pid,oid,patoxi){
        if(!validParam(pid) || !validParam(oid)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/oximeters/'+oid,
            data: patoxi
        }
        return sendRequest(req);
    }
    //3.4.2.10	GET /doctors/patients/{pid}/glucosemeters/{gid}
    db_operation.getPatGlucose = function(pid,gid){
        if(!validParam(pid) || !validParam(gid)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/glucosemeters/'+gid,
        }
        return sendRequest(req);
    }

    //3.4.2.11	PUT /doctors/patients/{pid}/glucosemeters/{gid}
    db_operation.putPatGlucose = function(pid,gid,patglucose){
        if(!validParam(pid) || !validParam(gid)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/glucosemeters/'+gid,
            data: patglucose
        }
        return sendRequest(req);
    }

    //3.4.2.12	GET /doctors/patients/{pid}/thermometers/{tid}
    db_operation.getPatThermome = function(pid,tid){
        if(!validParam(pid) || !validParam(tid)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/thermometers/'+tid
        }
        return sendRequest(req);
    }

    //3.4.2.13	PUT /doctors/patients/{pid}/thermometers/{tid}
    db_operation.putPatThermome = function(pid,tid,patthermome){
        if(!validParam(pid) || !validParam(tid)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/thermometers/'+tid,
            data: patthermome
        }
        return sendRequest(req);
    }
    //3.4.2.14	GET /doctors/patients/{pid}/fats/{fid}
    db_operation.getPatFat = function(pid,fid){
        if(!validParam(pid) || !validParam(fid)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/fats/'+fid
        }
        return sendRequest(req);
    }

    //3.4.2.15	PUT /doctors/patients/{pid}/fats/{fid}
    db_operation.putPatFat = function(pid,fid,patfat){
        if(!validParam(pid) || !validParam(fid)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/fats/'+fid,
            data: patfat
        }
        return sendRequest(req);
    }

    //3.4.2.16	GET /doctors/patients/{pid}/others/{otid}
    db_operation.getPatOther = function(pid,otid){
        if(!validParam(pid) || !validParam(otid)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/others/'+otid
        }
        return sendRequest(req);
    }

    //3.4.2.17	PUT /doctors/patients/{pid}/others/{otid}
    db_operation.putPatOther = function(pid,otid,patother){
        if(!validParam(pid) || !validParam(otid)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/others/'+otid,
            data: patother
        }
        return sendRequest(req);
    }
    return db_operation;
};
