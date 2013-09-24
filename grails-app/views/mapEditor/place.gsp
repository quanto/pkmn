<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name='layout' content='game'/>
    </head>
    <body>

    <div id="actionMap">

        <table cellpadding="0">

            <g:each in="${mapLayout.background}" var="row" status="y">
                <tr>
                    <g:each in="${row}" var="tileNr" status="x">

                        <td style="border:1px solid #ccc;">
                            <div id="current${x}-${y}" style="height:16px; width:16px; background:url('${resource(uri:'')}/images/tiles/sheet1/${tileNr}.png');" title="${x}-${y}">
                                <g:if test="${mapLayout.background[y][x]}">
                                    <g:link action="placeMe" params="${[x:x,y:y,mapId:map.id]}">
                                        <g:if test="${mapLayout.foreground[y][x] != '0'}">
                                            <img src='${resource(uri:'')}/images/tiles/sheet1/${mapLayout.foreground[y][x]}.png' />
                                        </g:if>
                                        <g:else>
                                            <img src='${resource(uri:'')}/images/mapEditor/empty.gif' />
                                        </g:else>
                                    </g:link>
                                </g:if>
                            </div>
                        </td>
                    </g:each>
                </tr>
            </g:each>
        </table>

    </div>

    </body>
</html>