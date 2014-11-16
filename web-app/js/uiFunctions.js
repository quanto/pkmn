
// Main start
$(document).ready( function () {

    $("#accordion").accordion({
        heightStyle: "content"
    });

    //Initial load
    //getMap(); staat in de game samen met startbattle();
    getView();

    var scope = angular.element($("#ngApp")).scope();
    scope.$broadcast('applicationStarted', []);

});

function updateLocation(x, y)
{
    $("#mapName").text(mapName);
    $("#location").text("X: "+x+"Y: "+y);
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
