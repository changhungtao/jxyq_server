'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.evaluation_fat_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.evaluation_fat_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  "entity",
  "health_data"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxdctsec.evaluation_fat_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {

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
        health_data.saveFats($scope.entity.id,$scope.entity).then(function(data){
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
