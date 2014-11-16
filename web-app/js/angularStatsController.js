app.controller('StatsController', function ($scope, $http) {

    $scope.model;

    $scope.$on('applicationStarted',function() {
        $scope.updateStats();
    });

    $scope.updateStats = function(){

        $http.get(serverUrl + '/stats').
            success(function(data, status, headers, config) {

                $scope.model = data;
            }).
            error(function(data, status, headers, config) {
                alert("error: updateStats")
            });
    };

});
