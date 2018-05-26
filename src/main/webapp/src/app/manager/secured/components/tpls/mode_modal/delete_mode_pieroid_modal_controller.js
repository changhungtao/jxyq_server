'use strict';
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.delete_mode_pieroid_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.delete_mode_pieroid_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity",
  "twatch"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.delete_mode_pieroid_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, entity, twatch) {


  $scope.pieroid_submit = {
    'start': entity.start,
    'stop': entity.stop,
    'runmode': [{
      "mode_id": "2",
      "mode_name": "GPS模式"
    }],
    'runmode_selected': {
      "mode_id": "2",
      "mode_name": "GPS模式"
    }
  }



  $scope.ok = function() {
    console.log(pieroid_submit);
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
