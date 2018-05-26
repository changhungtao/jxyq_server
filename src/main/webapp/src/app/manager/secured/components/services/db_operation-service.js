'use strict';

goog.provide('jxmgrsec.db_operation.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.db_operation.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxmgrsec.db_operation.Service = function($rootScope, $http, $q) {
  var db_operation = this;

  var validParam = function(param) {
    if (param != undefined && param != null && param != "") {
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

  db_operation.getFiles = function(file) {
    if (file == undefined || file == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/doctors/patients/files',
      data: file
    }
    return sendRequest(req);
  }

  //3.3.3.3.1   POST /admins/db/users
  db_operation.getUsers = function(reqData) {
    if (reqData == undefined || reqData == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/users',
      data: reqData
    }
    return sendRequest(req);
  };

  //3.3.3.3.2 POST /admins/db/users/fuzzy_search
  db_operation.getUsersByName = function(reqData) {
    if (reqData == undefined || reqData == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/users/fuzzy_search',
      data: reqData
    }
    return sendRequest(req);
  };

  //3.3.3.3.3 GET /admins/db/users/{uid}
  db_operation.getUserDetail = function(uid) {
    if (uid == undefined || uid == '') {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/users/' + uid,
    }
    return sendRequest(req);
  };

  //3.3.3.3.4 PUT /admins/db/users/{uid}/state
  db_operation.putUserStatus = function(uid, statusData) {
    if (uid == undefined || uid == '') {
      return null;
    }
    if (statusData == undefined || statusData == '') {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/users/' + uid + '/state',
      data: statusData
    }
    return sendRequest(req);
  };


  //3.3.3.3.5 PUT /admins/db/users/{uid}
  db_operation.putUser = function(uid, userInfo) {
    if (uid == undefined || uid == '') {
      return null;
    }
    if (userInfo == undefined || userInfo == '') {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/users/' + uid,
      data: userInfo
    }
    return sendRequest(req);
  };


  //3.3.3.4.1 POST /admins/db/doctors/add
  db_operation.addDoctor = function(doctorInfo) {
    if (doctorInfo == undefined || doctorInfo == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/doctors/add',
      data: doctorInfo
    }
    return sendRequest(req);
  };

  //3.3.3.4.2 POST /admins/db/doctors
  db_operation.getDoctors = function(searchData) {
    if (searchData == undefined || searchData == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/doctors',
      data: searchData
    }
    return sendRequest(req);
  };

  //3.3.3.4.3 GET /admins/db/doctors/{did}
  db_operation.getDoctorDetail = function(did) {
    if (did == undefined || did == '') {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/doctors/' + did
    }
    return sendRequest(req);
  };



  //3.3.3.4.4 PUT /admins/db/doctors/{did}
  db_operation.modifyDoctor = function(did, doctorInfo) {
    if (did == undefined || did == '') {
      return null;
    }
    if (doctorInfo == undefined || doctorInfo == '') {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/doctors/' + did,
      data: doctorInfo
    }
    return sendRequest(req);
  };

  //33.3.3.4.5    PUT /admins/db/doctors/{did}/state
  db_operation.modifyDoctorStatus = function(did, statusData) {
    if (did == undefined || did == '') {
      return null;
    }
    if (statusData == undefined || statusData == '') {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/doctors/' + did + '/state',
      data: statusData
    }
    return sendRequest(req);
  };


  //33.3.3.4.5    PUT /admins/db/doctors/{did}/state
  db_operation.deleteDoctor = function(did) {
    if (did == undefined || did == '') {
      return null;
    }
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/db/doctors/' + did,
    }
    return sendRequest(req);
  };

  //3.3.3.2.1 POST /admins/db/manufactories/add
  db_operation.addManu = function(manuInfo) {
    if (manuInfo == undefined || manuInfo == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/manufactories/add',
      data: manuInfo
    }
    return sendRequest(req);
  };

  //3.3.3.2.2 POST /admins/db/manufactories
  db_operation.getManusByType = function(searchData) {
    if (searchData == undefined || searchData == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/manufactories',
      data: searchData
    }
    return sendRequest(req);
  };

  //3.3.3.2.3 POST /admins/db/manufactories/fuzzy_search
  db_operation.getManusByName = function(searchData) {
    if (searchData == undefined || searchData == '') {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/manufactories/fuzzy_search',
      data: searchData
    }
    return sendRequest(req);
  };


  //3.3.3.2.4 GET /admins/db/manufactories/{mid}
  db_operation.getManuDetail = function(mid) {
    if (mid == undefined || mid == '') {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/manufactories/' + mid,
    }
    return sendRequest(req);
  };

  //3.3.3.2.5 PUT /admins/db/manufactories/{mid}
  db_operation.modifyManu = function(mid, manuInfo) {
    if (mid == undefined || mid == '') {
      return null;
    }
    if (manuInfo == undefined || manuInfo == '') {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/manufactories/' + mid,
      data: manuInfo
    }
    return sendRequest(req);
  };


  //3.3.3.2.6 PUT /admins/db/manufactories/{mid}/state
  db_operation.modifyManuStatus = function(mid, statusData) {
    if (mid == undefined || mid == '') {
      return null;
    }
    if (statusData == undefined || statusData == '') {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/manufactories/' + mid + '/state',
      data: statusData
    }
    return sendRequest(req);
  };


  //3.3.3.2.7 DELETE /admins/db/manufactories/{mid}
  db_operation.deleteManu = function(mid) {
    if (mid == undefined || mid == '') {
      return null;
    }
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/db/manufactories/' + mid,
    }
    return sendRequest(req);
  };



  //3.4.2.2   GET /doctors/patients/{pid}/files
  db_operation.getPatFiles = function(pid, file) {
    if (!validParam(pid)) {
      return null;
    }
    console.log('service');
    console.log(file);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/files',
      params: file
    }
    return sendRequest(req);
  };

  //3.4.2.4   GET /doctors/patients/{pid}/wristbands/{wid}
  db_operation.getPatWrist = function(pid, wid) {
    if (!validParam(pid) || !validParam(wid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/wristbands/' + wid
        //data: file
    }
    return sendRequest(req);
  };

  //3.4.2.5   PUT /doctors/patients/{pid}/wristbands/{wid}
  db_operation.putPatWrist = function(pid, wid, patwrist) {
    if (!validParam(pid) || !validParam(wid)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/wristbands/' + wid,
      data: patwrist
    }
    return sendRequest(req);
  };

  //3.4.2.6   GET /doctors/patients/{pid}/sphygmomanometers/{sid}
  db_operation.getPatSphyg = function(pid, sid) {
    if (!validParam(pid) || !validParam(sid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/sphygmomanometers/' + sid,
      //data: patwrist
    }
    return sendRequest(req);
  };

  //3.4.2.7   PUT /doctors/patients/{pid}/sphygmomanometers/{sid}
  db_operation.putPatSphyg = function(pid, sid, patsphy) {
    if (!validParam(pid) || !validParam(sid)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/sphygmomanometers/' + sid,
      data: patsphy
    }
    return sendRequest(req);
  };

  //3.4.2.8   GET /doctors/patients/{pid}/oximeters/{oid}
  db_operation.getPatOxi = function(pid, oid) {
    if (!validParam(pid) || !validParam(oid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/oximeters/' + oid,
      //data: patwrist
    }
    return sendRequest(req);
  };

  //3.4.2.9   PUT /doctors/patients/{pid}/oximeters/{oid}
  db_operation.putPatOxi = function(pid, oid, patoxi) {
    if (!validParam(pid) || !validParam(oid)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/oximeters/' + oid,
      data: patoxi
    }
    return sendRequest(req);
  };
  //3.4.2.10  GET /doctors/patients/{pid}/glucosemeters/{gid}
  db_operation.getPatGlucose = function(pid, gid) {
    if (!validParam(pid) || !validParam(gid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/glucosemeters/' + gid,
    }
    return sendRequest(req);
  };

  //3.4.2.11  PUT /doctors/patients/{pid}/glucosemeters/{gid}
  db_operation.putPatGlucose = function(pid, gid, patglucose) {
    if (!validParam(pid) || !validParam(gid)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/glucosemeters/' + gid,
      data: patglucose
    }
    return sendRequest(req);
  };

  //3.4.2.12  GET /doctors/patients/{pid}/thermometers/{tid}
  db_operation.getPatThermome = function(pid, tid) {
    if (!validParam(pid) || !validParam(tid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/thermometers/' + tid
    }
    return sendRequest(req);
  };

  //3.4.2.13  PUT /doctors/patients/{pid}/thermometers/{tid}
  db_operation.putPatThermome = function(pid, tid, patthermome) {
    if (!validParam(pid) || !validParam(tid)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/thermometers/' + tid,
      data: patthermome
    }
    return sendRequest(req);
  };
  //3.4.2.14  GET /doctors/patients/{pid}/fats/{fid}
  db_operation.getPatFat = function(pid, fid) {
    if (!validParam(pid) || !validParam(fid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/fats/' + fid
    }
    return sendRequest(req);
  };

  //3.4.2.15  PUT /doctors/patients/{pid}/fats/{fid}
  db_operation.putPatFat = function(pid, fid, patfat) {
    if (!validParam(pid) || !validParam(fid)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/doctors/patients/' + pid + '/fats/' + fid,
      data: patfat
    }
    return sendRequest(req);
  };
  // 3.3.3.1.1    POST /admins/db/terminals
  db_operation.postTerminals = function(rbody) {
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/terminals',
      data: rbody
    }
    return sendRequest(req);
  };
  // 3.3.3.1.3    GET /admins/db/terminals/{tid}
  db_operation.getTerminalDetails = function(tid) {
    if (!validParam(tid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/terminals/' + tid,
    }
    return sendRequest(req);
  };
  //3.3.3.1.2 POST /admins/db/terminals/fuzzy_search
  db_operation.fuzzySearchTerminals = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/terminals/fuzzy_search',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.5.1 POST /admins/db/product_types
  db_operation.postDBProductTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/product_types',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.5.3 GET /admins/db/product_types/{pid}
  db_operation.getDBDeviceTypes = function(pid) {
    if (!validParam(pid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/product_types/' + pid,
    }
    return sendRequest(req);
  };
  //3.3.3.5.4 POST /admins/db/product_types/add
  db_operation.postAddProductTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/product_types/add',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.5.5 PUT /admins/db/product_types/{pid}
  db_operation.putEditProductTypes = function(pid, rbody) {
    if (!validParam(rbody) || !validParam(pid)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/product_types/' + pid,
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.5.2 POST /admins/db/product_types/fuzzy_search
  db_operation.fuzzySearchProductTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/product_types/fuzzy_search',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.5.3 GET /admins/db/product_types/{pid}
  db_operation.getDBProductTypeDetails = function(pid) {
    if (!validParam(pid)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/product_types/' + pid,
    }
    return sendRequest(req);
  };
  //3.3.3.6.2 POST /admins/db/device_types/fuzzy_search
  db_operation.fuzzySearchDeviceTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/device_types/fuzzy_search',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.6.3 GET /admins/db/device_types/{did}
  db_operation.getDBDeviceTypes = function(did) {
    if (!validParam(did)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/device_types/' + did,
    }
    return sendRequest(req);
  };
  //3.3.3.6.4 POST /admins/db/device_types/add
  db_operation.postAddDeviceTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/device_types/add',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.5.2 POST /admins/db/product_types/fuzzy_search
  db_operation.fuzzySearchProductTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/product_types/fuzzy_search',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.6.1 POST /admins/db/device_types
  db_operation.postDBDeviceTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/device_types',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.6.2 POST /admins/db/device_types/fuzzy_search
  db_operation.fuzzySearchDeviceTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/device_types/fuzzy_search',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.6.3 GET /admins/db/device_types/{did}
  db_operation.getDBManufacturers = function(did) {
    if (!validParam(did)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/device_types/' + did,
    }
    return sendRequest(req);
  };
  //3.3.3.6.4 POST /admins/db/device_types/add
  db_operation.postAddDeviceTypes = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/device_types/add',
      data: rbody
    }
    return sendRequest(req);
  };
  //3.3.3.6.5 PUT /admins/db/device_types/{did}
  db_operation.putEditDeviceTypes = function(did, rbody) {
    if (!validParam(rbody) || !validParam(did)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/device_types/' + did,
      data: rbody
    }
    return sendRequest(req);
  };

    //3.3.3.7.1	POST  /admins/db/terminal_catagories_more
    db_operation.postTerminalCatagorise = function(rbody) {
        if (!validParam(rbody)) {
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/admins/db/terminal_catagories_more',
            data: rbody
        }
        return sendRequest(req);
    };
    //3.3.3.7.4	PUT /admins/db/manufactories/{mid}/terminal_catagories/{tcid}/status
    db_operation.updateTerCatSataus = function(mid, tcid, state) {
        if (!validParam(mid) || !validParam(tcid) || !validParam(state)) {
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/admins/db/manufactories/' + mid+ '/terminal_catagories/' + tcid + '/status',
            data: state
        }
        return sendRequest(req);
    };


  return db_operation;
};
