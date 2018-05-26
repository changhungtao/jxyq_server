'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxdctsec.evaluation_wristband_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxdctsec.evaluation_wristband_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  "entity",
  "health_data"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxdctsec.evaluation_wristband_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';
  //var ctrl = this;
    $scope.entity = entity;
    $scope.entity.measured_at = $filter('unixTodate')(entity.measured_at);
    console.log("$scope.entity");
    console.log($scope.entity);

    $scope.submitEvaluation = function(){
        $scope.entity.measured_at = $filter('dateTounix')($scope.entity.measured_at);
        health_data.saveWristbands($scope.entity.id,$scope.entity).then(function(data){
            console.log(data);
            $scope.cancel();
        },function(error){
            console.log(error);
        })
    };

//  console.log($scope.entity);

//  $scope.today = function() {
//    $scope.dt = new Date();
//
//  };
//  $scope.dt = entity.measured_at_date;
//
//
//  $scope.clear = function() {
//    $scope.dt = null;
//  };
//
//  // Disable weekend selection
//  $scope.disabled = function(date, mode) {
//    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
//  };
//
//  $scope.toggleMin = function() {
//    $scope.minDate = $scope.minDate ? null : new Date();
//  };
//  $scope.toggleMin();
//
//  $scope.open = function($event) {
//    $event.preventDefault();
//    $event.stopPropagation();
//
//    $scope.opened = true;
//  };
//
//  $scope.dateOptions = {
//    formatYear: 'yy',
//    startingDay: 1
//  };
//
//  $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
//  $scope.format = $scope.formats[0];
//
//  var tomorrow = new Date();
//  tomorrow.setDate(tomorrow.getDate() + 1);
//  var afterTomorrow = new Date();
//  afterTomorrow.setDate(tomorrow.getDate() + 2);
//  $scope.events = [{
//    date: tomorrow,
//    status: 'full'
//  }, {
//    date: afterTomorrow,
//    status: 'partially'
//  }];
//
//  $scope.getDayClass = function(date, mode) {
//    if (mode === 'day') {
//      var dayToCheck = new Date(date).setHours(0, 0, 0, 0);
//
//      for (var i = 0; i < $scope.events.length; i++) {
//        var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);
//
//        if (dayToCheck === currentDay) {
//          return $scope.events[i].status;
//        }
//      }
//    }
//
//    return '';
//  };
  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
