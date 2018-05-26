'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.page.exhibition.Ctrl');



/**
 * Exhibition controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.page.exhibition.Ctrl.$inject = [
  '$scope',
  '$http',
  '$q',
  'i18nService',
  'constants',
  'page'
];
jxmgrsec.main.page.exhibition.Ctrl = function($scope, $http, i18nService, $q, constants, $modal, ModalService, page) {

  this.label = 'some label from category template controller';
  var ctrl = this;

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: 'exhibition_resource_id',
      name: '宣传页资源编号',
      width: '20%',
      pinnedLeft: true
    }, {
//      field: 'pic_url',
      name: '图片链接',
      width: '50%',
      cellTemplate:'<a target="_blank" href="{{row.entity.pic_url}}"><img ng-src="{{row.entity.pic_url}}" style="width: 50px"></a>'
    }, {
      name: '操作',
      width: '30%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.popup(row.entity)" >编辑地址</button></div><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.deleteExhibition(row.entity)">删除宣传页</button></div></div>'
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
        $scope.exhibition.current_page = newPage - 1;
        $scope.exhibition.page_size = pageSize;

        $scope.getExhibition();
      });
    }
  };


  $scope.gridOptions.data = [{
    'category': '手环',
    'terminal_number': '01234567',
    'model_category': '系统类型'

  }, {
    'category': '血压仪',
    'terminal_number': '01234567',
    'model_category': '系统类型'
  }, {
    'category': '手环',
    'terminal_number': '01234567',
    'model_category': '自定义类型'
  }, {
    'category': '血糖仪',
    'terminal_number': '01234567',
    'model_category': '自定义类型'
  }];

  var query_date = -1;
  $scope.exhibition = {
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }
  $scope.getExhibition = function() {
    page.getExhibitionResources($scope.exhibition).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.resources;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.exhibition.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }
  $scope.getExhibition();

  var refreshExhibition = {
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }
  var refresh = function() {
    page.getExhibitionResources(refreshExhibition).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.resources;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.exhibition.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }

  $scope.deleteExhibition = function(entity) {
    page.deleteExhibitionResources(entity.exhibition_resource_id).then(function(res) {
      console.log(res);
      refresh();
    }, function(error) {
      console.log(error);
    });
  }

  $scope.popup = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/edit_exhi_address_modal/edit_exhi_address_modal.html',
      controller: 'EditExhiAddressModalCtrl',
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
      refresh();
    });

  };

  ctrl.add = function() {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/add_page_modal/add_page_modal.html',
      controller: 'AddPageModalCtrl',

    });
    modalInstance.result.then(function() {

    }, function() {
      refresh();
    });
  }

};




/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `exhibition.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.page.exhibition.Ctrl.prototype.log = function(text) {
  console.log(text);
};
