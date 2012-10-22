var freeze = false;
var chat = false;
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
	disconnect();
	serverMessage();
	//getParty();
	getMenu();
	
//	setInterval ("setOtherPlayers()", 2000); // Update andere spelers elke 2 sec
	setInterval ("mediumWait()", 8000); //Update chat elke 8 sec
//	setInterval ("longWait()", 60000); //Kijk of server down is en serverbericht 60 Seconden
//
	$("#chatMessage").focus(function () {
         freeze = true;
		 chat = true;
    });

	$("#chatMessage").blur(function () {
         freeze = false;
		 chat = false;
    });
//
	$(document).keypress(function(e){
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
		if(freeze == false)
			{
			switch (e.which){
				case 97:
				//links
				movePlayer(3);
				break;

				case 119:
				//boven
				movePlayer(0);
				break;

				case 100:
				//rechts
				movePlayer(1);
				break;

				case 115:
				//onder
				movePlayer(2);
				break;

				case 32:
				//spatie
				actionA();
				break;
			}
		};
//
		if(e.which == 13)
		{
			sendChatMessage();
		}
	});
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
			url: "/game/choosePokemon/choose",
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

function getQuests()
{
//	$.ajax({
//		type: "GET",
//		url: "Gajax.php",
//		data: "ajax=getQuests",
//		cache: false,
//		success: function(quests){
//			//$("#whois").html(quests);
//			createDialog("Quests", quests);
//		}
//	});
}

function getView()
{
	freeze = true;
	$("#textBox").text("");
	$.ajax({
		type: "GET",
		url: "/game/game/view",
		data: "",
		cache: false,
		success: function(view){
			$("#theMap").html(view);
			getPlayerLocation();
			getMenu();
			freeze = false;
		}
	});		
}

function getParty()
{
	$.ajax({
		type: "GET",
		url: "/game/party/index",
		cache: false,
		success: function(party){
            $("#party").html(party)
		}
	});
}

function serverMessage()
{
//	$.ajax({
//		type: "GET",
//		url: "Gajax.php",
//		data: "ajax=servermessage",
//		cache: false,
//		success: function(message){
//			$("#serverMessage").html(message);
//		}
//	});
};

function disconnect()
{
//	$.ajax({
//		type: "GET",
//		url: "Gajax.php",
//		data: "ajax=disconnect",
//		cache: false,
//		success: function(msg){
//			if(msg == 1)
//			{
//				alert("You have been disconnected from the server");
//				window.location="index.php";
//			}
//		}
//	});
};


function checkBattle()
{
	$.ajax({
        async:false,
		type: "GET",
		url: "/game/game/checkBattle",
		data: "",
		cache: false,
		success: function(battleFrame){
			if(battleFrame != "")
			{
				$("#theMap").html(battleFrame);
			}
		}
	});
};

function continueBattle()
{
//	$.ajax({
//		type: "GET",
//		url: "Gajax.php",
//		data: "ajax=continueBattle",
//		cache: false,
//		success: function(battleFrame){
//			$("#theMap").html(battleFrame);
//		}
//	});
}

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
		url: "/game/admin/showNewsItems",
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
		url: "/game/online/index",
		data: "",
		cache: false,
		success: function(whoisList){
			$("#online").html(whoisList);
			//createDialog("Online", whoisList);
		}
	});
};

function updateChat()
{
	$.ajax({
		type: "GET",
		url: "/game/chat/index",
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
			url: "/game/chat/send",
			data: "chatMessage="+message,
			success: function(chatMessages){
				updateChat();
			}
		});
	}
};

function actionA()
{
	freeze = true;
	$.ajax({
        async : true,
		type: "GET",
		url: "/game/game/action",
		data: "",
		cache: false,
		success: function(msg){

			msg = $.trim(msg);
			if(msg == "refreshMap")
			{
				getView();
			}
			else if(msg == "showMarket")
			{
				getView();
			}
			else if(msg == "showComputer")
			{
				getView();
			}
			else if(msg != "")
			{
				$("#textBox").text(msg);
				freeze = false;
			}
			else
			{
				freeze = false;
			}
//
//			getMenu();

			//freeze = false;
		}
	});
    freeze = false;
};

function updateStats(){
    $.ajax({
        type: "GET",
        url: "/game/stats/index",
        data: "",
        cache: false,
        success: function(data){
            $("#stats").html(data);
        }
    });
}

function updateItems(){
    $.ajax({
        type: "GET",
        url: "/game/market/getItems",
        data: "",
        cache: false,
        success: function(data){
            $("#items").html(data);
        }
    });
}


function getPlayerLocation()
{
	$.ajax({
		type: "GET",
		url: "/game/game/playerLocation",
		data: "",
		cache: false,
		success: function(json){
			var playerData = $.evalJSON(json);
			$("#mapName").text(playerData.player[0].map);
			setPlayer(playerData.player[0].x, playerData.player[0].y, playerData.player[0].name);
			$("#location").text("X: "+playerData.player[0].x+" Y: "+playerData.player[0].y);
		}
	});
};

function movePlayer(direction)
{
    if (!freeze){
        pos = $("#player").attr("alt");

        coords = pos.split("-");

        x = coords[0];
        y = coords[1];

        checkMove(direction, x, y);
    }

};

function checkMove(direction, x, y)
{	
	freeze = true;
	$.ajax({
        async:false,
		type: "POST",
		url: "/game/game/checkMove",
		data: "direction="+direction,
		cache: false,
		success: function(msg){
			if(msg == "1")
			{
				$("#player").remove();
				switch (direction)
				{
					case 0:
					y--;
					break;

					case 1:
					x++;
					break;

					case 2:
					y++;
					break;

					case 3:
					x--;
					break;
				}

				setPlayer(x, y);
				updateLocation(x, y);
				checkBattle();
			}
			else
			{
				actionA();
			}

		}
	});
    freeze = false
};

function setOtherPlayers()
{	
//	$.ajax({
//		type: "GET",
//		url: "Gajax.php",
//		data: "ajax=5",
//		cache: false,
//		success: function(msg){
//			if(msg != 0)
//			{
//				var players = $.evalJSON(msg);
//
//				for(i=0;i<players.player.length;i++)
//				{
//					$("#other").remove();
//				}
//
//				for(i=0;i<players.player.length;i++)
//				{
//					setOtherPlayer(players.player[i].x,players.player[i].y, players.player[i].name);
//				}
//			}
//			else
//			{
//				$("#other").remove();
//			}
//		}
//	});
};
function setPlayer(x, y, name)
{
	$("#"+x+"-"+y).append("<img id='player' class='"+name+"' src='/game/images/player/down.png' alt='"+x+"-"+y+"' />");
};

function setOtherPlayer(x, y, name)
{
	$("#"+x+"-"+y).append("<img id='other' src='images/ash.gif' onmouseover='Tip(\""+name+"\")' onmouseout='UnTip()' />");
}

function updateLocation(x, y)
{
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