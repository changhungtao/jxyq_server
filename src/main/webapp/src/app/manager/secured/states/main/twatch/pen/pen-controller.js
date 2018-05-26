'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.pen.Ctrl');



/**
 * main.twatch.pen controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.twatch.pen.Ctrl.$inject = [
    "$rootScope",
    "$scope",
    "$http",
    "$modal",
    "$ocLazyLoad",
    "i18nService",
    "ModalService",
    "twatch"
];
jxmgrsec.main.twatch.pen.Ctrl = function(
    $rootScope,
    $scope,
    $http,
    $modal,
    $ocLazyLoad,
    i18nService,
    ModalService,
    twatch
) {

    /**
     * @type {String}
     * @nocollapse
     */
    this.label = 'some other label from main.twatch.pen controller';
    var ctrl = this;

    $scope.penset = {
        imei: $rootScope.imei
            // imei:undefined
            // pen_id:undefined
    }

    $scope.existsIMEI = undefined;
    $scope.penModel = {
        pen_id: undefined,
        imei: undefined,
        pen_name: undefined,
        pen_type: 1,
        points: {
            longitute: undefined,
            latitude: undefined
        },
        radius: 500,
        address: undefined,
        rec_number: undefined
    }

    i18nService.setCurrentLang('zh-CN');

    $scope.raw_pensets = [];
    $scope.pen_type_selects = [];
    $scope.getPensets = function() {
        //console.log($scope.penset);
        if ($scope.validParam($scope.penset.imei)) {
            return twatch.getPensets($scope.penset).then(function(data) {
                //        console.log(data);
                $scope.raw_pensets = data;
                $scope.gridOptions.data = data.filter(function(item) {
                    return item.deleted == 0
                });
                $scope.existsIMEI = $scope.penset.imei;
                $rootScope.imeiChanged($scope.penset.imei);
                //        $rootScope.imei = $scope.penset.imei;
                $scope.gridOptions.totalItems = data.length;

                $scope.pen_type_selects = [];
                for (var i = 0; i < $scope.raw_pensets.length; i++) {
                    if ($scope.validParam($scope.raw_pensets[i].pen_name) && $scope.raw_pensets[i].pen_type == $scope.penModel.pen_type) {
                        $scope.pen_type_selects.push(data[i]);
                    }
                }

                //            console.log('$scope.pen_type_selects');
                //            console.log($scope.pen_type_selects);

            }, function(error) {
                console.log(error);
            });
        } else {
            $scope.addAlert('danger', '请使用合法的IMEI号');
            //      alert('请使用合法的IMEI号！');
        }
    }

    $scope.bindSelects = function() {
        $scope.pen_type_selects = [];
        for (var i = 0; i < $scope.raw_pensets.length; i++) {
            if ($scope.validParam($scope.raw_pensets[i].pen_name) && $scope.raw_pensets[i].pen_type == $scope.penModel.pen_type) {
                $scope.pen_type_selects.push($scope.raw_pensets[i]);
            }
        }
    }

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        //        useExternalPagination: true,
        //        useExternalSorting: true,
        columnDefs: [{
                field: "pen_name",
                name: '围栏名称',
                width: '20%',
                pinnedLeft: true
            }, {
                field: 'pen_type',
                name: '围栏类型',
                width: '20%',
                cellFilter: 'mapPenShape'
            },
            //            {
            //            field: 'points',
            //            name: '坐标点位置',
            //            width: '15%'
            ////            cellFilter:'mapDoctorStatus'
            //        }, {
            //            field: 'radius',
            //            name: '半径',
            //            width: '15%'
            ////            cellFilter:'mapDoctorStatus'
            //        },
            {
                field: 'address',
                name: '位置说明',
                width: '20%'
                    //            cellFilter:'mapDoctorStatus'
            }, {
                field: 'rec_number',
                name: '接收号码',
                width: '20%'
                    //            cellFilter:'mapDoctorStatus'
            }, {
                name: '操作',
                width: '22%',
                cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.editPen(row.entity)" >编辑围栏</button></div><div class="col-md-offset-2 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.pointsModal(row.entity)" >查看位置信息</button></div><div class="col-md-offset-2 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.delPen(row.entity)" >删除</button></div></div>',
                pinnedRight: true
            }
        ]
    }

    $scope.pointsModal = function(entity) {
        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'components/tpls/pen_point_modal/pen_point_modal.html',
            controller: 'PenPointModalCtrl',
            resolve: {
                entity: function() {
                    return entity;
                }
            }
        });
        modalInstance.result.then(function() {
            console.log('Modal closed at: ' + new Date());
        }, function() {
            //console.log('Modal dismissed at: ' + new Date());
            //$window.location.reload();

        });
    }

    $scope.delPen = function(entity) {
        if (confirm('确认删除此条围栏！')) {
            if ($scope.validParam($scope.existsIMEI) && $scope.validParam(entity.pen_id)) {
                return twatch.delPenset({
                    imei: $scope.existsIMEI,
                    pen_id: entity.pen_id
                }).then(function(res) {
                    if ($scope.validParam(res.pen_id)) {
                        alert('删除成功！');
                        $scope.getPensets();
                    }
                });
            } else {
                //        alert('参数有误！');
                $scope.addAlert('danger', '参数有误');
            }
        }
    }

    $scope.penTypes = [{
        pen_type_id: 1,
        pen_type_name: '圆形'
    }, {
        pen_type_id: 2,
        pen_type_name: '多边形'
    }];
    console.log($scope.penTypes);

    var calcArctan = function(pairPointsArry) {
        if ($scope.validParam(pairPointsArry)) {
            //按纬度从小到大排序
            pairPointsArry.sort(function(point1, point2) {
                return point1.latitude > point2.latitude ? 1 : -1
            });
            //            console.log('按纬度从小到大排序');
            //            console.log(pairPointsArry);


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

    var map;
    var marker;
    var b = [118.784406, 31.98078];
    var circle;
    var polygon;
    var count = 0;
    $scope.polygonPoints = [];

    var drawCircle = function(lng, lat) {
        if ($scope.validParam(lng) && $scope.validParam(lat)) {
            var pointMark = new BMap.Point(lng, lat);
            marker = new BMap.Marker(pointMark);
            map.addOverlay(marker);
        }
    }

    var drawMarker = function(lng, lat) {
        if ($scope.validParam(lng) && $scope.validParam(lat)) {
            var marker = new BMap.Marker(new BMap.Point(lng, lat));
            map.addOverlay(marker); //增加点
            marker.addEventListener("click", attribute); //给每个点增加单击事件
        }
    }

    //获取覆盖物位置
    function attribute(e) {
        var p = e.target;
        //把地图上的覆盖物都清除，只剩下被单击的点，也就是全部清除再画一次本点
        var lng = p.getPosition().lng;
        var lat = p.getPosition().lat;
        map.clearOverlays();
        drawMarker(lng, lat);


        var lng_and_lat = {
            longitute: lng,
            latitude: lat
        }
        $scope.polygonPoints = [];
        $scope.polygonPoints.push(lng_and_lat);
        count = 1;
    }

    var drawPolygon = function(pointsPair) {
        calcArctan(pointsPair);
        var longLans = [];
        var newPoint = new BMap.Point(pointsPair[5].longitute, pointsPair[5].latitude);
        map.setCenter(newPoint);
        for (var i = 0; i < 6; i++) {
            longLans.push(new BMap.Point(pointsPair[i].longitute, pointsPair[i].latitude));
        }

        map.clearOverlays(); //画之前先清掉所有覆盖物
        //画6个点
        for (var j = 0; j < 6; j++) {
            var marker = new BMap.Marker(longLans[j]);
            map.addOverlay(marker); //增加点
            marker.addEventListener("click", attribute); //给每个点增加单击事件
        }

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

    function onload() {
        map = new BMap.Map("map_canvas");
        var point = new BMap.Point(118.784406, 31.98078); // 创建点坐标
        map.centerAndZoom(point, 14);
        map.addControl(new BMap.NavigationControl());
        map.addControl(new BMap.MapTypeControl());
        marker = new BMap.Marker(point);
        map.addOverlay(marker);
        map.setDefaultCursor("crosshair"); //设置地图默认的鼠标指针样式
        map.enableScrollWheelZoom(); //启用滚轮放大缩小，默认禁用。

        var myCity = new BMap.LocalCity();
        //console.log(myCity);

        function iploac(result) { //根据IP设置地图中心
            //                console.log(result);
            var cityName = result.name;
            map.setCenter(cityName);
        }
        myCity.get(iploac);

        // 增加地图事件监听
        map.addEventListener('click', function(event) {
            if (event.overlay) {
                return;
            }
            //            var lat = "" + event.point.lat;
            //            var lon = "" + event.point.lng;
            console.log(event.point);
            if ($scope.penModel.pen_type == 1 && confirm("确认将圆心移到这里吗？")) {
                //                $scope.$apply();
                $scope.penModel.points.longitute = event.point.lng;
                $scope.penModel.points.latitude = event.point.lat;
                //              console.log($scope.penModel);
                $scope.$digest(); // Bug fix: tell AngularJS to refresh, 2015/07/11, Gangti

                var pointMark = new BMap.Point(event.point.lng, event.point.lat);
                map.clearOverlays();
                marker = new BMap.Marker(pointMark);
                marker.enableDragging();
                map.addOverlay(marker);

                //                marker.addEventListener("dragend", function(e){
                //                    //获取覆盖物位置
                //                    var o_Point_now =  marker.getPosition();
                //                    var lng = o_Point_now.lng;
                //                    var lat = o_Point_now.lat;
                //                    console.log('拖拽结束');
                //                    console.log(o_Point_now);
                //
                //                })
                circle = new BMap.Circle(new BMap.Point(event.point.lng, event.point.lat), $scope.penModel.radius);
                circle.setFillColor("#FFFA62"); //填充颜色
                circle.setStrokeColor("#f50704"); //边线颜色
                //                circle.setTitle('设置围栏');
                //                circleMarker.enableDragging() ;
                //                circleMarker.setIcon(image);
                //circle.enableDragging();
                map.addOverlay(circle);

                marker.addEventListener('dragging', function() {
                    map.removeOverlay(circle);
                    //map.clearOverlays();
                    var point_now = marker.getPosition();
                    $scope.penModel.points.longitute = point_now.lng;
                    $scope.penModel.points.latitude = point_now.lat;
                    circle = new BMap.Circle(new BMap.Point(point_now.lng, point_now.lat), $scope.penModel.radius);
                    circle.setFillColor("#FFFA62"); //填充颜色
                    circle.setStrokeColor("#f50704"); //边线颜色
                    map.addOverlay(circle); //增加圆
                    $scope.$digest();
                });

                $scope.polygonPoints = [];
                count = 0;
            } else if ($scope.penModel.pen_type == 2) {
                //alert("请连续点击6个以确认多边形！");
                var lng_and_lat = {
                    longitute: event.point.lng,
                    latitude: event.point.lat
                }
                $scope.polygonPoints.push(lng_and_lat);

                //画六个点之前清除所有的点
                if (++count == 1) {
                    map.clearOverlays();
                }
                //                if($scope.polygonPoints.length == 2){
                //                    drawMarker($scope.polygonPoints[0].longitute,$scope.polygonPoints[0].latitude);
                //                }
                if ($scope.polygonPoints.length <= 6) {
                    drawCircle(event.point.lng, event.point.lat);
                }
                //够6个点画多边形
                if ($scope.polygonPoints.length == 6) {
                    drawPolygon($scope.polygonPoints);
                    //                    console.log('$scope.polygonPoints');
                    //                    console.log($scope.polygonPoints);
                }
                console.log($scope.polygonPoints.length);
                console.log($scope.polygonPoints.length);
            }
        });

        //        calcArctan(points);
        //        var longLans = [];
        //        for(var i = 0;i < 6;i++){
        //            longLans.push(new  BMap.Point(points[i].longitute,points[i].latitude));
        //        }
        //        console.log('longLans');
        //        console.log(longLans);
        //
        //        polygon = new BMap.Polygon(
        //            longLans, {strokeColor:"#f50704",fillColor:"#FFFA62", strokeWeight:3, strokeOpacity:0,fillOpacity:0});
        //            map.addOverlay(polygon);

        //        for( var i = 0;i<6;i++){
        //            var pointMark =  new  BMap.Point(points[i].longitute,points[i].latitude)
        //            marker=new BMap.Marker(pointMark);
        //            map.addOverlay(marker);
        //        }
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

    $scope.address = undefined;
    $scope.searchAddress = function() {
        if (!$scope.validParam($scope.address)) {
            //      alert("搜索位置不能为空！");
            $scope.addAlert('danger', '搜索位置不能为空！');
        } else {
            var options = {
                onSearchComplete: function(results) {
                    if (local.getStatus() == BMAP_STATUS_SUCCESS) {
                        console.log('搜索成功');
                        console.log(results);
                        //                        map.setCenter(results.getPoi(0).point);
                        console.log('results.getPoi(0).point');
                        console.log(results.getPoi(0).point);
                        map.clearOverlays();
                        //map.removeOverlay(polygon);
                        //                        map.removeOverlay(marker);
                        map.setCenter(results.getPoi(0).point);

                        //                        $scope.$apply();
                        $scope.penModel.points.longitute = results.getPoi(0).point.lng;
                        $scope.penModel.points.latitude = results.getPoi(0).point.lat;

                        //画点
                        marker = new BMap.Marker(results.getPoi(0).point);
                        map.addOverlay(marker);
                        marker.addEventListener('dragging', function() {
                            map.removeOverlay(circle);
                            //map.clearOverlays();
                            var point_now = marker.getPosition();
                            $scope.penModel.points.longitute = point_now.lng;
                            $scope.penModel.points.latitude = point_now.lat;
                            circle = new BMap.Circle(new BMap.Point(point_now.lng, point_now.lat), $scope.penModel.radius);
                            circle.setFillColor("#FFFA62"); //填充颜色
                            circle.setStrokeColor("#f50704"); //边线颜色
                            map.addOverlay(circle); //增加圆
                            $scope.$digest();
                        });



                        $scope.confirmAddress = results.getPoi(0).address;

                        // var lng_and_lat = {
                        //   longitute: results.getPoi(0).point.lng,
                        //   latitude: results.getPoi(0).point.lat
                        // }
                        $scope.polygonPoints = [];
                        count = 0;
                        // $scope.polygonPoints.push(lng_and_lat);

                        $scope.$digest();
                    } else {
                        //            alert('无法搜索到到此地址！');
                        $scope.addAlert('danger', '无法搜索到到此地址！');
                    }
                }
            };

            var local = new BMap.LocalSearch(map, options);
            local.search($scope.address);
        }
    }

    $scope.circleChanged = function() {
        drawCircleMarker($scope.penModel.points.longitute, $scope.penModel.points.latitude, $scope.penModel.radius);
    }
    var drawCircleMarker = function(longitute, latitude, radius) {
        if ($scope.validParam(longitute) && $scope.validParam(latitude) && $scope.validParam(radius)) {
            var newPoint = new BMap.Point(longitute, latitude);
            //            $scope.$apply();
            map.setCenter(newPoint);
            //清除已有的覆盖层（Overlay）
            //            map.removeOverlay(circle);
            //            map.removeOverlay(marker);
            map.clearOverlays();

            //重新画圆心和圆
            marker = new BMap.Marker(newPoint);
            marker.enableDragging();
            map.addOverlay(marker);

            circle = new BMap.Circle(newPoint, radius);
            circle.setFillColor("#FFFA62"); //填充颜色
            circle.setStrokeColor("#f50704"); //边线颜色
            map.addOverlay(circle);

            marker.addEventListener('dragging', function() {
                map.removeOverlay(circle);
                //map.clearOverlays();
                var point_now = marker.getPosition();
                $scope.penModel.points.longitute = point_now.lng;
                $scope.penModel.points.latitude = point_now.lat;
                circle = new BMap.Circle(new BMap.Point(point_now.lng, point_now.lat), $scope.penModel.radius);
                circle.setFillColor("#FFFA62"); //填充颜色
                circle.setStrokeColor("#f50704"); //边线颜色
                map.addOverlay(circle); //增加圆
                $scope.$digest();
            });
        }
    }

    $scope.editPen = function(entity) {
        console.log(entity);
        $scope.penModel.pen_name = entity.pen_name;
        $scope.penModel.rec_number = entity.rec_number;
        $scope.penModel.address = entity.address;
        $scope.confirmAddress = entity.address;
        $scope.penModel.pen_id = entity.pen_id;
        //圆形围栏，先显示出来，
        if (entity.pen_type == 1) {
            if ($scope.validParam(entity.points[0].longitute) && $scope.validParam(entity.points[0].latitude)) {

                //更改围栏类型下拉框类型和锁边机类型保持一致
                $scope.penModel.pen_type = 1;
                $scope.bindSelects();

                //经纬度坐标点不为空，画圆和圆心
                $scope.penModel.points.longitute = entity.points[0].longitute;
                $scope.penModel.points.latitude = entity.points[0].latitude;
                $scope.penModel.radius = entity.radius;
                //                $scope.penModel.pen_name = entity.pen_name;
                //                $scope.penModel.rec_number = entity.rec_number;

                drawCircleMarker(entity.points[0].longitute, entity.points[0].latitude, entity.radius);

                marker.addEventListener('dragging', function() {
                    map.removeOverlay(circle);
                    //map.clearOverlays();
                    var point_now = marker.getPosition();
                    $scope.penModel.points.longitute = point_now.lng;
                    $scope.penModel.points.latitude = point_now.lat;
                    circle = new BMap.Circle(new BMap.Point(point_now.lng, point_now.lat), $scope.penModel.radius);
                    circle.setFillColor("#FFFA62"); //填充颜色
                    circle.setStrokeColor("#f50704"); //边线颜色
                    map.addOverlay(circle);
                });
            }
        } else if (entity.pen_type == 2) {
            if ($scope.validParam(entity.points[0].longitute) && $scope.validParam(entity.points[0].latitude) &&
                $scope.validParam(entity.points[1].longitute) && $scope.validParam(entity.points[1].latitude) &&
                $scope.validParam(entity.points[2].longitute) && $scope.validParam(entity.points[2].latitude) &&
                $scope.validParam(entity.points[3].longitute) && $scope.validParam(entity.points[3].latitude) &&
                $scope.validParam(entity.points[4].longitute) && $scope.validParam(entity.points[4].latitude) &&
                $scope.validParam(entity.points[5].longitute) && $scope.validParam(entity.points[5].latitude)
            ) {

                $scope.polygonPoints = entity.points;

                //更改围栏类型下拉框类型和锁边机类型保持一致
                $scope.penModel.pen_type = 2;
                $scope.bindSelects();

                $scope.penModel.pen_name = entity.pen_name;
                $scope.penModel.rec_number = entity.rec_number;
                $scope.penModel.address = entity.address;

                drawPolygon(entity.points);
            } else {
                //        alert("坐标点非法，请修正后再试！");
                $scope.addAlert('danger', '坐标点非法，请修正后再试！');
            }
        }
    }

    $scope.savepen = function() {
        if ($scope.validParam($scope.penModel.pen_type) && $scope.penModel.pen_type == 1) {
            if ($scope.validParam($scope.penModel.points.longitute) && $scope.validParam($scope.penModel.points.latitude) &&
                $scope.validParam($scope.penModel.radius) && $scope.validParam($scope.penModel.pen_name) &&
                $scope.validParam($scope.penModel.rec_number) && $scope.validParam($scope.existsIMEI) &&
                $scope.validParam($scope.penModel.pen_type) && $scope.validParam($scope.confirmAddress)) {
                $scope.penModel.imei = $scope.existsIMEI;
                $scope.penModel.address = $scope.confirmAddress;
                console.log('$scope.penModel1');
                console.log($scope.penModel);

                var pointsArry = [{
                    latitude: $scope.penModel.points.latitude,
                    longitute: $scope.penModel.points.longitute
                }];
                $scope.penModel.points = pointsArry;
                $scope.penModel.points.longitute = pointsArry[0].longitute;
                $scope.penModel.points.latitude = pointsArry[0].latitude;
                //          console.log('$scope.penModel2');
                //          console.log($scope.penModel);
                return twatch.savePensets($scope.penModel).then(function(data) {
                    if ($scope.validParam(data.pen_id)) {
                        $scope.getPensets();
                        //            alert('围栏设置成功！');
                        $scope.addAlert('success', '围栏设置成功！');
                    }
                });
            } else {
                console.log($scope.penModel);
                //        alert('请检查围栏参数是否完整！');
                $scope.addAlert('danger', '请检查围栏参数是否完整！');
            }
        } else if ($scope.validParam($scope.penModel.pen_type) && $scope.penModel.pen_type == 2) {
            if ($scope.validParam($scope.polygonPoints) && $scope.polygonPoints.length == 6 &&
                $scope.validParam($scope.penModel.pen_name) &&
                $scope.validParam($scope.penModel.rec_number) && $scope.validParam($scope.existsIMEI) &&
                $scope.validParam($scope.penModel.pen_type) && $scope.validParam($scope.confirmAddress)) {

                $scope.penModel.imei = $scope.existsIMEI;
                $scope.penModel.address = $scope.confirmAddress;
                $scope.penModel.points = $scope.polygonPoints;

                return twatch.savePensets($scope.penModel).then(function(data) {
                    if ($scope.validParam(data.pen_id)) {
                        $scope.getPensets();
                        //            alert('围栏设置成功！');
                        $scope.addAlert('success', '围栏设置成功！');

                    }
                });
            } else {
                console.log($scope.penModel);
                //        alert('请检查围栏参数是否完整！');
                $scope.addAlert('danger', '请检查围栏参数是否完整！');

            }
        }
    }

    $scope.pen_select = {
        pen_type_select: undefined
    }
    $scope.penTypechanged = function() {
        //        console.log($scope.pen_select.pen_type_select);
        $scope.penModel.pen_id = $scope.pen_select.pen_type_select.pen_id;
        $scope.penModel.pen_name = $scope.pen_select.pen_type_select.pen_name;
        $scope.penModel.rec_number = $scope.pen_select.pen_type_select.rec_number;
        $scope.penModel.address = $scope.pen_select.pen_type_select.address;
        $scope.confirmAddress = $scope.pen_select.pen_type_select.address;
        if ($scope.pen_select.pen_type_select.pen_type == 1) {
            $scope.penModel.points.longitute = $scope.pen_select.pen_type_select.points[0].longitute;
            $scope.penModel.points.latitude = $scope.pen_select.pen_type_select.points[0].latitude;
            $scope.penModel.radius = $scope.pen_select.pen_type_select.radius;

            if ($scope.validParam($scope.penModel.points.longitute) && $scope.validParam($scope.penModel.points.latitude)) {
                drawCircleMarker($scope.penModel.points.longitute, $scope.penModel.points.latitude, $scope.penModel.radius);

                marker.addEventListener('dragging', function() {
                    map.removeOverlay(circle);
                    //map.clearOverlays();
                    var point_now = marker.getPosition();
                    $scope.penModel.points.longitute = point_now.lng;
                    $scope.penModel.points.latitude = point_now.lat;
                    circle = new BMap.Circle(new BMap.Point(point_now.lng, point_now.lat), $scope.penModel.radius);
                    circle.setFillColor("#FFFA62"); //填充颜色
                    circle.setStrokeColor("#f50704"); //边线颜色
                    map.addOverlay(circle);
                });
            }
        } else if ($scope.pen_select.pen_type_select.pen_type == 2) {
            if ($scope.validParam($scope.pen_select.pen_type_select.points[0].longitute) && $scope.validParam($scope.pen_select.pen_type_select.points[0].latitude) &&
                $scope.validParam($scope.pen_select.pen_type_select.points[1].longitute) && $scope.validParam($scope.pen_select.pen_type_select.points[1].latitude) &&
                $scope.validParam($scope.pen_select.pen_type_select.points[2].longitute) && $scope.validParam($scope.pen_select.pen_type_select.points[2].latitude) &&
                $scope.validParam($scope.pen_select.pen_type_select.points[3].longitute) && $scope.validParam($scope.pen_select.pen_type_select.points[3].latitude) &&
                $scope.validParam($scope.pen_select.pen_type_select.points[4].longitute) && $scope.validParam($scope.pen_select.pen_type_select.points[4].latitude) &&
                $scope.validParam($scope.pen_select.pen_type_select.points[5].longitute) && $scope.validParam($scope.pen_select.pen_type_select.points[5].latitude)
            ) {

                //更改围栏类型下拉框类型和锁边机类型保持一致
                $scope.penModel.pen_type = 2;
                drawPolygon($scope.pen_select.pen_type_select.points);
            } else {
                //        alert("含有非法坐标点，请修正后再试！");
                $scope.addAlert('danger', '含有非法坐标点，请修正后再试！');

            }
        }

    }

    $scope.addpen = function() {
        if ($scope.validParam($scope.penModel.pen_type) && $scope.penModel.pen_type == 1) {
            if ($scope.validParam($scope.penModel.points.longitute) && $scope.validParam($scope.penModel.points.latitude) &&
                $scope.validParam($scope.penModel.radius) && $scope.validParam($scope.penModel.pen_name) &&
                $scope.validParam($scope.penModel.rec_number) && $scope.validParam($scope.existsIMEI) &&
                $scope.validParam($scope.penModel.pen_type) && $scope.validParam($scope.confirmAddress)) {
                $scope.penModel.imei = $scope.existsIMEI;
                $scope.penModel.address = $scope.confirmAddress;
                var pointsArry = [{
                    latitude: $scope.penModel.points.latitude,
                    longitute: $scope.penModel.points.longitute
                }];
                $scope.penModel.points = pointsArry;
                return twatch.addPensets($scope.penModel).then(function(data) {
                    if ($scope.validParam(data.pen_id)) {
                        $scope.getPensets();
                        //            alert('围栏设置成功！');
                        $scope.addAlert('success', '围栏设置成功！');

                    }
                });
            } else {
                console.log($scope.penModel);
                //        alert('请检查围栏参数是否完整！');
                $scope.addAlert('danger', '请检查围栏参数是否完整！');
            }
        } else if ($scope.validParam($scope.penModel.pen_type) && $scope.penModel.pen_type == 2) {
            if ($scope.validParam($scope.polygonPoints) && $scope.polygonPoints.length == 6 &&
                $scope.validParam($scope.penModel.radius) && $scope.validParam($scope.penModel.pen_name) &&
                $scope.validParam($scope.penModel.rec_number) && $scope.validParam($scope.existsIMEI) &&
                $scope.validParam($scope.penModel.pen_type) && $scope.validParam($scope.confirmAddress)) {

                $scope.penModel.imei = $scope.existsIMEI;
                $scope.penModel.address = $scope.confirmAddress;
                $scope.penModel.points = $scope.polygonPoints;

                return twatch.addPensets($scope.penModel).then(function(data) {
                    if ($scope.validParam(data.pen_id)) {
                        $scope.getPensets();
                        //            alert('围栏设置成功！');
                        $scope.addAlert('success', '围栏设置成功！');

                    }
                });
            } else {
                console.log($scope.penModel);
                //        alert('请检查围栏参数是否完整！');
                $scope.addAlert('danger', '请检查围栏参数是否完整！');
            }
        }

    }



};
