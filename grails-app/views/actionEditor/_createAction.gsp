<%@ page import="game.MapLayout; game.Map" %>
Create action
<g:form action="createAction">
    <g:hiddenField name="map.id" value="${map.id}" />
    <g:hiddenField name="positionX" value="${positionX}" />
    <g:hiddenField name="positionY" value="${positionY}" />
    <g:select name="actionType" onchange="actionTypeChanged(this)" noSelection="['':'-Choose Action-']" from="${['MapTransition', 'ComputerAction', 'MapMessage', 'MarketAction', 'RecoverAction']}" />
    <div id="MapMessage" style="display: none">
        <g:textField name="message" />
    </div>
    <div id="MapTransition" style="display: none">
        <g:select name="mapId" from="${game.Map.list()}" optionKey="${ { it.id } }" onchange="selectMap(this)" noSelection="['':'-Choose Map-']" />
    </div>


    <g:submitButton name="create" />
</g:form>
<div id="mapAjax">


</div>