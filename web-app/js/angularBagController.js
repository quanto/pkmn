app.controller('BagController', function ($scope, $http) {

    $scope.model;
    $scope.tab = 'usableItems';

    $scope.$on('applicationStarted',function() {
        $scope.updateBag();
    });

    $scope.updateBag = function(){

        $http.get(serverUrl + '/bag/getItems').
            success(function(data, status, headers, config) {

                $scope.model = data;
            }).
            error(function(data, status, headers, config) {
                alert("error: updateBag")
            });
    };

});
