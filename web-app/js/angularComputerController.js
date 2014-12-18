app.controller('ComputerController', function ($scope, $http) {

    $scope.model;
    $scope.selectedPokemon;

    $scope.$on('initComputer',function(event, data) {
        $scope.deselectPokemon();
        $scope.init(data);
    });

    $scope.init = function(data){
        $scope.model = data;
    };

    $scope.deposit = function(ownerPokemonId){
        $http.get(serverUrl + '/party/deposit?id='+ownerPokemonId).
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: deposit")
            });
    };

    $scope.withdraw = function(ownerPokemonId){
        $http.get(serverUrl + '/party/withdraw?id='+ownerPokemonId).
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: withdraw")
            });
    };


    $scope.moveUp = function(ownerPokemonId){
        $http.get(serverUrl + '/party/moveUp?id='+ownerPokemonId).
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: moveUp")
            });
    };

    $scope.moveDown = function(ownerPokemonId){
        $http.get(serverUrl + '/party/moveDown?id='+ownerPokemonId).
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: moveDown")
            });
    };

    $scope.deselectPokemon = function(){
        $scope.selectedPokemon = null;
    };

    $scope.selectPokemon = function(pokemon){
        $scope.selectedPokemon = pokemon;
    };

    $scope.exit = function(){
        $http.get(serverUrl + '/party/exit').
            success(function(data, status, headers, config) {
                $scope.updateView();
            }).
            error(function(data, status, headers, config) {
                alert("error: exit");
            });
    };

});