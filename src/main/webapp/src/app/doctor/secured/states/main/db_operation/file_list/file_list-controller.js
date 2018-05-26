'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.db_operation.file_list.Ctrl');

/**
 * main.db_operation.file_list controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.db_operation.file_list.Ctrl.$inject = [
  "$scope",
  "$http",
  "$modal",
  "$state",
  '$filter',
  '$q',
  "i18nService",
  "ModalService",
  'constants',
  'db_operation'
];
jxdctsec.main.db_operation.file_list.Ctrl = function(
  $scope,
  $http,
  $modal,
  $state,
  $filter,
  $q,
  i18nService,
  ModalService,
  constants,
  db_operation
) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some other label from main.db_operation.file_list controller';

  var numArry = function(count) {
    var nums = [];
    for (var i = 1; i <= count; i++) {
      nums.push(i);
    }
    return nums;
  }
  $scope.ages = numArry(120);


  $scope.showHistory = function(entity) {
//    console.log("历史数据");
//    console.log(entity);
    var params = {
      "patient_id": entity.patient_id,
      "file_type": entity.file_type
    };
    switch (entity.file_type) {
      case 2: //血压
        $state.go('main.db_operation.sphygmomanometer_files', params);
        break;
      case 4: //血糖
        $state.go('main.db_operation.glucosemeter_files', params);
        break;
      case 6: //脂肪
        $state.go('main.db_operation.fat_files', params);
        break;
      case 5: //体温
        $state.go('main.db_operation.thermometer_files', params);
        break;
      case 1: //手环
        $state.go('main.db_operation.wristband_files', params);
        break;
      case 3: //血氧
        $state.go('main.db_operation.oximeter_files', params);
        break;
      case 7: //其他
        $state.go('main.db_operation.other_files', params);
        break;
    };

  };


  $scope.popup = function(entity) {
    switch (entity.file_type) {
      case 2: //血压
        $scope.popupSphygmomanometerModal(entity);
        break;
      case 4: //血糖
        $scope.popupGlucosemeterModal(entity);
        break;
      case 6: //脂肪
        $scope.popupFatModal(entity);
        break;
      case 5: //体温
        $scope.popupThermometerModal(entity);
        break;
      case 1: //手环
        $scope.popupWristbandModal(entity);
        break;
      case 3: //血氧
        $scope.popupOximeterModal(entity);
        break;
      case 7: //其他
        $scope.popupOtherModal(entity);
        break;
    };

  };

    $scope.popupOtherModal = function(entity){
        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'components/tpls/health_report_modal/other_modal/other_modal.html',
            controller: 'OtherModalCtrl as otherModal',
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
    }

  $scope.animationsEnabled = true;
  $scope.popupFatModal = function(entity) {
    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_report_modal/fat_modal/fat_modal.html',
      controller: 'FatModalCtrl',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    //    modalInstance.result.then(function() {
    //      console.log('Modal closed at: ' + new Date());
    //    }, function() {
    //      console.log('Modal dismissed at: ' + new Date());
    //    });

  };


  $scope.popupOximeterModal = function(entity) {
    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_report_modal/oximeter_modal/oximeter_modal.html',
      controller: 'OximeterModalCtrl as oximeterModal',
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


  $scope.popupWristbandModal = function(entity) {
    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_report_modal/wristband_modal/wristband_modal.html',
      controller: 'WristbandModalCtrl as wristbandModal',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });

    //modal弹出后的操作
    modalInstance.result.then(function() {
      console.log('Modal closed at: ' + new Date());
    }, function() {
      console.log('Modal dismissed at: ' + new Date());
    });

  };


  $scope.popupThermometerModal = function(entity) {
    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_report_modal/thermometer_modal/thermometer_modal.html',
      controller: 'ThermometerModalCtrl as thermometerModal',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });

    modalInstance.result.then(function() {
      //console.log('Modal closed at: ' + new Date());
    }, function() {
      //console.log('Modal dismissed at: ' + new Date());
    });

  };

  $scope.popupGlucosemeterModal = function(entity) {
    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_report_modal/glucosemeter_modal/glucosemeter_modal.html',
      controller: 'GlucosemeterModalCtrl as glucosemeterModal',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });

    modalInstance.result.then(function() {

      //console.log('Modal closed at: ' + new Date());
    }, function() {
      //console.log('Modal dismissed at: ' + new Date());
    });

  };



  $scope.popupSphygmomanometerModal = function(entity) {


    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_report_modal/sphygmomanometer_modal/sphygmomanometer_modal.html',
      controller: 'SphygmomanometerModalCtrl as sphygmomanometerModal',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });
  };


  //    $scope.age = {
  //    selected: "1"
  //  };
  //  ctrl.ages = Ages.query({}, function(age) {
  //    ctrl.age = {
  //      selected: age[0].id
  //    }
  //  });

  //      ctrl.file_type = {
  //        selected: undefined
  //      };
  //      ctrl.file_types = [];

  $scope.genders = constants.gotGENDERS();
  $scope.gender = {
    selected: undefined
  };
//  $scope.dataTypes = constants.gotDATATYPES();

  $scope.fileTypes = constants.gotFILETYPES();

  activate();

  function activate() {
    // var promises = [getGenders(), getDataTypes()];
    // return $q.all(promises).then(function() {});
  }

  // function getGenders() {
  //   return constants.getGenders().then(function(data) {
  //     $scope.genders = data.genders;
  //     $scope.gender = {
  //       selected: undefined
  //     };
  //     return $scope.genders;
  //   });
  // }

  // function getDataTypes() {
  //   return constants.getDataTypes().then(function(data) {
  //     $scope.dataTypes = data.data_types;
  //     console.log($scope.dataTypes);
  //     return $scope.dataTypes;
  //   });
  // }

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
        field: 'patient_phone',
        name: '手机号码',
        width: '16%',
        grouping: { groupPriority: 0}

    }, {
      field: "patient_name",
      name: '姓名',
      width: '10%',
      pinnedLeft: true
    }, {
      field: 'patient_age',
      name: '年龄',
      width: '10%'
    }, {
      field: 'patient_gender',
      name: '性别',
      width: '10%',
      cellFilter: 'mapGender'
    }, {
      field: 'measured_at',
      name: '数据收集时间',
      width: '14%',
      cellFilter: 'unixTodate'
    }, {
      field: 'file_type',
      name: '健康数据类型',
      width: '14%',
      cellFilter: 'mapFileType'
    }, {
      name: '操作',
      width: '26%',
      cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.popup(row.entity)" >编辑健康档案</button></div><div class="col-md-offset-2 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.showHistory(row.entity)">查看历史档案</button></div></div>'
    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
        //                if (sortColumns.length == 0) {
        //                    paginationOptions.sort = null;
        //                } else {
        //                    paginationOptions.sort = sortColumns[0].sort.direction;
        //                }
        //                //getPage();
      });
      gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
        //               if($scope.consultation.pageSize != pageSize){
        //                $scope.consultation.query_date = $filter('dateTounix')($filter('date')(new Date(),'yyyy-M-dd H:mm:ss'));
        //               }
        $scope.file.current_page = newPage - 1;
        $scope.file.page_size = pageSize;

        $scope.queryFile();
      });
    }
  }

  $scope.query_date = {
    created_from: new Date('2014-01-01'),
    created_to: new Date()
  }
  $scope.query_date.created_to.setHours(23);
  $scope.query_date.created_to.setMinutes(59);
  $scope.query_date.created_to.setSeconds(59);

  $scope.arr = {
    data_file_type: 1
  };
  $scope.file = {
    name: '',
    age_from: 10,
    age_to: 60,
    gender: undefined,
    file_type: [1],
    measured_from: $scope.query_date.created_from,
    measured_to: $scope.query_date.created_to,
    page_size: 10,
    current_page: 0,
    phone: undefined,
    query_date: undefined
  }

  $scope.queryFile = function() {
    $scope.file.measured_from = $filter('dateTounix')($filter('date')($scope.query_date.created_from, 'yyyy-M-dd H:mm:ss'));
    $scope.file.measured_to = $filter('dateTounix')($filter('date')($scope.query_date.created_to, 'yyyy-M-dd H:mm:ss'));
    $scope.file.file_type = [];
    $scope.file.file_type.push($scope.arr.data_file_type);
    db_operation.getFiles($scope.file).then(function(res) {
      $scope.gridOptions.data = res.files;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.file.query_date = res.query_date;
    }, function(error) {
      console.log("error!");
    })
  }

  $scope.queryFile();


  //  $scope.today = function() {
  //    $scope.dt = new Date();
  //
  //  };
  //  $scope.today();
  //
  //
  //  $scope.clear = function() {
  //    $scope.dt = null;
  //  };

  $scope.dt = new Date();

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
  };

  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
  $scope.toggleMin();

  $scope.open_form_1 = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.form1_opened = true;
  };

  $scope.open_form_2 = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.form2_opened = true;
  };




  $scope.dateOptions = {
//    formatYear: 'yy',
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


  $scope.isCollapsed = true;


};
