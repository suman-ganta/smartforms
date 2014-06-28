var dynFormControllers = angular.module('dyn-forms', ['dynform']);

dynFormControllers.controller('dynFormCtrl', ['$scope', function($scope){
	$scope.myFormTemplate = {
        "first": {
            "type": "text",
            "label": "First Name"
        },
        "last": {
            "type": "text",
            "label": "Last Name"
        },
        "address": {
            "type": "text",
            "label": "Address"
        },
        "dob": {
            "type": "date",
            "label": "Date of birth",
            "placeholder": "person's DOB"
          },
        "image": {
	        "type": "image",
	        "label": "image",
	        "source": "http://angularjs.org/img/AngularJS-large.png"
        },
        "legend": {
            "type": "legend",
        },
        "reset": {
            "type": "reset",
            "label": "Reset"
        },
        "submit": {
            "type": "submit",
            "label": "Send"
        },
    };
    //this is model object, but dynamic-forms still mandates it to declare the object (although regular model do not require it)
    $scope.formData = {};
    $scope.submitForm = function(){
    	alert('Form submitted' + angular.toJson($scope.formData));
    }
}]);

dynFormControllers.filter('pretty', function() {
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