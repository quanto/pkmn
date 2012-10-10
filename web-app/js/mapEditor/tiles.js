
$(document).ready(function(){
    $("#tileset").click(function(e){

        var pos = $(this).position();

        var x = e.pageX - parseInt(pos.left);
        var y = e.pageY - parseInt(pos.top);

        x = Math.floor(x / 16)
        y = Math.floor(y / 16)

        //alert(x +', '+ y);
        setTile(x + "" + y)
    });
})
