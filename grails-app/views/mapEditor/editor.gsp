<%@ page import="game.MapLayout" %>
<html>
<head>
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery-1.7.min.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/mapEditor/sets.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/mapEditor/editor.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/mapEditor/tabs.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/mapEditor/tiles.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/mapEditor/data.js"></script>
    <script type="text/javascript">

        $(document).ready( function () {

            field = new Array();

            // background layer
            field[0] = new Array();
            // foreground
            field[1] = new Array();
            // Object/Action layer
            field[2] = new Array();
            // Top layer
            field[3] = new Array();

            setSize(${mapLayout.getRows()},${mapLayout.getColumns()});

            <g:each in="${mapLayout.background}" var="row" status="y">
                field[0][${y}] = new Array();
                <g:each in="${row}" var="tileNr" status="x">
                    field[0][${y}][${x}] = "${tileNr}"
                </g:each>
            </g:each>

            drawField();
        });
    </script>
    <style>

    body
    {
        font-family: verdana;
    }

    table
    {
        font-size: 11px;
    }

    #tileWrapper img
    {
        border:1px solid white;
    }

    table#fieldWrapper td
    {
        border:1px solid white;
        font-size:8px;
    }

    a:link, a:visited
    {

        border:0;
    }

    img
    {
        border:0;
    }

    a:link, a:visited
    {
        color:black;
    }
    </style>
</head>
<body>

<table>
    <tr>
        <td valign="top">
            <table cellspacing='0' cellpadding='0' id="fieldWrapper" style='display:inline;'>
            </table>
        </td>
        <td valign="top">
            <input type="image" src="${resource(uri:'')}/images/mapEditor/undo.png" value="undo" onclick="undo()" />
            <img id="selectedTile" src="${resource(uri:'')}/images/mapEditor/empty.gif" height="32" width="32" />
            <br />

            <div id="tabWrapper">
            </div>

            <div id="tab5" title="Background">
                <input type="button" value="Grass" onclick="action = 'random';" />
            </div>

            <div id="tab6" title="Foreground">
                <input type="button" value="pattern" onclick="action = 'pattern';" />
            </div>

            <div id="tab3" title="Tiles">
                <br />Selection<br />
                <input type="button" value="multiple" onclick="toggleSelection(this)" />
                <br />Layer<br />
                <input type="button" value="foreground" onclick="toggleLayer(this)" />
                <p>

                    <input type="button" value="prev" onclick="loadNextTiles(-1);" />
                    <input type="button" value="backward" onclick="loadNextTiles(-45);" />

                <div id="tileWrapper"></div>

                <input type="button" value="next" onclick="loadNextTiles(1);" />
                <input type="button" value="forward" onclick="loadNextTiles(45);" />

            </div>

            <div id="tab4" title="Mapsize">
                <input type="text" value="25" id="fieldWidth" />
                <input type="text" value="25" id="fieldHeight" />
                <input type="button" value="Set" onclick="setSizeAction()" />
            </div>

            <div id="tab1" title="Paths">
                <input type="button" value="fill path" onclick="fillPath();" />
                <img src="${resource(uri:'')}/images/mapEditor/path1.png" onclick='setPath(new Array(new Array("08","18","28"),new Array("09","19","29"),new Array("010","110","210"),new Array("38","48","39","49")))' />
                <img src="${resource(uri:'')}/images/mapEditor/path2.png" onclick='setPath(new Array(new Array("59","69","79"),new Array("510","610","710"),new Array("511","611","711"), new Array("310","410","311","411")))' />
                <img src="${resource(uri:'')}/images/mapEditor/path3.png" onclick='setPath(new Array(new Array("014","114","214"),new Array("015","115","215"), new Array("016","116","216"),new Array("315","415","316","416")))' />
                <img src="${resource(uri:'')}/images/mapEditor/path4.png" onclick='setPath(new Array(new Array("526","626","726"),new Array("527","627","727"), new Array("528","628","728"),new Array("629","729","630","730") ))' />
                <img src="${resource(uri:'')}/images/mapEditor/path5.png" onclick='setPath(new Array(new Array("02","12","22"),new Array("03","13","23"),new Array("04","14","24"),new Array("34","44","35","45")))' />
                <img src="${resource(uri:'')}/images/mapEditor/path6.png" onclick='setPath(new Array(new Array("05","15","25"), new Array("06","16","26"), new Array("07","17","27"),new Array("36","46","37","47")))' />

                <br />
                <input type="button" value="stones path 1" onclick="setPath(stonepath1)" />
                <input type="button" value="stones path 2" onclick="setPath(stonepath2)" />
            </div>

            <div id="tab2" title="Objects">
                <br />Trees<br />
                <input type="button" value="forest tree" onclick="setStamp(foresttree)" />
                <input type="button" value="big tree" onclick="setStamp(bigtree)" />
                <input type="button" value="small tree" onclick="setStamp(smalltree)" />

                <br />Houses<br />
                <input type="button" value="mart" onclick="setStamp(mart)" />
                <input type="button" value="pc" onclick="setStamp(pc)" />
                <input type="button" value="small house" onclick="setStamp(smallhouse)" />
                <input type="button" value="gym" onclick="setStamp(gym)" />
                <input type="button" value="brown house" onclick="setStamp(brownhouse)" />

                <br />Chars<br />
                <input type="button" value="char1" onclick="setStamp(char1)" />
                <input type="button" value="char2" onclick="setStamp(char2)" />
                <input type="button" value="char3" onclick="setStamp(char3)" />
                <input type="button" value="char4" onclick="setStamp(char4)" />
                <input type="button" value="char5" onclick="setStamp(char5)" />
                <input type="button" value="char6" onclick="setStamp(char6)" />
                <input type="button" value="char7" onclick="setStamp(char7)" />
                <input type="button" value="char8" onclick="setStamp(char8)" />

            </div>

            <div id="tab7" title="Other">
                <br />
                <input type="button" value="mapdata" onclick="mapdata()" />
                <input type="button" value="gamedata" onclick="gamedata()" />
                <br />
                <textarea style="width:300px;height:200px;" id="mapdata"></textarea>
            </div>

        </td>
    </tr>
</table>

</body>

<script language="javascript">

    //init();
    initTabs("tab");
    // Select first tab
    tabClick("tab", 2);
    loadNextTiles(0);

</script>
</html>