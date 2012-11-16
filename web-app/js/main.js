var freeze = false;
var view = "pokemon";

$(document).ready( function () {

    $("#accordion").accordion({
        heightStyle: "content"
    });

	/*
	if (typeof window.loadFirebugConsole == "undefined") {
	}
	else
	{
		$("#firebug").html("You have Firebug running, this will slow down the game. Please disable it for optimal performance");
	}
	*/
	
//	$.ajax({
//		type: "POST",
//		url: "Gajax.php",
//		data: "ajax=7",
//		cache: false,
//		success: function(msg){
//		}
//	});
	
	//Initial load
	//getMap(); staat in de game samen met startbattle();
	getView();
	updateChat();
    updateStats();
    updateItems();
	updateWhoisList();
    loadNewsItems();
//	disconnect();
//	serverMessage();
	getParty();
//	getMenu();
	
//	setInterval ("setOtherPlayers()", 2000); // Update andere spelers elke 2 sec
	setInterval ("mediumWait()", 8000); //Update chat elke 8 sec
//	setInterval ("longWait()", 60000); //Kijk of server down is en serverbericht 60 Seconden
//
//	$("#chatMessage").focus(function () {
//         freeze = true;
//		 chat = true;
//    });
//
//	$("#chatMessage").blur(function () {
//         freeze = false;
//		 chat = false;
//    });
////
//	$(document).keypress(function(e){
//		if(chat == false)
//		{
//			switch (e.which)
//			{
//				case 113: //q
//				loadLink('quests');
//				break;
//
//				case 111: //o
//				loadLink('online');
//				break;
//
//				case 112: //p
//				loadLink('pokemon');
//				break;
//
//				case 110: //n
//				loadLink('news');
//				break;
//			}
//		}
//
//		if(freeze == false)
//			{
//			switch (e.which){
//				case 97:
//				//links
//				movePlayer(3);
//				break;
//
//				case 119:
//				//boven
//				movePlayer(0);
//				break;
//
//				case 100:
//				//rechts
//				movePlayer(1);
//				break;
//
//				case 115:
//				//onder
//				movePlayer(2);
//				break;
//
//				case 32:
//				//spatie
//				actionA();
//				break;
//			}
//		};
////
//		if(e.which == 13)
//		{
//			sendChatMessage();
//		}
//	});
});

function getMenu()
{
	if(view == "online")
	{
		updateWhoisList();
	}
	else if(view == "pokemon")
	{
		getParty();
	}
	else if(view == "news")
	{
		loadNewsItems();
	}
	else if(view == "quests")
	{
		getQuests();
	}
}

function loadLink(menuItem)
{
	view = menuItem;
	if(menuItem == "online")
	{
		updateWhoisList();
	}
	else if(menuItem == "pokemon")
	{
		getParty();
	}
	else if(menuItem == "news")
	{
		loadNewsItems();
	}
	else if(menuItem == "quests")
	{
		getQuests();
	}
}

function choose()
{
	pkmn = $("input[name='pokemon']:checked").val();

	if(pkmn != "undefined" && pkmn != undefined)
	{
		$.ajax({
			type: "GET",
			url: serverUrl + "/choosePokemon/choose",
			data: "pkmn="+pkmn,
			cache: false,
			success: function(msg){
				getView();
			}
		});
	}
	else
	{
		alert("Choose a Pokemon");
	}
}

function getNews()
{
//	$.ajax({
//		type: "GET",
//		url: "Gajax.php",
//		data: "ajax=getNews",
//		cache: false,
//		success: function(news){
//			createDialog("News", news);
//
//		}
//	});
}

function createDialog(name, content)
{
	if($('#'+name+'Dialog').get(0) == undefined)
	{
		$("body").append("<div id=\""+name+"Dialog\" title=\""+name+"\">"+content+"</div>");
	}

	$('#'+name+'Dialog').dialog({autoOpen: false});
	$('#'+name+'Dialog').dialog('open');
}

function getView()
{

    if ($('#player').length>0){
        $('#player').destroy();
    }
	$("#textBox").text("");

	$.ajax({
		type: "GET",
		url: serverUrl + "/game/view",
		data: "",
		cache: false,
		success: function(view){
			$("#theMap").html(view);
			//getPlayerLocation();
			getMenu();
		}
	});		
}

function getParty()
{
	$.ajax({
		type: "GET",
		url: serverUrl + "/party/index",
		cache: false,
		success: function(party){
            $("#party").html(party)
		}
	});
}

