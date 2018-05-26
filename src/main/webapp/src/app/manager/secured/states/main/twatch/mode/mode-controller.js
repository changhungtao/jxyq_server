'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.mode.Ctrl');



/**
 * main.twatch.mode controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.twatch.mode.Ctrl.$inject = [
  "$rootScope",
  "$scope",
  "$http",
  "$modal",
  "$filter",
  "$q",
  "i18nService",
  "ModalService",
  "constants",
  "twatch"
];
jxmgrsec.main.twatch.mode.Ctrl = function(
  $rootScope,
  $scope, 
  $http, 
  $modal, 
  $filter, 
  $q, 
  i18nService, 
  ModalService, 
  constants, 
  twatch
) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.twatch.mode controller';
  var ctrl = this;

  $scope.work_modes = constants.gotWORKMODES();
  $scope.work_types = constants.gotWORKTYPES();
  $scope.alarm_events = constants.gotALARMEVENTS();
  console.log($scope.work_modes);
  console.log($scope.work_types);
  console.log($scope.alarm_events);

  $scope.savemode = {
    imei: $rootScope.imei,
    defaultmode: undefined
  }

  var getDefaultmode = function() {
    if ($scope.mode.defaulttype_selected == 2) {
      $scope.savemode.defaultmode = 5;
    } else if ($scope.mode.defaulttype_selected == 1) {
      $scope.savemode.defaultmode = $scope.mode.defaultmode_selected;
    }

  }

  $scope.saveModeSet = function() {
    $scope.savemode.imei = $rootScope.imei;
    getDefaultmode();
    twatch.setModeset($scope.savemode).then(function(res) {
      $scope.queryModeSet();
    }, function(error) {
      console.log(error);
    });
  }

  $scope.query = {
    imei: $rootScope.imei
  }

  $scope.jsonImei = {
    imei: $rootScope.imei
  }
  $scope.queryModeSet = function() {
    return twatch.getModeset($scope.jsonImei).then(function(res) {
      console.log(res);
      //$scope.mode.defaultmode_selected = res.defaultmode;
      $scope.gridPieroids.data = res.periods;
//      $rootScope.imei = $scope.jsonImei.imei;
      $rootScope.imeiChanged($scope.jsonImei.imei);
      if (5 == res.defaultmode) {
        $scope.mode.defaulttype_selected = 2;
        $scope.mode.defaultmode_selected = 4;
      } else {
        $scope.mode.defaulttype_selected = 1;
        $scope.mode.defaultmode_selected = res.defaultmode;
      }
    }, function(error) {
      console.log(error);
    });
  }
  $scope.queryImei = function() {
    $scope.jsonImei.imei = $scope.query.imei;
    $scope.queryModeSet();
    $scope.getWhite();
    $scope.getBlack();
  };

  i18nService.setCurrentLang('zh-CN');
  $scope.gridPieroids = {
    paginationPageSizes: [25, 50, 75],
    paginationPageSize: 25,
    /*paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
    useExternalSorting: true,*/
    columnDefs: [{
      field: 'period_id',
      name: '时段编号',
      width: '10%',
      pinnedLeft: true
    }, {
      field: 'start',
      name: '开始时间',
      width: '20%'
    }, {
      field: 'stop',
      name: '结束时间',
      width: '20%',
    }, {
      field: 'weeks',
      name: '星期几',
      width: '10%',
      cellFilter: 'mapWeekDays'
    }, {
      field: 'runmode',
      name: '时段模式',
      width: '20%',
      cellFilter: 'mapTwatchWorkMode'
    }, {
      name: '操作',
      width: '20%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.modifyPieroid(row.entity)" >修改</button></div><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.deletePieroid(row.entity)">删除</button></div></div>'
    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
    }
  };


  /* $scope.gridPieroids.data = [{
     'pieroid_id': '1',
     'start': '08:00',
     'stop': '19:00',
     'weeks': '3',
     'runmode': 'GPS模式'

   }, {
     'pieroid_id': '2',
     'start': '11:00',
     'stop': '12:00',
     'weeks': '1',
     'runmode': 'GPS模式'
   }, {
     'pieroid_id': '3',
     'start': '11:00',
     'stop': '12:00',
     'weeks': '3',
     'runmode': 'GPS模式'
   }];*/


  $scope.modifyPieroid = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/modify_mode_pieroid_modal.html',
      controller: 'ModifyModePieroidModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.queryModeSet();
    });
  }

  $scope.deletePieroid = function(entity) {

    var deleteperiod = {
      imei: $scope.jsonImei.imei,
      period_id: entity.period_id
    }
    twatch.deleteModePeriods(deleteperiod).then(function(res) {
      console.log(res);
      $scope.queryModeSet();
    }, function(error) {
      console.log(error);
    });

    /*var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/delete_mode_pieroid_modal.html',
      controller: 'DeleteModePieroidModalCtrl',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      refreshMode();
    });*/
  }

  ctrl.addPieroid = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/add_mode_pieroid_modal.html',
      controller: 'AddModePieroidModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.queryModeSet();
    });
  }

  //白名单

  $scope.getWhite = function() {
    twatch.getWhiteList($scope.jsonImei).then(function(res) {
      console.log(res);
      $scope.gridWhiteList.data = res.whitelist;
    }, function(error) {
      console.log(error);
    });
  }
  //$scope.getWhite();

  $scope.gridWhiteList = {
    paginationPageSizes: [25, 50, 75],
    paginationPageSize: 25,
    // paginationPageSizes: [10, 20, 50, 100],
    // paginationPageSize: 10,
    // useExternalPagination: true,
    // useExternalSorting: true,
    columnDefs: [{
      field: 'name',
      name: '姓名',
      width: '33%',
      pinnedLeft: true
    }, {
      field: 'number',
      name: '号码',
      width: '33%'
    }, {
      name: '操作',
      width: '34%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.modifyWhiteName(row.entity)" >修改</button></div><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.deleteWhiteName(row.entity)">删除</button></div></div>'
    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
    }
  };

  /* $scope.gridWhiteList.data = [{
     'name': '姓名1',
     'number': '18200000001'

   }, {
     'name': '姓名2',
     'number': '18200000002'
   }, {
     'name': '姓名3',
     'number': '18200000003'
   }];*/

  $scope.modifyWhiteName = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/modify_mode_whitename_modal.html',
      controller: 'ModifyModeWhitenameModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        griddata: function() {
          return $scope.gridWhiteList.data;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.getWhite();
    });
  }
  $scope.deleteWhiteName = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/delete_mode_whitename_modal.html',
      controller: 'DeleteModeWhitenameModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        griddata: function() {
          return $scope.gridWhiteList.data;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.getWhite();
    });

  }
  ctrl.addWhiteName = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/add_mode_whitename_modal.html',
      controller: 'AddModeWhitenameModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.getWhite();
    });

  }

  //黑名单

  $scope.getBlack = function() {
    twatch.getBlackList($scope.jsonImei).then(function(res) {
      console.log(res);
      $scope.gridBlackList.data = res.blacklist;
    }, function(error) {
      console.log(error);
    });
  }
  //$scope.getBlack();

  $scope.gridBlackList = {
    paginationPageSizes: [25, 50, 75],
    paginationPageSize: 25,
    /*paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
    useExternalSorting: true,*/
    columnDefs: [{
      field: 'name',
      name: '姓名',
      width: '33%',
      pinnedLeft: true
    }, {
      field: 'number',
      name: '号码',
      width: '33%'
    }, {
      name: '操作',
      width: '34%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.modifyBlackName(row.entity)" >修改</button></div><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.deleteBlackName(row.entity)">删除</button></div></div>'
    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
    }
  };

  /*$scope.gridBlackList.data = [{
    'name': '姓名4',
    'number': '18200000004'

  }, {
    'name': '姓名5',
    'number': '18200000005'
  }, {
    'name': '姓名6',
    'number': '18200000006'
  }];*/


  $scope.modifyBlackName = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/modify_mode_blackname_modal.html',
      controller: 'ModifyModeBlacknameModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        griddata: function() {
          return $scope.gridBlackList.data;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.getBlack();
    });

  }
  $scope.deleteBlackName = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/delete_mode_blackname_modal.html',
      controller: 'DeleteModeBlacknameModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        griddata: function() {
          return $scope.gridBlackList.data;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.getBlack();
    });

  }
  ctrl.addBlackName = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/mode_modal/add_mode_blackname_modal.html',
      controller: 'AddModeBlacknameModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.getBlack();
    });
  }


  // ctrl.defaultmode = [{
  //   "mode_id": "1",
  //   "mode_name": "家长"
  // }, {
  //   "mode_id": "2",
  //   "mode_name": "GPS模式"
  // }, {
  //   "mode_id": "3",
  //   "mode_name": "儿童模式"
  // }, {
  //   "mode_id": "4",
  //   "mode_name": "校园模式"
  // }, {
  //   "mode_id": "5",
  //   "mode_name": "上课隐身"
  // }];
  ctrl.defaultmode_selected = {
    selected: 0,
    defaulttype_selected: 0
  };


  // $scope.queryImei();
};
