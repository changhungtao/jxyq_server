'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.health_report.wristband_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.health_report.wristband_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "entity",
  "db_operation",
  "$filter"
];
jxdctsec.health_report.wristband_modal.Ctrl = function($scope,$filter,$timeout ,$modalInstance, entity,db_operation) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';

  $scope.entity = entity;


  $scope.patWrist = {
      step_count:undefined,
      distance:undefined,
      calories:undefined,
      walk_count:undefined,
      walk_distance:undefined,
      walk_calories:undefined,
      run_count:undefined,
      run_distance:undefined,
      run_calories:undefined,
      deep_duration:undefined,
      shallow_duration:undefined,
      heart_rate:undefined,
      symptom:undefined,
      proposal:undefined,
      measured_at:undefined
  }

  $scope.getPatWrist = function(){
      db_operation.getPatWrist($scope.entity.patient_id,$scope.entity.file_id).then(function(data){
          console.log(data);
          $scope.patWrist = data;
          $scope.patWrist.measured_at = $filter('unixTodate')(data.measured_at);
      },function(error){
          console.log(data);
      });
  }

  $scope.getPatWrist();
  var timer = undefined;

  $scope.disabledBtn = false;
  $scope.savePatWrist = function(){
      $scope.disabledBtn = true;
      //還原為unix时间
      $scope.patWrist.measured_at = $filter('dateTounix')($filter('date')($scope.patWrist.measured_at,'yyyy-M-dd H:mm:ss'));
      db_operation.putPatWrist($scope.entity.patient_id,$scope.entity.file_id,$scope.patWrist).then(function(data){
//          console.log("保存成功！");
//          console.log(data);
          $scope.patWrist.measured_at = $filter('unixTodate')($scope.patWrist.measured_at);
          $scope.addAlert('success','推送成功，3秒后自动关闭！');
          timer = $timeout(function(){
              $scope.disabledBtn = false;
              $scope.cancel();
          },3000);
      },function(error){
//          console.log("保存失败！");
//          console.log(error);
          $scope.addAlert('danger','推送失败，请稍后再试！');
          $scope.disabledBtn = false;
      })
  }

    $scope.$on("$destroy", function() {
        console.log('modal destroy!');
        if (timer) {
            $timeout.cancel(timer);
        }
    });

  $scope.reset = function(){
      $scope.patWrist = {};
  }

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
