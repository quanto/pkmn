app.controller('PartyController', function ($scope, $http, $filter) {

    $scope.model;
    $scope.selectedPokemon;

    $scope.$on('applicationStarted',function() {
        $scope.updateParty();
    });

    $scope.updateParty = function(){
        $http.get(serverUrl + '/party').
            success(function(data, status, headers, config) {
                $scope.model = $filter('orderBy')(data, 'partyPosition');
            }).
            error(function(data, status, headers, config) {
                alert("error: updateParty")
            });
    };

    $scope.moveUp = function(ownerPokemonId){
        $http.get(serverUrl + '/party/moveUp?id='+ownerPokemonId).
            success(function(data, status, headers, config) {
                $scope.updateParty();
            }).
            error(function(data, status, headers, config) {
                alert("error: moveUp")
            });
    };

    $scope.moveDown = function(ownerPokemonId){
        $http.get(serverUrl + '/party/moveDown?id='+ownerPokemonId).
            success(function(data, status, headers, config) {
                $scope.updateParty();
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

});
