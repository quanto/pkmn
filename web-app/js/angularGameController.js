var app = angular.module('game', []);

app.controller('GameController', function ($scope, $http, $rootScope) {

    $scope.view;

    $scope.$on('applicationStarted',function() {
        $scope.updateView();
    });

    $scope.updateView = function(){
        $http.get(serverUrl + '/game/jsonView').
            success(function(data, status, headers, config) {
                $scope.view = data.view;

                if ($scope.view == 'ShowMap'){
                    $rootScope.$broadcast('initMap', data);
                }
                else if ($scope.view == 'ShowMarket'){
                    $rootScope.$broadcast('initMarket', data);
                }
                else if ($scope.view == 'ShowComputer'){
                    $rootScope.$broadcast('initComputer', data);
                }
                else if ($scope.view == 'Battle'){
                    $rootScope.$broadcast('initBattle', data);
                }
                else if ($scope.view == 'ShowPvpSelect'){
                    $rootScope.$broadcast('initPvpSelect', data);
                }

            }).
            error(function(data, status, headers, config) {
                alert("error: updateView")
            });
    };

    $scope.$on('keypress', function(onEvent, keypressEvent) {

        var key = 0;
        if (keypressEvent.keyCode>0){
            key = keypressEvent.keyCode;
        }
        else if (keypressEvent.which > 0){
            key = keypressEvent.which;
        }

        if ($scope.view == 'ShowMap'){
            $rootScope.$broadcast('mapEvent', key);
        }

    });




});
