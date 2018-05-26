'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.user.Ctrl');



/**
 * main.db_operation.user controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.db_operation.user.Ctrl.$inject = [
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
jxmgrsec.main.db_operation.user.Ctrl = function(
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
  i18nService.setCurrentLang('zh-CN');

    $scope.online_count = 0;
    $scope.member_count = 0;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some other label from main.db_operation.user controller';

  var NAME_SEARCH_STATUS = 'NAME_SEARCH_STATUS';
  var OTHER_SEARCH_STATUS = "OTHER_SEARCH_STATUS";

  $scope.searchStatus = NAME_SEARCH_STATUS;

  $scope.page_param = {
    page_size: 10,
    current_page: 0,
    query_date: undefined
  }


  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: 'full_name',
      name: '用户名',
      width: '10%',
      pinnedLeft: true
    }, {
      field: 'phone',
      name: '注册手机',
      width: '10%'
    }, {
      field: 'device_type_ids',
      name: '设备类型',
      width: '20%',
      cellFilter: 'mapDeviceTypeArray'

    }, {
      field: 'registered_at',
      name: '注册时间',
      width: '15%',
      cellFilter: 'unixTodate'
    }, {
      field: 'status',
      name: '状态',
      width: '15%',
      cellFilter: 'mapUserStatus'
    }, {
      name: '操作',
      width: '30%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.changeStatus(row.entity)" >{{(row.entity.status == 1 ? "禁用账户": "启用账户" )}}</button></div><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.showUserDetail(row.entity.user_id)" >查看详情</button></div><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.editUser(row.entity.user_id)" >编辑用户</button></div></div>'
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
        if ($scope.searchStatus == NAME_SEARCH_STATUS) {
          $scope.page_param.current_page = newPage - 1;
          $scope.page_param.page_size = pageSize;
          $scope.regetUsersByName();
        } else {
          $scope.page_param.current_page = newPage - 1;
          $scope.page_param.page_size = pageSize;
          $scope.regetUsersByOther();
        }
      });
    }
  };

  $scope.device_types = [];

  $scope.ages = [];

  for (var i = 0; i <= 120; i++) {
    $scope.ages[i] = i;
  }

  $scope.userSearchNameData = {
    full_name: '',
    page_size: 10,
    current_page: 0,
    query_date: undefined
  };

  $scope.device_type_id = {
    selected: undefined
  };
  $scope.clearDeviceType = function() {
    $scope.device_type_id.selected = undefined;
  };
  $scope.userSearchOtherData = {
    phone: undefined,
    device_type_ids: undefined,
    registered_from: undefined,
    registered_to: undefined,
    begin_age: undefined,
    end_age: undefined,
    page_size: 10,
    current_page: 0,
    query_date: undefined
  };

  // $scope.userInfo = {
  //     nick_name: '',
  //     full_name: '',
  //     password: '',
  //     phone: '',
  //     device_type_ids: '',
  //     registered_at: '',
  //     status: '',
  //     email: '',
  //     avatar_url: '',
  //     gender: '',
  //     birthday: '',
  //     height: '',
  //     weight: '',
  //     target_weight: '',
  //     address: '',
  //     qq: '',
  //     points: ''
  // };

  $scope.dt = new Date();
  $scope.query_date = {
    from: new Date('2014-01-01'),
    to: new Date()
  };
  $scope.query_date.to.setHours(23);
  $scope.query_date.to.setMinutes(59);
  $scope.query_date.to.setSeconds(59);

  $scope.open_form_from = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.from_opened = true;
  };

  $scope.open_form_to = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.to_opened = true;
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

  $scope.getDeviceTypes = function() {
    return constants.getDeviceTypes().then(function(data) {
      $scope.device_types = data.device_types;
      return $scope.device_types;
    });
  }

  $scope.changeStatus = function(entity) {
    entity.status = entity.status == 1 ? 2 : 1;
    var reqData = {
      status: ''
    }
    reqData.status = entity.status;
    console.log(entity);
    db_operation.putUserStatus(entity.user_id, reqData).then(function(data) {

    });
  };

  $scope.showUserDetail = function(entity) {

    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/show_user_modal/show_user_modal.html',
      controller: 'ShowUserModalCtrl',
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

  $scope.editUser = function(entity) {

    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/modify_user_modal/modify_user_modal.html',
      controller: 'ModifyUserModalCtrl',
      size:'lg',
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

  $scope.getUsersByName = function() {
    console.log($scope.userSearchNameData);
    db_operation.getUsersByName($scope.userSearchNameData).then(function(res) {
      console.log(res);
      $scope.searchStatus = NAME_SEARCH_STATUS;
      $scope.gridOptions.data = res.users;
      $scope.userSearchNameData.query_date = res.query_date;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.online_count = res.online_count;
      $scope.member_count = res.member_count;
    });
  }

  $scope.regetUsersByName = function() {
    $scope.userSearchNameData.current_page = $scope.page_param.current_page;
    $scope.userSearchNameData.page_size = $scope.page_param.page_size;    
    $scope.getUsersByName();
  }


  $scope.getUsersByOther = function() {
    if ($scope.device_type_id.selected === undefined) {
      $scope.userSearchOtherData.device_type_ids = undefined;
    } else {
      $scope.userSearchOtherData.device_type_ids = [];
      $scope.userSearchOtherData.device_type_ids.push($scope.device_type_id.selected);
    }
    $scope.userSearchOtherData.registered_from = $filter('dateTounix')($filter('date')($scope.query_date.from, 'yyyy-M-dd H:mm:ss'));
    $scope.userSearchOtherData.registered_to = $filter('dateTounix')($filter('date')($scope.query_date.to, 'yyyy-M-dd H:mm:ss'));    
    console.log($scope.userSearchOtherData);
    db_operation.getUsers($scope.userSearchOtherData).then(function(res) {
      console.log(res);
      $scope.searchStatus = OTHER_SEARCH_STATUS;
      $scope.gridOptions.data = res.users;
      $scope.userSearchOtherData.query_date = res.query_date;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.online_count = res.online_count;
      $scope.member_count = res.member_count;
    });
  }

  $scope.regetUsersByOther = function() {
    $scope.userSearchOtherData.current_page = $scope.page_param.current_page;
    $scope.userSearchOtherData.page_size = $scope.page_param.page_size;    
    $scope.getUsersByOther();
  }

  $scope.activate = function() {
    var promises = [$scope.getDeviceTypes(), $scope.regetUsersByName()];
    return $q.all(promises).then(function() {});
  }

  //初始化界面
  $scope.activate();

};
