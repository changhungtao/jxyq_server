'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.loc.Ctrl');



/**
 * main.twatch.loc controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.twatch.loc.Ctrl.$inject = [
    "$rootScope",
    "$scope",
    "$http",
    "$modal",
    "$state",
    '$filter',
    '$q',
    "$ocLazyLoad",
    "i18nService",
    "ModalService",
    'constants',
    'twatch',
    '$timeout'
];
jxmgrsec.main.twatch.loc.Ctrl = function(
    $rootScope,
    $scope,
    $http,
    $modal,
    $state,
    $filter,
    $q,
    $ocLazyLoad,
    i18nService,
    ModalService,
    constants,
    twatch,
    $timeout
) {

    /**
     * @type {String}
     * @nocollapse
     */
    this.label = 'some other label from main.twatch.loc controller';
    var ctrl = this;

    i18nService.setCurrentLang('zh-CN');


    $scope.locSearchData = {
        imei: $rootScope.imei,
        begin_date: undefined,
        end_date: undefined,
        page_size: 10,
        current_page: 0,
        query_date: undefined
    };

    $scope.tempArray = [];
    $scope.phones = [];

    $scope.monitorData = {
        imei: '',
        rec_number: undefined
    };



    $scope.gridOptions = {
        useExternalPagination: true,
//        useExternalSorting: true,
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10
    };

    $scope.gridOptions.columnDefs = [{
        field: 'date',
        name: '定位日期',
        width: '20%',
        pinnedLeft: true,
        cellFilter: 'unixTodate'
    }, {
        field: 'sub_mode',
        name: '上传方式',
        width: '20%'
    }, {
        field: 'count',
        name: '总定位数量',
        width: '15%'
    }, {
        field: 'inner_count',
        name: '安全区域内',
        width: '15%'
    }, {
        field: 'outer_count',
        name: '安全区域外',
        width: '15%'
    }, {
        filed: 'locations',
        name: '位置信息队列',
        width: '15%'
            // cellTemplate: '<div class="row"><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.changeStatus(row.entity)" >{{(row.entity.status == 1 ? "禁用账户": "启用账户" )}}</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.showManuDetail(row.entity.manufactory_id)" >查看详情</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.editManu(row.entity.manufactory_id)" >编辑厂商</button></div><div class="col-md-3 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.deleteManu(row.entity.manufactory_id)" >删除厂商</button></div></div>'
    }, {
        name: '操作',
        width: '15%',
        cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.showOnMap(row.entity)" >在地图上含查看</button></div></div>',
        pinnedRight: true
    }];
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
            $scope.locSearchData.current_page = newPage - 1;
            $scope.locSearchData.page_size = pageSize;
            $scope.getLocations();
        });
    }

    $scope.getLocations = function() {
        console.log($scope.locSearchData);
        if (!validParam($scope.locSearchData.imei)) {
            $scope.addAlert("danger", "请输入IMEI号码");
            return;
        };
        $scope.locSearchData.begin_date = $filter('dateTounix')($filter('date')($scope.query_date.from, 'yyyy-M-dd H:mm:ss'));
        $scope.locSearchData.end_date = $filter('dateTounix')($filter('date')($scope.query_date.to, 'yyyy-M-dd H:mm:ss'));
        if ($scope.locSearchData.begin_date > $scope.locSearchData.end_date) {
            $scope.addAlert("danger", "开始时间不能早于结束时间");
            return;
        };
        $scope.monitorData.imei = $scope.locSearchData.imei;
        //        $rootScope.imei = $scope.locSearchData.imei;
        $rootScope.imeiChanged($scope.locSearchData.imei);
        twatch.getLocations($scope.locSearchData).then(function(res) {
            console.log(res);
            $scope.gridOptions.data = res.day_locations;
            $scope.query_date.query_date = res.query_date;
            $scope.gridOptions.totalItems = res.total_count;
            if (validParam($scope.monitorData.imei)) {
                $scope.getPhones();
            };
        });


    }

    $scope.unique = function(input) {
        input.sort();
        var re = [input[0]];
        for (var i = 1; i < input.length; i++) {
            if (this[i] !== re[re.length - 1]) {
                re.push(input[i]);
            }
        }
        return re;
    }

    function getArray(array) {
        var hash = {},
            len = array.length,
            result = [];

        for (var i = 0; i < len; i++) {
            if (!hash[array[i]]) {
                hash[array[i]] = true;
                result.push(array[i]);
            }
        }
        return result;
    }


    $scope.getPhones = function() {
        var reqData = {
            imei: undefined
        }
        reqData.imei = $scope.monitorData.imei;
        console.log(reqData);
        twatch.getWhiteList(reqData).then(function(res) {
            console.log(res);
            if (!validParam(res.whitelist)) {
                return;
            };
            for (var i = 0; i < res.whitelist.length; i++) {
                $scope.tempArray.push(res.whitelist[i].number);
            };
            // $scope.phones = res.whitelist;
        });
        twatch.getNumbers(reqData).then(function(res) {
            console.log(res);
            if (validParam(res.qing1)) {
                $scope.tempArray.push(res.qing1);
            }
            if (validParam(res.qing2)) {
                $scope.tempArray.push(res.qing2);
            }

            if (validParam(res.qing3)) {
                $scope.tempArray.push(res.qing3);
            }
            if (validParam(res.qing4)) {
                $scope.tempArray.push(res.qing4);
            }

            if (validParam(res.sos)) {
                $scope.tempArray.push(res.sos);
            }
            $scope.phones = getArray($scope.tempArray);
        });
    };


    $scope.getCurentLocation = function() {
        var reqData = {
            imei: undefined
        }
        reqData.imei = $scope.monitorData.imei;
        if (validParam(reqData.imei)) {
            twatch.getCurLocation(reqData).then(function(res) {
                $scope.addAlert("success", "获取当前位置成功");
            });
        }
    }

    $scope.monitor = function() {

        if (!validParam($scope.monitorData.imei)) {
            return;
        }
        if (!validParam($scope.monitorData.rec_number)) {
            $scope.addAlert("danger", "请选择接受号码");
            return;
        }
        twatch.getMonitor($scope.monitorData).then(function(res) {
            $scope.addAlert("success", "实时监听成功");
        });

    }

    var map;
    var marker;
    var circle;
    var polygon;
    var count = 0;
    var longLans = [];


    function onload() {
        map = new BMap.Map("map_canvas");
        var point = new BMap.Point(118.784406, 31.98078); // 创建点坐标
        map.centerAndZoom(point, 14);
        map.addControl(new BMap.NavigationControl());
        map.addControl(new BMap.MapTypeControl());
        //        marker=new BMap.Marker(point);
        //        map.addOverlay(marker);
        map.setDefaultCursor("crosshair"); //设置地图默认的鼠标指针样式
        map.enableScrollWheelZoom(); //启用滚轮放大缩小，默认禁用。

        var myCity = new BMap.LocalCity();

        function iploac(result) { //根据IP设置地图中心
            console.log('result');
            console.log(result);
            var cityName = result.name;
            map.setCenter(cityName);
        }
        myCity.get(iploac);
    }

    // window.BMap_loadScriptTime = (new Date).getTime();
    $ocLazyLoad.load(
        [{
            files: ['js!http://api.map.baidu.com/getscript?v=2.0&ak=1ioiKRqWB1EeDjtyka2fSfQ3'], 
            cache: true
        }]
    ).then(function(data){
        onload();
    }, function(data){
    });
    // onload();

    var drawMarker = function(lng, lat) {
            if ($scope.validParam(lng) && $scope.validParam(lat)) {
                var point = new BMap.Point(lng, lat);
                longLans.push(point);
                var marker = new BMap.Marker(point);
                map.addOverlay(marker); //增加点
                marker.addEventListener("click", attribute); //给每个点增加单击事件
            }
        }
        //    drawMarker(118.781038,31.98327);

    //获取覆盖物位置
    function attribute(e) {
        var p = e.target;
        //        var lng = p.getPosition().lng;
        //        var lat = p.getPosition().lat;
        console.log(p.getPosition());
    }

    $scope.showOnMap = function(entity) {
        console.log(entity);
        if ($scope.validParam(entity.locations) && entity.locations.length != 0) {
            for (var i = 0; i < entity.locations.length; i++) {
                drawMarker(entity.locations[i].longitute, entity.locations[i].latitude);
            }
            //全部点显示在视野内
            map.setViewport(longLans);
        } else {
            alert('坐标点不可用！');
        }

    }


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


    var validParam = function(param) {
        if (param != undefined && param != null && param != "") {
            return true;
        }
        return false;
    }


};
