'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.health_data_modals.wristband_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.health_data_modals.wristband_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "$timeout",
  "constants",
  "entity",
  "health_data"
];
jxmgrsec.health_data_modals.wristband_modal.Ctrl = function($scope, $filter,$modalInstance,$q,$timeout,constants,entity,health_data) {

    $scope.entity = entity;
//    console.log(entity);

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
//        proposal:undefined,
        measured_at:undefined
    }

    $scope.getPatWrist = function(){
        return health_data.getWristbands($scope.entity.measurement_id).then(function(data){
//            console.log(data);
            if(!isNaN(data.distance)){
                data.distance /=10;
            }
            if(!isNaN(data.walk_distance)){
                data.walk_distance /=10;
            }
            $scope.patWrist = data;
            $scope.patWrist.measured_at = $filter('unixTodate')(data.measured_at);
        },function(error){
            console.log(error);
        });
    }

    $scope.getPatWrist();

    $scope.disabledBtn = false;
    $scope.savePatWrist = function(){
        if(!isNaN($scope.patWrist.distance)){
            $scope.patWrist.distance *=10;
        }
        if(!isNaN($scope.patWrist.walk_distance)){
            $scope.patWrist.walk_distance *=10;
        }
        //還原為unix时间
        $scope.patWrist.measured_at = $filter('dateTounix')($filter('date')($scope.patWrist.measured_at,'yyyy-M-dd H:mm:ss'));
        return health_data.saveWristbands($scope.entity.measurement_id,$scope.patWrist).then(function(data){
            $scope.disabledBtn = true;
            $scope.addAlert('success','推送成功，3秒后自动关闭！');
            $timeout(function(){
                $scope.cancel();
            },3000);
            $scope.patWrist.measured_at = $filter('unixTodate')($scope.patWrist.measured_at);
            if(!isNaN($scope.patWrist.distance)){
                $scope.patWrist.distance /=10;
            }
            if(!isNaN($scope.patWrist.walk_distance)){
                $scope.patWrist.walk_distance /=10;
            }
        },function(error){
//            console.log("保存失败！");
//            console.log(error);
            $scope.addAlert('danger','推送失败，请稍后再试！');
            $scope.disabledBtn = false;
        })
    }


  $scope.entity=entity;
  $scope.product_types=[];
  $scope.product_types_selected={selected:undefined};

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
