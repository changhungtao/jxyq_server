'use strict';

goog.provide('jxdctopn.sign_up.Ctrl');

/**
 * Sign up controller.
 *
 * @constructor
 */
jxdctopn.sign_up.Ctrl.$inject = [
  "$scope",
  "$q",
  "$filter",
  'constants',
  '$rootScope',
  'basic',
  'md5',
  '$timeout',
  '$location'
];
jxdctopn.sign_up.Ctrl = function($scope, $q, constants, basic, md5, $rootScope,$timeout,$location,$filter) {
    var ctrl = this;

    ctrl.genders = constants.gotGENDERS();
    ctrl.gender = {selected: ctrl.genders[0]};
    ctrl.departments = constants.gotDEPARTMENTS();
    ctrl.department = {selected: undefined};
    ctrl.distrits = constants.gotDISTRICTS();

    // ctrl.province_list = [];
    // ctrl.province = {selected: undefined};
    // ctrl.city_list = [];
    // ctrl.city = {selected: undefined};
    // ctrl.county_list = [];
    // ctrl.county = {selected: undefined};

    ctrl.signUpInf = {
        login_name: "",
        password: "",
        full_name: "",
        identification_number: "",
        gender: "",
        birthday: "",
        email: "",
        avatar_url: "",
        phone: "",
        department_id: 1,
//        expert_team_id: "",
        district_id: 1,
        profile: "",
        physician_certificate: "",
        practicing_certificate: "",
        captcha: ""
    };
    ctrl.password = "";
    ctrl.confirm_password = "";

    ctrl.avatar_url = "";
    ctrl.physician_certificate = "";
    ctrl.practicing_certificate = "";

    activate();

    function activate() {
        // var promises = [getGender(),getDepartments(),getDistricts()];
        // return $q.all(promises).then(function () {
        // });
    }

    ctrl.today = function () {
        ctrl.dt = new Date();
    };
    ctrl.today();

    ctrl.clear = function () {
        ctrl.dt = null;
    };

    // Disable weekend selection
    ctrl.disabled = function (date, mode) {
        //return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    ctrl.toggleMin = function () {
        //ctrl.minDate = ctrl.minDate ? null : new Date();
    };
    ctrl.toggleMin();

    ctrl.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        ctrl.opened = true;
    };

    // function getGender() {
    //     return constants.getGenders().then(function (data) {
    //         console.log(data)
    //         ctrl.genders = data.genders;
    //         ctrl.gender = {selected: data.genders[0]}
    //         return ctrl.genders;
    //     });
    // }

    // function getDepartments(){
    //     return constants.getDepartments().then(function(data){
    //         ctrl.departments = data.departments;
    //         ctrl.department = {selected: undefined};
    //         return ctrl.departments;
    //     })
    // }

    // function getDistricts(){
    //     return constants.getDistricts().then(function(data){
    //         ctrl.distrits =  data.districts ;
    //         return ctrl.distrits;
    //     });
    // }

    ctrl.dateOptions = {
//        formatYear: 'yy',
        startingDay: 1
    };

    ctrl.formats = ['yyyy-MM-dd', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    ctrl.format = ctrl.formats[0];

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 2);
    ctrl.events =
        [
            {
                date: tomorrow,
                status: 'full'
            },
            {
                date: afterTomorrow,
                status: 'partially'
            }
        ];

    ctrl.getDayClass = function (date, mode) {
        if (mode === 'day') {
            var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

            for (var i = 0; i < ctrl.events.length; i++) {
                var currentDay = new Date(ctrl.events[i].date).setHours(0, 0, 0, 0);

                if (dayToCheck === currentDay) {
                    return ctrl.events[i].status;
                }
            }
        }

        return '';
    };

    ctrl.captcha_url = $rootScope.endPoint + '/common/sign_in_captcha?d=' + new Date().getTime();
    ctrl.captcha_click = function () {
        var time = new Date().getTime();
        ctrl.captcha_url = $rootScope.endPoint + '/common/sign_in_captcha?d=' + time;
    }

    ctrl.avatarFile = undefined;  //头像
    ctrl.idenFile = undefined;    //资格证书
    ctrl.proIdenFile = undefined; //执业证书


    var uploadAvatarFiles = function () {
        //上传图像，或得头像url存入数据库
        if (validParam(ctrl.avatarFile)) {
            return basic.fileUpload(ctrl.avatarFile).then(function (res) {
                ctrl.signUpInf.avatar_url = res.success_message[0];
                console.log("上传头像成功！");
                console.log(ctrl.signUpInf.avatar_url);
                return res;
            }, function (error) {
                console.log("上传头像失败！");
                console.log(error);
                return error;
            });
        } else {
            return $q.when(0);
        }
    }

    var uploadIdenFiles = function () {
        //资格证书
        if (validParam(ctrl.idenFile)) {
            return basic.fileUpload(ctrl.idenFile).then(function (res) {
                ctrl.signUpInf.practicing_certificate = res.success_message[0];
                console.log("上传头资格证书成功！");
                return res;
            }, function (error) {
                console.log("上传头资格证书失败！");
                console.log(error);
                return error;
            });
        } else {
            return $q.when(0);
        }
    }


    var uploadProIdenFiles = function () {
        //执业证书
        if (validParam(ctrl.proIdenFile)) {
            return basic.fileUpload(ctrl.proIdenFile).then(function (res) {
                ctrl.signUpInf.physician_certificate = res.success_message[0];
                console.log("上传执业证书成功！");
                return res;
            }, function (error) {
                console.log("上传执业证书失败！");
                console.log(error);
                return error;
            });
        } else {
            return $q.when(0);
        }
    }

