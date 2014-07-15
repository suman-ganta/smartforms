/*
Top level module for the app
*/
var smartFormsApp = angular.module('smartforms', ['viewDefModule', 'datasetsModule', 'ngRoute']);

smartFormsApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
            templateUrl: 'home.html'
          }).
      when('/dataset/:datasetid', {
            templateUrl: 'viewDefList.html',
            controller: 'viewDefListCtrl'
          }).
      when('/viewdef/:viewdefid', {
        templateUrl: 'viewDef.html',
        controller: 'viewDefCtrl',
        resolve: {
          'viewDefService':function($http, $route){
              return $http.get('/rest/datadefs/views/' + $route.current.params.viewdefid);
          },
          'dataDefs':function($http, $route){
            return $http.get('/rest/datadefs');
          }
        }
      }).
      otherwise({
        redirectTo: '/home'
      });
  }]);