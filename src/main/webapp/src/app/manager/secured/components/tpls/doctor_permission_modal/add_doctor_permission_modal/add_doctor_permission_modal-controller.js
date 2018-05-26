/**
 * Created by zhanga.fnst on 2015/7/3.
 */
'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.add_doctor_permission_modal.Ctrl');

/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.add_doctor_permission_modal.Ctrl.$inject = [
    "$scope",
    "$filter",
    "$modalInstance",
    "$timeout",
    "$q",
    "constants",
    "doctor"
];
jxmgrsec.add_doctor_permission_modal.Ctrl = function($scope,$filter,$modalInstance,$timeout,$q,constants,doctor,doctor_id) {
    $scope.data_selects = [];
    $scope.permission = {
        permission_name:undefined,
        district_id:undefined,
        province_id:undefined,
        city_id:undefined,
        zone_id:undefined,
        data_type:1,
        filters:[]
    }
    var validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

    var permissionIsValided = function(){
        if(!validParam($scope.permission.permission_name)){
            $scope.disabledBtn = false;
            return false;
        }
        if(!validParam($scope.permission.data_type)){
            $scope.disabledBtn = false;
            return false;
        }
        return true;
    }

    var getWristbandColumns = function(){
        constants.getWristbandColumns().then(function(data){
            $scope.data_selects = data.columns;
            //console.log($scope.data_selects);
        },function(error){
            console.log(error);
        });
    }

    var getSphygmomanometerColumns = function(){
        constants.getSphygmomanometerColumns().then(function(data){
            $scope.data_selects = data.columns;
            //console.log($scope.data_selects);
        },function(error){
            console.log(error);
        });
    }

    var getGlucosemeterColumns = function(){
        constants.getGlucosemeterColumns().then(function(data){
            $scope.data_selects = data.columns;
            //console.log($scope.data_selects);
        },function(error){
            console.log(error);
        });
    }

    var getThermometerColumns = function(){
        constants.getThermometerColumns().then(function(data){
            $scope.data_selects = data.columns;
            //console.log($scope.data_selects);
        },function(error){
            console.log(error);
        });
    }

    var getOximeterColumns = function(){
        constants.getOximeterColumns().then(function(data){
            $scope.data_selects = data.columns;
            //console.log($scope.data_selects);
        },function(error){
            console.log(error);
        });
    }

    var getFatColumns = function(){
        constants.getFatColumns().then(function(data){
            $scope.data_selects = data.columns;
            //console.log($scope.data_selects);
        },function(error){
            console.log(error);
        });
    }

    var getComparisonOps = function(){
        constants.getComparisonOps().then(function(data){
            //console.log(data);
            $scope.comparisonOps_selects = data.ops;
            //console.log($scope.comparisonOps_selects);
        },function(error){
            console.log(error);
        });
    }
    getComparisonOps();

    var getLogicalOps = function(){
        constants.getLogicalOps().then(function(data){
            //console.log(data);
            $scope.logicalOps_selects = data.ops;
            //console.log($scope.logicalOps_selects);
        },function(error){
            console.log(error);
        });
    }
    getLogicalOps();

    var getDataType = function(){
        constants.getDataTypes().then(function(data){
            $scope.dataType_selects = data.data_types;
            //console.log(data);
        });
    }

    $scope.changeDataType  = function(){
        initialSelects($scope.permission.data_type);
        $scope.permission.filters = [];
    }

    getDataType();

    var getDistricts = function(){
        constants.getDistricts().then(function(data){
            $scope.districts_selects = data.districts;
//            console.log('Districts');
//            console.log(data);
        });
    }
    //getDistricts();

    $scope.changeDistrict = function(district_id){
        if(validParam($scope.province_list)){
            $scope.province_select_list = [];
            $scope.permission.city_id = undefined;
            $scope.permission.province_id = undefined;
            $scope.permission.zone_id = undefined;

            for(var i = 0;i <  $scope.province_list.length;i++){
                if($scope.province_list[i].district_id == district_id){
                    $scope.province_select_list.push($scope.province_list[i]);
                }
            }

        }
    }

    function getProvinces() {
        return constants.getProvinces().then(function (data) {
            $scope.province_list = data.provinces;
        });
    }

    //getProvinces();

    function getCities() {
        return constants.getCities().then(function (data) {
            $scope.city_list = data.cities;
        });
    }
    getCities();

    function getZones() {
        return constants.getZones().then(function (data) {
            $scope.county_list = data.zones;
        });
    }
    //getZones();

    activate();

    function activate() {
        var promises = [ getDistricts(), getProvinces(), getCities(), getZones()];
        return $q.all(promises).then(function() {
            $scope.changeDistrict($scope.permission.district_id);
            updateCityList($scope.permission.province_id);
            updateCountyList($scope.permission.city_id);
        });
    }

    function updateCityList(province_id){
        if(validParam($scope.city_list.length)){
            $scope.city_select_list = [];
            for(var i = 0; i < $scope.city_list.length; i++){
                if ($scope.city_list[i].province_id == province_id) {
                    $scope.city_select_list.push($scope.city_list[i]);
                };
            }
        }
    }

    function updateCountyList(city_id){
        if(validParam($scope.county_list.length)){
            $scope.county_select_list = [];
            for(var i = 0; i < $scope.county_list.length; i++){
                if ($scope.county_list[i].city_id == city_id) {
                    $scope.county_select_list.push($scope.county_list[i]);
                };
            }
        }
    }

    $scope.changeProvince=function(){
        $scope.permission.city_id = undefined;
        $scope.permission.zone_id = undefined;
        updateCityList($scope.permission.province_id);
    }

    $scope.changeCity=function(){
        $scope.permission.zone_id = undefined;
        updateCountyList($scope.permission.city_id);


    }

    var initialSelects = function(datatype){
        if(validParam(datatype)){
            switch(datatype){
                case 1:getWristbandColumns();break;         //手环
                case 2:getSphygmomanometerColumns();break;  //血压
                case 3:getOximeterColumns();break;          //血氧
                case 4:getGlucosemeterColumns();break;      //血糖
                case 5:getThermometerColumns();break;       //体温
                case 6:getFatColumns();break;               //脂肪
            }
            //console.log($scope.data_selects);
        }
    }
    //根据date_type绑定下拉框
    initialSelects($scope.permission.data_type);

    $scope.oneFilter = {
        name_id:undefined,
        comparison_op_id:undefined,
        value:undefined,
        logical_op_id:undefined
    }

      $scope.visible = true;
//    $scope.visible = false;
//    $scope.show_add_permission_row = function(){
//        $scope.visible = true;
//    }

    $scope.push_one_permission = function(){
        if(!validParam($scope.oneFilter.name_id) || !validParam($scope.oneFilter.comparison_op_id)||
             !validParam($scope.oneFilter.value) || !validParam($scope.oneFilter.logical_op_id)   ||
             isNaN($scope.oneFilter.value)){
            $scope.addAlert("warning","请检查参数的正确性！");
            return;
        }
        $scope.permission.filters.push(angular.copy($scope.oneFilter));
        //$scope.visible = false;
        $scope.isSave = 0;
        $scope.oneFilter = {};
    }

    //暂时移除数组中的元素
    $scope.removePermission = function(filter){
        $scope.permission.filters.splice(filter)
    }

    $scope.isSave = 1; //三种状态：0：未保存，1：刚加载默认状态，2：已经保存
    $scope.disabledBtn = false;
    $scope.addDoctorPermission = function(){
        $scope.disabledBtn = true;
        if(permissionIsValided()){
            doctor.addDoctorPermission(doctor_id,{permission:$scope.permission}).then(function(data){
//                console.log(data);
                $scope.isSave = 2;
                if(data.permission_id !== undefined){
                    $scope.addAlert("success","添加医生权限成功,3秒后自动关闭！");
                    $timeout(function(){
                        $scope.disabledBtn = false;
                        $scope.cancel();
                    }, 3000);
                }
            },function(error){
                $scope.addAlert("success","添加医生权限，请稍后再试！");
                $scope.disabledBtn = false;
            });
        }
    }

    $scope.clearDistrict = function(){
        $scope.permission.district_id = undefined;
        $scope.permission.province_id = undefined;
        $scope.permission.city_id = undefined;
        $scope.permission.zone_id = undefined;
    }

    //getWristbandColumns();
    //getSphygmomanometerColumns();
    //getGlucosemeterColumns();
    //getThermometerColumns();
    //getOximeterColumns();
    //getFatColumns();
    //getComparisonOps();
    //getLogicalOps();

    //Alert
    $scope.alerts = [];
    $scope.addAlert = function (type, msg) {
        var alert = {'type': type, 'msg': msg};
        $scope.alerts.push(alert);
        $timeout(function(){
            $scope.closeAlert($scope.alerts.indexOf(alert));
        }, 3000);
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.clearAlerts = function() {
        $scope.alerts = [];
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

};
