'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.add_manager_modal.add_manager_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxsprsec.add_manager_modal.add_manager_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$filter",
  "$q",
  "$timeout",
  "md5",
  "constants",
  "basic"
];
jxsprsec.add_manager_modal.add_manager_modal.Ctrl = function($scope, $modalInstance,$filter,$q,$timeout,md5,constants,basic) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';

    $scope.new_manager = {
        login_name:undefined,
        password:undefined,
        confirm_password:undefined,//只起校验作用，后台不作处理
        full_name:undefined,
        avatar_url:undefined,
        gender:undefined,
        birthday:undefined,
        phone:undefined,
        email:undefined
    }

    $scope.genders = constants.gotGENDERS();

    $scope.avatarFile = undefined;

    var uploadAdvar = function(){
        if(validParam($scope.avatarFile)){
           return basic.fileUpload($scope.avatarFile).then(function(res){
                $scope.new_manager.avatar_url = res.success_message[0];
            },function(error){
                alert('文件上传失败，请稍后再试！');
            });
        }
    }

    $scope.addManager = function(){
        var promise = [uploadAdvar()];
        return $q.all(promise).then(function(){
            if($scope.new_manager.confirm_password == $scope.new_manager.password){
                $scope.new_manager.password = md5.createHash($scope.new_manager.password);
            }else{
                //$scope.new_manager.password = undefined;
                $scope.addAlert('danger','两次密码不一致！');
                return;
            }
//            console.log($scope.new_manager);
            $scope.new_manager.birthday = $filter('date')( $scope.new_manager.birthday,'yyyy-MM-dd');
            delete $scope.new_manager.confirm_password;
            return basic.addManager($scope.new_manager).then(function(data){
                if(validParam(data.success_message.administrator_id)){
                    $scope.addAlert("success","新建管理员成功！");
                    $timeout(function(){
                        $scope.cancel();
                    },6000);
                }else{
                    $scope.addAlert("danger","新建管理员失败，请稍后再试！");
                }
            },function(error){
                console.log(error);
                $scope.addAlert("danger","新建管理员失败，请稍后再试！");
            });
        });
    }

    $scope.filled = false;

    $scope.isFilled = function(){
        if(validParam($scope.new_manager.password)){
            $scope.filled = true;
        }else{
            $scope.filled = false;
        }

    }

    $scope.confirmSuccess = false;
    $scope.confirmPWD = function(){
        if($scope.new_manager.password == $scope.new_manager.confirm_password){
            $scope.confirmSuccess = true;
        }else{
            $scope.confirmSuccess = false;
        }
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
