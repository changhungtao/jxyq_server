'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.check_devices_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.check_devices_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  "entity",
  '$q',
  "constants",
  "db_operation"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.check_devices_modal.Ctrl = function($scope, $filter,$modalInstance, entity,$q,constants,db_operation) {
 
  $scope.entity=entity;
  $scope.device_types=[];
  $scope.deviceTypes_selects = constants.gotDEVICETYPES();

  activate();

  function activate() {
    var promises = [getDeviceTypes($scope.entity.product_type_id)];
    return $q.all(promises).then(function() {
    });
  }

  function getDeviceTypes(product_id) {
    return db_operation.getDBProductTypeDetails(product_id).then(function (data) {
      $scope.device_types = data.device_type_ids;

      return $scope.device_types;
    });
  }


  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };
};
