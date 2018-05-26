'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.check_terminal_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.check_terminal_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "db_operation",
  "entity"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.check_terminal_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,db_operation,entity) {


  // $scope.product_types=[];
  // $scope.product_types_selected={selected:undefined};


  db_operation.getTerminalDetails(entity.terminal_id).then(function(data){
    console.log(data);
    $scope.terminalDetails={
      product_type_id: data.product_type_id,
      device_type_id: data.device_type_id,
      terminal_catagory_id: data.terminal_catagory_id,
      terminal_name: data.terminal_name,
      manufactory_name:data.manufactory_name,
      activated_at:data.activated_at
    }

  });
  

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

 

  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
