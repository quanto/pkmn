
var tileStartPointX = -1;
var tileStartPointY = -1;

$(document).ready(function(){

    $("#tileset").click(function(e){

        if (singleSelection){

            var points = getTilePoints(e)

            setTile(translatePos(points))

            tileStartPointX = -1
            tileStartPointY = -1

            $("#tilesetSelection").css('display','none')
        }
        else {
            $("#tilesetSelection").css('display','block')
            tileSetClick(e)
        }
    });

    $("#tilesetSelection").click(function(e){
        if (singleSelection){

            var points = getTilePoints(e)

            setTile(translatePos(points))

            tileStartPointX = -1
            tileStartPointY = -1

            $("#tilesetSelection").css('display','none')
        }
        else {
            $("#tilesetSelection").css('display','block')
            tileSetClick(e)
        }

    });

    $("#tileset").mousemove(function(e){
        if (tileStartPointX != -1){
            var points = getTilePoints(e)
            showTileSelection(tileStartPointY,tileStartPointX,points[1],points[0])
        }
    });

//    $("#tilesetSelection").mousemove(function(e){
//        if (tileStartPointX != -1){
//            var points = getTilePoints(e)
//            showTileSelection(tileStartPointX,tileStartPointY,points[0],points[1])
//        }
//    });
})

function tileSetClick(e){
    var points = getTilePoints(e)

    if (tileStartPointX == -1){
        tileStartPointX = points[0]
        tileStartPointY = points[1]
    }
    else {

        var endPointX = points[0]
        var endPointY = points[1]

        var newPattern = new Array()
        for (var h=tileStartPointY;h<=endPointY;h++)
        {
            newPattern[h - tileStartPointY] = new Array()
            for (var w=tileStartPointX;w<=endPointX;w++)
            {
                var tilePoints = new Array()
                tilePoints[0] = w
                tilePoints[1] = h
                newPattern[h-tileStartPointY][(w-tileStartPointX)*2] = translatePos(tilePoints)
                newPattern[h-tileStartPointY][((w-tileStartPointX)*2)+1] = selectedLayer
            }
        }
        setStamp(newPattern)

        tileStartPointX = -1
        tileStartPointY = -1

    }
}

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
//    if (tileStartPointX == -1){
//        tileStartPointX = x
//        tileStartPointY = y
//    }
    var points = new Array()
    points[0] = x
    points[1] = y
    return points
}

/*
 Shows current selection by applying black borders
 */
function showTileSelection(startPointY,startPointX,y,x)
{
    // Draw white borders

    //$("#fieldWrapper td").css("border","1px solid white");

    // switch if nessecery
//    if (startPointX > x)
//    {
//        var temp = x;
//        x = startPointX;
//        startPointX = temp;
//    }
//
//    if (startPointY > y)
//    {
//        var temp = y;
//        y = startPointY;
//        startPointY = temp;
//    }
    var pos = $("#tileset").position();

    $("#tilesetSelection").css('left',parseInt(pos.left) + (startPointX * 16))
    $("#tilesetSelection").css('top',parseInt(pos.top) + (startPointY * 16))
    $("#tilesetSelection").css('width',(((x + 1) - startPointX) * 16) + 'px');
    $("#tilesetSelection").css('height',(((y + 1) - startPointY) * 16) + 'px');
    /*
    // Draw the border
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
    */
}
