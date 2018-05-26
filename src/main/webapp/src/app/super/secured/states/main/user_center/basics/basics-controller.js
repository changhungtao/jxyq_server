'use strict';

goog.require('jxsprsec.main.user_center.Ctrl');

/**
 * Create namespace.
 */
goog.provide('jxsprsec.main.user_center.basics.Ctrl');

jxsprsec.main.user_center.basics.Ctrl.$inject = [
  "$controller", 
  "$scope", 
  "$timeout",
  "$filter",
  "$q",
  "basic",
  "constants"
];


/**
 * user_center.basics controller.
 *
 * @param {angular.$controller} $controller
 * @constructor
 * @export
 * @ngInject
 */
jxsprsec.main.user_center.basics.Ctrl = function(
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
  this.label = 'some other label from user_center.basics controller';

  /**
   * Inherit from parent controller. We'd like to call the `log`
   * function here.
   *
   * @type {Object}
   * @nocollapse
   */
  this.parent = $controller(jxsprsec.main.user_center.Ctrl);

  /**
   * Call parent `log` function.
   */
  this.parent.log('`log` function called from child controller');

  $scope.dt = new Date();

  $scope.genders = constants.gotGENDERS();

  $scope.admin = {
    full_name: undefined,
    email: undefined,
    gender: undefined,
    birthday: undefined
  }

  $scope.getAdminBasics = function() {
      basic.getAdminBasics().then(function(data) {
        //具体还得看数据返回格式微调一下
        $scope.admin = data.success_message;
        $scope.admin.last_login = $filter('unixTodate')($scope.admin.last_login);
      }, function(error) {
        $scope.addAlert("danger", "数据加载异常，请稍后再试！");
      });
    }
    //加载管理员基本数据
  $scope.getAdminBasics();


  //保存用户修改基本资料
  $scope.saveAdminBasic = function() {
    //console.log($scope.doctor);
    basic.putAdminBasic($scope.admin).then(function(succ) {
      $scope.addAlert("success", "保存成功！");
    }, function(error) {
      console.log(error);
      $scope.addAlert("danger", "保存失败,请稍后再试！");
    })
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
