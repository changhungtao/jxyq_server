'use strict';
/**
 * Create namespace.
 */
goog.provide('jxmgrsec.modify_route_period_modal.Ctrl');
/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.modify_route_period_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "twatch",
  "entity",
  "imei",
  "$timeout"
];
jxmgrsec.modify_route_period_modal.Ctrl = function($scope, $timeout,$filter, $modalInstance, $q, constants, twatch, entity,imei) {


  console.log(entity);

  $scope.time = {
    imei: imei, 
    set_en: entity.set_en,
    start: entity.start,
    stop: entity.stop,
    weeks: constants.weekDayHelperInt2Array(entity.weeks),
    route_period_id:entity.route_period_id
  }
  $scope.weeks = constants.gotWEEKDAYS();
  

  // $scope.unixToweek = function(weeks){
  //   var i=0;
  //   while(weeks!=0){
  //     if(weeks%2==1){
  //       $scope.weeks[i]=true;        
  //     }
  //     weeks=weeks>>1;
  //     i++;
  //   }
  // }
  // $scope.unixToweek($scope.time.weeks);

  // $scope.weekTounix = function(weeks){
  //   var res = 0;
  //   for (var i = 0;i < 7; i++) {
  //     if(weeks[i]){
  //       res = res+Math.pow(2,i);
  //     }
  //   }
  //   return res;
  // }
  

  $scope.addPeriod = function() {
    $scope.time.weeks = constants.weekDayHelperArray2Int($scope.time.weeks);
    twatch.putRoutePeriod($scope.time).then(function(succ){
      $scope.addAlert("success", "修改成功"); 
    },function(error){
      console.log(error);
      $scope.addAlert("danger", "修改失败，请稍后再试！");
    });
    
    
    console.log($scope.time);
     
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
