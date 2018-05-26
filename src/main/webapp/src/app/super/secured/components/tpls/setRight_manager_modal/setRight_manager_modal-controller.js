'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.setRight_manager_modal.setRight_manager_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxsprsec.setRight_manager_modal.setRight_manager_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "entity",
  "constants",
  "basic"
];
jxsprsec.setRight_manager_modal.setRight_manager_modal.Ctrl = function($scope, $modalInstance,$timeout,entity,constants,basic) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';
  $scope.district_list = constants.gotDISTRICTS();
  $scope.province_list = constants.gotPROVINCES();
  $scope.city_list = constants.gotCITIES();
  $scope.zone_list = constants.gotZONES();
  $scope.deviceTypes = constants.gotDEVICETYPES();

    $scope.manPermission = {};
    var getManagerPermissions = function(mid){
       return basic.getManagerPermissions(mid).then(function(data){
            $scope.manPermission = data.success_message;

            console.log(data);

           //初始化绑定下拉框
           $scope.changeDistrict($scope.manPermission.district.district_id,1);
           $scope.changeProvince($scope.manPermission.district.province_id,1);
           $scope.changeCity($scope.manPermission.district.city_id,1);
        });
    }

    //获取管理员权限
    getManagerPermissions(entity.administrator_id);

    $scope.changeDistrict = function(district_id,first){
        if(validParam($scope.province_list)){
            $scope.province_select_list = [];
            if(first != 1){//第一次要根据值绑定下拉框，所以不能置为undefined
                $scope.manPermission.district.province_id = undefined;
                $scope.manPermission.district.city_id = undefined;
                $scope.manPermission.district.zone_id = undefined;

                $scope.province_select_list = [];
                $scope.city_select_list = [];
                $scope.zone_select_list = [];
            }
            for(var i = 0;i <  $scope.province_list.length;i++){
                if($scope.province_list[i].district_id == district_id){
                    $scope.province_select_list.push($scope.province_list[i]);
                }
            }
        }
    }

    $scope.changeProvince = function(province_id,first){
        if(validParam($scope.city_list)){
//            console.log($scope.city_list);
            $scope.city_select_list = [];
            if(first != 1) {//第一次要根据值绑定下拉框，所以不能置为undefined
                $scope.manPermission.district.city_id = undefined;
                $scope.manPermission.district.zone_id = undefined;

                $scope.city_select_list = [];
                $scope.zone_select_list = [];
            }
            for(var i = 0;i <  $scope.city_list.length;i++){
                if($scope.city_list[i].province_id == province_id){
                    $scope.city_select_list.push($scope.city_list[i]);
                }
            }
        }
    }

    $scope.changeCity = function(city_id,first){
        if(validParam($scope.zone_list)){
           // console.log($scope.zone_list);
            $scope.zone_select_list = [];
            if(first != 1) {//第一次要根据值绑定下拉框，所以不能置为undefined
                $scope.manPermission.district.zone_id = undefined;

                $scope.zone_select_list = [];
            }
            for(var i = 0;i <  $scope.zone_list.length;i++){
                if($scope.zone_list[i].city_id == city_id){
                    $scope.zone_select_list.push($scope.zone_list[i]);
                }
            }
        }
    }

    $scope.editManagerPermission = function(){
        if(validParam($scope.manPermission)){
            basic.editManagerPermissions(entity.administrator_id,$scope.manPermission).then(function(data){
                if(validParam(data.success_message)){
                    $scope.addAlert('success','编辑管理员权限成功！');
                    $timeout(function(){
                        $scope.cancel();
                    },6000);
                }else{
                    $scope.addAlert('danger','编辑管理员权限失败，请稍后再试！');
                }
            },function(error){
                $scope.addAlert('danger','编辑管理员权限出错，请稍后再试！');
            });
        }
    }


  // $scope.entity = entity;

  // $scope.today = function() {
  //   $scope.dt = new Date();

  // };
  // $scope.dt = entity.measured_at_date;


  // $scope.clear = function() {
  //   $scope.dt = null;
  // };

  // // Disable weekend selection
  // $scope.disabled = function(date, mode) {
  //   return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
  // };

  // $scope.toggleMin = function() {
  //   $scope.minDate = $scope.minDate ? null : new Date();
  // };
  // $scope.toggleMin();

  // $scope.open = function($event) {
  //   $event.preventDefault();
  //   $event.stopPropagation();

  //   $scope.opened = true;
  // };

  // $scope.dateOptions = {
  //   formatYear: 'yy',
  //   startingDay: 1
  // };

  // $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  // $scope.format = $scope.formats[0];

  // var tomorrow = new Date();
  // tomorrow.setDate(tomorrow.getDate() + 1);
  // var afterTomorrow = new Date();
  // afterTomorrow.setDate(tomorrow.getDate() + 2);
  // $scope.events = [{
  //   date: tomorrow,
  //   status: 'full'
  // }, {
  //   date: afterTomorrow,
  //   status: 'partially'
  // }];

  // $scope.getDayClass = function(date, mode) {
  //   if (mode === 'day') {
  //     var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

  //     for (var i = 0; i < $scope.events.length; i++) {
  //       var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

  //       if (dayToCheck === currentDay) {
  //         return $scope.events[i].status;
  //       }
  //     }
  //   }

  //   return '';
  // };
  // $scope.ok = function() {
  //   $modalInstance.close();
  // };

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


    $scope.cancel = function() {
     $modalInstance.dismiss('cancel');
  };

    function validParam(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

};
