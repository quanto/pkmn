
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

//function createDialog(name, content)
//{
//    if($('#'+name+'Dialog').get(0) == undefined)
//    {
//        $("body").append("<div id=\""+name+"Dialog\" title=\""+name+"\">"+content+"</div>");
//    }
//
//    $('#'+name+'Dialog').dialog({autoOpen: false});
//    $('#'+name+'Dialog').dialog('open');
//}

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

var messages = {};
function setMessage(msg){
    messages = $.trim(msg).split(";");
    nextMessage();
}

function nextMessage(){

    if (messages.length > 0){
        $("#textBox").html("<a href='javascript:nextMessage()'>X</a> " + messages[0]);
        messages = messages.splice(1,messages.length);
    }
    else {
        closeMessage();
    }
}
function closeMessage(){
    $("#textBox").html('');
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