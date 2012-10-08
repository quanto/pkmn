<%@ page import="game.MapLayout" %>

<table cellpadding="0" cellspacing="0" border="0">

    <g:each in="${mapLayout.background}" var="row" status="y">
        <tr>
        <g:each in="${row}" var="tileNr" status="x">
            <td>
                <div id="${x}-${y}" title="${x}-${y}" style="height:16px; width:16px; background:url('${resource(uri:'')}/images/tiles/sheet1/${tileNr}.png');">
                <%--
                    foreground image
                --%>
                <%
                    String f = mapLayout.foreground[y][x]?.trim()
                %>
                <g:if test="${f && f != "0"}">

                    <img src='${resource(uri:'')}/images/tiles/sheet1/${f}.png' />
                </g:if>
            </div>
            </td>
        </g:each>
        </tr>
    </g:each>
</table>