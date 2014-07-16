var formRenderer = angular.module('sfViewDefs', ['dynform']);

formRenderer.controller('viewDefCtrl', ['$scope', '$http', 'viewDef', 'dataDef', function($scope, $http, viewDef, dataDef){
    $scope.viewDefTemplate = JSON.parse(viewDef.data.viewDefJson);
    $scope.viewDefTitle = viewDef.data.title;
    var data = dataDef.data;
    $scope.dataSetName = data.name;
    var typedef = JSON.parse(data.dataFieldsJson);
    var keys = Object.keys(typedef);
    for(var i = 0; i < keys.length; ++i){
       if($scope.viewDefTemplate.hasOwnProperty(keys[i])){
        $scope.viewDefTemplate[keys[i]]['type'] = typedef[keys[i]];
       }
    }
    console.log($scope.viewDefTemplate);
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