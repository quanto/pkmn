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

    var height = ${mapLayout.getRows()};
    var width = ${mapLayout.getColumns()};

    var playerPosition = new position(${player.positionY},${player.positionX});

    var actionObjects = new Array(
        <%
            def clientActions = map.actions.collect{ it }
            // remove one time actions
            clientActions.removeAll { it.placeOneTimeActionLock && OneTimeActionLock.findByPlayerAndAction(player, it) }
        %>
        <g:each in="${clientActions}" var="clientAction" status="i">
            {
                id:"actionObject${clientAction.id}",
                clientAction:${clientAction.actionType == ActionType.Client || clientAction.actionType == ActionType.Mixed},
                serverAction:${clientAction.actionType == ActionType.Server || clientAction.actionType == ActionType.Mixed},
                y:${clientAction.positionY},
                x:${clientAction.positionX},
                cssClass:"${clientAction.cssClass?:''}",
                <g:if test="${clientAction.image}">
                    backgroundImage:"${createLink(uri:'')}${clientAction.image}",
                </g:if>
                triggerBeforeStep:${clientAction.triggerBeforeStep},
                triggerOnActionButton:${clientAction.triggerOnActionButton},
                action:"${clientAction.actionFunction}",
                <g:if test="${clientAction in PersonAction && clientAction.macro}">
                    macro:"${clientAction.macro}",
                </g:if>
                correctionLeft:${clientAction.correctionLeft?:0},
                correctionTop:${clientAction.correctionTop?:0}
            }
            <g:if test="${clientAction != clientActions.last()}">,</g:if>
        </g:each>
    );

//    // Test object
//    actionObjects.push(
//            {
//                id:"actionObject3",
//                y:10,
//                x:10,
//                clientAction:true,
//                serverAction:false,
//                cssClass:"actionObject spritely",
//                backgroundImage:"/game/images/chars/person1.png",
//                triggerBeforeStep:true,
//                triggerOnActionButton:false,
//                action:"person",
//                correctionLeft:0,
//                correctionTop:-16,
//                macro:'llrr'
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
    <img style="position:absolute;top:0px;left:0px;" src="${resource(uri:'')}${map.getForegroundImage()}" />

    <div id="player" class="player spritely actionObject" />

    <div id="objectContainer">

    </div>
    <img style="position:absolute;top:0px;left:0px;" src="${resource(uri:'')}${map.getBackgroundImage()}" />
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