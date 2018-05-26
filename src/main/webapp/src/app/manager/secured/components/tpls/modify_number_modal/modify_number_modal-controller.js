'use strict';
/**
 * Create namespace.
 */

goog.provide('jxmgrsec.modify_number_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.modify_number_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity",
  "twatch"  
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.modify_number_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, entity, twatch) {

  //console.log(entity);
  
  $scope.qing_names = constants.gotQINGNAMES();
  //console.log($scope.qing_names); 

  $scope.currentNumber = {
    name: entity.name,
    number: entity.number
  }
  
  $scope.myPlaceholder = "选择亲情号码称呼";
  if($scope.currentNumber.name == "SOS"){
    $scope.currentNumber.name = undefined;
    $scope.name_disabled = true;
    $scope.myPlaceholder = "SOS";
  }
  $scope.modifynumber = function() {
    if($scope.currentNumber.name == undefined){      
      entity.number = $scope.currentNumber.number;
    }else{
      entity.name = $scope.currentNumber.name;
      entity.number = $scope.currentNumber.number;
    }
    
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
