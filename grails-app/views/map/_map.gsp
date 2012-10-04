<%@ page import="game.MapLayout" %>

<table cellpadding="0" cellspacing="0" border="0">

    <g:each in="${mapLayout.background}" var="row" status="x">
        <tr>
        <g:each in="${row}" var="tileNr" status="y">
            <td>
                <div id="${x}-${y}" style="height:16px; width:16px; background:url('${resource(uri:'')}/images/tiles/sheet1/${tileNr}.png');">
                <%--
                    foreground image
                --%>
            </div>
            </td>
        </g:each>
        </tr>
    </g:each>
</table>