function checkBattle()
{
    var hasBattle = false;
	$.ajax({
        async:false,
		type: "GET",
		url: serverUrl + "/game/checkBattle",
		data: "",
		cache: false,
		success: function(battleFrame){
			if(battleFrame != "")
			{
                hasBattle = true;
				$("#theMap").html(battleFrame);
			}
		}
	});
    return hasBattle;
};

function mediumWait()
{
	updateChat();	
};

function longWait()
{
	disconnect();
	serverMessage();	
};

function loadNewsItems()
{
	$.ajax({
		type: "GET",
		url: serverUrl + "/chat/showNewsItems",
		data: "",
		cache: false,
		success: function(newsItems){
            $("#news").html(newsItems);
		}
	});
};

function updateWhoisList()
{
	$.ajax({
		type: "GET",
		url: serverUrl + "/online/index",
		data: "",
		cache: false,
		success: function(whoisList){
			$("#online").html(whoisList);
		}
	});
};

function updateChat()
{
	$.ajax({
		type: "GET",
		url: serverUrl + "/chat/index",
		data: "",
		cache: false,
		success: function(chatMessages){
			$("#chatBox").html(chatMessages);
			el = document.getElementById("chatBox");
			el.scrollTop = el.scrollHeight;
		}
	});
};

function sendChatMessage()
{
	message = $("#chatMessage").attr("value");
	if(message != "")
	{
		$("#chatMessage").attr({value: ""});
		$.ajax({
			type: "POST",
			url: serverUrl + "/chat/send",
			data: "chatMessage="+message,
			success: function(chatMessages){
				updateChat();
			}
		});
	}
};

function actionA(direction)
{
    var currentPos = getObjectPosition("player");

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

function setMessage(msg){
    msg = $.trim(msg);
    $("#textBox").html(msg);
}

function updateStats(){
    $.ajax({
        type: "GET",
        url: serverUrl + "/stats/index",
        data: "",
        cache: false,
        success: function(data){
            $("#stats").html(data);
        }
    });
}

var itemTab = "usableItems"

function updateItems(){
    $.ajax({
        type: "GET",
        url: serverUrl + "/bag/getItems?itemTab=" + itemTab,
        data: "",
        cache: false,
        success: function(data){
            $("#items").html(data);
        }
    });
}

function updateLocation(x, y)
{
    $("#mapName").text(mapName);
	$("#location").text("X: "+x+"Y: "+y);
}

/**
 * Info on your party pokemon.
 */
function getPokemonData(obj){
    $.ajax({
        type: "GET",
        url: $(obj).attr('href'),
        data: "",
        cache: false,
        success: function(data){
            $("#party").html(data);
        }
    });
    return false
}

$(window).unload( function () { 

//	$.ajax({
//		type: "POST",
//		url: "Gajax.php",
//		data: "ajax=1",
//		cache: false,
//		success: function(msg){
//		}
//	});

});

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
function getNewPosition(pos,direction)
{
    var newPos = new position(pos.y,pos.x);
    if (direction == "up")
    {
        newPos.y--;
    }
    else if (direction == "down")
    {
        newPos.y++;
    }
    else if (direction == "left")
    {
        newPos.x--;
    }
    else if (direction == "right")
    {
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
    var currentPos = getObjectPosition(objectId);

    // get new position
    var pos = getNewPosition(currentPos,direction)


    var updateViewAfterAnimation = false

    // Before move event or pokemon event
    if (objectId == "player"){

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
            y -= $("#" + objectId).height() - 16
            $("#" + objectId).animate({
                left: pos.x * 16 + "px",
                top: y + "px"
            },{
                duration: 250,
                complete: function() {
                    if (objectId == "player"){
                        updateLocation(pos.x, pos.y);
                        var src = $("#player").attr("src");
                        if (updateViewAfterAnimation){
                            getView();
                        }
                    }
                }
            });

            if (objectId == "player"){
                $("#" + objectId).spStart();
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
    var actionObject = getActionObject(pos);

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
function getActionObject(pos)
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
                return actionObjects[i];
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
    console.log(freeze)
    if (!freeze)
    {
        freeze = true
        switch (e.which)
        {
            case 97:
                $("#player").spState(3);
                move("player","left");
                break;

            case 119:
                $("#player").spState(2);
                move("player","up");
                break;

            case 100:
                $("#player").spState(4);
                move("player","right");
                break;

            case 115:
                //onder
                $("#player").spState(1);
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
    $('#player')
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
    var nextActionObject = getActionObject(getNewPosition(pos,direction));
    if (nextActionObject != null) // && nextActionObject[1] == "stone")
    {
        // next object is a stone, dont move it
        return false;
    }
    // else move the stone
    return move(actionObject[0], direction);
}
