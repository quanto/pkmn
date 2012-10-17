
var tileStartPointX = -1;
var tileStartPointY = -1;

$(document).ready(function(){



    $("#tileset").click(function(e){

        var points = getTilePoints(e)

        setTile(translatePos(points))

    });

    $("#tileset").mouseover(function(e){
        if (tileStartPointX > 0){

        }
    });
})

function translatePos(points){

    var x = points[0] % 8
    var y = points[1]

    if (points[0] > 15){
        y = y + 30
    }
    else if (points[0] > 7){
        y = y + 15
    }
    return x + "" + y
}

function getTilePoints(e){
    var pos = $("#tileset").position();

    var x = e.pageX - parseInt(pos.left);
    var y = e.pageY - parseInt(pos.top);

    x = Math.floor(x / 16)
    y = Math.floor(y / 16)

    //setTile(x + "" + y)
    if (tileStartPointX == -1){
        tileStartPointX = x
        tileStartPointY = y
    }
    var points = new Array()
    points[0] = x
    points[1] = y
    return points
}
/*
function mouseOverTilesEvent(y,x)
{
    // Only on mouse down
    if (tileStartPointX > -1)
    {
        showTileSelection(tileStartPointY,tileStartPointX,y,x);
    }
}

/*
 Shows current selection by applying black borders
 */
function showTileSelection(startPointY,startPointX,y,x)
{
    // Draw white borders

    $("#fieldWrapper td").css("border","1px solid white");

    // show step borders
    showStepSelection();

    // switch if nessecery
    if (startPointX > x)
    {
        var temp = x;
        x = startPointX;
        startPointX = temp;
    }

    if (startPointY > y)
    {
        var temp = y;
        y = startPointY;
        startPointY = temp;
    }

    // Draw border
    for (var h=startPointY;h<=y;h++)
    {
        for (var w=startPointX;w<=x;w++)
        {
            if (w < width && h < height)
            {
                //$(h + "-" + w).css("border","1px solid black");
                document.getElementById(h + "-" + w).style.border = "1px solid black";
            }
        }
    }

}
*/