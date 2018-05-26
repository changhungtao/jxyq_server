'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.modify_user_modal.modify_user_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.modify_user_modal.modify_user_modal.Ctrl.$inject = [
    "$scope",
    "$modalInstance",
    '$q',
    "entity",
    "constants",
    "db_operation",
    "basic",
    '$timeout',
    'md5',
    '$filter'
];
jxmgrsec.modify_user_modal.modify_user_modal.Ctrl = function($scope, $filter,$modalInstance, $q, entity, constants, db_operation, basic, $timeout, md5) {

    var ctrl = this;


    // $scope.userInfo = entity;
    $scope.userId = entity;
    $scope.imgUrl = "../../../../img/touxiang.png";
    $scope.userInfo = {
        nick_name: undefined,
        full_name: undefined,
        password: undefined,
        phone: undefined,
        status: undefined,
        email: undefined,
        avatar_url: undefined,
        gender: undefined,
        birthday: undefined,
        height: undefined,
        weight: undefined,
        target_weight: undefined,
        address: undefined,
        qq: undefined,
        operation_type: undefined,
        delta_points: undefined,
        reason: undefined

    };

    $scope.uploadFile = undefined;

    // $scope.gender = {
    //     selected: undefined
    // };
    $scope.genders = [];

    $scope.operationTypes = [{
        "id": 1,
        "name": "增加"
    }, {
        "id": 2,
        "name": "减少"
    }];

    $scope.birthday_date = new Date();

    $scope.chPassword = {
        confirm_password: undefined,
        new_password: undefined
    }

    //判断是否为正数
    $scope.ChenPosNum = function(num) {
        var reg = /^\d+(?=\.{0,1}\d+$|$)/
        if (reg.test(num)) return true;
        return false;
    }

    //判断是否为手机号
    $scope.checkMobile = function(str) {
        var re = /^1\d{10}$/
        if (re.test(str)) {
            return true;
        } else {
            return false;
        }
    }


    $scope.checkEmail = function(str) {
        // var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
        var re = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
        if (re.test(str)) {
            return true;
        } else {
            return false;
        }
    }


    $scope.checkNumData = function(data) {
        if (data == undefined || data == '') {
            return true;
        } else if ($scope.ChenPosNum(data)) {
            return true;
        } else {
            return false;
        }
    }

    $scope.checkMobileData = function(data) {
        if (data == undefined || data == '') {
            return true;
        } else if ($scope.checkMobile(data)) {
            return true;
        } else {
            return false;
        }
    }

    $scope.checkEmailData = function(data) {
        if (data == undefined || data == '') {
            return true;
        } else if ($scope.checkEmail(data)) {
            return true;
        } else {
            return false;
        }
    }

    $scope.checkPassword = function() {

        if (validParam($scope.chPassword.new_password)) {
            if (!validParam($scope.chPassword.confirm_password)) {
                $scope.addAlert("danger", "输入确认密码");
                return false;
            } else if ($scope.chPassword.confirm_password != $scope.chPassword.new_password) {
                $scope.addAlert("danger", "密码不一致");
                return false;
            } else {
                $scope.userInfo.password = md5.createHash($scope.chPassword.new_password);
                return true;
            }
        } else {
            return true;
        }
    }

    $scope.putUserModel = function() {
        console.log("submit");
        $scope.userInfo.birthday = $filter('date')($scope.birthday_date, 'yyyy-MM-dd');
        if (validParam($scope.uploadFile)) {
            console.log("fileUpload");
            basic.fileUpload($scope.uploadFile).then(function(res) {
                console.log(res);
                $scope.imgUrl = res.success_message[0];
                $scope.userInfo.avatar_url = res.success_message[0];
                upload();
            }, function(error) {
                console.log(error);
                $scope.addAlert("danger", "上传头像異常，请稍后再试！");
            })

        } else {
            upload();
        }
    }

    $scope.disableBtn = false;
    var upload = function() {
        $scope.disableBtn = true;

        if(!validParam($scope.userInfo.gender)){ $scope.addAlert("danger", "性别必填！");$scope.disableBtn = false;return;}
        if(!validParam($scope.userInfo.email)){ $scope.addAlert("danger", "邮箱必填！");$scope.disableBtn = false;return;}
        if(!validParam($scope.userInfo.address)){ $scope.addAlert("danger", "地址必填！");$scope.disableBtn = false;return;}
        if (!$scope.checkPassword()) {
            $scope.disableBtn = false;
            return;
        }
        if (!$scope.checkEmailData($scope.userInfo.email)) {
//            console.log("输入正确邮箱");
            $scope.addAlert("danger", "输入正确邮箱");
            $scope.disableBtn = false;
            return;
        };

        if (!$scope.checkNumData($scope.userInfo.height)) {
            $scope.addAlert("danger", "输入正确身高");
            $scope.disableBtn = false;
            return;
        };

        if (!$scope.checkNumData($scope.userInfo.weight)) {
            $scope.addAlert("danger", "输入正确体重");
            $scope.disableBtn = false;
            return;
        };
        if (!$scope.checkNumData($scope.userInfo.target_weight)) {
            $scope.addAlert("danger", "输入正确目标体重");
            $scope.disableBtn = false;
            return;
        };
        if (!$scope.checkNumData($scope.userInfo.delta_points)) {
            $scope.addAlert("danger", "输入正确变更分值");
            $scope.disableBtn = false;
            return;
        };
//        console.log($scope.userInfo);
        if(!isNaN($scope.userInfo.weight)){
            $scope.userInfo.weight = $scope.userInfo.weight*10;
        }
        if(!isNaN($scope.userInfo.target_weight)){
            $scope.userInfo.target_weight = $scope.userInfo.target_weight*10;
        }
        db_operation.putUser($scope.userId, $scope.userInfo).then(function(res) {

//            console.log(res);
            $scope.addAlert("success","修改用户信息成功，3秒后自动关闭！");
            $timeout(function(){
                $scope.disableBtn = true;
                $scope.cancel();
            },3000);
        }, function(error) {
            console.log(error);
            //$scope.addAlert("danger","提交失败，请稍后再试！");
        });


    }

    $scope.cancel = function() {
        console.log("cancel");
        $modalInstance.dismiss('cancel');
    };

    $scope.clear = function() {
        console.log("666666666666");
        $scope.userInfo = undefined;
        $scope.chPassword = {
            confirm_password: undefined,
            new_password: undefined
        }

    };

    $scope.dt = new Date();

    $scope.open_form_birthday = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.birthday_opened = true;
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



    $scope.getGenders = function() {
        return constants.getGenders().then(function(data) {
            console.log(data);
            $scope.genders = data.genders;
            // $scope.gender = {
            //     selected: undefined
            // };
            return $scope.genders;
        });
    }


    $scope.getUsersById = function() {
        db_operation.getUserDetail($scope.userId).then(function(res) {
            console.log(res);
            $scope.userInfo = res;
            if(validParam($scope.userInfo.birthday)){
                $scope.birthday_date = new Date($scope.userInfo.birthday);
            }
            if(!isNaN($scope.userInfo.weight)){
                $scope.userInfo.weight = $scope.userInfo.weight/10;
            }
            if(!isNaN($scope.userInfo.target_weight)){
                $scope.userInfo.target_weight = $scope.userInfo.target_weight/10;
            }
            if ($scope.userInfo.avatar_url == undefined || $scope.userInfo.avatar_url == '') {

            } else {
                $scope.imgUrl = $scope.userInfo.avatar_url;
            }
        });
    }

    $scope.activate = function() {
        var promises = [$scope.getGenders(), $scope.getUsersById()];
        return $q.all(promises).then(function() {});
    }

    $scope.activate();

    //Alert
    $scope.alerts = [];
    $scope.addAlert = function(type, msg) {
        var alert = {
            'type': type,
            'msg': msg
        };
        $scope.alerts.push(alert);
        $timeout(function() {
            $scope.closeAlert($scope.alerts.indexOf(alert));
        }, 3000);
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.clearAlerts = function() {
        $scope.alerts = [];
    };


    var validParam = function(param) {
        if (param != undefined && param != null && param != "") {
            return true;
        }
        return false;
    }

};
