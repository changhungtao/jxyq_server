'use strict';

goog.provide('jxmnfopn.sign_up.Ctrl');

/**
 * Sign up controller.
 *
 * @constructor
 */
jxmnfopn.sign_up.Ctrl.$inject = [
  "$q",
  '$rootScope',
  '$timeout ',
  '$window',
  'constants',
  'basic',
  'md5'
];
jxmnfopn.sign_up.Ctrl = function(
  $q,
  $rootScope,
  $scope,
  $timeout,
  $window,
  constants,
  basic,
  md5
) {

  var ctrl = this;
  /**
   * @type {Array}
   * @nocollapse
   */
  this.anything = ['x', 'y', 'z'];

  ctrl.company_departments = constants.gotCOMPANYDEPARTMENTS();
  ctrl.company_department = {selected: undefined};

  ctrl.device_types = constants.gotDEVICETYPES();
  ctrl.device_types_selected = [];

  ctrl.company_members_list = constants.gotCOMPANYMEMBERS();
  ctrl.company_members = {selected: undefined};

  ctrl.company_industry_list = constants.gotCOMPANYINDUSTRIES();
  ctrl.company_industry = {selected: undefined};

  ctrl.company_nature_list = constants.gotCOMPANYNATURES();
  ctrl.company_nature = {selected: undefined};

  ctrl.province_list = constants.gotPROVINCES();
  ctrl.province = {selected: undefined};

  ctrl.city_list = constants.gotCITIES();
  ctrl.city_select_list = [];
  ctrl.city = {selected: undefined};

  ctrl.county_list = constants.gotZONES();
  ctrl.county_select_list = [];
  ctrl.county = {selected: undefined};

  ctrl.signUpInf = {
    login_name: '',
    password: '',
    contactor: '',
    department: '',
    telephone: '',
    phone: '',
    email: '',
    full_name: '',
    code: '',
    profile: '',
    logo_url: '',
    province_id: '',
    city_id: '',
    zone_id: '',
    address: '',
    device_type_ids: [],
    members: '',
    industry: '',
    nature: '',
    captcha: '',
    business_licence: '',
    internal_certificate: '',
    local_certificate: '',
    code_certificate: ''
  };
  ctrl.password = '';
  ctrl.confirm_password = '';
  activate();

  function activate() {
    // var promises = [getCompanyDepartments(), getDeviceTypes(), getCompanyMembers(),
    //   getCompanyIndustry(), getCompanyNature(), getProvinces(), getCities(), getZones()
    // ];
    // return $q.all(promises).then(function() {});
  }

  // function getCompanyDepartments() {
  //   return constants.getCompanyDepartments().then(function(data) {
  //     ctrl.company_departments = data.departments;
  //     ctrl.company_department = {
  //       selected: undefined
  //     }
  //     return ctrl.company_departments;
  //   });
  // }

  // function getDeviceTypes() {
  //   return constants.getDeviceTypes().then(function(data) {
  //     ctrl.device_types = data.device_types;
  //     ctrl.device_types_selected = [];
  //     return ctrl.device_types;
  //   });
  // }

  // function getCompanyMembers() {
  //   return constants.getCompanyMembers().then(function(data) {
  //     ctrl.company_members_list = data.members;
  //     ctrl.company_members = {
  //       selected: undefined
  //     }
  //     return ctrl.company_members_list;
  //   });
  // }

  // function getCompanyIndustry() {
  //   return constants.getCompanyIndustries().then(function(data) {
  //     ctrl.company_industry_list = data.industries;
  //     ctrl.company_industry = {
  //       selected: undefined
  //     }
  //     return ctrl.company_industry_list;
  //   });
  // }

  // function getCompanyNature() {
  //   return constants.getCompanyNatures().then(function(data) {
  //     ctrl.company_nature_list = data.natures;
  //     ctrl.company_nature = {
  //       selected: undefined
  //     }
  //     return ctrl.company_nature_list;
  //   });
  // }

  // function getProvinces() {
  //   return constants.getProvinces().then(function(data) {
  //     ctrl.province_list = data.provinces;
  //     ctrl.province = {
  //       selected: undefined
  //     }
  //     return ctrl.province_list;
  //   });
  // }

  // function getCities() {
  //   return constants.getCities().then(function(data) {
  //     ctrl.city_list = data.cities;
  //     ctrl.city = {
  //       selected: undefined
  //     }
  //     return ctrl.city_list;
  //   });
  // }

  // function getZones() {
  //   return constants.getZones().then(function(data) {
  //     ctrl.county_list = data.zones;
  //     ctrl.county = {
  //       selected: undefined
  //     }
  //     return ctrl.county_list;
  //   });
  // }

  function updateCityList(province_id) {
    ctrl.city_select_list = [];
    ctrl.city.selected = undefined;
    ctrl.county.selected = undefined;
    for (var i = 0; i < ctrl.city_list.length; i++) {
      if (ctrl.city_list[i].province_id == province_id) {
        ctrl.city_select_list.push(ctrl.city_list[i]);
      };
    }
  }

  function updateCountyList(city_id) {
    ctrl.county_select_list = [];
    ctrl.county.selected = undefined;
    for (var i = 0; i < ctrl.county_list.length; i++) {
      if (ctrl.county_list[i].city_id == city_id) {
        ctrl.county_select_list.push(ctrl.county_list[i]);
      };
    }
  }

  ctrl.changeProvince = function() {
    ctrl.checkDistrict();
    updateCityList(ctrl.province.selected.province_id);
  }

  ctrl.changeCity = function() {
    ctrl.checkDistrict();
    updateCountyList(ctrl.city.selected.city_id);

  }

  ctrl.changeCounty = function() {
    ctrl.checkDistrict();
  }

  ctrl.captcha_url = $rootScope.endPoint + '/common/sign_in_captcha?d=' + new Date().getTime();
  ctrl.captcha_click = function() {
    var time = new Date().getTime();
    ctrl.captcha_url = $rootScope.endPoint + '/common/sign_in_captcha?d=' + time;
  }

  ctrl.logoUrl = undefined; //logol
  ctrl.businessLicence = undefined; //工商营业执照
  ctrl.internalCertificate = undefined; //组织机构代码证
  ctrl.localCertificate = undefined; //税务登记证(国)
  ctrl.codeCertificate = undefined; //税务登记证(地)

  var uploadLogoUrl = function() {
    if (validParam(ctrl.logoUrl)) {
      return basic.fileUpload(ctrl.logoUrl).then(function(res) {
        ctrl.signUpInf.logo_url = res.success_message[0];
        console.log('上传公司logol成功！');
      }, function(error) {
        alert(error);
      });
    }
  }

  var uploadbusinessLicence = function() {
    if (validParam(ctrl.businessLicence)) {
      return basic.fileUpload(ctrl.businessLicence).then(function(res) {
        ctrl.signUpInf.business_licence = res.success_message[0];
        console.log('上传工商营业执照成功！');
      }, function(error) {
        alert(error);
      });
    }
  }

  var uploadInternalCertificate = function() {
    if (validParam(ctrl.internalCertificate)) {
      return basic.fileUpload(ctrl.internalCertificate).then(function(res) {
        ctrl.signUpInf.internal_certificate = res.success_message[0];
        console.log('上传组织机构代码证成功！');
      }, function(error) {
        alert(error);
      });
    }
  }

  var uploadLocalCertificate = function() {
    if (validParam(ctrl.localCertificate)) {
      return basic.fileUpload(ctrl.localCertificate).then(function(res) {
        ctrl.signUpInf.local_certificate = res.success_message[0];
        console.log('上传税务登记证(国)成功！');
      }, function(error) {
        alert(error);
      });
    }
  }

  var uploadCodeCertificate = function() {
    if (validParam(ctrl.codeCertificate)) {
      return basic.fileUpload(ctrl.codeCertificate).then(function(res) {
        ctrl.signUpInf.code_certificate = res.success_message[0];
        console.log('上传税务登记证(地)成功！');
      }, function(error) {
        alert(error);
      });
    }
  }

  ctrl.signUp = function() {
      if(!validParam(ctrl.signUpInf.login_name)){$scope.addAlert("warning", "用户名必填");return;}
      if(!validParam(ctrl.password)){$scope.addAlert("warning", "密码必填");return;}
      if(!validParam(ctrl.confirm_password)){$scope.addAlert("warning", "确认密码必填");return;}
      if(!validParam(ctrl.signUpInf.contactor)){$scope.addAlert("warning", "姓名必填");return;}
      if(!validParam(ctrl.company_department.selected)){$scope.addAlert("warning", "公司部门必填");return;}
      if(!validParam(ctrl.device_types_selected)){$scope.addAlert("warning", "设备类型必填");return;}
      if(!validParam(ctrl.signUpInf.telephone)){$scope.addAlert("warning", "固定电话必填");return;}
      if(!validParam(ctrl.signUpInf.phone)){$scope.addAlert("warning", "移动电话必填");return;}
      if(!validParam(ctrl.signUpInf.full_name)){$scope.addAlert("warning", "公司名称必填");return;}
      if(!validParam(ctrl.signUpInf.code)){$scope.addAlert("warning", "公司编码必填");return;}
      if(!validParam(ctrl.logoUrl)){$scope.addAlert("warning", "公司商标必填");return;}
      if(!validParam(ctrl.province.selected)){$scope.addAlert("warning", "省必填");return;}
      if(!validParam(ctrl.city.selected)){$scope.addAlert("warning", "市必填");return;}
      if(!validParam(ctrl.county.selected)){$scope.addAlert("warning", "县/区名必填");return;}
      if(!validParam(ctrl.signUpInf.address)){$scope.addAlert("warning", "详细地址必填");return;}
      if(!validParam(ctrl.company_members.selected)){$scope.addAlert("warning", "企业人数必填");return;}
      if(!validParam(ctrl.company_industry.selected)){$scope.addAlert("warning", "公司行业必填");return;}
      if(!validParam(ctrl.company_nature.selected)){$scope.addAlert("warning", "公司性质必填");return;}
      if(!validParam(ctrl.businessLicence)){$scope.addAlert("warning", "工商营业执照必填");return;}
      if(!validParam(ctrl.signUpInf.captcha)){$scope.addAlert("warning", "验证码必填");return;}

      if(!validPic(ctrl.logoUrl,'png|jpg|jpeg|gif')){$scope.addAlert('warning','公司商标请上传png、jpg类型的文件！');return;}
      if(!validPic(ctrl.businessLicence,'png|jpg|jpeg|gif')){$scope.addAlert('warning','工商营业执照请上传png、jpg类型的文件！');return;}
      if(validParam(ctrl.internalCertificate) && !validPic(ctrl.internalCertificate,'png|jpg|jpeg|gif')){$scope.addAlert('warning','组织机构代码证请上传png、jpg类型的文件！');return;}
      if(validParam(ctrl.localCertificate) && !validPic(ctrl.localCertificate,'png|jpg|jpeg|gif')){$scope.addAlert('warning','税务登记证(国)请上传png、jpg类型的文件！');return;}
      if(validParam(ctrl.codeCertificate) && !validPic(ctrl.codeCertificate,'png|jpg|jpeg|gif')){$scope.addAlert('warning','税务登记证(地)请上传png、jpg类型的文件！');return;}

      if(!isTelephone(ctrl.signUpInf.telephone)){
          $scope.addAlert("warning", "固定电话无效");
          return;
      }

      if(!isMobile(ctrl.signUpInf.phone)){
          $scope.addAlert("warning", "移动电话无效");
          return;
      }

    ctrl.signUpInf.device_type_ids = ctrl.device_types_selected;
    ctrl.signUpInf.department = ctrl.company_department.selected.department_id;
    ctrl.signUpInf.members = ctrl.company_members.selected.member_id;
    ctrl.signUpInf.industry = ctrl.company_industry.selected.industry_id;
    ctrl.signUpInf.nature = ctrl.company_nature.selected.nature_id;
    ctrl.signUpInf.password = md5.createHash(ctrl.password);
    ctrl.signUpInf.province_id = ctrl.province.selected.province_id;
    ctrl.signUpInf.city_id = ctrl.city.selected.city_id;
    ctrl.signUpInf.zone_id = ctrl.county.selected.zone_id;

      if(!validParam(ctrl.signUpInf.login_name)){$scope.addAlert("warning", "用户名必填");return;}

    var promises = [uploadLogoUrl(), uploadbusinessLicence(), uploadInternalCertificate(), uploadLocalCertificate(), uploadCodeCertificate()];
    return $q.all(promises).then(function() {
      basic.signUp(ctrl.signUpInf).then(function(res) {
        if (res.success_message !== undefined) {
          $scope.addAlert("success", "注册成功，3秒钟后跳转到登陆页面！");
          ctrl.signUpInf = {};
          $timeout(function() {
            $window.location.href = '/manufactory/open';
          }, 3000);

        } else {
            ctrl.captcha_click();
          $scope.addAlert("danger", res.error_message.faultstring);
        }
      }, function(error) {
        //ctrl.errmsg = error.data;
        $scope.addAlert("danger", error.error_message.faultstring);
      })
    });


  };
  ctrl.checkUserName = function() {
    var node = document.getElementById("user_name_group");
    var helper = document.getElementById("user_name_helper");
    if (ctrl.signUpInf.login_name != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkPassword = function() {
    var node = document.getElementById("password_group");
    var helper = document.getElementById("password_helper");
    if (ctrl.password != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkPasswordRepeat = function() {
    var node = document.getElementById("password_repeat_group");
    var helper = document.getElementById("password_repeat_helper");
    if (ctrl.confirm_password != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }

    if (ctrl.confirm_password != ctrl.password) {
      node.className = "form-group has-error";
      helper.innerHTML = "密码不一致";
    };
  }


  ctrl.checkContactorName = function() {
    var node = document.getElementById("contactor_name_group");
    var helper = document.getElementById("contactor_name_helper");
    if (ctrl.signUpInf.contactor != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkDepartment = function() {
    var node = document.getElementById("department_group");
    var helper = document.getElementById("department_helper");
    if (ctrl.company_department.selected != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkTelephone = function() {
    var node = document.getElementById("telephone_group");
    var helper = document.getElementById("telephone_helper");
    if (ctrl.signUpInf.telephone != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkMobile = function() {
    var node = document.getElementById("mobile_group");
    var helper = document.getElementById("mobile_helper");
    if (ctrl.signUpInf.phone != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkCompanyName = function() {
    var node = document.getElementById("company_name_group");
    var helper = document.getElementById("company_name_helper");
    if (ctrl.signUpInf.full_name != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkCompanyCode = function() {
    var node = document.getElementById("company_code_group");
    var helper = document.getElementById("company_code_helper");
    if (ctrl.signUpInf.code != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkDistrict = function() {
    var node = document.getElementById("district_group");
    var helper = document.getElementById("district_helper");
    if ((ctrl.province.selected != null) && (ctrl.city.selected != null) && (ctrl.county.selected != null)) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkAddress = function() {
    var node = document.getElementById("address_group");
    var helper = document.getElementById("address_helper");
    if (ctrl.signUpInf.address != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }


  ctrl.checkDevices = function() {
    var node = document.getElementById("devices_group");
    var helper = document.getElementById("devices_helper");
    if ((ctrl.device_types_selected != null) && (ctrl.device_types_selected.length != 0)) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkCaptcha = function() {
    var node = document.getElementById("captcha_group");
    var helper = document.getElementById("captcha_helper");
    if (ctrl.signUpInf.captcha != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

  ctrl.checkBusinessLicence = function() {
    var node = document.getElementById("business_licence_group");
    var helper = document.getElementById("business_licence_helper");
    if (ctrl.signUpInf.business_licence != null) {
      node.className = "form-group has-success";
      helper.innerHTML = "已填";
    } else {
      node.className = "form-group has-error";
      helper.innerHTML = "必填";
    }
  }

    //判断是否为有效手机号或座机号
    function isMobileOrTell(str){
        var mobile = /^1\d{10}$/;//手机
        var tel = /^0\d{2,3}-?\d{7,8}$/;
        if(mobile.test(str) || tel.test(str)){
            return true;
        }
        return false;
    }

    function isMobile(str){
        var mobile = /^1\d{10}$/;//手机
        if(mobile.test(str)){
            return true;
        }
        return false;
    }

    function isTelephone(str){
        var tel = /^0\d{2,3}-?\d{7,8}$/;//固定电话
        if(tel.test(str)){
            return true;
        }
        return false;
    }

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

    function validParam(param){
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

};
