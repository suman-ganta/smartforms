var instanceRenderer = angular.module('sfInstance', ['dynform']);

instanceRenderer.controller('instanceCtrl', ['$scope', '$http', 'viewDef', 'dataDef', 'instance', function($scope, $http, viewDef, dataDef, instance){
    instData = JSON.parse(instance.data.dataAsJson);
    viewDefTemplate = JSON.parse(viewDef.data.viewDefJson);
    var data = dataDef.data;
    $scope.instanceTitle = viewDef.data.title;
    var typedef = JSON.parse(data.dataFieldsJson);
    var keys = Object.keys(typedef);
    var submitRequired = false;
    for(var i = 0; i < keys.length; ++i){
       if(viewDefTemplate.hasOwnProperty(keys[i])){
          viewDefTemplate[keys[i]]['type'] = typedef[keys[i]];
          viewDefTemplate[keys[i]]['val'] = instData[keys[i]];
          viewDefTemplate[keys[i]]['readonly'] = !viewDefTemplate[keys[i]]['mutable'];
          viewDefTemplate[keys[i]]['required'] = viewDefTemplate[keys[i]]['optionality'] == 'required';
          submitRequired = submitRequired  || viewDefTemplate[keys[i]]['mutable'];
       }
    }

    if(submitRequired){
       viewDefTemplate['legend'] = {"type" : "legend"};
       viewDefTemplate['submit'] = {"type" : "submit", "label" : "Send"};
       viewDefTemplate['reset'] = {"type" : "reset", "label" : "Reset"};
    }
    console.log(viewDefTemplate);
    $scope.instanceTemplate = viewDefTemplate;

    //attach instance to scope to submit it for update action
    $scope.instance = instance.data;

    //this is model object, but dynamic-forms still mandates it to declare the object
    //(although regular model do not require it)
    $scope.formData = {};
    $scope.submitForm = function(){
    	$scope.instance.dataAsJson = JSON.stringify($scope.formData);
    	$http.put('/rest/instances',$scope.instance)
    	   .success(function(data){
    	       alert('updated successfully!');
    	   });
    }
}]);
