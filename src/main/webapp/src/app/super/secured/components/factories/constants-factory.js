'use strict';

goog.provide('jxsprsec.constants.Factory');

/**
 * constants Factory. 
 *
 * @constructor
 */
jxsprsec.constants.Factory.$inject = [
  '$rootScope',
  '$http', 
  '$q'
];
jxsprsec.constants.Factory = function($rootScope,$http, $q) {

  /**
   * 通用函数
   */
  function getConstants(req) {
    req.url = $rootScope.endPoint + req.url;
    return $http(req)
      .then(success)
      .catch(fail);

    function success(response) {
      return response.data.success_message;
    }

    function fail(error) {
    return $q.reject("rejected");
    }
  };

  /**
   * 区域常量
   */
  //中国传统地理大区
  var DISTRICTS = [];
  var DISTRICTSPromise = getDistricts().then(
    function(data){
      DISTRICTS = data.districts;
    }, 
    function(data){
      DISTRICTS = [];
  });
  function getDistricts(){
    var req = {
      method: 'GET',
      url: '/common/constants/districts'
    }
    return getConstants(req);
  };
  function gotDISTRICTS(){
    return DISTRICTS;
  };

  //省级行政区
  var PROVINCES = [];
  var PROVINCESPromise = getProvinces().then(
    function(data){
      PROVINCES = data.provinces;
    }, 
    function(data){
      PROVINCES = [];
  });
  function getProvinces(){
    var req = {
      method: 'GET',
      url: '/common/constants/provinces'
    }
    return getConstants(req);
  };
  function gotPROVINCES(){
    return PROVINCES;
  };

  //市级行政区
  var CITIES = [];
  var CITIESPromise = getCities().then(
    function(data){
      CITIES = data.cities;
    }, 
    function(data){
      CITIES = [];
  });
  function getCities(){
    var req = {
      method: 'GET',
      url: '/common/constants/cities'
    }
    return getConstants(req);
  };
  function gotCITIES(){
    return CITIES;
  };

  //区县行政区
  var ZONES = [];
  var ZONESPromise = getZones().then(
    function(data){
      ZONES = data.zones;
    }, 
    function(data){
      ZONES = [];
  });
  function getZones(){
    var req = {
      method: 'GET',
      url: '/common/constants/zones'
    }
    return getConstants(req);
  };
  function gotZONES(){
    return ZONES;
  };

  /**
   * 产品常量
   */
  //产品类型
  var PRODUCTTYPES = [];
  var PRODUCTTYPESPromise = getProductTypes().then(
    function(data){
      PRODUCTTYPES = data.product_types;
    }, 
    function(data){
      PRODUCTTYPES = [];
  });
  function getProductTypes(){
    var req = {
      method: 'GET',
      url: '/common/constants/product_types'
    }
    return getConstants(req);
  };
  function gotPRODUCTTYPES(){
    return PRODUCTTYPES;
  };

  //设备类型
  var DEVICETYPES = [];
  var DEVICETYPESPromise = getDeviceTypes().then(
    function(data){
      DEVICETYPES = data.device_types;
    }, 
    function(data){
      DEVICETYPES = [];
  });
  function getDeviceTypes(){
    var req = {
      method: 'GET',
      url: '/common/constants/device_types'
      // headers: {
      //   'Content-Type': undefined
      // },
      // data: { 
      //   test: 'test'
      // }
    }
    return getConstants(req);
  };
  function gotDEVICETYPES(){
    return DEVICETYPES;
  };

  /**
   * 健康常量
   */
  //健康数据类型
  var DATATYPES = [];
  var DATATYPESPromise = getDataTypes().then(
    function(data){
      DATATYPES = data.data_types;
    }, 
    function(data){
      DATATYPES = [];
  });
  function getDataTypes(){
    var req = {
      method: 'GET',
      url: '/common/constants/data_types'
    }
    return getConstants(req);
  };
  function gotDATATYPES(){
    return DATATYPES;
  };

  //健康档案类型
  var FILETYPES = [];
  var FILETYPESPromise = getFileTypes().then(
    function(data){
      FILETYPES = data.file_types;
    }, 
    function(data){
      FILETYPES = [];
  });
  function getFileTypes(){
    var req = {
      method: 'GET',
      url: '/common/constants/file_types'
    }
    return getConstants(req);
  };
  function gotFILETYPES(){
    return FILETYPES;
  };

  //手环数据列名
  var WRISTBANDCOLUMNS = [];
  var WRISTBANDCOLUMNSPromise = getWristbandColumns().then(
    function(data){
      WRISTBANDCOLUMNS = data.columns;
    }, 
    function(data){
      WRISTBANDCOLUMNS = [];
  });
  function getWristbandColumns(){
    var req = {
      method: 'GET',
      url: '/common/constants/wristband_columns'
    }
    return getConstants(req);
  };
  function gotWRISTBANDCOLUMNS(){
    return WRISTBANDCOLUMNS;
  };

  //血压数据列名
  var SPHYGMOMANOMETERCOLUMNS = [];
  var SPHYGMOMANOMETERCOLUMNSPromise = getSphygmomanometerColumns().then(
    function(data){
      SPHYGMOMANOMETERCOLUMNS = data.columns;
    }, 
    function(data){
      SPHYGMOMANOMETERCOLUMNS = [];
  });
  function getSphygmomanometerColumns(){
    var req = {
      method: 'GET',
      url: '/common/constants/sphygmomanometer_columns'
    }
    return getConstants(req);
  };
  function gotSPHYGMOMANOMETERCOLUMNS(){
    return SPHYGMOMANOMETERCOLUMNS;
  };

  //血糖数据列名
  var GLUCOSEMETERCOLUMNS = [];
  var GLUCOSEMETERCOLUMNSPromise = getGlucosemeterColumns().then(
    function(data){
      GLUCOSEMETERCOLUMNS = data.columns;
    }, 
    function(data){
      GLUCOSEMETERCOLUMNS = [];
  });
  function getGlucosemeterColumns(){
    var req = {
      method: 'GET',
      url: '/common/constants/glucosemeter_columns'
    }
    return getConstants(req);
  };
  function gotGLUCOSEMETERCOLUMNS(){
    return GLUCOSEMETERCOLUMNS;
  };

  //体温数据列名
  var THERMOMETERCOLUMNS = [];
  var THERMOMETERCOLUMNSPromise = getThermometerColumns().then(
    function(data){
      THERMOMETERCOLUMNS = data.columns;
    }, 
    function(data){
      THERMOMETERCOLUMNS = [];
  });
  function getThermometerColumns(){
    var req = {
      method: 'GET',
      url: '/common/constants/thermometer_columns'
    }
    return getConstants(req);
  };
  function gotTHERMOMETERCOLUMNS(){
    return THERMOMETERCOLUMNS;
  };

  //血氧数据列名
  var OXIMETERCOLUMNS = [];
  var OXIMETERCOLUMNSPromise = getOximeterColumns().then(
    function(data){
      OXIMETERCOLUMNS = data.columns;
    }, 
    function(data){
      OXIMETERCOLUMNS = [];
  });
  function getOximeterColumns(){
    var req = {
      method: 'GET',
      url: '/common/constants/oximeter_columns'
    }
    return getConstants(req);
  };
  function gotOXIMETERCOLUMNS(){
    return OXIMETERCOLUMNS;
  };

  //脂肪数据列名
  var FATCOLUMNS = [];
  var FATCOLUMNSPromise = getFatColumns().then(
    function(data){
      FATCOLUMNS = data.columns;
    }, 
    function(data){
      FATCOLUMNS = [];
  });
  function getFatColumns(){
    var req = {
      method: 'GET',
      url: '/common/constants/fat_columns'
    }
    return getConstants(req);
  };
  function gotFATCOLUMNS(){
    return FATCOLUMNS;
  };

  //比较运算符
  var COMPARISONOPS = [];
  var COMPARISONOPSPromise = getComparisonOps().then(
    function(data){
      COMPARISONOPS = data.ops;
    }, 
    function(data){
      COMPARISONOPS = [];
  });
  function getComparisonOps(){
    var req = {
      method: 'GET',
      url: '/common/constants/comparison_ops'
    }
    return getConstants(req);
  };
  function gotCOMPARISONOPS(){
    return COMPARISONOPS;
  };

  //逻辑运算符
  var LOGICALOPS = [];
  var LOGICALOPSPromise = getLogicalOps().then(
    function(data){
      LOGICALOPS = data.ops;
    }, 
    function(data){
      LOGICALOPS = [];
  });
  function getLogicalOps(){
    var req = {
      method: 'GET',
      url: '/common/constants/logical_ops'
    }
    return getConstants(req);
  };
  function gotLOGICALOPS(){
    return LOGICALOPS;
  };

  //测量时段
  var PERIODS = [];
  var PERIODSPromise = getPeriods().then(
    function(data){
      PERIODS = data.periods;
    }, 
    function(data){
      PERIODS = [];
  });
  function getPeriods(){
    var req = {
      method: 'GET',
      url: '/common/constants/periods'
    }
    return getConstants(req);
  };
  function gotPERIODS(){
    return PERIODS;
  };

  /**
   * 状态常量
   */
  //用户状态
  var USERSTATUS = [];
  var USERSTATUSPromise = getUserStatus().then(
    function(data){
      USERSTATUS = data.user_status_list;
    }, 
    function(data){
      USERSTATUS = [];
  });
  function getUserStatus(){
    var req = {
      method: 'GET',
      url: '/common/constants/user_status'
    }
    return getConstants(req);
  };
  function gotUSERSTATUS(){
    return USERSTATUS;
  };

  //厂商状态
  var MANUFACTORYSTATUS = [];
  var MANUFACTORYSTATUSPromise = getManufactoryStatus().then(
    function(data){
      MANUFACTORYSTATUS = data.manufactory_status_list;
    }, 
    function(data){
      MANUFACTORYSTATUS = [];
  });
  function getManufactoryStatus(){
    var req = {
      method: 'GET',
      url: '/common/constants/manufactory_status'
    }
    return getConstants(req);
  };
  function gotMANUFACTORYSTATUS(){
    return MANUFACTORYSTATUS;
  };

  //终端状态
  var TERMINALSTATUS = [];
  var TERMINALSTATUSPromise = getTerminalStatus().then(
    function(data){
      TERMINALSTATUS = data.terminal_status_list;
    }, 
    function(data){
      TERMINALSTATUS = [];
  });
  function getTerminalStatus(){
    var req = {
      method: 'GET',
      url: '/common/constants/terminal_status'
    }
    return getConstants(req);
  };
  function gotTERMINALSTATUS(){
    return TERMINALSTATUS;
  };

  //医生状态
  var DOCTORSTATUS = [];
  var DOCTORSTATUSPromise = getDoctorStatus().then(
    function(data){
      DOCTORSTATUS = data.doctor_status_list;
    }, 
    function(data){
      DOCTORSTATUS = [];
  });
  function getDoctorStatus(){
    var req = {
      method: 'GET',
      url: '/common/constants/doctor_status'
    }
    return getConstants(req);
  };
  function gotDOCTORSTATUS(){
    return DOCTORSTATUS;
  };

  //健康咨询状态
  var HEALTHCONSULTATIONSTATUS = [];
  var HEALTHCONSULTATIONSTATUSPromise = getHealthConsultationStatus().then(
    function(data){
      HEALTHCONSULTATIONSTATUS = data.consultation_status_list;
    }, 
    function(data){
      HEALTHCONSULTATIONSTATUS = [];
  });
  function getHealthConsultationStatus(){
    var req = {
      method: 'GET',
      url: '/common/constants/health_consultation_status'
    }
    return getConstants(req);
  };
  function gotHEALTHCONSULTATIONSTATUS(){
    return HEALTHCONSULTATIONSTATUS;
  };

  //健康数据状态
  var HEALTHDATASTATUS = [];
  var HEALTHDATASTATUSPromise = getHealthDataStatus().then(
    function(data){
      HEALTHDATASTATUS = data.data_status_list;
    }, 
    function(data){
      HEALTHDATASTATUS = [];
  });
  function getHealthDataStatus(){
    var req = {
      method: 'GET',
      url: '/common/constants/health_data_status'
    }
    return getConstants(req);
  };
  function gotHEALTHDATASTATUS(){
    return HEALTHDATASTATUS;
  };

  //健康档案状态
  var HEALTHFILESTATUS = [];
  var HEALTHFILESTATUSPromise = getHealthFileStatus().then(
    function(data){
      HEALTHFILESTATUS = data.file_status_list;
    }, 
    function(data){
      HEALTHFILESTATUS = [];
  });
  function getHealthFileStatus(){
    var req = {
      method: 'GET',
      url: '/common/constants/health_file_status'
    }
    return getConstants(req);
  };
  function gotHEALTHFILESTATUS(){
    return HEALTHFILESTATUS;
  };

  /**
   * 公司常量
   */
  //公司部门
  var COMPANYDEPARTMENTS = [];
  var COMPANYDEPARTMENTSPromise = getCompanyDepartments().then(
    function(data){
      COMPANYDEPARTMENTS = data.departments;
    }, 
    function(data){
      COMPANYDEPARTMENTS = [];
  });
  function getCompanyDepartments(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_departments'
      // headers: {
      //   'Content-Type': undefined
      // },
      // data: { 
      //   test: 'test'
      // }
    }
    return getConstants(req);
  };
  function gotCOMPANYDEPARTMENTS(){
    return COMPANYDEPARTMENTS;
  };

  //公司规模
  var COMPANYMEMBERS = [];
  var COMPANYMEMBERSPromise = getCompanyMembers().then(
    function(data){
      COMPANYMEMBERS = data.members;
    }, 
    function(data){
      COMPANYMEMBERS = [];
  });
  function getCompanyMembers(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_members'
    }
    return getConstants(req);
  };
  function gotCOMPANYMEMBERS(){
    return COMPANYMEMBERS;
  };

  //公司性质
  var COMPANYNATURES = [];
  var COMPANYNATURESPromise = getCompanyNatures().then(
    function(data){
      COMPANYNATURES = data.natures;
    }, 
    function(data){
      COMPANYNATURES = [];
  });
  function getCompanyNatures(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_natures'
    }
    return getConstants(req);
  };
  function gotCOMPANYNATURES(){
    return COMPANYNATURES;
  };

  //公司行业
  var COMPANYINDUSTRIES = [];
  var COMPANYINDUSTRIESPromise = getCompanyIndustries().then(
    function(data){
      COMPANYINDUSTRIES = data.industries;
    }, 
    function(data){
      COMPANYINDUSTRIES = [];
  });
  function getCompanyIndustries(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_industries'
    }
    return getConstants(req);
  };
  function gotCOMPANYINDUSTRIES(){
    return COMPANYINDUSTRIES;
  };

  /**
   * 其他常量
   */
  //医院科室
  var DEPARTMENTS = [];
  var DEPARTMENTSPromise = getDepartments().then(
    function(data){
      DEPARTMENTS = data.departments;
    }, 
    function(data){
      DEPARTMENTS = [];
  });
  function getDepartments(){
    var req = {
      method: 'GET',
      url: '/common/constants/departments'
    }
    return getConstants(req);
  };
  function gotDEPARTMENTS(){
    return DEPARTMENTS;
  };

  //性别
  var GENDERS = [];
  var GENDERSPromise = getGenders().then(
    function(data){
      GENDERS = data.genders;
    }, 
    function(data){
      GENDERS = [];
  });
  function getGenders(){
    var req = {
      method: 'GET',
      url: '/common/constants/genders'
    }
    return getConstants(req);
  };
  function gotGENDERS(){
    return GENDERS;
  };
  //角色
  var USERROLES = [];
  var USERROLESPromise = getUserRoles().then(
      function(data){
          USERROLES = data.role_list;
      },
      function(data){
          USERROLES = [];
      });
  function getUserRoles(){
      var req = {
          method: 'GET',
          url: '/common/constants/user_roles'
      }
      return getConstants(req);
  }
  function gotUSERROLES(){
      return USERROLES;
  }
  //事件
  var OPERATIONS = [];
  var OPERATIONSPromise = getOperations().then(
      function(data){
          OPERATIONS = data.title_list;
      },
      function(data){
          OPERATIONS = [];
      });
  function getOperations(){
      var req = {
          method: 'GET',
          url: '/common/constants/op_titles'
      }
      return getConstants(req);
  }
  function gotOPERATIONS(){
      return OPERATIONS;
  }

  var service = {
    getConstants: getConstants,

    getDistricts: getDistricts,
    gotDISTRICTS: gotDISTRICTS,
    DISTRICTSPromise: DISTRICTSPromise,

    getProvinces: getProvinces,
    gotPROVINCES: gotPROVINCES,
    PROVINCESPromise: PROVINCESPromise,

    getCities: getCities,
    gotCITIES: gotCITIES,
    CITIESPromise: CITIESPromise,

    getZones: getZones,
    gotZONES: gotZONES,
    ZONESPromise: ZONESPromise,

    getProductTypes: getProductTypes,
    gotPRODUCTTYPES: gotPRODUCTTYPES,
    PRODUCTTYPESPromise: PRODUCTTYPESPromise,

    getDeviceTypes: getDeviceTypes,
    gotDEVICETYPES: gotDEVICETYPES,
    DEVICETYPESPromise: DEVICETYPESPromise,

    getDataTypes: getDataTypes,
    gotDATATYPES: gotDATATYPES,
    DATATYPESPromise: DATATYPESPromise,

    getFileTypes: getFileTypes,
    gotFILETYPES: gotFILETYPES,
    FILETYPESPromise: FILETYPESPromise,

    getWristbandColumns: getWristbandColumns,
    gotWRISTBANDCOLUMNS: gotWRISTBANDCOLUMNS,
    WRISTBANDCOLUMNSPromise: WRISTBANDCOLUMNSPromise,

    getSphygmomanometerColumns: getSphygmomanometerColumns,
    gotSPHYGMOMANOMETERCOLUMNS: gotSPHYGMOMANOMETERCOLUMNS,
    SPHYGMOMANOMETERCOLUMNSPromise: SPHYGMOMANOMETERCOLUMNSPromise,

    getGlucosemeterColumns: getGlucosemeterColumns,
    gotGLUCOSEMETERCOLUMNS: gotGLUCOSEMETERCOLUMNS,
    GLUCOSEMETERCOLUMNSPromise: GLUCOSEMETERCOLUMNSPromise,

    getThermometerColumns: getThermometerColumns,
    gotTHERMOMETERCOLUMNS: gotTHERMOMETERCOLUMNS,
    THERMOMETERCOLUMNSPromise: THERMOMETERCOLUMNSPromise,

    getOximeterColumns: getOximeterColumns,
    gotOXIMETERCOLUMNS: gotOXIMETERCOLUMNS,
    OXIMETERCOLUMNSPromise: OXIMETERCOLUMNSPromise,

    getFatColumns: getFatColumns,
    gotFATCOLUMNS: gotFATCOLUMNS,
    FATCOLUMNSPromise: FATCOLUMNSPromise,

    getComparisonOps: getComparisonOps,
    gotCOMPARISONOPS: gotCOMPARISONOPS,
    COMPARISONOPSPromise: COMPARISONOPSPromise,

    getLogicalOps: getLogicalOps,
    gotLOGICALOPS: gotLOGICALOPS,
    LOGICALOPSPromise: LOGICALOPSPromise,

    getPeriods: getPeriods,
    gotPERIODS: gotPERIODS,
    PERIODSPromise: PERIODSPromise,

    getUserStatus: getUserStatus,
    gotUSERSTATUS: gotUSERSTATUS,
    USERSTATUSPromise: USERSTATUSPromise,

    getManufactoryStatus: getManufactoryStatus,
    gotMANUFACTORYSTATUS: gotMANUFACTORYSTATUS,
    MANUFACTORYSTATUSPromise: MANUFACTORYSTATUSPromise,

    getTerminalStatus: getTerminalStatus,
    gotTERMINALSTATUS: gotTERMINALSTATUS,
    TERMINALSTATUSPromise: TERMINALSTATUSPromise,

    getDoctorStatus: getDoctorStatus,
    gotDOCTORSTATUS: gotDOCTORSTATUS,
    DOCTORSTATUSPromise: DOCTORSTATUSPromise,

    getHealthConsultationStatus: getHealthConsultationStatus,
    gotHEALTHCONSULTATIONSTATUS: gotHEALTHCONSULTATIONSTATUS,
    HEALTHCONSULTATIONSTATUSPromise: HEALTHCONSULTATIONSTATUSPromise,

    getHealthDataStatus: getHealthDataStatus,
    gotHEALTHDATASTATUS: gotHEALTHDATASTATUS,
    HEALTHDATASTATUSPromise: HEALTHDATASTATUSPromise,

    getHealthFileStatus: getHealthFileStatus,
    gotHEALTHFILESTATUS: gotHEALTHFILESTATUS,
    HEALTHFILESTATUSPromise: HEALTHFILESTATUSPromise,

    getCompanyDepartments: getCompanyDepartments,
    gotCOMPANYDEPARTMENTS: gotCOMPANYDEPARTMENTS,
    COMPANYDEPARTMENTSPromise: COMPANYDEPARTMENTSPromise,

    getCompanyMembers: getCompanyMembers,
    gotCOMPANYMEMBERS: gotCOMPANYMEMBERS,
    COMPANYMEMBERSPromise: COMPANYMEMBERSPromise,

    getCompanyNatures: getCompanyNatures,
    gotCOMPANYNATURES: gotCOMPANYNATURES,
    COMPANYNATURESPromise: COMPANYNATURESPromise,

    getCompanyIndustries: getCompanyIndustries,
    gotCOMPANYINDUSTRIES: gotCOMPANYINDUSTRIES,
    COMPANYINDUSTRIESPromise: COMPANYINDUSTRIESPromise,

    getDepartments: getDepartments,
    gotDEPARTMENTS: gotDEPARTMENTS,
    DEPARTMENTSPromise: DEPARTMENTSPromise,

    getGenders: getGenders,
    gotGENDERS: gotGENDERS,
    GENDERSPromise: GENDERSPromise,

    getUserRoles: getUserRoles,
    gotUSERROLES: gotUSERROLES,
    USERROLESPromise: USERROLESPromise,

    getOperations: getOperations,
    gotOPERATIONS: gotOPERATIONS,
    OPERATIONSPromise: OPERATIONSPromise
  };
  return service;
};
