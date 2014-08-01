var searchResults = angular.module('sfViewSearch', ['ui.bootstrap', 'ngCookies']);

searchResults.controller('viewSearchCtrl', ['$scope', '$http', 'params', function($scope, $http, params){
    $http.get('/rest/datadefs/views?query=' + params.query).success(function(data){
        $scope.viewDefs = data;
    });
}]);

searchResults.controller('searchCtrl', ['$scope', '$location', '$modal', '$log', 'Auth', function($scope, $location, $modal, $log, Auth){

    if(typeof $scope.viewDef != 'undefined'){
        $scope.searchPlaceHolder = 'Search ' + $scope.viewDef.id;
    }else{
        $scope.searchPlaceHolder = 'Search Views';
    }

    $log.info('Auth.user ' + Auth.user);
    $scope.signedin = (typeof Auth.user != 'undefined');

    /**
     * Search handler
     */
    $scope.performSearch = function(){
        if(typeof $scope.viewDef != 'undefined'){
            $location.path( '/viewdef/' + $scope.viewDef.id + '/query/' + $scope.query );
        }else{
            $location.path( 'search/' + $scope.query );
        }
    }

    /**
     *Keyboard binding to launch search
     */
    $scope.handleKeyPress = function(ev) {
      if (ev.which==13){
         $scope.performSearch();
      }
    }

    /**
     * Login handling
     */
    $scope.signin = function (size) {
       var modalInstance = $modal.open({
         templateUrl: 'partials/loginDialog.html',
         controller: 'loginInstanceCtrl',
         size: size
       });

       modalInstance.result.then(function (selectedItem) {
         $scope.selected = selectedItem;
         $scope.signedin = true;
       }, function () {
         $log.info('Modal dismissed at: ' + new Date());
       });
    };

    $scope.signout = function (){
        $scope.signedin = false;
    }
}]);

// Please note that $modalInstance represents a modal window (instance) dependency.
// It is not the same as the $modal service used above.
searchResults.controller('loginInstanceCtrl', ['$scope', '$modalInstance', '$http', 'Base64',
  function ($scope, $modalInstance, $http, Base64) {
      $scope.loginModel = {};

      $scope.ok = function () {
        //make rest call to login
        var encoded = Base64.encode($scope.loginModel.user + ':' + $scope.loginModel.password);
        $http({method:'GET', url:'/rest/users/login', headers:{'Authorization' : 'Basic ' + encoded}}).success(function(data){
            $modalInstance.close('outcome');
        });
      };

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };
  }
]);

searchResults.factory('Auth', ['$cookieStore', function ($cookieStore) {
    var _user;
    return {
        user : _user,
        set: function (_user) {
            // you can retrieve a user set by another page, like login successful page.
            existing_cookie_user = $cookieStore.get('JSESSIONID');
            _user =  _user || existing_cookie_user;
            $cookieStore.put('JSESSIONID', _user);
        },
        remove: function () {
            $cookieStore.remove('JSESSIONID', _user);
        }
    };
}]);

searchResults.run(['Auth', '$http', function run(Auth, $http) {
    $http({method:'GET', url:'/rest/users/login'}).success(function(data){
        Auth.set(data);
    }).error(function(data){console.log('FAILED');});

    //Auth.set({});
}]);


//Base64 service
searchResults.factory('Base64', function() {
    //factory function body that constructs Base64 service
   var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
   return {
       encode: function (input) {
           var output = "";
           var chr1, chr2, chr3 = "";
           var enc1, enc2, enc3, enc4 = "";
           var i = 0;

           do {
               chr1 = input.charCodeAt(i++);
               chr2 = input.charCodeAt(i++);
               chr3 = input.charCodeAt(i++);

               enc1 = chr1 >> 2;
               enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
               enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
               enc4 = chr3 & 63;

               if (isNaN(chr2)) {
                   enc3 = enc4 = 64;
               } else if (isNaN(chr3)) {
                   enc4 = 64;
               }

               output = output +
                   keyStr.charAt(enc1) +
                   keyStr.charAt(enc2) +
                   keyStr.charAt(enc3) +
                   keyStr.charAt(enc4);
               chr1 = chr2 = chr3 = "";
               enc1 = enc2 = enc3 = enc4 = "";
           } while (i < input.length);

           return output;
       },

       decode: function (input) {
           var output = "";
           var chr1, chr2, chr3 = "";
           var enc1, enc2, enc3, enc4 = "";
           var i = 0;

           // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
           var base64test = /[^A-Za-z0-9\+\/\=]/g;
           if (base64test.exec(input)) {
               alert("There were invalid base64 characters in the input text.\n" +
                   "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                   "Expect errors in decoding.");
           }
           input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

           do {
               enc1 = keyStr.indexOf(input.charAt(i++));
               enc2 = keyStr.indexOf(input.charAt(i++));
               enc3 = keyStr.indexOf(input.charAt(i++));
               enc4 = keyStr.indexOf(input.charAt(i++));

               chr1 = (enc1 << 2) | (enc2 >> 4);
               chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
               chr3 = ((enc3 & 3) << 6) | enc4;

               output = output + String.fromCharCode(chr1);

               if (enc3 != 64) {
                   output = output + String.fromCharCode(chr2);
               }
               if (enc4 != 64) {
                   output = output + String.fromCharCode(chr3);
               }

               chr1 = chr2 = chr3 = "";
               enc1 = enc2 = enc3 = enc4 = "";

           } while (i < input.length);

           return output;
       }
   };
});