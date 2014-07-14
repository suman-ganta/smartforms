var datasetsModule = angular.module('datasetsModule', []);

datasetsModule.controller('dataSetsCtrl', ['$scope', '$http', function($scope, $http){
     console.log('dataSetsCtrl executed');
     $http.get('/rest/datadefs').success(function(data) {
          console.log('XHR datasets returned!!');
          $scope.datasets = data;
          //$scope.$apply();
     });
}]);

datasetsModule.controller('viewDefListCtrl', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams){
    $http.get('/rest/datadefs/' + $routeParams.datasetid + '/views').success(function(data){
        $scope.viewDefs = data;
        //$scope.$apply();
    });
}]);
