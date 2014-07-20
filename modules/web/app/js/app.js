/*
Top level module for the app
*/
var smartFormsApp = angular.module('smartforms', [
     'sfViewDefs',
     'sfViewSearch',
     'sfDatasets',
     'sfInstance',
     'ngRoute'
]);

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
      when('/instances/:instanceid/viewdef/:viewdefid', {
         templateUrl: 'instance.html',
         controller: 'instanceCtrl',
         resolve: {
           'viewDef':function($http, $route){
               return $http.get('/rest/datadefs/views/' + $route.current.params.viewdefid);
           },
           'dataDef':function($http, $route){
               return $http.get('/rest/datadefs/view/' + $route.current.params.viewdefid);
           },
           'instance':function($http, $route){
               return $http.get('/rest/instances/' + $route.current.params.instanceid);
           }
         }
      }).
      when('/viewdef/:viewdefid', {
        templateUrl: 'viewDef.html',
        controller: 'viewDefCtrl',
        resolve: {
          'viewDef':function($http, $route){
              return $http.get('/rest/datadefs/views/' + $route.current.params.viewdefid);
          },
          'dataDef':function($http, $route){
            return $http.get('/rest/datadefs/view/' + $route.current.params.viewdefid);
          }
        }
      }).
      when('/search/:query', {
        templateUrl: 'viewSearchResults.html',
        controller: 'viewSearchCtrl'
      }).
      otherwise({
        redirectTo: '/home'
      });
  }]);