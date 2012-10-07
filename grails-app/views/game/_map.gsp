<%@ page import="game.MapLayout" %>

<table cellpadding="0" cellspacing="0" border="0">

    <g:each in="${mapLayout.background}" var="row" status="y">
        <tr>
        <g:each in="${row}" var="tileNr" status="x">
            <td>
                <div id="${x}-${y}" style="height:16px; width:16px; background:url('${resource(uri:'')}/images/tiles/sheet1/${tileNr}.png');">
                <%--
                    foreground image
                --%>
                <g:if test="${mapLayout.background[x][y]}">
                    <img src='${resource(uri:'')}/images/tiles/sheet1/${mapLayout.foreground[y][x]}.png' />
                </g:if>
            </div>
            </td>
        </g:each>
        </tr>
    </g:each>
</table>