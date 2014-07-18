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
    //Always add submit button for viewDef
    $scope.viewDefTemplate['legend'] = {"type" : "legend"};
    $scope.viewDefTemplate['submit'] = {"type" : "submit", "label" : "Send"};
    $scope.viewDefTemplate['reset'] = {"type" : "reset", "label" : "Reset"};

    //console.log($scope.viewDefTemplate);
    //this is model object, but dynamic-forms still mandates it to declare the object
    //(although regular model do not require it)
    $scope.formData = {};

    //These are needed in scope for view to access them
    $scope.dataDef = dataDef.data;
    $scope.viewDef = viewDef.data;

    /**
     * Handle form submission. Leads to creation of new data instance
    */
    $scope.submitForm = function(){
        $http.post('/rest/instances', {"dsId":$scope.dataDef.id, "dataAsJson":JSON.stringify($scope.formData)})
           .success(function(data){
               alert('new instance! ' + data.id);
           });
    }

    $scope.searchResult = {};

    /**
     * Search handler
     */
    $scope.performSearch = function(){
        $http.get('/rest/instances?viewDefId=' + viewDef.data.id + '&query=' + $scope.instanceQuery).success(
            function(instances){
                $scope.searchResult = instances;
            }
        ).error(function(data, status, headers, config) {
           alert('response code: ' + status);
        });
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

/**
 *Filter to prettify javascript objects to print
 */

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