//    function activate() {
//        var promises = [uploadAvatarFiles(), uploadIdenFiles(), uploadProIdenFiles()];
//        return $q.all(promises).then(function () {
//            logger.info('Activated Dashboard View');
//        });
//    }


    ctrl.signUp = function () {
        if(ctrl.password == ctrl.confirm_password){
            ctrl.signUpInf.gender = ctrl.gender.selected.gender_id;
            ctrl.signUpInf.password = md5.createHash(ctrl.password);

            if(validParam(ctrl.avatarFile) && !validPic(ctrl.avatarFile,'png|jpg|jpeg|gif')){$scope.addAlert('warning','头像请上传png、jpg类型的文件！');return;}
            if(!validParam(ctrl.idenFile) || !validPic(ctrl.idenFile,'png|jpg|jpeg|gif')){$scope.addAlert('warning','资格证书请上传png、jpg类型的文件！');return;}
            if(!validParam(ctrl.proIdenFile) || !validPic(ctrl.proIdenFile,'png|jpg|jpeg|gif')){$scope.addAlert('warning','执业证书请上传png、jpg类型的文件！');return;}

            var promises = [uploadAvatarFiles(), uploadIdenFiles(), uploadProIdenFiles()];
            if(validParam(ctrl.signUpInf.birthday)){
                ctrl.signUpInf.birthday = $filter('date')(ctrl.signUpInf.birthday, 'yyyy-M-dd');
            }
            //判断号码是否合法，最基本的判断，是数字
            if(validParam(ctrl.signUpInf.phone) && !isMobileOrTell(ctrl.signUpInf.phone)){
                $scope.addAlert('warning','联系电话非法！');
                return;
            }
            return $q.all(promises).then(function () {
                basic.signUp(ctrl.signUpInf)
                    .then(function (res) {
                        $scope.addAlert('success','注册成功，3秒钟后跳转到登陆页面！');
                        $timeout(function(){
                        //注册成功，跳转到登陆界面
                        $location.url('/panel/sign_in');
                        },3000);

                    }, function (error) {
                        ctrl.errmsg = error;
                        if(validParam(error.error_message.faultstring)){
                            $scope.addAlert('danger',error.error_message.faultstring);
                            ctrl.captcha_click();
                        }
                    })
            },function(error){
                console.log(error);
            });
        }

    };

    //判断是否为有效手机号或座机号
    function isMobileOrTell(str){
        var mobile = /^1\d{10}$/;//手机
        var tel = /^0\d{2,3}-?\d{7,8}$/;
        if(mobile.test(str) || tel.test(str)){
            return true;
        }
       return false;
    }

    //Alert
    $scope.alerts = [];
    $scope.addAlert = function (type, msg) {
        var alert = {'type': type, 'msg': msg};
        $scope.alerts.push(alert);
        $timeout(function(){
            $scope.closeAlert($scope.alerts.indexOf(alert));
        }, 6000);
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.clearAlerts = function() {
        $scope.alerts = [];
    };

    var validParam = function (param) {
        if (param != undefined && param != null && param != "") {
            return true;
        }
        return false;
    }

    /**
     * file:上传的文件对象
     * str：允许上传的文件类型字符串，例如：'png|jpg|jpeg|bmp|gif'
     * */
    function validPic(file,str){
        if(file != undefined && file.type != undefined && str != undefined && str != '' && str.length != 0){
            var suffixs = str.split('|');
            var fileSuf = file.type.split('/');
            var res = suffixs.filter(function(item){
                return item == fileSuf[1];
            });
            if(res.length != 0){
                return true;
            }
            return false;
        }
    }
}
