'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.doctor.Ctrl');



/**
 * main.db_operation.doctor controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.db_operation.doctor.Ctrl.$inject = [
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
jxmgrsec.main.db_operation.doctor.Ctrl = function(
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

    $scope.searchData = {
        full_name: undefined,
        registered_from: undefined,
        registered_to: undefined,
        page_size: 10,
        current_page: 0,
        query_date: undefined
    };

    var ctrl = this;

    $scope.online_count = 0;
    $scope.member_count = 0;

    i18nService.setCurrentLang('zh-CN');

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[
            {
                field: 'login_name',
                name: '登录名',
                width: '15%',
                pinnedLeft: true
            },
            {
                field: 'full_name',
                name: '医生名称',
                width: '15%',
                pinnedLeft: true
            }, {
                field: 'registered_at',
                name: '注册时间',
                width: '20%',
                cellFilter: "unixTodate"
            }, {
                field: 'status',
                name: '账户状态',
                width: '20%',
                cellFilter: "mapDoctorStatus"
            },{
                name: '操作',
                width: '30%',
//                cellTemplate: '<div class="row"><div class="col-md-3 text-center"><button class="btn btn-warning btn-sm" ng-if="row.entity.status==1" ng-click="grid.appScope.changeStatus(row.entity)" >禁用账户</button><button class="btn btn-primary btn-sm" ng-if="row.entity.status==2" ng-click="grid.appScope.changeStatus(row.entity)" >启用账户</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.showDoctorDetail(row.entity.doctor_id)" >查看详情</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.editDoctor(row.entity.doctor_id)" >编辑医生</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.deleteDoctor(row.entity.doctor_id)" >删除医生</button></div></div>'
                cellTemplate: '<div class="row"><div class="col-md-3 text-center"><button class="btn btn-warning btn-sm" ng-if="row.entity.status==1" ng-click="grid.appScope.changeStatus(row.entity)" >禁用账户</button><button class="btn btn-primary btn-sm" ng-if="row.entity.status==2" ng-click="grid.appScope.changeStatus(row.entity)" >启用账户</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.showDoctorDetail(row.entity.doctor_id)" >查看详情</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.editDoctor(row.entity.doctor_id)" >编辑医生</button></div></div>',
                pinnedRight: true

            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                //
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.searchData.current_page = newPage - 1;
                $scope.searchData.page_size = pageSize;

                $scope.getDoctors();
            });
        }
    };

        /**
         * @type {String}
         * @nocollapse
         */
    ctrl.label = 'some other label from main.db_operation.terminal controller';

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

    $scope.getDoctors = function() {
        if ($scope.query_date.from > $scope.query_date.to) {
            $scope.addAlert("danger", "结束日期不能小于开始日期");
            return;
        }
        $scope.searchData.registered_from = $filter('dateTounix')($filter('date')($scope.query_date.from, 'yyyy-M-dd H:mm:ss'));
        $scope.searchData.registered_to = $filter('dateTounix')($filter('date')($scope.query_date.to, 'yyyy-M-dd H:mm:ss'));
        console.log($scope.searchData);
        return db_operation.getDoctors($scope.searchData).then(function(res) {
            console.log(res);
            $scope.gridOptions.data = res.doctors;
            $scope.gridOptions.totalItems = res.total_count;
            $scope.searchData.query_date = res.query_date;
            $scope.online_count = res.online_count;
            $scope.member_count = res.member_count;
        });
    }

    $scope.addDoctor = function() {

        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/add_doctor_modal/add_doctor_modal.html',
            controller: 'AddDoctorModalCtrl',
            size: 'lg'
        });
        modalInstance.result.then(function() {

        }, function() {
            console.log('close modal:');
            $scope.searchData.query_date = undefined;
            console.log($scope.searchDatas);
            $scope.getDoctors();
        });
    }

    $scope.changeStatus = function(entity) {
        entity.status = entity.status == 1 ? 2 : 1;
        var reqData = {
            status: ''
        }
        reqData.status = entity.status;
        console.log(entity);
        db_operation.modifyDoctorStatus(entity.doctor_id, reqData).then(function(data) {

        });
    };

    $scope.showDoctorDetail = function(entity) {

        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/show_doctor_modal/show_doctor_modal.html',
            controller: 'ShowDoctorModalCtrl',
            size: 'lg',
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

    $scope.editDoctor = function(entity) {

        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/modify_doctor_modal/modify_doctor_modal.html',
            controller: 'ModifyDoctorModalCtrl',
            size: 'lg',
            resolve: {
                entity: function() {

                    return entity;
                }
            }
        });
        modalInstance.result.then(function() {
            console.log('Modal closed at: ' + new Date());
            $scope.getDoctors();
        }, function() {
            console.log('Modal dismissed at: ' + new Date());
            $scope.getDoctors();
        });
    }

    $scope.deleteDoctor = function(entity) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'components/tpls/delete_doctor_modal/delete_doctor_modal.html',
            controller: 'DeleteDoctorModalCtrl',
            resolve: {
                entity: function() {

                    return entity;
                }
            }
        });
        modalInstance.result.then(function() {
            console.log('Modal closed at: ' + new Date());
            $scope.getDoctors();
        }, function() {
            console.log('Modal dismissed at: ' + new Date());
            $scope.getDoctors();
        });
    }


    activate();

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

    function activate() {
        var promises = [$scope.getDoctors()];
        return $q.all(promises).then(function() {});
    }

};
