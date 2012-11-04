<%--
  Created by IntelliJ IDEA.
  User: kevinverhoef
  Date: 08-10-12
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="game.MapPokemon; game.MapLayout" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="game">
</head>
<body>
    <table>
        <g:each in="${(lowestY-1..highestY+1)}" var="y">
            <tr>
                <g:each in="${(lowestX-1..highestX+1)}" var="x">
                    <%
                        def map = worldMaps.find{ it.worldX == x && it.worldY == y }
                        MapLayout mapLayout
                        if (map){
                            mapLayout = MapLayout.createMapArray(map)
                        }
                    %>
                    <td>
                        <center>
                            <div style="z-index:9999;position:relative;background-color:white;">
                            (${x})-(${y})
                            <g:if test="${map}">
                                <g:link action="editor" id="${map.id}">${mapLayout.map?.name}</g:link>
                                <g:link controller="pokemonEditor" action="edit" id="${map.id}">Pokemon</g:link>
                                <g:link controller="actionEditor" action="actions" id="${map.id}">Actions</g:link>
                                <%
                                    println MapPokemon.countByMap(map)
                                %>
                            </g:if>
                            <g:else>
                                <g:link action="editor" params="${[worldX:x,worldY:y]}">New</g:link>
                            </g:else>
                            </div>
                            <g:if test="${mapLayout}">

                                <g:render template="/game/layerMap" model="${[mapLayout:mapLayout,map:map]}" />
                            </g:if>
                        </center>
                    </td>


                </g:each>
            </tr>
        </g:each>
    </table>


</body>
</html>