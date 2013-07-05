var freeze = false;
var view = "pokemon";

function actionA(direction)
{

    var currentPos = getObjectPosition("player");
    var actionObject = getActionObject(currentPos,"triggerOnActionButton");
    if (actionObject != null)
    {
        // perform the action of the object
        eval(actionObject[1] + "(currentPos,direction,actionObject);");
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
    var leftPos = parseInt($("#" + objectId).css("left"));
    var topPos = parseInt($("#" + objectId).css("top"));

    topPos += $("#" + objectId).height() - 16

    // tile position
    var left = leftPos / 16;
    var top = topPos / 16;

    return new position(top,left);
}

/*
 Get the a new position in a direction
 */
function getNewPosition($moveObj, pos,direction)
{
    var newPos = new position(pos.y,pos.x);
    if (direction == "up")
    {
        if($moveObj.hasClass("spritely"))
            $moveObj.spState(2);
        newPos.y--;
    }
    else if (direction == "down")
    {
        if($moveObj.hasClass("spritely"))
            $moveObj.spState(1);
        newPos.y++;
    }
    else if (direction == "left")
    {
        if($moveObj.hasClass("spritely"))
            $moveObj.spState(3);
        newPos.x--;
    }
    else if (direction == "right")
    {
        if($moveObj.hasClass("spritely"))
            $moveObj.spState(4);
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
    var pos = getNewPosition($moveObj, currentPos,direction)

    var updateViewAfterAnimation = false

    // Before move event or pokemon event
    if (isPlayer){

        var checkMove = false

        if (positionObjectExists(triggerBeforeStepObjects,pos)){
            checkMove = true
        }
        else if (positionObjectExists(pokemonObjects,pos)){
            if (Math.floor((Math.random()*6)) == 1){
                checkMove = true
            }
        }

        if (checkMove){

            var allowMove = true

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
    if (positionInBoundary(pos,objectId, direction,currentPos))
    {
        if (checkPosition(pos,direction))
        {
            var y = pos.y * 16
            y -= $moveObj.height() - 16
            $moveObj.animate({
                left: pos.x * 16 + "px",
                top: y + "px"
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
function checkPosition(pos,direction)
{
    var actionObject = getActionObject(pos,"triggerBeforeStep");

    if (actionObject != null)
    {
        // perform the action of the object
        return eval(actionObject[1] + "(pos,direction,actionObject);");
    }
    return true;
}

/*
 Remove a action object from the map
 */
function removeActionObject(actionObject)
{
    for (var i=0;i<actionObjects.length;i++)
    {
        if (actionObjects[i] == actionObject)
        {
            actionObjects.splice(i,1);
            $("#" + actionObject[0]).remove();
            break;
        }
    }
}

/*
 Get a object at a position
 */
function getActionObject(pos,triggerType)
{
    // check if there are objects
    for (var i=0;i<actionObjects.length;i++)
    {
        var object = $("#" + actionObjects[i][0]);
        if (object != null)
        {
            var leftPos = parseInt(object.css("left"));
            var topPos = parseInt(object.css("top"));

            if (topPos / 16 == pos.y && leftPos / 16 == pos.x)
            {
                if (triggerType == "triggerBeforeStep"){
                    if (actionObjects[i][3])
                        return actionObjects[i];
                }
                else if (triggerType == "triggerOnActionButton"){
                    if (actionObjects[i][4])
                        return actionObjects[i];
                }

            }
        }
    }
    return null;
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

    // Check if there's now wall blocking
    if (positionObjectExists(blockObjects,pos)){
        if (objectId == "player"){
            actionA(direction);
        }
        return false
    }

    return true;
}

function positionObjectExists(objects,pos){
    for (var i=0;i<objects.length;i++)
    {
        if (objects[i].x == pos.x && objects[i].y == pos.y)
        {
            return true;
        }
    }
    return false;
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
        };

        setTimeout(function(){ freeze = false },300)
    }

});

function loadmap()
{
    var html = "";

    $("#player").css("left",(playerPosition.x * 16) + "px");
    $("#player").css("top",((playerPosition.y * 16) - 16) + "px");

    for (var i=0;i<actionObjects.length;i++)
    {
        var actionObject = actionObjects[i];
        var splitPosition = actionObject[0].split("-");

        html += "<img src=\"" + serverUrl + "/images/tiles/sheet1/" + actionObject[2] + "\" id=\"" + splitPosition[0] + "-" + splitPosition[1] + "\" style=\"position:absolute;top:" + (splitPosition[0] * 16) + "px;left:" + (splitPosition[1] * 16) + "px;\" />";

    }

    $("#objectContainer").html(html);

    // Spritely player
    $('.spritely')
        .sprite({
            fps: 9,
            no_of_frames: 4,
            on_first_frame: function(obj) {

            },
            on_last_frame: function(obj) {
                obj.spStop(true);
            }
        }).spStop(true);

    updateLocation(playerPosition.x, playerPosition.y);
}

function boulder(pos,direction,actionObject)
{
    // Check if next object is not a stone
    var nextActionObject = getActionObject(getNewPosition(null, pos,direction),"triggerBeforeStep");
    if (nextActionObject != null) // && nextActionObject[1] == "stone")
    {
        // next object is a stone, dont move it
        return false;
    }
    // else move the stone
    return move(actionObject[0], direction);
}

function bush(pos,direction,actionObject)
{
    removeActionObject(actionObject)
    // Dont move
    return false;
}

function person(){
    alert('todo:implement')
    // Dont move
    return false;
}

function findItem(pos,direction,actionObject)
{
    removeActionObject(actionObject)
    return true;
}

