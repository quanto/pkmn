<%@ page import="game.action.MapTransition; game.action.Action; game.MapPokemon" %>
<g:link action="editor" target="_blank" id="${map.id}" style="color:#006600;">${map?.name}</g:link>
<g:link controller="pokemonEditor" target="_blank" style="color:#0000CC;" action="edit" id="${map.id}">Pokemon ${MapPokemon.countByMap(map)}</g:link>
<g:link controller="actionEditor" target="_blank"  style="color:#FF0000;" action="actions" id="${map.id}">Actions ${game.action.Action.countByMap(map)}</g:link>
<g:link controller="mapEditor" target="_blank" action="transitions" id="${map.id}">Transitions: ${game.action.MapTransition.countByMap(map)}</g:link>
<g:link controller="mapEditor" target="_blank" action="altMap" id="${map.id}">AltMap</g:link>
<g:link action="place" target="_blank" id="${map.id}">Place</g:link>
<g:each in="${map.altMaps}" var="altMap">
    <br/>

    <g:link controller="mapEditor" target="_blank" action="altMap" id="${map.id}" params="${[altMapId:altMap.id]}">AltMap ${altMap.id}</g:link>
    <g:link action="editor" target="_blank" id="${map.id}" params="${[altMapId:altMap.id]}" style="color:#006600;">Editor</g:link>
    <g:link controller="actionEditor" action="actions" target="_blank" id="${map.id}" params="${[altMapId:altMap.id]}">Actions</g:link>
</g:each>
