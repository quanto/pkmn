var pressTimerUp
var pressTimerDown
var pressTimerLeft
var pressTimerRight

$(document).ready(function() {

    if(is_touch_device()) {

        $("#tableNavigation").css('display','block')


        $("#moveUp").bind('touchstart click', function(){
            $("#player").spState(2);
            move("player","up");
            pressTimerUp = setInterval(function(){
                $("#player").spState(2);
                move("player","up");

            }, 500);
            return false
        });
        $("#moveUp").bind('touchend', function(){
            clearInterval(pressTimerUp)
            return false
        });

        $("#moveDown").bind('touchstart click', function(){
            $("#player").spState(1);
            move("player","down");
            pressTimerDown = setInterval(function(){
                $("#player").spState(1);
                move("player","down");
            }, 500);
            return false
        });
        $("#moveDown").bind('touchend', function(){
            clearInterval(pressTimerDown)
            return false
        });

        $("#moveLeft").bind('touchstart click', function(){
            $("#player").spState(3);
            move("player","left");
            pressTimerLeft = setInterval(function(){
                $("#player").spState(3);
                move("player","left");
            }, 500);
            return false
        });
        $("#moveLeft").bind('touchend', function(){
            clearInterval(pressTimerLeft)
            return false
        });

        $("#moveRight").bind('touchstart click', function(){
            $("#player").spState(4);
            move("player","right");
            pressTimerRight = setInterval(function(){
                $("#player").spState(4);
                move("player","right");
            }, 500);
            return false
        });
        $("#moveRight").bind('touchend', function(){
            clearInterval(pressTimerRight)
            return false
        });
    }
});

function is_touch_device() {
    try {
        document.createEvent("TouchEvent");
        return true;
    } catch (e) {
        return false;
    }
}
