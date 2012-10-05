
                

var tileStep = 0;

function loadNextTiles(steps)
{
    tileStep += steps;
    if (tileStep > 369)
        tileStep = 369;
    if (tileStep < 0)
        tileStep = 0;
    loadTiles(tileStep,10);
}

// 0, 407 rows
function loadTiles(startrow,rows)
{
    //startrow = startrow * 3;
    //var total = 360;
    var total = rows * 24;

    var i=0;
    var html = "";
    for(var i=0;i<total;i++)
    {
        var x = i % 8;
        
        var row = Math.floor(i / 24);
        var cell = Math.floor((i % 24) / 8);
        var y = row + (cell * 15) + startrow;
        
        html += "<a href='#' onclick='return setTile(" + x + y + ")'><img height='16' width='16' src='/game/images/tiles/sheet1/" + x + y + ".png' /></a>";

        if (i % 24 == 23)
        {
            html += "<br>";
        }
    }
    
    $("#tileWrapper").html(html);
}