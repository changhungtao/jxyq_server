'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.page.news.Ctrl');



/**
 * News controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.page.news.Ctrl.$inject = [
  '$scope',
  '$http',
  '$q',
  'i18nService',
  'constants',
  'page'
];
jxmgrsec.main.page.news.Ctrl = function($scope, $http, i18nService, $q, constants, $modal, ModalService, page) {

  this.label = 'some label from category template controller';
  var ctrl = this;

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
//      field: 'pic_url',
      name: '图片链接',
      width: '20%',
      cellTemplate:'<a target="_blank" href="{{row.entity.pic_url}}"><img ng-src="{{row.entity.pic_url}}" style="width: 50px"></a>',
      pinnedLeft: true
    }, {
//      field: 'news_link',
      name: '新闻链接',
      width: '45%',
      cellTemplate:'<a target="_blank" href="{{row.entity.news_link}}"style="font-size: 12px">{{row.entity.news_link}}</a>'
    }, {
      name: '操作',
      width: '30%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.popup(row.entity)" >编辑</button></div><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.deleteNews(row.entity)">删除宣传页</button></div></div>'
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
        $scope.news.current_page = newPage - 1;
        $scope.news.page_size = pageSize;

        $scope.getNews();
      });
    }
  };


  /*$scope.gridOptions.data = [{
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
  }];*/

  var query_date = -1;
  $scope.news = {
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }
  $scope.getNews = function() {
    page.getNewResources($scope.news).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.news_resources;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.news.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }
  $scope.getNews();

  var refreshnews = {
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }
  var refresh = function() {
    page.getNewResources(refreshnews).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.news_resources;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.news.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }

  $scope.deleteNews = function(entity) {
    page.deleteNewResources(entity.news_resource_id).then(function(res) {
      console.log(res);
      refresh();

    }, function(error) {
      console.log(error);
    });
  }

  $scope.popup = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/edit_news_modal/edit_news_modal.html',
      controller: 'EditNewsModalCtrl',
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

  ctrl.addNews = function() {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/add_news_modal/add_news_modal.html',
      controller: 'AddNewsModalCtrl',

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
 * Example function that we'd like to access in our `news.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.page.news.Ctrl.prototype.log = function(text) {
  console.log(text);
};
