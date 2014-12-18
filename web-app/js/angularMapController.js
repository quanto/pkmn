app.controller('MapController', function ($scope, $http, $timeout) {

    $scope.model;
    $scope.freeze;
    $scope.playerPosition;
    $scope.mapName;
    $scope.mapId;
    $scope.height;
    $scope.width;
    $scope.pokemonObjects;
    $scope.blockObjects;
    $scope.actionObjects;
    $scope.macroTimerStarted = false;

    $scope.$on('initMap',function(event, data) {
        $scope.init(data);
    });

    $scope.init = function(data){
        $scope.model = data;

        $scope.setPlayerDirection();

        $scope.setPlayerImage();

        $scope.playerPosition = new position($scope.model.player.y, $scope.model.player.x);

        $scope.mapName = $scope.model.map.name;
        $scope.mapId = $scope.model.map.id;

        $scope.height = $scope.model.map.rows;
        $scope.width = $scope.model.map.columns;

        $scope.pokemonObjects = _.map($scope.model.pokemonObjects, function(obj){ return new position(obj.y,obj.x); });
        $scope.blockObjects = _.map($scope.model.blockObjects, function(obj){ return new position(obj.y,obj.x); });

        $scope.actionObjects = $scope.model.actionObjects;

        $scope.loadmap();

        if (!$scope.macroTimerStarted){
            $scope.macroTimerStarted = true;
            $scope.macroTimer();
        }

    };

    $scope.$on('mapEvent',function(event, key) {
        if (!$scope.freeze){

            $scope.freeze = true;

            $timeout(function(){ $scope.freeze = false; },300);

            switch (key){

                case 97:
                    $scope.move("player","left");
                    break;
                case 37:
                    $scope.move("player","left");
                    break;
                case 119:
                    $scope.move("player","up");
                    break;
                case 38:
                    $scope.move("player","up");
                    break;
                case 100:
                    $scope.move("player","right");
                    break;
                case 39:
                    $scope.move("player","right");
                    break;
                case 115:
                    $scope.move("player","down");
                    break;
                case 40:
                    $scope.move("player","down");
                    break;
                case 32: //spatie

                    if ($("#textBox").html('') != ''){
                        nextMessage();
                    }
                    else {
                        $scope.actionA("up");
                    }
                    break;
            };

        }
    });

    /*
     Move an object in a direction
     up | down | left | right
     */
    $scope.move = function(objectId, direction){
        var $moveObj = $("#"+objectId);
        var isPlayer = $moveObj.hasClass('player');
        var isSpritely = $moveObj.hasClass('spritely');

        var currentPos = $scope.getObjectPosition(objectId);

        // get new position
        var pos = $scope.getNewPosition($moveObj, currentPos, direction);

        $scope.updateViewAfterAnimation = false;

        // Before move event or pokemon event
        if (isPlayer){

            closeMessage();

            var checkMove = false

            if ($("#objectContainer div[serverAction='true'][triggerBeforeStep='true'][x='" + pos.x + "'][y='" + pos.y + "']").length > 0){
                checkMove = true
            }
            else if (_.find($scope.pokemonObjects, function(objectPos){ return objectPos.x == pos.x && objectPos.y == pos.y; }) != undefined){
                if (Math.floor((Math.random()*6)) == 1){
                    checkMove = true
                }
            }

            if (checkMove){
                var allowMove = true;

                $.ajax({
                    async:false,
                    type: "POST",
                    url: serverUrl + "/game/checkMove?direction=" + direction + "&x=" + parseInt(pos.x) + "&y=" + parseInt(pos.y),
                    data: "direction="+direction,
                    cache: false,
                    success: function(json){

                        $scope.decideAction(json.actions);

                    }
                });
                // Move can be disallowed
                if (!allowMove){
                    return false
                }
            }
        }

        // check inside field
        if ($scope.positionInBoundary(pos, objectId, direction, currentPos))
        {
            if ($scope.checkPosition(pos,direction))
            {
                var y = pos.y * 16;
                //y -= $moveObj.height() - 16;

                // update the location
                var $actionObject = $("#"+objectId);
                $actionObject.attr('x',pos.x);
                $actionObject.attr('y',pos.y);

                $moveObj.animate({
                    left: ((pos.x * 16) + parseInt($actionObject.attr('correctionLeft'))) + "px",
                    top: (y + parseInt($actionObject.attr('correctionTop'))) + "px"
                },{
                    duration: 250,
                    complete: function() {

                        if (isPlayer){
                            $scope.updateLocation($scope.mapName, pos.x, pos.y);

                            if ($scope.updateViewAfterAnimation){
                                $scope.updateView();
                            }
                        }
                    }
                });

                if (isSpritely){
                    $moveObj.spStart();
                }

                return true;
            }
        }

        return false;
    };



    $scope.loadmap = function(){
        $("#objectContainer").html("");

        $("#player").css("left",($scope.playerPosition.x * 16) + "px");
        $("#player").css("top",(($scope.playerPosition.y * 16) - 16) + "px");

        $("#player").attr("x", $scope.playerPosition.x);
        $("#player").attr("y", $scope.playerPosition.y);

        for (var i=0;i<$scope.actionObjects.length;i++){

            var actionObject = $scope.actionObjects[i];

            var $actionObject = $("<div class='actionObject " + actionObject.cssClass + "' />");

            if (actionObject.backgroundImage != undefined){
                $actionObject.css('background-image',"url('" + serverUrl + actionObject.backgroundImage + "')");
                $actionObject.css('position',"absolute");
                $actionObject.css('top',((actionObject.y * 16) + actionObject.correctionTop) + "px");
                $actionObject.css('left',((actionObject.x * 16)  + actionObject.correctionLeft) + "px");
            }

            for (var key in actionObject) {
                if (actionObject.hasOwnProperty(key) && key != 'cssClass' && key != 'backgroundImage' ) {
                    $actionObject.attr(key,actionObject[key])
                }
            }

            if (actionObject.macro != undefined){
                $actionObject.attr('macroStep',0);
            }

            $("#objectContainer").append($actionObject);
        }

        // Spritely objects
        $.each($('.spritely'), function(index, value) {
            $(value).sprite({
                fps: 9,
                no_of_frames: 4,
                on_first_frame: function(obj) {

                },
                on_last_frame: function(obj) {
                    obj.spStop(true);
                }
            }).spStop(true);


            var initialDirection = $(value).attr('initialDirection');
            if (initialDirection == 'u')
            {
                $(value).spState(2);
            }
            else if (initialDirection == 'd')
            {
                $(value).spState(1);

            }
            else if (initialDirection == 'l')
            {
                $(value).spState(3);
            }
            else if (initialDirection == 'r')
            {
                $(value).spState(4);
            }

        });

        $scope.updateLocation($scope.mapName, $scope.playerPosition.x, $scope.playerPosition.y);
    };

    $scope.updateLocation = function(mapName, x, y)
    {
        $("#mapName").text(mapName);
        $("#location").text("X: "+x+"Y: "+y);
    }

    $scope.setPlayerDirection = function(){
        if ($scope.model.player.direction == 'left'){
            $("#player").spState(3);
        }
        else if ($scope.model.player.direction == 'up'){
            $("#player").spState(2);
        }
        else if ($scope.model.player.direction == 'right'){
            $("#player").spState(4);
        }

    };

    $scope.setPlayerImage = function(){
        $("#player").css("background","transparent url('" + serverUrl + "/images/chars/" + $scope.model.player.characterImage + ".png') 0 0 no-repeat");
    };

    /*
     Check if position falls out from the boundary
     */
    $scope.positionInBoundary = function(pos, objectId, direction, currentPos){

        // Boundary's
        if (pos.y < 0 || pos.y >= $scope.height || pos.x < 0 || pos.x >= $scope.width)
        {
            if (objectId == "player"){
                $scope.actionA(direction);
            }
            return false;
        }

        // Check if there's a wall blocking
        if (_.find($scope.blockObjects, function(objectPos){ return objectPos.x == pos.x && objectPos.y == pos.y; }) != undefined){

            if (objectId == "player"){
                $scope.actionA(direction);
            }
            return false
        }

        return true;
    }

    $scope.actionA = function(direction){

        var currentPos = $scope.getObjectPosition("player");

        var $actionObject = $("#objectContainer div[clientAction='true'][triggerOnActionButton='true'][x='" + currentPos.x + "'][y='" + currentPos.y + "']")
        if ($actionObject.length > 0){
            // perform the action of the object
            return $scope[$actionObject.attr('action')](pos,direction,$actionObject);
//            eval($actionObject.attr('action') + "(currentPos,direction,$actionObject);");
        }

        $.ajax({
            async : false,
            type: "GET",
            url: serverUrl + "/game/action?direction=" + direction + "&x=" + parseInt(currentPos.x) + "&y=" + parseInt(currentPos.y),
            data: "",
            cache: false,
            success: function(json){
                $scope.decideAction(json.actions);
            }
        });
    };

    $scope.decideAction = function(actions){
        _.each(actions, function(value, action){

            if (action == "DisallowMove"){
                allowMove = false;
            }
            else if (action == "AllowMove"){
                allowMove = true;
            }
            else if (action == "Message"){
                setMessage(value);
            }
            else if (action == "UpdateView"){
                $scope.updateView();
            }
            else if (action == "UpdateViewAfterAnimation"){
                $scope.updateViewAfterAnimation = true;
            }
            else {
                alert("Unknown action: " + action)
            }

        });
    }

    /*
     Get the position from a object by its id
     */
    $scope.getObjectPosition = function(objectId){

        // position
        $obj = $("#" + objectId);

        // tile position
        var left = parseInt($obj.attr("x"));
        var top = parseInt($obj.attr("y"));

        return new position(top,left);
    }

    /*
     Check a position on action objects
     Evals a action if it exists
     */
    $scope.checkPosition = function(pos, direction){

        var $actionObject = $("#objectContainer div[clientAction='true'][triggerBeforeStep='true'][x='" + pos.x + "'][y='" + pos.y + "']")

        if ($actionObject.length > 0)
        {
            // perform the action of the object
            return $scope[$actionObject.attr('action')](pos,direction,$actionObject);

//            return eval($actionObject.attr('action') + "");
        }
        // Check if the player is on the location
        else if (parseInt($('#player').attr('x')) == pos.x && parseInt($('#player').attr('y')) == pos.y){
            return false;
        }
        return true;
    }

    /*
     Remove a action object from the map
     */
    $scope.removeActionObject = function($actionObject)
    {
        $actionObject.remove();
    }

    /*
     Get the a new position in a direction
     */
    $scope.getNewPosition = function($moveObj, pos, direction)
    {
        var newPos = new position(pos.y,pos.x);
        if (direction == "up" || direction == 'u')
        {
            if($moveObj && $moveObj.hasClass("spritely")){
                $moveObj.spState(2);
            }
            newPos.y--;
        }
        else if (direction == "down" || direction == 'd')
        {
            if($moveObj && $moveObj.hasClass("spritely")){
                $moveObj.spState(1);
            }
            newPos.y++;
        }
        else if (direction == "left" || direction == 'l')
        {
            if($moveObj && $moveObj.hasClass("spritely")){
                $moveObj.spState(3);
            }
            newPos.x--;
        }
        else if (direction == "right" || direction == 'r')
        {
            if($moveObj && $moveObj.hasClass("spritely")){
                $moveObj.spState(4);
            }
            newPos.x++;
        }
        return newPos;
    };

    $scope.macroTimer = function(){
        $actionObjects = $("#objectContainer div[clientAction='true'][macro]");

        $.each($actionObjects, function(index, actionObject) {
            var $actionObject = $(actionObject);
            var macroStep = parseInt($actionObject.attr('macroStep'));
            var macro = $actionObject.attr('macro');
            var macroLength = macro.length;
            macroStep += 1;
            if (macroStep >= macroLength){
                macroStep = 0;
            }

            var macroChar = macro[macroStep];

            var pos = new position(parseInt($actionObject.attr('y')), parseInt($actionObject.attr('x')));
            var success = $scope[$actionObject.attr('action') + "Macro"](pos, macroChar, $actionObject);

            if (success){
                $actionObject.attr('macroStep', macroStep);
            }
        });
        // Loop
        $timeout(function(){ $scope.macroTimer(); }, 1000);
    }

    $scope.boulder = function(pos, direction, $actionObject){
        // Check if next object is not a stone
        var newPos = $scope.getNewPosition(null, pos, direction);
        var $nextActionObject = $("#objectContainer div[clientAction='true'][triggerBeforeStep='true'][x='" + newPos.x + "'][y='" + newPos.y + "']");

        if ($nextActionObject.length > 0){ // && nextActionObject[1] == "stone")
            // next object is a stone, dont move it
            return false;
        }

        // else move the stone
        return $scope.move($actionObject.attr('id'), direction);
    }

    $scope.bush = function(pos, direction, $actionObject){
        $scope.removeActionObject($actionObject);
        // Dont move
        return false;
    }

    $scope.person = function(pos, direction, $actionObject){
        return false;
        //return move($actionObject.attr('id'), direction);
    }

    $scope.messagePerson = function(pos, direction, $actionObject){
        setMessage($actionObject.attr('message'));
        return false;
    }

    $scope.personMacro = function(pos, macroChar, $actionObject){
        return $scope.move($actionObject.attr('id'), macroChar);
    }

    $scope.messagePersonMacro = function(pos, macroChar, $actionObject){
        return $scope.move($actionObject.attr('id'), macroChar);
    }

    $scope.findItem = function(pos, direction, $actionObject){
        $scope.removeActionObject($actionObject);
        return true;
    }

    $scope.npc = function(pos, direction, $actionObject){
        return false;
    }

});