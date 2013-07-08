<%@ page import="game.lock.OneTimeActionLock; game.context.ActionType; game.context.ActionType; game.MapLayout;" %>
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
            def clientActions = map.actions.findAll{ (it.actionType == ActionType.Client || it.actionType == ActionType.Mixed) }
            // remove one time actions
            clientActions.removeAll { it.placeOneTimeActionLock && OneTimeActionLock.findByPlayerAndAction(player,it) }
        %>
        <g:each in="${clientActions}" var="clientAction" status="i">
            {
                id:"${clientAction.positionY}-${clientAction.positionX}",
                y:${clientAction.positionY},
                x:${clientAction.positionX},
                cssClass:"",
                backgroundImage:"${createLink(uri:'')}/images/tiles/sheet1/${clientAction.tileImage}.png",
                triggerBeforeStep:${clientAction.triggerBeforeStep},
                triggerOnActionButton:${clientAction.triggerOnActionButton},
                action:"${clientAction.actionFunction}"
            }
            <g:if test="${clientAction != clientActions.last()}">,</g:if>
        </g:each>
    );

    //actionObjects.push(new Array("9-10","person","/game/images/chars/person1.png",true,false,"actionObject spritely"));
    //actionObjects.push(new Array("player","person","/game/images/chars/person1.png",false, false,"player spritely actionObject"));

    var pokemonObjects = new Array(
        <g:each in="${pokemonObjects}" var="po" status="i">new position(${po[0]},${po[1]})<g:if test="${po != pokemonObjects.last()}">,</g:if>
        </g:each>
    );

    var triggerBeforeStepObjects = new Array(
        <% def triggerBeforeStepActions = map.actions.findAll{ it.triggerBeforeStep && (it.actionType == ActionType.Server || it.actionType == ActionType.Mixed) } %>
        <g:each in="${triggerBeforeStepActions}" var="action" status="i">new position(${action.positionY},${action.positionX})<g:if test="${action != triggerBeforeStepActions.last()}">,</g:if>
        </g:each>
    )

    var triggerOnActionButtonObjects = new Array(
        <% def triggerOnActionButtonActions = map.actions.findAll{ it.triggerOnActionButton && (it.actionType == ActionType.Server || it.actionType == ActionType.Mixed) } %>
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