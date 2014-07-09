/*
Top level module for the app
*/
var smartFormsApp = angular.module('smartforms', ['viewDefRender', 'ngRoute']);

smartFormsApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/viewdef', {
        templateUrl: 'viewDefRenderer.html',
        controller: 'viewDefRenderCtrl',
        resolve: {
          'viewDefService':function(viewDefService){
            // viewDefService will also be injectable in your controller,
            //if you don't want this you could create a new promise with the $q service
            return viewDefService.promise;
          }
        }
      }).
      otherwise({
        redirectTo: '/viewdef'
      });
  }]);

smartFormsApp.service('viewDefService', ['$http', function($http){
    var myData;
    var promise = $http.get('/rest/datadefs/views/vd11').success(function(data) {
            console.log('XHR returned!!');
            myData = JSON.parse(data.viewDefJson);
        });
    return {
      promise:promise,
      setData: function (data) {
          myData = data;
      },
      getData: function () {
          return myData;
      }
    };
}]);
