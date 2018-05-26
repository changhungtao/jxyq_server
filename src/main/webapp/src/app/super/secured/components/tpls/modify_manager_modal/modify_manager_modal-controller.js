'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.modify_manager_modal.modify_manager_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxsprsec.modify_manager_modal.modify_manager_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$filter",
  "$q",
  "$timeout",
  "md5",
  "entity",
  "constants",
  "basic"
];
jxsprsec.modify_manager_modal.modify_manager_modal.Ctrl = function($scope, $modalInstance,$filter,$q,$timeout,md5,entity,constants,basic) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';

  $scope.entity = entity;
//  console.log(entity);

  $scope.genders = constants.gotGENDERS();

    $scope.avatarFile = undefined;
    $scope.new_password = undefined;

    var uploadAdvar = function(){
        if(validParam($scope.avatarFile)){
            return basic.fileUpload($scope.avatarFile).then(function(res){
                $scope.entity.avatar_url = res.success_message[0];
            },function(error){
                alert('文件上传失败，请稍后再试！');
            });
        }
    }

    $scope.updateManager = function(){
        var promise = [uploadAdvar()];
        return $q.all(promise).then(function(){
            if(validParam($scope.new_password)){
                $scope.entity.password = md5.createHash($scope.new_password);
            }else{
                $scope.entity.password = undefined;
            }
            $scope.entity.birthday = $filter('date')( $scope.entity.birthday,'yyyy-MM-dd');
//            console.log($scope.entity);
            //注意这里必须return
           return basic.updateNormalManagers($scope.entity.administrator_id,$scope.entity).then(function(data){
//                console.log('administrator_id');
//                console.log(data);
                if(validParam(data.success_message)){
                    $scope.addAlert("success","修改成功！");
                    $timeout(function(){
                        $scope.cancel();
                    },6000)
                }else{
                    $scope.addAlert("danger","修改失败，请稍后再试！");
                }
            },function(error){
                    console.log(error);
                    $scope.addAlert("danger","修改失败，请稍后再试！");
                });
        });
    }

    $scope.maxDate = new Date();
    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.opened = false;;
    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };

    $scope.dateOptions = {
//        formatYear: 'yy',
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

       function validParam(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

  $scope.cancel = function() {
     $modalInstance.dismiss('cancel');
  };

};
