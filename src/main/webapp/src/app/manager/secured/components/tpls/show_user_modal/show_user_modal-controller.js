'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.show_user_modal.show_user_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.show_user_modal.show_user_modal.Ctrl.$inject = [
    "$scope",
    "$modalInstance",
    '$q',
    "entity",
    "constants",
    "db_operation",
    "basic"
];
jxmgrsec.show_user_modal.show_user_modal.Ctrl = function($scope, $modalInstance, $q, entity, constants, db_operation, basic) {

    var ctrl = this;


    // $scope.userInfo = entity;
    $scope.userId = entity;
    $scope.imgUrl = "../../../../img/touxiang.png";
    $scope.userInfo = {
        nick_name: undefined,
        full_name: undefined,
        password: undefined,
        phone: undefined,
        device_type_ids: undefined,
        status: undefined,
        email: undefined,
        avatar_url: "../../../img/touxiang.png",
        gender: undefined,
        birthday: undefined,
        height: undefined,
        weight: undefined,
        target_weight: undefined,
        address: undefined,
        qq: undefined,
        points: undefined
    };

    $scope.uploadFile = undefined;

    // $scope.gender = {
    //     selected: undefined
    // };
    $scope.genders = [];

    $scope.birthday_date = new Date('2014-01-01');

    $scope.chPassword = {
        confirm_password: undefined,
        new_password: undefined
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
                return true;
            }

        } else {
            return true;
        }
    }

    $scope.toDDMMMYYYY = function(date) {
        var d = new Date(date.getTime());
        var dd = d.getDate() < 10 ? "0" + d.getDate() : d.getDate().toString();
        var mmm = d.getMonth() < 10 ? "0" + d.getMonth() : d.getMonth().toString();;
        var yyyy = d.getFullYear().toString(); //2011  
        //var YY = YYYY.substr(2);   // 11  
        return yyyy + "-" + mmm + "-" + dd;

    }

    $scope.putUserModel = function() {
        console.log("submit");
        $scope.userInfo.birthday = $scope.toDDMMMYYYY($scope.birthday_date);
        if (validParam($scope.uploadFile)) {
            console.log("fileUpload");
            basic.fileUpload($scope.uploadFile).then(function(res) {
                console.log(res);
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

    var upload = function() {
        if ($scope.checkPassword()) {
            db_operation.putUser($scope.userId, $scope.userInfo).then(function(res) {
                console.log(res);
                $scope.cancel();
                //$scope.addAlert("success","提交成功");
            }, function(error) {
                console.log(error);
                //$scope.addAlert("danger","提交失败，请稍后再试！");
            });
        };

    }

    $scope.cancel = function() {
        console.log("cancel");
        $modalInstance.dismiss('cancel');
    };

    $scope.clear = function() {
        console.log("666666666666");
        $scope.userInfo = undefined;
    };

    //     $scope.dt = new Date();
    // $scope.query_date = {
    //       from:new Date('2014-01-01'),
    //       to:new Date()
    // }
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


    $scope.getUsersByName = function() {
        console.log($scope.userSearchNameData);
        db_operation.getUserDetail($scope.userId).then(function(res) {
            console.log(res);
            $scope.userInfo = res;
            if(!isNaN($scope.userInfo.weight)){
                $scope.userInfo.weight = $scope.userInfo.weight/10;
            }
            if(!isNaN($scope.userInfo.target_weight)){
                $scope.userInfo.target_weight = $scope.userInfo.target_weight/10;
            }
            if(validParam($scope.userInfo.birthday)){
                $scope.birthday_date = new Date($scope.userInfo.birthday);
            }
            if ($scope.userInfo.avatar_url == undefined || $scope.userInfo.avatar_url == '') {

            } else {
                $scope.imgUrl = $scope.userInfo.avatar_url;
            }
        });
    }

    $scope.activate = function() {
        var promises = [$scope.getGenders(), $scope.getUsersByName()];
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
