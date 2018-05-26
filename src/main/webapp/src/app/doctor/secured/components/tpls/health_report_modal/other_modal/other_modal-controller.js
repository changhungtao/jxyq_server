'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.health_report.other_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.health_report.other_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "entity",
  "db_operation",
  "$filter"
];
jxdctsec.health_report.other_modal.Ctrl = function($scope, $filter,$modalInstance,$timeout,entity,db_operation) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';

  $scope.entity = entity;
//  console.log($scope.entity);
  $scope.patOther = {
      measured_at:undefined,
      symptom:undefined,
      proposal:undefined
  }
    $scope.getPatOther = function(){
        db_operation.getPatOther($scope.entity.patient_id,$scope.entity.file_id).then(function(data){
            $scope.patOther = data;
            $scope.patOther.measured_at = $filter('unixTodate')(data.measured_at);
//            console.log(data);
        },function(error){
            console.log(error);
        });
    }

    $scope.getPatOther();

    var timer = undefined;

    $scope.disabledBtn = false;
    $scope.savePatOther = function(){
        $scope.disabledBtn = true;
        $scope.patOther.measured_at = $filter('dateTounix')($filter('date')($scope.patOther.measured_at,'yyyy-M-dd H:mm:ss'));
        db_operation.putPatOther($scope.entity.patient_id,$scope.entity.file_id,$scope.patOther).then(function(data){
//            console.log("保存成功！");
//            $scope.cancel();
            $scope.patOther.measured_at = $filter('unixTodate')($scope.patOther.measured_at);
            $scope.addAlert('success','推送成功，3秒后自动关闭！');
            timer = $timeout(function(){
                $scope.disabledBtn = false;
                $scope.cancel();
            },3000);
        },function(error){
//            console.log("保存失败！");
//            $scope.cancel();
            $scope.addAlert('danger','推送失败，请稍后再试！');
            $scope.disabledBtn = false;
        });
    }

    $scope.$on("$destroy", function() {
        console.log('modal destroy!');
        if (timer) {
            $timeout.cancel(timer);
        }
    });

  $scope.today = function() {
    $scope.dt = new Date();

  };
  $scope.dt = entity.measured_at_date;


  $scope.clear = function() {
    $scope.dt = null;
  };

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
  };

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
//    formatYear: 'yy',
    startingDay: 1
  };

  $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  $scope.format = $scope.formats[0];

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



  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
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
