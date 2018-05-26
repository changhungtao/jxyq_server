'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxdctsec.evaluation_sphygmomanometer_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxdctsec.evaluation_sphygmomanometer_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  "entity",
  "health_data"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxdctsec.evaluation_sphygmomanometer_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';
  //var ctrl = this;
    //$scope.entity = angular.copy(entity);
    $scope.entity = entity;
    $scope.entity.measured_at = $filter('unixTodate')(entity.measured_at);
    console.log("$scope.entity");
    console.log($scope.entity);

    $scope.submitEvaluation = function(){
        $scope.entity.measured_at = $filter('dateTounix')($scope.entity.measured_at);
        health_data.saveSphygmomanometers($scope.entity.id,$scope.entity).then(function(data){
            console.log(data);
            $scope.cancel();
        },function(error){
            console.log(error);
        })
    };
  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
