<%@ page import="game.action.*; game.lock.OneTimeActionLock; game.context.ActionType; game.context.ActionType; game.MapLayout;" %>
<%
    // MapLayout mapLayout
    def blockObjects = []
    def pokemonObjects = []
    mapLayout.foreground.eachWithIndex { def row, def y ->
        row.eachWithIndex { def tileNr, def x ->
            if (tileNr && tileNr != "0"){
                blockObjects.add([y,x])
            }
            else if (tileNr == "0" && mapLayout.background[y][x] == "01"){
                pokemonObjects.add([y,x])
            }
        }
    }
%>
<script type="text/javascript">

    var mapName = "${map.name}";
    var mapId = "${map.id}";

    var height = ${mapLayout.getRows()};
    var width = ${mapLayout.getColumns()};

    var playerPosition = new position(${player.positionY},${player.positionX});

    var actionObjects = new Array(
        <%

            def actions
            if (player.altMap){
                actions = player.altMap.actions.collect{ it }
            }
            else {
                actions = map.actions.collect{ it }
            }

            // remove one time actions
            actions.removeAll { it.placeOneTimeActionLock && OneTimeActionLock.findByPlayerAndAction(player, it) }
        %>
        <g:each in="${actions}" var="action" status="i">
            {
                id:"actionObject${action.id}",
                clientAction:${action.actionType == ActionType.Client || action.actionType == ActionType.Mixed},
                serverAction:${action.actionType == ActionType.Server || action.actionType == ActionType.Mixed},
                y:${action.positionY},
                x:${action.positionX},
                cssClass:"${action.cssClass?:''}",
                <g:if test="${action.image}">
                    backgroundImage:"${createLink(uri:'')}${action.image}",
                </g:if>
                triggerBeforeStep:${action.triggerBeforeStep},
                triggerOnActionButton:${action.triggerOnActionButton},
                action:"${action.actionFunction}",
                <g:if test="${action in CharacterAction && action.macro}">
                    macro:"${action.macro}",
                </g:if>
                <g:if test="${action in MessagePersonAction}">
                    message:"${action.message}",
                </g:if>
                correctionLeft:${action.correctionLeft?:0},
                correctionTop:${action.correctionTop?:0}
            }
            <g:if test="${action != actions.last()}">,</g:if>
        </g:each>
    );

    // Test object
//    actionObjects.push(
//            {
//                id:"actionObject666",
//                y:10,
//                x:10,
//                action:true,
//                clientAction:true,
//                serverAction:false,
//                cssClass:"actionObject spritely overworldPokemon",
//                backgroundImage:"/game/images/followers/51.png",
//                triggerBeforeStep:true,
//                triggerOnActionButton:false,
//                action:"person",
//                correctionLeft:-8,
//                correctionTop:-16,
//                initialDirection:"l",
//                macro:'lr'
//            }
//    );

    var pokemonObjects = new Array(
        <g:each in="${pokemonObjects}" var="po" status="i">new position(${po[0]},${po[1]})<g:if test="${po != pokemonObjects.last()}">,</g:if>
        </g:each>
    );

    var blockObjects = new Array(
        <g:each in="${blockObjects}" var="bo" status="i">new position(${bo[0]},${bo[1]})<g:if test="${bo != blockObjects.last()}">,</g:if>
        </g:each>
    );

</script>

<style>

    #player {
        background: transparent url('${resource(uri:'')}/images/chars/${player.characterImage}.png') 0 0 no-repeat;
        z-index: 2000;
    }

</style>

<div style="position:relative;height:${mapLayout.getRows() * 16}px;width: ${mapLayout.getColumns() * 16}px;">
    <img style="position:absolute;top:0px;left:0px;" src="${resource(uri:'')}${map.getForegroundImage(player.altMap)}" />

    <div id="player" class="player spritely actionObject" correctionTop="-16" correctionLeft="0" />

    <div id="objectContainer">

    </div>
    <img style="position:absolute;top:0px;left:0px;" src="${resource(uri:'')}${map.getBackgroundImage(player.altMap)}" />
</div>

<script type="text/javascript">

    loadmap();
    <g:if test="${flash.direction=='left'}">
        $("#player").spState(3);
    </g:if>
    <g:elseif test="${flash.direction=='up'}">
        $("#player").spState(2);
    </g:elseif>
    <g:elseif test="${flash.direction=='right'}">
        $("#player").spState(4);
    </g:elseif>

</script>