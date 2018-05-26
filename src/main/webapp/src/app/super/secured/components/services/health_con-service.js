/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxsprsec.health_con.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxsprsec.health_con.Service.$inject = [
    "$rootScope",
    "$http",
    "$q"
];
jxsprsec.health_con.Service = function($rootScope,$http, $q) {
    var health_con = this;

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


    health_con.dealConsultation = function(consultation){
        if(consultation == undefined || consultation == ''){
            return null;
        }
        //var defer = $q.defer();
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/consultations',
            data: consultation
        }
        return sendRequest(req);
    }

    //3.4.5.2	GET /doctors/consultations/{cid}
    health_con.getQAandReply = function(cid){
        if(cid == undefined || cid == ''){
            return null;
        }
        //var defer = $q.defer();
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/consultations/' + cid
//          data: consultation
        }
        return sendRequest(req);
//        $http(req)
//            .success(function(res){
//                defer.resolve(res);
//            })
//            .error(function(err, status){
//                defer.reject(err);
//            })
//        return defer.promise;
    }

    //3.4.5.3	POST /doctors/consultations/{cid}
    health_con.sendReply = function(cid,content){
        if(cid == undefined || cid == ''){
            return null;
        }
        if(content == undefined || content == ''){
            return null;
        }
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/consultations/' + cid,
            data: content
        }
        return  sendRequest(req);;
    }

    return health_con;
};
