app.controller('NewsController', function ($scope, $http) {

    $scope.model;

    $scope.$on('applicationStarted',function() {
        $scope.updateNews();
    });

    $scope.updateNews = function(){
        $http.get(serverUrl + '/chat/showNewsItems').
            success(function(data, status, headers, config) {
                $scope.model = data;
            }).
            error(function(data, status, headers, config) {
                alert("error: updateNews")
            });
    };

});