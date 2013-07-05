<%@ page import="game.Action; game.MapTransition; game.MapPokemon" %>
<g:link action="editor" target="_blank" id="${map.id}" style="color:#006600;">${map?.name}</g:link>
<g:link controller="pokemonEditor" target="_blank" style="color:#0000CC;" action="edit" id="${map.id}">Pokemon ${MapPokemon.countByMap(map)}</g:link>
<g:link controller="actionEditor" target="_blank"  style="color:#FF0000;" action="actions" id="${map.id}">Actions ${Action.countByMap(map)}</g:link>
<g:link controller="mapEditor" target="_blank" action="transitions" id="${map.id}">Transitions: ${MapTransition.countByMap(map)}</g:link>