app.controller('PvpSelectController', function ($scope, $http, $timeout) {

    $scope.model;

    $scope.$on('initPvpSelect',function(event, data) {
        $scope.init(data);
    });

    $scope.init = function(data){
        $scope.model = data;

        // Wait until someone has accepted
        if ($scope.model.waiting){
            $timeout(function(){
                $scope.updateView();
            },2000);
        }

    };

    $scope.exit = function(){
        $http.get(serverUrl + '/pvpSelect/exit').
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: exit");
            });
    };

    $scope.createInvite = function(){
        $http.get(serverUrl + '/pvpSelect/createInvite').
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: exit");
            });
    };

    $scope.acceptInvite = function(inviteNr){
        $http.get(serverUrl + '/pvpSelect/acceptInvite/'+inviteNr).
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: exit");
            });
    };

    $scope.cancelInvite = function(){
        $http.get(serverUrl + '/pvpSelect/cancelInvite').
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: exit");
            });
    };

    $scope.reload = function(){
        $scope.updateView();
    }


});