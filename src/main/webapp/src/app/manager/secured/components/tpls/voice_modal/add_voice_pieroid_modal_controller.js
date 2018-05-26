'use strict';
// ----------------------pieroid---------------------
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.add_voice_pieroid_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.add_voice_pieroid_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "twatch",
  "entity"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.add_voice_pieroid_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, twatch, entity) {

  console.log(entity);

  $scope.alarm_events = constants.gotALARMEVENTS();
  $scope.week_days = constants.gotWEEKDAYS();

  $scope.weekdays = {
    week: []
  }

  $scope.period_states = constants.gotPERIODSTATES();
  console.log($scope.week_days);

  $scope.playvoice = {
    imei: entity,
    eventname: undefined,
    start: undefined,
    msg: undefined,
    times: 1,
    interval: 1,
    weeks: undefined,
    playvoice_en: 1  
  }

  $scope.addVoicePeriod = function() {

    $scope.playvoice.weeks = constants.weekDayHelperArray2Int($scope.weekdays.week);
    console.log($scope.playvoice);
    
    twatch.addPlayVoices($scope.playvoice).then(function(res) {
      console.log(res);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });

  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

  $scope.defaulttime= [{
    "time_id": "1",
    "time_name": "1"
  }, {
    "time_id": "2",
    "time_name": "2"
  }, {
    "time_id": "5",
    "time_name": "5"
  }, {
    "time_id": "10",
    "time_name": "10"
  }];

  $scope.defaultinterval= [{
    "interval_id": "1",
    "interval_name": "1"
  }, {
    "interval_id": "2",
    "interval_name": "2"
  }, {
    "interval_id": "3",
    "interval_name": "3"
  }, {
    "interval_id": "4",
    "interval_name": "4"
  }, {
    "interval_id": "5",
    "interval_name": "5"
  }];


};
