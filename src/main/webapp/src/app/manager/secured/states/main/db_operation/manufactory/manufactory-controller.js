'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.manufactory.Ctrl');



/**
 * main.db_operation.manufactory controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.db_operation.manufactory.Ctrl.$inject = [
    "$scope",
    "$http",
    "$modal",
    "$state",
    '$filter',
    '$q',
    "i18nService",
    "ModalService",
    'constants',
    'db_operation',
    '$timeout'
];
jxmgrsec.main.db_operation.manufactory.Ctrl = function(
    $scope,
    $http,
    $modal,
    $state,
    $filter,
    $q,
    i18nService,
    ModalService,
    constants,
    db_operation,
    $timeout
) {

    var ctrl = this;

    /**
     * @type {String}
     * @nocollapse
     */
    ctrl.label = 'some other label from main.db_operation.manufactory controller';


    var NAME_STATUS = 'NAME_STATUS';
    var TYPE_STATUS = "TYPE_STATUS";

    $scope.searchStatus = NAME_STATUS;
    $scope.online_count = 1;
    $scope.member_count = 1;

    $scope.gridOptions = {
        useExternalPagination: true,
//        useExternalSorting: true,
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10
    };

    i18nService.setCurrentLang('zh-CN');

    ////dsadsa

    $scope.gridOptions.columnDefs = [
        {
            field: 'login_name',
            name: '登录名',
            width: '10%',
            pinnedLeft: true
        },
        {
            field: 'manufactory_name',
            name: '厂商名称',
            width: '15%',
            pinnedLeft: true
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
            name: '账户状态',
            width: '15%',
            cellFilter: 'mapManufactoryStatus'
        },

        {
            name: '操作',
            width: '30%',
//            cellTemplate: '<div class="row"><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.changeStatus(row.entity)" >{{(row.entity.status == 1 ? "禁用账户": "启用账户" )}}</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.showManuDetail(row.entity.manufactory_id)" >查看详情</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.editManu(row.entity.manufactory_id)" >编辑厂商</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.deleteManu(row.entity.manufactory_id)" >删除厂商</button></div></div>'
            cellTemplate: '<div class="row"><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.changeStatus(row.entity)" >{{(row.entity.status == 1 ? "禁用账户": "启用账户" )}}</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.showManuDetail(row.entity.manufactory_id)" >查看详情</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.editManu(row.entity.manufactory_id)" >编辑厂商</button></div></div>',
            pinnedRight: true

        },
    ];
    // $scope.gridOptions.enableFiltering = false;
    // $scope.gridOptions.enableCellEdit = false;
    $scope.gridOptions.onRegisterApi = function(gridApi) {
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
            if ($scope.searchStatus == NAME_STATUS) {
                $scope.manuSearchNameData.current_page = newPage - 1;
                $scope.manuSearchNameData.page_size = pageSize;
                $scope.getManusByName();
            } else {
                $scope.manuSearchTypeData.current_page = newPage - 1;
                $scope.manuSearchTypeData.page_size = pageSize;
                $scope.getManusByType();
            }
        });
    }


    $scope.manuSearchNameData = {
        full_name: '',
        page_size: 10,
        current_page: 0,
        query_date: undefined
    };

    $scope.manuSearchTypeData = {
        manufactory_ids: undefined,
        device_type_ids: undefined,
        registered_from: undefined,
        registered_to: undefined,
        page_size: 10,
        current_page: 0,
        query_date: undefined
    };

    $scope.manufactory = {
        manufactory_id: '',
        manufactory_name: '',
        device_type_ids: '',
        registered_at: '',
        status: '',
    };

    // $scope.device_types = [];
    // $scope.device_types_selected = [];

    // $scope.manufacturers = [];
    // $scope.manufacturer_selected = [];

    $scope.manufacturers = constants.gotMANUFACTORIES();
    $scope.device_types = constants.gotDEVICETYPES();
    $scope.device_type_id = {selected: undefined};
    $scope.clearDeviceType = function() {
      $scope.device_type_id.selected = undefined;
    };

    console.log($scope.manufacturers);
    console.log($scope.device_types);

    $scope.dt = new Date();
    $scope.query_date = {
        from: new Date('2014-01-01'),
        to: new Date()
    }
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
//        formatYear: 'yy',
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

    $scope.getManusByName = function() {
        //console.log($scope.manuSearchNameData);
        db_operation.getManusByName($scope.manuSearchNameData).then(function(res) {
            console.log(res);
            $scope.searchStatus = NAME_STATUS;
            $scope.gridOptions.data = res.manufactories;
            $scope.manuSearchNameData.query_date = res.query_date;
            $scope.gridOptions.totalItems = res.total_count;
            $scope.online_count = res.online_count;
            $scope.member_count = res.member_count;
        });
    }

    $scope.regetManusByName = function() {
        $scope.manuSearchNameData.current_page = 0;
        $scope.manuSearchNameData.page_size = 10;
        $scope.gridOptions.paginationCurrentPage = 1;
        $scope.getManusByName();
    }

    $scope.getManusByType = function() {
        // $scope.manuSearchTypeData.device_type_ids = undefined;
        if($scope.device_type_id.selected === undefined){
            $scope.manuSearchTypeData.device_type_ids = undefined;
        }else{
            $scope.manuSearchTypeData.device_type_ids = [];
            $scope.manuSearchTypeData.device_type_ids.push($scope.device_type_id.selected);
        }
        console.log($scope.manuSearchTypeData);
        $scope.manuSearchTypeData.registered_from = $filter('dateTounix')($filter('date')($scope.query_date.from, 'yyyy-M-dd H:mm:ss'));
        $scope.manuSearchTypeData.registered_to = $filter('dateTounix')($filter('date')($scope.query_date.to, 'yyyy-M-dd H:mm:ss'));
        if ($scope.manuSearchTypeData.registered_from > $scope.manuSearchTypeData.registered_to) {
            $scope.addAlert("danger", "开始时间不能早于结束时间");
            return;
        };
        console.log($scope.manuSearchTypeData);
        db_operation.getManusByType($scope.manuSearchTypeData).then(function(res) {
            console.log(res);
            $scope.searchStatus = TYPE_STATUS;
            $scope.gridOptions.data = res.manufactories;
            $scope.manuSearchTypeData.query_date = res.query_date;
            $scope.gridOptions.totalItems = res.total_count;
            $scope.online_count = res.online_count;
            $scope.member_count = res.member_count;
        });
    }

    $scope.regetManusByType = function() {
        $scope.manuSearchTypeData.current_page = 0;
        $scope.manuSearchTypeData.page_size = 10;
        $scope.gridOptions.paginationCurrentPage = 1;
        $scope.getManusByType();
    }


    $scope.changeStatus = function(entity) {
        entity.status = entity.status == 1 ? 2 : 1;
        var reqData = {
            status: ''
        }
        reqData.status = entity.status;
        console.log(reqData);
        db_operation.modifyManuStatus(entity.manufactory_id, reqData).then(function(data) {

        });
    };

    $scope.addManu = function() {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/add_manufacturers_modal/add_manufactory_modal.html',
            controller: 'AddManufactoryModal',
            size: 'lg'

        });
        modalInstance.result.then(function() {

        }, function() {
            $scope.manuSearchNameData.query_date = undefined;
            $scope.regetManusByName();
        });
    }

    $scope.showManuDetail = function(entity) {

        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/manufacturer_detail_modal/manufacturer_detail.html',
            controller: 'ManugfacturerDetailModal',
            size: 'lg',
            resolve: {
                entity: function() {

                    return entity;
                }
            }
        });
        modalInstance.result.then(function() {

        }, function() {
            $scope.regetManusByName();
        });
    }

    $scope.editManu = function(entity) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/modify_manufactory_modal/modify_manufactory_modal.html',
            controller: 'ModifyManufactoryModal',
            size: 'lg',
            resolve: {
                entity: function() {

                    return entity;
                }
            }

        });
        modalInstance.result.then(function() {

        }, function() {
            $scope.regetManusByName();
        });
    }
    $scope.deleteManu = function(entity) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/delete_manufactory_modal/delete_manufactory_modal.html',
            controller: 'DeleteManufactoryModal',
            size: 'lg',
            resolve: {
                entity: function() {

                    return entity;
                }
            }

        });
        modalInstance.result.then(function() {

        }, function() {
            $scope.regetManusByName();
        });
    }




    activate();

    function activate() {
        var promises = [$scope.regetManusByName()];
        return $q.all(promises).then(function() {});
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


};
