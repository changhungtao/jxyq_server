'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.modify_device_type_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.modify_device_type_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity",
  "db_operation"
];
jxmgrsec.modify_device_type_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,entity,$timeout,db_operation) {

  
  $scope.product_types=[];
  $scope.product_types_selected={selected:undefined};

    console.log('device entity');
    console.log(entity);
  $scope.device_submit={
    "device_name":$filter('mapDeviceType')(entity.device_type_id),
    "product_type_id":entity.product_type_id
  }

  
  activate();

  function activate() {
    var promises = [getProductTypes()];
    return $q.all(promises).then(function() {
    });
  }

  function getProductTypes() {
    return constants.getProductTypes().then(function (data) {
      $scope.product_types = data.product_types;

      return $scope.product_types;
    });
  }

 

  $scope.ok = function() {
   
    $scope.jsonData={
      name:$scope.device_submit.device_name,
      product_type_id:$scope.device_submit.product_type_id
    }
    db_operation.putEditDeviceTypes(entity.device_type_id,$scope.jsonData).then(function(succ){
        $scope.addAlert("success", "修改成功");  
    },function(error){
      console.log(error);
      $scope.addAlert("danger", "修改失败，请稍后再试！");
    });
    //$modalInstance.close();
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
