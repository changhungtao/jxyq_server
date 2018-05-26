'use strict';
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.modify_voice_pieroid_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.modify_voice_pieroid_modal.Ctrl.$inject = [
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
jxmgrsec.modify_voice_pieroid_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, entity, twatch, imei) {

  console.log(entity);
  console.log(imei);

  $scope.alarm_events = constants.gotALARMEVENTS();
  $scope.week_days = constants.gotWEEKDAYS();
  $scope.weekdays = {
    week: constants.weekDayHelperInt2Array(entity.weeks)
  }

  $scope.period_states = constants.gotPERIODSTATES();

  $scope.modifyplayvoice = {
    imei: imei,
    eventid: entity.eventid,
    //eventname: $filter('mapTwatchAlarmEventContent')(entity.eventname),
    eventname: entity.eventname,
    start: entity.start,
    msg: entity.msg,
    times: entity.times,
    interval: entity.interval,
    weeks: undefined,
    playvoice_en: entity.playvoice_en
  }

  $scope.modifyVoicePeriod = function() {
    $scope.modifyplayvoice.weeks = constants.weekDayHelperArray2Int($scope.weekdays.week);
    console.log($scope.modifyplayvoice);
    twatch.putPlayVoices($scope.modifyplayvoice).then(function(res) {
      console.log(res);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

  $scope.defaulttime = [{
    "time_id": "1",
    "time_name": "1分钟"
  }, {
    "time_id": "2",
    "time_name": "2分钟"
  }, {
    "time_id": "5",
    "time_name": "5分钟"
  }, {
    "time_id": "10",
    "time_name": "10分钟"
  }];

  $scope.defaultinterval = [{
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
