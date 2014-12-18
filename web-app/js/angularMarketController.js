app.controller('MarketController', function ($scope, $http) {

    $scope.$on('initMarket',function(event, data) {
        $scope.init(data);
    });

    $scope.init = function(data){
        $scope.model = data;
    };

    $scope.buyItem = function(itemId){
        $http.get(serverUrl + '/market/buy?id=' + itemId).
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: buyItem");
            });
    };

    $scope.exit = function(){
        $http.get(serverUrl + '/market/exit').
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: exit");
            });
    };

});