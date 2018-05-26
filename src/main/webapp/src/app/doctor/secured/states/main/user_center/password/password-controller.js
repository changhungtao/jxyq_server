'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.user_center.password.Ctrl');

jxdctsec.main.user_center.password.Ctrl.$inject = [
  "basic",
  'md5'
];

/**
 * main.user_center.password controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.user_center.password.Ctrl = function($scope, basic, md5) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.user_center.password controller';
  $scope.chPassword = {
    password: undefined,
    confirm_password: undefined,
    new_password: undefined
  }

  $scope.activeSave = false;
  var validParam = function(param) {
    if (param != undefined && param != null && param != "") {
      return true;
    }
    return false;
  }
  $scope.checkUserName = function() {
    var node = document.getElementById("user_name_group");
    var helper = document.getElementById("user_name_helper");
    if (validParam($scope.chPassword.password)) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
      if (validParam($scope.chPassword.new_password) && validParam($scope.chPassword.password) &&
        validParam($scope.chPassword.confirm_password)) {
        $scope.activeSave = true;
      }
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
      $scope.activeSave = false;
    }
  }

  $scope.checkPassword = function() {
    var node = document.getElementById("password_group");
    var helper = document.getElementById("password_helper");
    if (validParam($scope.chPassword.password)) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
      $scope.activeSave = false;
    }
  }

  $scope.checkPasswordRepeat = function() {
    var node = document.getElementById("password_repeat_group");
    var helper = document.getElementById("password_repeat_helper");
    if (validParam($scope.chPassword.confirm_password)) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
      //新密码正确，且旧密码正确
      if (validParam($scope.chPassword.new_password) && $scope.chPassword.confirm_password == $scope.chPassword.new_password) {
        $scope.activeSave = true;
      }
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
      $scope.activeSave = false;
    }

    if ($scope.chPassword.confirm_password != $scope.chPassword.new_password) {
      node.className = "form-group has-error";
      helper.innerHTML = "密码不一致";
      $scope.activeSave = false;
    };
  }

  $scope.saveNewPwd = function() {
    console.log('test');
    basic.change_password({
      old_password: md5.createHash($scope.chPassword.password),
      new_password: md5.createHash($scope.chPassword.new_password)
    }).then(function(res) {
      if (validParam(res.success_message)) {
        $scope.addAlert("success", "修改密码成功！");
        clearData();
      }
      if (validParam(res.error_message)) {
        $scope.addAlert("danger", res.error_message.faultstring);
      }
    }, function(error) {
      $scope.addAlert("danger", "修改密码异常，请稍后再试！");
    })
  }

  var clearData = function() {
    $scope.chPassword.password = null;
    $scope.chPassword.new_password = null;
    $scope.chPassword.confirm_password = null;

    var helper1 = document.getElementById("user_name_helper");
    helper1.innerHTML = "必填";

    var helper2 = document.getElementById("password_helper");
    helper2.innerHTML = "必填";

    var helper3 = document.getElementById("password_repeat_helper");
    helper3.innerHTML = "必填";
  }
};
