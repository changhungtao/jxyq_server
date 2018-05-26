'use strict';
/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.permission_list.Ctrl');
/**
 * main.health_data.permission_list controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.health_data.permission_list.Ctrl.$inject = [
  "$scope",
  "$http",
  '$modal',
  "$state",
  "$q",
  "$interval",
  "i18nService",
  'health_data',
  "constants"
];
jxdctsec.main.health_data.permission_list.Ctrl = function(
  $scope, 
  $http, 
  $modal, 
  $state, 
  $q,
  $interval,
  i18nService, 
  health_data, 
  constants,
  uiGridGroupingConstants
) {

  var ctrl = this;
  i18nService.setCurrentLang('zh-cn');

  //   $scope.show=function(entity){
  //       console.log("sss");
  //       console.log(entity);
  //      switch (entity.file_type) {
  //      case "血压类数据":
  //        $state.go('main.health_data.sphygmomanometer_datas', {"entity":entity});
  //        break;
  //      case "血糖类数据":
  //        $state.go('main.health_data.glucosemeter_datas', {"entity":entity});
  //        break;
  //      case "脂肪类数据":
  //        $state.go('main.health_data.fat_datas', {});
  //        break;
  //      case "体温类数据":
  //        $state.go('main.health_data.thermometer_datas', {"entity":entity});
  //        break;
  //      case "手环类数据":
  //        $state.go('main.health_data.wristband_datas', {"entity":entity});
  //        break;
  //      case "血氧类数据":
  //        $state.go('main.health_data.oximeter_datas', {"entity":entity});
  //        break;
  //      };
  //
  //  };

  $scope.show = function(entity) {
    //        console.log("sss");
    //        console.log(entity);
    switch (entity.data_type) {
      case 2: //血压类数据
        $state.go('main.health_data.sphygmomanometer_datas', {
          "permission_id": entity.permission_id,
          "permission_name": entity.permission_name
        });
        break;
      case 4: //血糖类数据
        $state.go('main.health_data.glucosemeter_datas', {
          "permission_id": entity.permission_id,
          "permission_name": entity.permission_name
        });
        break;
      case 6: //脂肪类数据
        $state.go('main.health_data.fat_datas', {
          "permission_id": entity.permission_id,
          "permission_name": entity.permission_name
        });
        break;
      case 5: //体温类数据
        $state.go('main.health_data.thermometer_datas', {
          "permission_id": entity.permission_id,
          "permission_name": entity.permission_name
        });
        break;
      case 1: //手环类数据
        $state.go('main.health_data.wristband_datas', {
          "permission_id": entity.permission_id,
          "permission_name": entity.permission_name
        });
        break;
      case 3: //血氧类数据
        $state.go('main.health_data.oximeter_datas', {
          "permission_id": entity.permission_id,
          "permission_name": entity.permission_name
        });
        break;
    };

  };
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.health_data.permission_list controller';
  ctrl.dataTypes = constants.gotDATATYPES();

  activate();

  function activate() {
    // var promises = [getDataTypes()];
    // return $q.all(promises).then(function() {

    // });
  }

  // function getDataTypes() {
  //   return constants.getDataTypes().then(function(data) {
  //     ctrl.dataTypes = data.data_types;
  //     console.log(ctrl.dataTypes);
  //     return ctrl.dataTypes;
  //   });
  // }

  $scope.datatype = {
    permission_name: undefined,
    data_type: undefined,
    page_size: 10,
    current_page: 0,
    query_date: undefined
  }

  $scope.getPermissionList = function() {
    health_data.getPermissions($scope.datatype).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.permission_list;
      $scope.datatype.query_date = res.query_date;
      $scope.gridOptions.totalItems = res.total_count;
    }, function(error) {
      console.log(error);
    });
  }
  $scope.getPermissionList();


  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    treeRowHeaderAlwaysVisible: false,
//    enableFiltering: true,
    columnDefs: [{
      field: 'permission_name',
      name: '数据集名称',
      enableCellEdit: false,
      width: '15%',
//      grouping: { groupPriority: 0}
    }, {
      field: 'district_id',
      name: '地区',
      enableCellEdit: false,
      width: '10%',
      cellFilter: 'mapDistrict'
    }, {
      field: 'province_id',
      name: '省份',
      enableCellEdit: false,
      width: '10%',
      cellFilter: 'mapProvince'
    }, {
      field: 'city_id',
      name: '城市',
      enableCellEdit: false,
      width: '10%',
      cellFilter: 'mapCity'
    }, {
      field: 'zone_id',
      name: '区县',
      enableCellEdit: false,
      width: '15%',
      cellFilter: 'mapZone'
    }, {
      field: 'data_type',
      name: '数据类型',
      enableCellEdit: false,
      width: '15%',
      cellFilter: 'mapDataType'
    }, {
      name: '数据范围',
      enableFiltering: false,
      width: '15%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2"><button class="btn btn-link btn-group-lg" ng-click="grid.appScope.showDetail(row.entity)" >查看条件</button></div></div>',
      pinnedRight: true
    }, {
      name: '数据列表',
      enableFiltering: false,
      width: '10%',
      cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.show(row.entity)">查询数据</button></div></div>',
      pinnedRight: true
    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
      });
      gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {

        $scope.datatype.current_page = newPage - 1;
        $scope.datatype.page_size = pageSize;

        $scope.getPermissionList();
      });
    }
  }
//  $scope.gridApi.grouping.groupColumn('permission_name');

  $scope.showDetail = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/health_report_modal/permission_list_modal/permission_list_modal.html',
      controller: 'PermissionListModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        'WRISTBANDCOLUMNSPromise': function(constants) {
          return constants.WRISTBANDCOLUMNSPromise;
        },
        'SPHYGMOMANOMETERCOLUMNSPromise': function(constants) {
          return constants.SPHYGMOMANOMETERCOLUMNSPromise;
        },
        'GLUCOSEMETERCOLUMNSPromise': function(constants) {
          return constants.GLUCOSEMETERCOLUMNSPromise;
        },
        'THERMOMETERCOLUMNSPromise': function(constants) {
          return constants.THERMOMETERCOLUMNSPromise;
        },
        'OXIMETERCOLUMNSPromise': function(constants) {
          return constants.OXIMETERCOLUMNSPromise;
        },
        'FATCOLUMNSPromise': function(constants) {
          return constants.FATCOLUMNSPromise;
        },
        'COMPARISONOPSPromise': function(constants) {
          return constants.COMPARISONOPSPromise;
        },
        'LOGICALOPSPromise': function(constants) {
          return constants.LOGICALOPSPromise;
        }
      }
    });
  }

  //  $http.get('https://rawgit.com/angular-ui/ui-grid.info/gh-pages/data/500_complex.json').success(function(data) {});



  $scope.evaluation = function(entity) {


    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_data_operation_modals/evaluation_wristband_modal.html',
      controller: 'EvaluationCtrl',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });

    modalInstance.result.then(function() {

      console.log('Modal closed at: ' + new Date());
    }, function() {
      console.log('Modal dismissed at: ' + new Date());
    });

  };
};
