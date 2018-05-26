'use strict';
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.add_mode_whitename_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.add_mode_whitename_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "twatch",
  "entity",
  "imei"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.add_mode_whitename_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, twatch, entity, imei) {

  // console.log(entity[0].name);
  // console.log(entity[entity.length-1].name);
  // console.log(imei);

  var getAllData = function() {
    for (var i = entity.length-1; i >= 0; i--) {
      addwhite.whitelist.push(entity[i]);
    }
    addwhite.whitelist.push($scope.white);
  }

  $scope.white = {
    name: undefined,
    number: undefined
  }

  var addwhite = {
    imei: imei,
    whitelist: []
  }

  $scope.addWhite = function() {
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
