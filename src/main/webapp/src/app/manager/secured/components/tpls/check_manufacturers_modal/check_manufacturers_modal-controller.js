'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.check_manufacturers_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.check_manufacturers_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity",
  "db_operation"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.check_manufacturers_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,entity,db_operation) {

  
  $scope.manufacturers=[];
//console.log(entity);


  activate();

  function activate() {
    var promises = [getDeviceTypes(entity.device_type_id)];
    return $q.all(promises).then(function() {
    });
  }

  function getDeviceTypes(did) {
    return db_operation.getDBManufacturers(did).then(function (data) {
      $scope.manufacturers = data.manufactories;

      return $scope.manufacturers;
    });
  }

 

  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
