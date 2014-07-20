var searchResults = angular.module('sfViewSearch', []);

searchResults.controller('viewSearchCtrl', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams){
    $http.get('/rest/datadefs/views?query=' + $routeParams.query).success(function(data){
        $scope.viewDefs = data;
    });

}]);

searchResults.controller('searchCtrl', ['$scope', '$location', function($scope, $location){
    /**
     * Search handler
     */
    $scope.performSearch = function(){
        $location.path( 'search/' + $scope.query );
    }

    /**
     *Keyboard binding to launch search
     */
    $scope.handleKeyPress = function(ev) {
      if (ev.which==13){
         //console.log("about to perform search");
         $scope.performSearch();
      }
    }
}]);
