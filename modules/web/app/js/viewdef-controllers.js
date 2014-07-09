var formRenderer = angular.module('viewDefRender', ['dynform']);

formRenderer.controller('viewDefRenderCtrl', ['$scope', '$http', 'viewDefService', function($scope, $http, viewDefService){
    console.log('Promise is now resolved: ');
    $scope.viewDefTemplate = JSON.parse(viewDefService.data.viewDefJson);;
    //this is model object, but dynamic-forms still mandates it to declare the object
    //(although regular model do not require it)
    $scope.formData = {};
    $scope.submitForm = function(){
    	alert('Form submitted' + angular.toJson($scope.formData));
    }
    console.log("completed!");
}]);

formRenderer.filter('pretty', function() {
    return function (input) {
      var temp;
      try {
        temp = angular.fromJson(input);
      }
      catch (e) {
        temp = input;
      }
      
      return angular.toJson(temp, true);
    };
});