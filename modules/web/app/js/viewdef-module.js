var formRenderer = angular.module('viewDefModule', ['dynform']);

formRenderer.controller('viewDefCtrl', ['$scope', '$http', 'viewDefService', 'dataDefs', function($scope, $http, viewDefService, dataDefs){
    $scope.viewDefTemplate = JSON.parse(viewDefService.data.viewDefJson);
    $scope.viewDefTitle = viewDefService.data.title;
    var data = dataDefs.data;
    for(var i = 0; i < data.length; ++i) {
        if(data[i].id == viewDefService.data.dsId){
            $scope.dataSetName = data[i].name;
            var typedef = JSON.parse(data[i].dataFieldsJson);
            var keys = Object.keys(typedef);
            for(var i = 0; i < keys.length; ++i){
               if($scope.viewDefTemplate.hasOwnProperty(keys[i])){
                $scope.viewDefTemplate[keys[i]]['type'] = typedef[keys[i]];
               }
            }
            console.log($scope.viewDefTemplate);
            break;
        }
    }
    //this is model object, but dynamic-forms still mandates it to declare the object
    //(although regular model do not require it)
    $scope.formData = {};
    $scope.submitForm = function(){
    	alert('Form submitted' + angular.toJson($scope.formData));
    }
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