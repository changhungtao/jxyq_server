'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.modify_product_type_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.modify_product_type_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  "entity",
  '$q',
  "constants",
  "db_operation"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.modify_product_type_modal.Ctrl = function($scope, $filter, $modalInstance, entity, $q, constants, db_operation, $timeout) {

  $scope.device_types = constants.gotDEVICETYPES();
  console.log(constants.gotDEVICETYPES());
  console.log(entity);
  $scope.help = {
    device_types_selected: undefined
  }
  $scope.help.device_types_selected = [];
  // $scope.productName = $filter('mapProductType')(entity.product_type_id);
  $scope.productName = entity.product_type_name;
//  console.log($scope.productName);

  $scope.disabledBtn = false;
  $scope.submit = function() {
    $scope.disabledBtn = true;
    $scope.jsonData = {
      name: $scope.productName,
      device_type_ids: $scope.help.device_types_selected
    }
    db_operation.putEditProductTypes(entity.product_type_id, $scope.jsonData).then(function(succ) {
      $scope.addAlert("success", "保存成功,3秒钟后自动关闭！");
        $timeout(function(){
            $scope.disabledBtn = false;
            $scope.cancel();
        },3000);
    }, function(error) {
//      console.log(error);
      $scope.disabledBtn = false;
      $scope.addAlert("danger", "保存失败，请稍后再试！");
    });
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };
  //Alert
  $scope.alerts = [];
  $scope.addAlert = function(type, msg) {
    var alert = {
      'type': type,
      'msg': msg
    };
    $scope.alerts.push(alert);
    $timeout(function() {
      $scope.closeAlert($scope.alerts.indexOf(alert));
    }, 3000);
  };

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  $scope.clearAlerts = function() {
    $scope.alerts = [];
  };


};
