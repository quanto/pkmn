
// Main start
$(document).ready( function () {

    $("#accordion").accordion({
        heightStyle: "content"
    });

    //Initial load
    var scope = angular.element($("#ngApp")).scope();
    scope.$broadcast('applicationStarted', []);

});

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
