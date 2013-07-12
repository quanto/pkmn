<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name='layout' content='game'/>
</head>
<body>
    <g:form action="createAltMap">
        <table>
            <tr>
                <td>
                    Map id
                </td>
                <td>
                    <g:hiddenField name="altMapId" value="${altMap?.id}" />
                    <g:textField name="mapId" value="${mapId}" />
                </td>
            </tr>
            <tr>
                <td>
                    newDataBackground
                </td>
                <td>
                    <g:checkBox name="newDataBackground" checked="${altMap?.newDataBackground}" value="true" />
                </td>
            </tr>
            <tr>
                <td>
                    newDataForeground
                </td>
                <td>
                    <g:checkBox name="newDataForeground" checked="${altMap?.newDataForeground}" value="true" />
                </td>
            </tr>
            <tr>
                <td>
                    newActions
                </td>
                <td>
                    <g:checkBox name="newActions" checked="${altMap?.newActions}" value="true" />
                </td>
            </tr>
            <tr>
                <td>
                    condition
                </td>
                <td>
                    <g:select noSelection="['':'']" from="${game.Condition.values()}" name="condition" value="${altMap?.condition}" />
                </td>
            </tr>
            <tr>
                <td>
                </td>
                <td>
                    <g:submitButton name="save" value="save" />
                </td>
            </tr>
        </table>
    </g:form>
</body>
</html>