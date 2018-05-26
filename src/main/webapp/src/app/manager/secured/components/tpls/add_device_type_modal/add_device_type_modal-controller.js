'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.add_device_type_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.add_device_type_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "$timeout",
  "constants",
  "db_operation"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.add_device_type_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,$timeout,db_operation) {


  $scope.product_types=[];
  $scope.product_types_selected={selected:undefined};

  $scope.device_submit={
    "device_name":undefined,
    "product_type_id":undefined
  }


  activate();

  function activate() {
    var promises = [getProductTypes()];
    return $q.all(promises).then(function() {
    });
  }

  function getProductTypes() {
    return constants.getProductTypes().then(function (data) {
      $scope.product_types = data.product_types;

      return $scope.product_types;
    });
  }

  $scope.disabledBtn = false;
  $scope.ok = function() {
    $scope.disabledBtn = true;
    $scope.jsonData={
      name:$scope.device_submit.device_name,
      product_type_id:$scope.device_submit.product_type_id
    }
    if(!validParam($scope.jsonData.name)){$scope.addAlert("warning", "设备名不能为空");$scope.disabledBtn = false;return;}
    if(!validParam($scope.jsonData.product_type_id)){$scope.addAlert("warning", "隶属产品类型不能为空");$scope.disabledBtn = false;return;}

    db_operation.postAddDeviceTypes($scope.jsonData).then(function(succ){
        $scope.addAlert("success", "保存成功,3秒后自动关闭！");
        $timeout(function(){
            $scope.cancel();
            $scope.disabledBtn = false;
        },3000);
    },function(error){
      $scope.addAlert("danger", "保存失败，请稍后再试！");
      $scope.disabledBtn = false;
    });
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };
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

    function validParam(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }


};
