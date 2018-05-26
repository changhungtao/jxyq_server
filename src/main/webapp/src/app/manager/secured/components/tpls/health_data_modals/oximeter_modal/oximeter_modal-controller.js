'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.health_data_modals.oximeter_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.health_data_modals.oximeter_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "$timeout",
  "constants",
  "entity",
  "health_data"
];
jxmgrsec.health_data_modals.oximeter_modal.Ctrl = function($scope, $filter,$modalInstance,$q,$timeout,constants,entity,health_data) {

    $scope.entity = entity;
    console.log($scope.entity);
    $scope.patOxi = {
        measured_at:undefined,
        oximeter_value:undefined,
//        symptom:undefined,
        proposal:undefined
    }
    $scope.getPatOxi = function(){
        health_data.getOximeter($scope.entity.measurement_id).then(function(data){
            $scope.patOxi = data;
            $scope.patOxi.measured_at = $filter('unixTodate')(data.measured_at);
            console.log(data);
        },function(error){
            console.log(error);
        });
    }

    $scope.getPatOxi();

    $scope.disabledBtn = false;
    $scope.savePatOxi = function(){
        $scope.disabledBtn = true;
        $scope.patOxi.measured_at = $filter('dateTounix')($filter('date')($scope.patOxi.measured_at,'yyyy-M-dd H:mm:ss'));
        return health_data.saveOximeters($scope.entity.measurement_id,$scope.patOxi).then(function(data){
            $scope.addAlert('success','推送成功，3秒后自动关闭！');
            $timeout(function(){
                $scope.disabledBtn = false;
                $scope.cancel();
            },3000);
            $scope.patOxi.measured_at = $filter('unixTodate')($scope.patOxi.measured_at);
        },function(error){
//            console.log("保存失败！");
//            $scope.cancel();
            $scope.addAlert('danger','推送失败，请稍后再试！');
            $scope.disabledBtn = false;
        });
    }



    $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

    $scope.dt = new Date();
    $scope.minDate = new Date('1900/01/01');

    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.dateOptions = {
//        formatYear: 'yy',
        startingDay: 1
    };

    $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[2];

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 2);
    $scope.events = [{
        date: tomorrow,
        status: 'full'
    }, {
        date: afterTomorrow,
        status: 'partially'
    }];

    $scope.getDayClass = function(date, mode) {
        if (mode === 'day') {
            var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

            for (var i = 0; i < $scope.events.length; i++) {
                var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                if (dayToCheck === currentDay) {
                    return $scope.events[i].status;
                }
            }
        }
        return '';
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


};
