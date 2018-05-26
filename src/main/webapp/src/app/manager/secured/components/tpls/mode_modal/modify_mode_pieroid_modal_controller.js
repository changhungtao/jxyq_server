'use strict';
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.modify_mode_pieroid_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.modify_mode_pieroid_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity",
  "twatch",
  "imei"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.modify_mode_pieroid_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, entity, twatch, imei) {
  console.log(entity);
  console.log(imei);

  $scope.week_days = constants.gotWEEKDAYS();

  $scope.weekdays = {
    week: constants.weekDayHelperInt2Array(entity.weeks)
  }

  $scope.period_submit = {
    period_id: entity.period_id,
    start: entity.start,
    stop: entity.stop,
    runmode: 2,
    weeks: undefined  
  }

  var modifyperiod = {
    imei: imei,
    period: $scope.period_submit
  }

  $scope.modifyPeriod = function() {
    $scope.period_submit.weeks = constants.weekDayHelperArray2Int($scope.weekdays.week);
    console.log(modifyperiod);
    twatch.modifyModePeriods(modifyperiod).then(function(res) {
      console.log(res);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};