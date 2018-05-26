'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.delete_doctor_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.delete_doctor_modal.Ctrl.$inject = [
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

jxmgrsec.delete_doctor_modal.Ctrl = function($scope, $filter, $modalInstance, $q, entity, constants, db_operation, basic, $timeout, md5) {

    $scope.doctorId = entity;

    $scope.deleteDoctor = function() {
        console.log('deleteDoctor id: ' + $scope.doctorId);
        db_operation.deleteDoctor($scope.doctorId).then(function(res) {
            $modalInstance.dismiss('cancel');
        }, function(error) {
            console.log(error);
            $scope.addAlert("danger", "删除医生失败，请稍后再试！");
        });
    }


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
};
