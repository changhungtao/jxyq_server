'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.modify_manufactory_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.modify_manufactory_modal.Ctrl.$inject = [
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

jxmgrsec.modify_manufactory_modal.Ctrl = function($scope, $filter, $modalInstance, $q, entity, constants, db_operation, basic, $timeout, md5) {


    $scope.manuId = entity;

    $scope.company_departments = constants.gotCOMPANYDEPARTMENTS();
    $scope.device_types = constants.gotDEVICETYPES();
    $scope.company_members_list = constants.gotCOMPANYMEMBERS();
    $scope.company_industry_list = constants.gotCOMPANYINDUSTRIES();
    $scope.company_nature_list = constants.gotCOMPANYNATURES();
    $scope.province_list = constants.gotPROVINCES();
    $scope.city_list = constants.gotCITIES();
    $scope.county_list = constants.gotZONES();

    $scope.business_licencefile = undefined; //工商营业执照
    $scope.internal_certificatefile = undefined; //组织机构代码证
    $scope.local_certificatefile = undefined; //税务登记证-国税
    $scope.code_certificatefile = undefined; //税务登记证-地税
    $scope.logo_file = undefined; //税务登记证-地税

    $scope.manufactory = {
        login_name: undefined,
        full_name: undefined,
        contactor: undefined,
        password: undefined,
        department: undefined,
        telephone: undefined,
        phone: undefined,
        email: undefined,
        code: undefined,
        profile: undefined,
        province_id: undefined,
        city_id: undefined,
        zone_id: undefined,
        address: undefined,
        device_type_ids: undefined,
        members: undefined,
        industry: undefined,
        nature: undefined,
        business_licence: undefined,
        internal_certificate: undefined,
        local_certificate: undefined,
        code_certificate: undefined,
        logo_url: undefined
    };
    $scope.new_password = "";
    $scope.confirm_password = "";

    $scope.changeProvince = function(province_id){
        if(validParam($scope.city_list)){
            $scope.city_select_list = [];
            $scope.manufactory.city_id = undefined;
            $scope.manufactory.zone_id = undefined;

            for(var i = 0;i <  $scope.city_list.length;i++){
                if($scope.city_list[i].province_id == province_id){
                    $scope.city_select_list.push($scope.city_list[i]);
                }
            }
        }
    }

    $scope.changeCity = function(city_id){
        if(validParam($scope.county_list)){
            $scope.county_select_list = [];
            $scope.manufactory.zone_id = undefined;

            for(var i = 0;i <  $scope.county_list.length;i++){
                if($scope.county_list[i].city_id == city_id){
                    $scope.county_select_list.push($scope.county_list[i]);
                }
            }

        }
    }


    $scope.getManuById = function() {
        return db_operation.getManuDetail($scope.manuId).then(function(res) {
//            console.log(res);
            $scope.changeProvince(res.province_id);
            $scope.changeCity(res.city_id);
            $scope.manufactory = res;
            // if (validParam($scope.signUpInf.birthday)) {
            //     $scope.birthday_date = new Date($scope.signUpInf.birthday);
            // }
            // if (validParam($scope.signUpInf.avatar_url)) {
            //     $scope.headUrl = $scope.signUpInf.avatar_url;
            // }
            // if (validParam($scope.signUpInf.physician_certificate)) {
            //     $scope.physicianUrl = $scope.signUpInf.physician_certificate;
            // }
            // if (validParam($scope.signUpInf.practicing_certificate)) {
            //     $scope.practicingUrl = $scope.signUpInf.practicing_certificate;
            // }
        });
    }

    $scope.activate = function() {
        var promises = [$scope.getManuById()];
        return $q.all(promises).then(function() {});
    }

    $scope.activate();

    //判断是否为手机号
    function checkMobile(str) {
            var re = /^1\d{10}$/
            if (re.test(str)) {
                return true;
            } else {
                return false;
            }
        }

    function isTelephone(str){
        var tel = /^0\d{2,3}-?\d{7,8}$/;//固定电话
        if(tel.test(str)){
            return true;
        }
        return false;
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
                $scope.manufactory.password = md5.createHash($scope.new_password);
                return true;
            }
        } else {
            // $scope.addAlert("danger", "输入密码");
            return true;
        }
    }


    $scope.modifyManuInfo = function() {
        if ($scope.checkParam()) {
            uploadBusiness_licenceFile();
        };
    };

    var uploadBusiness_licenceFile = function() {
        //工商营业执照
        if (validParam($scope.business_licencefile)) {
            return basic.fileUpload($scope.business_licencefile).then(function(res) {
                $scope.manufactory.business_licence = res.success_message[0];
                console.log("上传工商营业执照成功！");
                uploadInternal_certificateFile();
            }, function(error) {
                console.log("上传工商营业执照失败！");
                console.log(error);
            });
        } else {
            uploadInternal_certificateFile();
        }
    }

    var uploadInternal_certificateFile = function() {
        //组织机构代码证
        if (validParam($scope.internal_certificatefile)) {
            return basic.fileUpload($scope.internal_certificatefile).then(function(res) {
                $scope.manufactory.internal_certificate = res.success_message[0];
                console.log("上传组织机构代码证成功！");
                uploadLocal_certificateFile();
            }, function(error) {
                console.log("上传组织机构代码证失败！");
                console.log(error);
            });
        } else {
            uploadLocal_certificateFile();
        }
    }

    var uploadLocal_certificateFile = function() {
        //税务登记证-国税
        if (validParam($scope.local_certificatefile)) {
            return basic.fileUpload($scope.local_certificatefile).then(function(res) {
                $scope.manufactory.local_certificate = res.success_message[0];
                console.log("上传税务登记证-国税成功！");
                uploadCode_certificateFile();
            }, function(error) {
                console.log("上传税务登记证-国税失败！");
                console.log(error);
            });
        } else {
            uploadCode_certificateFile();
        }
    }

    var uploadCode_certificateFile = function() {
        //税务登记证-地税
        if (validParam($scope.code_certificatefile)) {
            return basic.fileUpload($scope.code_certificatefile).then(function(res) {
                $scope.manufactory.code_certificate = res.success_message[0];
                console.log("上传税务登记证-地税成功！");
                uploadLogoFile();
            }, function(error) {
                console.log("上传税务登记证-地税失败！");
                console.log(error);
            });
        } else {
            uploadLogoFile();
        }
    }

    var uploadLogoFile = function() {
        //税务登记证-地税
        if (validParam($scope.logo_file)) {
            return basic.fileUpload($scope.logo_file).then(function(res) {
                $scope.manufactory.logo_url = res.success_message[0];
                console.log("上传厂商商标成功！");
                $scope.modifyManu();
            }, function(error) {
                console.log("上传厂商商标失败！");
                console.log(error);
            });
        } else {
            $scope.modifyManu();
        }
    }



    $scope.checkParam = function() {
        if (!$scope.checkPassword()) {
            return false;
        }
        //以下下联系人信息
        if (!validParam($scope.manufactory.contactor)) {
            $scope.addAlert("danger", "请输入联系人姓名！");
            return false;
        }
        if (!validParam($scope.manufactory.department)) {
            $scope.addAlert("danger", "请选择所在部门！");
            return false;
        }

        if (!validParam($scope.manufactory.telephone) || !isTelephone($scope.manufactory.telephone)) { //
            $scope.addAlert("danger", "请输入正确的固定电话！");
            return false;
        }

        if (!validParam($scope.manufactory.phone) || !checkMobile($scope.manufactory.phone)) {
            $scope.addAlert("danger", "请输入正确的手机号！");
            return false;
        }

        if (!validParam($scope.manufactory.email)) {
            $scope.addAlert("danger", "请输入联系人邮箱！");
            return false;
        }

        //以下为公司公司信息

        if (!validParam($scope.manufactory.full_name)) {
            $scope.addAlert("danger", "请输入厂商名称！");
            return false;
        }

        if (!validParam($scope.manufactory.code)) {
            $scope.addAlert("danger", "厂商编码！");
            return false;
        }

        if (!validParam($scope.manufactory.profile)) {
            $scope.addAlert("danger", "请输入厂商简介！");
            return false;
        }

        if (!validParam($scope.manufactory.province_id)) {
            $scope.addAlert("danger", "请选择一级行政区！");
            return false;
        }

        if (!validParam($scope.manufactory.city_id)) {
            $scope.addAlert("danger", "请选择二级行政区！");
            return false;
        }
        if (!validParam($scope.manufactory.zone_id)) {
            $scope.addAlert("danger", "请选择三级行政区！");
            return false;
        }

        if (!validParam($scope.manufactory.address)) {
            $scope.addAlert("danger", "请输入公司地址！");
            return false;
        }
        if (!validParam($scope.manufactory.device_type_ids)) {
            $scope.addAlert("danger", "请选择负责销售设备类型！");
            return false;
        }
        if (!validParam($scope.manufactory.members)) {
            $scope.addAlert("danger", "请输入企业人数！");
            return false;
        }

        if (!validParam($scope.manufactory.industry)) {
            $scope.addAlert("danger", "请选择公司行业！");
            return false;
        }

        if (!validParam($scope.manufactory.nature)) {
            $scope.addAlert("danger", "请选择公司性质！");
            return false;
        }
        if (!validParam($scope.manufactory.members)) {
            $scope.addAlert("danger", "请输入企业人数！");
            return false;
        }


        if (!validParam($scope.manufactory.business_licence) && !validParam($scope.business_licencefile)) {
            $scope.addAlert("danger", "请上传工商营业执照！");
            return false;
        }

        if (!validParam($scope.manufactory.internal_certificate) && !validParam($scope.internal_certificatefile)) {
            $scope.addAlert("danger", "请上传组织机构代码证！");
            return false;
        }
        if (!validParam($scope.manufactory.local_certificate) && !validParam($scope.local_certificatefile)) {
            $scope.addAlert("danger", "请上传税务登记证-国税！");
            return false;
        }
        if (!validParam($scope.manufactory.code_certificate) && !validParam($scope.code_certificatefile)) {
            $scope.addAlert("danger", "请上传税务登记证-地税！");
            return false;
        }

        if (!validParam($scope.manufactory.logo_url) && !validParam($scope.logo_file)) {
            $scope.addAlert("danger", "请上传厂商商标！");
            return false;
        }
        return true;
    };

    $scope.disabledBtn = false;
    $scope.modifyManu = function() {
        $scope.disabledBtn = true;
        db_operation.modifyManu($scope.manuId, $scope.manufactory).then(function(res) {
            $scope.addAlert('success','修改厂商信息成功，3秒钟后自动关闭！');
            $timeout(function(){
                $scope.cancel();
            },3000);
        }, function(error) {
            console.log(error);
            $scope.addAlert("danger", "修改失败，请稍后再试！");
        });
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

    function validParam(param) {
        if (param != undefined && param != null && param != "") {
            return true;
        }
        return false;
    }

};
