<%@ page import="game.ClientAction; game.ServerAction; game.MapLayout" %>
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
        <% def clientActions = map.actions.findAll{ it in ClientAction } %>
        <g:each in="${clientActions}" var="clientAction" status="i">new Array("${clientAction.positionX}-${clientAction.positionY}","${clientAction.actionFunction}","32.png")<g:if test="${clientAction != clientActions.last()}">,</g:if>
        </g:each>
    );

    var pokemonObjects = new Array(
        <g:each in="${pokemonObjects}" var="po" status="i">new position(${po[0]},${po[1]})<g:if test="${po != pokemonObjects.last()}">,</g:if>
        </g:each>
    );

    var triggerBeforeStepObjects = new Array(
        <% def triggerBeforeStepActions = map.actions.findAll{ it in ServerAction && it.triggerBeforeStep } %>
        <g:each in="${triggerBeforeStepActions}" var="action" status="i">new position(${action.positionY},${action.positionX})<g:if test="${action != triggerBeforeStepActions.last()}">,</g:if>
        </g:each>
    )

    var triggerOnActionButtonObjects = new Array(
        <% def triggerOnActionButtonActions = map.actions.findAll{ it in ServerAction && it.triggerOnActionButton } %>
        <g:each in="${triggerOnActionButtonActions}" var="action" status="i">new position(${action.positionY},${action.positionX})<g:if test="${action != triggerOnActionButtonActions.last()}">,</g:if>
        </g:each>
    )

    var blockObjects = new Array(
        <g:each in="${blockObjects}" var="bo" status="i">new position(${bo[0]},${bo[1]})<g:if test="${bo != blockObjects.last()}">,</g:if>
        </g:each>
    );

</script>

<style>

#player {
    background: transparent url('${resource(uri:'')}/images/chars/${player.characterImage}.png') 0 0 no-repeat;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 16px;
    height: 32px;
    z-index: 2000;]
}

</style>

<div style="position:relative;height:${mapLayout.getRows() * 16}px;width: ${mapLayout.getColumns() * 16}px;">
    <img style="position:absolute;top:0px;left:0px;" src="${resource(uri:'')}${map.getForegroundImage()}" />

    <div id="player"></div>

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