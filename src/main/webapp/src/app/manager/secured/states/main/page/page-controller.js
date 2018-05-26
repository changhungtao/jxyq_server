'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.page.Ctrl');

jxmgrsec.main.page.Ctrl.$inject = [
  "$window",
  "basic"
];

/**
 * Page controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.page.Ctrl = function($scope, $window, basic, $timeout) {

  /**
   * @type {String}
   * @nocollapse
   */
  var ctrl = this;
  this.label = 'some label from page controller';


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

  $scope.validParam = function(param) {
    if (param != undefined && param != null && param != "") {
      return true;
    }
    return false;
  }


};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.page.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.page.Ctrl.prototype.log = function(text) {
  console.log(text);
};
