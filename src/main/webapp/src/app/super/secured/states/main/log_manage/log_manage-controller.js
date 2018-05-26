/**
 * Created by zhanga.fnst on 2015/7/16.
 */
'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.main.log_manage.Ctrl');



/**
 * log manage controller.
 *
 * @constructor
 * @export
 */

jxsprsec.main.log_manage.Ctrl.$inject = ['$scope', '$http', '$filter','i18nService','$q','constants','basic','log_manage'];
jxsprsec.main.log_manage.Ctrl = function($scope, $http, $filter, i18nService, $q, constants, $modal, ModalService, basic, log_manage) {

    /**
     * gridOptions似乎必须放在$scope上？简单地改成ctrl运行有错误
     */

    i18nService.setCurrentLang('zh-CN');

    $scope.query_date = {
        happened_from:new Date('2015/07/01'),
        happened_to:new Date()
    }

    $scope.query_date.happened_to.setHours(23);
    $scope.query_date.happened_to.setMinutes(59);
    $scope.query_date.happened_to.setSeconds(59);

    $scope.log = {
        user_role:undefined,
        user_login_name:undefined,
        op_title:undefined,
        happened_from:undefined,
        happened_to:undefined,
        page_size:10,
        current_page:0,
        query_date:undefined
    }

//    $scope.ops = [{op_title_id:1,op_title_name:'登陆'},{op_title_id:2,op_title_name:'登出'}];
//    $scope.roles = [{user_role_id:1,user_role_name:'超级管理员'},{user_role_id:2,user_role_name:'普通管理员'}];
    $scope.ops = constants.gotOPERATIONS();
    $scope.roles = constants.gotUSERROLES();

    $scope.getOperationLogs = function(){

        $scope.log.happened_from = $filter('dateTounix')($filter('date')($scope.query_date.happened_from, 'yyyy-M-dd H:mm:ss'));
        $scope.log.happened_to = $filter('dateTounix')($filter('date')($scope.query_date.happened_to, 'yyyy-M-dd H:mm:ss'));

//        console.log($scope.log);
        return log_manage.getOperationLogs($scope.log).then(function(res){
            $scope.gridOptions.data = res.operations;
            $scope.gridOptions.totalItems = res.total_count;
            $scope.log.query_date = res.query_date;
        },function(error){
            $scope.addAlert('danger','加载数据异常,请稍后再试！');
        });
    }

    $scope.getOperationLogs();

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[
            {
                field:'user_login_name',
                name: '用户名称',
                width: '20%',
                pinnedLeft: true
            },{
                field:'user_role',
                name: '用户角色',
                width: '20%',
                cellFilter:'mapUserRole'
            }, {
                field:'op_title',
                name: '操作类型',
                width: '20%',
                cellFilter:'mapOperation'
            },
            {
                field:'happened_at',
                name: '发生时间',
                width: '20%',
                cellFilter:'unixTodate'
            },
            {
                field:'origin_ip',
                name: '来源 IP',
                width: '20%'
            }
//            ,
//            {
//                name: '操作',
//                width: '35%',
//                cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.popup(row.entity)" >修改资料</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.setRight(row.entity)">设置权限</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.changeManStatus(row.entity)">{{row.entity.status == 1 ? "禁用" : "启用"}}</button></div><div class="col-md-2"><button class="btn btn-warning btn-xs" ng-click="grid.appScope.deleteManager(row.entity)" >删除</button></div></div>',
//                cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.popup(row.entity)" >修改资料</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.setRight(row.entity)">设置权限</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.changeManStatus(row.entity)">{{row.entity.status == 1 ? "禁用" : "启用"}}</button></div></div>',
//                pinnedRight: true
//            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                //
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.log.current_page = newPage - 1;
                $scope.log.page_size = pageSize;
                $scope.getOperationLogs();
            });
        }
    }


//
//    $scope.addManagerModal = function() {
//        var modalInstance = $modal.open({
//            animation: true,
//            templateUrl: 'components/tpls/add_manager_modal/add_manager_modal.html',
//            controller: 'AddManagerModalCtrl',
//            size:'lg'
//        });
//
//        modalInstance.result.then(function() {
//            console.log('Modal closed at: ' + new Date());
//        }, function() {
//            //console.log('Modal dismissed at: ' + new Date());
//            $scope.getNormalManagers();
//        });
//    }
//

    $scope.dt = new Date();
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



};

/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `log_manage.?`
 * child controller.
 *
 * @param {String} text
 */
jxsprsec.main.log_manage.Ctrl.prototype.log = function(text) {
    console.log(text);
};

