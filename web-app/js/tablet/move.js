var pressTimerUp
var pressTimerDown
var pressTimerLeft
var pressTimerRight

$(document).ready(function() {

    if(is_touch_device()) {

        $("#tableNavigation").css('display','block')


        $("#moveUp").bind('touchstart click', function(){
            movePlayer(0);
            pressTimerUp = setInterval(function(){ movePlayer(0); }, 500);
            return false
        });
        $("#moveUp").bind('touchend', function(){
            clearInterval(pressTimerUp)
            return false
        });

        $("#moveDown").bind('touchstart click', function(){
            movePlayer(2);
            pressTimerDown = setInterval(function(){ movePlayer(2); }, 500);
            return false
        });
        $("#moveDown").bind('touchend', function(){
            clearInterval(pressTimerDown)
            return false
        });

        $("#moveLeft").bind('touchstart click', function(){
            movePlayer(3);
            pressTimerLeft = setInterval(function(){ movePlayer(3); }, 500);
            return false
        });
        $("#moveLeft").bind('touchend', function(){
            clearInterval(pressTimerLeft)
            return false
        });

        $("#moveRight").bind('touchstart click', function(){
            movePlayer(1);
            pressTimerRight = setInterval(function(){ movePlayer(1); }, 500);
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
