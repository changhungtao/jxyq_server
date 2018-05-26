'use strict';

goog.provide('jxspropn.panel.sign_in.Ctrl');

/**
 * Sign in controller.
 *
 * @constructor
 */

jxspropn.panel.sign_in.Ctrl.$inject = [
  "$window",
  'basic',
  '$rootScope',
  'md5',
  '$timeout'
];
jxspropn.panel.sign_in.Ctrl = function($window, basic, $rootScope, md5, $timeout) {

    var ctrl=this;

    ctrl.userLoginInfo = {
        login_name: '',
        password: '',
        captcha: '',
        role:'super'
    };
    ctrl.password = '';

    ctrl.captcha_url = $rootScope.endPoint + '/common/sign_in_captcha?d=' + new Date().getTime();
    ctrl.captcha_click = function(){
        ctrl.userLoginInfo.captcha = '';
        var time = new Date().getTime();
        ctrl.captcha_url = $rootScope.endPoint + '/common/sign_in_captcha?d=' + time;
    }

    ctrl.alert_type = undefined;
    ctrl.alert_msg = undefined;
    ctrl.addAlert = function (type, msg) {
        ctrl.alert_type = type;
        ctrl.alert_msg = msg;
        $timeout(function(){ctrl.closeAlert();}, 3000);
    };

    ctrl.closeAlert = function() {
        ctrl.alert_type = undefined;
        ctrl.alert_msg = undefined;
    };

    ctrl.signIn = function(){
        if (ctrl.userLoginInfo.login_name==undefined || ctrl.userLoginInfo.login_name=='') {return;}
        if (ctrl.password==undefined || ctrl.password=='') {return;}
        if (ctrl.userLoginInfo.captcha==undefined || ctrl.userLoginInfo.captcha=='') {return;}

        ctrl.userLoginInfo.password = md5.createHash(ctrl.password);
        basic.signIn(ctrl.userLoginInfo)
            .then(function(res){
                if (res.success_message == undefined) {
                    if (res.error_message.faultcode == 1001022) {
                        ctrl.addAlert('warning', '验证码错误');
                    }else if (res.error_message.faultcode == 1002036){
                        ctrl.captcha_click();
                        ctrl.addAlert('warning', '该用户尚未激活,请联系超级管理员');
                    }else if (res.error_message.faultcode == 1002010){
                        ctrl.captcha_click();
                        ctrl.addAlert('danger', '该用户不是超级管理员,无法登陆');
                    }else{
                        ctrl.captcha_click();
                        ctrl.addAlert('danger', '用户名或密码错误');
                    };
                    return;
                };
                $window.location.href = '/super/secured';
            }, function(error){
                ctrl.addAlert('danger', '系统错误');
            })
    };

};