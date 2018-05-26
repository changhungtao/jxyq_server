'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.add_doctor_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.add_doctor_modal.Ctrl.$inject = [
    "$scope",
    "$filter",
    "$modalInstance",
    '$q',
    "constants",
    "db_operation",
    "basic",
    '$timeout',
    'md5'
];

jxmgrsec.add_doctor_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, db_operation, basic, $timeout, md5) {


    $scope.genders = [];
    $scope.districts = [];
    $scope.departments = [];

    $scope.headUrl = "../../../../img/touxiang.png";
    $scope.headFile = undefined;
    $scope.physicianFile = undefined;
    $scope.practicingFile = undefined;

    $scope.birthday_date = undefined;

    $scope.signUpInf = {
        login_name: "",
        password: "",
        full_name: "",
        identification_number: "",
        gender: undefined,
        birthday: "",
        email: "",
        avatar_url: "",
        phone: "",
        department_id: undefined,
        // expert_team_id: undefined,
        district_id: undefined,
        profile: "",
        physician_certificate: "",
        practicing_certificate: "",

    };
    $scope.new_password = "";
    $scope.confirm_password = "";

    activate();

    function activate() {
        var promises = [initArray()];
        return $q.all(promises).then(function() {});
    }

    function initArray() {
        // console.log(constants.gotGENDERS());
        // console.log(constants.gotDISTRICTS());
        // console.log(constants.gotDEPARTMENTS());
        $scope.genders = constants.gotGENDERS();
        $scope.districts = constants.gotDISTRICTS();
        $scope.departments = constants.gotDEPARTMENTS();
    }


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

    //判断是否为手机号
    $scope.checkMobile = function(str) {
        var re = /^1\d{10}$/
        if (re.test(str)) {
            return true;
        } else {
            return false;
        }
    }

    $scope.toDDMMMYYYY = function(date) {
        var d = new Date(date.getTime());
        var dd = d.getDate() < 10 ? "0" + d.getDate() : d.getDate().toString();
        var mmm = d.getMonth() < 10 ? "0" + d.getMonth() : d.getMonth().toString();;
        var yyyy = d.getFullYear().toString(); //2011  
        return yyyy + "-" + mmm + "-" + dd;
    }

    $scope.checkPassword = function() {

        if (validParam($scope.new_password)) {
            if (!validParam($scope.confirm_password)) {
                $scope.addAlert("danger", "输入确认密码");
                return false;
            } else if ($scope.confirm_password != $scope.new_password) {
                $scope.addAlert("danger", "密码不一致");
                return false;
            } else {
                $scope.signUpInf.password = md5.createHash($scope.new_password);
                return true;
            }
        } else {
            $scope.addAlert("danger", "输入密码");
            return false;
        }
    }

    $scope.uploadInfo = function() {
        $scope.uploadHeadImg();
    };

    //上传头像
    $scope.uploadHeadImg = function() {
        if (validParam($scope.headFile)) {
            basic.fileUpload($scope.headFile).then(function(res) {
                console.log("上传头像");
                console.log(res);
                $scope.headUrl = res.success_message[0];
                $scope.signUpInf.avatar_url = res.success_message[0];
                $scope.uploadPhysImg();
            }, function(error) {
                console.log(error);
                $scope.addAlert("danger", "上传头像異常，请稍后再试！");
            })
        } else {
            $scope.uploadPhysImg();
        }

    };

    //上传资格证书
    $scope.uploadPhysImg = function() {
        if (validParam($scope.physicianFile)) {
            basic.fileUpload($scope.physicianFile).then(function(res) {
                console.log("上传资格证书");
                console.log(res);
                $scope.headUrl = res.success_message[0];
                $scope.signUpInf.physician_certificate = res.success_message[0];
                $scope.uploadPracImg();
            }, function(error) {
                console.log(error);
                $scope.addAlert("danger", "上传资格证书異常，请稍后再试！");
            })
        } else {
            $scope.addAlert("danger", "请上传资格证书");
        }

    };

    //上传执业证书
    $scope.uploadPracImg = function() {
        if (validParam($scope.practicingFile)) {
            basic.fileUpload($scope.practicingFile).then(function(res) {
                console.log("上传执业证书");
                console.log(res);
                $scope.headUrl = res.success_message[0];
                $scope.signUpInf.practicing_certificate = res.success_message[0];
                $scope.addDoctor();
            }, function(error) {
                console.log(error);
                $scope.addAlert("danger", "上传执业证书異常，请稍后再试！");
            })
        } else {
            $scope.addAlert("danger", "请上传执业证书");
        }

    };


    $scope.checkParam = function() {
        if (!validParam($scope.signUpInf.login_name)) {
            $scope.addAlert("danger", "请输入用户名！");
            return false;
        }
        if (!$scope.checkPassword()) {
            return false;
        }
        if (!validParam($scope.signUpInf.full_name)) {
            $scope.addAlert("danger", "请输入医生姓名！");
            return false;
        }
        if (!validParam($scope.signUpInf.identification_number)) {
            $scope.addAlert("danger", "请输入医生工号！");
            return false;
        }
        if (!validParam($scope.signUpInf.email)) {
            $scope.addAlert("danger", "请输入电子邮箱！");
            return false;
        }
        if (undefined == $scope.signUpInf.gender) {
            $scope.addAlert("danger", "请输入性别！");
            return false;
        }
        if (validParam($scope.birthday_date)) {
            $scope.signUpInf.birthday = $filter('date')($scope.birthday_date, 'yyyy-MM-dd');
        } else {
            $scope.addAlert("danger", "请输入出生日期！");
            return false;
        }
        if (!validParam($scope.signUpInf.phone) || !$scope.checkMobile($scope.signUpInf.phone)) {
            $scope.addAlert("danger", "请输入正确的联系电话！");
            return false;
        }
        if (!validParam($scope.signUpInf.department_id)) {
            $scope.addAlert("danger", "请选择科室！");
            return false;
        }
        if (!validParam($scope.signUpInf.district_id)) {
            $scope.addAlert("danger", "请选择地区！");
            return false;
        }

        if (!validParam($scope.physicianFile)) {
            $scope.addAlert("danger", "请上传资格证书");
            return false;
        }
        if (!validParam($scope.practicingFile)) {
            $scope.addAlert("danger", "请上传执业证书");
            return false;
        }

        if (!$scope.uploadHeadImg()) {
            return;
        }
        if (!$scope.uploadPhysImg()) {
            return false;
        }
        if (!$scope.uploadPracImg()) {
            return false;
        }
    };

    $scope.disabledBtn = false;
    $scope.addDoctor = function() {
//        $scope.signUpInf.gender = $scope.gender.selected.gender_id;
//        console.log("submit");
        $scope.disabledBtn = true;
        if (validParam($scope.birthday_date)) {
            $scope.signUpInf.birthday = $filter('date')($scope.birthday_date, 'yyyy-M-dd');
        };
        if($scope.signUpInf.birthday == ''){
            $scope.signUpInf.birthday = undefined;
        }
        db_operation.addDoctor($scope.signUpInf).then(function(res) {
            $scope.addAlert("success", "添加用户成功,3秒后自动关闭！");
            $timeout(function(){
                $scope.cancel();
            },3000);

        }, function(error) {
//            console.log(error);
            $scope.addAlert("danger", "添加失败，请稍后再试！");
            $scope.disabledBtn = false;
        });
    }


    $scope.signUp = function() {
        if ($scope.checkParam()) {
            $scope.uploadInfo();
        }
    }

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

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
