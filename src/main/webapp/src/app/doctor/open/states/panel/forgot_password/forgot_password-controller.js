'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctopn.panel.forgot_password.Ctrl');

jxdctopn.panel.forgot_password.Ctrl.$inject = [
  "$window",
  "basic",
  "$timeout",
  'md5'
];

jxdctopn.panel.forgot_password.Ctrl = function($window, basic, $timeout, md5) {
  var ctrl = this;

  ctrl.alert_msg = undefined;
  ctrl.alert_type = undefined;
  ctrl.forgotPasswordInf = {
    login_name: '',
    certification_code: '',
    password: ''
  };
//  ctrl.getVerCodeInf = {
//    phone: '',
//    used_for: '1'
//  };
    ctrl.getVerCodeInf = {
        user_name : '',
        user_role:'doctor',
        used_for: 1
    }
  ctrl.password = undefined;
  ctrl.confirm_password = undefined;

//  ctrl.verificationCode = function(){
//    ctrl.setVerBtnDisabled('verCodeBtn', true);
//    if (ctrl.getVerCodeInf.phone == '' || ctrl.getVerCodeInf.phone == null) {return;};
//      basic.verificationCode(ctrl.getVerCodeInf)
//      .then(function(res){
//        $timeout(function(){
//          ctrl.setVerBtnDisabled('verCodeBtn', false);
//        }, 1000 * 60);
//        ctrl.addAlert('info', '验证码发送成功，请查收。');
//      }, function(error){
//        ctrl.addAlert('danger', error.error_message.faultstring);
//        ctrl.setVerBtnDisabled('verCodeBtn', false);
//      });
//  };

    ctrl.verificationCode = function(){
        ctrl.setVerBtnDisabled('verCodeBtn', true);
        ctrl.getVerCodeInf.user_name = ctrl.forgotPasswordInf.login_name;
//        console.log(ctrl.getVerCodeInf);
        if(!validParam(ctrl.getVerCodeInf.user_name)){
            return;
        }
        return basic.verificationCode(ctrl.getVerCodeInf).then(function(res){
            console.log(res);
            if(validParam(res.error_message) && res.error_message.faultcode == 1001004){
                ctrl.addAlert('danger', res.error_message.faultstring);
                ctrl.setVerBtnDisabled('verCodeBtn', false);
                return;
            }else if(validParam(res.success_message)){
                $timeout(function(){
                    ctrl.setVerBtnDisabled('verCodeBtn', false);
                }, 1000 * 60);
                ctrl.addAlert('info', '验证码发送成功，请查收。');
            }
        }, function(error){
            ctrl.addAlert('danger', error.error_message.faultstring);
            ctrl.setVerBtnDisabled('verCodeBtn', false);
        });
    };

  ctrl.setVerBtnDisabled = function(name, status){
    var btn = document.getElementById(name);
    btn.disabled = status;
  }

  ctrl.forgotPassword = function(){
    if (ctrl.forgotPasswordInf.login_name == '' || ctrl.forgotPasswordInf.login_name == null) {return;};
    if (ctrl.forgotPasswordInf.certification_code == '' || ctrl.forgotPasswordInf.certification_code == null) {return;};
//    if (ctrl.getVerCodeInf.phone == '' || ctrl.getVerCodeInf.phone == null) {return;};
    if (ctrl.password == '' || ctrl.password == null) {return;};
    if (ctrl.confirm_password == '' || ctrl.confirm_password == null) {return;};
    if (ctrl.password != ctrl.confirm_password) {
      ctrl.addAlert('warning', '密码不一致');
      return;
    };
    
    ctrl.setVerBtnDisabled('forgotBtn', true);
    ctrl.forgotPasswordInf.password = md5.createHash(ctrl.password);
    basic.resetPassword(ctrl.forgotPasswordInf)
    .then(function(res){
      if (res.success_message == undefined) {
        if (res.error_message.faultcode == 1001002) {
          ctrl.addAlert('warning', '验证码错误');
        }else{
          ctrl.addAlert('danger', res.error_message.faultstring);
        };
        ctrl.setVerBtnDisabled('forgotBtn', false);
        return;
      };
      ctrl.addAlert('success', '密码重置成功。');
    }, function(error){
      ctrl.addAlert('danger', error.error_message.faultstring);
      ctrl.setVerBtnDisabled('forgotBtn', false);
    })
  };

  ctrl.addAlert = function (type, msg) {
    ctrl.alert_type = type;
    ctrl.alert_msg = msg;
    $timeout(function(){ctrl.closeAlert();}, 3000);
  };

  ctrl.closeAlert = function() {
    ctrl.alert_type = undefined;
    ctrl.alert_msg = undefined;
  };

  ctrl.checkPassword=function(){
    var node= document.getElementById("password_group");
    var re_node= document.getElementById("password_repeat_group");
    
    if(ctrl.password!=null){
      node.className="form-group col-md-offset-1 col-md-9 has-success";
    }else{
      node.className="form-group col-md-offset-1 col-md-9 has-error";
    }

    if (ctrl.confirm_password != ctrl.password) {
      re_node.className="form-group col-md-offset-1 col-md-9 has-error";
    } else {
      re_node.className="form-group col-md-offset-1 col-md-9 has-success";
    }
  }

  ctrl.checkPasswordRepeat=function(){
    var node= document.getElementById("password_repeat_group");
    if(ctrl.confirm_password!=null){
      node.className="form-group col-md-offset-1 col-md-9 has-success";
    }else{
      node.className="form-group col-md-offset-1 col-md-9 has-error";
    }

    if (ctrl.confirm_password != ctrl.password) {
      node.className="form-group col-md-offset-1 col-md-9 has-error";
    }
  }

    function validParam(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

  this.$window = $window;
};

jxdctopn.panel.forgot_password.Ctrl.prototype.say = function() {
  var $window = this.$window;
  $window.alert(this.text);
};
