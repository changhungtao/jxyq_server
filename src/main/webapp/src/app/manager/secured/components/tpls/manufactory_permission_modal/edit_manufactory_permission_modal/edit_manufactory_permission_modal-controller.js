/**
 * Created by zhanga.fnst on 2015/7/6.
 */
'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.edit_manufactory_permission_modal.Ctrl');

/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.edit_manufactory_permission_modal.Ctrl.$inject = [
    "$scope",
    "$filter",
    "$modalInstance",
    "$timeout",
    "$q",
    "entity",
    "constants",
    "manufactory"
];
jxmgrsec.edit_manufactory_permission_modal.Ctrl = function($scope,$filter,$modalInstance, $timeout,$q,entity,constants,manufactory) {

    //console.log(entity);

    var validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

   var device_length = 6;

    $scope.deviceTypes_selects = constants.gotDEVICETYPES();
    if(validParam($scope.deviceTypes_selects)){
        device_length = $scope.deviceTypes_selects.length;
    }
    $scope.entity = entity;

    $scope.checkedValid = function(){
        if($scope.entity.device_type_ids.length > 4 ){
            $scope.isErr = true;
            $scope.entity.device_type_ids.splice(4);
            $scope.addAlert('warning','最多可管理４种设备！');
            console.log($scope.entity.device_type_ids);
        }
    }

     var Right = {
        device_type_id:undefined,
        enabled:0
    }

    var RightLists = function(rights){
        var allRights = [];
        for(var i = 0; i< device_length;i++){
            Right.device_type_id = i + 1;
            if(validParam(rights.length && rights.length != 0)){
               if(rights.contains(i+1)){
                   Right.enabled = 1;
               }
            }
            allRights.push(angular.copy(Right));
            Right = {
                device_type_id:undefined,
                enabled:0
            }
        }
        return allRights;
    }

    $scope.clear = function(){
        $scope.entity.device_type_ids = [];
        $scope.isErr = false;
    }
    $scope.disabledBtn = false;
    $scope.updateManufactoryPermission = function(){
        $scope.disabledBtn = true;
        if(validParam($scope.entity)){
            if(validParam($scope.entity.device_type_ids) && $scope.entity.device_type_ids.length > 4){
                $scope.addAlert('warning','最多可管理４种设备！');
                $scope.disabledBtn = false;
                return;
            }
            return manufactory.editManPermission($scope.entity.manufactory_id,{right_list: RightLists($scope.entity.device_type_ids)}).then(function(data){
//                console.log(data);
                if(data.error_message == undefined){
                    $scope.addAlert("success","保存成功,3秒后自动关闭！");
                    $timeout(function(){
                        $scope.disabledBtn = false;
                        $scope.cancel();
                    }, 3000);
                }
            },function(error){
                $scope.addAlert("danger","保存失败，请稍后再试！");
//                alert('保存失败，请稍后再试！');
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

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    Array.prototype.contains = function (obj) {
        var i = this.length;
        while (i--) {
            if (this[i] === obj) {
                return true;
            }
        }
        return false;
    }

};
