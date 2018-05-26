'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.edit_patient_modal.edit_patient_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.edit_patient_modal.edit_patient_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "entity",
  "$filter",
  "health_file",
  "constants"
];
jxdctsec.edit_patient_modal.edit_patient_modal.Ctrl = function($scope,$modalInstance,$timeout,health_file,constants,$filter,entity) {

  var ctrl=this;

    $scope.patient = entity;

    function getGenders(){
        return constants.getGenders().then(function(data){
            $scope.genders =  data.genders;
            $scope.gender = {selected: undefined};
            return $scope.genders;
        });
    }
    getGenders();


    $scope.updatePatient = function(){
        if(!validParam($scope.patient.birthday)){
            $scope.addAlert("warning","出生年月字段必填！");
            return;
        }
        $scope.patient.birthday = $filter('date')($scope.patient.birthday,'yyyy-MM-dd');
        return health_file.updatePatient($scope.patient.patient_id,$scope.patient).then(function(data){
//            console.log(data);
            $scope.addAlert('success','修改成功！');
//            $scope.cancel();
        },function(error){
            console.log(error);
            $scope.addAlert('danger','修改出错，请稍后再试！');
        });
    }
  $scope.dt = new Date();

  $scope.minDate = new Date('1949/01/01');

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

    $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

    var validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }


};
