'use strict';

goog.require('jxmnfsec.main.user_center.Ctrl');

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.main.user_center.basics.Ctrl');

jxmnfsec.main.user_center.basics.Ctrl.$inject = [
  "basic",
  "$timeout",
  "$filter",
  "md5",
  "constants"
];

/**
 * user_center.basics controller.
 *
 * @param {angular.$controller} $controller
 * @constructor
 * @export
 * @ngInject
 */
jxmnfsec.main.user_center.basics.Ctrl = function($controller, $q, $scope, $timeout, basic,md5,constants,$filter) {


  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from user_center.basics controller';

  /**
   * Inherit from parent controller. We'd like to call the `log`
   * function here.
   *
   * @type {Object}
   * @nocollapse
   */
  this.parent = $controller(jxmnfsec.main.user_center.Ctrl);

  /**
   * Call parent `log` function.
   */
  this.parent.log('`log` function called from child controller');
  

  $scope.company_departments = constants.gotCOMPANYDEPARTMENTS();
  $scope.company_department = {selected: undefined};

  $scope.device_types = constants.gotDEVICETYPES();
  $scope.device_types_selected = [];

  $scope.company_members_list = constants.gotCOMPANYMEMBERS();
  $scope.company_members = {selected: undefined};

  $scope.company_industry_list = constants.gotCOMPANYINDUSTRIES();
  $scope.company_industry = {selected: undefined};

  $scope.company_nature_list = constants.gotCOMPANYNATURES();
  $scope.company_nature = {selected: undefined};

  $scope.province_list = constants.gotPROVINCES();
  $scope.province = {selected: undefined};

  $scope.city_list = constants.gotCITIES();
  $scope.city_select_list = [];
  $scope.city = {selected: undefined};

  $scope.county_list = constants.gotZONES();
  $scope.county_select_list = [];
  $scope.county = {selected: undefined};


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


    // $scope.company_departments = [];
  // $scope.company_department = {selected: undefined};
  // $scope.device_types = [];
  // $scope.device_types_selected = [];
  // $scope.company_members_list = [];
  // $scope.company_members = {selected: undefined};
  // $scope.company_industry_list = [];
  // $scope.company_industry = {selected: undefined};
  // $scope.company_nature_list = [];
  // $scope.company_nature = {selected: undefined};

  // $scope.province_list = [];
  // $scope.province = {selected: undefined};
  // $scope.city_list = [];
  // $scope.city_select_list = [];
  // $scope.city = {selected: undefined};
  // $scope.county_list = [];
  // $scope.county_select_list = [];
  // $scope.county = {selected: undefined}; 

  $scope.manufactory = {
    contactor: undefined,
    department: undefined,
    telephone: undefined,
    photo: undefined,
    email: undefined,
    full_name: undefined,
    code: undefined,
    profile: undefined,
    province_id: undefined,
    city_id: undefined,
    zone_id: undefined,
    address: undefined,
    device_type_ids: [],
    members: undefined,
    industry: undefined,
    nature: undefined,
    business_licence: '',
    internal_certificate: '',
    local_certificate: '',
    code_certificate: ''
  }


  activate();

  function activate() {
    // var promises = [getCompanyDepartments(), getDeviceTypes(), getCompanyMembers(), 
    //         getCompanyIndustry(), getCompanyNature(), getProvinces(), getCities(), getZones()];
    // return $q.all(promises).then(function() {
    // });
  }

  // function getCompanyDepartments() {
  //   return constants.getCompanyDepartments().then(function (data) {
  //     $scope.company_departments = data.departments;
  //   });
  // }

  //   function getCompanyDepartments() {
  //   return constants.getCompanyDepartments().then(function (data) {
  //     console.log(data);
  //     $scope.company_departments = data.departments;
  //     $scope.company_department = {selected: undefined}
  //     return $scope.company_departments;
  //   });
  // } 

  // function getDeviceTypes() {
  //   return constants.getDeviceTypes().then(function (data) {
  //     $scope.device_types = data.device_types;
  //     $scope.device_types_selected = [];
  //     return $scope.device_types;
  //   });
  // }

  // function getCompanyMembers() {
  //   return constants.getCompanyMembers().then(function (data) {
  //      console.log(data);
  //     $scope.company_members_list = data.members;
  //     $scope.company_members = {selected: undefined}
  //     return $scope.company_members_list;
  //   });
  // }

  // function getCompanyIndustry() {
  //   return constants.getCompanyIndustries().then(function (data) {
  //     $scope.company_industry_list = data.industries;
  //     $scope.company_industry = {selected: undefined}
  //     return $scope.company_industry_list;
  //   });
  // }

  // function getCompanyNature() {
  //   return constants.getCompanyNatures().then(function (data) {
  //      console.log(data);
  //     $scope.company_nature_list = data.natures;
  //     $scope.company_nature = {selected: undefined}
  //     return $scope.company_nature_list;
  //   });
  // }

  // function getProvinces() {
  //   return constants.getProvinces().then(function (data) {
  //      console.log(data);
  //     $scope.province_list = data.provinces;
  //     $scope.province = {selected: undefined}
  //     return $scope.province_list;
  //   });
  // }

  // function getCities() {
  //   return constants.getCities().then(function (data) {
  //     $scope.city_list = data.cities;
  //     $scope.city = {selected: undefined}
  //     return $scope.city_list;
  //   });
  // }

  // function getZones() {
  //   return constants.getZones().then(function (data) {
  //     $scope.county_list = data.zones;
  //     $scope.county = {selected: undefined}
  //     return $scope.county_list;
  //   });
  // }

//  function updateCityList(province_id){
//    $scope.city_select_list = [];
//    $scope.city.selected = undefined;
//    $scope.county.selected = undefined;
//    for(var i = 0; i < $scope.city_list.length; i++){
//      if ($scope.city_list[i].province_id == province_id) {
//        $scope.city_select_list.push($scope.city_list[i]);
//      };
//    }
//  }

//  function updateCountyList(city_id){
//    $scope.county_select_list = [];
//    $scope.county.selected = undefined;
//    for(var i = 0; i < $scope.county_list.length; i++){
//      if ($scope.county_list[i].city_id == city_id) {
//        $scope.county_select_list.push($scope.county_list[i]);
//      };
//    }
//  }

//  $scope.changeProvince=function(){
//    $scope.checkDistrict();
//    updateCityList($scope.manufactory.province_id);
//    //$scope.manufactory.province_id = $scope.province.selected.province_id;
//  }

//  $scope.changeCity=function(){
//    $scope.checkDistrict();
//    updateCountyList($scope.manufactory.city_id);
//    //console.log('city id');
//    //console.log($scope.city.selected.city_id);
//    // $scope.manufactory.city_id = $scope.province.selected.city_id;
//  }

//  $scope.changeCounty=function(){
//    $scope.checkDistrict();
//    //$scope.manufactory.zone_id = $scope.county.selected.zone_id;
//  }


    $scope.updateManufactory = function(){
    $scope.manufactory.device_type_ids = $scope.device_types_selected;
    $scope.manufactory.department = $scope.company_department.selected.department_id;
    $scope.manufactory.members = $scope.company_members.selected.members_id;
    $scope.manufactory.industry = $scope.company_industry.selected.industry_id;
    $scope.manufactory.nature = $scope.company_nature.selected.nature_id;
    $scope.manufactory.province_id = $scope.province.selected.province_id;
    $scope.manufactory.city_id = $scope.city.selected.city_id;
    $scope.manufactory.zone_id = $scope.county.selected.zone_id;

    basic.signUp($scope.manufactory).then(function(res){
      
    }, function(error){
      $scope.errmsg = error.data;
    })
  };




//  $scope.checkContactorName=function(){
//    var node= document.getElementById("contactor_name_group");
//    var helper= document.getElementById("contactor_name_helper");
//      if($scope.manufactory.contactor!=null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//  $scope.checkDepartment=function(){
//    var node= document.getElementById("department_group");
//    var helper= document.getElementById("department_helper");
//      if($scope.manufactory.department != null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//  $scope.checkTelephone=function(){
//    var node= document.getElementById("telephone_group");
//    var helper= document.getElementById("telephone_helper");
//      if($scope.manufactory.telephone!=null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//    $scope.checkMobile=function(){
//    var node= document.getElementById("mobile_group");
//    var helper= document.getElementById("mobile_helper");
//      if($scope.manufactory.phone!=null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//    $scope.checkCompanyName=function(){
//    var node= document.getElementById("company_name_group");
//    var helper= document.getElementById("company_name_helper");
//      if($scope.manufactory.full_name!=null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//  $scope.checkCompanyCode=function(){
//    var node= document.getElementById("company_code_group");
//    var helper= document.getElementById("company_code_helper");
//      if($scope.manufactory.code!=null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//  $scope.checkDistrict=function(){
//    var node= document.getElementById("district_group");
//    var helper= document.getElementById("district_helper");
//      if(($scope.manufactory.province_id!=null)&&($scope.manufactory.city_id!=null)&&($scope.manufactory.zone_id!=null)){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//  $scope.checkAddress=function(){
//    var node= document.getElementById("address_group");
//    var helper= document.getElementById("address_helper");
//      if($scope.manufactory.address!=null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }


//$scope.checkDevices=function(){
//    var node= document.getElementById("devices_group");
//    var helper= document.getElementById("devices_helper");
//      if(($scope.manufactory.device_type_ids!=null)&&($scope.manufactory.device_type_ids.length!=0)){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//  $scope.checkCaptcha=function(){
//    var node= document.getElementById("captcha_group");
//    var helper= document.getElementById("captcha_helper");
//      if($scope.manufactory.captcha!=null){
//        node.className="form-group has-success";
//        helper.innerHTML="已填";
//      }else{
//       node.className="form-group has-error";
//        helper.innerHTML="必填";
//      }
//  }

//  $scope.checkBusinessLicence=function(newValue,oldValue,scope){
//        $scope.$watch('business_licencefile',function(){
//         console.log(newValue);
//         console.log(oldValue);
//
//        var node= document.getElementById("business_licence_group");
//        var helper= document.getElementById("business_licence_helper");
//        console.log("test");
//          if($scope.business_licencefile!=null){
//            node.className="form-group has-success";
//            helper.innerHTML="已填";
//          }else{
//           node.className="form-group has-error";
//            helper.innerHTML="必填";
//          }
//        });
//  }

//   $scope.checkBusinessLicence();

  $scope.getManufactoryBasics = function() {
      return basic.getManufactoryBasics().then(function(data) {
          $scope.changeProvince(data.success_message.province_id);
          $scope.changeCity(data.success_message.city_id);
          $scope.manufactory = data.success_message;
          $scope.manufactory.last_login = $filter('unixTodate')($scope.manufactory.last_login);
//        console.log($scope.manufactory);
      }, function(error) {
        $scope.addAlert("danger", "数据加载异常，请稍后再试！");
      });
    }
    //加载工厂基本数据
  $scope.getManufactoryBasics();


  //保存用户修改基本资料
  $scope.saveManufactoryBasic = function() {

    var promise = [uploadBusiness_licenceFiles(), uploadInternal_certificateFiles(), uploadLocal_certificateFiles(), uploadCode_certificateFiles()];
    return $q.all(promise).then(function() {
      basic.putManufactoryBasic($scope.manufactory).then(function(succ) {
        $scope.addAlert("success", "保存成功！");
      }, function(error) {
        console.log(error);
        $scope.addAlert("danger", "保存失败,请稍后再试！");
      })
    });

  }

  $scope.business_licencefile = undefined; //工商营业执照
  $scope.internal_certificatefile = undefined; //组织机构代码证
  $scope.local_certificatefile = undefined; //税务登记证-国税
  $scope.code_certificatefile = undefined; //税务登记证-地税

  var uploadBusiness_licenceFiles = function() {
    //工商营业执照
    if (validParam($scope.business_licencefile)) {
      return basic.fileUpload($scope.business_licencefile).then(function(res) {
        $scope.manufactory.business_licence = res.success_message[0];
        console.log("上传工商营业执照成功！");
        return res;
      }, function(error) {
        console.log("上传工商营业执照失败！");
        console.log(error);
        return error;
      });
    } else {
      return $q.when(0);
    }
  }

  var uploadInternal_certificateFiles = function() {
    //组织机构代码证
    if (validParam($scope.internal_certificatefile)) {
      return basic.fileUpload($scope.internal_certificatefile).then(function(res) {
        $scope.manufactory.internal_certificate = res.success_message[0];
        console.log("上传组织机构代码证成功！");
        return res;
      }, function(error) {
        console.log("上传组织机构代码证失败！");
        console.log(error);
        return error;
      });
    } else {
      return $q.when(0);
    }
  }

  var uploadLocal_certificateFiles = function() {
    //税务登记证-国税
    if (validParam($scope.local_certificatefile)) {
      return basic.fileUpload($scope.local_certificatefile).then(function(res) {
        $scope.manufactory.local_certificate = res.success_message[0];
        console.log("上传税务登记证-国税成功！");
        return res;
      }, function(error) {
        console.log("上传税务登记证-国税失败！");
        console.log(error);
        return error;
      });
    } else {
      return $q.when(0);
    }
  }

  var uploadCode_certificateFiles = function() {
    //税务登记证-地税
    if (validParam($scope.code_certificatefile)) {
      return basic.fileUpload($scope.code_certificatefile).then(function(res) {
        $scope.manufactory.code_certificate = res.success_message[0];
        console.log("上传税务登记证-地税成功！");
        return res;
      }, function(error) {
        console.log("上传税务登记证-地税失败！");
        console.log(error);
        return error;
      });
    } else {
      return $q.when(0);
    }
  }

  var validParam = function(param) {
    if (param != undefined && param != null && param != "") {
      return true;
    }
    return false;
  }
};
