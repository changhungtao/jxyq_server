'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.add_patient_modal.add_patient_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.add_patient_modal.add_patient_modal.Ctrl.$inject = [
    "$scope",
    "$modalInstance",
    "$timeout",
    "entity",
    "$filter",
    "health_file",
    "constants"

];
jxdctsec.add_patient_modal.add_patient_modal.Ctrl = function($scope, $modalInstance, health_file, constants, $filter, $timeout) {

    var ctrl = this;

    $scope.patient = {
        name: undefined,
        birthday: undefined,
        gender: 1,
        phone: undefined,
        address: undefined,
        profile: undefined
    }

    $scope.buttonEnabled = false; //防止重复提交
    $scope.addPatient = function() {
        if (!isMobileOrTell($scope.patient.phone)) {
            $scope.addAlert("warning", "手机号非法！");
            return;
        }
        if (!validParam($scope.patient.birthday)) {
            $scope.addAlert("warning", "出生日期必填！");
            return;
        }
        $scope.patient.birthday = $filter('date')($scope.patient.birthday, 'yyyy-M-dd');
        $scope.buttonEnabled = true;
        return health_file.addPatient($scope.patient).then(function(data) {
            console.log(data);
            $scope.addAlert("success", "添加患者成功！");
            $scope.buttonEnabled = false;
            $timeout(function() {
                $scope.cancel();
            }, 3000);
        }, function(error) {
            console.log(error);
            if (validParam(error.error_message) && error.error_message.faultcode == 1002020) {
                $scope.addAlert('danger', error.error_message.faultstring);
            } else if (validParam(error.success_message)) {
                $scope.addAlert("danger", "添加患者失败！");
            }
            $scope.buttonEnabled = false;
        });
    }

    function getGenders() {
        return constants.getGenders().then(function(data) {
            $scope.genders = data.genders;
            $scope.gender = {
                selected: undefined
            };
            return $scope.genders;
        });
    }

    getGenders();
    //  $scope.today = function() {
    //    $scope.dt = new Date();
    //
    //  };
    $scope.dt = new Date();


    //  $scope.clear = function() {
    //    $scope.dt = null;
    //  };

    // Disable weekend selection
    //  $scope.disabled = function(date, mode) {
    //    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
    //  };

    $scope.minDate = new Date('1900/01/01');
    //  $scope.toggleMin = function() {
    //    $scope.minDate = $scope.minDate ? null : new Date();
    //  };
    //  $scope.toggleMin();

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

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    //判断是否为有效手机号或座机号
    function isMobileOrTell(str) {
        var mobile = /^1\d{10}$/; //手机
        var tel = /^0\d{2,3}-?\d{7,8}$/;
        if (mobile.test(str) || tel.test(str)) {
            return true;
        }
        return false;
    }

    $scope.alert_type = undefined;
    $scope.alert_msg = undefined;
    $scope.addAlert = function(type, msg) {
        $scope.alert_type = type;
        $scope.alert_msg = msg;
        $timeout(function() {
            $scope.closeAlert();
        }, 3000);
    };

    $scope.closeAlert = function() {
        $scope.alert_type = undefined;
        $scope.alert_msg = undefined;
    };

    var validParam = function(param) {
        if (param != undefined && param != null && param != "") {
            return true;
        }
        return false;
    }

};
