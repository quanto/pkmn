var freeze = false;
var view = "pokemon";

function actionA(direction)
{

    var currentPos = getObjectPosition("player");

    var $actionObject = $("#objectContainer div[clientAction='true'][triggerOnActionButton='true'][x='" + currentPos.x + "'][y='" + currentPos.y + "']")
    if ($actionObject.length > 0){
        // perform the action of the object
        eval($actionObject.attr('action') + "(currentPos,direction,$actionObject);");
    }

    $.ajax({
        async : false,
        type: "GET",
        url: serverUrl + "/game/action?direction=" + direction + "&x=" + parseInt(currentPos.x) + "&y=" + parseInt(currentPos.y),
        data: "",
        cache: false,
        success: function(msg){
            eval(msg);
        }
    });
};


/*
 Object holding a position
 */
function position(y,x)
{
    this.y = y;
    this.x = x;
}

/*
 Get the position from a object by its id
 */
function getObjectPosition(objectId)
{
    // position
    $obj = $("#" + objectId);

    // tile position
    var left = parseInt($obj.attr("x"));
    var top = parseInt($obj.attr("y"));

    return new position(top,left);
}

/*
 Get the a new position in a direction
 */
function getNewPosition($moveObj, pos, direction)
{
    var newPos = new position(pos.y,pos.x);
    if (direction == "up" || direction == 'u')
    {
        if($moveObj.hasClass("spritely")){
            $moveObj.spState(2);
        }
        newPos.y--;
    }
    else if (direction == "down" || direction == 'd')
    {
        if($moveObj.hasClass("spritely")){
            $moveObj.spState(1);
        }
        newPos.y++;
    }
    else if (direction == "left" || direction == 'l')
    {
        if($moveObj.hasClass("spritely")){
            $moveObj.spState(3);
        }
        newPos.x--;
    }
    else if (direction == "right" || direction == 'r')
    {
        if($moveObj.hasClass("spritely")){
            $moveObj.spState(4);
        }
        newPos.x++;
    }
    return newPos;
}

/*
 Move an object in a direction
 up | down | left | right
 */
function move(objectId, direction)
{
    var $moveObj = $("#"+objectId);
    var isPlayer = $moveObj.hasClass('player');
    var isSpritely = $moveObj.hasClass('spritely');

    var currentPos = getObjectPosition(objectId);

    // get new position
    var pos = getNewPosition($moveObj, currentPos, direction);

    var updateViewAfterAnimation = false

    // Before move event or pokemon event
    if (isPlayer){

        closeMessage();

        var checkMove = false

        if ($("#objectContainer div[serverAction='true'][triggerBeforeStep='true'][x='" + pos.x + "'][y='" + pos.y + "']").length > 0){
            checkMove = true
        }
        else if (_.find(pokemonObjects, function(objectPos){ return objectPos.x == pos.x && objectPos.y == pos.y; }) != undefined){
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
                success: function(msg){
                    // An empty message means walking against the wall for now
                    if(msg != "")
                    {
                        eval(msg);
                    }

                }
            });
            // Move can be disallowed
            if (!allowMove){
                return false
            }
        }
    }

    // check inside field
    if (positionInBoundary(pos, objectId, direction, currentPos))
    {
        if (checkPosition(pos,direction))
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
                        updateLocation(pos.x, pos.y);

                        if (updateViewAfterAnimation){
                            getView();
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
}

/*
 Check a position on action objects
 Evals a action if it exists
 */
function checkPosition(pos, direction){

    var $actionObject = $("#objectContainer div[clientAction='true'][triggerBeforeStep='true'][x='" + pos.x + "'][y='" + pos.y + "']")

    if ($actionObject.length > 0)
    {
        // perform the action of the object
        return eval($actionObject.attr('action') + "(pos,direction,$actionObject);");
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
function removeActionObject($actionObject)
{
    $actionObject.remove();
}

/*
 Check if position falls out from the boundary
 */
function positionInBoundary(pos, objectId, direction, currentPos)
{
    // Boundary's
    if (pos.y < 0 || pos.y >= height || pos.x < 0 || pos.x >= width)
    {
        if (objectId == "player"){
            actionA(direction);
        }
        return false;
    }

    // Check if there's a wall blocking
    if (_.find(blockObjects, function(objectPos){ return objectPos.x == pos.x && objectPos.y == pos.y; }) != undefined){
    //if (positionObjectExists(blockObjects,pos)){
        if (objectId == "player"){
            actionA(direction);
        }
        return false
    }

    return true;
}

/*
 Key actions
 */
$(document).keypress(function(e)
{
    if (!freeze)
    {
        freeze = true
        switch (e.which)
        {
            case 97:
                move("player","left");
                break;
            case 119:
                move("player","up");
                break;
            case 100:
                move("player","right");
                break;
            case 115:
                move("player","down");
                break;
            case 32:
                //spatie
                actionA("up");
                break;
            case 13:
                sendChatMessage();
                break;
        };

        setTimeout(function(){ freeze = false },300)
    }

});

function loadmap()
{
    $("#player").css("left",(playerPosition.x * 16) + "px");
    $("#player").css("top",((playerPosition.y * 16) - 16) + "px");

    $("#player").attr("x", playerPosition.x);
    $("#player").attr("y", playerPosition.y);

    for (var i=0;i<actionObjects.length;i++)
    {
        var actionObject = actionObjects[i];

        var $actionObject = $("<div class='actionObject " + actionObject.cssClass + "' />");

        if (actionObject.backgroundImage != undefined){
            $actionObject.css('background-image',"url('" + actionObject.backgroundImage + "')");
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

    setInterval(macroTimer,1000);

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
    });

    updateLocation(playerPosition.x, playerPosition.y);
}

function macroTimer(){
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

        var pos = new position(parseInt($actionObject.attr('y')), parseInt($actionObject.attr('x')))
        var success = eval($actionObject.attr('action') + "Macro(pos, macroChar, $actionObject);");

        if (success){
            $actionObject.attr('macroStep', macroStep);
        }
    });
}

function boulder(pos, direction, $actionObject)
{
    // Check if next object is not a stone

    var newPos = getNewPosition(null, pos, direction);
    var $nextActionObject = $("#objectContainer div[clientAction='true'][triggerBeforeStep='true'][x='" + newPos.x + "'][y='" + newPos.y + "']");

    if ($nextActionObject != undefined) // && nextActionObject[1] == "stone")
    {
        // next object is a stone, dont move it
        return false;
    }
    // else move the stone
    return move($actionObject.attr('id'), direction);
}

function bush(pos, direction, $actionObject)
{
    removeActionObject($actionObject)
    // Dont move
    return false;
}

function person(pos, direction, $actionObject){
    return false;
    //return move($actionObject.attr('id'), direction);
}

function messagePerson(pos, direction, $actionObject){
    setMessage($actionObject.attr('message'));
    return false;
}

function personMacro(pos, macroChar, $actionObject){
    return move($actionObject.attr('id'), macroChar);
}

function messagePersonMacro(pos, macroChar, $actionObject){
    return move($actionObject.attr('id'), macroChar);
}

function findItem(pos, direction, $actionObject)
{
    removeActionObject($actionObject);
    return true;
}

