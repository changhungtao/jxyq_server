'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.number.Ctrl');



/**
 * main.twatch.number controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.twatch.number.Ctrl.$inject = [
  "$rootScope",
  "$scope",
  "$http",
  "$modal",
  "$state",
  '$filter',
  '$q',
  "i18nService",
  'constants',
  'twatch',
  '$timeout'
];

jxmgrsec.main.twatch.number.Ctrl = function(
  $rootScope,
  $scope,
  $http,
  $modal,
  $state,
  $filter,
  $q,
  i18nService,
  constants,
  twatch,
  $timeout
) {
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.twatch.number controller';
  var ctrl = this;  

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: false,
//    useExternalSorting: false,
    columnDefs: [{
      field: 'key',
      name: '按键',
      width: '15%',
      pinnedLeft: true
    }, {
      field: 'name',
      name: '称呼',
      width: '15%',      
      pinnedLeft: true
    }, {
      field: 'number',
      name: '号码',
      width: '15%',
      pinnedLeft: true
    }, {
      name: '操作',
      width: '15%',
      cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.modifyNumber(row.entity)">修改亲情号码</button></div></div>',
      pinnedRight: true
    }, {
      field: 'description',
      name: '说明',
      width: '40%',
      pinnedLeft: true
    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
    }
  }

  i18nService.setCurrentLang('zh-CN');

  

  ctrl.imei1 = $rootScope.imei;
  ctrl.imei2 = undefined;

  $scope.queryFile = function(){
    twatch.getNumbers({imei:ctrl.imei1}).then(function(data){
     
      $scope.gridOptions.data = [
    {
      'key': 1,
      'name': $filter('mapTwatchQingName')(data.name1),
      'number': data.qing1,
      'description': '第1监护人号码对应8点位按键'
    },
    {
      'key': 2,
      'name': $filter('mapTwatchQingName')(data.name2),
      'number': data.qing2,
      'description': '第2监护人号码对应2点位按键'
    },
    {
      'key': 3,
      'name': $filter('mapTwatchQingName')(data.name3),
      'number': data.qing3,
      'description': '第3监护人号码对应4点位按键'
    },
    {
      'key': 4,
      'name': $filter('mapTwatchQingName')(data.name4),
      'number': data.qing4,
      'description': '第4监护人号码对应开机键'
    },
    {
      'key': 5,
      'name': $filter('mapTwatchSosName')(data.sosname+1),
      'number': data.sos,
      'description': 'SOS 长按2点位按键'
    }
  ];
  ctrl.imei2 = ctrl.imei1;
//  $rootScope.imei = ctrl.imei1;
  $rootScope.imeiChanged(ctrl.imei1);

  });    

  }  
  $scope.submit = function(){
     var gridData = $scope.gridOptions.data;     
    var jsonData = {
      imei:ctrl.imei2,
      name1:$scope.guardPeople(gridData[0].name),
      qing1:gridData[0].number,
      name2:$scope.guardPeople(gridData[1].name),
      qing2:gridData[1].number,
      name3:$scope.guardPeople(gridData[2].name),
      qing3:gridData[2].number,
      name4:$scope.guardPeople(gridData[3].name),
      qing4:gridData[3].number,
      sosname:0,
      sos:gridData[4].number
    }
    twatch.postNumbers(jsonData).then(function(succ){
      $scope.addAlert("success", "提交成功"); 
    },function(error){
      console.log(error);
      $scope.addAlert("danger", "提交失败，请稍后再试！");
    });
  }

  $scope.guardPeople = function(name){
    var res ;
    switch(name){   
      case "爸爸":
      res=1;break;
      case "妈妈":
      res=2;break;
      case "爷爷":
      res=3;break;
      case "奶奶":
      res=4;break;
      case "老师":
      res=5;break;
      case "同学":
      res=6;break;
      case "电话":
      res=7;break;                         
    }
    return res;
  }
  $scope.modifyNumber = function(entity){
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/modify_number_modal/modify_number_modal.html',
      controller: 'ModifyNumberModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      
    });
  }

  //Alert
    $scope.alerts = [];
    $scope.addAlert = function (type, msg) {
        var alert = {'type': type, 'msg': msg};
        $scope.alerts.push(alert);
        $timeout(function(){
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
