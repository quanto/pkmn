<%@ page import="game.action.Action; game.MapLayout; game.Map" %>

<table cellpadding="0">

    <g:each in="${mapLayout.background}" var="row" status="y">
        <tr>
            <g:each in="${row}" var="tileNr" status="x">
                <%
                    game.action.Action action = map.actions.find { it.positionX == x && it.positionY == y }
                %>
                <td style="${action?'border:1px solid #444;':'border:1px solid #ccc;'}">
                    <div id="to${x}-${y}" style="height:16px; width:16px; background:url('${resource(uri:'')}/images/tiles/sheet1/${tileNr}.png');" title="${x}-${y}" onclick="toMapPosition(${x},${y})">
                        <g:if test="${mapLayout.background[x][y]}">
                            <g:if test="${mapLayout.foreground[y][x] != '0'}">
                                <img src='${resource(uri:'')}/images/tiles/sheet1/${mapLayout.foreground[y][x]}.png' />
                            </g:if>
                        </g:if>
                    </div>
                </td>
            </g:each>
        </tr>
    </g:each>
</table>