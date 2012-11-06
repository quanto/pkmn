
var tileStartPointX = -1;
var tileStartPointY = -1;

$(document).ready(function(){

    $("#tileset").click(function(e){

        if (singleSelection){

            var points = getTilePoints(e, "tileset")

            setTile(translatePos(points))

            tileStartPointX = -1
            tileStartPointY = -1

            $("#tilesetSelection").css('display','none')
        }
        else {
            $("#tilesetSelection").css('display','block')
            tileSetClick(e,"tileset","")
        }
    });

    $("#tilesetSelection").click(function(e){
        if (singleSelection){

            var points = getTilePoints(e, "tileset")

            setTile(translatePos(points))

            tileStartPointX = -1
            tileStartPointY = -1

            $("#tilesetSelection").css('display','none')
        }
        else {
            $("#tilesetSelection").css('display','block')
            tileSetClick(e,"tileset","")
        }

    });

    $("#tileset").mousemove(function(e){
        if (tileStartPointX != -1){
            var points = getTilePoints(e, "tileset")
            showTileSelection(tileStartPointY,tileStartPointX,points[1],points[0], "tilesetSelection","tileset2")
        }
    });

//    $("#tilesetSelection").mousemove(function(e){
//        if (tileStartPointX != -1){
//            var points = getTilePoints(e)
//            showTileSelection(tileStartPointX,tileStartPointY,points[0],points[1])
//        }
//    });


    $("#tileset2").click(function(e){

        if (singleSelection){

            var points = getTilePoints(e, "tileset2")

            setTile("a" + translatePos2(points))

            tileStartPointX = -1
            tileStartPointY = -1

            $("#tilesetSelection2").css('display','none')
        }
        else {
            $("#tilesetSelection2").css('display','block')
            tileSetClick(e,"tileset2","a")
        }
    });

    $("#tilesetSelection2").click(function(e){
        if (singleSelection){

            var points = getTilePoints(e, "tileset2")

            setTile(translatePos(points))

            tileStartPointX = -1
            tileStartPointY = -1

            $("#tilesetSelection2").css('display','none')
        }
        else {
            $("#tilesetSelection2").css('display','block')
            tileSetClick(e,"tileset2","a")
        }

    });

    $("#tileset2").mousemove(function(e){
        if (tileStartPointX != -1){
            var points = getTilePoints(e, "tileset2")
            showTileSelection(tileStartPointY,tileStartPointX,points[1],points[0],"tilesetSelection2", "tileset2")
        }
    });

})

function tileSetClick(e, tilesetId, prefix){
    var points = getTilePoints(e, tilesetId)

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
                newPattern[h-tileStartPointY][(w-tileStartPointX)*2] = prefix + "" + translatePos(tilePoints)
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

function translatePos2(points){
    var x = parseInt(points[0])
    var y = parseInt(points[1]) * 8

    return x + y
}

function getTilePoints(e, tilesetId){
    var pos = $("#" + tilesetId).position();

    var x = e.pageX - parseInt(pos.left);
    var y = e.pageY - parseInt(pos.top);

    x = Math.floor(x / 16)
    y = Math.floor(y / 16)

    var points = new Array()
    points[0] = x
    points[1] = y
    return points
}

/*
 Shows current selection by applying black borders
 */
function showTileSelection(startPointY,startPointX,y,x, selectionId, tilesetId)
{
    var pos = $("#" + tilesetId).position();

    $("#" + selectionId).css('left',parseInt(pos.left) + (startPointX * 16))
    $("#" + selectionId).css('top',parseInt(pos.top) + (startPointY * 16))
    $("#" + selectionId).css('width',(((x + 1) - startPointX) * 16) + 'px');
    $("#" + selectionId).css('height',(((y + 1) - startPointY) * 16) + 'px');
}
