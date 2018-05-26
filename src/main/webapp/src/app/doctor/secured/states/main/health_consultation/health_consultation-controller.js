'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_consultation.Ctrl');



/**
 * Health consultation controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.health_consultation.Ctrl.$inject = [
  '$q',
  '$scope',
  '$filter',
  'i18nService',
  'constants',
  'health_con',
  'ui.grid',
  'ui.grid.pagination'

];

jxdctsec.main.health_consultation.Ctrl = function($q, $scope, $filter, $state, $modal, i18nService, constants, health_con) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from health consultation controller';

  var ctrl = this;

  ctrl.districts = constants.gotDISTRICTS();
  ctrl.district = {selected: undefined};
  ctrl.district_selected = [];
  ctrl.departments = constants.gotDEPARTMENTS();
  ctrl.department = {selected: undefined};
  ctrl.department_selected = [];
  $scope.consultation_status = constants.gotHEALTHCONSULTATIONSTATUS();

  console.log(ctrl.district_selected);

  activate();

  function activate() {
    // var promises = [getDistricts(), getDepartments(), getHealthConsultationStatus()];
    // return $q.all(promises).then(function() {});
  }

  // function getDistricts() {
  //   return constants.getDistricts().then(function(data) {
  //     //            console.log("district:");
  //     //            console.log(data);
  //     ctrl.districts = data.districts;
  //     ctrl.district = {
  //       selected: undefined
  //     };
  //     return ctrl.districts;
  //   });
  // }

  // function getDepartments() {
  //   return constants.getDepartments().then(function(data) {
  //     //            console.log('getDepartments:');
  //     //            console.log(data);
  //     ctrl.departments = data.departments;
  //     ctrl.department = {
  //       selected: undefined
  //     };
  //     return ctrl.departments;
  //   })
  // }

  // function getHealthConsultationStatus() {
  //   return constants.getHealthConsultationStatus().then(function(data) {
  //     //console.log(data);
  //     $scope.consultation_status = data.consultation_status_list;
  //   })
  // }

  $scope.con_status = "已回复";

  $scope.query_date = {
    created_from: new Date('2014-01-01'),
    created_to: new Date()
  }
  $scope.query_date.created_to.setHours(23);
  $scope.query_date.created_to.setMinutes(59);
  $scope.query_date.created_to.setSeconds(59);
  
  var query_date = -1;
  //$scope.current_date = $filter('dateTounix')($filter('date')(new Date(),'yyyy-M-dd H:mm:ss')),
  $scope.tmp = {
    district_id: undefined,
    department_id: undefined
  };

  $scope.clearDistrict = function() {
    $scope.tmp.district_id = undefined;
    $scope.consultation.district_id = undefined;
  }

  $scope.clearDepartment = function() {
    $scope.tmp.department_id = undefined;
    $scope.consultation.department_id = undefined;
  }

  $scope.consultation = {
    district_id: [1],
    department_id: [0],
    status: undefined,
    created_from: $filter('dateTounix')($filter('date')($scope.query_date.created_from, 'yyyy-M-dd H:mm:ss')),
    created_to: $filter('dateTounix')($filter('date')($scope.query_date.created_to, 'yyyy-M-dd H:mm:ss')),
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }
  $scope.queryConsultation = function() {

    // $scope.consultation.query_date = $filter('dateTounix')($filter('date')(new Date(),'yyyy-M-dd H:mm:ss'));
    $scope.consultation.created_from = $filter('dateTounix')($filter('date')($scope.query_date.created_from, 'yyyy-M-dd H:mm:ss'));
    $scope.consultation.created_to = $filter('dateTounix')($filter('date')($scope.query_date.created_to, 'yyyy-M-dd H:mm:ss'));
    //console.log($scope.consultation);

    //        var a = $scope.consultation.department_id;
    //        var b = $scope.consultation.district_id;

    $scope.consultation.department_id = [];
    $scope.consultation.district_id = [];
    if ($scope.tmp.department_id === undefined) {
      $scope.consultation.department_id = undefined;
    } else {
      $scope.consultation.department_id.push($scope.tmp.department_id);
    }
    if ($scope.tmp.district_id === undefined) {
      $scope.consultation.district_id = undefined;
    } else {
      $scope.consultation.district_id.push($scope.tmp.district_id);
    }

    //        console.log('tmp_district_id');
    //        console.log($scope.tmp.district_id);
    //
    //        console.log('$scope.consultation:');
    //        console.log($scope.consultation);

    health_con.dealConsultation($scope.consultation).then(function(res) {
      //console.log(res);
      $scope.gridOptions.data = res.consultations;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.consultation.query_date = res.query_date;
    }, function(error) {
      console.log("error!");
    })
  }

  $scope.queryConsultation();

  i18nService.setCurrentLang('zh-CN');
  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: "user_name",
      name: '用户昵称',
      width: '15%',
      pinnedLeft: true
    }, {
      field: 'user_phone',
      name: '手机号码',
      width: '15%'
    }, {
      field: 'district_id',
      name: '地区类别',
      width: '20%',
      cellFilter: 'mapDistrict'
    }, {
      field: 'created_at',
      name: '数据收集时间',
      width: '20%',
      cellFilter: 'unixTodate'
    }, {
      field: 'status',
      name: '已回复',
      width: '15%',
      cellFilter: 'mapHelathConsultationStatus'
    }, {
      name: '操作',
      enableFiltering: false,
      width: '15%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2"><button class="btn btn-link btn-group-lg" ng-click="grid.appScope.replyModal(row.entity)" >回复</button></div></div>',
      pinnedRight: true
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
        $scope.consultation.current_page = newPage - 1;
        $scope.consultation.page_size = pageSize;

        $scope.queryConsultation();
      });
    }
  }

  $scope.replyModal = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/health_report_modal/health_con_modal/health_con_modal.html',
      controller: 'HealthConModalCtrl',
      size: 'lg',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });
  }

  //     $scope.today = function() {
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
  //  $scope.disabled = function(date, mode) {
  //    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
  //  };
  //
  //  $scope.toggleMin = function() {
  //    $scope.minDate = $scope.minDate ? null : new Date();
  //  };
  //  $scope.toggleMin();

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
};


/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.health_consultation.?`
 * child controller.
 *
 * @param {String} text
 */
jxdctsec.main.health_consultation.Ctrl.prototype.log = function(text) {
  console.log(text);
};
