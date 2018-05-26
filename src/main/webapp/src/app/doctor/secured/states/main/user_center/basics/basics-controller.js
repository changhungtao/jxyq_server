'use strict';

goog.require('jxdctsec.main.user_center.Ctrl');

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.user_center.basics.Ctrl');

jxdctsec.main.user_center.basics.Ctrl.$inject = [
  "$controller",
  "$scope",
  "$timeout",
  "$filter",
  "$q",
  "basic",
  "constants"
];

/**
 * main.user_center.basics controller.
 *
 * @param {angular.$controller} $controller
 * @constructor
 * @export
 * @ngInject
 */
jxdctsec.main.user_center.basics.Ctrl = function(
  $controller,
  $scope,
  $timeout,
  $filter,
  $q,
  basic,
  constants
) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.user_center.basics controller';

  /**
   * Inherit from parent controller. We'd like to call the `log`
   * function here.
   *
   * @type {Object}
   * @nocollapse
   */
  this.parent = $controller(jxdctsec.main.user_center.Ctrl);

  /**
   * Call parent `log` function.
   */
  //this.parent.log('`log` function called from child controller');

  $scope.dt = new Date();

  $scope.departments = constants.gotDEPARTMENTS();
  $scope.department = {
    selected: undefined
  };

  $scope.genders = constants.gotGENDERS();

  $scope.distrits = constants.gotDISTRICTS();
  $scope.distrit = {
    selected: undefined
  };
  //完整功能未测试
  $scope.doctor = {
    full_name: undefined,
    email: undefined,
    phone: undefined,
    gender: undefined,
    birthday: undefined,
    identification_number: undefined,
    department_id: undefined,
    district_id: undefined,
    profile: undefined,
    physician_certificate: '',
    practicing_certificate: ''
  }
  $scope.getDoctorBasics = function() {

      basic.getDoctorBasics().then(function(data) {
        //具体还得看数据返回格式微调一下
        $scope.doctor = data.success_message;
        $scope.doctor.last_login =$filter('unixTodate')(data.success_message.last_login);
      }, function(error) {
        $scope.addAlert("danger", "数据加载异常，请稍后再试！");
      });
    }
    //加载医生基本数据
  $scope.getDoctorBasics();

  //保存用户修改基本资料
  $scope.saveDoctorBasic = function() {
    //console.log($scope.doctor);
      var promise = [upLoadPhysicianCertificate(),upLoadPracticingCertificate()];
      return $q.all(promise).then(function(){
          $scope.doctor.birthday = $filter('date')($scope.doctor.birthday, 'yyyy-MM-dd');
          basic.putDoctorBasic($scope.doctor).then(function(succ) {
              $scope.addAlert("success", "保存成功！");
          }, function(error) {
              console.log(error);
              $scope.addAlert("danger", "保存失败,请稍后再试！");
          })
      });
  }

  $scope.physician_certificate = undefined;
  $scope.practicing_certificate = undefined;

    function upLoadPhysicianCertificate(){
      if($scope.validParam($scope.physician_certificate)){
          return basic.fileUpload($scope.physician_certificate).then(function(data){
              $scope.doctor.physician_certificate = data.success_message[0];
              console.log('上传physician_certificate文件成功！');
          },function(error){
              console.log('上传physician_certificate文件失败！');
          });
      }else{
          console.log('文件没发生更改，或文件为空');
      }
  }

    function upLoadPracticingCertificate(){
        if($scope.validParam($scope.practicing_certificate)){
            return basic.fileUpload($scope.practicing_certificate).then(function(data){
                $scope.doctor.practicing_certificate = data.success_message[0];
                console.log('上传practicing_certificate文件成功！');
            },function(error){
                console.log('上传practicing_certificate文件失败！');
            });
        }else{
            console.log('文件没发生更改，或文件为空');
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
//    formatYear: 'yy',
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
};
