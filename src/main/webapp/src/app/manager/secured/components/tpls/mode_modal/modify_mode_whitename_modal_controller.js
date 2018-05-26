'use strict';
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.modify_mode_whitename_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.modify_mode_whitename_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity",
  "twatch",
  "griddata",
  "imei"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.modify_mode_whitename_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, entity, twatch, imei, griddata) {

  console.log(entity);
  console.log(griddata);
  console.log(imei);

  var getAllData = function() {
    for (var i = griddata.length-1; i >= 0; i--) {
      if(entity.$$hashKey == griddata[i].$$hashKey){
        addwhite.whitelist.push($scope.white);
      }else{
        addwhite.whitelist.push(griddata[i]);
      }   
    }
  }

  $scope.white = {
    name: entity.name,
    number: entity.number
  }

  var addwhite = {
    imei: imei,
    whitelist: []
  }



  $scope.modifyWhite = function() {
    getAllData();
    console.log(addwhite);
     twatch.addWhiteList(addwhite).then(function(res) {
      console.log(res);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //$modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
