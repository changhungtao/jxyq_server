'use strict';
/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.Ctrl');

jxmgrsec.main.twatch.Ctrl.$inject = [
  "$window",
  "$rootScope",
  "$timeout",
  "basic",
  "twatch"
];
/**
 * Twatch controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.twatch.Ctrl = function($scope,$rootScope, $timeout, $window, basic,twatch) {
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from twatch controller';

  // $scope.imei = "888888888888888";

    $rootScope.imeiChanged = function(newImei){
        if($scope.validParam(newImei) && newImei != $rootScope.imei){
            $rootScope.imei = newImei;
            return twatch.heartbeat({imei:newImei}).then(function(res){
                console.log('加速心跳');
            },function(error){
                console.log(error);
            });
        }
    }

    //Alert
    $scope.alerts = [];
    $scope.addAlert = function (type, msg) {
        var alert = {'type': type, 'msg': msg};
        $scope.alerts.push(alert);
        $timeout(function(){
            $scope.closeAlert($scope.alerts.indexOf(alert));
        }, 3000);
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.clearAlerts = function() {
        $scope.alerts = [];
    };

    $scope.validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

};

jxmgrsec.main.twatch.Ctrl.prototype.log = function(text) {
  console.log(text);
};
