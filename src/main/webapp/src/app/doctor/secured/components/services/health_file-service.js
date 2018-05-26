'use strict';

goog.provide('jxdctsec.health_file.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxdctsec.health_file.Service.$inject = [
    "$rootScope",
    "$http",
    "$q"
];
jxdctsec.health_file.Service = function($rootScope,$http, $q) {
    var health_file = this;

    function sendRequest(req) {
        return $http(req)
            .then(success)
            .catch(fail);

        function success(response) {
            return response.data.success_message;
        }
        function fail(error) {
            return $q.reject(error.data);
        }
    };

    var validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

    //3.4.3.1	GET /doctors/patients
    health_file.getHealthFile = function(params){
        if(!validParam(params)){
            return null;
        }
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/patients',
            params: params
        }
        return sendRequest(req);
    }

    //3.4.3.2	POST /doctors/patients
    health_file.addPatient = function(patient){
        if(!validParam(patient)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients',
            data: patient
        }
        return sendRequest(req);
    }

    //3.4.3.3	PUT /doctors/patients/{pid}
    health_file.updatePatient = function(pid,patient){
        if(!validParam(pid) || !validParam(patient)){
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/patients/'+pid,
            data: patient
        }
        return sendRequest(req);
    }

    //3.4.3.4	POST /doctors/patients/{pid}/wristbands/new
    health_file.addWristbandData = function(pid,wristbands){
        if(!validParam(pid) || !validParam(wristbands)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/wristbands/new',
            data: wristbands
        }
        return sendRequest(req);
    }

    //3.4.3.5	POST /doctors/patients/{pid}/sphygmomanometers/new
    health_file.addSphygmomanometerData = function(pid,sphygmomanometers){
        if(!validParam(pid) || !validParam(sphygmomanometers)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/sphygmomanometers/new',
            data: sphygmomanometers
        }
        return sendRequest(req);
    }

    //3.4.3.6	POST /doctors/patients/{pid}/oximeters/new
    health_file.addOximeterData = function(pid,oximeters){
        if(!validParam(pid) || !validParam(oximeters)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/oximeters/new',
            data: oximeters
        }
        return sendRequest(req);
    }

    //3.4.3.7	POST /doctors/patients/{pid}/glucosemeters/new
    health_file.addGlucosemeterData = function(pid,glucosemeters){
        if(!validParam(pid) || !validParam(glucosemeters)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/glucosemeters/new',
            data: glucosemeters
        }
        return sendRequest(req);
    }

    //3.4.3.8	POST /doctors/patients/{pid}/thermometers/new
    health_file.addThermometerData = function(pid,thermometers){
        if(!validParam(pid) || !validParam(thermometers)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/thermometers/new',
            data: thermometers
        }
        return sendRequest(req);
    }

    //3.4.3.9	POST /doctors/patients/{pid}/fats/new
    health_file.addFatsData = function(pid,fats){
        if(!validParam(pid) || !validParam(fats)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/fats/new',
            data: fats
        }
        return sendRequest(req);
    }

    //3.4.3.10	POST /doctors/patients/{pid}/others/new
    health_file.addOthersData = function(pid,others){
        if(!validParam(pid) || !validParam(others)){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/patients/'+pid+'/others/new',
            data: others
        }
        return sendRequest(req);
    }
    return health_file;
};
