'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.show_doctor_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.show_doctor_modal.Ctrl.$inject = [
    "$scope",
    "$filter",
    "$modalInstance",
    '$q',
    "entity",
    "constants",
    "db_operation",
    "basic",
    '$timeout',
    'md5'
];

jxmgrsec.show_doctor_modal.Ctrl = function($scope, $filter, $modalInstance, $q, entity, constants, db_operation, basic, $timeout, md5) {


    $scope.doctorId = entity;

    $scope.genders = [];
    $scope.districts = [];
    $scope.departments = [];

    $scope.headUrl = "../../../../img/touxiang.png";
    // $scope.headFile = undefined;
    $scope.physicianUrl = "../../../../img/touxiang.png";
    // $scope.physicianFile = undefined;
    $scope.practicingUrl = "../../../../img/touxiang.png";
    // $scope.practicingFile = undefined;

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
        practicing_certificate: ""

    };
    $scope.new_password = "";
    $scope.confirm_password = "";


    $scope.getDoctorById = function() {
        db_operation.getDoctorDetail($scope.doctorId).then(function(res) {
            $scope.signUpInf = res;
            if(validParam($scope.signUpInf.birthday)){
                $scope.birthday_date = new Date($scope.signUpInf.birthday);
            }
            if (validParam($scope.signUpInf.avatar_url)) {
                $scope.headUrl = $scope.signUpInf.avatar_url;
            }
            if (validParam($scope.signUpInf.physician_certificate)) {
                $scope.physicianUrl = $scope.signUpInf.physician_certificate;
            }
            if (validParam($scope.signUpInf.practicing_certificate)) {
                $scope.practicingUrl = $scope.signUpInf.practicing_certificate;
            }
        });
    }

    $scope.initArray = function() {
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


    $scope.activate = function() {
        var promises = [$scope.initArray(), $scope.getDoctorById()];
        return $q.all(promises).then(function() {});
    }

    $scope.activate();

    var validParam = function(param) {
        if (param != undefined && param != null && param != "") {
            return true;
        }
        return false;
    }

};
