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
            templateUrl: 'partials/landing.html',
            controller: 'viewSearchCtrl',
            resolve : {'params' : function(){return {'query' : 'Mark'};}} //FIXME
          }).
      when('/dataset/:datasetid', {
        templateUrl: 'partials/viewDefList.html',
        controller: 'viewDefListCtrl'
          }).
      when('/instances/:instanceid/viewdef/:viewdefid', {
         templateUrl: 'partials/instance.html',
         controller: 'instanceCtrl',
         resolve: {
           'viewDef':function($http, $route){
               return $http.get('/rest/datadefs/views/' + $route.current.params.viewdefid);
           },
           'dataDef':function($http, $route){
               return $http.get('/rest/datadefs?viewId=' + $route.current.params.viewdefid);
           },
           'instance':function($http, $route){
               return $http.get('/rest/instances/' + $route.current.params.instanceid);
           }
         }
      }).
      when('/viewdef/:viewdefid/query/:instanceQuery', {
        templateUrl: 'partials/viewDef.html',
        controller: 'viewDefCtrl',
        resolve: {
          'viewDef':function($http, $route){
              return $http.get('/rest/datadefs/views/' + $route.current.params.viewdefid);
          },
          'dataDef':function($http, $route){
              return $http.get('/rest/datadefs?viewId=' + $route.current.params.viewdefid);
          }
        }
      }).
      when('/search/:query', {
        templateUrl: 'partials/viewSearchResults.html',
        controller: 'viewSearchCtrl',
        resolve : {
           'params' : function($route){
              return $route.current.params;
           }
        }
      }).
      otherwise({
        redirectTo: '/home'
      });
  }]);