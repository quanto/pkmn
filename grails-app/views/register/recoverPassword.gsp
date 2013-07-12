<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title></title>
        <meta name="layout" content="game"/>
    </head>
    <body>
        <g:form action="recoverPassword">
            <g:hiddenField name="secret" value="${secret}" />
            <table>
                <tr>
                    <td>
                        Password
                    </td>
                    <td>
                        <g:passwordField name="password" />
                    </td>
                </tr>
                <tr>
                    <td>

                    </td>
                    <td>
                        <g:submitButton name="set" />
                    </td>
                </tr>
            </table>
        </g:form>
    </body>
</html>