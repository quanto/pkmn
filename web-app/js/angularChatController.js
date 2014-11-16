app.controller('ChatController', function ($scope, $http, $timeout) {

    $scope.model = [];
    $scope.lastChatId = 0;
    $scope.scope = 'Map'

    $scope.$on('applicationStarted',function() {
        $scope.updateChat();
        $scope.updateChatLoop();
    });

    $scope.updateChatLoop = function(){
        $timeout(function() {
            $scope.updateChat();
            $scope.updateChatLoop();
        }, 8000);
    };

    $scope.updateChat = function(){
        $http.get(serverUrl + '/chat/index?lastChatId=' + $scope.lastChatId).
            success(function(data, status, headers, config) {
                // Save last id
                if (data.length>0){
                    $scope.lastChatId = data[data.length-1].id;
                }
                $scope.model = $scope.model.concat(data);

            }).
            error(function(data, status, headers, config) {
                alert("error: updateChat")
            });
    };

    $scope.sendMessage = function(){

        if($scope.message != "")
        {
            $http({
                method: 'POST',
                url: serverUrl + '/chat/send?',
                data: $.param({chatScope: $scope.scope, message: $scope.message}),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function(data, status, headers, config) {
                $scope.message = '';
                $scope.updateChat();
            }).
            error(function(data, status, headers, config) {
                alert("error: sendMessage")
            });
        }
    };

});