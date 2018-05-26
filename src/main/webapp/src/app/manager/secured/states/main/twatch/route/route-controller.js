'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.route.Ctrl');



/**
 * main.twatch.route controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.twatch.route.Ctrl.$inject = [
    "$rootScope",
    "$scope",
    "$http",
    "$modal",
    "$filter",
    "$timeout",
    "$ocLazyLoad",
    "i18nService",
    "ModalService",
    "twatch"
];
jxmgrsec.main.twatch.route.Ctrl = function(
    $scope,
    $http,
    $modal,
    $filter,
    $timeout,
    $rootScope,
    $ocLazyLoad,
    i18nService,
    ModalService,
    twatch
) {

    /**
     * @type {String}
     * @nocollapse
     */
    this.label = 'some other label from main.twatch.route controller';
    var ctrl = this;
    i18nService.setCurrentLang('zh-CN');

    $scope.routeParam = {
        imei: $rootScope.imei,
        begin_date: undefined,
        end_date: undefined,
        page_size: 10,
        current_page: 0,
        query_date: undefined
    }

    $scope.query_date = {
        from: new Date('2015-01-01'),
        to: new Date()
    };
    $scope.query_date.to.setHours(23);
    $scope.query_date.to.setMinutes(59);
    $scope.query_date.to.setSeconds(59);

    $scope.getLocations = function() {
        if (!$scope.validParam($scope.routeParam.imei)) {
            $scope.addAlert("danger", "请输入IMEI号码");
            return;
        };
        $scope.routeParam.begin_date = $filter('dateTounix')($filter('date')($scope.query_date.from, 'yyyy-M-dd H:mm:ss'));
        $scope.routeParam.end_date = $filter('dateTounix')($filter('date')($scope.query_date.to, 'yyyy-M-dd H:mm:ss'));
        if ($scope.routeParam.begin_date > $scope.routeParam.end_date) {
            $scope.addAlert("danger", "开始时间不能早于结束时间");
            return;
        };
        return twatch.getLocations($scope.routeParam).then(function(res) {
            $scope.gridOptions.data = res.day_locations;
            $scope.query_date.query_date = res.query_date;
            $scope.gridOptions.totalItems = res.total_count;
            $scope.confirmIMEI = $scope.routeParam.imei;
            //            $rootScope.imei = $scope.routeParam.imei;
            $rootScope.imeiChanged($scope.routeParam.imei);

            //延时一会等待地图加载再画图
            $timeout(function() {
                map.clearOverlays();
                getPensets();
            }, 100);
        });
    }

    $scope.confirmIMEI = undefined;

    $scope.gridOptions = {
        useExternalPagination: true,
//        useExternalSorting: true,
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        columnDefs: [{
            field: 'date',
            name: '定位日期',
            width: '15%',
            pinnedLeft: true,
            cellFilter: 'unixTodate'
        }, {
            field: 'sub_mode',
            name: '上传方式',
            width: '15%',
            cellFilter: 'mapTwatchGpsMode'
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
        }, {
            name: '操作',
            width: '15%',
            cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.playBack(row.entity)" >回放</button></div></div>',
            pinnedRight: true
        }],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                //
            });
            gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                $scope.routeParam.current_page = newPage - 1;
                $scope.routeParam.page_size = pageSize;
                $scope.getLocations();
            });
        }
    };

    var map;
    var marker;
    var marker1;
    var circle;
    var polygon;
    var count = 0;
    var longLans = []
    $scope.speed = 1; //默认速度一秒钟画一次
    $scope.currentPoints = [];


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

    /*************************************************************************************************/

    var calcArctan = function(pairPointsArry) {
        if ($scope.validParam(pairPointsArry)) {
            //按纬度从小到大排序
            pairPointsArry.sort(function(point1, point2) {
                return point1.latitude > point2.latitude ? 1 : -1
            });

            var point = pairPointsArry[0];
            pairPointsArry[0].angle = 0;
            for (var i = 1; i < 6; i++) {
                var x = point.longitute - pairPointsArry[i].longitute;
                var y = pairPointsArry[i].latitude - point.latitude;
                var angle;
                angle = Math.atan(y / x);
                pairPointsArry[i].angle = angle;
            }
            //角度从大到小排序
            pairPointsArry.sort(function(point1, point2) {
                return point1.angle > point2.angle ? 1 : -1
            });
        }
    }

    var drawCircleMarker = function(longitute, latitude, radius) {
        if ($scope.validParam(longitute) && $scope.validParam(latitude) && $scope.validParam(radius)) {
            var newPoint = new BMap.Point(longitute, latitude);
            map.setCenter(newPoint);
            //            map.clearOverlays();

            //重新画圆心和圆
            marker1 = new BMap.Marker(newPoint);
            //            marker1.enableDragging();
            map.addOverlay(marker1);

            circle = new BMap.Circle(newPoint, radius);
            circle.setFillColor("#FFFA62"); //填充颜色
            circle.setStrokeColor("#f50704"); //边线颜色
            map.addOverlay(circle);
        }
    }

    //    drawCircleMarker(118.784406,31.98078,1000);

    var drawPolygon = function(pointsPair) {
            calcArctan(pointsPair);
            var longLans = [];
            var newPoint = new BMap.Point(pointsPair[5].longitute, pointsPair[5].latitude);
            map.setCenter(newPoint);
            for (var i = 0; i < 6; i++) {
                longLans.push(new BMap.Point(pointsPair[i].longitute, pointsPair[i].latitude));
            }

            //        map.clearOverlays(); //画之前先清掉所有覆盖物
            //画6个点
            //        for (var j = 0; j < 6; j++) {
            //            var marker = new BMap.Marker(longLans[j]);
            //            map.addOverlay(marker); //增加点
            ////            marker.addEventListener("click", attribute); //给每个点增加单击事件
            //        }

            //让所有点在视野范围内
            map.setViewport(longLans);

            polygon = new BMap.Polygon(
                longLans, {
                    strokeColor: "#f50704",
                    fillColor: "#FFFA62",
                    strokeWeight: 3,
                    strokeOpacity: 0,
                    fillOpacity: 0
                });
            map.addOverlay(polygon);
        }
        //    drawPolygon(points);

    $scope.drawPen = function(entity) {
        console.log(entity);
        //圆形围栏，先显示出来，
        if (entity.pen_type == 1) {
            if ($scope.validParam(entity.points[0].longitute) && $scope.validParam(entity.points[0].latitude)) {
                drawCircleMarker(entity.points[0].longitute, entity.points[0].latitude, entity.radius);
            }
        } else if (entity.pen_type == 2) {
            if ($scope.validParam(entity.points[0].longitute) && $scope.validParam(entity.points[0].latitude) &&
                $scope.validParam(entity.points[1].longitute) && $scope.validParam(entity.points[1].latitude) &&
                $scope.validParam(entity.points[2].longitute) && $scope.validParam(entity.points[2].latitude) &&
                $scope.validParam(entity.points[3].longitute) && $scope.validParam(entity.points[3].latitude) &&
                $scope.validParam(entity.points[4].longitute) && $scope.validParam(entity.points[4].latitude) &&
                $scope.validParam(entity.points[5].longitute) && $scope.validParam(entity.points[5].latitude)
            ) {
                drawPolygon(entity.points);
            } else {
                alert("坐标点非法，请修正后再试！");
            }
        }
    }

    //初始化加载围栏
    var getPensets = function() {
            if ($scope.validParam($scope.routeParam.imei)) {
                return twatch.getPensets({
                    imei: $scope.routeParam.imei
                }).then(function(data) {
                    $scope.graphData = data.filter(function(item) {
                        return item.deleted == 0
                    });
                    //                console.log('$scope.graphData');
                    //                console.log($scope.graphData);

                    if ($scope.validParam($scope.graphData) && $scope.graphData.length != 0) {
                        for (var i = 0; i < $scope.graphData.length; i++) {
                            if ($scope.graphData[i].pen_type == 1) {
                                //画圆
                                drawCircleMarker($scope.graphData[i].points[0].longitute, $scope.graphData[i].points[0].latitude, $scope.graphData[i].radius);
                            } else if ($scope.graphData[i].pen_type == 2) {
                                drawPolygon($scope.graphData[i].points);
                            }
                        }
                    }
                });
            } else {
                console.log('IMEI号为空！');
            }
        }
        /*************************************************************************************************/
    var drawMarker = function(lng, lat) {
        if ($scope.validParam(lng) && $scope.validParam(lat)) {
            var point = new BMap.Point(lng, lat);
            longLans.push(point);
            var marker = new BMap.Marker(point);
            map.addOverlay(marker); //增加点
            marker.addEventListener("click", attribute); //给每个点增加单击事件
        } else {
            alert('坐标点不可用！');
        }
    }

    //获取覆盖物位置
    function attribute(e) {
        var p = e.target;
        //        var lng = p.getPosition().lng;
        //        var lat = p.getPosition().lat;
        console.log(p.getPosition());
    }

    var drawTwoPointsLine = function(point1, point2) {
        if ($scope.validParam(point1) && $scope.validParam(point2)) {
            var polyline = new BMap.Polyline([
                new BMap.Point(point1.longitute, point1.latitude),
                new BMap.Point(point2.longitute, point2.latitude)
            ], {
                strokeColor: "#f50704",
                strokeWeight: 4,
                strokeOpacity: 0.5
            }); //创建折线
            map.addOverlay(polyline);
            //map.setViewport([new BMap.Point(point1.longitute,point1.latitude),new BMap.Point(point2.longitute,point2.latitude)]);//显示在视野中
        } else {
            alert('当前坐标点无效！');
        }
    }

    //先把线和点全部画好
    var drawPointsLine = function(points) {
        if ($scope.validParam(points)) {
            var polylinePoints = [];
            for (var i = 0; i < points.length; i++) {
                polylinePoints.push(new BMap.Point(points[i].longitute, points[i].latitude));
                drawMarker(points[i].longitute, points[i].latitude);
            }

            if (polylinePoints.length != 0) {
                var polyline = new BMap.Polyline(polylinePoints, {
                    strokeColor: "#f50704",
                    strokeWeight: 4,
                    strokeOpacity: 0.5
                }); //创建折线
                map.addOverlay(polyline);
            }
            //            map.setViewport([new BMap.Point(point1.longitute,point1.latitude),new BMap.Point(point2.longitute,point2.latitude)]);//显示在视野中
        } else {
            alert('当前坐标点无效！');
        }
    }

    var drawGirlMarker = function(point) {
            if ($scope.validParam(point.longitute) && $scope.validParam(point.latitude)) {
                map.removeOverlay(marker);
                var pt = new BMap.Point(point.longitute, point.latitude);
                var myIcon = new BMap.Icon("../../img/girl.png", new BMap.Size(50, 100), {
                    //                  anchor: new BMap.Size(75, 75)
                    //                offset: new BMap.Size(75, 75) // 指定定位位置
                    //                imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移
                });
                marker = new BMap.Marker(pt, {
                    icon: myIcon
                });
                marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
                map.addOverlay(marker);
            } else {
                alert('坐标点不可用');
            }
        }
        //    drawGirlMarker(points[0]);

    //    drawPointsLine(points);

    //    drawTwoPointsLine(points[0],points[1]);
    /**
     *
     * points:自定义坐标点（{longitute:118.76646,latitude:31.977995}）数组
     * speed:画线的速度，单位秒
     *
     **/


    var m = 0;
    var drawMarkBySpeed = function(points, speed) {
            if ($scope.validParam(points) && points.length > 1) {
                if (m > points.length - 1) {
                    return;
                }
                drawGirlMarker(points[m]);
                m += 1;
                $timeout(function() {
                    drawMarkBySpeed(points, speed);
                    console.log('speed');
                    console.log(speed);
                    $scope.$digest();
                }, speed * 1000);

            } else {
                alert('坐标点个数必须大于等于2！');
                return;
            }
        }
        //    drawPointsLine(points);
        //    drawMarkBySpeed(points,1);

    var k = 0;
    var drawPolyLineBySpeed = function(points, speed) {

        if ($scope.validParam(points) && points.length > 1) {
            if (k + 1 >= points.length) {
                return;
            }
            drawTwoPointsLine(points[k], points[k + 1]);
            k += 1;
            $timeout(function() {
                drawPolyLineBySpeed(points, speed);
            }, speed * 1000);

        } else {
            alert('坐标点个数必须大于等于2！');
            return;
        }
    }

    //    drawPolyLineBySpeed(points,0.5);
    //
    //    for(var i =0 ;i<10;i++){
    //        setTimeout("",3000);
    //        console.log(i);
    //    }

    var drawAllPoints = function() {
        if ($scope.validParam($scope.currentPoints) && $scope.currentPoints.length != 0) {
            map.clearOverlays();
            for (var i = 0; i < $scope.currentPoints.length; i++) {
                drawMarker($scope.currentPoints[i].longitute, $scope.currentPoints[i].latitude);
            }
            //全部点显示在视野内
            map.setViewport(longLans);
            k = 0;
            drawPolyLineBySpeed($scope.currentPoints, $scope.speed);
        } else {
            alert('坐标点不可用！');
        }
    }

    $scope.playBack = function(entity) {
        //console.log(entity);
        if ($scope.validParam($scope.confirmIMEI)) {
            if ($scope.validParam(entity.locations) && entity.locations.length != 0) {
                for (var i = 0; i < entity.locations.length; i++) {
                    drawMarker(entity.locations[i].longitute, entity.locations[i].latitude);
                }
                //全部点显示在视野内
                map.setViewport(longLans);
                $scope.currentPoints = entity.locations;
                drawPointsLine($scope.currentPoints);
                m = 0;
                drawMarkBySpeed($scope.currentPoints, $scope.speed); //动态小女孩路径
                //                k = 0;
                //                drawPolyLineBySpeed($scope.currentPoints,$scope.speed);
            } else {
                alert('坐标点不可用！');
            }
        } else {
            alert('请使用正确IMEI号！');
        }
    }

    $scope.oncePlay = function() {
        m = 0;
        drawMarkBySpeed($scope.currentPoints, $scope.speed); //动态小女孩路径
    }
    $scope.stopPlay = function() {
        if ($scope.currentPoints.length != 0) {
            drawGirlMarker($scope.currentPoints[0]);
        }
    }

    $scope.addSpeed = function() {
        $scope.speed -= 0.2;
        //        console.log('$scope.speed');
        //        console.log($scope.speed);
    }

    $scope.subSpeed = function() {
        $scope.speed += 0.2;
        //        console.log('$scope.speed');
        //        console.log($scope.speed);
    }

    $scope.dt = new Date();
    $scope.minDate = new Date('1900/01/01');

    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.open1 = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened1 = true;
    };

    $scope.dateOptions = {
//        formatYear: 'yy',
        startingDay: 1
    };

    $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[2];

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
