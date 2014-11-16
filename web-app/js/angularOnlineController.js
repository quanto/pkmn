app.controller('OnlineController', function ($scope, $http) {

    $scope.model;

    $scope.$on('applicationStarted',function() {
        $scope.updateOnline();
    });

    $scope.updateOnline = function(){

        $http.get(serverUrl + '/online/index').
            success(function(data, status, headers, config) {

                $scope.model = data;
            }).
            error(function(data, status, headers, config) {
                alert("error: updateOnline")
            });
    };

    $scope.invitePlayer = function(username){

        $http({
            method: 'POST',
            url: serverUrl + '/online/invite',
            data: $.param({playerName: username}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
                $scope.inviteFeedback = data;
        }).
        error(function(data, status, headers, config) {
            alert("error: invitePlayer")
        });
    };

    $scope.acceptInvite = function(id){

        $http({
            method: 'POST',
            url: serverUrl + '/online/acceptInvite',
            data: $.param({id: id}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            $scope.updateOnline();
        }).error(function(data, status, headers, config) {
            alert("error: acceptInvite")
        });
    };

    $scope.declineInvite = function(id){

        $http({
            method: 'POST',
            url: serverUrl + '/online/declineInvite',
            data: $.param({id: id}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data, status, headers, config) {
            $scope.updateOnline();
        }).error(function(data, status, headers, config) {
            alert("error: acceptInvite")
        });
    };

    $scope.setPrivateMessage = function(username){
        $('#chatMessage').val('@' +username + ' ');
        $('#chatMessage').putCursorAtEnd();
    };

});
