app.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});

app.filter('reverse', function() {
    return function(items) {
        return items.slice().reverse();
    };
});

app.directive('keypressEvents', [
    '$document',
    '$rootScope',
    function($document, $rootScope) {
        return {
            restrict: 'A',
            link: function() {
                $document.bind('keypress', function(e) {
                    $rootScope.$broadcast('keypress', e);
                    $rootScope.$broadcast('keypress:' + e.which, e);
                });
            }
        };
    }
]);