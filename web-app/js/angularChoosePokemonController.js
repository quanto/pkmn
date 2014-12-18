app.controller('ChoosePokemonController', function ($scope, $http) {

    $scope.choose = function(){
        if ($scope.pokemonId != undefined){

            $http.get(serverUrl + '/choosePokemon/choose?id='+$scope.pokemonId).
                success(function(data, status, headers, config) {
                    $scope.updateView();
                }).
                error(function(data, status, headers, config) {
                    alert("error: choose")
                });
        }
    };

});