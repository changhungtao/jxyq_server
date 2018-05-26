'use strict';
// ----------------------pieroid---------------------
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.add_mode_pieroid_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.add_mode_pieroid_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "twatch",
  "entity"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.add_mode_pieroid_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, twatch, entity) {

  console.log(entity);

  $scope.week_days = constants.gotWEEKDAYS();

  $scope.weekdays = {
    week: []
  }

  $scope.time = {
    runmode: 2,
    start: undefined,
    stop: undefined,
    weeks: undefined  
  }

  $scope.addModePeriod = {
    imei: entity,
    mode_periods: [
    $scope.time
    ]
  }



  $scope.addPeriod = function() {
    $scope.time.weeks = constants.weekDayHelperArray2Int($scope.weekdays.week);
    twatch.addModePeriods($scope.addModePeriod).then(function(res) {
      console.log(res);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //$modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
